package com.example.homelive.ui.Profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private FirebaseAuth fbAuth;
    private FirebaseStorage mStorage;
    private StorageReference storageReference;

    private ImageView userimg;
    private int PICK_IMAGE = 200;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        fbAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance();
        storageReference = mStorage.getReference();

        userimg = root.findViewById(R.id.pro_image);
        LoadImage();

        userimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertImage();
            }
        });
        return root;
    }

    private void LoadImage(){
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