package com.example.android.thirdeye;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import static android.R.attr.data;
import static android.R.attr.x;
import static android.os.Build.VERSION_CODES.N;

public class MainActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    TextToSpeech t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView)this.findViewById(R.id.imageView1);
        /*Button photoButton = (Button) this.findViewById(R.id.click);
        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(cameraIntent, CAMERA_REQUEST);}
            }
        });*/

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    Locale locale = new Locale("en", "IN");
                    int availability = t1.isLanguageAvailable(locale);
                    switch (availability) {
                        case TextToSpeech.LANG_NOT_SUPPORTED: {
                            t1.setLanguage(Locale.UK);
                            break;
                        }
                        case TextToSpeech.LANG_MISSING_DATA: {
                            t1.setLanguage(Locale.UK);
                            break;
                        }
                        case TextToSpeech.LANG_AVAILABLE: {
                            t1.setLanguage(Locale.UK);
                            break;
                        }
                        case TextToSpeech.LANG_COUNTRY_AVAILABLE:
                        case TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE: {
                            t1.setLanguage(locale);
                            break;
                        }
                    }
                }
            }
        });
    }
    void  search(View v){
        EditText g = (EditText) findViewById(R.id.newText);
        String x = g.getText().toString();
        if (x.matches("")) {
            Toast.makeText(this, "You did not enter anything", Toast.LENGTH_SHORT).show();
            return;
        }
            Uri uri = Uri.parse("http://www.google.com/#q=" + x);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
    }
    void  voice(View v){
        EditText g = (EditText) findViewById(R.id.newText);
        String x = g.getText().toString();
        if (x.matches("")) {
            Toast.makeText(this, "You did not enter anything", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getApplicationContext(), x,Toast.LENGTH_SHORT).show();
        t1.speak(x, TextToSpeech.QUEUE_FLUSH, null);
        //t1.speak(x.toCharArray(),TextToSpeech.QUEUE_FLUSH, null,null);
    }
    @Override
    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

    void  click(View v){
     Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_REQUEST);
        }

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    }


}
