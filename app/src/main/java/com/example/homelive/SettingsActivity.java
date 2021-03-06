package com.example.homelive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.homelive.model.Advert;
import com.example.homelive.recycler.AdapterSettings;
import com.example.homelive.recycler.OnAdvertClickListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth fbAuth;
    private FirebaseDatabase fDatabase;
    private DatabaseReference DatabaseUser;
    private DatabaseReference DatabaseAdvert;
    private FirebaseStorage mStorage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private AdapterSettings adapter;
    private RecyclerView recycler;
    private List<Advert> adverts;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        fbAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance();
        DatabaseUser = fDatabase.getReference("Users");
        DatabaseAdvert = fDatabase.getReference("Adverts");
        mStorage = FirebaseStorage.getInstance();
        storageReference = mStorage.getReference("Adverts");
        databaseReference = FirebaseDatabase.getInstance().getReference();

        recycler = findViewById(R.id.rv_settings);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        adverts = new ArrayList<>();
        showdata();

        adapter = new AdapterSettings(adverts, new OnAdvertClickListener() {
            @Override
            public void onClick(Advert ad, int cop) {
                switch (cop){
                    case OnAdvertClickListener.DEL_AD :
                    DatabaseUser.child(fbAuth.getUid()).child("Adverts").child(ad.getUid()).removeValue();
                    DatabaseAdvert.child(ad.getUid()).removeValue();
                    StorageReference imagesReference = storageReference.child(ad.getUid());
                    Task<ListResult> listResultTask = imagesReference.listAll();
                    listResultTask.addOnCompleteListener(new OnCompleteListener<ListResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<ListResult> task) {
                            List<StorageReference> items = task.getResult().getItems();
                            for(int i = 0; i < items.size(); i++){
                                items.get(i).delete();
                            }
                        }
                    });
                    adverts.remove(ad);
                    adapter.notifyDataSetChanged();
                    break;
                    case OnAdvertClickListener.EDIT_AD :
                        Intent intent = new Intent(SettingsActivity.this, AdvertActivity.class);
                        intent.putExtra("adex", ad);
                        setResult(RESULT_OK, intent);
                        startActivity(intent);
                }
            }
        });
        recycler.setAdapter(adapter);


    }
    private void showdata(){
        databaseReference.child("Users").child(fbAuth.getUid()).child("Adverts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adverts.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                        Advert ad = ds.getValue(Advert.class);
                    adverts.add(ad);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

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
            case R.id.item_create:
                Intent create = new Intent(SettingsActivity.this, AdvertActivity.class);
                startActivity(create);
                finish();
                break;

            case R.id.item_home:
                Intent home = new Intent(SettingsActivity.this, HomeActivity.class);
                startActivity(home);
                finish();
                break;
            case R.id.item_chat:
                Intent convers = new Intent(SettingsActivity.this, HomeActivity.class);
                startActivity(convers);
                finish();
                break;
            case R.id.item_logout :
                fbAuth.signOut();
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}