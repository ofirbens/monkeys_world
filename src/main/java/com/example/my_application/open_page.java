package com.example.my_application;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class open_page extends AppCompatActivity implements View.OnClickListener {

    Button btnplay;
    Switch switch1;
    private ImageView monkey;
    public static Intent serviceIntent;
    SharedPreferences sp;
    ImageButton how_button;
    AlertDialog.Builder howToPLayBuilder;
    AlertDialog howToPLayAlertDialog;
    ImageButton winbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_page);
        btnplay = findViewById(R.id.btnPlay);
        btnplay.setOnClickListener(this);
        monkey = findViewById(R.id.monkey);
        switch1 = findViewById(R.id.switch1);
        how_button = findViewById(R.id.how_button);
        how_button.setOnClickListener(this);
        winbtn = findViewById(R.id.win_button);

        sp = getSharedPreferences("info",0);
        //ListView listView = findViewById(R.id.my_list);
        String[] records = {""+sp.getInt("0",0),""+sp.getInt("1",0),""+sp.getInt("2",0),""+sp.getInt("3",0),""+sp.getInt("4",0)};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,records);

       // listView.setAdapter(adapter);

        winbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(open_page.this);
                builder.setTitle(getString(R.string.high_score)).setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setCancelable(false).show();
            }
        });

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rote);
        monkey.setAnimation(animation);
        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        btnplay.setAnimation(animation2);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        btnplay.setAnimation(animation1);
        serviceIntent = new Intent(this, MusicService.class);
        startService(serviceIntent);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    playAudio();
                } else {
                    stopAudio();
                }
            }
        });
    }

    public void playAudio() {
        Toast.makeText(this, "music is on ", Toast.LENGTH_SHORT).show();
        startService(serviceIntent);
    }

    public void stopAudio() {
        stopService(serviceIntent);
    }


    public void onClick(View v) {

        if (v == how_button) {

            View dialogView = getLayoutInflater().inflate(R.layout.dialog_help, null, false);
            howToPLayBuilder = new AlertDialog.Builder(this);
            howToPLayBuilder.setView(dialogView);
            howToPLayAlertDialog = howToPLayBuilder.create();
            TextView text_help;
            text_help = dialogView.findViewById(R.id.text_help);
            ImageView monkey1;
            monkey1 = dialogView.findViewById(R.id.monkey1);
            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale);
            monkey1.setAnimation(animation);
            ImageView banana1;
            banana1 = dialogView.findViewById(R.id.banana1);
            Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale1);
            banana1.setAnimation(animation1);
            howToPLayBuilder.setView(dialogView).setCancelable(false).setNegativeButton( getString(R.string.back), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).show();
        }

        if (v == btnplay) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    public void onBackPressed() {
        // super.onBackPressed();
        Board.isPaused = true;
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_back_presssed, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView).setCancelable(false).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                stopAudio();
                finish();
            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Board.isPaused = false;
            }
        }).show();
    }

}

