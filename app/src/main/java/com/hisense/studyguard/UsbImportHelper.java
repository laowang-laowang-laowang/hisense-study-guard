package com.hisense.studyguard;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UsbImportHelper {
    private static final String TAG = "UsbImportHelper";
    public static final String JSON_FILE_NAME = "study_questions.json";
    public static final String CSV_FILE_NAME = "study_questions.csv";

    public static class ImportResult {
        public boolean success;
        public int count;
        public String message;
        public List<QuestionItem> items = new ArrayList<>();
    }

    /**
     * 自动扫描电视连接的所有存储设备（包括 U 盘各分区与外部存储）
     */
    public static ImportResult scanAndImportFromUsb(Context context) {
        ImportResult result = new ImportResult();
        List<File> targetFiles = new ArrayList<>();

        // 1. 尝试通过系统标准接口获取所有外置存储路径（包括插上的 U 盘）
        File[] extDirs = context.getExternalFilesDirs(null);
        if (extDirs != null) {
            for (File dir : extDirs) {
                if (dir != null) {
                    // 向上逐级寻找根挂载目录
                    File root = getRootStorageDir(dir);
                    checkTargetFilesInDir(root, targetFiles);
                }
            }
        }

        // 2. 检查常见 Android TV / 海信电视的常见挂载目录
        String[] potentialPaths = new String[]{
                "/storage",
                "/mnt/usb",
                "/mnt/sdcard",
                "/mnt/media_rw",
                Environment.getExternalStorageDirectory().getAbsolutePath()
        };

        for (String path : potentialPaths) {
            File folder = new File(path);
            if (folder.exists() && folder.isDirectory()) {
                checkTargetFilesInDir(folder, targetFiles);
                File[] subFiles = folder.listFiles();
                if (subFiles != null) {
                    for (File sub : subFiles) {
                        if (sub.isDirectory()) {
                            checkTargetFilesInDir(sub, targetFiles);
                        }
                    }
                }
            }
        }

        if (targetFiles.isEmpty()) {
            result.success = false;
            result.message = "未在 U 盘根目录检测到题库文件！\n请将 " + JSON_FILE_NAME + " 或 " + CSV_FILE_NAME + " 放入 U 盘根目录后重试。";
            return result;
        }

        // 优先读取找到的第一个合法文件
        File target = targetFiles.get(0);
        Log.i(TAG, "找到 U 盘题库文件: " + target.getAbsolutePath());

        try {
            if (target.getName().toLowerCase().endsWith(".json")) {
                List<QuestionItem> list = parseJsonFile(target);
                result.success = true;
                result.count = list.size();
                result.items = list;
                result.message = "成功从 U 盘 (" + target.getName() + ") 导入 " + list.size() + " 道题目！";
            } else if (target.getName().toLowerCase().endsWith(".csv")) {
                List<QuestionItem> list = parseCsvFile(target);
                result.success = true;
                result.count = list.size();
                result.items = list;
                result.message = "成功从 U 盘表格 (" + target.getName() + ") 导入 " + list.size() + " 道题目！";
            }
        } catch (Exception e) {
            result.success = false;
            result.message = "解析 U 盘题库文件失败：" + e.getMessage();
        }

        return result;
    }

    private static File getRootStorageDir(File file) {
        File parent = file;
        while (parent != null && parent.getParentFile() != null) {
            if (parent.getParentFile().getAbsolutePath().equals("/storage") ||
                parent.getParentFile().getAbsolutePath().equals("/mnt")) {
                return parent;
            }
            parent = parent.getParentFile();
        }
        return file;
    }

    private static void checkTargetFilesInDir(File dir, List<File> list) {
        if (dir == null || !dir.exists() || !dir.isDirectory()) return;
        File jsonFile = new File(dir, JSON_FILE_NAME);
        if (jsonFile.exists() && jsonFile.isFile() && !list.contains(jsonFile)) {
            list.add(jsonFile);
        }
        File csvFile = new File(dir, CSV_FILE_NAME);
        if (csvFile.exists() && csvFile.isFile() && !list.contains(csvFile)) {
            list.add(csvFile);
        }
    }

    private static List<QuestionItem> parseJsonFile(File file) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();

        JSONArray arr = new JSONArray(sb.toString());
        List<QuestionItem> items = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            items.add(QuestionItem.fromJsonObject(arr.getJSONObject(i)));
        }
        return items;
    }

    /**
     * CSV 表格格式解析：
     * 分类,题目,选项A,选项B,选项C,选项D,正确选项(A/B/C/D)
     * 例如：
     * POETRY,床前明月光作者是谁？,李白,杜甫,白居易,苏轼,A
     */
    private static List<QuestionItem> parseCsvFile(File file) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
        List<QuestionItem> items = new ArrayList<>();
        String line;
        int row = 0;
        while ((line = reader.readLine()) != null) {
            row++;
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) continue; // 跳过空行或注释
            if (row == 1 && (line.contains("分类") || line.contains("题目"))) {
                continue; // 跳过表头
            }

            String[] cols = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            if (cols.length >= 7) {
                String category = cols[0].trim().toUpperCase();
                String question = cols[1].trim().replace("\"", "");
                String optA = cols[2].trim().replace("\"", "");
                String optB = cols[3].trim().replace("\"", "");
                String optC = cols[4].trim().replace("\"", "");
                String optD = cols[5].trim().replace("\"", "");
                String answerStr = cols[6].trim().toUpperCase();

                int correctIndex = 0;
                if ("B".equals(answerStr) || "1".equals(answerStr)) correctIndex = 1;
                else if ("C".equals(answerStr) || "2".equals(answerStr)) correctIndex = 2;
                else if ("D".equals(answerStr) || "3".equals(answerStr)) correctIndex = 3;

                QuestionItem item = new QuestionItem(
                        "usb_" + System.currentTimeMillis() + "_" + row,
                        category,
                        question,
                        new String[]{optA, optB, optC, optD},
                        correctIndex
                );
                items.add(item);
            }
        }
        reader.close();
        return items;
    }
}
