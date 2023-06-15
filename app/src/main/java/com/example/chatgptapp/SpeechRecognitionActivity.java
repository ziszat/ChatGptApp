package com.example.chatgptapp;

import static android.speech.SpeechRecognizer.isRecognitionAvailable;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatgptapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class SpeechRecognitionActivity extends AppCompatActivity {

    private static final int SPEECH_REQUEST_CODE = 123;

    private Button button;
    private TextView textView;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speechtext);

//        ArrayLimainst<String> supportedLanguages = new ArrayList<>( Arrays.asList(RecognizerIntent.getVoiceDetailsIntent(this).getStringArrayListExtra(RecognizerIntent.EXTRA_SUPPORTED_LANGUAGES)));
        Log.d("Supported Languages", Locale.getDefault().toString());
        ;
        button = findViewById(R.id.btn_speech_to_text);
        textView = findViewById(R.id.tv_recognized_text);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpeechToText();
            }
        });
    }

    private void startSpeechToText() {

        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        if(isRecognitionAvailable(getApplicationContext())){

            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        }else{
            Toast.makeText(getApplicationContext(),
                    "Oops! Your device doesn't support Speech to Text",
                    Toast.LENGTH_SHORT).show();  }


        try {
            startActivityForResult(intent, SPEECH_REQUEST_CODE);
            textView.setText("");
        } catch (ActivityNotFoundException a) {
            Toast t = Toast.makeText(getApplicationContext(),
                    "Opps! Your device doesn't support Speech to Text",
                    Toast.LENGTH_SHORT);
            t.show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("SpeechRecognition", "onActivityResult - requestCode: " + requestCode + ", resultCode: " + resultCode);

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            Log.d("SpeechRecognition", "Recognized text: " + result.get(0));
            textView.setText(result.get(0));
        } else {
            Log.d("SpeechRecognition", "Speech recognition failed");
        }


    }
}
