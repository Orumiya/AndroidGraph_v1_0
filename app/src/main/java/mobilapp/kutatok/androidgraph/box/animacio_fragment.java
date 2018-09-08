package mobilapp.kutatok.androidgraph.box;


import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import mobilapp.kutatok.androidgraph.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class animacio_fragment extends Fragment {

    AnimationDrawable frameAnimation;

    int index;
    ImageView img1;
    Button animPlayButton;
    int[] alloKepek = new int[] {R.drawable.sze0,R.drawable.me0,R.drawable.top0, R.drawable.di0,R.drawable.pr0,R.drawable.kr0};
    int[] kepek = new int[]{R.drawable.anim_szelessegi,R.drawable.anim_melysegi,R.drawable.anim_topologiai,R.drawable.anim_dijkstra,R.drawable.anim_prim,R.drawable.anim_kruskal};

    public animacio_fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //ezt itt mi írtuk
        RelativeLayout l1 = (RelativeLayout)inflater.inflate(R.layout.fragment_animacio_fragment, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            index = bundle.getInt("index");
        }
        return l1;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        img1 = (ImageView)view.findViewById(R.id.animationFrame1);
        img1.setImageResource(alloKepek[index]);

        animPlayButton = (Button) view.findViewById(R.id.animPlayButton);
        animPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img1.setImageResource(kepek[index]);
                frameAnimation = (AnimationDrawable)img1.getDrawable();
                frameAnimation.start();
                animPlayButton.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        //*frameAnimation.stop();
    }

    @Override
    public void onStop() {
        super.onStop();
        //*frameAnimation.stop();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (!isVisibleToUser) {
            if (frameAnimation != null){
                frameAnimation.stop();
                img1.setImageResource(alloKepek[index]);
                animPlayButton.setVisibility(View.VISIBLE);
            }
        }
    }
}
