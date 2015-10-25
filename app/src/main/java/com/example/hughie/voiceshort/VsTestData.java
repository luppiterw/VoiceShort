package com.example.hughie.voiceshort;

import android.util.Log;

/**
 * Created by hughie on 15/10/23.
 */
public class VsTestData extends VsShpInterface
{
    public String name;
    public int num;
    VsTestData(String name, int num)
    {
        this.name = name;
        this.num = num;
    }

    public  void printData()
    {
        Log.d("Hughie",String.format("[VsTestData] name=%s num=%d", name, num));
    }
}
