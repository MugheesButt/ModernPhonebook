package com.mugheesnadeem.i160029;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;

public class ModifyContact extends AppCompatActivity {

    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    private CarmenFeature home, work;

    EditText Name, Phn;
    Button Loc , Update , Del;
    ImageView img ;
    Uri imgpath ;
    int ConId;
    DBHelper dbHelper ;
    SQLiteDatabase db ;
    LocationModel lm ;
    ContactModel cm ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_profile);
        dbHelper = new DBHelper(ModifyContact.this);
        db = dbHelper.getReadableDatabase();

        Name = findViewById(R.id.nameModify);
        Phn = findViewById(R.id.phoneModify);
        Loc = findViewById(R.id.fab_location_searchModify);
        img = findViewById(R.id.ImgButtonEdit);
        Update = findViewById(R.id.buttonupdateModify);
        Del = findViewById(R.id.buttondelModify);

        ConId = getIntent().getIntExtra("id",ConId);
        cm = new ContactModel();
        lm = new LocationModel();

        cm = dbHelper.getContact(ConId);
        lm = dbHelper.getLocation(ConId);

        Name.setText(cm.getName());
        Phn.setText(cm.getPhno());
        Loc.setText(lm.getName());
        imgpath = cm.getImage();

        img.setImageURI(imgpath);
        initSearchFab();
        addUserLocations();

        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                dbHelper.getWritableDatabase();
                cm.setName(Name.getText().toString());
                cm.setPhno(Phn.getText().toString());
                cm.setId(ConId);
                lm.setId(ConId);


                boolean IsUpdated , IsLocUpdated;
                //EDIT ADDRESS
                IsUpdated = dbHelper.updateContact(cm);
                IsLocUpdated = dbHelper.updateLocation(lm);
                if (IsUpdated && IsLocUpdated) {
                    Toast.makeText(ModifyContact.this, "Data Updated", Toast.LENGTH_SHORT).show();
                    finish();

                } else
                    Toast.makeText(ModifyContact.this, "Data Not Updated", Toast.LENGTH_SHORT).show();



            }
        });

        Del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = dbHelper.getWritableDatabase();

                boolean IsDeleted;
                IsDeleted = dbHelper.deleteContact(ConId);

                if (IsDeleted) {
                    Toast.makeText(ModifyContact.this, "Data Deleted", Toast.LENGTH_SHORT).show();
                    finish();

                } else
                    Toast.makeText(ModifyContact.this, "Data Not Deleted", Toast.LENGTH_SHORT).show();
            }
        });



        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED) {

                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {

                        pickimagefromgallery();

                    }
                } else {

                    pickimagefromgallery();
                }

            }

        });
    }

    private void initSearchFab() {
        Loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken("sk.eyJ1IjoibXVnaGVlczk3IiwiYSI6ImNrMHY4ZWRqYjBvcm0zZHBrZWZkbHM5NWQifQ.JMn_JttRPhF5Qrbfh7Sq1g")
                        .placeOptions(PlaceOptions.builder()
                                .backgroundColor(Color.parseColor("#EEEEEE"))
                                .limit(10)
                                .addInjectedFeature(home)
                                .addInjectedFeature(work)
                                .build(PlaceOptions.MODE_CARDS))
                        .build(ModifyContact.this);
                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
            }
        });
    }

    private void addUserLocations() {
        home = CarmenFeature.builder().text("Mapbox SF Office")
                .geometry(Point.fromLngLat(-122.3964485, 37.7912561))
                .placeName("50 Beale St, San Francisco, CA")
                .id("mapbox-sf")
                .properties(new JsonObject())
                .build();

        work = CarmenFeature.builder().text("Mapbox DC Office")
                .placeName("740 15th Street NW, Washington DC")
                .geometry(Point.fromLngLat(-77.0338348, 38.899750))
                .id("mapbox-dc")
                .properties(new JsonObject())
                .build();
    }


    private void pickimagefromgallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        //img.setImageURI(ImgPath);
        intent.setType("image/*");
        //startActivity(intent);
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickimagefromgallery();
                } else {
                    Toast.makeText(this, "Permissions denied...!", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imgpath = data.getData();
            img.setImageURI(imgpath);
            cm.setImage(imgpath);
        }

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {

            // Retrieve selected location's CarmenFeature
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);
            lm.setName(selectedCarmenFeature.placeName());
            String temp = String.valueOf(((Point) selectedCarmenFeature.geometry()).latitude());
            lm.setLat(Double.valueOf(temp));
            temp = String.valueOf(((Point) selectedCarmenFeature.geometry()).longitude());
            lm.setLon(Double.valueOf(temp));
            Loc.setText(selectedCarmenFeature.placeName());

        }
    }

    // Add the mapView lifecycle to the activity's lifecycle methods
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
