package com.example.homelive.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.example.homelive.Services.FilterService;
import com.example.homelive.SettingsActivity;
import com.example.homelive.model.Advert;
import com.example.homelive.recycler.AdapterAdvert;
import com.example.homelive.recycler.OnAdvertClickInfo;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

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

    private SearchableSpinner search_city;
    private TextInputEditText search_title;
    private TextInputEditText top_number;
    private TextInputEditText low_number;

    private String listener = "";
    private String city_sl;
    private String number1 = "";
    private String number2 = "";

    private FilterService filters;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        filters = new FilterService();

        fbAuth = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance();
        DatabaseUser = fDatabase.getReference("Users");
        DatabaseAdvert = fDatabase.getReference("Adverts");
        search_city = root.findViewById(R.id.home_srccity);
        search_title = root.findViewById(R.id.home_srctittle);
        top_number = root.findViewById(R.id.home_topnumber);
        low_number = root.findViewById(R.id.home_lownumber);

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

        search_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                listener = String.valueOf(s);
                filterdata();
            }
        });

        search_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city_sl = search_city.getSelectedItem().toString();
                filterdata();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        top_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                number1 = String.valueOf(s);
                filterdata();
            }
        });

        low_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                number2 = String.valueOf(s);
                filterdata();
            }

        });

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


    private void filterdata(){
        //Nothing
        if(listener.isEmpty() && city_sl.contains("Todas") && number1.isEmpty() && number2.isEmpty()){
            Adapter.filtrar((ArrayList<Advert>) advertList);
        }
        //Title
        else if(!listener.isEmpty() && city_sl.contains("Todas") && number1.isEmpty() && number2.isEmpty()){
            filters.filtertitle(listener, advertList, Adapter);
        }
        //City
        else if(listener.isEmpty() && !city_sl.contains("Todas") && number1.isEmpty() && number2.isEmpty()){
            filters.filtercity(city_sl, advertList, Adapter);
        }
        //Number 1
        else if(listener.isEmpty() && city_sl.contains("Todas") && !number1.isEmpty() && number2.isEmpty()){
            filters.filterprice1(Integer.valueOf(number1), advertList, Adapter);
        }
        //Number 2
        else if(listener.isEmpty() && city_sl.contains("Todas") && number1.isEmpty() && !number2.isEmpty()){
            filters.filterprice2(Integer.valueOf(number2), advertList, Adapter);
        }
        //Title and city
        else if(!listener.isEmpty() &&  !city_sl.contains("Todas") && number1.isEmpty() && number2.isEmpty()) {
            filters.filtertitlecity(listener, city_sl, advertList, Adapter);
        }
        //Title and number1
        else if(!listener.isEmpty() && city_sl.contains("Todas") && !number1.isEmpty() && number2.isEmpty()){
            filters.filtertitleprice1(listener, Integer.valueOf(number1), advertList, Adapter);
        }
        //City and number1
        else if(listener.isEmpty() && !city_sl.contains("Todas") && !number1.isEmpty()  && number2.isEmpty()){
            filters.filtercityprice1(city_sl, Integer.valueOf(number1), advertList, Adapter);
        }
        //City and number 2
        else if(listener.isEmpty() && !city_sl.contains("Todas") && number1.isEmpty() && !number2.isEmpty()){
            filters.filtercityprice2(city_sl, Integer.valueOf(number2), advertList, Adapter);
        }
        //number1 and number2
        else if(listener.isEmpty() && city_sl.contains("Todas") && !number1.isEmpty() && !number2.isEmpty()){
            filters.filterprice1price2(Integer.valueOf(number1), Integer.valueOf(number2), advertList, Adapter);
        }
        //Title and number2
        else if(!listener.isEmpty() && city_sl.contains("Todas") && number1.isEmpty() && !number2.isEmpty()){
            filters.filtertitleprice2(listener, Integer.valueOf(number2), advertList, Adapter);
        }
        //Title city number1
        else if(!listener.isEmpty() && !city_sl.contains("Todas") && !number1.isEmpty() && number2.isEmpty()){
            filters.filtertitlecitynumber1(listener, city_sl, Integer.valueOf(number1), advertList, Adapter);
        }
        //Title city number2
        else if(!listener.isEmpty() && !city_sl.contains("Todas") && number1.isEmpty() && !number2.isEmpty()){
            filters.filtertitlecitynumber2(listener, city_sl, Integer.valueOf(number2), advertList, Adapter);
        }
        //City number1 number2
        else if(listener.isEmpty() && !city_sl.contains("Todas") && !number1.isEmpty() && !number2.isEmpty()){
            filters.filtercitynumber1number2(city_sl, Integer.valueOf(number1), Integer.valueOf(number2), advertList, Adapter);
        }
        //Title city number1 number2
        else if(!listener.isEmpty() && !city_sl.contains("Todas") && !number1.isEmpty() && !number2.isEmpty()){
            filters.filterall(listener, city_sl, Integer.valueOf(number1), Integer.valueOf(number2), advertList, Adapter);
        }

    }

}