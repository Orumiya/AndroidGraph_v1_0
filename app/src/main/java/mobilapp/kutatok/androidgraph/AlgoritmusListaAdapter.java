package mobilapp.kutatok.androidgraph;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mobilapp.kutatok.androidgraph.box.kezdokep_alg;

/**
 * Created by Orumiya on 2017.07.12..
 */

public class AlgoritmusListaAdapter extends PagerAdapter {

    Context mContext;
    List<AlgoritmusListaElem>lista=new ArrayList<AlgoritmusListaElem>();
    LayoutInflater layoutInflater;

    public AlgoritmusListaAdapter(Context mContext, List<AlgoritmusListaElem>lista) {
        this.lista = lista;
        this.mContext = mContext;
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View v = layoutInflater.inflate(R.layout.listasor,container,false);

        ImageView imageView = (ImageView)v.findViewById(R.id.alglistaimg1);
        TextView text1 = (TextView)v.findViewById(R.id.name);
        TextView text2 = (TextView)v.findViewById(R.id.description);
        imageView.setImageResource(lista.get(position).getKepId());
        text1.setText(lista.get(position).getNev());
        text2.setText(lista.get(position).getLeiras());
        container.addView(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,kezdokep_alg.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("CLICK_INDEX",position);
                mContext.startActivity(intent);
            }
        });
        return  v;


    }
}
