package com.hisense.studyguard;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class QuestionItem implements Serializable {
    public static final String CATEGORY_POETRY = "POETRY";           // 古诗词
    public static final String CATEGORY_MATH = "MATH";               // 简单数学
    public static final String CATEGORY_ENGLISH = "ENGLISH";         // 简单英语
    public static final String CATEGORY_ENCYCLOPEDIA = "ENCYCLOPEDIA"; // 生活百科
    public static final String CATEGORY_GEOGRAPHY = "GEOGRAPHY";       // 地理自然

    public String id;
    public String category;      // 5大类别之一
    public String question;      // 题目内容
    public String[] options;     // 4个选项 [A, B, C, D]
    public int correctIndex;     // 正确答案索引 0=A, 1=B, 2=C, 3=D

    public QuestionItem() {
        this.options = new String[4];
    }

    public QuestionItem(String id, String category, String question, String[] options, int correctIndex) {
        this.id = id;
        this.category = category;
        this.question = question;
        this.options = options;
        this.correctIndex = correctIndex;
    }

    public JSONObject toJsonObject() throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("category", category);
        obj.put("question", question);
        JSONArray arr = new JSONArray();
        for (String opt : options) {
            arr.put(opt);
        }
        obj.put("options", arr);
        obj.put("correctIndex", correctIndex);
        return obj;
    }

    public static QuestionItem fromJsonObject(JSONObject obj) throws JSONException {
        QuestionItem item = new QuestionItem();
        item.id = obj.optString("id", String.valueOf(System.currentTimeMillis()));
        item.category = obj.optString("category", CATEGORY_ENCYCLOPEDIA);
        item.question = obj.optString("question", "");
        JSONArray arr = obj.optJSONArray("options");
        if (arr != null && arr.length() >= 4) {
            item.options = new String[]{
                    arr.getString(0),
                    arr.getString(1),
                    arr.getString(2),
                    arr.getString(3)
            };
        } else {
            item.options = new String[]{"选项A", "选项B", "选项C", "选项D"};
        }
        item.correctIndex = obj.optInt("correctIndex", 0);
        return item;
    }
}
