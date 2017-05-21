package com.CDV;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class Pause extends Activity {

    private ArrayList<Button> listePause = new ArrayList<Button>();

    private Button Read,Skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause);

        Read = (Button) findViewById(R.id.Read);
        Skip = (Button) findViewById(R.id.Skip);

        listePause.add(Read);
        listePause.add(Skip);

        for (int b = 0; b < listePause.size(); b++) {
            Button btn = listePause.get(b);
            btn.setOnClickListener(new View.OnClickListener() {
                                       public void onClick(View v) {
                                           ActionBouton((Button) findViewById(v.getId()));
                                       }
                                   }
            );}
    }


    //Lire renvoie 1, ne pas lire renvoie 0
    public void ActionBouton(Button button) {
        if (button == Read) {
            this.setResult(1);
            this.finish();

        }
        if (button == Skip) {
            this.setResult(0);
            this.finish();
        }
    }
}
