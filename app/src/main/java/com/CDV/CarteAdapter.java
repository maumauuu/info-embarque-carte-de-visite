package com.CDV;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;


public class CarteAdapter extends ArrayAdapter<Carte> {

    public CarteAdapter(Context context, List<Carte> cartes) {
        super(context, 0, cartes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contacts,parent, false);
        }

        CarteViewHolder viewHolder = (CarteViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new CarteViewHolder();
            viewHolder.textName = (TextView) convertView.findViewById(R.id.textname);
            viewHolder.textFullname = (TextView) convertView.findViewById(R.id.textfullname);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        Carte carte = getItem(position);
        viewHolder.textName.setText(carte.getName()+" ");
        viewHolder.textFullname.setText(carte.getFullname());

        return convertView;
    }

    private class CarteViewHolder{
        public TextView textName;
        public TextView textFullname;
        public CheckBox checkBox;
    }
}
