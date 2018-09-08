package mobilapp.kutatok.androidgraph.box;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import mobilapp.kutatok.androidgraph.R;

public class kezdokep_alg extends AppCompatActivity {
    //public static final String CLICK_INDEX = "index";

    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kezdokep_alg);



        Intent intent = getIntent();
        index = intent.getExtras().getInt("CLICK_INDEX");

        //ez a view, amiben a fragmentek mozognak
        ViewPager viewPager = (ViewPager)findViewById(R.id.boxViewPager);
        //the adapter knows which fragment should be on the screen
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(),index);

        //sets the adapter into the view pager
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout)findViewById(R.id.slidingTabs);
        tabLayout.setupWithViewPager(viewPager);
    }
}
