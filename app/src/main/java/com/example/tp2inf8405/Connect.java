package com.example.tp2inf8405;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Connect extends AppCompatActivity {
    private Button signup;
    private EditText username;
    private EditText password;
    private FirebaseAuth mAuth;
    private Button connect;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        signup = (Button) findViewById(R.id.signup);
        connect = (Button) findViewById(R.id.connect);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister();
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString() + "@polymtl.ca";
                String pass = password.getText().toString();
                mAuth.signInWithEmailAndPassword(user, pass)
                        .addOnCompleteListener(Connect.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("CONNECTION", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    goToMap();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("CONNECTION", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(Connect.this, "Erreur d'authentification",
                                            Toast.LENGTH_SHORT).show();
                                    password.getText().clear();
                                    username.getText().clear();

                                }
                            }
                        });
            }
        });

    }
    private void goToRegister(){
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }
    private void goToMap(){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
        finish();
    }
}
