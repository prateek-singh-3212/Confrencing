package com.example.confrencing.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.confrencing.R;
import com.example.confrencing.Utils.Constants;
import com.example.confrencing.Utils.PreferenceManager;
import com.example.confrencing.models.User;
import com.example.confrencing.network.ApiClient;
import com.example.confrencing.network.ApiService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutgoingCall extends AppCompatActivity {

    private TextView email;
    private ImageView decline;
    private PreferenceManager manager;
    private String inviterToken = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outgoing_call);

        email = findViewById(R.id.User_Mail_Outgoing);
        decline = findViewById(R.id.Decline_Outgoing);


        manager = new PreferenceManager(getApplicationContext());
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if(task.isSuccessful() && task.getResult() != null){
                    inviterToken = task.getResult().getToken();
                }
            }
        });

        User user = (User) getIntent().getSerializableExtra("user");
        String meetingType = getIntent().getStringExtra("type");

        email.setText(user.EMAIL);

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelInvitationResponse(user.FCM_TOKEN);
                onBackPressed();
            }
        });

        if(user != null && meetingType != null){
            initateMeeting(meetingType,user.FCM_TOKEN);
        }

    }

    private void initateMeeting(String meetingType , String receiverToken){

        try{

            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            data.put(Constants.REMOTE_MESSAGE_TYPE,Constants.REMOTE_MESSAGE_INVITATION);
            data.put(Constants.REMOTE_MESSAGE_MEETING_TYPE,meetingType);
            data.put(Constants.KEY_EMAIL,manager.getString(Constants.KEY_EMAIL,null));
            data.put(Constants.REMOTE_MESSAGE_INVITOR_TOKEN,inviterToken);

            body.put(Constants.REMOTE_MESSAGE_DATA,data);
            body.put(Constants.REMOTE_MESSAGE_REGISTRATION_ID,tokens);

            sendRemoteMessage(body.toString(),Constants.REMOTE_MESSAGE_INVITATION);

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private void sendRemoteMessage(String remoteMessageBody , String type){
        ApiClient.getClient().create(ApiService.class)
                .sendRemoteMessage(
                        Constants.getRemoteMessage() , remoteMessageBody
                ).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call,@NonNull Response<String> response) {
                if(response.isSuccessful()){
                    if(type.equals(Constants.REMOTE_MESSAGE_INVITATION)){
                        Toast.makeText(OutgoingCall.this, "Invitation Send", Toast.LENGTH_SHORT).show();
                    }else if(type.equals(Constants.REMOTE_MESSAGE_CALL_CANCELED)){
                        Toast.makeText(OutgoingCall.this, "CALL  CANCELED", Toast.LENGTH_SHORT).show();
                    finish();
                    }

                }else {
                    Toast.makeText(OutgoingCall.this, "DEF: "+response.message(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call,@NonNull Throwable t) {
                Toast.makeText(OutgoingCall.this, "ABC" + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private void cancelInvitationResponse( String receiverToken){
        try{
            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            data.put(Constants.REMOTE_MESSAGE_TYPE,Constants.REMOTE_MESSAGE_INVITATION_RESPONSE);
            data.put(Constants.REMOTE_MESSAGE_INVITATION_RESPONSE,Constants.REMOTE_MESSAGE_CALL_CANCELED);

            body.put(Constants.REMOTE_MESSAGE_DATA,data);
            body.put(Constants.REMOTE_MESSAGE_REGISTRATION_ID,tokens);

            sendRemoteMessage(body.toString(),Constants.REMOTE_MESSAGE_CALL_CANCELED);

        }catch (Exception e){

        }
    }
}