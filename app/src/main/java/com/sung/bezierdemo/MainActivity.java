package com.sung.bezierdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{
    private SeekBar pointBar;
    private TextView simple;
    private TextView pointNum;
    private BezierView bezier;

    private int pointNums = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        simple = findViewById(R.id.simple_demo);
        pointBar = findViewById(R.id.point_total);
        pointNum = findViewById(R.id.point_num);
        bezier = findViewById(R.id.bezier);

        pointBar.setOnSeekBarChangeListener(this);
        simple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent simple = new Intent();
                simple.setClass(MainActivity.this,Main2Activity.class);
                startActivity(simple);
            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser) return;
        pointNums = progress + 3;
        pointNum.setText(String.valueOf(pointNums));
        bezier.setmPointsNumber(pointNums);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
