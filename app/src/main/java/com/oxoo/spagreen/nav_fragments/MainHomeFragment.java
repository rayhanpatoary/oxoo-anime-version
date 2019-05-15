package com.oxoo.spagreen.nav_fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.oxoo.spagreen.R;
import com.oxoo.spagreen.fragments.HomeFragment;
import com.oxoo.spagreen.fragments.LiveTvFragment;
import com.oxoo.spagreen.fragments.MoviesFragment;

public class MainHomeFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private ImageButton btnHome;
    private Fragment fragment=null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       return inflater.inflate(R.layout.fragment_main_home, container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        btnHome=view.findViewById(R.id.home);



        btnHome.setColorFilter(getActivity().getResources().getColor(R.color.colorPrimary));

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment=new HomeFragment();
                loadFragment(fragment);

                btnHome.setColorFilter(getActivity().getResources().getColor(R.color.colorPrimary));
            }
        });

        loadFragment(new HomeFragment());

    }


    //----load fragment----------------------
    private boolean loadFragment(Fragment fragment){

        if (fragment!=null){

            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .commit();

            return true;
        }
        return false;

    }


}