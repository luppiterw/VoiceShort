package utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import utils.VsException;


/**
 * Created by hughie on 15/10/23.
 */
public class VsSharedPreferenceOP
{
    private static final String UI_FILENAME = "ui_store";

    /**
     * Read key value from UI_FILENAME by object( Type range: String,Integer,Boolean,Float,Long)
     * Throw GException for some unmatched type error.
     * TODO Better type check method...
     * */
    public static Object getPreference(Context context, String key, Object defaultValue) throws VsException
    {
        if(context == null ||
                defaultValue == null)
            return null;
//            throw new GException("[GSharedPreferenceUtility] getPreference: Input context or defaultValue is null...");

        SharedPreferences sp = context.getSharedPreferences(UI_FILENAME, Context.MODE_PRIVATE);

        String valueType = defaultValue.getClass().getSimpleName();
        Log.d("Hughie", "valueType=" + valueType);
        Object ret = null;
        try
        {
            ///< input defaultValue maybe have a wrong type with saved value in xml
            if(valueType.equals("String"))
                ret = sp.getString(key, (String) defaultValue);
            else if(valueType.equals("Integer"))
                ret = sp.getInt(key, (Integer) defaultValue);
            else if(valueType.equals("Boolean"))
                ret = sp.getBoolean(key, (Boolean) defaultValue);
            else if(valueType.equals("Float"))
                ret = sp.getFloat(key, (Float) defaultValue);
            else if(valueType.equals("Long"))
                ret = sp.getLong(key, (Long) defaultValue);
//            else
//            {
//                throw new GException("[GSharedPreferenceUtility] getPreference: No matching input type...");
//            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new VsException("[GSharedPreferenceUtility] getPreference: Input type match GException...");
        }

        return ret;
    }

    /**
     * Write key value to UI_FILENAME by object( Type range: String,Integer,Boolean,Float,Long)
     * TODO Better type check method...
     * */
    public static void setPreference(Context context, String key, Object newValue) throws VsException
    {
        if(context == null ||
                newValue == null)
            return;

        SharedPreferences sp = context.getSharedPreferences(UI_FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Log.d("Hughie", "newValue=" + newValue);
        String valueType = newValue.getClass().getSimpleName();
        try
        {
            ///< it'll (may) change saved key type (...)
            if(valueType.equals("String"))
                editor.putString(key, (String)newValue);
            else if(valueType.equals("Integer"))
                editor.putInt(key, (Integer)newValue);
            else if(valueType.equals("Boolean"))
                editor.putBoolean(key, (Boolean)newValue);
            else if(valueType.equals("Float"))
                editor.putFloat(key, (Float)newValue);
            else if(valueType.equals("Long"))
                editor.putLong(key, (Long)newValue);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new VsException("[GSharedPreferenceUtility] setPreference: Input type match GException...");
        }
        editor.commit();
    }
}
