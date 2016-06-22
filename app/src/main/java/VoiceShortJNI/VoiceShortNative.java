package VoiceShortJNI;

/**
 * Created by hughie on 15/12/22.
 */
public class VoiceShortNative
{
    static
    {
        System.loadLibrary("voiceshortjni");
    }

    public static native int voiceshort_firstfunction();

}

