package com.example.googletranslate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    private TextView originalText, translatedText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        originalText = findViewById(R.id.text_ENG);
        translatedText = findViewById(R.id.text_SWE);


    }

    public void showSavedBtn (View view){

        SharedPreferences sp = getApplicationContext().getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
        originalText.setText(sp.getString("englishText",""));
        translatedText.setText(sp.getString("swedishText",""));



    }


}