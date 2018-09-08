package mobilapp.kutatok.androidgraph;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import mobilapp.kutatok.androidgraph.games.Discovery;
import mobilapp.kutatok.androidgraph.games.Pathfinding;
import mobilapp.kutatok.androidgraph.games.SpanningTree1;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout algoritmusInditas = (LinearLayout) findViewById(R.id.startAlgorithm);
        final Intent intentAlgoritmus = new Intent(this,AlgoritmusLista.class);
        algoritmusInditas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentAlgoritmus);
            }
        });

        LinearLayout jatekInditas = (LinearLayout) findViewById(R.id.startGames);
        final Intent intentJatek = new Intent(this,JatekValaszto.class);
        jatekInditas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentJatek);
            }
        });

        LinearLayout eredmenyKijelzoInditas = (LinearLayout) findViewById(R.id.startLeaderboard);
        final Intent intentLeaderboard = new Intent(this,EredmenyLista.class);
        eredmenyKijelzoInditas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentLeaderboard);
            }
        });

        LinearLayout infoInditas = (LinearLayout) findViewById(R.id.startAboutPage);
        final Intent intentInfo = new Intent(this,Maininfo.class);
        infoInditas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentInfo);
            }
        });

        ImageView logo=(ImageView)findViewById(R.id.logo);
        logo.bringToFront();


    }
}
