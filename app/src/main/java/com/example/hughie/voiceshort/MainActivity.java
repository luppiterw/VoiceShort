package com.example.hughie.voiceshort;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
//import android.widget.Toolbar;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {
//public class MainActivity extends Activity {



    private DrawerLayout mDrawerLayout = null;
    private Toolbar mToolBar = null;
    private ActionBarDrawerToggle mActionBarDrawerToggle = null;

    private Timer mTimer = null;
    private MainTimerTask mTimerTask = null;
    private MainTimerHandler mMainTimerHandler = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Hughie","MainActivity onCreate");
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
//        mToolBar.setNavigationIcon(R.drawable.logo);
        mToolBar.setNavigationIcon(R.mipmap.navigation);

        mToolBar.setTitle("Voice-Short");

        setSupportActionBar(mToolBar);


//        final android.support.v7.app.ActionBar ab = getSupportActionBar();
//        ab.setHomeAsUpIndicator(R.drawable.logo);
//        ab.setDisplayHomeAsUpEnabled(true);
//        mToolBar.setNavigationIcon(R.drawable.logo);
//        mToolBar.setNavigationIcon(R.string.actionbar_drawertoggle_open);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        ///< 动画切换效果
        mActionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
//                mToolBar,
                R.string.actionbar_drawertoggle_open,
                R.string.actionbar_drawertoggle_close
        );
        mActionBarDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);


//        mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){
//            @Override
//            public boolean onMenuItemClick(MenuItem item){
//                switch (item.getItemId()) {
//                    case R.id.action_settings:
//                        Toast.makeText(MainActivity.this, "action_settings", Toast.LENGTH_SHORT).show();
//                        break;
//                    case R.id.action_share:
//                        Toast.makeText(MainActivity.this, "action_share", Toast.LENGTH_SHORT).show();
//                        break;
//                    default:
//                        break;
//                }
//                return true;
//            }
//        });
        //        Toolbar toolbar_main = (Toolbar)findViewById(R.id.toolbar_main);
        //        setSupportActionBar(toolbar_main);


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

    @Override
    protected void onTitleChanged(CharSequence title, int color)
    {
        ;
        super.onTitleChanged(title, color);
        if(mToolBar != null)
        {
            mToolBar.setTitle(title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        Log.d("Hughie", "onCreateOptionsMenu ");
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    private static Integer mOptionsItemSelectedCount = 0;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        Log.d("Hughie", "item.getItemId = " + item.getItemId());
//        Log.d("Hughie", "item.toString() = " + item.toString());
//        Log.d("Hughie", "android.R.id.home = " + android.R.id.home);
//        if(mToolBar != null)
//            Log.d("Hughie", "Menu=" + mToolBar.getMenu().toString());
//        Log.d("Hughie", "onOptionsItemSelected--- " + item.getMenuInfo().toString());
        Log.d("Hughie", "");

        switch (item.getItemId())
        {
            case R.id.action_settings:
                Log.d("Hughie", "onOptionsItemSelected---action_settings " + ++mOptionsItemSelectedCount);
                Toast.makeText(MainActivity.this, "action_settings", Toast.LENGTH_SHORT).show();
                return true;
            case android.R.id.home:
                if(mDrawerLayout != null)
                {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                    return true;
                }

//            case R.id.action_share:
//                Log.d("Hughie","onOptionsItemSelected---action_share");
//                Toast.makeText(MainActivity.this, "action_share", Toast.LENGTH_SHORT).show();
//                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}