package com.example.my_application;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Board board;
    Handler handler;
    SharedPreferences sp;
    FrameLayout frameLayout;
    boolean isLoose = false;
    TextView scoreTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("info", 0);
        handler = new Handler(new Handler.Callback()
        {
            @Override
            public boolean handleMessage(Message message) { // when lost directed to here
                SharedPreferences.Editor editor = sp.edit();
                Map map = sp.getAll();
                ArrayList<Integer> records = new ArrayList<> (map.values());
                records.add(message.what);
                records.sort((curr,next) -> next-curr);
                if(records.size() >= 5)
                {
                    records.remove(records.size()-1);
                }
                for(int i = 0; i <records.size();i++)
                {
                    editor.putInt(""+i,records.get(i));
                }
                editor.commit();
                dialogBuilder(message.what);
                return true;
            }
        });

        board = new Board(this, handler);

        frameLayout = findViewById(R.id.mainFrame);
        frameLayout.addView(board);

        ImageButton btnPause = findViewById(R.id.btnPause);
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Board.isPaused=true;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(getString(R.string.pause_game)).setCancelable(false).setPositiveButton(getString(R.string.press_continue), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Board.isPaused=false;
                    }
                }).setNegativeButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       finish();
                    }
                }).show();
            }
        });

    }

    public void dialogBuilder(int score)
    {
        isLoose = true;
        Board.isPaused = true;
        View dialogView2 = getLayoutInflater().inflate(R.layout.loss_dialog, null, false);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        scoreTv = dialogView2.findViewById(R.id.resultTv);
        scoreTv.setText(getString(R.string.score) + score);
        builder.setView(dialogView2).setCancelable(false).setPositiveButton(getString(R.string.back_screen), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }

        }).setNegativeButton(getString(R.string.play_again), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                board = new Board(getBaseContext(), handler);
                frameLayout.removeAllViews();
                frameLayout.addView(board);
                isLoose = false;
                Board.isPaused = false;
            }
        }).show();
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        Board.isPaused = true;
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_back_presssed, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView).setCancelable(false).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Board.isPaused = false;
            }
        }).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Board.isPaused = true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        Board.isPaused = false;
    }
}


