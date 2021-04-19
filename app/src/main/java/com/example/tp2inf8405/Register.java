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


public class Register extends AppCompatActivity {
    private Button signup;
    private EditText username;
    private EditText password;
    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        username = (EditText) findViewById(R.id.usernameSignup);
        password = (EditText) findViewById(R.id.passwordSignup);
        signup = (Button) findViewById(R.id.createAccount);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                String user = username.getText().toString() + "@polymtl.ca";
                String pass = password.getText().toString();
                mAuth.createUserWithEmailAndPassword(user, pass)
                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("CREATION", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    goToMap();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(getApplicationContext(), "nom d'utilisateur deja utilisé", Toast.LENGTH_SHORT).show();
                                    password.getText().clear();
                                    username.getText().clear();
                                }
                            }
                        });


            }
        });

    }
    private void goToMap(){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
        finish();
    }
}
