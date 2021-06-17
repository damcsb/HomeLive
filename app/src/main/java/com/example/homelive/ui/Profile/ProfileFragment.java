package com.example.homelive.ui.Profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.homelive.HomeActivity;
import com.example.homelive.LoginActivity;
import com.example.homelive.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private FirebaseAuth fbAuth;
    private FirebaseStorage mStorage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private ImageView userimg;
    private int PICK_IMAGE = 200;

    private TextInputEditText newusername, passw1;
    private Button btn_editusername, btn_editpass;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        fbAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();
        storageReference = mStorage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        userimg = root.findViewById(R.id.pro_image);
        newusername = root.findViewById(R.id.pro_username);
        passw1 = root.findViewById(R.id.pro_pass);
        btn_editusername = root.findViewById(R.id.pro_editname);
        btn_editpass = root.findViewById(R.id.pro_editpass);

        LoadImage();

        userimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertImage();
            }
        });


        btn_editusername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = newusername.getText().toString().trim();

                if(username.isEmpty()) {
                    Snackbar.make(v, "Put a new username", Snackbar.LENGTH_LONG).show();
                    return;
                }
                databaseReference.child("Users").child(fbAuth.getUid()).child("username").setValue(username);
                Snackbar.make(v, "Username changed!", Snackbar.LENGTH_LONG).show();

                newusername.setText("");
            }
        });

        btn_editpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass1 = passw1.getText().toString().trim();
                if(pass1.isEmpty()){
                    Snackbar.make(v, "Put the new password", Snackbar.LENGTH_LONG).show();
                    return;
                }

                fbAuth.getCurrentUser().updatePassword(pass1);
                Snackbar.make(v, "Password changed!", Snackbar.LENGTH_LONG).show();
                passw1.setText("");

            }
        });


        return root;
    }

    private void LoadImage() {
        storageReference.child("Users Pictures").child(fbAuth.getUid()).child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(userimg);
            }
        });
    }

    private void AlertImage() {
        MaterialAlertDialogBuilder Dialog = new MaterialAlertDialogBuilder(getContext());
        Dialog.setTitle("Update Profile");
        Dialog.setMessage("Do you want to change your picture?");
        Dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE);
            }
        });
        Dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        }).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            StorageReference filePath = storageReference.child("Users Pictures").child(fbAuth.getUid()).child("Profile Pic");

            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Upload Succesfully", Toast.LENGTH_LONG).show();
                    LoadImage();
                }
            });
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}