package com.mugheesnadeem.i160029;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class RVAdapter extends RecyclerView.Adapter<RVAdapter.RVViewHolder> {

    DBHelper dbHelp ;
    SQLiteDatabase db ;

    ArrayList<ContactModel> ContactsList;
    Context c;
    int counter ;

    public RVAdapter(ArrayList<ContactModel> ContactsList, Context c) {
        this.ContactsList = ContactsList;
        this.c = c;

        counter = ContactsList.size();
    }


    @NonNull
    @Override
    public RVAdapter.RVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        dbHelp = new DBHelper(parent.getContext());
        db = dbHelp.getWritableDatabase();
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        return new RVViewHolder(row);
    }


    @Override
    public void onBindViewHolder(@NonNull final RVAdapter.RVViewHolder holder, final int position){

        holder.name.setText(ContactsList.get(position).getName());
        holder.phno.setText(ContactsList.get(position).getPhno());
        holder.id = ContactsList.get(position).getId();
        holder.image.setImageURI(ContactsList.get(position).getImage());

        LocationModel lm = new LocationModel();
        lm = dbHelp.getLocation(holder.id);
        holder.address.setText(lm.getName());


        holder.RowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(c , ViewContact.class);
                intent.putExtra("id" , holder.id);
                c.startActivity(intent);
            }
        });


        holder.RowLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                LocationModel lm = new LocationModel();
                lm = dbHelp.getLocation(holder.id);

                Intent intent = new Intent(c , ModifyContact.class);
                intent.putExtra("id" , ContactsList.get(position).getId());
                c.startActivity(intent);
                return true;
            }
        });
    }




    @Override
    public int getItemCount() {

        return counter;
    }


        public class RVViewHolder extends RecyclerView.ViewHolder{
            TextView name,phno,address;
            int id;
            LinearLayout RowLayout ;
            ImageView image;

        public RVViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            phno = itemView.findViewById(R.id.phno);
            address = itemView.findViewById(R.id.addrss);
            RowLayout = itemView.findViewById(R.id.RowLayout);
            image = itemView.findViewById(R.id.RowImg);

        }

    }

}
