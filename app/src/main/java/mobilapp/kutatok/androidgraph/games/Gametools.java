package mobilapp.kutatok.androidgraph.games;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.support.v7.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import mobilapp.kutatok.androidgraph.R;

/**
 * Created by Orumiya on 2017.08.12..
 */

public final class Gametools {

    Resources resources;
    Context context;
     HashMap<Character,ImageView> myHashMap;
    List<ImageView> csucsok;
    List<Vonal> vonalak;
    int csucsSzam;
    Activity activity;
    List<String> minta;
    ImageView firstClick;
    int discoveryMode;
    HashMap<ImageView,Vonal> szomszedok; //tárolja a megnyomott image view-hoz kapcsolódó vonalakat
    Rakter rakter;
    public enum Csucsstatus{
        ESZKOZ,
        BOLYGO,
        URHAJO,
        VEGCEL;
    }
    List<Csucs> ojjektumok;

    int resource =0; //erőforrás
    int scoreChange; //levonódott pontok
    int felfedezettGalaxis;
    List<Double> pathDistance = new ArrayList<>();

    public int getUzemanyag() {
        return uzemanyag;
    }

    public void setUzemanyag(int uzemanyag) {
        this.uzemanyag = uzemanyag;
    }

    int uzemanyag=0;

    public int getResource() {
        return resource;
    }

    public int getScoreChange() {
        return scoreChange;
    }

    public int getFelfedezettGalaxis() {
        return felfedezettGalaxis;
    }

    public int KezdopontszamBeallitas(){
        int osszpontszam =0;
        for (Vonal item:vonalak
             ) {
            osszpontszam += item.getNumber();
        }
        resource = osszpontszam/4;
        return resource;
    }

    public void setMinta(List<String> minta) {
        this.minta = minta;
    }

    public Rakter getRakter() {
        return rakter;
    }

    public List<Csucs> getOjjektumok() {
        return ojjektumok;
    }

    public void setOjjektumok(List<Csucs> ojjektumok) {
        this.ojjektumok = ojjektumok;
    }

    public Gametools(Resources resources, Context context, List<ImageView> csucsok, Activity activity, List<String> minta, int discoveryMode) {
        this.resources = resources;
        this.context = context;
        this.csucsok = csucsok;
        this.activity = activity;
        csucsSzam = csucsok.size();
        this.minta = minta;
        this.discoveryMode = discoveryMode;
        this.felfedezettGalaxis = 1;
    }

    public float CalcRatio(){
        float aspectRatio;
        float width = resources.getDisplayMetrics().widthPixels;
        float height = resources.getDisplayMetrics().heightPixels;

        if (width > height){
            aspectRatio = width/height;
        }else{
            aspectRatio = height/width;
        }
        return aspectRatio;

    }
    public int dpToPixel(int dp){
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        return Math.round(dp*(displayMetrics.densityDpi/160f));
    }
    public void myHashMapCreator() {
        myHashMap = new HashMap<>();
        for (int i = 0; i < csucsSzam; i++){
            int index = 97+i;
            myHashMap.put((char)index, csucsok.get(i));

        }
    }

    public int isDiscoveryMode() {
        return discoveryMode;
    }

