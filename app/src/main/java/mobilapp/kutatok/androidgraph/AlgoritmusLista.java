package mobilapp.kutatok.androidgraph;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;

import java.util.ArrayList;
import java.util.List;

import mobilapp.kutatok.androidgraph.box.kezdokep_alg;

public class AlgoritmusLista extends AppCompatActivity {

    List<AlgoritmusListaElem> listaElem = new ArrayList<AlgoritmusListaElem>();
    HorizontalInfiniteCycleViewPager coverFlow;
    AlgoritmusListaAdapter listaAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_algoritmus_lista);

        listaElem.add(new AlgoritmusListaElem("Szélességi bejárás", "A szélességi bejárás alapelve, hogy elsőként mindig a kiindulóponthoz legközelebbi csúcsokat dolgozza fel.",R.drawable.alg2));
        listaElem.add(new AlgoritmusListaElem("Mélységi bejárás", "A mélységi bejárás ugyanazokat a csúcsokat fogja elérni, mint a szélességi, azonban más sorrendben.",R.drawable.alg1));
        listaElem.add(new AlgoritmusListaElem("Topológiai rendezés", "A gráf csúcsainak akkor és csak akkor van topologikus sorrendje, amiben minden él előrefelé vezet, ha irányított körmentes gráf.",R.drawable.alg3));
        listaElem.add(new AlgoritmusListaElem("Dijkstra algoritmusa", "Két csúcs közti legrövidebb út keresésének gyakorlati problémájára ad megoldást Dijkstra algoritmusa.",R.drawable.alg5));
        listaElem.add(new AlgoritmusListaElem("Prim algoritmusa", "Egy összefüggő súlyozott gráf minimális feszítőfáját határozza meg mohó stratégia segítségével. Az algoritmus lépésenként építi fel a minimális feszítőfát, minden lépésben egy csúcsot adva hozzá.",R.drawable.alg4));
        listaElem.add(new AlgoritmusListaElem("Kruskal algoritmusa", "Az algoritmus egy súlyozott gráfokat feldolgozó mohó algoritmus, amely az éleket súlyuk szerint növekvő sorrendbe rendezi, és sorra veszi, hogy melyeket veheti be a megoldásba.",R.drawable.alg6));

        LinearLayout graph_altalanos = (LinearLayout)findViewById(R.id.graph_altalanos);
        graph_altalanos.bringToFront();
        final Intent intentGraph = new Intent(this,Graph_altalanos.class);
        graph_altalanos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentGraph);
            }
        });
        coverFlow=(HorizontalInfiniteCycleViewPager) findViewById(R.id.coverflow);
        listaAdapter=new AlgoritmusListaAdapter(getApplicationContext(),listaElem);
        coverFlow.setAdapter(listaAdapter);
    }
}
