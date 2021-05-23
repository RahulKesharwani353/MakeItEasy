package com.example.makeiteasy;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class notificationService extends FirebaseMessagingService {
    public notificationService() {
        super();
    }

    @Override
    public void onMessageReceived(@NonNull @org.jetbrains.annotations.NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }
}
