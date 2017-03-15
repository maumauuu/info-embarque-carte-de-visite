package com.example.thomas.visitcard;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class ContactActivity extends ListActivity {

    CarteDataSource datasource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        datasource = new CarteDataSource(this);
        datasource.open();

        List<Carte> cartes = datasource.getAllCarte();

        CarteAdapter adapter = new CarteAdapter(ContactActivity.this, cartes);

        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id){
        super.onListItemClick(l, v, position, id);

        Carte carte = (Carte) getListAdapter().getItem(position);

        Intent intent = new Intent(ContactActivity.this, ContactItemActivity.class);

        intent.putExtra("num", carte.getNumero());
        intent.putExtra("email", carte.getEmail());

        startActivity(intent);

    }
}
