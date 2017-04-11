package com.CDV.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.CDV.dataBase.Carte;
import com.CDV.dataBase.CarteDataSource;
import com.CDV.dataBase.ContactItemActivity;
import com.CDV.R;
import com.CDV.adapter.CarteAdapter;

import java.util.List;


public class ContactFragment extends ListFragment {

    private CarteDataSource datasource;


    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        datasource = new CarteDataSource(getActivity());
        datasource.open();

        List<Carte> cartes = datasource.getAllCarte();

        CarteAdapter adapter = new CarteAdapter(getActivity(), cartes);

        setListAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.activity_contact, container, false);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id){
        super.onListItemClick(l, v, position, id);

        Carte carte = (Carte) getListAdapter().getItem(position);

        Intent intent = new Intent(getActivity(), ContactItemActivity.class);

        intent.putExtra("name", carte.getName());
        intent.putExtra("fullname", carte.getFullname());
        intent.putExtra("email", carte.getEmail());
        intent.putExtra("num", carte.getNumero());
        intent.putExtra("address", carte.getAddress());
        intent.putExtra("city", carte.getCity());
        intent.putExtra("postal", carte.getPostal());

        startActivity(intent);

    }
}
