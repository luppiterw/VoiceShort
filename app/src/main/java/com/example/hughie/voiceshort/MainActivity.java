package com.example.hughie.voiceshort;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

import java.io.Console;

public class MainActivity extends AppCompatActivity {
//public class MainActivity extends Activity {

    private DrawerLayout mDrawerLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);



        Button button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 按钮按下，将抽屉打开
//                mDrawerLayout.openDrawer(Gravity.LEFT|Gravity.TOP);
                mDrawerLayout.openDrawer(GravityCompat.START);
//                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
//                builder.setTitle ("Hello Dialog")
//                        .setMessage ("Is this material design?")
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        } ).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                }).show();


            }
        });
    }

}