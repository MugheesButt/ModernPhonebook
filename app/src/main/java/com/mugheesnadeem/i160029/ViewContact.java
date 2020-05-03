package com.mugheesnadeem.i160029;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewContact extends AppCompatActivity {

    TextView Name , Phone ;
    ImageView img ;
    Button Direction ;
    Uri ImgPath ;
    ContactModel cm ;
    LocationModel lm ;
    DBHelper dbHelp ;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        Name = findViewById(R.id.NameText);
        Phone = findViewById(R.id.PhoneText);
        img = findViewById(R.id.ViewImage);
        Direction = findViewById(R.id.dir);
        dbHelp = new DBHelper(this);

        cm = new ContactModel();

        id = getIntent().getIntExtra("id", id) ;
        if (id > 0)
            cm = dbHelp.getContact(id) ;

        Name.setText(cm.getName());
        Phone.setText(cm.getPhno());
        ImgPath = cm.getImage();

        img.setImageURI(ImgPath);

        lm = new LocationModel();
        lm = dbHelp.getLocation(id);


        Direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ViewContact.this , DirectionsActivity.class);
                intent.putExtra("lon" , lm.getLon());
                intent.putExtra("lat", lm.getLat());
                startActivity(intent);

            }
        });

    }
}
