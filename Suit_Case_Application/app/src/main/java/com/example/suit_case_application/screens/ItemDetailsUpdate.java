package com.example.suit_case_application.screens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.suit_case_application.R;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ItemDetailsUpdate extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUST = 1;
    EditText Name1, Description1, entryDate, Price1;
    Button postButton, entryImageButton;
    ImageView Image;
    TextView address;

    ImageView cal;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    private FirebaseAuth mAuth;
    private Uri imageUri;
    private String entryID;
    private double latitude;
    private double longitude;
    private String image;

    Spinner spinner;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details_update);

        Intent intent = getIntent();
        longitude = intent.getDoubleExtra("Lon", 0);
        latitude = intent.getDoubleExtra("Lat", 0);

        Intent intentE = getIntent();
        entryID = intentE.getStringExtra("ID");
        String name = intentE.getStringExtra("Name");
        String price = intentE.getStringExtra("Price");
        String image = intentE.getStringExtra("Image");
        String description = intentE.getStringExtra("Description");
        String date = intentE.getStringExtra("Date");
        String entryaddress = intentE.getStringExtra("Location");

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Items").child(mAuth.getCurrentUser().getUid()).child(entryID);
        storageReference = FirebaseStorage.getInstance().getReference("Items").child(mAuth.getCurrentUser().getUid()).child(entryID);

        Image = findViewById(R.id.updateImageBtn);
        Name1 = findViewById(R.id.updateName);
        address =findViewById(R.id.location);
        Description1 = findViewById(R.id.updateDescription);
        entryDate = findViewById(R.id.update_Date);


        Price1 = findViewById(R.id.updatePrice);

        postButton = findViewById(R.id.updateBtn);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productlat = Double.toString(latitude);
                String productlon = Double.toString(longitude);
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(getApplicationContext(), "Still Uploading post", Toast.LENGTH_SHORT).show();

                } else {
                    processinsert();
                }
            }
        });

        //can tap on the image
        Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(ItemDetailsUpdate.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });


        Name1.setText(name);
        address.setText(entryaddress);
        entryDate.setText(date);
        Price1.setText(price);
       Description1.setText(description);
        Picasso.get().load(image).into(Image);



        //for item seletion in the bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        // get the current item activity selected
        //for item seletion in the bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add:
                        Intent add = new Intent(getApplicationContext(), ItemAdd.class);
                        startActivity(add);
                        finish();

                    case R.id.home:
                        Intent intent = new Intent(getApplicationContext(), ItemAdd.class);
                        startActivity(intent);
                        finish();

                    case R.id.log:
                        Intent settings = new Intent(getApplicationContext(), Login.class);
                        startActivity(settings);
                        finish();
                        return true;

                    case R.id.purchased:
                        Intent marked = new Intent(getApplicationContext(), MarkedItems.class);
                        startActivity(marked);
                        finish();
                        return true;

                }
                return false;
            }
        });

    }

    public void addlocation(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }



    private String getFileExtenstion(Uri imageUri) {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(this.getContentResolver().getType(imageUri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        imageUri = data.getData();
        Image.setImageURI(imageUri);

    }

    private void processinsert() {
        if (imageUri != null) {
            final StorageReference store = storageReference.child(System.currentTimeMillis() + "." + getFileExtenstion(imageUri));

            uploadTask = store.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    store.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String entryImage = uri.toString().trim();
                            Map<String, Object> map = new HashMap<>();
                            map.put("itemImage",entryImage);
                            map.put("itemName", Name1.getText().toString());
                            map.put("itemPrice", Price1.getText().toString());
                            map.put("itemDescription", Description1.getText().toString());
                            map.put("itemDate", entryDate.getText().toString());
                            map.put("itemLatitude", latitude);
                            map.put("itemLongitude", longitude);
                            databaseReference.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), "Could not insert", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        }
                    });
                }
            });
        }
        else {
            Map<String, Object> map = new HashMap<>();
            map.put("itemName", Name1.getText().toString());
            map.put("itemDescription", Description1.getText().toString());
            map.put("itemDate", entryDate.getText().toString());
            map.put("itemPrice", Price1.getText().toString());
            map.put("itemLatitude", latitude);
            map.put("itemLongitude", longitude);
            databaseReference.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Update Successful", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Could not insert", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }
}