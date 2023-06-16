package com.example.chatgptapp.appView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatgptapp.MainActivity;
import com.example.chatgptapp.R;
import com.example.chatgptapp.model.App;

public class AppGridAdapter extends RecyclerView.Adapter<AppGridAdapter.AppGridViewHolder> {

    @NonNull
    @Override
    public AppGridViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_app_grid, parent, false);
        return new AppGridViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AppGridViewHolder holder, int position) {
        // Set app icon, name, and button action for each grid item
        // You can modify this method to dynamically populate the data based on your requirements

        holder.bindData(App.Apps.get(position));
    }

    @Override
    public int getItemCount() {
        // Return the number of app grids you want to display
        // You can modify this method to dynamically determine the count based on your data
        return App.Apps.size();
    }

    public static class AppGridViewHolder extends RecyclerView.ViewHolder {

        ImageView appIcon;
        TextView appName;
        Button appButton;

        public AppGridViewHolder(@NonNull View itemView) {
            super(itemView);
            appIcon = itemView.findViewById(R.id.app_icon);
            appName = itemView.findViewById(R.id.app_name);
            appButton = itemView.findViewById(R.id.app_button);
        }

        public void bindData(App app) {
            // Bind app icon, name, and button action here
            // You can modify this method to bind the data for each grid item
            appName.setText(app.getName());
            appIcon.setImageResource(app.getImageResId());

            appButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.Instance.loadSingleAppActivity(app.getClassView());
                }
            });
        }
    }
}
