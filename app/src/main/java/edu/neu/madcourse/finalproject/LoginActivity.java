package edu.neu.madcourse.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText loginUsername;
    private EditText loginPassword;
    private Button loginButton;
    private TextView textRegister;
    private ProgressBar progressBar;

    private String email;

    private FirebaseAuth auth;

    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginUsername = findViewById(R.id.loginUsername);
        loginPassword = findViewById(R.id.loginPassword);
        textRegister = findViewById(R.id.textRegister);
        loginButton = findViewById(R.id.loginButton);

        progressBar = findViewById(R.id.loginProgressBar);

        auth = FirebaseAuth.getInstance();


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String user = loginUsername.getText().toString();
                String pw = loginPassword.getText().toString();

                if (user.isEmpty()) {
                    loginUsername.setError("Username is required");
                }
                else if (pw.isEmpty()) {
                    loginPassword.setError("Password is required");
                }

                else {
                    reference.child(user).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (!snapshot.exists()) {
                                Toast.makeText(LoginActivity.this, "Username does not exist", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                progressBar.setVisibility(view.VISIBLE);
                                email = snapshot.child("email").getValue().toString();
                                //authenticate user
                                auth.signInWithEmailAndPassword(email, pw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                            Intent toMainPage = new Intent(LoginActivity.this, MainPageActivity.class);
                                            toMainPage.putExtra("username", user);
                                            startActivity(toMainPage);
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }
                                        else {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }




                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }








            }
        });

        textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }




}
