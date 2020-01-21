package com.camera.camerawithtutk;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.camera.api.AVAPIsClient;
import com.camera.model.User;
import com.decode.tools.BufferInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;


public class MainActivity extends AppCompatActivity implements ClearData{

    private String UID = "GV4GRAS1S2XJY3F1111A";
    public static BlockingDeque<BufferInfo> bq;
    List<User> itemList = new ArrayList<>();
    String key = "";
    String name = "";
    String uid = "";
    User user;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(MainActivity.this);
        RecyclerView rc = findViewById(R.id.page_rec);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rc.setLayoutManager(linearLayoutManager);
        ImageView add = findViewById(R.id.page_add);
        add.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivity(intent);
        });
        getUserInfo();
        if(key==null){
            Intent intent = getIntent();
            key = intent.getStringExtra("key");
            name = intent.getStringExtra("name");
            uid = intent.getStringExtra("uid");
        }
        bq = new LinkedBlockingDeque<>();// videobuffer信息存储到这里 解码器从此阻塞队列poll video的信息
        if (key != null && name != null && uid != null) {
            saveUserInfo();
            user = new User(name, key, uid);
            itemList.add(user);
        }
        CameraAdapter adapter = new CameraAdapter(this, itemList,this);
        rc.setAdapter(adapter);
        ImageView refresh =findViewById(R.id.page_refresh);
        refresh.setOnClickListener(view -> {
            adapter.notifyDataSetChanged();
        });
        TextView textView = findViewById(R.id.page_tips);
        if (itemList.size() > 0) {
            textView.setVisibility(View.INVISIBLE);
            (new Thread() {
                public void run() {
//                User user = User.getInstance("admin", "123456");
                    AVAPIsClient.start(user, bq);
                    System.out.println("连接线程中断++++++");
                }
            }).start();
        }else {
            textView.setVisibility(View.VISIBLE);
        }
    }

    private void saveUserInfo() {
        SharedPreferences userInfo = getSharedPreferences(UID, MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();//获取Editor
        //得到Editor后，写入需要保存的数据
        editor.putString("name", name);
        editor.putString("key", key);
        editor.putString("uid", uid);
        editor.apply();//提交修改
    }

    private void getUserInfo() {
        SharedPreferences userInfo = getSharedPreferences(UID, MODE_PRIVATE);
        name = userInfo.getString("name", null);//读取username
        key = userInfo.getString("key", null);//读取username
        uid = userInfo.getString("uid", null);//读取username
    }
    @Override
    public void clear() {
        SharedPreferences userInfo = getSharedPreferences(UID, MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();//获取Editor
        editor.clear();
        editor.apply();
    }

    public void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
