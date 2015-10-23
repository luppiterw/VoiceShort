package utils;

import android.util.Log;

/**
 * Created by hughie on 15/10/23.
 * Easy VoiceShort UI exception class.
 */
public class VsException extends Exception
{
    public VsException()
    {
        super();
    }

    public VsException(String msg)
    {
        super(msg);
        Log.d("Hughie", "GException msg= [" + msg + "]");

    }
}
