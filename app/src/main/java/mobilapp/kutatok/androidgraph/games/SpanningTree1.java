package mobilapp.kutatok.androidgraph.games;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import mobilapp.kutatok.androidgraph.MainActivity;
import mobilapp.kutatok.androidgraph.R;

public class SpanningTree1 extends AppCompatActivity {

    ImageView a, b, c, d, e, f, g, h, i, j;
    List<String> minta = Arrays.asList("a-b-3", "a-c-4", "b-c-1", "b-d-8", "c-e-10", "d-e-9", "d-g-6", "c-f-4",
            "f-g-5", "g-h-4", "f-j-2", "a-i-6", "i-j-8", "j-h-5", "f-i-9", "d-h-7", "e-g-3");
    HashMap<Character, ImageView> myHashMap;
    List<Gametools.Vonal> vonalak;
    List<ImageView> csucsok = new ArrayList<ImageView>();
    EredmenyEllenorzo controller;
    Button keszButton;
    boolean lefutott = false;
    Gametools gametools;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spanning_tree1);
        csucsokCreator();
        gametools = new Gametools(getResources(), this.getApplicationContext(), csucsok, this, minta, 0);
        if (gametools.CalcRatio() < 1.5) {
            Space first = (Space) findViewById(R.id.space);
            Space last = (Space) findViewById(R.id.spaceLast);
            first.setVisibility(View.VISIBLE);
            last.setVisibility(View.VISIBLE);
        }
        gametools.myHashMapCreator();

        keszButton = (Button) findViewById(R.id.keszGomb);
        keszButton.setOnClickListener(new View.OnClickListener() {
            ProgressBar keszProgress;
            ImageView logo;
            TextView megoldas;
            TextView fomenu;
            @Override
            public void onClick(View v) {
                AsyncTask<Void,Void,Boolean> kiertekeles = new AsyncTask<Void, Void, Boolean>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        keszProgress=(ProgressBar)findViewById(R.id.keszProgress);
                        keszProgress.setVisibility(View.VISIBLE);
                    }

                    @Override
                    protected Boolean doInBackground(Void... params) {
                        controller =new EredmenyEllenorzo(gametools.vonalak, csucsok);
                        try{
                            Thread.sleep(2000);
                        } catch (InterruptedException e){
                            e.printStackTrace();
                        }

                        return controller.MegoldasEllenorzes();
                    }

                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        super.onPostExecute(aBoolean);
                        keszProgress.setVisibility(View.GONE);
                        logo=(ImageView)findViewById(R.id.logo);
                        logo.setVisibility(View.GONE);

                        fomenu=(TextView)findViewById(R.id.visszaFomenu);
                        fomenu.setVisibility(View.VISIBLE);
                        megoldas=(TextView)findViewById(R.id.eredmenyText);
                        megoldas.setVisibility(View.VISIBLE);
                        if(aBoolean){
                            megoldas.setText("Jó megoldás");
                            megoldas.setTextColor(Color.parseColor("#b0ee00"));
                        }
                        else{
                            megoldas.setText("A megoldás nem jó");
                            megoldas.setTextColor(Color.parseColor("#ee3c00"));
                        }

                        fomenu.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent mainMenu=new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(mainMenu);
                            }
                        });

                    }
                };

                kiertekeles.execute();
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
    }










}
