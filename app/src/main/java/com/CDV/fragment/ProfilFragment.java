package com.CDV.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.CDV.R;
import com.CDV.dataBase.Carte;
import com.CDV.dataBase.CarteDataSource;
import com.CDV.util.Code;
import com.CDV.util.RefreshEvent;

import java.util.List;

import de.greenrobot.event.EventBus;

public class ProfilFragment extends Fragment {

    private EditText editname;
    private EditText editprenom;
    private EditText editemail;
    private EditText editnumero;
    private EditText editadresse;
    private EditText editpostal;
    private EditText editcity;

    //Layout des boutons
    private LinearLayout layoutgenerer;
    private LinearLayout layoutcdv;
    private LinearLayout layoutSend;

    //Layout qui change
    private LinearLayout cdv;
    private LinearLayout qr_code;
    private LinearLayout send_number;

    private ImageView image;
    private Code code;


    private CarteDataSource dataSource;

    private Button scan;
    private Button send;
    private String msg;
    private EditText send_num;

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

        //Les layouts boutons
        layoutgenerer = (LinearLayout) view.findViewById(R.id.layoutgenerer);
        layoutcdv = (LinearLayout) view.findViewById(R.id.layoutcdv);
        layoutSend = (LinearLayout) view.findViewById(R.id.layoutSend);


        //Les layouts a remplacer
        cdv = (LinearLayout) view.findViewById(R.id.cdv);
        qr_code = (LinearLayout) view.findViewById(R.id.qr_code);
        send_number = (LinearLayout) view.findViewById(R.id.send_number);

        scan = (Button) view.findViewById(R.id.Scan);
        send = (Button) view.findViewById(R.id.Send);
        send_num = (EditText) view.findViewById(R.id.phone);


        dataSource = new CarteDataSource(getActivity());

        dataSource.open();
        List<Carte> cartes = dataSource.getAllProfil();
        if (cartes.size() != 0) {
            Carte carte = cartes.get(cartes.size()-1);
            editname.setText(carte.getName());
            editprenom.setText(carte.getFullname());
            editemail.setText(carte.getEmail());
            editnumero.setText(carte.getNumero());
            editadresse.setText(carte.getAddress());
            editpostal.setText(carte.getPostal());
            editcity.setText(carte.getCity());
        }

        dataSource.close();


        setHasOptionsMenu(true);

        layoutcdv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cdv.setVisibility(LinearLayout.VISIBLE);
                qr_code.setVisibility(LinearLayout.INVISIBLE);
                send_number.setVisibility(LinearLayout.INVISIBLE);
            }
        });


        layoutgenerer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qr_code.setVisibility(LinearLayout.VISIBLE);
                cdv.setVisibility(LinearLayout.INVISIBLE);
                send_number.setVisibility(LinearLayout.INVISIBLE);

                image = (ImageView) getActivity().findViewById(R.id.imageView);
                String data =editnumero.getText().toString();
                code = new Code(data);
                image.setImageBitmap(code.dataToBitmap());

            }
        });

        layoutSend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                send_number.setVisibility(LinearLayout.VISIBLE);
                cdv.setVisibility(LinearLayout.INVISIBLE);
                qr_code.setVisibility(LinearLayout.INVISIBLE);

                msg ="CDV" +"\n"+editprenom.getText().toString() + " "+editname.getText().toString()+"\n " +
                        editemail.getText().toString()+"\n " +editadresse.getText().toString() +" "+
                        editpostal.getText().toString() +" "+ editcity.getText().toString();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SmsManager.getDefault().sendTextMessage(send_num.getText().toString(), null, msg, null, null);
                Toast.makeText(getActivity(), "SMS envoyé", Toast.LENGTH_SHORT).show();
            }
        });



        // Inflate the layout for this fragment
        return view;
    }




    //gestion du menu de settings
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // pour sauvegarder le profil
            case R.id.save_profil:
                dataSource.open();

                dataSource.createProfil(editname.getText().toString(), editprenom.getText().toString(), editemail.getText().toString(),
                        editnumero.getText().toString(), editadresse.getText().toString(), editpostal.getText().toString(), editcity.getText().toString());

                dataSource.close();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    public void onEvent(RefreshEvent event){
        send_num.setText(event.getC());
    }


}
