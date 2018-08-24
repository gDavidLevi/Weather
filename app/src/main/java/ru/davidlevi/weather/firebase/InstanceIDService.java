package ru.davidlevi.weather.firebase;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * todo Устарел подход. Переделать!
 */
public class InstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = InstanceIDService.class.getSimpleName();

    /**
     * Получен новый токен
     */
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Firebase RegId
        Log.d(TAG, "Firebase RegId: " + refreshedToken);

        // Уведомить UI, что регистрация завершена, и индикатор прогресса может быть скрыт
        Intent intent = new Intent(Constant.FIREBASE_REGISTRATION_COMPLETE);
        intent.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
