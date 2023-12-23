package com.example.footballplayergame.Activities;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.footballplayergame.Models.QuestionModel;
import com.example.footballplayergame.R;
import com.example.footballplayergame.databinding.ActivityQuestionBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class QuestionActivity extends AppCompatActivity {
    ActivityQuestionBinding binding;
    ArrayList<QuestionModel> list;
    private int count = 0;
    private int position = 0;
    private int score = 0;
    CountDownTimer timer;
    FirebaseDatabase database;
    private int set;
    private String difficultyLevel;
    private boolean checkButtonPressed = false;
    private String currentDifficultyLevel;

    private MediaPlayer mediaPlayerStart;
    private MediaPlayer mediaPlayerRightAnswer;
    private MediaPlayer mediaPlayerWrongAnswer;
    private MediaPlayer mediaPlayerTimeOut;
    private MediaPlayer mediaPlayerEndGame;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        set = getIntent().getIntExtra("setNum", 1);

        Intent intent = getIntent();
        difficultyLevel = intent.getStringExtra("difficultyLevel");

        list = new ArrayList<>();

        int selectedNumber = getIntent().getIntExtra("selectedNumber", 5);

        mediaPlayerStart = MediaPlayer.create(this, R.raw.start_game);
        mediaPlayerRightAnswer = MediaPlayer.create(this, R.raw.right_answer);
        mediaPlayerWrongAnswer = MediaPlayer.create(this, R.raw.wrong_answer);
        mediaPlayerTimeOut = MediaPlayer.create(this, R.raw.time_out);
        mediaPlayerEndGame = MediaPlayer.create(this, R.raw.end_game);

        database.getReference().child("SETS/question")
                .orderByChild("SetNum").equalTo(set)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Log.d("FirebaseData", snapshot.getValue().toString());
                            for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                                QuestionModel model = dataSnapshot.getValue(QuestionModel.class);
                                list.add(model);
                            }

                            shuffleQuestionsAndOptions();

                            if (list.size() > selectedNumber) {
                                list.subList(selectedNumber, list.size()).clear();
                            }

                            playStartSound();

                            if(list.size()>0) {
                                if ("all".equals(difficultyLevel)) {
                                    ArrayList<String> allDifficultyLevels = new ArrayList<>();
                                    allDifficultyLevels.add("easy");
                                    allDifficultyLevels.add("normal");
                                    allDifficultyLevels.add("hard");

                                    Collections.shuffle(allDifficultyLevels);
                                    currentDifficultyLevel = allDifficultyLevels.get(0);
                                    resetTimer();
                                    timer.start();
                                } else {
                                    currentDifficultyLevel = difficultyLevel;
                                    resetTimer();
                                    timer.start();
                                }

                                applyDifficultyLevelConditions();

                                Picasso.get()
                                        .load(list.get(0).getImageQues())
                                        .into(binding.ImageQues);
                                QuestionModel currentQuestion = list.get(position);
                                for (int i = 0; i < 4; i++) {
                                    binding.optionContainer.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            checkAnswer((Button) view);
                                        }
                                    });
                                }
                                playAnimation(binding.question, 0, currentQuestion.getQuestion());

                                binding.btnNext.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        btnNext();
                                    }
                                });

                                binding.btnCheck.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(!checkButtonPressed){
                                            checkButtonPressed = true;
                                            checkAnswer((Button) view);

                                            binding.inputAnsw.setEnabled(false);
                                            binding.btnCheck.setEnabled(false);
                                        }
                                    }
                                });

                                if (binding.inputAnsw.getText().toString().trim().isEmpty()){
                                    binding.btnCheck.setEnabled(false);
                                }else {
                                    binding.btnCheck.setEnabled(true);
                                }

                                binding.inputAnsw.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                                        if(charSequence.toString().trim().isEmpty()){
                                            binding.btnCheck.setEnabled(false);
                                        } else {
                                            binding.btnCheck.setEnabled(true);
                                        }
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });

                            }
                            else {
                                Toast.makeText(QuestionActivity.this, "Question not exits", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(QuestionActivity.this, "Question not exits", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(QuestionActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void btnNext(){
        if (timer != null){
            timer.cancel();
        }

        binding.btnNext.setEnabled(false);
        binding.btnNext.setAlpha(0.3f);
        enableOption(true);

        position++;
        if(position == list.size()){
            Intent intent = new Intent(QuestionActivity.this, ScoreActivity.class);
            intent.putExtra("score", score);
            intent.putExtra("total", list.size());
            startActivity(intent);
            mediaPlayerEndGame.start();
            finish();
            return;
        }

        if ("normal".equals(difficultyLevel)){
            binding.ImageNor.setVisibility(View.GONE);
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.ImageNor.setVisibility(View.VISIBLE);
                }
            }, 700);

            resetTimer();
            timer.start();
        } else if("hard".equals(difficultyLevel)){
            binding.inputAnsw.setText("");
            binding.inputAnsw.setHint("");
            binding.inputAnsw.setBackgroundResource(R.drawable.intput_hard);

            if (binding.inputAnsw.getText().toString().trim().isEmpty()){
                binding.btnCheck.setEnabled(false);
            }else {
                binding.btnCheck.setEnabled(true);
            }

            if(checkButtonPressed){
                checkButtonPressed = false;
                binding.inputAnsw.setEnabled(true);
            }

            resetTimer();
            timer.start();
        } else if ("all".equals(difficultyLevel)) {
            ArrayList<String> allDifficultyLevels = new ArrayList<>();
            allDifficultyLevels.add("easy");
            allDifficultyLevels.add("normal");
            allDifficultyLevels.add("hard");
            Collections.shuffle(allDifficultyLevels);
            currentDifficultyLevel = allDifficultyLevels.get(0);

            applyDifficultyLevelConditions();

            if("hard".equals(currentDifficultyLevel)){
                binding.inputAnsw.setText("");
                binding.inputAnsw.setHint("");
                binding.inputAnsw.setBackgroundResource(R.drawable.intput_hard);

                if (binding.inputAnsw.getText().toString().trim().isEmpty()){
                    binding.btnCheck.setEnabled(false);
                }else {
                    binding.btnCheck.setEnabled(true);
                }

                if(checkButtonPressed){
                    checkButtonPressed = false;
                    binding.inputAnsw.setEnabled(true);
                }
            }

            resetTimer();
            timer.start();
        } else {
            resetTimer();
            timer.start();
        }

        count = 0;
        Picasso.get()
                .load(list.get(position).getImageQues())
                .into(binding.ImageQues);
        playAnimation(binding.question,0,list.get(position).getQuestion());
    }

    private void playStartSound() {
        if (mediaPlayerStart.isPlaying()) {
            mediaPlayerStart.stop();
            mediaPlayerStart.reset();
        }

        mediaPlayerStart.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                resetAndPlayStartSound();
            }
        });
        mediaPlayerStart.start();
    }

    private void resetAndPlayStartSound() {
        mediaPlayerStart.reset();
        mediaPlayerStart = MediaPlayer.create(this, R.raw.start_game);
        playStartSound();
    }

    private void applyDifficultyLevelConditions() {
        if ("normal".equals(currentDifficultyLevel)) {
            binding.ImageNor.setVisibility(View.VISIBLE);
        } else {
            binding.ImageNor.setVisibility(View.GONE);
        }

        if ("hard".equals(currentDifficultyLevel)) {
            binding.inputContainer.setVisibility(View.VISIBLE);
            binding.optionContainer.setVisibility(View.GONE);
            binding.btnCheck.setVisibility(View.VISIBLE);
        } else {
            binding.inputContainer.setVisibility(View.GONE);
            binding.optionContainer.setVisibility(View.VISIBLE);
            binding.btnCheck.setVisibility(View.GONE);
        }
    }

    private void shuffleQuestionsAndOptions() {
        Collections.shuffle(list);
        for (QuestionModel question : list) {
            ArrayList<String> options = new ArrayList<>();
            options.add(question.getOptionA());
            options.add(question.getOptionB());
            options.add(question.getOptionC());
            options.add(question.getOptionD());
            Collections.shuffle(options);
            question.setOptionA(options.get(0));
            question.setOptionB(options.get(1));
            question.setOptionC(options.get(2));
            question.setOptionD(options.get(3));
        }
    }

    private void enableOption(boolean enable) {
        for(int i=0; i<4; i++){
            binding.optionContainer.getChildAt(i).setEnabled(enable);
            if(enable){
                binding.optionContainer.getChildAt(i).setBackgroundResource(R.drawable.btn_opt);
            }
        }
    }

    private void playAnimation(View view, int value, String data) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(@NonNull Animator animator) {
                        if (value == 0 && count < 4) {
                            String option = "";
                            if (count == 0) {
                                option = list.get(position).getOptionA();
                            } else if (count == 1) {
                                option = list.get(position).getOptionB();
                            } else if (count == 2) {
                                option = list.get(position).getOptionC();
                            } else if (count == 3) {
                                option = list.get(position).getOptionD();
                            }
                            playAnimation(binding.optionContainer.getChildAt(count), 0, option);
                            count++;
                        }
                    }

                    @Override
                    public void onAnimationEnd(@NonNull Animator animator) {
                        if (value == 0){
                            try{
                                ((TextView)view).setText(data);
                                binding.totalQuestion.setText(position+1+"/"+list.size());
                            } catch (Exception e){
                                ((Button)view).setText(data);
                            }
                            view.setTag(data);
                            playAnimation(view, 1, data);
                        }
                    }

                    @Override
                    public void onAnimationCancel(@NonNull Animator animator) {
                    }

                    @Override
                    public void onAnimationRepeat(@NonNull Animator animator) {
                    }
                });
    }

    private void checkAnswer(Button selectedOption) {
        if (timer != null){
            timer.cancel();
        }

        if (selectedOption == null) {
            // Handle the case where selectedOption is null
            return;
        }

        if ("hard".equals(difficultyLevel) || "hard".equals(currentDifficultyLevel)) {
            new android.os.Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    enableOption(false);
                    binding.btnNext.setEnabled(true);
                    binding.btnNext.setAlpha(1);
                }
            }, 3000);

            String userAnswer = binding.inputAnsw.getText().toString().trim();
            String correctAnswer = list.get(position).getCorrectionAnswer();

            if (userAnswer.equalsIgnoreCase(correctAnswer)) {
                binding.inputAnsw.setBackgroundResource(R.drawable.right_answ);
                mediaPlayerRightAnswer.start();
                score++;
            } else {
                binding.inputAnsw.setBackgroundResource(R.drawable.wrong_answ);
                mediaPlayerWrongAnswer.start();
                binding.inputAnsw.setHint("The correct answer is: "+correctAnswer);

                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.inputAnsw.setBackgroundResource(R.drawable.right_answ);
                        binding.inputAnsw.setText("");
                    }
                }, 2000);
            }
        } else {
            enableOption(false);
            binding.btnNext.setEnabled(true);
            binding.btnNext.setAlpha(1);

            if (selectedOption.getText().toString().equals(list.get(position).getCorrectionAnswer())) {
                selectedOption.setBackgroundResource(R.drawable.right_answ);
                mediaPlayerRightAnswer.start();
                score++;
            } else {
                selectedOption.setBackgroundResource(R.drawable.wrong_answ);
                mediaPlayerWrongAnswer.start();
//                Button correctOption = (Button) binding.optionContainer.findViewWithTag(list.get(position).getCorrectionAnswer());
//                correctOption.setBackgroundResource(R.drawable.right_answ);

                // Ensure correctOption is not null before accessing its properties
                Button correctOption = (Button) binding.optionContainer.findViewWithTag(list.get(position).getCorrectionAnswer());
                if (correctOption != null) {
                    correctOption.setBackgroundResource(R.drawable.right_answ);
                }
            }
        }
    }

    private void resetTimer() {
        long countdownTime = 100000;

        if("hard".equals(difficultyLevel)){
            countdownTime = 20000;
        }else if("normal".equals(difficultyLevel)){
            countdownTime = 15000;
        }else if("easy".equals(difficultyLevel)){
            countdownTime = 10000;
        } else if("all".equals(difficultyLevel)) {
            if("hard".equals(currentDifficultyLevel)){
                countdownTime = 20000;
            }else if("normal".equals(currentDifficultyLevel)){
                countdownTime = 15000;
            } else {
                countdownTime = 10000;
            }
        }

        timer = new CountDownTimer(countdownTime, 1000) {
            @Override
            public void onTick(long l) {
                binding.timer.setText(String.valueOf(l/1000));
            }
            @Override
            public void onFinish() {
                if(!isFinishing()){
                    if (mediaPlayerTimeOut != null) {
                        mediaPlayerTimeOut.start();
                    } else {
                        Log.e("MediaPlayer", "mediaPlayerTimeOut is null in onFinish");
                    }
                    Dialog dialog = new Dialog(QuestionActivity.this);
                    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.timeout_dialog);
                    dialog.findViewById(R.id.tryAgain).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            btnNext();
                        }
                    });
                    dialog.show();
                }
            }
        };
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mediaPlayerStart != null) {
            mediaPlayerStart.release();
            mediaPlayerStart = null;
        }

        if (mediaPlayerRightAnswer != null) {
            mediaPlayerRightAnswer.release();
            mediaPlayerRightAnswer = null;
        }

        if (mediaPlayerWrongAnswer != null) {
            mediaPlayerWrongAnswer.release();
            mediaPlayerWrongAnswer = null;
        }

        if (mediaPlayerTimeOut != null) {
            mediaPlayerTimeOut.release();
            mediaPlayerTimeOut = null;
        }

//        if (mediaPlayerEndGame != null) {
//            mediaPlayerEndGame.release();
//            mediaPlayerEndGame = null;
//        }
    }
}
