package com.example.suit_case_application.screens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.suit_case_application.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ItemAdd extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUST = 1;
    TextView address;
    EditText itmName,itmPrice, itmDescription, dateText, productPrice1;
    Button postButton;
    ImageView itmImage, image_One;
    FirebaseUser firebaseUser;

    DatabaseReference databaseReference;
    StorageReference storageReference;
    private FirebaseAuth mAuth;
    private Uri imageUri;
    ImageView cal;
    private int mDate, mMonth, mYear;
    private StorageTask uploadTask;
    private TextView dateTimeDisplay;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_add);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date = dateFormat.format(calendar.getTime());

        Intent intent = getIntent();
        double longitude = intent.getDoubleExtra("lon", 0);
        double latitude = intent.getDoubleExtra("lat", 0);
        String itmaddress = intent.getStringExtra("address");
        //Database initialisation
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Items").child(mAuth.getCurrentUser().getUid());
        storageReference = FirebaseStorage.getInstance().getReference("Items").child(mAuth.getCurrentUser().getUid());
        //initialisation of widgets
       itmImage = findViewById(R.id.entryImage1);
        address = findViewById(R.id.location);
       itmName = findViewById(R.id.entryName);
        itmPrice = findViewById(R.id.entryPrice);
        itmDescription = findViewById(R.id.entryDescription);
        address.setText(itmaddress);
//        button
        postButton = findViewById(R.id.addEntry);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productlat = Double.toString(latitude);
                String productlon = Double.toString(longitude);
                String address = itmaddress;
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(getApplicationContext(), "Still Uploading wait a sec", Toast.LENGTH_SHORT).show();

                } else {
                    postToDatabase(productlat, productlon,address);
                }
            }
        });

        //can tap on the image
       itmImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                ImagePicker.with(ItemAdd.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.add);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.home:
                        Intent home = new Intent(getApplicationContext(), HomeScreen.class);
                        startActivity(home);
                        finish();
                        return true;
                    case R.id.add:
                        return true;

                    case R.id.log:
                        Intent settings = new Intent(getApplicationContext(), Login.class);
                        startActivity(settings);
                        finish();
                        return true;

                    case R.id.purchased:
                        Intent intent02 = new Intent(getApplicationContext(), MarkedItems.class);
                        startActivity(intent02);
                        finish();
                        return true;

                }
                return false;
            }
        });
    }


    public void addlocation(View view) {
//        if (ActivityCompat.checkSelfPermission(JournalEntry.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("Name", itmName.getText().toString().trim());
        intent.putExtra("Price", itmPrice.getText().toString().trim());
        intent.putExtra("Description", itmDescription.getText().toString().trim());
        startActivity(intent);

    }

    private void postToDatabase(String productlat, String productlon,String address) {
        String latitude = productlat;
        String longitude = productlon;
        String entryaddress = address;

        if (imageUri != null) {
            final StorageReference store = storageReference.child(System.currentTimeMillis() + "." + getFileExtenstion(imageUri));

            uploadTask = store.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    store.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String entryImage = uri.toString().trim();
                            double lat = Double.parseDouble(latitude.trim());
                            double lon = Double.parseDouble(longitude.trim());
                            String uniquekey = databaseReference.push().getKey();
                            String entryDate = date;
                            String entryAddress = entryaddress;
                            String entryname = itmName.getText().toString().trim();
                            String entryprice = itmPrice.getText().toString().trim();
                            String entrydescription = itmDescription.getText().toString().trim();


                            HashMap<String, Object> map = new HashMap<>();
                            map.put("itemId", uniquekey);
                            map.put("itemLatitude", lat);
                            map.put("itemLongitude", lon);
                            map.put("itemDate", entryDate);
                            map.put("itemAddress", entryAddress);
                            map.put("itemDescription", entrydescription);
                            map.put("itemImage", entryImage);
                            map.put("itemName", entryname);
                            map.put("itemPrice", entryprice);


                            databaseReference.child(uniquekey).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "Posted item", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    });
                }
            });

        }
    }

    private String getFileExtenstion(Uri imageUri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(imageUri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        imageUri = data.getData();
       itmImage.setImageURI(imageUri);

    }
}
