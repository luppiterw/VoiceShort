package com.example.hughie.voiceshort;

import android.os.Message;

//import java.util.logging.Handler;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hughie on 15/10/19.
 * Self-define MainActivity Timer Handler
 */
public class MainTimerHandler extends Handler
{
    View mView = null;

    MainTimerHandler(View view)
    {
        mView = view;
    }
    @Override
    public void handleMessage(Message msg)
    {

        switch (msg.what)
        {
            case MainTimerTask.UPDATETIME:
            {
//                Log.d("MassHug","MainTimerHandler-handleMessage-UPDATETIME...");
//                System.out.println("MainTimerHandler-handleMessage-UPDATETIME...");
                if (mView != null)
                {


                    TextView tv = (TextView)mView.findViewById(R.id.textView);
                    Long nTime = System.currentTimeMillis();
                    String sTime;
                    Date date = new Date(nTime);
//                    date.setTime(nTime);

//                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); ///< 12小时制
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); ///< 24小时制
                    sTime = df.format(date);
                    tv.setText(sTime);
//                    tv.setText(date.toString());

//                    tv.setText(nTime.toString());
//                    tv.setText(String.valueOf(System.currentTimeMillis()));
                }
            }
        }
        super.handleMessage(msg);
    }
}
