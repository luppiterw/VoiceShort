package com.example.hughie.voiceshort;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
//import android.widget.Toolbar;
import android.support.v7.widget.Toolbar;
import java.io.Console;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
//public class MainActivity extends Activity {

    private DrawerLayout mDrawerLayout = null;
    private Toolbar mToolBar = null;

    private Timer mTimer = null;
    private MainTimerTask mTimerTask = null;
    private MainTimerHandler mMainTimerHandler = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.activity_main, null);

        setContentView(view);
//        setContentView(R.layout.activity_main);


        mMainTimerHandler = new MainTimerHandler(view);
        mTimerTask = new MainTimerTask(mMainTimerHandler);

        mTimer = new Timer();
        mTimer.schedule(mTimerTask, 0, 1000);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        mToolBar = (Toolbar)findViewById(R.id.toolbar);
//        mToolBar.setNavigationIcon();
        mToolBar.setTitle("Voice-Short");
        setSupportActionBar(mToolBar);
        //        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        //        setSupportActionBar(toolbar);


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

    @Override
    protected void onResume()
    {
        Log.d("MassHug", "MainActivity onResume calling.");
        if(mTimerTask == null)
        {
            mTimerTask = new MainTimerTask(mMainTimerHandler);

        }
        if(mTimer == null)
        {
            mTimer = new Timer();
            mTimer.schedule(mTimerTask, 0, 1000);

        }
//        if(mTimer != null)
//        {
//            mTimer.cancel();
//        }
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        Log.d("MassHug", "MainActivity onPause calling.");
        if(mTimer != null)
        {
            mTimer.cancel();
            mTimer = null;
        }
        if(mTimerTask != null)
        {
            mTimerTask.cancel();
            mTimerTask = null;
        }

        super.onPause();
    }

//    @Override
    protected void onTitleChange(CharSequence title, int color)
    {
        ;
        super.onTitleChanged(title,color);
        if(mToolBar != null)
        {
            mToolBar.setTitle(title);
        }
    }
}