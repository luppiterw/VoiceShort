package com.example.hughie.voiceshort;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Collection;
import java.util.IllegalFormatException;
import java.util.Map;
import java.util.Set;
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
        VS_TYPES = new Map<Class<?>, Integer>() {
            @Override
            public void clear() {

            }

            @Override
            public boolean containsKey(Object key) {
                return false;
            }

            @Override
            public boolean containsValue(Object value) {
                return false;
            }

            @NonNull
            @Override
            public Set<Entry<Class<?>, Integer>> entrySet() {
                return null;
            }

            @Override
            public Integer get(Object key) {
                return null;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @NonNull
            @Override
            public Set<Class<?>> keySet() {
                return null;
            }

            @Override
            public Integer put(Class<?> key, Integer value) {
                return null;
            }

            @Override
            public void putAll(Map<? extends Class<?>, ? extends Integer> map) {

            }

            @Override
            public Integer remove(Object key) {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }

            @NonNull
            @Override
            public Collection<Integer> values() {
                return null;
            }
        };

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
    public void doSave(VsShpInterface object, Context context)
    {
        if(object == null ||
                context == null)
        {
            ///
            return;
        }
        SharedPreferences sp = context.getSharedPreferences(object.getClass().getName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        Class<? extends VsShpInterface> clazz = object.getClass();
        Field[] arrayField = clazz.getDeclaredFields();
        try
        {
            for(Field f : arrayField)
            {
                Log.d("Hughie","type[" + f.getType() + "] name[" + f.getName() + "]");
                int type = VS_TYPES.get(f.getType());
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
    public VsShpInterface doRead(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(VsSharedPreference.class.getName(), Context.MODE_PRIVATE);
        VsShpInterface object = new VsShpInterface() {
        };
        Class<? extends  VsShpInterface> clazz = object.getClass();

        Field[] arryFiled = clazz.getDeclaredFields();
        try
        {
            for (Field f : arryFiled)
            {
                int type = VS_TYPES.get(f.getType());
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
                        f.set(object, intValue);
                        break;
                    case VS_LONG:
                        long longValue = sp.getLong(f.getName(), 0L);
                        f.set(object, longValue);
                        break;
                    case VS_STRING:
                        String str = sp.getString(f.getName(), null);
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
        return object;
    }

}
