package com.example.camerawithtutk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    Client client  =new Client(this);
    String UID ="GV4GRAS1S2XJY3F1111A";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client.init();
        client.connet(UID);
        client.cloes();
    }
}
