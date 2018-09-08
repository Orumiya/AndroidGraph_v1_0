package mobilapp.kutatok.androidgraph;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 2017.08.30..
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "graphdb";
    private static final int DB_VERSION = 1;
    //SQLiteDatabase db;


    public DbHelper(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        UpdateMyDatabase(db, 0, DB_VERSION);
        //this.db=db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        UpdateMyDatabase(db, oldVersion,newVersion);
    }

    private void UpdateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion){
        if (oldVersion < 1){
            db.execSQL("CREATE TABLE RANKING (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + "NAME TEXT, "
                        + "SCORE INTEGER, "
                        + "GAMETYPE INTEGER);");
        }
    }

    /*public void InsertRanking(String name, int score, int gameType){
        ContentValues rankingValues = new ContentValues();
        rankingValues.put("NAME", name);
        rankingValues.put("SCORE", score);
        rankingValues.put("GAMETYPE", gameType);
        //db.insert("RANKING",null, rankingValues);
    }*/

    public List<Ranking> getRanking(int gametype){
        List<Ranking> listRanking = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String lekerdezes = "SELECT * FROM RANKING WHERE GAMETYPE = "+gametype +" ORDER BY SCORE DESC;";

        Cursor c;
        try{
           c = db.rawQuery(lekerdezes,null);
            if (c == null) return null;
            c.moveToNext();

            do{
                int id = c.getInt(c.getColumnIndex("_id"));
                int score = c.getInt(c.getColumnIndex("SCORE"));
                String name = c.getString(c.getColumnIndex("NAME"));
                int gameType = c.getInt(c.getColumnIndex("GAMETYPE"));

                Ranking ranking = new Ranking(id,score,name,gameType);
                listRanking.add(ranking);
            }while (c.moveToNext());
            c.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        db.close();
        return listRanking;
    }
}
