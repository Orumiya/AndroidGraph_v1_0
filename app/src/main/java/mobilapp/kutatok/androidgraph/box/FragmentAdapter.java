package mobilapp.kutatok.androidgraph.box;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by Orumiya on 2017.07.12..
 */

public class FragmentAdapter extends FragmentPagerAdapter{

    int index;
    String[] tabNames = new String[]{"Leírás","Pszeudokód","Animáció"};
    public FragmentAdapter(FragmentManager fm, int index) {
        super(fm);
        this.index = index;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("index",index);

        Fragment fragment;

        if (position ==0){
            fragment=new leiras_fragment();
            fragment.setArguments(bundle);
            return fragment;
        } else if(position ==1) {
            fragment=new pszeudo_fragment();
            fragment.setArguments(bundle);
            return fragment;
        } else
        {
            fragment=new animacio_fragment();
            fragment.setArguments(bundle);
            return fragment;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabNames[position];
    }

}
