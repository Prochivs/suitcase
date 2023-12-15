package com.example.suit_case_application.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suit_case_application.R;
import com.example.suit_case_application.model.ItemDetail;
import com.example.suit_case_application.utils.ItemAdapter;
import com.example.suit_case_application.utils.OnClick;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeScreen extends AppCompatActivity implements OnClick {

    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private DatabaseReference databaseReference;
    private List<ItemDetail> items;
    private Button location, seachbutton;
    private ImageView emptyText;
    private TextView emptyTextView;
    private SearchView searchfield;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);



        //Initialization
        searchfield = findViewById(R.id.search);
        emptyText = findViewById(R.id.tv_no_data);
        emptyTextView = findViewById(R.id._no_data);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

       items = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Items").child(mAuth.getCurrentUser().getUid());
        Toast.makeText(getApplicationContext(), "Loading items please wait ...", Toast.LENGTH_LONG).show();


        databaseReference.addValueEventListener((new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    ItemDetail products = postSnapshot.getValue(ItemDetail.class);
                    items.add(products);
                }

                if (!items.isEmpty()) {
                    adapter = new ItemAdapter(getApplicationContext(), items, HomeScreen.this);
                    recyclerView.setAdapter(adapter);

                } else {
                    emptyText.setVisibility(View.VISIBLE);
                    emptyTextView.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }));



        searchfield.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String text) {
                search(text);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

        });



        //for item seletion in the bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        // get the current item activity selected
        bottomNavigationView.setSelectedItemId(R.id.home);
        //for item seletion in the bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add:
                        Intent add = new Intent(getApplicationContext(), ItemAdd.class);
                        startActivity(add);
                        finish();
                        return true;
                    case R.id.home:
                        return true;

                    case R.id.purchased:
                        Intent intent = new Intent(getApplicationContext(), MarkedItems.class);
                        startActivity(intent);
                        finish();
                        return true;

                    case R.id.log:
                        Intent settings = new Intent(getApplicationContext(), Login.class);
                        startActivity(settings);
                        finish();
                        return true;

                }
                return false;
            }
        });
    }

    private void search(String text) {
        Query searchQuery = databaseReference.orderByChild("itemName").startAt(text).endAt(text + "\ufBff");
        searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear();
//TODO MAKE SURE THAT IF NO RESULTS SOMETHING SHOLULD SHOW
                for (DataSnapshot searchInfo : dataSnapshot.getChildren()) {
                    ItemDetail productinfo = searchInfo.getValue(ItemDetail.class);
                    items.add(productinfo);

                }
                adapter = new ItemAdapter(getApplicationContext(), items,HomeScreen.this );
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //allows user to log out of the application
    public void logout(View v){
        Intent intentLogout = new Intent(HomeScreen.this, Login.class);
        FirebaseAuth.getInstance().signOut();
        startActivity(intentLogout);
        finish();
    }

    //on selecting a certain entry the entry details are sent to the entry details activity
    @Override
    public void onItemClicked(int pos) {
        Intent intent = new Intent(HomeScreen.this, ItemDetails.class);
        intent.putExtra("ID", items.get(pos).getItemId());
        intent.putExtra("Name", items.get(pos).getItemName());
        intent.putExtra("Price", items.get(pos).getItemPrice());
        intent.putExtra("Description",items.get(pos).getItemDescription());
        intent.putExtra("Date", items.get(pos).getItemDate());
        intent.putExtra("Latitude",items.get(pos).getItemLatitude());
        intent.putExtra("Longitude", items.get(pos).getItemLongitude());
        intent.putExtra("Image", items.get(pos).getItemImage());
        intent.putExtra("Address", items.get(pos).getItemAddress());
        startActivity(intent);
    }

}