    public boolean SearchLine(ImageView img){

        if (szomszedok == null) {
            szomszedok = new HashMap<ImageView, Vonal>();
            firstClick = img;
            for (Vonal item : vonalak
                    ) {
                if (item.getA().equals(img) && item.isDiscovered()) {
                    szomszedok.put(item.getZ(), item);
                    item.getZ().setBackgroundResource(R.drawable.discoveryselected);
                } else if (item.getZ().equals(img)&& item.isDiscovered()) {
                    szomszedok.put(item.getA(), item);
                    item.getA().setBackgroundResource(R.drawable.discoveryselected);
                }
            }

            if (szomszedok.size() ==0){
                szomszedok = null;
                return false;
            }
            firstClick.setBackgroundResource(R.drawable.discoverycenter);
            return true;
        }

        return false;
    }
    public void MatchLine(ImageView img){
        if (szomszedok != null){
            Vonal v = szomszedok.get(img);
            if (v != null){
                v.setPressed(!v.isPressed());
                if (v.isPressed()){
                    if (resource >= v.getNumber()) {
                        v.getLine().setVisibility(View.GONE);
                        v.setLine(addLine(v.getA(), v.getZ(), Color.YELLOW, v));
                        resource -= v.getNumber();
                        scoreChange = -v.getNumber();
                    }else{
                        Toast.makeText(context, "Nincs elég erőforrás új hálózat kiépítéséhez!", Toast.LENGTH_SHORT).show();
                        v.setPressed(false);
                    }
                }else{
                    v.getLine().setVisibility(View.GONE);
                    v.setLine(addLine(v.getA(),v.getZ(),Color.WHITE, v));
                    resource+= v.getNumber();
                    scoreChange = +v.getNumber();
                }
                ClearLines();
                szomszedok = null;

            }else{
                ClearLines();
                szomszedok = null;
            }


        }
    }
    public void Discover(ImageView img){
        if (!img.isSelected()) {
            img.setSelected(true);
            ArrayList<Vonal> szomszedVonal = new ArrayList<Vonal>();
            int minVonalSuly = 10;
            for (Vonal v : vonalak
                    ) {
                if ((v.getA().equals(img) || v.getZ().equals(img)) && !v.discovered) {
                    szomszedVonal.add(v);
                } else if ((v.getA().equals(img) || v.getZ().equals(img)) && v.discovered && (v.getNumber() < minVonalSuly)) {
                    minVonalSuly = v.getNumber();
                }
            }
            if (minVonalSuly <= resource) {
                for (Vonal v : szomszedVonal
                        ) {
                    if (v.getA().equals(img)) {
                        v.discovered = true;
                        if (v.getZ().getVisibility() == View.INVISIBLE) {
                            v.getZ().setVisibility(View.VISIBLE);
                            felfedezettGalaxis++;
                        }
                        v.getNumberDisplay().setVisibility(View.VISIBLE);
                        v.setLine(addLine(v.getA(), v.getZ(), Color.WHITE, v));

                    } else if (v.getZ().equals(img)) {
                        v.discovered = true;
                        if (v.getA().getVisibility() ==View.INVISIBLE){
                            v.getA().setVisibility(View.VISIBLE);
                            felfedezettGalaxis++;
                        }
                        v.getNumberDisplay().setVisibility(View.VISIBLE);
                        v.setLine(addLine(v.getA(), v.getZ(), Color.WHITE, v));
                    }
                }
                if (minVonalSuly == 10) {
                    minVonalSuly = 0;
                }
                resource -= minVonalSuly;
                scoreChange = -minVonalSuly;
            } else {
                Toast.makeText(context, "Nincs elég erőforrás a felfedező expedíció indításához!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(context, "Ebből a galaxisból már indult felfedező expedíció!", Toast.LENGTH_SHORT).show();
        }

    }
    public void ClearLines(){
        Set<ImageView> osszesKulcs = szomszedok.keySet();
        for (ImageView item:osszesKulcs
             ) {
            item.setBackgroundResource(R.drawable.discoverydraw);
        }
        firstClick.setBackgroundResource(R.drawable.discoverydraw);
        firstClick = null;
    }
    public int SzelessegiBejaras(){ //mekkora a leghosszabb hálózatom?
        int faMeret = 0;
        List<ImageView> elertCsucsok = new ArrayList<>();
        for (ImageView csucs: csucsok
             ) {
            if (!elertCsucsok.contains(csucs)) {
                List<ImageView> Slista = new ArrayList<>();
                List<ImageView> Flista = new ArrayList<>();

                Slista.add(csucs);
                Flista.add(csucs);
                while (Slista.size() != 0) {
                    ImageView k = Slista.get(0);
                    Slista.remove(0);
                    List<ImageView> szomszedok = new ArrayList<>();
                    for (Vonal v : vonalak
                            ) {
                        if (v.isPressed() && v.getA().equals(k)) {
                            szomszedok.add(v.getZ());
                        } else if (v.isPressed() && v.getZ().equals(k)) {
                            szomszedok.add(v.getA());
                        }
                    }
                    for (ImageView sz : szomszedok
                            ) {
                        if (!Flista.contains(sz)) {
                            Slista.add(sz);
                            Flista.add(sz);
                        }
                    }
                }
                if (Flista.size() > faMeret){
                    faMeret = Flista.size();
                }
                for (ImageView f : Flista
                        ) {
                    if (!elertCsucsok.contains(f)) {
                        elertCsucsok.add(f);
                    }
                }
            }
        }
        return faMeret;
    }

    public List<ImageView> SzelessegiBejarasLista(){ //leghosszabb gráfot adja vissza
        int faMeret = 0;
        List<ImageView> leghosszabbGraf = new ArrayList<>();
        List<ImageView> elertCsucsok = new ArrayList<>();
        for (ImageView csucs: csucsok
                ) {
            if (!elertCsucsok.contains(csucs)) {
                List<ImageView> Slista = new ArrayList<>();
                List<ImageView> Flista = new ArrayList<>();

                Slista.add(csucs);
                Flista.add(csucs);
                while (Slista.size() != 0) {
                    ImageView k = Slista.get(0);
                    Slista.remove(0);
                    List<ImageView> szomszedok = new ArrayList<>();
                    for (Vonal v : vonalak
                            ) {
                        if (v.getA().equals(k)) {
                            szomszedok.add(v.getZ());
                        } else if (v.getZ().equals(k)) {
                            szomszedok.add(v.getA());
                        }
                    }
                    for (ImageView sz : szomszedok
                            ) {
                        if (!Flista.contains(sz)) {
                            Slista.add(sz);
                            Flista.add(sz);
                        }
                    }
                }
                if (Flista.size() > faMeret){
                    faMeret = Flista.size();
                    leghosszabbGraf = Flista;
                }
                for (ImageView f : Flista
                        ) {
                    if (!elertCsucsok.contains(f)) {
                        elertCsucsok.add(f);
                    }
                }
            }
        }
        return leghosszabbGraf;
    }
    public MyView addLine(ImageView a, ImageView z, int color, Vonal v) {
        MyView line_az = new MyView(context, a, z, color, v);
        activity.addContentView(line_az, new LinearLayoutCompat.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        line_az.bringToFront();
        return line_az;
    }
    public List<Vonal> vonalCreator() {
        if (Gametools.this.isDiscoveryMode() == 2){
            pathDistance.clear();
        }
        vonalak = new ArrayList<Vonal>();
        for (String item : minta
                ) {
            Character elso = item.charAt(0);
            Character masodik = item.charAt(2);
            int suly = (int) item.charAt(4) - 48;
            vonalak.add(new Vonal(myHashMap.get(elso), myHashMap.get(masodik)));
            if (Gametools.this.isDiscoveryMode() == 2){

                pathDistance.add(vonalak.get(vonalak.size()-1).getDistanceBetweenCenters());
            }

        }
        double min=1000000;
        double max = -1;
        for (Double d: pathDistance
             ) {
            if ((double)d > max){
                max = d;
            }
            if ((double)d < min){
                min = d;
            }
        }
        double egyseg = max/8;
        for (int i=0; i<pathDistance.size(); i++){
            vonalak.get(i).setNumber((int)(pathDistance.get(i)/egyseg)+1);
            vonalak.get(i).getNumberDisplay().setText(Integer.toString(vonalak.get(i).getNumber()));
        }
        return vonalak;
    }

    public class Vonal  {

        TextView numberDisplay;
        MyView line;
        ImageView a;//1. csúcs
        ImageView z;//2. csúcs
        int number;//az élhez tartozó súly: egyelőre fixen megkapja konstruktorból, később inkább random legyen
        boolean pressed;//ha meg lesz nyomva az adott él, akkor igaz
        boolean discovered;

        float Xcoord_v1;
        float Xcoord_v2;
        float Ycoord_v1;
        float Ycoord_v2;
        double distanceBetweenCenters;

        public double getDistanceBetweenCenters() {
            return distanceBetweenCenters;
        }

        public float getXcoord_v1() {
            return Xcoord_v1;
        }

        public float getXcoord_v2() {
            return Xcoord_v2;
        }

        public float getYcoord_v1() {
            return Ycoord_v1;
        }

        public float getYcoord_v2() {
            return Ycoord_v2;
        }

        public ImageView getA() {
            return a;
        }
        public ImageView getZ() {
            return z;
        }
        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public boolean isPressed() {
            return pressed;
        }

        public boolean isDiscovered() {
            return discovered;
        }
        public void setDiscovered(boolean discovered) {
            this.discovered = discovered;
        }

        public TextView getNumberDisplay() {
            return numberDisplay;
        }

        public MyView getLine() {
            return line;
        }

        public void setLine(MyView line) {
            this.line = line;
        }

        public void setPressed(boolean pressed) {
            this.pressed = pressed;
        }

        public void addNumber(){
            numberDisplay = new TextView(context);

            if (a.getX() >= z.getX() && a.getY() >= z.getY()){
                float centerX_v1 = a.getX() + (a.getWidth()/2);
                float centerY_v1 = a.getY() + (a.getHeight() /2);
                float centerX_v2 = z.getX() + (z.getWidth()/2);
                float centerY_v2 = z.getY() + (z.getHeight() /2);
                double radius_v1 = a.getHeight()/2;
                double radius_v2 = z.getHeight()/2;

                double diffX = centerX_v1 - centerX_v2;
                double diffY = centerY_v1 - centerY_v2;
                distanceBetweenCenters = Math.sqrt(Math.pow(diffX,2) + Math.pow(diffY,2));
                Xcoord_v1 = (float)(centerX_v1 - ((diffX)*(radius_v1/distanceBetweenCenters)));
                Ycoord_v1 = (float)(centerY_v1 - ((diffY)*(radius_v1/distanceBetweenCenters)));
                Xcoord_v2 = (float)(centerX_v2 + ((diffX)*(radius_v2/distanceBetweenCenters)));
                Ycoord_v2 = (float)(centerY_v2 + ((diffY)*(radius_v2/distanceBetweenCenters)));
            }
            else if (a.getX() < z.getX() && a.getY() < z.getY()){
                float centerX_v1 = a.getX() + (a.getWidth()/2);
                float centerY_v1 = a.getY() + (a.getHeight() /2);
                float centerX_v2 = z.getX() + (z.getWidth()/2);
                float centerY_v2 = z.getY() + (z.getHeight() /2);
                double radius_v1 = a.getHeight()/2;
                double radius_v2 = z.getHeight()/2;

                double diffX = centerX_v2 - centerX_v1;
                double diffY = centerY_v2 - centerY_v1;
                distanceBetweenCenters = Math.sqrt(Math.pow(diffX,2) + Math.pow(diffY,2));
                Xcoord_v1 = (float)(centerX_v1 + ((diffX)*(radius_v1/distanceBetweenCenters)));
                Ycoord_v1 = (float)(centerY_v1 + ((diffY)*(radius_v1/distanceBetweenCenters)));
                Xcoord_v2 = (float)(centerX_v2 - ((diffX)*(radius_v2/distanceBetweenCenters)));
                Ycoord_v2 = (float)(centerY_v2 - ((diffY)*(radius_v2/distanceBetweenCenters)));


            }
            else if (a.getX() >= z.getX() && a.getY() < z.getY()){
                float centerX_v1 = a.getX() + (a.getWidth()/2);
                float centerY_v1 = a.getY() + (a.getHeight() /2);
                float centerX_v2 = z.getX() + (z.getWidth()/2);
                float centerY_v2 = z.getY() + (z.getHeight() /2);
                double radius_v1 = a.getHeight()/2;
                double radius_v2 = z.getHeight()/2;

                double diffX = centerX_v1 - centerX_v2;
                double diffY = centerY_v2 - centerY_v1;
                distanceBetweenCenters = Math.sqrt(Math.pow(diffX,2) + Math.pow(diffY,2));
                Xcoord_v1 = (float)(centerX_v1 - ((diffX)*(radius_v1/distanceBetweenCenters)));
                Ycoord_v1 = (float)(centerY_v1 + ((diffY)*(radius_v1/distanceBetweenCenters)));
                Xcoord_v2 = (float)(centerX_v2 + ((diffX)*(radius_v2/distanceBetweenCenters)));
                Ycoord_v2 = (float)(centerY_v2 - ((diffY)*(radius_v2/distanceBetweenCenters)));

            }else
            {
                float centerX_v1 = a.getX() + (a.getWidth()/2);
                float centerY_v1 = a.getY() + (a.getHeight() /2);
                float centerX_v2 = z.getX() + (z.getWidth()/2);
                float centerY_v2 = z.getY() + (z.getHeight() /2);
                double radius_v1 = a.getHeight()/2;
                double radius_v2 = z.getHeight()/2;

                double diffX = centerX_v2 - centerX_v1;
                double diffY = centerY_v1 - centerY_v2;
                distanceBetweenCenters = Math.sqrt(Math.pow(diffX,2) + Math.pow(diffY,2));
                Xcoord_v1 = (float)(centerX_v1 + ((diffX)*(radius_v1/distanceBetweenCenters)));
                Ycoord_v1 = (float)(centerY_v1 - ((diffY)*(radius_v1/distanceBetweenCenters)));
                Xcoord_v2 = (float)(centerX_v2 - ((diffX)*(radius_v2/distanceBetweenCenters)));
                Ycoord_v2 = (float)(centerY_v2 + ((diffY)*(radius_v2/distanceBetweenCenters)));
            }

            numberDisplay.setX((Xcoord_v1+Xcoord_v2)/2-dpToPixel(35/2));
            numberDisplay.setY((Ycoord_v1+Ycoord_v2)/2-dpToPixel(35/2));
            numberDisplay.setText(Integer.toString(number));
            numberDisplay.setTextColor(Color.parseColor("#eaeaea"));
            if (Gametools.this.isDiscoveryMode() ==0) {

                numberDisplay.setTextSize(25);
                numberDisplay.setBackgroundResource(R.drawable.circle);
                Drawable bckgrnd = numberDisplay.getBackground();
                bckgrnd.setAlpha(70);

                numberDisplay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pressed = !pressed;
                        if (isPressed()){
                            line.setVisibility(View.GONE);
                            line = addLine(a,z,Color.YELLOW,Vonal.this);
                        }else{
                            line.setVisibility(View.GONE);
                            line = addLine(a,z,Color.WHITE,Vonal.this);
                        }
                    }
                });
            }else if(Gametools.this.isDiscoveryMode() ==1){

                numberDisplay.setTextSize(15);
                numberDisplay.setVisibility(View.GONE);
            }else {
                numberDisplay.setTextSize(15);
            }
            //itt határozzuk meg a szám (mint TextView) helyét és adjuk hozzá a layouthoz

            activity.addContentView(numberDisplay,new LinearLayoutCompat.LayoutParams(dpToPixel(35), dpToPixel(35)));
            numberDisplay.setGravity(Gravity.CENTER);
            numberDisplay.bringToFront();



        }

        public Vonal(ImageView a, ImageView z) {
            this.a = a;
            this.z = z;
            Random rnd = new Random();
            int min =1;
            int max = 9;
            int range = max - min +1;
            this.number = rnd.nextInt(range) + min;
            this.pressed = false;
            this.discovered = false;
            if (Gametools.this.isDiscoveryMode() !=1){
                line = addLine(a,z, Color.WHITE,this);
                addNumber();
            }else{
                addNumber();
            }

        }
    }

