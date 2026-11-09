package com.hisense.studyguard;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class QuestionRepository {
    private static final String TAG = "QuestionRepository";
    private static final String LOCAL_SAVED_FILE = "saved_questions.json";
    private static QuestionRepository instance;

    private Context context;
    private List<QuestionItem> allQuestions = new ArrayList<>();
    private Random random = new Random();

    private QuestionRepository(Context context) {
        this.context = context.getApplicationContext();
        loadAllQuestions();
    }

    public static synchronized QuestionRepository getInstance(Context context) {
        if (instance == null) {
            instance = new QuestionRepository(context);
        }
        return instance;
    }

    private void loadAllQuestions() {
        allQuestions.clear();
        File localFile = new File(context.getFilesDir(), LOCAL_SAVED_FILE);
        if (localFile.exists()) {
            try {
                allQuestions = readQuestionsFromFile(localFile);
                Log.i(TAG, "从本地私有存储读取到 " + allQuestions.size() + " 道题目");
            } catch (Exception e) {
                Log.e(TAG, "读取本地私有存储题库失败，将加载 assets 内置题库", e);
            }
        }

        // 如果本地文件为空或不存在，从 assets 内置题库加载
        if (allQuestions.isEmpty()) {
            try {
                InputStream is = context.getAssets().open("default_questions.json");
                allQuestions = readQuestionsFromStream(is);
                saveQuestionsToLocal(allQuestions);
                Log.i(TAG, "首次启动：从 assets 加载内置题库 " + allQuestions.size() + " 道");
            } catch (Exception e) {
                Log.e(TAG, "加载 assets 内置题库失败", e);
            }
        }
    }

    public synchronized void importQuestions(List<QuestionItem> newItems) {
        if (newItems == null || newItems.isEmpty()) return;

        Map<String, QuestionItem> questionMap = new HashMap<>();
        for (QuestionItem q : allQuestions) {
            questionMap.put(q.question.trim(), q);
        }
        for (QuestionItem q : newItems) {
            questionMap.put(q.question.trim(), q); // 去重更新
        }

        allQuestions = new ArrayList<>(questionMap.values());
        saveQuestionsToLocal(allQuestions);
        Log.i(TAG, "题库合并更新成功，当前总题量: " + allQuestions.size());
    }

    public synchronized Map<String, Integer> getCategoryCountMap() {
        Map<String, Integer> map = new HashMap<>();
        map.put(QuestionItem.CATEGORY_POETRY, 0);
        map.put(QuestionItem.CATEGORY_MATH, 0);
        map.put(QuestionItem.CATEGORY_ENGLISH, 0);
        map.put(QuestionItem.CATEGORY_ENCYCLOPEDIA, 0);
        map.put(QuestionItem.CATEGORY_GEOGRAPHY, 0);

        for (QuestionItem q : allQuestions) {
            String cat = q.category.toUpperCase();
            if (map.containsKey(cat)) {
                map.put(cat, map.get(cat) + 1);
            }
        }
        return map;
    }

    /**
     * 依据配置的题库开关及数量配额加权随机抽取一道题
     */
    public synchronized QuestionItem drawRandomQuestion(ConfigManager config) {
        List<QuestionItem> pool = new ArrayList<>();
        Map<String, List<QuestionItem>> grouped = new HashMap<>();

        grouped.put(QuestionItem.CATEGORY_POETRY, new ArrayList<>());
        grouped.put(QuestionItem.CATEGORY_MATH, new ArrayList<>());
        grouped.put(QuestionItem.CATEGORY_ENGLISH, new ArrayList<>());
        grouped.put(QuestionItem.CATEGORY_ENCYCLOPEDIA, new ArrayList<>());
        grouped.put(QuestionItem.CATEGORY_GEOGRAPHY, new ArrayList<>());

        for (QuestionItem item : allQuestions) {
            String cat = item.category.toUpperCase();
            if (grouped.containsKey(cat)) {
                grouped.get(cat).add(item);
            }
        }

        // 根据开关和自定义配额填入抽题池
        for (String cat : grouped.keySet()) {
            if (config.isCategoryEnabled(cat)) {
                List<QuestionItem> list = grouped.get(cat);
                if (list != null && !list.isEmpty()) {
                    Collections.shuffle(list);
                    int take = Math.min(list.size(), config.getCategoryQuota(cat));
                    pool.addAll(list.subList(0, take));
                }
            }
        }

        if (pool.isEmpty()) {
            // 保底兜底题目，防止配置导致池空
            if (!allQuestions.isEmpty()) {
                return allQuestions.get(random.nextInt(allQuestions.size()));
            }
            return new QuestionItem(
                    "fallback",
                    QuestionItem.CATEGORY_MATH,
                    "计算：25 + 75 = ?",
                    new String[]{"A. 90", "B. 100", "C. 110", "D. 120"},
                    1
            );
        }

        return pool.get(random.nextInt(pool.size()));
    }

    private void saveQuestionsToLocal(List<QuestionItem> list) {
        try {
            JSONArray arr = new JSONArray();
            for (QuestionItem item : list) {
                arr.put(item.toJsonObject());
            }
            File file = new File(context.getFilesDir(), LOCAL_SAVED_FILE);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(arr.toString(2).getBytes(StandardCharsets.UTF_8));
            fos.close();
        } catch (Exception e) {
            Log.e(TAG, "写入本地题库缓存失败", e);
        }
    }

    private List<QuestionItem> readQuestionsFromFile(File file) throws Exception {
        return readQuestionsFromStream(new FileInputStream(file));
    }

    private List<QuestionItem> readQuestionsFromStream(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        JSONArray arr = new JSONArray(sb.toString());
        List<QuestionItem> list = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            list.add(QuestionItem.fromJsonObject(arr.getJSONObject(i)));
        }
        return list;
    }
}
