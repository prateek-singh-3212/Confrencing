package com.example.confrencing.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.confrencing.R;
import com.example.confrencing.Utils.Constants;
import com.example.confrencing.Utils.PreferenceManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {
    private EditText email,password;
    private Button goUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        email = findViewById(R.id.EmailUP);
        password = findViewById(R.id.PasswordUP);
        goUp = findViewById(R.id.GOUP);



        goUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().trim().isEmpty()){
                    Toast.makeText(SignUp.this , "Provide Email" , Toast.LENGTH_SHORT).show();
                }else if(password.getText().toString().isEmpty()){
                    Toast.makeText(SignUp.this , "Provide Password" , Toast.LENGTH_SHORT).show();
                }else {
                    signUp();
                }
            }
        });


    }

    private void signUp() {

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String,Object> user = new HashMap<>();
        user.put(Constants.KEY_EMAIL,email.getText().toString());
        user.put(Constants.KEY_PASSWORD,password.getText().toString());

        PreferenceManager manager = new PreferenceManager(getApplicationContext());


        database.collection(Constants.KEY_USERS)
                .add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                manager.putBoolean(Constants.KEY_USER_SIGNED_IN,true);
                manager.putString(Constants.KEY_USER_ID , documentReference.getId());
                manager.putString(Constants.KEY_EMAIL,email.getText().toString());
                manager.putString(Constants.KEY_PASSWORD,password.getText().toString());
                Intent intent = new Intent(SignUp.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                Toast.makeText(SignUp.this,"User Added",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUp.this,"User Not Added",Toast.LENGTH_SHORT).show();
            }
        });


    }
}