    public class MyView extends View{
        Paint paint = new Paint();
        View v1;
        View v2;
        int color;
        Vonal vonal;
        int alpha;

        public MyView(Context context, View v1, View v2, int color, Vonal vonal) {
            super(context);
            this.color = color;
            paint.setColor(color);
            paint.setStrokeWidth(dpToPixel(10));

            if (color == Color.WHITE){
                alpha = 35;
            }else{
                alpha = 90;
            }
            paint.setAlpha(alpha); //átlátszósági érték beállítása
            paint.setAntiAlias(true); //elsimítja a vonalakat
            this.v1 = v1;
            this.v2 = v2;
            this.vonal = vonal;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawLine(vonal.getXcoord_v1(),vonal.getYcoord_v1(),vonal.getXcoord_v2(),vonal.getYcoord_v2(),paint);
        }

    }


    public class Csucs{

        ImageView kisImageV;
        int tipus; //összepárosítás alapja
        Csucsstatus csucsstatus;
        int kepId;
        boolean feldolgozva;
        int[] tavolsagok;

        public boolean isLeadva() {
            return leadva;
        }

        public void setLeadva(boolean leadva) {
            this.leadva = leadva;
        }

        boolean leadva;

        public boolean isFeldolgozva() {
            return feldolgozva;
        }

