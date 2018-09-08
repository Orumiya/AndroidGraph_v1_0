package mobilapp.kutatok.androidgraph.games;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import mobilapp.kutatok.androidgraph.Done;
import mobilapp.kutatok.androidgraph.MainActivity;
import mobilapp.kutatok.androidgraph.R;

public class Discovery extends AppCompatActivity {

    ImageView a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z;
    List<String> minta = Arrays.asList("a-b-2", "a-d-4", "a-g-5","a-k-1","b-f-3","b-e-1","d-f-5","d-j-2",
            "e-c-7","e-h-1","c-h-6","b-i-5","g-f-6","f-i-7","i-h-4","j-i-6","g-k-4","i-l-9","h-l-4",
            "j-m-1","i-m-3","i-q-7","m-q-6","f-p-5","k-o-1","j-o-8","j-p-4","l-n-7","n-q-5","n-r-3",
            "o-p-4","o-s-4","p-s-1","s-q-6","q-t-3","q-r-4","t-r-7","q-u-9","s-w-1","u-w-5","u-x-9",
            "t-x-7","r-y-1","r-v-5","v-y-4","x-y-2","u-z-4","w-z-6","x-z-1","y-z-5");
    HashMap<Character, ImageView> myHashMap;
    List<Gametools.Vonal> vonalak;
    List<ImageView> csucsok = new ArrayList<ImageView>();
    //EredmenyEllenorzo controller;


    boolean lefutott = false;
    Gametools gametools;
    View.OnLongClickListener discover;
    View.OnClickListener vonalKijeloles;

