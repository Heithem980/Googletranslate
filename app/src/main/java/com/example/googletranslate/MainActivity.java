package com.example.googletranslate;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.contentcapture.ContentCaptureSession;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView englishText;
    private TextView swedishText;
    private Button translate;
    SharedPreferences sp;



        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        englishText = findViewById(R.id.englishText);
        swedishText = findViewById(R.id.swedishText);
        translate = findViewById(R.id.translateBtn);

        translate.setOnClickListener(this);



        }

    public void nextPage(View view){
        Intent intent = new Intent(this, SecondActivity.class);

        startActivity(intent);
    }

    public void saveTranslation(View view){

        sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();


        editor.putString("englishText",englishText.getText().toString());
        editor.putString("swedishText",swedishText.getText().toString());

        editor.commit();
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);

    }


        public void getSpeechInput(View view) {

            resultLauncher.launch(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                    .putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                .putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault()));


        }
        ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {

        @Override
        public void onActivityResult(ActivityResult result) {

            if (result.getResultCode() == Activity.RESULT_OK) {

                Intent intent = result.getData();

                ArrayList<String> resultAr = intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                englishText.setText(resultAr.get(0));
            }

        }
    });




    @Override
    public void onClick(View view) {
        translateText(englishText.getText().toString());
    }

    private void translateText(String textToTranslate) {

        swedishText.setText("Downloading language...");

        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.SWEDISH)
                .build();

        final Translator englishSwedishTranslator =
                Translation.getClient(options);

        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();
        englishSwedishTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {

                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this,"Failed to translate : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
        englishSwedishTranslator.translate(textToTranslate)
                .addOnSuccessListener(
                        new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                swedishText.setText(o.toString());
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Error.
                                Toast.makeText(MainActivity.this,"Failed to translate : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


    }

}