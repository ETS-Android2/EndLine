package com.example.endline_v1.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.endline_v1.util.DisplayDataFromFirebase;
import com.example.endline_v1.R;
import com.example.endline_v1.ui.all.AllFragment;
import com.example.endline_v1.ui.beauty.BeautyFragment;
import com.example.endline_v1.ui.food.FoodFragment;
import com.example.endline_v1.ui.health.HealthFragment;
import com.example.endline_v1.ui.medical.MedicalFragment;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView recyclerView;
    private Activity activity;
    private DisplayDataFromFirebase displayer;
    private CardView cv_all, cv_food, cv_beauty, cv_medical, cv_health;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        cv_all = (CardView) root.findViewById(R.id.cv_all);
        cv_beauty = (CardView) root.findViewById(R.id.cv_beauty);
        cv_medical = (CardView) root.findViewById(R.id.cv_medical);
        cv_food = (CardView) root.findViewById(R.id.cv_food);
        cv_beauty = (CardView) root.findViewById(R.id.cv_beauty);
        cv_health = (CardView) root.findViewById(R.id.cv_health);

        cv_all.setOnClickListener(listener);
        cv_beauty.setOnClickListener(listener);
        cv_medical.setOnClickListener(listener);
        cv_food.setOnClickListener(listener);
        cv_health.setOnClickListener(listener);

        return root;
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            switch (view.getId()){
                case R.id.cv_all:
                    Fragment fragment_all = new AllFragment();
                    transaction.replace(R.id.nav_host_fragment, fragment_all);
                    transaction.commit();
                    break;
                case R.id.cv_beauty:
                    Fragment fragment_beauty = new BeautyFragment();
                    transaction.replace(R.id.nav_host_fragment, fragment_beauty);
                    transaction.commit();
                    break;
                case R.id.cv_health:
                    Fragment fragment_health = new HealthFragment();
                    transaction.replace(R.id.nav_host_fragment, fragment_health);
                    transaction.commit();
                    break;
                case R.id.cv_medical:
                    Fragment fragment_medical = new MedicalFragment();
                    transaction.replace(R.id.nav_host_fragment, fragment_medical);
                    transaction.commit();
                    break;
                case R.id.cv_food:
                    Fragment fragment_food = new FoodFragment();
                    transaction.replace(R.id.nav_host_fragment, fragment_food);
                    transaction.commit();
                    break;
            }
        }
    };
}