        public void setFeldolgozva(boolean feldolgozva) {
            this.feldolgozva = feldolgozva;
        }


        public void setTavolsagok(int[] tavolsagok) {
            this.tavolsagok = tavolsagok;
        }

        public int[] getTavolsagok() {
            return tavolsagok;
        }



        public Csucs(ImageView kisImageV, int tipus, Csucsstatus csucsstatus, int kepId) {
            this.kisImageV = kisImageV;
            this.tipus = tipus;
            this.csucsstatus = csucsstatus;
            this.kepId = kepId;
            this.tavolsagok = new int[12];
            this.feldolgozva = false;
            kisImageV.setBackgroundResource(kepId);
            this.leadva = false;
        }

        public ImageView getKisImageV() {
            return kisImageV;
        }

        public void setKisImageV(ImageView kisImageV) {
            this.kisImageV = kisImageV;
        }

        public int getTipus() {
            return tipus;
        }

        public void setTipus(int tipus) {
            this.tipus = tipus;
        }

        public Csucsstatus getCsucsstatus() {
            return csucsstatus;
        }

        public void setCsucsstatus(Csucsstatus csucsstatus) {
            this.csucsstatus = csucsstatus;
        }

        public int getKepId() {
            return kepId;
        }

        public void setKepId(int kepId) {
            this.kepId = kepId;
        }
    }
    public class Rakter{
        int maxTarhely;
        List<Csucs> rakomany;

