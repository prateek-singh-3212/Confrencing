package com.example.confrencing.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.confrencing.R;
import com.example.confrencing.Utils.Constants;
import com.example.confrencing.Utils.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class SignIn extends AppCompatActivity {

    private EditText email,password;
    private Button goIn;
    private PreferenceManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        findViewById(R.id.SignUPIN).setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SignUp.class)));


        email = findViewById(R.id.EmailIN);
        password = findViewById(R.id.PasswordIN);
        goIn = findViewById(R.id.GOIN);


        checkUserIsSignedIn();

        goIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().trim().isEmpty()){
                    Toast.makeText(SignIn.this , "Provide Email" , Toast.LENGTH_SHORT).show();
                }else if(password.getText().toString().isEmpty()){
                    Toast.makeText(SignIn.this , "Provide Password" , Toast.LENGTH_SHORT).show();
                }else {
                    signIn();
                }
            }
        });



    }

    private void checkUserIsSignedIn() {
        manager = new PreferenceManager(getApplicationContext());
        if (manager.getBoolean(Constants.KEY_USER_SIGNED_IN,false)){
            Intent intent = new Intent(SignIn.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void signIn() {

        FirebaseFirestore database= FirebaseFirestore.getInstance();

        database.collection(Constants.KEY_USERS)
                .whereEqualTo(Constants.KEY_EMAIL,email.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, password.getText().toString())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult().getDocuments().size() >0 && task.getResult()!= null ){

                            DocumentSnapshot snapshot = task.getResult().getDocuments().get(0);

                            manager.putBoolean(Constants.KEY_USER_SIGNED_IN,true);
                            manager.putString(Constants.KEY_USER_ID , snapshot.getId());
                            manager.putString(Constants.KEY_EMAIL,email.getText().toString());
                            manager.putString(Constants.KEY_PASSWORD,password.getText().toString());

                            Intent intent = new Intent(SignIn.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else {
                            Toast.makeText(getApplicationContext(),"Error In Singning In",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}