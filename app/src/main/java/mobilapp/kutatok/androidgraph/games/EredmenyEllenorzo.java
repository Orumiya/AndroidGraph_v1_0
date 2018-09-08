package mobilapp.kutatok.androidgraph.games;

import android.media.Image;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Orumiya on 2017.08.04..
 */

public class EredmenyEllenorzo {

    List<Gametools.Vonal> vonalak;
    List<ImageView> csucsok;
    HashMap<ImageView, Boolean> vezetElHashMap;
    ArrayList[] csucsHalmaz;

    public EredmenyEllenorzo(List<Gametools.Vonal> vonalak, List<ImageView> csucsok) {
        this.vonalak = vonalak;
        this.csucsok = csucsok;
    }

    public void VezetElHashMapCreator(){
        vezetElHashMap = new HashMap<>();
        for (ImageView csucs:csucsok
             ) {
            vezetElHashMap.put(csucs,false);
        }
    }
    public int SetVezetElHashMap(){
        int db =0;
        for (Gametools.Vonal vonal:vonalak
             ) {
            if (vonal.isPressed()){
                if (vezetElHashMap.get(vonal.getA()) == false){
                    vezetElHashMap.put(vonal.getA(), true);
                    db++;
                }
                if (vezetElHashMap.get(vonal.getZ()) == false){
                    vezetElHashMap.put(vonal.getZ(), true);
                    db++;
                }
            }
        }
        return db;
    }

    public int AktualisSuly(){
        int vonalSuly = 0;
        if (vonalak != null) {
            for (Gametools.Vonal item : vonalak
                    ) {
                if (item.isPressed()) {
                    vonalSuly += item.getNumber();
                }
            }
        }
        return vonalSuly;
    }
    public int HanyVonalPressed(){
        int db=0;
        for (Gametools.Vonal item :vonalak
                ) {
            if (item.isPressed()){
                db++;
            }
        }
        return db;
    }
    public int OptimalisVonalSzam(){
        return csucsok.size() -1;
    }


    public boolean MegoldasEllenorzes(){
        VezetElHashMapCreator();
        int hanyhozVezetEl = SetVezetElHashMap();
        int hanyVonalPressed = HanyVonalPressed();
        int optimalisVonalSzam = OptimalisVonalSzam();
        int osszSuly = AktualisSuly();
        int minimalisFeszitoErteke = Kruskal();
        if (hanyhozVezetEl != csucsok.size())return false;
        if (hanyVonalPressed != optimalisVonalSzam) return false;
        if (osszSuly != minimalisFeszitoErteke) return false;
        return true;
    }

    public int Kruskal() {
        int value =0;
        csucsHalmaz = new ArrayList[csucsok.size()];
        for (int i = 0; i <csucsok.size(); i++){
            csucsHalmaz[i] = new ArrayList<ImageView>();
            csucsHalmaz[i].add(csucsok.get(i));
        }

        VonalListaRendez();
        for (int i = 0; i <vonalak.size(); i++) {
            ArrayList<ImageView> a = CsucsKereses(vonalak.get(i).getA());
            ArrayList<ImageView> b = CsucsKereses(vonalak.get(i).getZ());
            if (!a.equals(b)){
                value += vonalak.get(i).getNumber();
                for (int j =0; j < b.size(); j++){
                    a.add(b.get(j));
                }
                b.clear();
            }
        }
        return value;

    }

    public void VonalListaRendez(){
    Collections.sort(vonalak, new Comparator<Gametools.Vonal>() {
        @Override
        public int compare(Gametools.Vonal o1, Gametools.Vonal o2) {
            return o1.getNumber() - o2.getNumber();
        }
    });
    }

    public ArrayList<ImageView> CsucsKereses(ImageView a){
       int i = 0;
        while (!csucsHalmaz[i].contains(a)){
            i++;
        }
        return csucsHalmaz[i];
    }
}
