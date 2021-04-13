package com.example.homelive.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homelive.AdvertActivity;
import com.example.homelive.HomeActivity;
import com.example.homelive.InfoActivity;
import com.example.homelive.R;
import com.example.homelive.SettingsActivity;
import com.example.homelive.model.Advert;
import com.example.homelive.recycler.AdapterAdvert;
import com.example.homelive.recycler.OnAdvertClickInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    private RecyclerView recycler;
    private AdapterAdvert Adapter;
    private List<Advert> advertList;
    private FirebaseAuth fbAuth;
    private FirebaseDatabase fDatabase;
    private DatabaseReference DatabaseUser;
    private DatabaseReference DatabaseAdvert;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        fbAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance();
        DatabaseUser = fDatabase.getReference("Users");
        DatabaseAdvert = fDatabase.getReference("Adverts");

        recycler = root.findViewById(R.id.hm_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        advertList = new ArrayList<>();
        showdata();

        Adapter = new AdapterAdvert(advertList, new OnAdvertClickInfo() {
            @Override
            public void onClick(Advert ad) {
                Intent intent = new Intent(getActivity(), InfoActivity.class);
                intent.putExtra("adinf", ad);
                getActivity().setResult(RESULT_OK, intent);
                startActivity(intent);
            }
        });
        recycler.setAdapter(Adapter);

        return root;
    }

    private void showdata(){
        DatabaseAdvert.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                advertList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    Advert ad = ds.getValue(Advert.class);
                    advertList.add(ad);
                }
                Adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}