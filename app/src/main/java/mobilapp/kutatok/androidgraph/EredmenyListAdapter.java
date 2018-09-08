package mobilapp.kutatok.androidgraph;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Adam on 2017.08.30..
 */

public class EredmenyListAdapter extends BaseAdapter {

    private Context context;
    private List<Ranking>lstRanking;
    private int melyikJatek;

    public EredmenyListAdapter(Context context, List<Ranking> lstRanking, int melyikJatek) {
        this.context = context;
        this.lstRanking = lstRanking;
        this.melyikJatek=melyikJatek;
    }

    @Override
    public int getCount() {
        return lstRanking.size();
    }

    @Override
    public Object getItem(int position) {
        return lstRanking.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        if(this.melyikJatek==1){
            view=inflater.inflate(R.layout.row,null);
        }
        else{
            view=inflater.inflate(R.layout.row_inv,null);
        }

        TextView nameText=(TextView)view.findViewById(R.id.nameText);
        TextView scoreText=(TextView)view.findViewById(R.id.scoreText);

        nameText.setText(lstRanking.get(position).getName());
        scoreText.setText(Integer.toString(lstRanking.get(position).getScore()));

        return view;

    }
}
