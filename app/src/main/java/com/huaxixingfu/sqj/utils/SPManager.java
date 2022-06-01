package com.huaxixingfu.sqj.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.huaxixingfu.sqj.commom.Constants;
import com.tencent.mmkv.MMKV;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;


public class SPManager {

    private static SPManager sPManager;
    private MMKV sp;


    /**
     * 针对复杂类型存储<对象>
     *
     * @param key
     * @param /val
     */
    public void setObject(String key, Object object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {

            out = new ObjectOutputStream(baos);
            out.writeObject(object);
            String objectVal = new String(Base64.encode(baos.toByteArray(),
                    Base64.DEFAULT));
            Editor editor = sp.edit();
            editor.putString(key, objectVal);
//            editor.commit();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getObject(String key, Class<T> clazz) {
        if (sp.contains(key)) {
            String objectVal = sp.getString(key, null);
            byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(bais);
                T t = (T) ois.readObject();
                return t;
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bais != null) {
                        bais.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public void deleteKey(String key) {
        Editor et = sp.edit();
        et.remove(key);
    }

    public <T> void putModel(String key, T t) {
        put(key, GsonUtil.gsonString(t));
    }

    public <T> T getModel(String key, Class<T> clazz) {
        final String string = getString(key, "");
        if (!TextUtils.isEmpty(string)) {
            return (T) GsonUtil.GsonToBean(string, clazz);
        } else {
            return null;
        }
    }





    @SuppressWarnings("static-access")
    private SPManager(Context mContext) {
        sp = MMKV.mmkvWithID(Constants.sp_name, MMKV.MULTI_PROCESS_MODE);
        SharedPreferences old_man;
        // 迁移旧数据
        old_man = mContext.getSharedPreferences(Constants.sp_name, mContext.MODE_PRIVATE);
        sp.importFromSharedPreferences(old_man);
        old_man.edit().clear().commit();
    }

    public static synchronized SPManager instance(Context context) {
        if (sPManager == null) {
            MMKV.initialize(context);
            sPManager = new SPManager(context);
        }
        return sPManager;
    }

    public void saveUserPhone(String phone) {
        put(Constants.USERINFO_PHONE, phone);
    }
    public String getUserPhone() {
        return getString(Constants.USERINFO_PHONE);
    }

    public void saveUserId(long userid) {
        put(Constants.USER_ID, userid);
    }
    public long getUserId() {
        return getLong(Constants.USER_ID);
    }

    public void savePrivate(String url) {
        put(Constants.PRIVATE_KEY, url);
    }
    public String getPrivate() {
        return getString(Constants.PRIVATE_KEY,Constants.PRIVACYPOLICY);
    }

    public void saveAgreement(String url) {
        put(Constants.AGREEMENT_KEY, url);
    }
    public String getAgreement() {
        return getString(Constants.AGREEMENT_KEY,Constants.AGREEMENT);
    }

    public void saveAboutUs(String url) {
        put(Constants.ABOUT_KEY, url);
    }
    public String getAboutUs() {
        return getString(Constants.ABOUT_KEY,Constants.ABOUTUS);
    }


    public void setToken(String token) {
        put(Constants.KEY_PREF_TOKEN, token);
    }

    public String getToken() {
        return getString(Constants.KEY_PREF_TOKEN);
    }

    public void setLogin(boolean b) {
        put(Constants.USERINFO_LOGIN, b);
    }

    public boolean isLogin() {
        return getBoolean(Constants.USERINFO_LOGIN);
    }

    public void setGuideShow(boolean b) {
        put(Constants.IS_GIUDE_SHOW, b);
    }

    public boolean isGuideShow() {
        return getBoolean(Constants.IS_GIUDE_SHOW);
    }


    public void clearUserInfo() {
        remove(Constants.USERINFO);
        remove(Constants.KEY_PREF_TOKEN);
        remove(Constants.USERINFO_PHONE);
        setLogin(false);
    }

    /**
     * @param key
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getValue(String key, Class<T> clazz) {
        T t;
        try {

//            clazz.getDeclaredConstructor().newInstance()
            t = clazz.newInstance();

            if (t instanceof Integer) {
                return (T) Integer.valueOf(sp.getInt(key, 0));
            } else if (t instanceof String) {
                return (T) sp.getString(key, "");
            } else if (t instanceof Boolean) {
                return (T) Boolean.valueOf(sp.getBoolean(key, false));
            } else if (t instanceof Long) {
                return (T) Long.valueOf(sp.getLong(key, 0L));
            } else if (t instanceof Float) {
                return (T) Float.valueOf(sp.getFloat(key, 0L));
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
            Log.e("system", "类型输入错误或者复杂类型无法解析[" + e.getMessage() + "]");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.e("system", "类型输入错误或者复杂类型无法解析[" + e.getMessage() + "]");
        }
        Log.e("system", "无法找到" + key + "对应的值");
        return null;
    }

    public <T> void setValue(String key, Object object, Class<T> clazz) {
        T t;
        try {
            t = clazz.newInstance();

            if (t instanceof Integer) {
                int value = (Integer) object;
                Editor et = sp.edit();
                et.putInt(key, value);
//                et.commit();
            } else if (t instanceof Boolean) {
                boolean value = (Boolean) object;
                Editor et = sp.edit();
                et.putBoolean(key, value);
//                et.commit();

            } else if (t instanceof String) {
                String value = (String) object;
                Editor et = sp.edit();
                et.putString(key, value);
                et.commit();
            } else if (t instanceof Float) {
                Float value = (Float) object;
                Editor et = sp.edit();
                et.putFloat(key, value);
//                et.commit();
            } else if (t instanceof Long) {
                Long value = (Long) object;
                Editor et = sp.edit();
                et.putLong(key, value);
//                et.commit();
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
            Log.e("system", "类型输入错误或者复杂类型无法解析[" + e.getMessage() + "]");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.e("system", "类型输入错误或者复杂类型无法解析[" + e.getMessage() + "]");
        }
    }

    /**
     * Put the string value in sp.
     *
     * @param key   The key of sp.
     * @param value The value of sp.
     */
    public void put(@NonNull final String key, final String value) {
        put(key, value, false);
    }

    /**
     * Put the string value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use {@link Editor#commit()},
     *                 false to use {@link Editor#apply()}
     */
    public void put(@NonNull final String key, final String value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putString(key, value);
        } else {
            sp.edit().putString(key, value);
        }
    }


    /**
     * Return the string value in sp.
     *
     * @param key The key of sp.
     * @return the string value if sp exists or {@code ""} otherwise
     */
    public String getString(@NonNull final String key) {
        return getString(key, "");
    }

    /**
     * Return the string value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @return the string value if sp exists or {@code defaultValue} otherwise
     */
    public String getString(@NonNull final String key, final String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    /**
     * Put the int value in sp.
     *
     * @param key   The key of sp.
     * @param value The value of sp.
     */
    public void put(@NonNull final String key, final int value) {
        put(key, value, false);
    }

    /**
     * Put the int value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use {@link Editor#commit()},
     *                 false to use {@link Editor#apply()}
     */
    public void put(@NonNull final String key, final int value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putInt(key, value);
        } else {
            sp.edit().putInt(key, value);
        }
    }

    /**
     * Return the int value in sp.
     *
     * @param key The key of sp.
     * @return the int value if sp exists or {@code -1} otherwise
     */
    public int getInt(@NonNull final String key) {
        return getInt(key, -1);
    }

    /**
     * Return the int value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @return the int value if sp exists or {@code defaultValue} otherwise
     */
    public int getInt(@NonNull final String key, final int defaultValue) {
        return sp.getInt(key, defaultValue);
    }

    /**
     * Put the long value in sp.
     *
     * @param key   The key of sp.
     * @param value The value of sp.
     */
    public void put(@NonNull final String key, final long value) {
        put(key, value, false);
    }

    /**
     * Put the long value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use {@link Editor#commit()},
     *                 false to use {@link Editor#apply()}
     */
    public void put(@NonNull final String key, final long value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putLong(key, value);
        } else {
            sp.edit().putLong(key, value);
        }
    }

    /**
     * Return the long value in sp.
     *
     * @param key The key of sp.
     * @return the long value if sp exists or {@code -1} otherwise
     */
    public long getLong(@NonNull final String key) {
        return getLong(key, -1L);
    }

    /**
     * Return the long value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @return the long value if sp exists or {@code defaultValue} otherwise
     */
    public long getLong(@NonNull final String key, final long defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    /**
     * Put the float value in sp.
     *
     * @param key   The key of sp.
     * @param value The value of sp.
     */
    public void put(@NonNull final String key, final float value) {
        put(key, value, false);
    }

    /**
     * Put the float value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use {@link Editor#commit()},
     *                 false to use {@link Editor#apply()}
     */
    public void put(@NonNull final String key, final float value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putFloat(key, value);
        } else {
            sp.edit().putFloat(key, value);
        }
    }

    /**
     * Return the float value in sp.
     *
     * @param key The key of sp.
     * @return the float value if sp exists or {@code -1f} otherwise
     */
    public float getFloat(@NonNull final String key) {
        return getFloat(key, -1f);
    }

    /**
     * Return the float value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @return the float value if sp exists or {@code defaultValue} otherwise
     */
    public float getFloat(@NonNull final String key, final float defaultValue) {
        return sp.getFloat(key, defaultValue);
    }

    /**
     * Put the boolean value in sp.
     *
     * @param key   The key of sp.
     * @param value The value of sp.
     */
    public void put(@NonNull final String key, final boolean value) {
        put(key, value, false);
    }

    /**
     * Put the boolean value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use {@link Editor#commit()},
     *                 false to use {@link Editor#apply()}
     */
    public void put(@NonNull final String key, final boolean value, final boolean isCommit) {
        if (isCommit) {
            sp.edit().putBoolean(key, value);
        } else {
            sp.edit().putBoolean(key, value);
        }
    }

    /**
     * Return the boolean value in sp.
     *
     * @param key The key of sp.
     * @return the boolean value if sp exists or {@code false} otherwise
     */
    public boolean getBoolean(@NonNull final String key) {
        return getBoolean(key, false);
    }

    /**
     * Return the boolean value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @return the boolean value if sp exists or {@code defaultValue} otherwise
     */
    public boolean getBoolean(@NonNull final String key, final boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    /**
     * Put the set of string value in sp.
     *
     * @param key   The key of sp.
     * @param value The value of sp.
     */
    public void put(@NonNull final String key, final Set<String> value) {
        put(key, value, false);
    }

    /**
     * Put the set of string value in sp.
     *
     * @param key      The key of sp.
     * @param value    The value of sp.
     * @param isCommit True to use {@link Editor#commit()},
     *                 false to use {@link Editor#apply()}
     */
    public void put(@NonNull final String key,
                    final Set<String> value,
                    final boolean isCommit) {
        if (isCommit) {
            sp.edit().putStringSet(key, value);
        } else {
            sp.edit().putStringSet(key, value);
        }
    }

    /**
     * Return the set of string value in sp.
     *
     * @param key The key of sp.
     * @return the set of string value if sp exists
     * or {@code Collections.<String>emptySet()} otherwise
     */
    public Set<String> getStringSet(@NonNull final String key) {
        return getStringSet(key, Collections.<String>emptySet());
    }

    /**
     * Return the set of string value in sp.
     *
     * @param key          The key of sp.
     * @param defaultValue The default value if the sp doesn't exist.
     * @return the set of string value if sp exists or {@code defaultValue} otherwise
     */
    public Set<String> getStringSet(@NonNull final String key,
                                    final Set<String> defaultValue) {
        return sp.getStringSet(key, defaultValue);
    }

    /**
     * Return all values in sp.
     *
     * @return all values in sp
     */
    public Map<String, ?> getAll() {
        return sp.getAll();
    }

    /**
     * Return whether the sp contains the preference.
     *
     * @param key The key of sp.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public boolean contains(@NonNull final String key) {
        return sp.contains(key);
    }

    /**
     * Remove the preference in sp.
     *
     * @param key The key of sp.
     */
    public void remove(@NonNull final String key) {
        remove(key, false);
    }

    /**
     * Remove the preference in sp.
     *
     * @param key      The key of sp.
     * @param isCommit True to use {@link Editor#commit()},
     *                 false to use {@link Editor#apply()}
     */
    public void remove(@NonNull final String key, final boolean isCommit) {
        if (isCommit) {
            sp.edit().remove(key);
        } else {
            sp.edit().remove(key);
        }
    }

    /**
     * Remove all preferences in sp.
     */
    public void clear() {
        clear(false);
    }

    /**
     * Remove all preferences in sp.
     *
     * @param isCommit True to use {@link Editor#commit()},
     *                 false to use {@link Editor#apply()}
     */
    public void clear(final boolean isCommit) {
        if (isCommit) {
            sp.edit().clear();
        } else {
            sp.edit().clear();
        }
    }

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }


}
