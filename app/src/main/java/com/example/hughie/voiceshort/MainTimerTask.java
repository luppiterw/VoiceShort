package com.example.hughie.voiceshort;

import android.os.Handler;
import android.os.Message;

import java.util.TimerTask;

/**
 * Created by hughie on 15/10/19.
 */
public class MainTimerTask extends TimerTask {

    static final int UPDATETIME = 0;
    private Handler mHandler = null;

    MainTimerTask(Handler handler)
    {
        mHandler = handler;
    }
    @Override
    public void run()
    {
        if(mHandler == null)
            return;
        Message upTime = new Message();
        upTime.what = UPDATETIME;
        mHandler.sendMessage(upTime);
    }
}
