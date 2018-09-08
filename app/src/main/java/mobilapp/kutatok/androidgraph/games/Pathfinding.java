package mobilapp.kutatok.androidgraph.games;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import mobilapp.kutatok.androidgraph.Done;
import mobilapp.kutatok.androidgraph.R;

public class Pathfinding extends AppCompatActivity {

    ImageView a, b, c, d, e, f, g, h, i, j,k,l;

    //loading elemek
    ProgressBar loadProgress;
    ImageView loadLogo;
    TextView uzemAnyagView;

    //kész elemek
    ImageView item1;
    ImageView item2;
    ImageView item3;
    TextView rakterFelirat;

    TextView uzemAnyagChange;
    TextView megjelenoText1;
    ImageView circularDisplay;

    List<String> minta=new ArrayList<>();
    List<ImageView> csucsok = new ArrayList<ImageView>();
    Gametools gametools;
    List<Integer> t0, t1, t2,t3, t4, t5, t6, t7, t8, t9, t10, t11;

    List<List<Integer>> tartomanyokOsszesen = new ArrayList<>();
    int[][] szomszedsag = new int[][]{
            { 0, 1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0 }, //0
            { 1, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0 },  //1
            { 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0 }, //2
            { 1, 1, 0, 0, 1, 0, 1, 1, 0, 0, 0, 0 }, //3
            { 1, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0 }, //4
            { 0, 1, 1, 0, 1, 0, 0, 1, 1, 0, 0, 0 }, //5
            { 0, 0, 0, 1, 1, 0, 0, 1, 0, 1, 1, 0 }, //6
            { 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 1 }, //7
            { 0, 0, 0, 0, 1, 1, 0, 1, 0, 0, 1, 1 }, //8
            { 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0 }, //9
            { 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1 }, //10
            { 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0 } //11
    };
    int[][] aktualisSzomszedsag;
    HashMap<Character, ImageView> myHashMap;
    boolean lefutott = false;
    List<Gametools.Vonal> vonalak = new ArrayList<>();
    View.OnClickListener hovaRepulunk;
    List<ImageView> rakterHelyek = new ArrayList<>();
    int pontszam =0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pathfinding);
        CsucsokCreator();
        gametools = new Gametools(getResources(), this.getApplicationContext(), csucsok, this, minta, 2);
        if (gametools.CalcRatio() < 1.5) {
            Space first = (Space) findViewById(R.id.space);
            Space last = (Space) findViewById(R.id.spaceLast);
            first.setVisibility(View.VISIBLE);
            last.setVisibility(View.VISIBLE);
        }

        gametools.myHashMapCreator();
        do {
            minta.clear();
            MintaGeneralas();
            gametools.setMinta(minta);
            this.vonalak = gametools.vonalCreator();
        }while (!OsszefuggoE());

        hovaRepulunk = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int urkm = GoToWithMySpaceShip((ImageView)v);
                ImageView parkoloHely = HolParkolaSpaceShip();
                int aktualis = gametools.getUzemanyag();
                Gametools.Csucs csucsKlicked = gametools.getOjjektumok().get(MelyikCsucs((ImageView)v,gametools.getOjjektumok()));
                if (csucsKlicked.isLeadva() != true){
                    if (aktualis-urkm >= 0) {
                        if (csucsKlicked.getCsucsstatus() == Gametools.Csucsstatus.ESZKOZ) {
                            if (gametools.getRakter().getRakomany().size() == 3) {
                                Toast.makeText(getApplicationContext(), "A raktér megtelt, nem vehető fel több eszköz.", Toast.LENGTH_SHORT).show();
                            } else if (gametools.getRakter().getRakomany().size() < 3) {
                                gametools.getRakter().getRakomany().add(csucsKlicked);
                                csucsKlicked.setLeadva(true);

                                gametools.setUzemanyag(aktualis - urkm);
                                uzemAnyagView.setText(Integer.toString(gametools.getUzemanyag()));
                                uzemAnyagChange.setText(Integer.toString(-urkm));

                                csucsKlicked.getKisImageV().setBackgroundResource(R.drawable.urhajo);
                                parkoloHely.setBackgroundResource(R.drawable.discoverydraw);
                                parkoloHely.setSelected(false);
                                csucsKlicked.getKisImageV().setSelected(true);

                                for (int i = 0; i < gametools.getRakter().getRakomany().size(); i++) {
                                    rakterHelyek.get(i).setImageResource(gametools.getRakter().getRakomany().get(i).getKepId());
                                }
                            }
                        } else if (csucsKlicked.getCsucsstatus() == Gametools.Csucsstatus.BOLYGO) {
                            if (gametools.getRakter().getRakomany().size() == 0) {
                                Toast.makeText(getApplicationContext(), "A raktér üres, keress eszközt.", Toast.LENGTH_SHORT).show();
                            } else if (gametools.getRakter().getRakomany().size() > 0) {
                                int megtalalta = -1;
                                for (int j = 0; j < gametools.getRakter().getRakomany().size(); j++) {
                                    if (csucsKlicked.getTipus() == gametools.getRakter().getRakomany().get(j).getTipus()) {
                                        megtalalta = j;
                                    }
                                }
                                if (megtalalta != -1) {
                                    gametools.getRakter().getRakomany().remove(megtalalta);
                                    csucsKlicked.setLeadva(true);
                                    gametools.setUzemanyag(aktualis - urkm);
                                    uzemAnyagView.setText(Integer.toString(gametools.getUzemanyag()));
                                    uzemAnyagChange.setText(Integer.toString(-urkm));

                                    csucsKlicked.getKisImageV().setBackgroundResource(R.drawable.urhajo);
                                    parkoloHely.setBackgroundResource(R.drawable.discoverydraw);
                                    parkoloHely.setSelected(false);
                                    csucsKlicked.getKisImageV().setSelected(true);

                                    for (int i = 0; i < rakterHelyek.size(); i++) {
                                        if (gametools.getRakter().getRakomany().size() > i) {
                                            rakterHelyek.get(i).setImageResource(gametools.getRakter().getRakomany().get(i).getKepId());
                                        } else {
                                            rakterHelyek.get(i).setImageResource(R.drawable.uresrakter);
                                        }
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Nincs a raktérben az ide szállítandó eszköz.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else if (csucsKlicked.getCsucsstatus() == Gametools.Csucsstatus.VEGCEL) {
                            int db = 0;
                            for (int i = 0; i < 10; i++) {
                                if (gametools.getOjjektumok().get(i).isLeadva()) db++;
                            }
                            if (db == 10) {
                                csucsKlicked.setLeadva(true);
                                gametools.setUzemanyag(aktualis - urkm);
                                uzemAnyagView.setText(Integer.toString(gametools.getUzemanyag()));
                                uzemAnyagChange.setText(Integer.toString(-urkm));
                                csucsKlicked.getKisImageV().setBackgroundResource(R.drawable.urhajo);
                                parkoloHely.setBackgroundResource(R.drawable.discoverydraw);
                                parkoloHely.setSelected(false);
                                csucsKlicked.getKisImageV().setSelected(true);
                            }
                        }
                        if (aktualis-urkm ==0){
                            JatekVege();
                        }
                    } else {
                        JatekVege();
                    }
                } //leadva vége
            }
        };

        ImageButton infoButton=(ImageButton) findViewById(R.id.infoButton);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPopupWindow(v);
            }
        });
        item1 = (ImageView) findViewById(R.id.rakterHely1);
        item2 = (ImageView) findViewById(R.id.rakterHely2);
        item3 = (ImageView) findViewById(R.id.rakterHely3);
        rakterFelirat = (TextView)findViewById(R.id.rakterFelirat);
        rakterHelyek.add(item1);
        rakterHelyek.add(item2);
        rakterHelyek.add(item3);
        uzemAnyagView=(TextView)findViewById(R.id.loadText);
        uzemAnyagChange=(TextView)findViewById(R.id.ScoreChange);
        megjelenoText1=(TextView)findViewById(R.id.megjeleno1);
        circularDisplay=(ImageView)findViewById(R.id.circledisplay);

    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (!lefutott) {
                this.vonalak = gametools.vonalCreator();
                this.lefutott = true;
                gametools.PathfindingInicializalas();



                AsyncTask<Void,Void,Integer> megoldasCreator = new AsyncTask<Void, Void, Integer>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();

                        loadProgress=(ProgressBar)findViewById(R.id.loadProgress);
                        loadLogo=(ImageView)findViewById(R.id.loadLogo);

                    }

                    @Override
                    protected Integer doInBackground(Void... params) {
                        int optimalis;
                        try{
                            for (int i=0; i < gametools.getOjjektumok().size(); i++){
                                gametools.getOjjektumok().get(i).setTavolsagok(DijkstraTavolsag(gametools.getOjjektumok(), i));
                            }
                            optimalis = BacktrackIndito();


                        }
                        catch (Exception e){
                            optimalis=0;
                        }
                        return optimalis;

                    }

                    @Override
                    protected void onPostExecute(Integer optMegoldas) {
                        super.onPostExecute(optMegoldas);


                        loadProgress.setVisibility(View.GONE);
                        loadLogo.setVisibility(View.GONE);

                        rakterFelirat.setVisibility(View.VISIBLE);
                        item1.setVisibility(View.VISIBLE);
                        item2.setVisibility(View.VISIBLE);
                        item3.setVisibility(View.VISIBLE);
                        uzemAnyagChange.setVisibility(View.VISIBLE);
                        megjelenoText1.setVisibility(View.VISIBLE);
                        circularDisplay.setVisibility(View.VISIBLE);

                        uzemAnyagView.setText(Integer.toString(optMegoldas));
                        uzemAnyagView.setTextSize(23);
                        gametools.setUzemanyag(optMegoldas);

                        for (ImageView csucs:csucsok
                                ) {
                            csucs.setOnClickListener(hovaRepulunk);

                        }
                    }
                };

                megoldasCreator.execute();
            }
        }
    }

    public void CsucsokCreator() {
        t0=new ArrayList<>();
        t0.add(R.id.tart0_a);
        t0.add(R.id.tart0_b);
        t0.add(R.id.tart0_c);
        tartomanyokOsszesen.add(t0);

        t1=new ArrayList<>();
        t1.add(R.id.tart1_a);
        t1.add(R.id.tart1_b);
        t1.add(R.id.tart1_c);
        tartomanyokOsszesen.add(t1);

        t2=new ArrayList<>();
        t2.add(R.id.tart2_a);
        t2.add(R.id.tart2_b);
        t2.add(R.id.tart2_c);
        tartomanyokOsszesen.add(t2);

        t3=new ArrayList<>();
        t3.add(R.id.tart3_a);
        t3.add(R.id.tart3_b);
        t3.add(R.id.tart3_c);
        tartomanyokOsszesen.add(t3);

        t4=new ArrayList<>();
        t4.add(R.id.tart4_a);
        t4.add(R.id.tart4_b);
        t4.add(R.id.tart4_c);
        tartomanyokOsszesen.add(t4);

        t5=new ArrayList<>();
        t5.add(R.id.tart5_a);
        t5.add(R.id.tart5_b);
        t5.add(R.id.tart5_c);
        tartomanyokOsszesen.add(t5);

        t6=new ArrayList<>();
        t6.add(R.id.tart6_a);
        t6.add(R.id.tart6_b);
        t6.add(R.id.tart6_c);
        tartomanyokOsszesen.add(t6);

        t7=new ArrayList<>();
        t7.add(R.id.tart7_a);
        t7.add(R.id.tart7_b);
        t7.add(R.id.tart7_c);
        tartomanyokOsszesen.add(t7);

        t8=new ArrayList<>();
        t8.add(R.id.tart8_a);
        t8.add(R.id.tart8_b);
        t8.add(R.id.tart8_c);
        tartomanyokOsszesen.add(t8);

        t9=new ArrayList<>();
        t9.add(R.id.tart9_a);
        t9.add(R.id.tart9_b);
        t9.add(R.id.tart9_c);
        tartomanyokOsszesen.add(t9);

        t10=new ArrayList<>();
        t10.add(R.id.tart10_a);
        t10.add(R.id.tart10_b);
        t10.add(R.id.tart10_c);
        tartomanyokOsszesen.add(t10);

        t11=new ArrayList<>();
        t11.add(R.id.tart11_a);
        t11.add(R.id.tart11_b);
        t11.add(R.id.tart11_c);
        tartomanyokOsszesen.add(t11);


        Random rnd = new Random();
        int min =0;
        int max = 2;
        int range = max-min +1;
        int ind = rnd.nextInt(range)+min;
        csucsok.add(this.a = (ImageView) findViewById(tartomanyokOsszesen.get(0).get(ind)));

        csucsok.add(this.b = (ImageView) findViewById(tartomanyokOsszesen.get(1).get(ind)));

        csucsok.add(this.c = (ImageView) findViewById(tartomanyokOsszesen.get(2).get(ind)));

        csucsok.add(this.d = (ImageView) findViewById(tartomanyokOsszesen.get(3).get(ind)));

        csucsok.add(this.e = (ImageView) findViewById(tartomanyokOsszesen.get(4).get(ind)));

        csucsok.add(this.f = (ImageView) findViewById(tartomanyokOsszesen.get(5).get(ind)));

        csucsok.add(this.g = (ImageView) findViewById(tartomanyokOsszesen.get(6).get(ind)));

        csucsok.add(this.h = (ImageView) findViewById(tartomanyokOsszesen.get(7).get(ind)));

        csucsok.add(this.i = (ImageView) findViewById(tartomanyokOsszesen.get(8).get(ind)));

        csucsok.add(this.j = (ImageView) findViewById(tartomanyokOsszesen.get(9).get(ind)));

        csucsok.add(this.k = (ImageView) findViewById(tartomanyokOsszesen.get(10).get(ind)));

        csucsok.add(this.l = (ImageView) findViewById(tartomanyokOsszesen.get(11).get(ind)));


        for (ImageView item:csucsok
                ) {
            item.setVisibility(View.VISIBLE);
        }
    }
    public void MintaGeneralas(){
        aktualisSzomszedsag = new int[12][12];
        for (int x =0; x < szomszedsag[0].length;x++){
            for (int y =0; y < szomszedsag[0].length;y++){
                aktualisSzomszedsag[x][y] = szomszedsag[x][y];
            }
        }
        for (int i=0; i < aktualisSzomszedsag[0].length; i++){
            for (int j=0; j < aktualisSzomszedsag[1].length; j++){
                    if ( aktualisSzomszedsag[i][j]==1){
                        Random rnd = new Random();
                        int min =0;
                        int max = 100;
                        int range = max - min +1;

                        int letezikE=0;
                        if((rnd.nextInt(range) + min)>60){
                            letezikE=1;
                        }
                        aktualisSzomszedsag[i][j] = letezikE;
                    }
            }
        }


        for (int i=0; i < aktualisSzomszedsag[0].length; i++) {
            for (int j = 0; j < aktualisSzomszedsag[1].length; j++) {
                if ( aktualisSzomszedsag[i][j]==1){
                    int index = 97+i;
                    int ind = 97+j;
                    if (!minta.contains((char)ind+"-"+(char)index+"-1")) {
                        minta.add((char)index + "-" + (char)ind+"-1");
                    }
                }
            }
        }

    }
    public boolean OsszefuggoE() {
        List<ImageView> lista = gametools.SzelessegiBejarasLista();
        if (!(lista.size() == 12)) {
            return false;
        }else{
            return true;
        }

    }
    public void BacktrackRekurziv(int szint, Gametools.Csucs[] eredmenyTomb, int[] optEredmeny, List<Gametools.Csucs> rakter){
        int i =0;
        List<Gametools.Csucs>rakterAdottSzinten=new ArrayList<>(rakter);
        Collections.copy(rakterAdottSzinten,rakter);
        while (i < 10){

            if (Ft(szint,gametools.getOjjektumok().get(i),rakter)) {
                if (Fk(szint,gametools.getOjjektumok().get(i),eredmenyTomb,rakter )){
                    eredmenyTomb[szint-1] = gametools.getOjjektumok().get(i);
                    if (eredmenyTomb[szint-1].getCsucsstatus() == Gametools.Csucsstatus.ESZKOZ){
                        rakter.add(eredmenyTomb[szint-1]);
                    }else if (eredmenyTomb[szint-1].getCsucsstatus() == Gametools.Csucsstatus.BOLYGO){
                        int dex = 0;
                        for (int j=0; j < rakter.size(); j++){
                            if (rakter.get(j).getTipus() == eredmenyTomb[szint-1].getTipus()){
                                dex = j;
                            }
                        }
                        rakter.remove(dex);
                    }
                    if (szint == 10){

                        int aktualishossz=TeljesUtHossza(eredmenyTomb);
                       if (aktualishossz < optEredmeny[0]){
                           optEredmeny[0] = aktualishossz;

                       }
                    }else{
                        BacktrackRekurziv(szint+1,eredmenyTomb,optEredmeny,rakter);
                    }
                }
            }
            i++;
            rakter=new ArrayList<>(rakterAdottSzinten);
            Collections.copy(rakter,rakterAdottSzinten);

        }
    }
    public int BacktrackIndito(){
        int[] optEredmeny = new int[]{1000};
        Gametools.Csucs[] eredmenyTomb = new Gametools.Csucs[10];
        List<Gametools.Csucs> rakter = new ArrayList<>();
        int szint = 1;
        BacktrackRekurziv(szint,eredmenyTomb,optEredmeny,rakter);
        return optEredmeny[0];

    }
    public boolean Ft(int szint, Gametools.Csucs csucs,List<Gametools.Csucs> rakter){
        if (rakter.size() ==0){
            if (csucs.getCsucsstatus() == Gametools.Csucsstatus.BOLYGO){
                return false;
            }else{
                return true;
            }
        }else if (rakter.size() ==3){
            if (csucs.getCsucsstatus() == Gametools.Csucsstatus.ESZKOZ){
                return false;
            }else{
                return true;
            }
        }
        return true;
    }
    public boolean Fk(int szint, Gametools.Csucs csucs, Gametools.Csucs[] eredmenyTomb, List<Gametools.Csucs> rakter){

        for (int i=0; i < szint-1;i++ ){
                if (eredmenyTomb[i].equals(csucs)) {
                    return false;

            }
        }

        if (csucs.getCsucsstatus() == Gametools.Csucsstatus.BOLYGO){
            for (Gametools.Csucs item:rakter
                 ) {
                if (item.getTipus() == csucs.getTipus()){
                    return true;
                }
            }
            return false;
        }
        return true;

    }
    public int[] DijkstraTavolsag(List<Gametools.Csucs> ojjektumok, int index){
        Gametools.Csucs[] Slista = new Gametools.Csucs[12];
        int[] tavolsagok = new int[12];
        Gametools.Csucs[] elozoElem = new Gametools.Csucs[12];
        for (int i=0; i < ojjektumok.size(); i++){
            tavolsagok[i] = 100000;
            elozoElem[i] = null;
            Slista[i] = ojjektumok.get(i);
        }
        tavolsagok[index] = 0;

        boolean vanMegEleme = true;

        while (vanMegEleme){
            int minIndex = 0;
            int min=50;
            for(int k=0; k < tavolsagok.length; k++){
                    if (!Slista[k].isFeldolgozva() && tavolsagok[k] < min) {
                        min = tavolsagok[k];
                        minIndex=k;
                    }

            }
            Gametools.Csucs u = Slista[minIndex];

            Slista[minIndex].setFeldolgozva(true);

            List<ImageView> szomszedok = new ArrayList<>();
            List<Integer> vonalSulyok = new ArrayList<>();

            ImageView keresett=u.getKisImageV();
            for (Gametools.Vonal von : vonalak
                    ) {

                if (von.getA().equals(keresett)) {
                    szomszedok.add(von.getZ());
                    vonalSulyok.add(von.getNumber());
                } else if (von.getZ().equals(keresett)) {
                    szomszedok.add(von.getA());
                    vonalSulyok.add(von.getNumber());
                }
            }
            for (int h = 0; h < szomszedok.size(); h++){
                int mennyi = tavolsagok[minIndex] + vonalSulyok.get(h);
                int hanyadik = MelyikCsucs(szomszedok.get(h),ojjektumok);
                if ( mennyi < tavolsagok[hanyadik]){
                    tavolsagok[hanyadik] = mennyi;
                    elozoElem[hanyadik] = u;
                }
            }
            int db=0;
            for (Gametools.Csucs item:Slista
                 ) {
                if (item.isFeldolgozva()){
                    db++;
                }
            }

            if(db==12)
                vanMegEleme=false;
        }
        for (int i=0; i < ojjektumok.size(); i++){
            ojjektumok.get(i).setFeldolgozva(false);
        }
        return tavolsagok;
    }
    public int MelyikCsucs(ImageView img, List<Gametools.Csucs> ojjektumok){
        int i=0;
        while (!ojjektumok.get(i).getKisImageV().equals(img)){
            i++;
        }
         return i;
    }
    public int TeljesUtHossza(Gametools.Csucs[] eredmenyTomb){
        int osszeg = 0;
        int[]kezdotavolsag=gametools.getOjjektumok().get(11).getTavolsagok();
        Gametools.Csucs elso=eredmenyTomb[0];
        osszeg += kezdotavolsag[gametools.getOjjektumok().indexOf(elso)];

        for (int i=0; i < eredmenyTomb.length-1; i++){
            int [] tavolsagok=eredmenyTomb[i].getTavolsagok();
            Gametools.Csucs kovetkezo=eredmenyTomb[i+1];
            osszeg += tavolsagok[gametools.getOjjektumok().indexOf(kovetkezo)];
        }

        int[]vegtavolsag=eredmenyTomb[9].getTavolsagok();
        Gametools.Csucs utolso=gametools.getOjjektumok().get(10);
        osszeg += vegtavolsag[gametools.getOjjektumok().indexOf(utolso)];

        return osszeg;
    }

    public int GoToWithMySpaceShip(ImageView img){
        int i=0;
        while (i < csucsok.size() && !csucsok.get(i).isSelected()){ //ő lesz itt az űrhajóm
            i++;
        }
        int index = MelyikCsucs(csucsok.get(i),gametools.getOjjektumok());
        int[] urhajotolTavolsagok = gametools.ojjektumok.get(index).getTavolsagok();

        int ind = MelyikCsucs(img,gametools.getOjjektumok());

        return urhajotolTavolsagok[ind];
    }
    public ImageView HolParkolaSpaceShip(){
        ImageView referencia = new ImageView(getApplicationContext());
        for (int i=0; i < gametools.getOjjektumok().size(); i++){
            if(gametools.getOjjektumok().get(i).getKisImageV().isSelected()){
                referencia = gametools.getOjjektumok().get(i).getKisImageV();
            }
        }
        return referencia;
    }
    public void JatekVege(){
        int db = 0;
        for (int i=0; i < 10; i++){
            if(gametools.getOjjektumok().get(i).isLeadva())db++;
        }
        if (gametools.getOjjektumok().get(10).isLeadva()) db++;
        pontszam = db*10;
        Intent intent=new Intent(getApplicationContext(),Done.class);
        Bundle dataSend=new Bundle();
        dataSend.putInt("SCORE",pontszam);
        dataSend.putInt("MELYIKJATEK",1);
        intent.putExtras(dataSend);
        startActivity(intent);
        finish();
    }

    public void displayPopupWindow(View anchorView) {
        final PopupWindow popup = new PopupWindow(Pathfinding.this);
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
        title.setText("Útkeresés");

        WebView webView = (WebView) layout.findViewById(R.id.informationContent);
        webView.getSettings().setLoadWithOverviewMode(true);
        //webView.getSettings().setSupportZoom(true);
        //webView.getSettings().setBuiltInZoomControls(true);
        //webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.loadUrl("file:///android_asset/pathfindingtutorial.htm");

        popup.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        popup.setWidth(WindowManager.LayoutParams.MATCH_PARENT);

        popup.setOutsideTouchable(true);
        popup.setFocusable(true);

        popup.showAsDropDown(anchorView);
    }
}
