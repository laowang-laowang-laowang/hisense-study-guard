package com.hisense.studyguard;

import android.content.Context;
import android.content.SharedPreferences;

public class ConfigManager {
    private static final String PREF_NAME = "GuardSettingsPrefs";
    private SharedPreferences sp;

    public ConfigManager(Context context) {
        sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // 1. 播放累加时间（单位：分钟，默认 20 分钟）
    public int getPlayAccumulateMinutes() {
        return sp.getInt("play_accumulate_min", 20);
    }
    public void setPlayAccumulateMinutes(int min) {
        sp.edit().putInt("play_accumulate_min", Math.max(1, min)).apply();
    }

    // 2. 回答错误增加的累加时间（单位：分钟，默认罚加 5 分钟）
    public int getErrorPenaltyMinutes() {
        return sp.getInt("error_penalty_min", 5);
    }
    public void setErrorPenaltyMinutes(int min) {
        sp.edit().putInt("error_penalty_min", Math.max(1, min)).apply();
    }

    // 3. 题库开关（5大类）
    public boolean isCategoryEnabled(String category) {
        return sp.getBoolean("enable_" + category, true);
    }
    public void setCategoryEnabled(String category, boolean enable) {
        sp.edit().putBoolean("enable_" + category, enable).apply();
    }

    // 4. 自定义各题库抽题数量/权重配额（默认每类 30 道）
    public int getCategoryQuota(String category) {
        return sp.getInt("quota_" + category, 30);
    }
    public void setCategoryQuota(String category, int quota) {
        sp.edit().putInt("quota_" + category, Math.max(1, quota)).apply();
    }
}
