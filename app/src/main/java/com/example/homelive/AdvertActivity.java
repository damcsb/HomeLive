package com.example.homelive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.example.homelive.model.Advert;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
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
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class AdvertActivity extends AppCompatActivity {

    private FirebaseAuth fbAuth;
    private FirebaseDatabase fDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference DatabaseAdvert;
    private DatabaseReference DatabaseUser;
    private FirebaseStorage mStorage;
    private StorageReference storageReference;

    private TextInputEditText edTitle;
    private TextInputEditText edDesc;
    private TextInputEditText edprice;
    private TextInputEditText edPhone;
    private SearchableSpinner ctSpinner;
    private String ct_selected;
    private MaterialButton btn_save;
    private MaterialButton btn_cancel;
    private ImageSwitcher carousel;
    private ImageButton car_right;
    private ImageButton car_left;
    private int carpos;
    private String presentdate;

    private int PICK_IMAGE = 200;
    private String urlauthor;
    private String urlimage;
    private Advert advertex;
    private String usern;

    private ArrayList<Uri> imagesUri;
    private String linkimg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert);
        getSupportActionBar().setTitle("Your advert");
        //Method declarations
        declarations();
        //Carousel start-up
        imagesUri = new ArrayList<>();
        carouselconstructor();
        //Get Advert from intent to edit
        comprobationadvert();
        //Get user photo for advert
        getuserphoto();


        getCityFromSpinner();

        getDate();
        getusername();


        edTitle.setText("Ejemplo");
        edDesc.setText("ejemplo2");
        edPhone.setText("65555");
        edprice.setText("10000");
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Take data
                String title = edTitle.getText().toString().trim();
                String desc = edDesc.getText().toString().trim();
                String price = edprice.getText().toString().trim();
                String Phone = edPhone.getText().toString().trim();
                String uid = UUID.randomUUID().toString();



                if(title.isEmpty() || desc.isEmpty() || price.isEmpty() || Phone.isEmpty()){
                    Snackbar.make(view, "Complete the fields", Snackbar.LENGTH_LONG).show();
                    return;
                }else if(imagesUri.isEmpty()){
                    Snackbar.make(view, "Put some photos", Snackbar.LENGTH_LONG).show();
                    return;
                }


                //create object
                Advert ad = new Advert();
                ad.setTittle(title);
                ad.setDescription(desc);
                ad.setPrice(Integer.valueOf(price));
                ad.setPhone(Integer.valueOf(Phone));
                ad.setEmail(fbAuth.getCurrentUser().getEmail());
                ad.setCity(ct_selected);
                ad.setUid(uid);
                ad.setDate(presentdate);
                ad.setUserpic(urlauthor);
                ad.setUserid(fbAuth.getUid());

                ad.setUsername(usern);
                for (int i=0; i<imagesUri.size(); i++){
                    storageReference.child("Adverts").child(ad.getUid()).child(String.valueOf(i)).putFile(imagesUri.get(i));
                    storageReference.child("Adverts").child(ad.getUid()).child(String.valueOf(i)).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            linkimg = uri.toString();
                        }
                    });
                    databaseReference.child("Uploads").child(ad.getUid()).setValue(linkimg);

                }

                //Comprobation advert to edit
                if(advertex == null){
                    DatabaseAdvert.child("Adverts").child(uid).setValue(ad);
                    DatabaseUser.child("Users").child(fbAuth.getUid()).child("Adverts").child(uid).setValue(ad);
                }else if(advertex != null){
                    //Update Advert
                    Advert adup = advertex;
                    adup.setUid(advertex.getUid());
                    adup.setTittle(title);
                    adup.setDescription(desc);
                    adup.setPrice(Integer.valueOf(price));
                    adup.setPhone(Integer.valueOf(Phone));
                    adup.setEmail(fbAuth.getCurrentUser().getEmail());
                    adup.setCity(ct_selected);
                    adup.setDate(presentdate);
                    adup.setUserpic(urlauthor);
                    getadvertphoto(adup);
                    DatabaseAdvert.child("Adverts").child(adup.getUid()).setValue(adup);
                    DatabaseUser.child("Users").child(fbAuth.getUid()).child("Adverts").child(adup.getUid()).setValue(adup);
                }
                advertex = null;
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdvertActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    //Get Today date
    private void getDate(){
        //Get date
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Date date = new Date();
        presentdate = dateFormat.format(date);
    }
    //Get username
    private void getusername(){
        databaseReference.child("Users").child(fbAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                usern = snapshot.child("username").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }
    //Comprobation advert to edit
    private void comprobationadvert(){
        advertex = (Advert) getIntent().getSerializableExtra("adex");
        if(advertex != null){
            //Replace data
            edTitle.setText(advertex.getTittle());
            edDesc.setText(advertex.getDescription());
            edPhone.setText(String.valueOf(advertex.getPhone()));
            edprice.setText(String.valueOf(advertex.getPrice()));
        }
    }
    //Get Author photo
    private void getuserphoto(){
        storageReference.child("Users Pictures").child(fbAuth.getUid()).child("Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                urlauthor = uri.toString();
            }
        });
    }
    private String getadvertphoto(Advert ad){
        storageReference.child("Adverts").child(ad.getUid()).child("0").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                urlimage = uri.toString();
            }
        });;
        return urlimage;
    }
    //Get city of spinner
    private void getCityFromSpinner(){

        ctSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ct_selected = ctSpinner.getAdapter().getItem(i).toString().trim();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }});
    }

    private void carouselconstructor(){
        //Carousel images
        carousel.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { pickImageInt(); }});
        //Carousel press right button
        car_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(carpos < imagesUri.size()-1 ){
                    //Position carousel +1
                    carpos++;
                    //Get image position
                    carousel.setImageURI(imagesUri.get(carpos));
                }else{
                    //End images array message
                    Toast.makeText(getApplicationContext(), "No more images...", Toast.LENGTH_LONG).show();
                }

            }
        });
        //Carousel press left button
        car_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(carpos > 0 ){
                    //Position carousel -1
                    carpos--;
                    //Get image position
                    carousel.setImageURI(imagesUri.get(carpos));
                }else{
                    //End images array message
                    Toast.makeText(getApplicationContext(), "No previus image", Toast.LENGTH_LONG).show();
                }
            }
        });
        //Setup switcher
        carousel.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                return imageView;
            }
        });

    }

    //Menu
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_home:
                Intent home = new Intent(AdvertActivity.this, HomeActivity.class);
                startActivity(home);
                finish();
                break;
            case R.id.item_chat:
                Intent conver = new Intent(AdvertActivity.this, ConversActivity.class);
                startActivity(conver);
                finish();
                break;
            case R.id.item_logout :
                fbAuth.signOut();
                Intent intent = new Intent(AdvertActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.item_settings:
                Intent setting = new Intent(AdvertActivity.this, SettingsActivity.class);
                startActivity(setting);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {

            if(data.getClipData() != null){

                int counts = data.getClipData().getItemCount();
                for(int i=0; i<counts; i++){
                    Uri imagesuri = data.getClipData().getItemAt(i).getUri();
                    imagesUri.add(imagesuri);//add to array uri
                }
                //first image carousel
                carousel.setImageURI(imagesUri.get(0));
                carpos = 0;
            }else {
                Uri imageuri = data.getData();
                imagesUri.add(imageuri);
                carousel.setImageURI(imagesUri.get(0));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void cleanedit(){
        edTitle.setText("");
        edDesc.setText("");
        edprice.setText("");
        edPhone.setText("");
    }

    private void pickImageInt(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Images to Upload"), PICK_IMAGE);
    }



    private void declarations(){
        //Firebase declarations
        fbAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance();
        DatabaseUser = fDatabase.getReference();
        DatabaseAdvert = fDatabase.getReference();
        mStorage = FirebaseStorage.getInstance();
        storageReference = mStorage.getReference();
        databaseReference = fDatabase.getReference();

        //View objects
        edTitle = findViewById(R.id.ad_tittle);
        edDesc = findViewById(R.id.ad_desc);
        edprice = findViewById(R.id.ad_price);
        edPhone = findViewById(R.id.ad_phone);
        btn_save = findViewById(R.id.ad_save);
        btn_cancel = findViewById(R.id.ad_cancel);
        ctSpinner = findViewById(R.id.ad_spinner);
        carousel = findViewById(R.id.ad_images);
        car_left = findViewById(R.id.caro_left);
        car_right = findViewById(R.id.caro_right);
    }
}