package com.hisense.studyguard;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;
import android.os.SystemClock;
import android.view.KeyEvent;

/**
 * 负责在弹窗触发时，强制使电视内容或手机投屏画面【自动暂停】并在答对后恢复
 */
public class MediaControlHelper {

    private static AudioFocusRequest audioFocusRequest;
    private static AudioManager.OnAudioFocusChangeListener focusChangeListener = focusChange -> {
        // 保持静音焦点
    };

    /**
     * 暂停播放（双重强控：音频焦点抢占 + 全局媒体按键 PAUSE 广播）
     * 绝大多数视频软件（爱奇艺、腾讯视频、优酷、B站TV）及投屏接收端（乐播投屏、系统投屏）
     * 监听到音频焦点丢失或媒体暂停键时，都会强制暂停画面和声音。
     */
    public static void pausePlayback(Context context) {
        if (context == null) return;
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (am == null) return;

        // 1. 抢占音频焦点 (Transient 独占抢占，强制让前台播放器静音并自动暂停)
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AudioAttributes playbackAttributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .build();
                audioFocusRequest = new AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                        .setAudioAttributes(playbackAttributes)
                        .setAcceptsDelayedFocusGain(false)
                        .setOnAudioFocusChangeListener(focusChangeListener)
                        .build();
                am.requestAudioFocus(audioFocusRequest);
            } else {
                am.requestAudioFocus(focusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. 模拟系统全局 Media 按键：发送 KEYCODE_MEDIA_PAUSE 暂停广播
        dispatchMediaKey(am, KeyEvent.KEYCODE_MEDIA_PAUSE);
    }

    /**
     * 答对后恢复播放（释放音频焦点 + 可选发送 PLAY 键）
     */
    public static void resumePlayback(Context context) {
        if (context == null) return;
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (am == null) return;

        // 1. 释放音频焦点
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && audioFocusRequest != null) {
                am.abandonAudioFocusRequest(audioFocusRequest);
            } else {
                am.abandonAudioFocus(focusChangeListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 2. 模拟发送继续播放按键
        dispatchMediaKey(am, KeyEvent.KEYCODE_MEDIA_PLAY);
    }

    private static void dispatchMediaKey(AudioManager am, int keyCode) {
        try {
            long eventTime = SystemClock.uptimeMillis();
            KeyEvent downEvent = new KeyEvent(eventTime, eventTime, KeyEvent.ACTION_DOWN, keyCode, 0);
            KeyEvent upEvent = new KeyEvent(eventTime, eventTime, KeyEvent.ACTION_UP, keyCode, 0);

            am.dispatchMediaKeyEvent(downEvent);
            am.dispatchMediaKeyEvent(upEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
