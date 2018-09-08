package mobilapp.kutatok.androidgraph.box;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import mobilapp.kutatok.androidgraph.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class pszeudo_fragment extends Fragment {

    int index;
    String[] pszeudok = new String[]{"file:///android_asset/pszeudo_szelessegi.htm","file:///android_asset/pszeudo_melysegi.htm","file:///android_asset/pszeudo_topologiai.htm","file:///android_asset/pszeudo_dijkstra.htm","file:///android_asset/pszeudo_prim.htm","file:///android_asset/pszeudo_kruskal.htm"};
    public pszeudo_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FrameLayout l1 = (FrameLayout)inflater.inflate(R.layout.fragment_pszeudo_fragment,container,false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            index = bundle.getInt("index");
        }
        WebView pszeudoText = (WebView) l1.findViewById(R.id.pszeudoText);
        pszeudoText.loadUrl(pszeudok[index]);

        return l1;
    }

}
