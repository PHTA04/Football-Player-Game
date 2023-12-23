package com.example.footballplayergame.Adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;

import com.example.footballplayergame.Activities.UserManual;
import com.example.footballplayergame.R;

public class ViewFlipperAdapter extends PagerAdapter {

    private final FragmentActivity activity;
    private final int[] imageIds = {R.drawable.scr_home, R.drawable.scr_sets, /* Add other drawable resources here */};

    public ViewFlipperAdapter(UserManual activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return imageIds.length;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(activity);
        imageView.setImageResource(imageIds[position]);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
