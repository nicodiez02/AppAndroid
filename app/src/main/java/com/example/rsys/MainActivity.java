package com.example.rsys;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    private Button toggleActivity;
    private Button Login;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this,R.color.black));

        toggleActivity = (Button)findViewById(R.id.buttonReport);
        Login = (Button)findViewById(R.id.buttonLogin);


        toggleActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle1();
            }
        });
    }

    private void toggle1() {
        Intent intent= new Intent(this, Login.class);
        startActivity(intent);
    }

    private void toggle() {
        Intent i= new Intent(this, Reportes.class);
        startActivity(i);
    }

}