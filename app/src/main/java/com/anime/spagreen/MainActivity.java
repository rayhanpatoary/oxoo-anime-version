package com.anime.spagreen;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;

import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.anime.spagreen.fragments.HomeFragment;
import com.anime.spagreen.fragments.MoviesFragment;
import com.anime.spagreen.fragments.TvSeriesFragment;
import com.anime.spagreen.models.NavigationModel;
import com.anime.spagreen.nav_fragments.FavoriteFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;

    private List<NavigationModel> list =new ArrayList<>();
    private NavigationView navigationView;
    private String[] navItemImage;

    private String[] navItemName2;
    private String[] navItemImage2;
    private boolean status=false;

    private SharedPreferences preferences;
    private TextView tvLogin,tvRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //----init---------------------------
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        navigationView = findViewById(R.id.nav_view);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        View header = navigationView.getHeaderView(0);
        tvLogin = header.findViewById(R.id.tv_login);
        tvRegister = header.findViewById(R.id.tv_register);

        //---check if login or not--------
        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        status = prefs.getBoolean("status",false);
//        if (!status){
//            startActivity(new Intent(MainActivity.this,LoginActivity.class));
//            finish();
//        }


        //----dark mode----------
        preferences=getSharedPreferences("push",MODE_PRIVATE);

        //----navDrawer------------------------
        if (status){
            navigationView.inflateMenu(R.menu.menu_nav_with_login);
            tvLogin.setVisibility(View.GONE);
            tvRegister.setVisibility(View.GONE);
        }else {
            navigationView.inflateMenu(R.menu.menu_nav_without_login);
        }
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);



        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        //----external method call--------------
        loadFragment(new HomeFragment());

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_action, menu);
        return true;
    }


    private boolean loadFragment(Fragment fragment){

        if (fragment!=null){

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .commit();

            return true;
        }
        return false;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.action_search:

                final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {

                        Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                        intent.putExtra("q",s);
                        startActivity(intent);

                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        return false;
                    }
                });

                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawers();
        }else {

            new AlertDialog.Builder(MainActivity.this).setMessage("Do you want to exit ?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(1);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).create().show();

        }
    }

    //----nav menu item click---------------
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){

            case R.id.nav_home:
                loadFragment(new HomeFragment());
                menuItem.setChecked(true);
                break;
            case R.id.nav_logout:
                logOutDialog();
                break;
            case R.id.nav_settings:
                Intent intent=new Intent(MainActivity.this,SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_fav:
                loadFragment(new FavoriteFragment());
                menuItem.setChecked(true);
                break;
            case R.id.nav_latest_epi:

                break;
            case R.id.nav_movies:
                loadFragment(new MoviesFragment());
                menuItem.setChecked(true);
                break;
            case R.id.nav_tv_series:
                loadFragment(new TvSeriesFragment());
                menuItem.setChecked(true);
                break;


        }

        mDrawerLayout.closeDrawers();


        return true;
    }

    private void logOutDialog(){

        new AlertDialog.Builder(MainActivity.this).setMessage("Are you sure to logout ?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                        editor.putBoolean("status",false);
                        editor.apply();

                        Intent intent=new Intent(MainActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).create().show();


    }

}
