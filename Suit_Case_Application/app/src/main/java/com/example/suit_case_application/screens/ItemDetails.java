package com.example.suit_case_application.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suit_case_application.R;
import com.example.suit_case_application.screens.HomeScreen;
import com.example.suit_case_application.screens.ItemAddress;
import com.example.suit_case_application.screens.ItemDetailsUpdate;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ItemDetails extends AppCompatActivity {
    TextView nam, pric, dat, des, address;
    ImageView ima;
    ImageButton location, edit, delete, share;
    Button purchase;
    DatabaseReference databaseReference;
    DatabaseReference databaseReference1;
    FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        databaseReference1 = FirebaseDatabase.getInstance().getReference("PurchasedItems").child(mAuth.getCurrentUser().getUid());
        databaseReference = FirebaseDatabase.getInstance().getReference("Items").child(mAuth.getCurrentUser().getUid());
        storageReference = FirebaseStorage.getInstance().getReference("Items").child(mAuth.getCurrentUser().getUid());
        location = findViewById(R.id.locationButton);
        edit = findViewById(R.id.editButton);
        delete = findViewById(R.id.deleteButton);
        purchase = findViewById(R.id.purchaseButton);
        share = findViewById(R.id.shareButton);
        address = findViewById(R.id.iaddress);
        nam = findViewById(R.id.iName);
        pric = findViewById(R.id.iprice);
        ima = findViewById(R.id.iimage);
        dat = findViewById(R.id.iDate);
        des = findViewById(R.id.idescription);
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Deleting Item");

        Intent intent = getIntent();
        String entryId = intent.getStringExtra("ID");
        String name = intent.getStringExtra("Name");
        String price = intent.getStringExtra("Price");
        String entryaddress = intent.getStringExtra("Address");
        String image = intent.getStringExtra("Image");
        String description = intent.getStringExtra("Description");
        String date = intent.getStringExtra("Date");
        Double latitude = intent.getDoubleExtra("Latitude", 0.0);
        Double longitude = intent.getDoubleExtra("Longitude", 0.0);

        nam.setText(name);
        dat.setText(date);
        pric.setText(price);
        des.setText(description);
        address.setText(entryaddress);
        Picasso.get().load(image).into(ima);

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ItemAddress.class);
                i.putExtra("Latitude", latitude);
                i.putExtra("Longitude", longitude);
                i.putExtra("name", name);
                startActivity(i);

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                                        databaseReference.child(entryId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getApplicationContext(), "Succefully Deleted", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                                                startActivity(intent);
                                            }
                                        });
                            }
        });
        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                                HashMap<String, Object> map = new HashMap<>();
                                map.put("itemId", entryId);
                                map.put("itemLatitude", latitude);
                                map.put("itemLongitude", longitude);
                                map.put("itemDate", date);
                                map.put("itemPrice", price);
                                map.put("itemDescription", description);
                                map.put("itemImage", image);
                                map.put("itemName", name);

                                databaseReference1.child(entryId).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        databaseReference.child(entryId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(getApplicationContext(), "Item marked as purchased ", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });

                            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ItemDetails.this, ItemDetailsUpdate.class);
                intent.putExtra("ID", entryId);
                intent.putExtra("Name", name);
                intent.putExtra("Price", price);
                intent.putExtra("Image", image);
                intent.putExtra("Location", entryaddress);
                intent.putExtra("Description", description);
                intent.putExtra("Date", date);
                startActivity(intent);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, " Hie please buy this product for me  " + "Product Name : "+ name + "Product Price: " + price +  "Product Location :" + entryaddress);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, "Purchase Suitcase Item");
                startActivity(shareIntent);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
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
                        Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                        startActivity(intent);
                        finish();
                        return true;
                    case R.id.log:
                        Intent settings = new Intent(getApplicationContext(), Login.class);
                        startActivity(settings);
                        finish();
                        return true;

                    case R.id.purchased:
                        Intent set = new Intent(getApplicationContext(), MarkedItems.class);
                        startActivity(set);
                        finish();
                        return true;

                }
                return false;
            }
        });

    }
}