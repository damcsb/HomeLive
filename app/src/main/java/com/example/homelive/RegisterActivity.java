package com.example.homelive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth fbAuth;
    private FirebaseStorage mStorage;
    private StorageReference storageReference;

    private TextInputEditText editusername;
    private TextInputEditText editemail;
    private TextInputEditText editpassword;
    private TextInputEditText editpassword2;
    private ImageView userpic;
    private int PICK_IMAGE = 267;
    Uri imagePath;

    private Button btn_register;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        fbAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();
        storageReference = mStorage.getReference();

        editusername = findViewById(R.id.reg_username);
        editemail = findViewById(R.id.reg_email);
        editpassword = findViewById(R.id.reg_pass);
        editpassword2 = findViewById(R.id.reg_pass2);
        userpic = findViewById(R.id.reg_pic);

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
                }else if(imagePath == null){
                    Snackbar.make(view, "Put an avatar", Snackbar.LENGTH_LONG).show();
                    return;
                }

                fbAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "", Toast.LENGTH_SHORT).show();
                            StorageReference imageReference = storageReference.child("Users Pictures").child(fbAuth.getUid()).child("Profile Pic");
                            imageReference.putFile(imagePath);
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

        userpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null){
            imagePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagePath);
                userpic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}