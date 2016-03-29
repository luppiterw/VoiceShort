/**
 * Created by hughie on 15/12/22.
 */
public class TestNative
{
    static
    {
        System.loadLibrary("testfunc");
    }

    public static native int firstfunction();

}

