package mobilapp.kutatok.androidgraph;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Maininfo extends AppCompatActivity {

    //megnyitja az adott layoutot - 3 felbontas kozul tud valasztani
    // --> activity_maininfo
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maininfo);
    }
}
