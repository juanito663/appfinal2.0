package com.example.bottonnav;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText mEditTextMensaje;
    private Button mBtnCrearDatos;
    private DatabaseReference mDatabase;

    Button Aon, Coff;
    TextView display;
    Boolean connected = false;
    SeekBar seekBar;
    int seeked;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.home);

        Aon = findViewById(R.id.abrir);
        Coff = findViewById(R.id.cerrar);
        seekBar = findViewById(R.id.seekBar);
        display = findViewById(R.id.display);
        seekBar.setMax(360);


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        if (!connected) {
            display.setText("No Internet");
            display.setTextColor(Color.BLACK);
            display.setBackgroundColor(Color.RED);
        } else {

            // Write a message to the database
            final FirebaseDatabase database = FirebaseDatabase.getInstance();

            final DatabaseReference onlineDbStatus = database.getReference("online");
            onlineDbStatus.setValue(0);
            final DatabaseReference intensityDbStatus = database.getReference("angle");

            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

            // Read from the database
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String onlinedb = dataSnapshot.child("online").getValue().toString();
                    String withXtdb = dataSnapshot.child("withXt").getValue().toString();
                    String withAsusdb = dataSnapshot.child("withAsus").getValue().toString();

                    if (Integer.parseInt(onlinedb) == 0) {
                        display.setText("Node MCU is Offline");
                        display.setTextColor(Color.RED);
                        display.setBackgroundColor(Color.YELLOW);
                    }
                    if (Integer.parseInt(onlinedb) == 1) {

                        if (Integer.parseInt(withXtdb) == 1) {
                            display.setText("Node MCU is Online with XT");
                            display.setTextColor(Color.BLUE);
                            display.setBackgroundColor(Color.TRANSPARENT);
                        }

                        if (Integer.parseInt(withAsusdb) == 1) {
                            display.setText("Node MCU is Online with ASUS");
                            display.setTextColor(Color.BLUE);
                            display.setBackgroundColor(Color.TRANSPARENT);
                        }


                    }


                }

                @Override
                public void onCancelled(DatabaseError error) {

                }
            });


            Aon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference ledDb = database.getReference("ledDb");
                    ledDb.setValue(1);
                }
            });

            Coff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatabaseReference ledDb = database.getReference("ledDb");
                    ledDb.setValue(0);
                }
            });


            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                    seeked = i;
                    seekBar.setProgress(seeked);
                    display.setText("Rotation in deg. "+ seeked);

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    intensityDbStatus.setValue(seeked);
                }
            });


        }


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(),Dashboard.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.home:
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