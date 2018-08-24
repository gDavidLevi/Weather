package ru.davidlevi.weather.firebase;

/**
 * Константы
 */
public class Constant {
    // Глобальный топик push-уведомлений
    public static final String FIREBASE_TOPIC_GLOBAL = "FIREBASE_TOPIC_GLOBAL";

    // Широковещательные интент-фильтры
    public static final String FIREBASE_REGISTRATION_COMPLETE = "FIREBASE_REGISTRATION_COMPLETE";
    public static final String FIREBASE_PUSH_NOTIFICATION = "FIREBASE_PUSH_NOTIFICATION";

    // Идентификаторы обработки уведомлений в нотификационном трее
    public static final int FIREBASE_NOTIFICATION_ID = 100;
    public static final int FIREBASE_NOTIFICATION_ID_IMAGE = 101;

    // Интент
    public static final String FIREBASE_INTENT_MESSAGE = "FIREBASE_INTENT_MESSAGE";
}
