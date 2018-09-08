package mobilapp.kutatok.androidgraph;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Done extends AppCompatActivity {

    Button btnTryAgain, btnPontMentes;
    TextView txtTotalScore, uzenet;
    ProgressBar progressBar;
    EditText editName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        final DbHelper dbHelper=new DbHelper(this);
        final SQLiteDatabase db=dbHelper.getWritableDatabase();

        btnTryAgain=(Button)findViewById(R.id.btnTryAgain);
        btnPontMentes=(Button)findViewById(R.id.btnPontMentes);
        txtTotalScore=(TextView)findViewById(R.id.txtTotalScore);
        progressBar=(ProgressBar)findViewById(R.id.doneProgressBar);
        editName=(EditText)findViewById(R.id.editName);
        uzenet=(TextView)findViewById(R.id.uzenet);

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Bundle extra=getIntent().getExtras();
        if(extra!=null){
            final int score=extra.getInt("SCORE");
            final int melyikJatek=extra.getInt("MELYIKJATEK");
            int szazalek = 0;

            txtTotalScore.setText(String.format("Pontszám: %d",score));
            if(melyikJatek==1){
                progressBar.setMax(110);
                if(score<110){
                    uzenet.setText("Az üzemanyag a cél előtt elfogyott.");
                    uzenet.setTextColor(Color.RED);
                }
                else{
                    uzenet.setText("A küldetés sikerült.");
                    uzenet.setTextColor(Color.WHITE);
                }

            }
            else{
                progressBar.setMax(260);
                uzenet.setText("Játék vége");

            }
            progressBar.setProgress(score);

            btnPontMentes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name;
                    if(editName.getText().toString().equals(""))
                        name="Játékos";
                    else
                        name=editName.getText().toString();

                    ContentValues rankingValues = new ContentValues();
                    rankingValues.put("NAME", name);
                    rankingValues.put("SCORE", score);
                    rankingValues.put("GAMETYPE", melyikJatek);
                    db.insert("RANKING",null, rankingValues);
                    db.close();

                    Intent intentMain=new Intent(getApplicationContext(),JatekValaszto.class);
                    startActivity(intentMain);
                    finish();

                }
            });



        }


    }
}
