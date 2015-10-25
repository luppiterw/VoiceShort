package com.example.hughie.voiceshort;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Field;

/**
 * Created by wanghui on 2015/10/22.
 * 处理SharedPreference数据的通用类，项目中所有
 */
public class VsSharedPreference
{
    public static final int VS_BYTE = 1;
    public static final int VS_SHORT = 2;
    public static final int VS_INTEGER = 3;
    public static final int VS_LONG = 4;
    public static final int VS_STRING = 5;
    public static final int VS_BOOLEAN = 6;
    public static final int VS_FLOAT = 7;
    public static final int VS_DOUBLE = 8;
    ///< use map to store  mapping of class-type to type-define-number
    public static final Map<Class<?>,Integer> VS_TYPES;
    static
    {
        VS_TYPES = new HashMap<Class<?>,Integer>();
        VS_TYPES.put(byte.class,VS_BYTE);
        VS_TYPES.put(short.class,VS_SHORT);
        VS_TYPES.put(int.class,VS_INTEGER);
        VS_TYPES.put(long.class,VS_LONG);
        VS_TYPES.put(String.class,VS_STRING);
        VS_TYPES.put(Boolean.class,VS_BOOLEAN);
        VS_TYPES.put(float.class,VS_FLOAT);
        VS_TYPES.put(double.class,VS_DOUBLE);
    }

    /**
     * @param object Object need to be handled
     * @param context Input Context for getSharedPreferences
     * */
    public void doSave(VsShpDataBase object, Context context)
    {
        if(object == null ||
                context == null)
        {
            ///
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(object.getClass().getName(), Context.MODE_PRIVATE);
        Log.d("Hughie", "doSave xml=" + object.getClass().getName());
        SharedPreferences.Editor editor = sp.edit();
        Class<? extends VsShpDataBase> clazz = object.getClass();
        Field[] arrayField = clazz.getDeclaredFields();
        try
        {
            for(Field f : arrayField)
            {
//                Log.d("Hughie","type[" + f.getType() + "] name[" + f.getName() + "]");
                int type = VS_TYPES.get(f.getType());
//                Log.d("Hughie","type[" + f.getType() + "] name[" + f.getName() + "] 00");
                switch (type)
                {
                    case VS_BYTE:
                    case VS_SHORT:
                    case VS_INTEGER:
                        editor.putInt(f.getName(), f.getInt(object));
                        break;
                    case VS_LONG:
                        editor.putLong(f.getName(), f.getLong(object));
                        break;
                    case VS_STRING:
                        Log.d("Hughie","type[" + f.getType() + "] name[" + f.getName() + "] " +
                                " value="+f.get(object));
                        editor.putString(f.getName(), (String)f.get(object));
                        break;
                    case VS_BOOLEAN:
                        editor.putBoolean(f.getName(), f.getBoolean(object));
                        break;
                    case VS_FLOAT:
                        editor.putFloat(f.getName(), f.getFloat(object));
                        break;
                    case VS_DOUBLE:
                        editor.putFloat(f.getName(), (float)f.getFloat(object));
                        break;
                }
            }
            editor.commit();
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {

        }
    }

    /**
     * @param context Input Context for getSharedPreferences
     * */
    public void doRead(VsShpDataBase object, Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(object.getClass().getName(), Context.MODE_PRIVATE);
        Log.d("Hughie", "doRead xml=" + object.getClass().getName());
//        VsShpDataBase object = new VsShpDataBase();
        Class<? extends VsShpDataBase> clazz = object.getClass();
        Log.d("Hughie","doRead clazz.class=" + clazz.getName());
        Field[] arryFiled = clazz.getDeclaredFields();
        try
        {
            for (Field f : arryFiled)
            {
                int type = VS_TYPES.get(f.getType());
                Log.d("Hughie","doRead Field.type=" + type);
                switch (type) {
                    case VS_BYTE:
                        byte byteValue = (byte) sp.getInt(f.getName(), 0);
                        f.set(object, byteValue);
                        break;
                    case VS_SHORT:
                        short shortValue = (short) sp.getInt(f.getName(), 0);
                        f.set(object, shortValue);
                        break;
                    case VS_INTEGER:
                        int intValue = sp.getInt(f.getName(), 0);
                        Log.d("Hughie","doRead Field.int=" + intValue);
                        f.set(object, intValue);
                        break;
                    case VS_LONG:
                        long longValue = sp.getLong(f.getName(), 0L);
                        f.set(object, longValue);
                        break;
                    case VS_STRING:
                        String str = sp.getString(f.getName(), null);
                        Log.d("Hughie","doRead Field.String=" + str);
                        f.set(object, str);
                        break;
                    case VS_BOOLEAN:
                        boolean bool = sp.getBoolean(f.getName(), false);
                        f.set(object, bool);
                        break;
                    case VS_FLOAT:
                        float floatValue = sp.getFloat(f.getName(), 0.0f);
                        f.set(object, floatValue);
                        break;
                    case VS_DOUBLE:
                        double doubleValue = sp.getFloat(f.getName(), 0.0f);
                        f.set(object, doubleValue);
                        break;
                }
            }
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
//        return object;
    }

}
