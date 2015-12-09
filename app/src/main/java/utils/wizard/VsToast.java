package utils.wizard;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by hughie on 15/12/8.
 *
 */
public class VsToast
{
    private static Toast mToast = null;
    private static VsToast mGToast = null;
    private static Context mContext = null;

    public static void show(final Context context,CharSequence message)
    {
        if(mGToast == null)
            mGToast = new VsToast(context);

        showText(context,message);
    }
    public void hide()
    {
        if(mToast != null)
            mToast.cancel();
    }

    private VsToast(final Context context)
    {
        mContext = context;
        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    private static void showText(final Context context,CharSequence message)
    {
        if(mToast != null)
        {
            if(context != mContext)
            {
                mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
                mContext = context;
            }
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.setText(message);
            mToast.show();
        }
    }

}
