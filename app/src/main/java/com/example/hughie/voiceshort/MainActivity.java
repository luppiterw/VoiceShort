package com.example.hughie.voiceshort;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
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
import android.view.ViewTreeObserver;
import android.widget.Button;
//import android.widget.Toolbar;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Timer;

import utils.richeditor.RichEditor;

public class MainActivity extends AppCompatActivity {
//public class MainActivity extends Activity {



    private DrawerLayout mDrawerLayout = null;
    private Toolbar mToolBar = null;
    private ActionBarDrawerToggle mActionBarDrawerToggle = null;

    private Timer mTimer = null;
    private MainTimerTask mTimerTask = null;
    private MainTimerHandler mMainTimerHandler = null;


    private RichEditor mEditor = null;

    private MediaPlayer mMediaplayer = null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        Log.d("Hughie", "MainActivity onCreate");
        super.onCreate(savedInstanceState);

//        VsTestData data1 = new VsTestData("data1", 1);
//        VsSharedPreference vsp = new VsSharedPreference();
//        vsp.doSave(data1, this);
////        Log.d("Hughie", "MainActivity onCreate doSave done!.");
//        vsp.doRead(data1, this);
////        Log.d("Hughie", "MainActivity onCreate doRead done!.");
//        data1.printData();
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Log.d("Hughie", "MainActivity onCreate before LayoutInflater.from");
        LayoutInflater inflater = LayoutInflater.from(this);
        Log.d("Hughie", "MainActivity onCreate after LayoutInflater.from");
        View view = inflater.inflate(R.layout.activity_main, null);

        Log.d("Hughie", "MainActivity onCreate before setContentView.");
        setContentView(view);
        Log.d("Hughie", "MainActivity onCreate after setContentView.");
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
//                mDrawerLayout.openDrawer(GravityCompat.START);
                if (mEditor != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mEditor.testJSInterface();
//                        mEditor.findAllAsync("a");
//                        if(mEditor.pageDown(true) == false)
//                        {
//                            Toast.makeText(v.getContext(),"PPaaa",Toast.LENGTH_SHORT).show();
//                        }
                    }
                }
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
//        Log.d("Hughie", "CUUUUUUUU=" + view.getWidth());
        Log.d("Hughie", "mEditor 0");
        mEditor = (RichEditor) findViewById(R.id.editor);
        Log.d("Hughie", "mEditor 1");
//        mEditor.setEditorHeight(200);
        mEditor.setBackgroundColor(Color.GREEN);
        mEditor.setEditorFontSize(22);
        mEditor.setEditorFontColor(Color.RED);
        //mEditor.setEditorBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundColor(Color.BLUE);
        //mEditor.setBackgroundResource(R.drawable.bg);
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setHorizontalScrollBarEnabled(true);
//        mEditor.setBackground("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg");
        mEditor.setPlaceholder("Insert text here...");

        final ViewTreeObserver editorObserver = mEditor.getViewTreeObserver();
        editorObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mEditor.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                mEditor.getHeight();
                mEditor.getWidth();
                Log.d("Hughie", "----mEditor.getHeight()=" + mEditor.getHeight());
                mEditor.setEditorHeight(mEditor.getHeight());
            }
        });

        mMediaplayer = new MediaPlayer();

//        mEditor.setBackgroundColor(Color.TRANSPARENT);
//        mEditor.set
//        mEditor.getBackground().setAlpha(0);
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
            {
                Log.d("Hughie", "onOptionsItemSelected---action_settings " + ++mOptionsItemSelectedCount);
                Toast.makeText(MainActivity.this, "action_settings", Toast.LENGTH_SHORT).show();

                readFile("P5");
//                if(mEditor != null)
//                {
//                    mEditor.setHtml("12345</br>" +
//                            "678910\r" +
//                            "ceshi" +
//                            "nihao" +
//                            "wanghui\n");
//                }
                return true;
            }
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


    /**读取文件内容*/
    public boolean readFile(final String fileName)
    {
        Log.d("Hughie","readFile["+fileName+"]");
        try
        {
            String sss = Environment.getExternalStorageDirectory().getPath() +
                    "/Giant/program/" +  fileName;
            Log.d("Hughie","sssssss="+sss);
            File file = new File(sss);
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader streamReader = new InputStreamReader(fis);
            BufferedReader bufReader = new BufferedReader(streamReader);
            int row = 1;
            String line;
            String content = "";
            while( (line = bufReader.readLine()) != null)
            {
//                content +=  String.format(" %03d  %s\n",row++,line);
//                content +=  String.format("    %s\n", line);
//                content += line + "</br>";
                content += line + "\n";
                Log.d("Hughie", row++ + " Line = [" + line + "]");
            }

            fis.close();
//            Log.d("Hughie", "Read profrag.txt=[" + content + "]");

            if(mEditor!=null)
            {
                mEditor.setText(content);
//                mEditor.setHtml(content);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return true;
    }
}