package com.example.homelive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth fbAuth;

    private TextInputEditText editusername;
    private TextInputEditText editpassword;

    private Button btn_login;
    private Button btn_register;

    private ImageView loginimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        fbAuth = FirebaseAuth.getInstance();

        editusername = findViewById(R.id.log_username);
        editpassword = findViewById(R.id.log_pass);
        btn_login = findViewById(R.id.log_login);
        btn_register = findViewById(R.id.log_register);
        loginimg = findViewById(R.id.log_pic);


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editusername.getText().toString().trim();
                String password = editpassword.getText().toString().trim();

                if(username.isEmpty() || password.isEmpty()){
                    Snackbar.make(view, "Put your username and password", Snackbar.LENGTH_LONG).show();
                    return;
                }
                fbAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Log in!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Snackbar.make(view, "Login ERROR", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}