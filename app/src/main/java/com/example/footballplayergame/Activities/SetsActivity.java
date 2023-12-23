package com.example.footballplayergame.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.footballplayergame.Adapters.SetAdapter;
import com.example.footballplayergame.Models.SetModel;
import com.example.footballplayergame.R;
import com.example.footballplayergame.databinding.ActivitySetsBinding;

import java.util.ArrayList;

public class SetsActivity extends AppCompatActivity {

    ActivitySetsBinding binding;
    ArrayList<SetModel>list;
    String difficultyLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String nameCategory = getIntent().getStringExtra("nameCategory");
        binding.nameCategory.setText(nameCategory);

        Intent intent = getIntent();
        difficultyLevel = intent.getStringExtra("difficultyLevel");

        list = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.setsRecy.setLayoutManager(linearLayoutManager);

        list.add(new SetModel("V-League", 1));
        list.add(new SetModel("Champions-League", 2));
        list.add(new SetModel("La Liga", 3));
        list.add(new SetModel("Serie A", 4));
        list.add(new SetModel("Ligue 1", 5));
        list.add(new SetModel("Bundesliga", 6));
        list.add(new SetModel("Premier League", 7));

        SetAdapter adapter = new SetAdapter(this, list, difficultyLevel);
        binding.setsRecy.setAdapter(adapter);

        binding.icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        adapter.setOnItemClickListener(new SetAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                showNumberPickerDialog(position);
            }
        });
    }

    private void showNumberPickerDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_number, null);
        builder.setView(dialogView);

        final NumberPicker numberPicker = dialogView.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(5);
        numberPicker.setMaxValue(10);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedNumber = numberPicker.getValue();
                Intent intent = new Intent(SetsActivity.this, QuestionActivity.class);
                intent.putExtra("set", list.get(position).getSetName());
                intent.putExtra("setNum", list.get(position).getSetNum());
                intent.putExtra("difficultyLevel", difficultyLevel);
                intent.putExtra("selectedNumber", selectedNumber);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }
}