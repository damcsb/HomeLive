package com.example.homelive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth fbAuth;

    private TextInputEditText editusername;
    private TextInputEditText editemail;
    private TextInputEditText editpassword;
    private TextInputEditText editpassword2;

    private Button btn_register;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        fbAuth = FirebaseAuth.getInstance();

        editusername = findViewById(R.id.reg_username);
        editemail = findViewById(R.id.reg_email);
        editpassword = findViewById(R.id.reg_pass);
        editpassword2 = findViewById(R.id.reg_pass2);

        btn_login = findViewById(R.id.reg_login);
        btn_register = findViewById(R.id.reg_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editusername.getText().toString().trim();
                String email = editemail.getText().toString().trim();
                String password = editpassword.getText().toString().trim();
                String password2 = editpassword2.getText().toString().trim();

                if(username.isEmpty() || email.isEmpty() || password.isEmpty() || password2.isEmpty()){
                    Snackbar.make(view, "Write all the info", Snackbar.LENGTH_LONG).show();
                    return;
                }else if (!password.equals(password2)){
                    Snackbar.make(view, "Passwords have to be the same", Snackbar.LENGTH_LONG).show();
                    return;
                }

                fbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            Snackbar.make(view, "Register ERROR!", Snackbar.LENGTH_LONG).show();
                            return;
                        }
                    }
                });

            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}