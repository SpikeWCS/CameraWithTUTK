package com.camera.camerawithtutk;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import es.dmoral.toasty.Toasty;

public class AddActivity extends AppCompatActivity {
    private EditText uidEdit;
    private static final int REQUEST_CODE_SCAN =1;
    private String key;
    private String uid;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Button scan =findViewById(R.id.add_scan);
        uidEdit=findViewById(R.id.add_edit);
        EditText keyEdit =findViewById(R.id.add_editkey);
        EditText nameEdit =findViewById(R.id.add_editname);
        ImageView back =findViewById(R.id.add_back);
        TextView save = findViewById(R.id.add_save);
        scan.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT>22) {
                if (ContextCompat.checkSelfPermission(AddActivity.this,
                        android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //先判断有没有权限 ，没有就在这里进行权限的申请
                    ActivityCompat.requestPermissions(AddActivity.this,
                            new String[]{android.Manifest.permission.CAMERA}, 1);

                } else {
                    Intent intent = new Intent(AddActivity.this, CaptureActivity.class);
                    ZxingConfig config = new ZxingConfig();
                    config.setShowbottomLayout(true);//底部布局（包括闪光灯和相册）
                    config.setShowAlbum(true);//是否显示相册
                    config.setShowFlashLight(true);//是否显示闪光灯
                    config.setShake(true);//是否震动
                    config.setPlayBeep(true);//是否播放提示音
                    intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                    startActivityForResult(intent, REQUEST_CODE_SCAN);
                    Toast.makeText(getApplicationContext(), "扫一扫", Toast.LENGTH_SHORT).show();
                }
            }
            save.setOnClickListener( view1-> {
                key=keyEdit.getText().toString();
                name =nameEdit.getText().toString();
                uid = uidEdit.getText().toString();
                Intent intent =new Intent(AddActivity.this,MainActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("key",key);
                intent.putExtra("uid",uid);
                Toasty.success(this,"添加成功",Toast.LENGTH_SHORT,true).show();
                startActivity(intent);
            });

//                Intent intent = new Intent(AddActivity.this, CaptureActivity.class);
            /*ZxingConfig是配置类  可以设置是否显示底部布局，闪光灯，相册，是否播放提示音  震动等动能
             * 也可以不传这个参数
             * 不传的话  默认都为默认不震动  其他都为true
             * */

            //ZxingConfig config = new ZxingConfig();
            //config.setShowbottomLayout(true);//底部布局（包括闪光灯和相册）
            //config.setPlayBeep(true);//是否播放提示音
            //config.setShake(true);//是否震动
            //config.setShowAlbum(true);//是否显示相册
            //config.setShowFlashLight(true);//是否显示闪光灯
            //intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
//                startActivityForResult(intent,REQUEST_CODE_SCAN);
        });
        back.setOnClickListener(view -> onBackPressed());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String content = data.getStringExtra(Constant.CODED_CONTENT);
                uidEdit.setText(content);
            }
        }
    }
}
