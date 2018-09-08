package mobilapp.kutatok.androidgraph.box;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mobilapp.kutatok.androidgraph.R;
import mobilapp.kutatok.androidgraph.games.SpanningTree1;

/**
 * A simple {@link Fragment} subclass.
 */
public class leiras_fragment extends Fragment {

    int index;
    Integer[] leirasok = new Integer[]{R.string.leiras_szelessegi,R.string.leiras_melysegi,R.string.leiras_topologiai,R.string.leiras_dijkstra,R.string.leiras_prim,R.string.leiras_kruskal};
    int [] leirasKepek=new int[]{R.drawable.descr2,R.drawable.descr1,R.drawable.descr3,R.drawable.descr5,R.drawable.descr4,R.drawable.descr6};
    String[] leirasNevek=new String[]{"Szélességi bejárás", "Mélységi bejárás", "Topológiai rendezés","Dijkstra algoritmusa","Prim algoritmusa","Kruskal algoritmusa"};
    LinearLayout spanningTreeButton;

    public leiras_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        RelativeLayout l1 = (RelativeLayout) inflater.inflate(R.layout.fragment_leiras_fragment,container,false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            index = bundle.getInt("index");
        }
        TextView leirasText = (TextView)l1.findViewById(R.id.leirasText);
        leirasText.setText(leirasok[index]);

        ImageView leirasKep=(ImageView)l1.findViewById(R.id.descriptionImage);
        leirasKep.setImageResource(leirasKepek[index]);


        TextView leirasCim=(TextView)l1.findViewById(R.id.descriptionTitle);
        leirasCim.setText(leirasNevek[index]);

        if(index==4 || index==5){
            spanningTreeButton=(LinearLayout)l1.findViewById(R.id.spanningTree);
            spanningTreeButton.setVisibility(View.VISIBLE);
            spanningTreeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent spanningtree=new Intent(getContext(), SpanningTree1.class);
                    startActivity(spanningtree);
                }
            });
        }

        return l1;
    }

}
