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

import com.example.homelive.model.Advert;
import com.example.homelive.recycler.AdapterSettings;
import com.example.homelive.recycler.OnAdvertClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth fbAuth;
    private FirebaseDatabase fDatabase;
    private DatabaseReference DatabaseUser;
    private DatabaseReference DatabaseAdvert;
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
                    adverts.remove(ad);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        recycler.setAdapter(adapter);



    }
    private void showdata(){
        DatabaseAdvert.addValueEventListener(new ValueEventListener() {
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
    public void onBackPressed() { }
}