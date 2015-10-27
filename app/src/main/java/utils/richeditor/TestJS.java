package utils.richeditor;

import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * Created by hughie on 15/10/27.
 */
public class TestJS {
    TestJS()
    {

    }
    @JavascriptInterface
    public void printTest(String result)
    {
        Log.d("Hughie", "[TestJS] getTest result=" + result);
    }
}