        public int getMaxTarhely() {
            return maxTarhely;
        }

        public void setMaxTarhely(int maxTarhely) {
            this.maxTarhely = maxTarhely;
        }

        public List<Csucs> getRakomany() {
            return rakomany;
        }

        public void setRakomany(List<Csucs> rakomany) {
            this.rakomany = rakomany;
        }

        public Rakter(int maxTarhely) {
            this.maxTarhely = maxTarhely;
            this.rakomany = new ArrayList<>();

        }
    }

    public void PathfindingInicializalas(){
        List<ImageView> randomCsucsok = new ArrayList<>(csucsok);

        Collections.copy(randomCsucsok,this.csucsok);
        Collections.shuffle(randomCsucsok);
        ojjektumok = new ArrayList<>();
        Csucs eszkoz1 = new Csucs(randomCsucsok.get(0),1,Csucsstatus.ESZKOZ, R.drawable.eszkoz1);
        Csucs eszkoz2 = new Csucs(randomCsucsok.get(1),2,Csucsstatus.ESZKOZ, R.drawable.eszkoz2);
        Csucs eszkoz3 = new Csucs(randomCsucsok.get(2),3,Csucsstatus.ESZKOZ, R.drawable.eszkoz3);
        Csucs eszkoz4 = new Csucs(randomCsucsok.get(3),4,Csucsstatus.ESZKOZ, R.drawable.eszkoz4);
        Csucs eszkoz5 = new Csucs(randomCsucsok.get(4),5,Csucsstatus.ESZKOZ, R.drawable.eszkoz5);
        Csucs bolygo1 = new Csucs(randomCsucsok.get(5),1,Csucsstatus.BOLYGO, R.drawable.pl13);
        Csucs bolygo2 = new Csucs(randomCsucsok.get(6),2,Csucsstatus.BOLYGO, R.drawable.pl6);
        Csucs bolygo3 = new Csucs(randomCsucsok.get(7),3,Csucsstatus.BOLYGO, R.drawable.pl11);
        Csucs bolygo4 = new Csucs(randomCsucsok.get(8),4,Csucsstatus.BOLYGO, R.drawable.pl1);
        Csucs bolygo5 = new Csucs(randomCsucsok.get(9),5,Csucsstatus.BOLYGO, R.drawable.pl8);
        Csucs urhajo = new Csucs(randomCsucsok.get(10),6,Csucsstatus.URHAJO, R.drawable.urhajo);
        Csucs vegcel = new Csucs(randomCsucsok.get(11),6,Csucsstatus.VEGCEL, R.drawable.vegcel);
        ojjektumok.add(eszkoz1);
        ojjektumok.add(eszkoz2);
        ojjektumok.add(eszkoz3);
        ojjektumok.add(eszkoz4);
        ojjektumok.add(eszkoz5);
        ojjektumok.add(bolygo1);
        ojjektumok.add(bolygo2);
        ojjektumok.add(bolygo3);
        ojjektumok.add(bolygo4);
        ojjektumok.add(bolygo5);
        ojjektumok.add(vegcel);
        ojjektumok.add(urhajo);
        urhajo.getKisImageV().setSelected(true);


        rakter = new Rakter(3);

    }

}
