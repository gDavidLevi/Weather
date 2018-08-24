package ru.davidlevi.weather.firebase;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

/**
 * Класс обрабатывает полученное сообщение
 */
public class MessagingService extends FirebaseMessagingService {
    // Logcat
    private static final String TAG = MessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom()); // From: 839975424458
        if (remoteMessage == null) return;

        // Если сообщение содержит уведомления, то...
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Notification body: " + remoteMessage.getNotification().getBody()); // Notification body: wwww
            handleNotification(remoteMessage.getNotification().getBody());
        }
    }

    /**
     * Обработка уведомления
     *
     * @param message String
     */
    private void handleNotification(String message) {
        Context context = getApplicationContext();
        //
        if (!isAppWorkingInBackground(context)) {
            // Приложение находится на переднем плане, показать push-сообщение
            Intent intent = new Intent(Constant.FIREBASE_PUSH_NOTIFICATION);
            intent.putExtra(Constant.FIREBASE_INTENT_MESSAGE, message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

            // Проиграть звук
            try {
                Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.getPackageName() + "/raw/notification");
                Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
                ringtone.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Если приложение находится в фоновом режиме, Firebase сама обрабатывает уведомление
        }
    }

    /**
     * Метод проверяет, находится ли приложение в фоновом режиме или нет.
     */
    private boolean isAppWorkingInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = activityManager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = activityManager.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }
}