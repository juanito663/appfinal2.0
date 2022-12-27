package com.example.bottonnav;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
public class Dashboard extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    DatabaseReference mRootReference;
    Button mButtonSubirDatosFirebase;
    EditText mE1, mE2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.dashboard);

        mButtonSubirDatosFirebase = findViewById(R.id.btnCrearDatos);
        mE1 = findViewById(R.id.idFecha);
        mE2 = findViewById(R.id.idHora);

        mRootReference = FirebaseDatabase.getInstance().getReference();


        mButtonSubirDatosFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fecha = mE1.getText().toString();
                String hora = mE2.getText().toString();

                Map<String, Object> horarios = new HashMap<>();
                horarios.put("fecha",fecha);
                horarios.put("hora",hora);

                mRootReference.child("Horarios").setValue(horarios);
            }
        });


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.dashboard:
                        return true;

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.about:
                        startActivity(new Intent(getApplicationContext(),About.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });

    }
}