    Button keszButton;
    ProgressBar keszProgress;
    TextView eroforrasView; //mennyi maradt neki
    TextView scoreChange; //pontszámváltozás a legutolsó művelettel
    TextView felfedezettGalaxisok;
    ImageButton infoButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery);
        csucsokCreator();
        gametools = new Gametools(getResources(), this.getApplicationContext(), csucsok, this, minta, 1);
        if (gametools.CalcRatio() < 1.5) {
            Space first = (Space) findViewById(R.id.space);
            Space last = (Space) findViewById(R.id.spaceLast);
            first.setVisibility(View.VISIBLE);
            last.setVisibility(View.VISIBLE);
        }
        gametools.myHashMapCreator();
        eroforrasView = (TextView)findViewById(R.id.Score);
        eroforrasView.bringToFront();
        scoreChange = (TextView)findViewById(R.id.ScoreChange);
        scoreChange.bringToFront();
        felfedezettGalaxisok = (TextView)findViewById(R.id.discoveredGalaxies);
        felfedezettGalaxisok.bringToFront();
        felfedezettGalaxisok.setText("1 / 26");
        infoButton=(ImageButton) findViewById(R.id.infoButton);
        keszProgress = (ProgressBar)findViewById(R.id.keszProgress);
        keszButton = (Button)findViewById(R.id.keszGomb);
        keszButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AsyncTask<Void,Void,Void>megoldasBetoltes=new AsyncTask<Void, Void, Void>() {
                    int eredmeny;
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        keszProgress.setVisibility(View.VISIBLE);
                        keszProgress.bringToFront();
                        eredmeny=gametools.SzelessegiBejaras();
                        keszButton.setText(Integer.toString(eredmeny));
                        keszButton.setTextSize(30);
                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        try{
                            Thread.sleep(2000);
                        } catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        Intent intent=new Intent(getApplicationContext(),Done.class);
                        Bundle dataSend=new Bundle();
                        dataSend.putInt("SCORE",eredmeny*10);
                        dataSend.putInt("MELYIKJATEK",2);
                        intent.putExtras(dataSend);
                        startActivity(intent);
                        finish();
                    }
                };

                megoldasBetoltes.execute();

            }
        });

        vonalKijeloles = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean searchResult = gametools.SearchLine((ImageView)v);
                if (!searchResult){
                    gametools.MatchLine((ImageView)v);
                    eroforrasView.setText(Integer.toString(gametools.getResource()));
                    scoreChange.setText(Integer.toString(gametools.getScoreChange()));
                    if (gametools.getScoreChange() > 0){
                        scoreChange.setTextColor(Color.GREEN);
                    }else{
                        scoreChange.setTextColor(Color.RED);
                    }
                }
            }
        };

        discover = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                gametools.Discover((ImageView) v);
                felfedezettGalaxisok.setText(Integer.toString(gametools.getFelfedezettGalaxis())+" / 26");
                eroforrasView.setText(Integer.toString(gametools.getResource()));
                scoreChange.setText(Integer.toString(gametools.getScoreChange()));
                scoreChange.setTextColor(Color.RED);
                return true;
            }
        };

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPopupWindow(v);
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (!lefutott) {
                this.vonalak = gametools.vonalCreator();
                this.lefutott = true;
                eroforrasView.setText(Integer.toString(gametools.KezdopontszamBeallitas()));
            }

            for (ImageView csucs:csucsok
                 ) {
                csucs.setOnClickListener(vonalKijeloles);
                csucs.setOnLongClickListener(discover);
            }

        }
    }

    public void csucsokCreator() {
        csucsok.add(this.a = (ImageView) findViewById(R.id.kor_a));
        csucsok.add(this.b = (ImageView) findViewById(R.id.kor_b));
        csucsok.add(this.c = (ImageView) findViewById(R.id.kor_c));
        csucsok.add(this.d = (ImageView) findViewById(R.id.kor_d));
        csucsok.add(this.e = (ImageView) findViewById(R.id.kor_e));
        csucsok.add(this.f = (ImageView) findViewById(R.id.kor_f));
        csucsok.add(this.g = (ImageView) findViewById(R.id.kor_g));
        csucsok.add(this.h = (ImageView) findViewById(R.id.kor_h));
        csucsok.add(this.i = (ImageView) findViewById(R.id.kor_i));
        csucsok.add(this.j = (ImageView) findViewById(R.id.kor_j));
        csucsok.add(this.k = (ImageView) findViewById(R.id.kor_k));
        csucsok.add(this.l = (ImageView) findViewById(R.id.kor_l));
        csucsok.add(this.m = (ImageView) findViewById(R.id.kor_m));
        csucsok.add(this.n = (ImageView) findViewById(R.id.kor_n));
        csucsok.add(this.o = (ImageView) findViewById(R.id.kor_o));
        csucsok.add(this.p = (ImageView) findViewById(R.id.kor_p));
        csucsok.add(this.q = (ImageView) findViewById(R.id.kor_q));
        csucsok.add(this.r = (ImageView) findViewById(R.id.kor_r));
        csucsok.add(this.s = (ImageView) findViewById(R.id.kor_s));
        csucsok.add(this.t = (ImageView) findViewById(R.id.kor_t));
        csucsok.add(this.u = (ImageView) findViewById(R.id.kor_u));
        csucsok.add(this.v = (ImageView) findViewById(R.id.kor_v));
        csucsok.add(this.w = (ImageView) findViewById(R.id.kor_w));
        csucsok.add(this.x = (ImageView) findViewById(R.id.kor_x));
        csucsok.add(this.y = (ImageView) findViewById(R.id.kor_y));
        csucsok.add(this.z = (ImageView) findViewById(R.id.kor_z));

        Random rnd = new Random();
        int min =0;
        int max = 25;
        int range = max - min +1;
        int melyik = rnd.nextInt(range) + min;
        csucsok.get(melyik).setVisibility(View.VISIBLE);
    }

    public void displayPopupWindow(View anchorView) {
        final PopupWindow popup = new PopupWindow(Discovery.this);
        View layout = getLayoutInflater().inflate(R.layout.information_page, null);
        popup.setContentView(layout);

        ImageButton closeButton = (ImageButton) layout.findViewById(R.id.informationCloseButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
            }
        });

        TextView title = (TextView) layout.findViewById(R.id.informationTitle);
        title.setText("Hálózatépítés");

        WebView webView = (WebView) layout.findViewById(R.id.informationContent);
        webView.getSettings().setLoadWithOverviewMode(true);
        //webView.getSettings().setSupportZoom(true);
        //webView.getSettings().setBuiltInZoomControls(true);
        //webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl("file:///android_asset/discoverytutorial.htm");

        popup.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        popup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);

        popup.setOutsideTouchable(true);
        popup.setFocusable(true);

        popup.showAsDropDown(anchorView);
    }

}
