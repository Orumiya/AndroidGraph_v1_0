package mobilapp.kutatok.androidgraph;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

public class EredmenyLista extends AppCompatActivity {

    ListView pathfinding, discovery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eredmeny_lista);

        pathfinding=(ListView)findViewById(R.id.pathfindingResultList);
        discovery=(ListView)findViewById(R.id.discoveryResultList);

        DbHelper dbHelper=new DbHelper(this);
        List<Ranking> listRanking=dbHelper.getRanking(1);
        if(listRanking.size()>0){

            EredmenyListAdapter adapter=new EredmenyListAdapter(this,listRanking,1);
            pathfinding.setAdapter(adapter);

        }

        List<Ranking> listRanking2=dbHelper.getRanking(2);
        if(listRanking.size()>0){

            EredmenyListAdapter adapter2=new EredmenyListAdapter(this,listRanking2,2);
            discovery.setAdapter(adapter2);

        }


    }
}
