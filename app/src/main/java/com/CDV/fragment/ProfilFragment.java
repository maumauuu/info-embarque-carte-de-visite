package com.CDV.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.CDV.CodeActivity;
import com.CDV.MainActivity;
import com.CDV.R;
import com.CDV.dataBase.Carte;
import com.CDV.dataBase.CarteDataSource;

import java.util.List;

public class ProfilFragment extends Fragment {

    private EditText editname;
    private EditText editprenom;
    private EditText editemail;
    private EditText editnumero;
    private EditText editadresse;
    private EditText editpostal;
    private EditText editcity;
    private LinearLayout layoutgenerer;
    private LinearLayout layoutregister;

    private CarteDataSource dataSource;


    public ProfilFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_profil, container, false);

        editname = (EditText) view.findViewById(R.id.editname);
        editprenom = (EditText) view.findViewById(R.id.editlastname);
        editemail = (EditText) view.findViewById(R.id.editemail);
        editnumero = (EditText) view.findViewById(R.id.editnumero);
        editadresse = (EditText) view.findViewById(R.id.editadresse);
        editpostal = (EditText) view.findViewById(R.id.editpostal);
        editcity = (EditText) view.findViewById(R.id.editcity);

        layoutgenerer = (LinearLayout) view.findViewById(R.id.layoutgenerer);
        layoutregister = (LinearLayout) view.findViewById(R.id.layoutregister);

        dataSource = new CarteDataSource(getActivity());

        dataSource.open();
        List<Carte> cartes = dataSource.getAllProfil();
        if (cartes.size() != 0) {
            Carte carte = cartes.get(cartes.size()-1);
            editname.setText(carte.getName());
            editprenom.setText(carte.getFullname());
            editemail.setText(carte.getEmail());
            editnumero.setText(carte.getNumero());
            editadresse.setText(carte.getAddress()+", ");
            editpostal.setText(carte.getPostal()+", ");
            editcity.setText(carte.getCity());
        }

        dataSource.close();

        layoutgenerer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = editname.getText().toString()+";"+editprenom.getText().toString()+";"+editemail.getText().toString()+";"+
                        editnumero.getText().toString()+";"+editadresse.getText().toString()+";"+editpostal.getText().toString()+";"+editcity.getText().toString();

                Intent intent = new Intent(getActivity(), CodeActivity.class);
                intent.putExtra("data", data);
                startActivity(intent);
            }
        });

        layoutregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSource.open();

                dataSource.createProfil(editname.getText().toString(), editprenom.getText().toString(), editemail.getText().toString(),
                        editnumero.getText().toString(), editadresse.getText().toString(), editpostal.getText().toString(), editcity.getText().toString());

                dataSource.close();
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}
