package com.mugheesnadeem.i160029;

import android.net.Uri;

class ContactModel {
    String name,phno;
    int id ;

    Uri image ;

    public ContactModel(int id, String name, String phno, Uri image) {
        this.id = id;
        this.name = name;
        this.phno = phno;
        this.image = image;
    }

    public ContactModel()
    {

    }


    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getPhno() {
        return phno;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }
}
