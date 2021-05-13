package com.example.homelive;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.homelive.model.Advert;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfoActivity extends AppCompatActivity {

    private ImageView advertpic;
    private CircleImageView profilepic;
    private TextView advertauth, advertdesc, advertplace;
    private FloatingActionButton floatingActionButton;
    private StorageReference storageReference;

    private ArrayList<Uri> imagesUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ViewInits();

        Advert advertinf = (Advert) getIntent().getSerializableExtra("adinf");

        storageReference = FirebaseStorage.getInstance().getReference();

        advertauth.setText(advertinf.getEmail());
        advertdesc.setText(advertinf.getDescription());
        advertplace.setText(advertinf.getCity());

        Uri authorpic = Uri.parse(advertinf.getUserpic());
        Picasso.get().load(authorpic).into(profilepic);

    }


    private void ViewInits(){
        advertpic = findViewById(R.id.info_image);
        profilepic = findViewById(R.id.info_propic);
        advertauth = findViewById(R.id.info_username);
        advertdesc = findViewById(R.id.info_desc);
        advertplace = findViewById(R.id.info_place);

    }
}