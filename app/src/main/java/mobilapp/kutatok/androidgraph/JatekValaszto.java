package mobilapp.kutatok.androidgraph;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import mobilapp.kutatok.androidgraph.games.Discovery;
import mobilapp.kutatok.androidgraph.games.Pathfinding;

public class JatekValaszto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jatek_valaszto);

        Button discoveryStart=(Button) findViewById(R.id.discoveryStart);
        final Intent intentDiscovery=new Intent(this, Discovery.class);
        discoveryStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentDiscovery);
            }
        });

        Button pathFindingStart=(Button) findViewById(R.id.pathFindingStart);
        final Intent intentPathFinding=new Intent(this, Pathfinding.class);
        pathFindingStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentPathFinding);
            }
        });
    }
}
