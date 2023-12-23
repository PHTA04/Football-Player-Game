package com.example.footballplayergame.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.footballplayergame.Models.SetModel;
import com.example.footballplayergame.R;
import com.example.footballplayergame.databinding.ItemSetsBinding;

import java.util.ArrayList;

public class SetAdapter extends RecyclerView.Adapter<SetAdapter.viewHolder> {

    Context context;
    ArrayList<SetModel> list;
    String difficultyLevel;
    private OnItemClickListener onItemClickListener;

    public SetAdapter(Context context, ArrayList<SetModel> list, String difficultyLevel) {
        this.context = context;
        this.list = list;
        this.difficultyLevel = difficultyLevel;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sets, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        final SetModel model = list.get(position);
        holder.binding.setName.setText(model.getSetName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ItemSetsBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = ItemSetsBinding.bind(itemView);
        }
    }

    // Phương thức để set sự kiện click
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    // Interface để định nghĩa sự kiện click
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
