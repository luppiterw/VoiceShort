package com.example.hughie.voiceshort;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        Button button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 按钮按下，将抽屉打开
                mDrawerLayout.openDrawer(Gravity.LEFT);

            }
        });
    }

}
