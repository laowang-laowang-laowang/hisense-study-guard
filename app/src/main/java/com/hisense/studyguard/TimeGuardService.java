package com.hisense.studyguard;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

public class TimeGuardService extends Service {
    private static final String TAG = "TimeGuardService";
    public static final String ACTION_CORRECT_ANSWER = "com.hisense.studyguard.ACTION_CORRECT_ANSWER";
    public static final String ACTION_WRONG_ANSWER = "com.hisense.studyguard.ACTION_WRONG_ANSWER";

    private Handler timerHandler = new Handler(Looper.getMainLooper());
    private int currentElapsedSeconds = 0;
    private ConfigManager config;
    private boolean isQuestionShown = false;

    private Runnable tickRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isQuestionShown) {
                currentElapsedSeconds++;
                int targetLimit = config.getPlayAccumulateMinutes() * 60;

                if (currentElapsedSeconds >= targetLimit) {
                    Log.i(TAG, "已达累计播放时间上限 (" + (targetLimit / 60) + "分钟)，弹出答题窗口并暂停视频");
                    triggerQuestionWindow();
                    return; // 暂停计时，等待答题
                }
            }
            timerHandler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        config = new ConfigManager(this);
        startForegroundServiceNotification();
        startTimer();
    }

    private void startTimer() {
        timerHandler.removeCallbacks(tickRunnable);
        timerHandler.postDelayed(tickRunnable, 1000);
    }

    private void triggerQuestionWindow() {
        isQuestionShown = true;
        Intent intent = new Intent(this, QuestionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            String action = intent.getAction();
            if (ACTION_CORRECT_ANSWER.equals(action)) {
                Log.i(TAG, "回答正确：重置累加时间，恢复正常计时");
                isQuestionShown = false;
                currentElapsedSeconds = 0;
                startTimer();
            } else if (ACTION_WRONG_ANSWER.equals(action)) {
                // 回答错误惩罚：按自定义的分钟数在当前累加时间中惩罚递增
                int penaltySec = config.getErrorPenaltyMinutes() * 60;
                int maxSec = config.getPlayAccumulateMinutes() * 60;
                // 惩罚机制：将下次弹窗时间提前 penaltySec 秒
                currentElapsedSeconds = Math.max(0, maxSec - penaltySec);
                Log.i(TAG, "回答错误：已执行时间惩罚，增加惩罚时间 " + (penaltySec / 60) + "分钟");
                isQuestionShown = false;
                startTimer();
            }
        }
        return START_STICKY;
    }

    private void startForegroundServiceNotification() {
        String channelId = "StudyGuardChannel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId, "健康守护服务", NotificationManager.IMPORTANCE_LOW);
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (nm != null) nm.createNotificationChannel(channel);
        }

        Notification notification = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, channelId)
                    .setContentTitle("电视学习守护运行中")
                    .setContentText("已开启健康视力保护与答题挑战")
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .build();
        }
        startForeground(2001, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
