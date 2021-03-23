package com.example.confrencing.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.confrencing.R;
import com.example.confrencing.Utils.Constants;
import com.example.confrencing.network.ApiClient;
import com.example.confrencing.network.ApiService;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetOngoingConferenceService;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IncommingCall extends AppCompatActivity {

    private ImageView accept, decline;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incomming_call);

        email = findViewById(R.id.User_Mail_Incoming);
        accept = findViewById(R.id.Accept_Incoming);
        decline = findViewById(R.id.Decline_Incoming);

        email.setText(getIntent().getStringExtra(Constants.KEY_EMAIL));

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInvitationResponse(Constants.REMOTE_MESSAGE_CALL_ACCEPTED,
                        getIntent().getStringExtra(Constants.REMOTE_MESSAGE_INVITOR_TOKEN));
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendInvitationResponse(Constants.REMOTE_MESSAGE_CALL_REJECTED,
                        getIntent().getStringExtra(Constants.REMOTE_MESSAGE_INVITOR_TOKEN));
                onBackPressed();
            }
        });

    }

    private void sendInvitationResponse(String type , String receiverToken){
        try{
            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            data.put(Constants.REMOTE_MESSAGE_TYPE,Constants.REMOTE_MESSAGE_INVITATION_RESPONSE);
            data.put(Constants.REMOTE_MESSAGE_INVITATION_RESPONSE,type);

            body.put(Constants.REMOTE_MESSAGE_DATA,data);
            body.put(Constants.REMOTE_MESSAGE_REGISTRATION_ID,tokens);

            sendRemoteMessage(body.toString(),type);

        }catch (Exception e){

        }
    }

    private void sendRemoteMessage(String remoteMessageBody , String type){
        ApiClient.getClient().create(ApiService.class)
                .sendRemoteMessage(
                        Constants.getRemoteMessage() , remoteMessageBody
                ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if(response.isSuccessful()){
                    if(type.equals(Constants.REMOTE_MESSAGE_CALL_ACCEPTED)) {

                        try {
                            URL serverURL = new URL("https://meet.jit.si");

                            JitsiMeetConferenceOptions conferenceService = new JitsiMeetConferenceOptions.Builder()
                                    .setServerURL(serverURL)
                                    .setWelcomePageEnabled(false)
                                    .setRoom(getIntent().getStringExtra(Constants.REMOTE_MESSAGE_MEETING_ROOM))
                                    .build();
                            JitsiMeetActivity.launch(IncommingCall.this,conferenceService);
                            finish();

                        } catch (Exception e) {
                            Toast.makeText(IncommingCall.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }else {
                        Toast.makeText(getApplicationContext(), "Invitation REJECTED", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }else {
                    Toast.makeText(getApplicationContext(), "DEF: "+response.message(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "ABC" + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private BroadcastReceiver invitationResponseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(Constants.REMOTE_MESSAGE_INVITATION_RESPONSE);
            if (type != null) {
                if (type.equals(Constants.REMOTE_MESSAGE_CALL_ACCEPTED)) {
                    Toast.makeText(context, "Invitation Accepted", Toast.LENGTH_SHORT).show();
                }
                if (type.equals(Constants.REMOTE_MESSAGE_CALL_REJECTED)) {
                    Toast.makeText(context, "Invitation Rejected", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                invitationResponseReceiver,
                new IntentFilter(Constants.REMOTE_MESSAGE_INVITATION_RESPONSE)
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(invitationResponseReceiver);
    }
}