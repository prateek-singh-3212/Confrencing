package com.example.confrencing.firebase;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.confrencing.Utils.Constants;
import com.example.confrencing.activites.IncommingCall;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        Log.d("FCM","Token : "+token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String type = remoteMessage.getData().get(Constants.REMOTE_MESSAGE_TYPE);
        if(type.equals(Constants.REMOTE_MESSAGE_INVITATION)){

            Intent intent = new Intent(getApplicationContext(), IncommingCall.class);
            intent.putExtra(Constants.KEY_EMAIL,remoteMessage.getData().get(Constants.KEY_EMAIL));
            intent.putExtra(Constants.REMOTE_MESSAGE_MEETING_TYPE , remoteMessage.getData().get(Constants.REMOTE_MESSAGE_MEETING_TYPE));
            intent.putExtra(Constants.REMOTE_MESSAGE_INVITOR_TOKEN,remoteMessage.getData().get(Constants.REMOTE_MESSAGE_INVITOR_TOKEN));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }


    }




//    @Override
//    public void onTokenRefresh() {
//        // Get updated InstanceID token.
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.d("FCM", "Refreshed token: " + refreshedToken);
//
//        // If you want to send messages to this application instance or
//        // manage this apps subscriptions on the server side, send the
//        // Instance ID token to your app server.
//        sendRegistrationToServer(refreshedToken);
//    }
}
