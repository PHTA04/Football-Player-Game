package com.example.footballplayergame.Activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.footballplayergame.MainActivity;
import com.example.footballplayergame.R;
import com.example.footballplayergame.databinding.ActivityScoreBinding;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {

    ActivityScoreBinding binding;
    private MediaPlayer mediaPlayerEndGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        PieChart pieChart = findViewById(R.id.pieChart);

        int totalScore = getIntent().getIntExtra("total", 0);
        int correctAnsw = getIntent().getIntExtra("score", 0);
        int wrong = totalScore - correctAnsw;

        binding.totalQuestions.setText(String.valueOf(totalScore));
        binding.rightAnsw.setText(String.valueOf(correctAnsw));
        binding.wrongAnsw.setText(String.valueOf(wrong));


        // Tạo danh sách các PieEntry
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(correctAnsw, "Correct"));
        entries.add(new PieEntry(wrong, "Wrong"));

        // Cấu hình DataSet
        PieDataSet dataSet = new PieDataSet(entries, "Score");
//        dataSet.setColors(new int[]{android.R.color.holo_green_light, android.R.color.holo_red_light}, this);
        dataSet.setColors(new int[]{ContextCompat.getColor(this, R.color.right_ans), ContextCompat.getColor(this, R.color.wrong_ans)});
        dataSet.setValueTextColor(getResources().getColor(android.R.color.white));
        dataSet.setValueTextSize(12f);

        // Tạo PieData và cấu hình
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter(pieChart));
        pieChart.setData(data);

        // Cấu hình PieChart
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setEntryLabelColor(getResources().getColor(android.R.color.white));
        pieChart.setEntryLabelTextSize(12f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(getResources().getColor(android.R.color.transparent));
        pieChart.setTransparentCircleColor(getResources().getColor(android.R.color.transparent));
        pieChart.setTransparentCircleRadius(58f);
        pieChart.setHoleRadius(58f);
        pieChart.animateY(1400);
        pieChart.invalidate();

        // Cấu hình chú thích (Legend)
        Legend legend = pieChart.getLegend();
        legend.setFormSize(11f);
        legend.setTextSize(11f);
        legend.setTextColor(getResources().getColor(android.R.color.white));

        binding.btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScoreActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}