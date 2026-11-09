package com.hisense.studyguard;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ThemeHelper {

    public static void applyCategoryTheme(String category, View rootLayout, View cardContainer,
                                          TextView tvCategoryBadge, TextView tvQuestion, Button[] buttons) {
        if (category == null) category = QuestionItem.CATEGORY_ENCYCLOPEDIA;

        GradientDrawable bgGradient = new GradientDrawable();
        bgGradient.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        bgGradient.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);

        GradientDrawable cardBg = new GradientDrawable();
        cardBg.setCornerRadius(24);

        int badgeColor;
        int focusBorderColor;
        int focusBgColor;
        int normalBorderColor;
        String badgeText;

        switch (category.toUpperCase()) {
            case QuestionItem.CATEGORY_POETRY: // 古诗词：水墨云山，古雅风韵
                bgGradient.setColors(new int[]{Color.parseColor("#141E1B"), Color.parseColor("#080D0B")});
                cardBg.setColor(Color.parseColor("#1B2A26"));
                cardBg.setStroke(3, Color.parseColor("#C5A059"));
                badgeColor = Color.parseColor("#E5C158");
                focusBorderColor = Color.parseColor("#FFD700");
                focusBgColor = Color.parseColor("#44C5A059");
                normalBorderColor = Color.parseColor("#55C5A059");
                badgeText = "📜 【古诗文荟萃】 · 腹有诗书气自华";
                break;

            case QuestionItem.CATEGORY_MATH: // 简单数学：经典智慧黑板，严谨理智
                bgGradient.setColors(new int[]{Color.parseColor("#0F281E"), Color.parseColor("#08140E")});
                cardBg.setColor(Color.parseColor("#16382A"));
                cardBg.setStroke(3, Color.parseColor("#00E676"));
                badgeColor = Color.parseColor("#00E676");
                focusBorderColor = Color.parseColor("#69F0AE");
                focusBgColor = Color.parseColor("#4400E676");
                normalBorderColor = Color.parseColor("#5500E676");
                badgeText = "📐 【趣味数学】 · 逻辑之美，探索规律";
                break;

            case QuestionItem.CATEGORY_ENGLISH: // 简单英语：活力英伦波普，明艳欢快
                bgGradient.setColors(new int[]{Color.parseColor("#2F1648"), Color.parseColor("#160B24")});
                cardBg.setColor(Color.parseColor("#3F1F60"));
                cardBg.setStroke(3, Color.parseColor("#FF4081"));
                badgeColor = Color.parseColor("#FFD54F");
                focusBorderColor = Color.parseColor("#FFEB3B");
                focusBgColor = Color.parseColor("#44FF4081");
                normalBorderColor = Color.parseColor("#55FF4081");
                badgeText = "🔤 【快乐英语 ABC】 · 拥抱世界的小窗口";
                break;

            case QuestionItem.CATEGORY_ENCYCLOPEDIA: // 生活百科：自然温暖，生活智慧
                bgGradient.setColors(new int[]{Color.parseColor("#132A42"), Color.parseColor("#0A1724")});
                cardBg.setColor(Color.parseColor("#1B3B5C"));
                cardBg.setStroke(3, Color.parseColor("#00BCD4"));
                badgeColor = Color.parseColor("#4DD0E1");
                focusBorderColor = Color.parseColor("#80DEEA");
                focusBgColor = Color.parseColor("#4400BCD4");
                normalBorderColor = Color.parseColor("#5500BCD4");
                badgeText = "💡 【生活百科全书】 · 点亮生活每一个常识";
                break;

            case QuestionItem.CATEGORY_GEOGRAPHY: // 地理自然：山川海陆，星球漫游
            default:
                bgGradient.setColors(new int[]{Color.parseColor("#0B132B"), Color.parseColor("#050914")});
                cardBg.setColor(Color.parseColor("#1C2541"));
                cardBg.setStroke(3, Color.parseColor("#3A86FF"));
                badgeColor = Color.parseColor("#64DFDF");
                focusBorderColor = Color.parseColor("#48CAE4");
                focusBgColor = Color.parseColor("#443A86FF");
                normalBorderColor = Color.parseColor("#553A86FF");
                badgeText = "🌍 【地理与大自然】 · 万里江山，天地探秘";
                break;
        }

        rootLayout.setBackground(bgGradient);
        cardContainer.setBackground(cardBg);
        tvCategoryBadge.setText(badgeText);
        tvCategoryBadge.setTextColor(badgeColor);

        // 按钮样式
        for (Button btn : buttons) {
            btn.setBackground(createButtonSelector(focusBgColor, focusBorderColor, normalBorderColor));
        }
    }

    private static StateListDrawable createButtonSelector(int focusBg, int focusBorder, int normalBorder) {
        StateListDrawable states = new StateListDrawable();

        // 焦点选中状态
        GradientDrawable focusDrawable = new GradientDrawable();
        focusDrawable.setColor(focusBg);
        focusDrawable.setCornerRadius(16);
        focusDrawable.setStroke(5, focusBorder);

        // 普通未选中状态
        GradientDrawable normalDrawable = new GradientDrawable();
        normalDrawable.setColor(Color.parseColor("#26000000"));
        normalDrawable.setCornerRadius(16);
        normalDrawable.setStroke(2, normalBorder);

        states.addState(new int[]{android.R.attr.state_focused}, focusDrawable);
        states.addState(new int[]{}, normalDrawable);
        return states;
    }
}
