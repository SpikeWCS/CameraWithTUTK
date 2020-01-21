package com.camera.camerawithtutk;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.camera.model.VideoInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class SaveAdapter extends RecyclerView.Adapter<SaveAdapter.ViewHolder>  {

    private ArrayList<VideoInfo> videoInfoArrayList;

    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView videoPath;
        private View videoView;

        public ViewHolder(View itemView) {
            super(itemView);
            videoView = itemView;
            videoPath = itemView.findViewById(R.id.item_save_text);
        }
    }

    public SaveAdapter(Context context, ArrayList<VideoInfo> infos) {
        this.context = context;
        this.videoInfoArrayList = infos;
//        for (int i = 0; i < videoInfoArrayList.size();i++){
//            System.out.println(videoInfoArrayList.get(i).getVideoName());
//            System.out.println(videoInfoArrayList.get(i).getVideoPath());
//        }

    }

    @Override
    public SaveAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_save, null, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        VideoInfo videoInfo = videoInfoArrayList.get(position);
        holder.videoPath.setText(videoInfo.getVideoName());
        holder.videoView.setOnClickListener(view -> {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            File data = videoInfoArrayList.get(position).getVideoPath();
            Uri uri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //data是file类型,忘了复制过来
                uri = FileProvider.getUriForFile(context, "com.camera.camerawithtutk.fileprovider", data);
            } else {
                uri=Uri.fromFile(data);
            }
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, "video/*");
            try {
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoInfoArrayList.size();
    }

}
