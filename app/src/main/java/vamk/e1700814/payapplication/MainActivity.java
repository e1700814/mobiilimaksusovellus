package vamk.e1700814.payapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;

import com.e1700814.payapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import vamk.e1700814.payapplication.api.SessionPreferences;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainerView);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        bottomNavigationView.setVisibility(View.GONE);

        SessionPreferences sessionPreferences = new SessionPreferences(this);
        sessionPreferences.setRecipientId(null);
        sessionPreferences.setRecipientName(null);
        sessionPreferences.setRecipientPhone(null);
    }
}