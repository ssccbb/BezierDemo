package com.sung.bezierdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener{
    private SeekBar pointBar;
    private TextView pointNum;
    private BezierView bezier;

    private int pointNums = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pointBar = findViewById(R.id.point_total);
        pointNum = findViewById(R.id.point_num);
        bezier = findViewById(R.id.bezier);

        pointBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (!fromUser) return;
        pointNums = progress + 2;
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
