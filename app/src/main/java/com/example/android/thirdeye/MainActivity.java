package com.example.android.thirdeye;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.R.attr.bitmap;
import static android.R.attr.data;
import static android.R.attr.x;
import static android.os.Build.VERSION_CODES.N;

public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    TextToSpeech t1;
    ImageView color;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView)this.findViewById(R.id.imageView1);
        color = (ImageView)this.findViewById(R.id.colored);
        /*Button photoButton = (Button) this.findViewById(R.id.click);
        photoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(cameraIntent, CAMERA_REQUEST);}
            }
        });*/
        color.setVisibility(View.INVISIBLE);
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
    public void  voice(View v){
        EditText g = (EditText) findViewById(R.id.newText);
        String x = g.getText().toString();
        if (x.matches("")) {
            Toast.makeText(this, "You did not enter anything", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getApplicationContext(), x,Toast.LENGTH_SHORT).show();
        t1.speak(x, TextToSpeech.QUEUE_FLUSH, null);
        //t1.speak(x.toCharArray(),TextToSpeech.QUEUE_FLUSH, null,null);
        /*MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.audiocheck);
        Toast.makeText(getApplicationContext(), "Playing sound",Toast.LENGTH_SHORT).show();
                mediaPlayer.start();*/
    }
    @Override
    public void onDestroy(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onDestroy();
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

    public void getColor(View view){
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            //Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),imageView.getId());
            if(bitmap!=null) {
                List<Palette.Swatch> swatchesTemp = Palette.from(bitmap).generate().getSwatches();
                List<Palette.Swatch> swatches = new ArrayList<Palette.Swatch>(swatchesTemp);
                Collections.sort(swatches, new Comparator<Palette.Swatch>() {
                    @Override
                    public int compare(Palette.Swatch swatch1, Palette.Swatch swatch2) {
                        return swatch2.getPopulation() - swatch1.getPopulation();
                    }
                });

                if (swatches.size() > 0)
                {
                    int argb = swatches.get(0).getRgb();
                    //int r = (argb>>16)&0xFF;
                    //int g = (argb>>8)&0xFF;
                    //int b = (argb>>0)&0xFF;
                    String hex = "#"+Integer.toHexString(argb).substring(2);
                    color.setVisibility(View.VISIBLE);
                    color.setBackgroundColor(argb);
                    Toast.makeText(getApplicationContext(),hex, Toast.LENGTH_SHORT).show();
                }
                else{
                    color.setVisibility(View.INVISIBLE);

                }
            }

    }




}
