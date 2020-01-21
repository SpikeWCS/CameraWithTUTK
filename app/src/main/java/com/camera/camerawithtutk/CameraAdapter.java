package com.camera.camerawithtutk;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.camera.api.AVAPIsClient;
import com.camera.model.User;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class CameraAdapter extends RecyclerView.Adapter<CameraAdapter.ViewHolder> {
    private Context context;
    private List<User> itemList;
    ClearData clearData;

    public CameraAdapter(Context context, List<User> itemList, ClearData clearData) {
        this.context = context;
        this.itemList = itemList;
        this.clearData = clearData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_camera, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, CameraActivity.class);
            context.startActivity(intent);
        });
        holder.name.setText(itemList.get(position).getUsername());
        holder.moreInfo.setOnClickListener(view -> bottomShow());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView islive;
        private ImageView moreInfo;
        private ImageView video;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            islive = itemView.findViewById(R.id.item_isAlive);
            moreInfo = itemView.findViewById(R.id.item_set);
            video = itemView.findViewById(R.id.item_video);
        }
    }

    private void bottomShow() {
        Dialog dialog = new Dialog(context, R.style.MyDialog);
        // 加载dialog布局view
        View purchase = LayoutInflater.from(context).inflate(R.layout.item_bottom, null);
        ImageView close = purchase.findViewById(R.id.bottom_info_next);
        ImageView reconnect = purchase.findViewById(R.id.bottom_re_next);
        ImageView delete = purchase.findViewById(R.id.bottom_del_next);
        TextView cancel = purchase.findViewById(R.id.bottom_cancel);
        // 设置外部点击 取消dialog
        dialog.setCancelable(true);
        // 获得窗体对象
        Window window = dialog.getWindow();
        // 设置窗体的对齐方式
        window.setGravity(Gravity.BOTTOM);
        // 设置窗体动画
        window.setWindowAnimations(R.style.AnimBottom);
        // 设置窗体的padding
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.setContentView(purchase);
        dialog.show();
        close.setOnClickListener(view -> {
            AVAPIsClient.controlVideoThread();
            AVAPIsClient.close();
            Toasty.success(context, "连接已断开", Toast.LENGTH_SHORT, true).show();
            dialog.dismiss();
        });
        reconnect.setOnClickListener(view -> {
            Toasty.normal(context, "正在尝试重新连接", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(context, MainActivity.class);
            context.startActivity(intent2);
        });
        delete.setOnClickListener(view -> {
            itemList.clear();
            clearData.clear();
            Toasty.success(context, "删除成功", Toast.LENGTH_SHORT, true).show();
            dialog.dismiss();
        });
        cancel.setOnClickListener(view -> dialog.dismiss());
    }
}
