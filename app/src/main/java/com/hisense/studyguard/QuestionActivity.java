package com.hisense.studyguard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuestionActivity extends Activity {
    private View rootLayout;
    private View cardContainer;
    private TextView tvCategoryBadge, tvQuestion, tvStatus;
    private Button[] btnOptions = new Button[4];
    private QuestionItem currentQuestion;
    private boolean isLocked = false;
    private ConfigManager config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_screen);

        config = new ConfigManager(this);
        rootLayout = findViewById(R.id.root_screen_layout);
        cardContainer = findViewById(R.id.card_container);
        tvCategoryBadge = findViewById(R.id.tv_category_badge);
        tvQuestion = findViewById(R.id.tv_question_content);
        tvStatus = findViewById(R.id.tv_status_tip);

        btnOptions[0] = findViewById(R.id.btn_option_a);
        btnOptions[1] = findViewById(R.id.btn_option_b);
        btnOptions[2] = findViewById(R.id.btn_option_c);
        btnOptions[3] = findViewById(R.id.btn_option_d);

        setupTvFocusAnimations();
        loadNextEligibleQuestion();

        for (int i = 0; i < 4; i++) {
            final int index = i;
            btnOptions[i].setOnClickListener(v -> handleAnswerSubmit(index));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 核心功能：只要弹窗处于前台，立即强制让正在播放的视频/投屏画面自动暂停！
        MediaControlHelper.pausePlayback(this);
    }

    private void setupTvFocusAnimations() {
        View.OnFocusChangeListener focusListener = (v, hasFocus) -> {
            if (hasFocus) {
                v.animate().scaleX(1.05f).scaleY(1.05f).setDuration(150).start();
            } else {
                v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).start();
            }
        };
        for (Button btn : btnOptions) {
            btn.setOnFocusChangeListener(focusListener);
        }
    }

    private void loadNextEligibleQuestion() {
        currentQuestion = QuestionRepository.getInstance(this).drawRandomQuestion(config);

        // 依据题目类别定制化渲染高颜值主题 UI
        ThemeHelper.applyCategoryTheme(currentQuestion.category, rootLayout, cardContainer,
                tvCategoryBadge, tvQuestion, btnOptions);

        tvQuestion.setText(currentQuestion.question);
        for (int i = 0; i < 4; i++) {
            btnOptions[i].setText(currentQuestion.options[i]);
            btnOptions[i].setEnabled(true);
        }
        tvStatus.setText("请使用遥控器【上下左右方向键】选择，按【OK键】确认");
        tvStatus.setTextColor(Color.parseColor("#E0E0E0"));

        btnOptions[0].requestFocus();
    }

    private void handleAnswerSubmit(int selectedIndex) {
        if (isLocked) return;

        if (selectedIndex == currentQuestion.correctIndex) {
            // 回答正确
            tvStatus.setText("🎉 太棒了，回答正确！正在恢复播放...");
            tvStatus.setTextColor(Color.GREEN);

            // 1. 恢复被暂停的播放画面和声音
            MediaControlHelper.resumePlayback(this);

            // 2. 通知后台服务重置时间
            Intent serviceIntent = new Intent(this, TimeGuardService.class);
            serviceIntent.setAction(TimeGuardService.ACTION_CORRECT_ANSWER);
            startService(serviceIntent);

            // 延迟 1 秒退出，给孩子一个获得成就感的提示
            tvStatus.postDelayed(this::finish, 1000);
        } else {
            // 回答错误
            isLocked = true;
            tvStatus.setTextColor(Color.parseColor("#FF5252"));
            for (Button btn : btnOptions) btn.setEnabled(false);

            int penaltyMin = config.getErrorPenaltyMinutes();

            // 通知服务记录回答错误，并增加累加时间
            Intent serviceIntent = new Intent(this, TimeGuardService.class);
            serviceIntent.setAction(TimeGuardService.ACTION_WRONG_ANSWER);
            startService(serviceIntent);

            // 罚时静置倒计时思考（锁定 30 秒，不能乱猜按键）
            new CountDownTimer(30000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tvStatus.setText("❌ 回答错误！已罚加 " + penaltyMin + " 分钟累计时间。请冷静思考 " + (millisUntilFinished / 1000) + " 秒后重试");
                }

                @Override
                public void onFinish() {
                    isLocked = false;
                    loadNextEligibleQuestion();
                }
            }.start();
        }
    }

    // 严密拦截遥控器返回键，防止孩子直接按返回退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Toast.makeText(this, "必须正确答对题目才能继续观看！", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
