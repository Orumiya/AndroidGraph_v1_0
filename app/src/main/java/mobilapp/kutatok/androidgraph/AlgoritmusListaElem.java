package mobilapp.kutatok.androidgraph;

/**
 * Created by Orumiya on 2017.07.12..
 */

public class AlgoritmusListaElem {
    String nev;
    String leiras;
    int kepId;

    public AlgoritmusListaElem(String nev, String leiras, int kepId) {
        this.nev = nev;
        this.leiras = leiras;
        this.kepId = kepId;
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public String getLeiras() {
        return leiras;
    }

    public void setLeiras(String leiras) {
        this.leiras = leiras;
    }

    public int getKepId() {
        return kepId;
    }

    public void setKepId(int kepId) {
        this.kepId = kepId;
    }
}
