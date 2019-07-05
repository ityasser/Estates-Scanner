package points.comparison.app.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import points.comparison.app.CustomAdapter.CustomDrawerAdapter;
import points.comparison.app.R;
import points.comparison.app.fragments.AboutFragment;
import points.comparison.app.fragments.HomePageFragment;
import points.comparison.app.fragments.Point1Fragment;
import points.comparison.app.models.DrawerItem;
import points.comparison.app.utils.Utility;

public class MainActivity extends BaseActivity {
    public final static int  Home=111;
    public final static int logout=2222;
    public final static int about_app=3333;

    public final static int service_add=88888;

    @BindView(R.id.lielift)
    LinearLayout lielift;

    @BindView(R.id.left_drawer)
    ListView mDrawerList;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;





    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.prof)
    CircleImageView prof;

    @BindView(R.id.uname)
    TextView uname;

    View last_view;





    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    CustomDrawerAdapter adapter;
    List<DrawerItem> itemsDrawer;
    public ActionBarDrawerToggle mDrawerToggle;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);




        prof.setImageResource(R.drawable.icon);
        uname.setText( "welcome");

        itemsDrawer = new ArrayList<DrawerItem>();
        Log.d("is_admin", Utility.getStringFromPref(MainActivity.this, "is_admin") );


            itemsDrawer.add(new DrawerItem(getString(R.string.home), R.drawable.home,Home));
            itemsDrawer.add(new DrawerItem(getString(R.string.StartAnalysis), R.drawable.logout,service_add));
        itemsDrawer.add(new DrawerItem(getString(R.string.about_app), R.drawable.about,about_app));
        itemsDrawer.add(new DrawerItem(getString(R.string.logou), R.drawable.ic_menu_logout,logout));















        mTitle = mDrawerTitle = getTitle();
        adapter = new CustomDrawerAdapter(this, R.layout.item_custom_drawer, itemsDrawer);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        setSupportActionBar(toolbar);
        this.getSupportActionBar().setHomeButtonEnabled(true);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.getSupportActionBar().setDisplayShowTitleEnabled(true);



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {


                Utility.setTitle(mTitle+"", MainActivity.this);

                Log.d("mTitle",mTitle+"");
                invalidateOptionsMenu(); // creates call to
                // onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                // MainActivity.this.getSupportActionBar().setTitle(mDrawerTitle);
                Log.d("sdsadsadsada","sdsdsdsdsdsdsd");
                Utility.setTitle("Main Menu ", MainActivity.this);

                invalidateOptionsMenu(); // creates call to

            }
        };


        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerToggle.syncState();




       HomePageFragment servicesFragment = new HomePageFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, servicesFragment).commit();
        mTitle=getString(R.string.home);

    }

    public void setTitle(String title){
        Utility.setTitle(title+"", MainActivity.this);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();



    }

    public boolean isGPSEnabled() {
        return ((LocationManager) MainActivity.this.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER);
    }



    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            TextView ItemName;

            if(last_view!=null){
                ItemName = (TextView) last_view .findViewById(R.id.drawer_itemName);

                last_view.setBackgroundColor(Color.parseColor("#f4f4f4"));


            }

            last_view =  parent.getChildAt(position);

            last_view.setBackgroundColor(Color.parseColor("#c0d7da"));



        /*    for(int i=0; i<parent.getCount();i++)
            {
                parent.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
            }

            view.setBackgroundColor(Color.parseColor("#f2f2f2"));*/

            Log.d("ddd",position+"");

            SelectItem(position);


        }
    }



    public void SelectItem(int possition) {
        SlectItemMain(possition);

        mDrawerList.setItemChecked(possition, true);
        mDrawerLayout.closeDrawer(lielift);
        Utility.setTitle(itemsDrawer.get(possition).getItemName(),this);

    }
    public void SlectItemMain(final int possition){

        switch (itemsDrawer.get(possition).getIdItem()) {



            case logout:

                finish();
                break;







            case Home:

                HomePageFragment homeMapFragment = new HomePageFragment();
                getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, homeMapFragment).commit();
                mTitle=itemsDrawer.get(possition).getItemName();
                break;




            case about_app:
                getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, AboutFragment.newInstance()).commit();

                mTitle=itemsDrawer.get(possition).getItemName();

                break;

            case service_add:
                Point1Fragment point1Fragment=new Point1Fragment();
                getSupportFragmentManager().beginTransaction().addToBackStack("").replace(R.id.container, point1Fragment).commit();

                mTitle=itemsDrawer.get(possition).getItemName();

                break;




        }
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(Gravity.START); //CLOSE Nav Drawer!
        }else{
            super.onBackPressed();
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);







    }
}
