package com.hisense.studyguard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

public class SettingsActivity extends Activity {
    private ConfigManager config;
    private TextView tvPlayMinutes, tvErrorMinutes;
    private TextView tvPoetryCount, tvMathCount, tvEnglishCount, tvEncyclopediaCount, tvGeographyCount;
    private CheckBox cbPoetry, cbMath, cbEnglish, cbEncyclopedia, cbGeography;
    private Button btnImportUsb, btnStartGuard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        config = new ConfigManager(this);
        initViews();
        loadDataToUi();
    }

    private void initViews() {
        tvPlayMinutes = findViewById(R.id.tv_play_minutes);
        tvErrorMinutes = findViewById(R.id.tv_error_minutes);

        findViewById(R.id.btn_play_minus).setOnClickListener(v -> {
            int current = config.getPlayAccumulateMinutes();
            if (current > 5) {
                config.setPlayAccumulateMinutes(current - 5);
                loadDataToUi();
            }
        });
        findViewById(R.id.btn_play_plus).setOnClickListener(v -> {
            int current = config.getPlayAccumulateMinutes();
            config.setPlayAccumulateMinutes(current + 5);
            loadDataToUi();
        });

        findViewById(R.id.btn_error_minus).setOnClickListener(v -> {
            int current = config.getErrorPenaltyMinutes();
            if (current > 1) {
                config.setErrorPenaltyMinutes(current - 1);
                loadDataToUi();
            }
        });
        findViewById(R.id.btn_error_plus).setOnClickListener(v -> {
            int current = config.getErrorPenaltyMinutes();
            config.setErrorPenaltyMinutes(current + 1);
            loadDataToUi();
        });

        cbPoetry = findViewById(R.id.cb_poetry);
        cbMath = findViewById(R.id.cb_math);
        cbEnglish = findViewById(R.id.cb_english);
        cbEncyclopedia = findViewById(R.id.cb_encyclopedia);
        cbGeography = findViewById(R.id.cb_geography);

        tvPoetryCount = findViewById(R.id.tv_poetry_count);
        tvMathCount = findViewById(R.id.tv_math_count);
        tvEnglishCount = findViewById(R.id.tv_english_count);
        tvEncyclopediaCount = findViewById(R.id.tv_encyclopedia_count);
        tvGeographyCount = findViewById(R.id.tv_geography_count);

        cbPoetry.setOnCheckedChangeListener((b, isChecked) -> config.setCategoryEnabled(QuestionItem.CATEGORY_POETRY, isChecked));
        cbMath.setOnCheckedChangeListener((b, isChecked) -> config.setCategoryEnabled(QuestionItem.CATEGORY_MATH, isChecked));
        cbEnglish.setOnCheckedChangeListener((b, isChecked) -> config.setCategoryEnabled(QuestionItem.CATEGORY_ENGLISH, isChecked));
        cbEncyclopedia.setOnCheckedChangeListener((b, isChecked) -> config.setCategoryEnabled(QuestionItem.CATEGORY_ENCYCLOPEDIA, isChecked));
        cbGeography.setOnCheckedChangeListener((b, isChecked) -> config.setCategoryEnabled(QuestionItem.CATEGORY_GEOGRAPHY, isChecked));

        // U 盘更新题库按钮
        btnImportUsb = findViewById(R.id.btn_import_usb);
        btnImportUsb.setOnClickListener(v -> handleUsbImport());

        // 启动守护服务按钮
        btnStartGuard = findViewById(R.id.btn_start_guard);
        btnStartGuard.setOnClickListener(v -> {
            Intent serviceIntent = new Intent(this, TimeGuardService.class);
            startService(serviceIntent);
            Toast.makeText(this, "守护服务已成功启动！正在后台守护观看健康", Toast.LENGTH_LONG).show();
        });
    }

    private void loadDataToUi() {
        tvPlayMinutes.setText(config.getPlayAccumulateMinutes() + " 分钟");
        tvErrorMinutes.setText(config.getErrorPenaltyMinutes() + " 分钟");

        cbPoetry.setChecked(config.isCategoryEnabled(QuestionItem.CATEGORY_POETRY));
        cbMath.setChecked(config.isCategoryEnabled(QuestionItem.CATEGORY_MATH));
        cbEnglish.setChecked(config.isCategoryEnabled(QuestionItem.CATEGORY_ENGLISH));
        cbEncyclopedia.setChecked(config.isCategoryEnabled(QuestionItem.CATEGORY_ENCYCLOPEDIA));
        cbGeography.setChecked(config.isCategoryEnabled(QuestionItem.CATEGORY_GEOGRAPHY));

        Map<String, Integer> countMap = QuestionRepository.getInstance(this).getCategoryCountMap();
        tvPoetryCount.setText("当前题量：" + countMap.get(QuestionItem.CATEGORY_POETRY) + " 题");
        tvMathCount.setText("当前题量：" + countMap.get(QuestionItem.CATEGORY_MATH) + " 题");
        tvEnglishCount.setText("当前题量：" + countMap.get(QuestionItem.CATEGORY_ENGLISH) + " 题");
        tvEncyclopediaCount.setText("当前题量：" + countMap.get(QuestionItem.CATEGORY_ENCYCLOPEDIA) + " 题");
        tvGeographyCount.setText("当前题量：" + countMap.get(QuestionItem.CATEGORY_GEOGRAPHY) + " 题");
    }

    private void handleUsbImport() {
        Toast.makeText(this, "正在扫描 U 盘题库文件...", Toast.LENGTH_SHORT).show();
        UsbImportHelper.ImportResult result = UsbImportHelper.scanAndImportFromUsb(this);
        if (result.success) {
            QuestionRepository.getInstance(this).importQuestions(result.items);
            loadDataToUi();
            new AlertDialog.Builder(this)
                    .setTitle("✅ U 盘导入成功")
                    .setMessage(result.message)
                    .setPositiveButton("我知道了", null)
                    .show();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("⚠️ 导入提示")
                    .setMessage(result.message)
                    .setPositiveButton("确定", null)
                    .show();
        }
    }
}
