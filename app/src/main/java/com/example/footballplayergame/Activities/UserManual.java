package com.example.footballplayergame.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.footballplayergame.MainActivity;
import com.example.footballplayergame.R;
import com.example.footballplayergame.databinding.ActivityUserManualBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class UserManual extends AppCompatActivity {

    ActivityUserManualBinding binding;
    FirebaseAuth auth;
    private ViewFlipper viewFlipper;
    private TabLayout indicator;
    private float lastX;
    private Handler autoUpdateHandler = new Handler();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserManualBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();

        viewFlipper = findViewById(R.id.viewFlipper);
        indicator = findViewById(R.id.indicator);

        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.startFlipping();

        for (int i = 0; i < viewFlipper.getChildCount(); i++) {
            TabLayout.Tab tab = indicator.newTab();
            indicator.addTab(tab);
        }

        // Thêm một TabSelectedListener để theo dõi sự kiện khi người dùng chọn tab
        indicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Khi người dùng chọn một tab, chuyển đến View tương ứng trong ViewFlipper
                viewFlipper.setDisplayedChild(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Không cần xử lý khi tab không được chọn
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Không cần xử lý khi tab được chọn lại
            }
        });

        // Xử lý sự kiện vuốt để chuyển đổi giữa các CardView
        viewFlipper.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    lastX = event.getX();
                    break;
                case MotionEvent.ACTION_UP:
                    float currentX = event.getX();
                    if (lastX < currentX) {
                        // Vuốt sang phải, hiển thị CardView trước
                        viewFlipper.showPrevious();
                    } else if (lastX > currentX) {
                        // Vuốt sang trái, hiển thị CardView tiếp theo
                        viewFlipper.showNext();
                    }
                    // Cập nhật tab tương ứng với CardView hiện tại
                    indicator.getTabAt(viewFlipper.getDisplayedChild()).select();
                    break;
            }
            return true;
        });

        autoUpdateHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Cập nhật tab tương ứng với CardView hiện tại
                indicator.getTabAt(viewFlipper.getDisplayedChild()).select();
                // Tiếp tục đặt lịch trình cập nhật sau 3 giây nữa
                autoUpdateHandler.postDelayed(this, 1000);
            }
        }, 1000);


        binding.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserManual.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.btnUserManual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserManual.this, UserManual.class);
                startActivity(intent);
                finish();
            }
        });

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(UserManual.this,
                        GoogleSignInOptions.DEFAULT_SIGN_IN);
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(UserManual.this, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }
    @Override
    protected void onDestroy() {
        // Dừng việc cập nhật khi Activity bị hủy
        autoUpdateHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}