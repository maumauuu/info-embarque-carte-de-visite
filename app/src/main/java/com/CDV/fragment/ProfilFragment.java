package com.CDV.fragment;


import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.CDV.MainActivity;
import com.CDV.R;
import com.CDV.dataBase.Carte;
import com.CDV.dataBase.CarteDataSource;
import com.CDV.dataBase.Image;
import com.CDV.util.Code;
import com.CDV.util.RefreshEvent;

import java.io.File;
import java.util.List;

import de.greenrobot.event.EventBus;

public class ProfilFragment extends Fragment {

    private ImageView imgUser;
    private EditText editname;
    private EditText editprenom;
    private EditText editemail;
    private EditText editnumero;
    private EditText editcity;

    //Layout des boutons
    private LinearLayout layoutgenerer;
    private LinearLayout layoutcdv;
    private LinearLayout layoutSend;

    //Layout qui change
    private RelativeLayout cdv;
    private LinearLayout qr_code;
    private LinearLayout send_number;

    private ImageView image;
    private Code code;


    private CarteDataSource dataSource;

    private Button scan;
    private Button send;
    private String msg;
    private EditText send_num;

    private final int MODIFICATION = 10;


    public ProfilFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_profil, container, false);

        imgUser = (ImageView) view.findViewById(R.id.img);
        editname = (EditText) view.findViewById(R.id.editname);
        editprenom = (EditText) view.findViewById(R.id.editlastname);
        editemail = (EditText) view.findViewById(R.id.editemail);
        editnumero = (EditText) view.findViewById(R.id.editnumero);
        editcity = (EditText) view.findViewById(R.id.editcity);

        //Les layouts boutons
        layoutgenerer = (LinearLayout) view.findViewById(R.id.layoutgenerer);
        layoutcdv = (LinearLayout) view.findViewById(R.id.layoutcdv);
        layoutSend = (LinearLayout) view.findViewById(R.id.layoutSend);


        //Les layouts a remplacer
        cdv = (RelativeLayout) view.findViewById(R.id.cdv);
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
            editcity.setText(carte.getAddress()+" "+carte.getPostal()+" "+carte.getCity());
        }

        List<Image> images = dataSource.getAllImage();
        if (images.size() != 0) {
            Image image = images.get(images.size()-1);

            imgUser.setImageBitmap(BitmapFactory.decodeFile(image.getChemin()));

        }

        dataSource.close();

        cdv.setVisibility(LinearLayout.VISIBLE);
        qr_code.setVisibility(LinearLayout.INVISIBLE);
        send_number.setVisibility(LinearLayout.INVISIBLE);

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
                String data = editnumero.getText().toString();
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

                String chaine = editcity.getText().toString();
                String[] mChaine = chaine.split(",");
                String adresse = " ";
                String postal = " ";
                String city = " ";
                if(mChaine.length == 3){
                    adresse = mChaine[0];
                    postal = mChaine[1];
                    city = mChaine[2];
                }

                msg ="CDV" +"\n"+editprenom.getText().toString() + " "+editname.getText().toString()+"\n " +
                        editemail.getText().toString()+"\n " +adresse +" "+
                       postal +" "+ city;
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dataSource.open();

                if(!send_num.getText().toString().equals("")) {
                   /* if(dataSource.getAllImage().size() != 0) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.putExtra("address", send_num.getText().toString());
                        i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(dataSource.getAllImage().get(dataSource.getAllImage().size() - 1).getChemin())));
                        i.setType("image/*");
                        startActivity(i);
                    }*/
                    Toast.makeText(getActivity(), "SMS envoyé", Toast.LENGTH_SHORT).show();
                    SmsManager.getDefault().sendTextMessage(send_num.getText().toString(), null, msg, null, null);
                }else{
                    Toast.makeText(getActivity(), "Vous n'avez pas entré de numéro", Toast.LENGTH_SHORT);
                }
                dataSource.close();
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

                String chaine = editcity.getText().toString();
                String[] mChaine = chaine.split(",");
                String adresse = " ";
                String postal = " ";
                String city = " ";

                if(mChaine.length == 3){
                    adresse = mChaine[0];
                    postal = mChaine[1];
                    city = mChaine[2];
                }

                dataSource.createProfil(editname.getText().toString(), editprenom.getText().toString(), editemail.getText().toString(),
                        editnumero.getText().toString(),adresse,postal, city);

                dataSource.close();
                Toast.makeText(getActivity(), "Profil Sauvegardé", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivityForResult(intent, MODIFICATION);
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
