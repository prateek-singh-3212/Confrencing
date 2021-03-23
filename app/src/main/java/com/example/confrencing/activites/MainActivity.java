package com.example.confrencing.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.confrencing.R;
import com.example.confrencing.Utils.Constants;
import com.example.confrencing.Utils.PreferenceManager;
import com.example.confrencing.adapters.UserAdapter;
import com.example.confrencing.listeners.UserListener;
import com.example.confrencing.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements UserListener {

    private TextView userName;
    private PreferenceManager manager;
    private RecyclerView recyclerView;
    private List<User> users;
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = new PreferenceManager(getApplicationContext());

        userName = findViewById(R.id.UserName);
        userName.setText(manager.getString(Constants.KEY_EMAIL,"USERNAME"));

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if(task.isSuccessful() && task.getResult() != null){
                    updateFCMToken(task.getResult().getToken());
                }else{
                    Toast.makeText(getApplicationContext(),"Token Not Found",Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerView = findViewById(R.id.UsersRecyclerView);

        users = new ArrayList<>();
        adapter = new UserAdapter(users , this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

        getUsers();
    }

    private void getUsers(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_USERS)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        String currentUserId = manager.getString(Constants.KEY_USER_ID,null);
                        if(task.isSuccessful() && task.getResult() != null){
                            for(QueryDocumentSnapshot snapshot : task.getResult()){
                                if(currentUserId.equals(snapshot.getId())){
                                    continue;
                                }
                                User user = new User();
                                user.EMAIL = snapshot.getString(Constants.KEY_EMAIL);
                                user.FCM_TOKEN = snapshot.getString(Constants.KEY_FCM_TOKEN);
                                users.add(user);

                            }

                            if(users.size() > 0){
                                adapter.notifyDataSetChanged();
                            }else {
                                Toast.makeText(getApplicationContext(),"We Are Facing Some Server Error",Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Toast.makeText(getApplicationContext(),"No User Is Available",Toast.LENGTH_SHORT).show();
                        }




                    }
                });
    }

    private void updateFCMToken(String token){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference reference = database.collection(Constants.KEY_USERS)
                .document(manager.getString(Constants.KEY_USER_ID,null));

        reference.update(Constants.KEY_FCM_TOKEN, token)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"FCM Token Updated",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"FCM Token Not Updated",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void initiateVideoMeeting(User user) {

        if(user.FCM_TOKEN == null || user.FCM_TOKEN.isEmpty()){
            Toast.makeText(getApplicationContext(),"User Token in not Available",Toast.LENGTH_SHORT).show();
        }else {
            Intent intent = new Intent(getApplicationContext(),OutgoingCall.class);
            intent.putExtra("user",user);
            intent.putExtra("type","video");
            startActivity(intent);
            Toast.makeText(getApplicationContext(),"Video Calling ..",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void initiateAudioMeeting(User user) {
        if(user.FCM_TOKEN == null || user.FCM_TOKEN.isEmpty()){
            Intent intent = new Intent(getApplicationContext(),OutgoingCall.class);
            intent.putExtra("user",user);
            intent.putExtra("type","audio");
            startActivity(intent);
            Toast.makeText(getApplicationContext(),"User Token in not Available",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(),"Audio Calling ..",Toast.LENGTH_SHORT).show();
        }
    }
}