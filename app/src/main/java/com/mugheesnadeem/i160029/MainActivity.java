package com.mugheesnadeem.i160029;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MainActivity extends AppCompatActivity {
    RecyclerView rv;
    ArrayList<ContactModel> MainContactList = new ArrayList<>();
    RVAdapter adapter;
    Button add_but ;
    public static final int TEXT_REQUEST = 1;
    DBHelper dbHelper ;
    SQLiteDatabase db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add_but = (Button)findViewById(R.id.AddButton) ;
        rv = findViewById(R.id.rv);
        dbHelper = new DBHelper(MainActivity.this);
        db = dbHelper.getReadableDatabase();


        MainContactList = dbHelper.getAllContacts();

        adapter = new RVAdapter(MainContactList,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        add_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this , AddContact.class);
                startActivityForResult(intent,TEXT_REQUEST);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        MainContactList = dbHelper.getAllContacts();
        adapter = new RVAdapter(MainContactList, this);
        rv.setAdapter(adapter);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TEXT_REQUEST)
        {
            if (resultCode == RESULT_OK)
            {

                //String rep_name = data.getStringExtra("name");
                //String rep_phone = data.getStringExtra("phone") ;
                //String rep_address = data.getStringExtra("address") ;
                //Uri rep_path = Uri.parse(data.getStringExtra("imageuri")) ;
                //MainContactList.add(new ContactModel(rep_name,rep_address,rep_phone));

            }
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}
