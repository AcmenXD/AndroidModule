package com.acmenxd.sptool;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/12/26 17:18
 * @detail sp实体类
 */
public final class SpTool {
    /**
     * 存储类型
     */
    private final int TYPE_INT = 0x1;
    private final int TYPE_LONG = 0x2;
    private final int TYPE_FLOAT = 0x3;
    private final int TYPE_BOOLEAN = 0x4;
    private final int TYPE_STRING = 0x5;
    private final int TYPE_SETSTRING = 0x6;
    private final String INT_STR = "|#@int_int@#|";
    private final String LONG_STR = "|#@long_long@#|";
    private final String FLOAT_STR = "|#@float_float@#|";
    private final String BOOLEAN_STR = "|#@boolean_boolean@#|";
    private final String STRING_STR = "|#@string_string@#|";
    private final String SETSTRING_STR = "|#@setString_setString@#|";

    private Context mContext; //上下文对象
    private String mName; //sp名称
    private SharedPreferences mSp; //实例对象
    private List<SpChangeListener> mListeners;
    private SharedPreferences.OnSharedPreferenceChangeListener mChangeListener;

    protected SpTool(@NonNull Context pContext, @NonNull String pName) {
        mContext = pContext;
        mName = pName;
        mSp = mContext.getSharedPreferences(mName, mContext.MODE_PRIVATE);
    }

    /**
     * 注册监听 -> 允许多个监听同时存在
     */
    public SpChangeListener registerOnChangeListener(@NonNull SpChangeListener pListener) {
        if (pListener == null) {
            return null;
        }
        if (mListeners == null) {
            mListeners = new ArrayList<>();
        }
        if (mChangeListener == null) {
            mChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences pSharedPreferences, String pKey) {
                    if (mSp == pSharedPreferences && mListeners != null) {
                        String key = decode(pKey);
                        Iterator<SpChangeListener> it = mListeners.iterator();
                        while (it.hasNext()) {
                            SpChangeListener listener = it.next();
                            if (listener != null) {
                                listener.onChanged(key);
                            }
                        }
                    }
                }
            };
            registerOnSharedPreferenceChangeListener(mChangeListener);
        }
        mListeners.add(pListener);
        return pListener;
    }

    /**
     * 注销监听
     * * 注意:无用的Listener一定要及时销毁,否则可能会引发异常
     */
    public void unregisterOnChangeListener(@NonNull SpChangeListener pListener) {
        if (pListener == null) {
            return;
        }
        if (mListeners == null) {
            return;
        }
        Iterator<SpChangeListener> it = mListeners.iterator();
        while (it.hasNext()) {
            SpChangeListener listener = it.next();
            if (listener != null && listener == pListener) {
                it.remove();
                break;
            }
        }
        if (mListeners.size() <= 0) {
            mListeners = null;
            if (mChangeListener != null) {
                unregisterOnSharedPreferenceChangeListener(mChangeListener);
                mChangeListener = null;
            }
        }
    }

    /**
     * 销毁所有监听器
     */
    public void unregisterOnChangeListenerAll() {
        if (mListeners == null) {
            return;
        }
        Iterator<SpChangeListener> it = mListeners.iterator();
        while (it.hasNext()) {
            SpChangeListener listener = it.next();
            if (listener != null) {
                it.remove();
            }
        }
        mListeners.clear();
        mListeners = null;
        if (mChangeListener != null) {
            unregisterOnSharedPreferenceChangeListener(mChangeListener);
            mChangeListener = null;
        }
    }

    /**
     * 检查 key 首选项是否存在
     *
     * @param pKey
     * @return
     */
    public boolean contains(@NonNull String pKey) {
        return mSp.contains(encode(pKey));
    }

    /**
     * 清除所有数据
     *
     * @return 返回是否成功结果
     */
    public boolean clear() {
        return mSp.edit().clear().commit();
    }

    /**
     * 移除 key值的首选项
     *
     * @return 返回是否成功结果
     */
    public boolean remove(@NonNull String pKey) {
        return mSp.edit().remove(encode(pKey)).commit();
    }

    /**
     * 存入 key - value
     *
     * @return 返回是否成功结果
     */
    public boolean putInt(@NonNull String pKey, int pValue) {
        return putObject(pKey, pValue, TYPE_INT);
    }

    public boolean putLong(@NonNull String pKey, long pValue) {
        return putObject(pKey, pValue, TYPE_LONG);
    }

    public boolean putFloat(@NonNull String pKey, float pValue) {
        return putObject(pKey, pValue, TYPE_FLOAT);
    }

    public boolean putBoolean(@NonNull String pKey, boolean pValue) {
        return putObject(pKey, pValue, TYPE_BOOLEAN);
    }

    public boolean putString(@NonNull String pKey, @NonNull String pValue) {
        return putObject(pKey, pValue, TYPE_STRING);
    }

    public boolean putStringSet(@NonNull String pKey, @NonNull Set<String> pValue) {
        return putObject(pKey, pValue, TYPE_SETSTRING);
    }
    //-------------------------------------------- put end

    /**
     * 读取 key - 默认值
     *
     * @return 读取值
     */
    public int getInt(@NonNull String pKey, int pDefaultValue) {
        Object obj = getObject(pKey, TYPE_INT);
        if (obj != null) {
            return Integer.parseInt(String.valueOf(obj));
        } else {
            return pDefaultValue;
        }
    }

    public long getLong(@NonNull String pKey, long pDefaultValue) {
        Object obj = getObject(pKey, TYPE_LONG);
        if (obj != null) {
            return Long.parseLong(String.valueOf(obj));
        } else {
            return pDefaultValue;
        }
    }

    public float getFloat(@NonNull String pKey, float pDefaultValue) {
        Object obj = getObject(pKey, TYPE_FLOAT);
        if (obj != null) {
            return Float.parseFloat(String.valueOf(obj));
        } else {
            return pDefaultValue;
        }
    }

    public boolean getBoolean(@NonNull String pKey, boolean pDefaultValue) {
        Object obj = getObject(pKey, TYPE_BOOLEAN);
        if (obj != null) {
            return Boolean.parseBoolean(String.valueOf(obj));
        } else {
            return pDefaultValue;
        }
    }

    public String getString(@NonNull String pKey, @NonNull String pDefaultValue) {
        Object obj = getObject(pKey, TYPE_STRING);
        if (obj != null) {
            return String.valueOf(obj);
        } else {
            return pDefaultValue;
        }
    }

    public Set<String> getStringSet(@NonNull String pKey, @NonNull Set<String> pDefaultValue) {
        Object obj = getObject(pKey, TYPE_SETSTRING);
        if (obj != null) {
            return (Set<String>) obj;
        } else {
            return pDefaultValue;
        }
    }

    public Map<String, ?> getAll() {
        Map<String, ?> map = mSp.getAll();
        Map<String, Object> result = new HashMap<>();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            String key = decode(entry.getKey());
            if (entry.getValue() instanceof Set) {
                result.put(key, typeParseSet((Set<String>) entry.getValue()));
            } else {
                String[] obj = typeParse(entry.getValue());
                if (obj != null && obj.length >= 2 && obj[0] != null && obj[1] != null) {
                    if (obj[1] == INT_STR) {
                        result.put(key, Integer.parseInt(obj[0]));
                    } else if (obj[1] == LONG_STR) {
                        result.put(key, Long.parseLong(obj[0]));
                    } else if (obj[1] == FLOAT_STR) {
                        result.put(key, Float.parseFloat(obj[0]));
                    } else if (obj[1] == BOOLEAN_STR) {
                        result.put(key, Boolean.parseBoolean(obj[0]));
                    } else if (obj[1] == STRING_STR) {
                        result.put(key, obj[0]);
                    }
                }
            }
        }
        return result;
    }
    //-------------------------------------------- get end

    //-----------------------private 方法,无需关心------------------------------

    /**
     * 获取Editor 实例
     */
    private Editor edit() {
        return mSp.edit();
    }

    /**
     * 注册监听
     */
    private void registerOnSharedPreferenceChangeListener(@NonNull SharedPreferences.OnSharedPreferenceChangeListener pOnSharedPreferenceChangeListener) {
        mSp.registerOnSharedPreferenceChangeListener(pOnSharedPreferenceChangeListener);
    }

    /**
     * 注销监听
     */
    private void unregisterOnSharedPreferenceChangeListener(@NonNull SharedPreferences.OnSharedPreferenceChangeListener pOnSharedPreferenceChangeListener) {
        mSp.unregisterOnSharedPreferenceChangeListener(pOnSharedPreferenceChangeListener);
    }

    /**
     * 加密
     */
    private String encode(@NonNull String pStr) {
        String str = null;
        if (SpManager.sEncodeDecodeCallback != null) {
            String result = SpManager.sEncodeDecodeCallback.encode(pStr);
            if (result != null) {
                // 去掉结尾无用字符
                str = result.trim();
            }
        }
        return str;
    }

    /**
     * 解密
     */
    private String decode(@NonNull String pStr) {
        String str = null;
        if (SpManager.sEncodeDecodeCallback != null) {
            String result = SpManager.sEncodeDecodeCallback.decode(pStr);
            if (result != null) {
                // 去掉结尾无用字符
                str = result.trim();
            }
        }
        return str;
    }

    /**
     * * 加密处理
     * 存储到SharedPreferences
     */
    private boolean putObject(@NonNull String pKey, @NonNull Object pValue, int type) {
        boolean result = false;
        switch (type) {
            case TYPE_INT:
                result = edit().putString(encode(pKey), encode(INT_STR + String.valueOf(pValue))).commit();
                break;
            case TYPE_LONG:
                result = edit().putString(encode(pKey), encode(LONG_STR + String.valueOf(pValue))).commit();
                break;
            case TYPE_FLOAT:
                result = edit().putString(encode(pKey), encode(FLOAT_STR + String.valueOf(pValue))).commit();
                break;
            case TYPE_BOOLEAN:
                result = edit().putString(encode(pKey), encode(BOOLEAN_STR + String.valueOf(pValue))).commit();
                break;
            case TYPE_STRING:
                result = edit().putString(encode(pKey), encode(STRING_STR + String.valueOf(pValue))).commit();
                break;
            case TYPE_SETSTRING:
                Set<String> values = new HashSet<>();
                Iterator it = ((Set<String>) pValue).iterator();
                while (it.hasNext()) {
                    values.add(encode(SETSTRING_STR + it.next()));
                }
                result = edit().putStringSet(encode(pKey), values).commit();
                break;
        }
        return result;
    }

    /**
     * * 解密处理
     * 存储到SharedPreferences
     */
    private Object getObject(@NonNull String pKey, int type) {
        Object result = null;
        if (type != TYPE_SETSTRING) {
            String value = mSp.getString(encode(pKey), null);
            if (!TextUtils.isEmpty(value)) {
                String v = decode(value);
                switch (type) {
                    case TYPE_INT:
                        result = v.replace(INT_STR, "");
                        break;
                    case TYPE_LONG:
                        result = v.replace(LONG_STR, "");
                        break;
                    case TYPE_FLOAT:
                        result = v.replace(FLOAT_STR, "");
                        break;
                    case TYPE_BOOLEAN:
                        result = v.replace(BOOLEAN_STR, "");
                        break;
                    case TYPE_STRING:
                        result = v.replace(STRING_STR, "");
                        break;
                }
            }
        } else if (type == TYPE_SETSTRING) {
            Set<String> v = mSp.getStringSet(encode(pKey), null);
            if (v != null) {
                result = typeParseSet(v);
            }
        }
        return result;
    }

    /**
     * 解析Set<String>类型
     */
    private Set<String> typeParseSet(@NonNull Set<String> v) {
        Set<String> result = new HashSet<>();
        Iterator<String> it = v.iterator();
        while (it.hasNext()) {
            result.add(decode(it.next()).replace(SETSTRING_STR, ""));
        }
        return result;
    }

    /**
     * 解析非Set<String>类型
     */
    private String[] typeParse(@NonNull Object value) {
        String result = null; // 解析结果
        String type = null; // 解析类型
        String v = decode(String.valueOf(value));
        if (v.indexOf(INT_STR) != -1) {
            result = v.replace(INT_STR, "");
            type = INT_STR;
        } else if (v.indexOf(LONG_STR) != -1) {
            result = v.replace(LONG_STR, "");
            type = LONG_STR;
        } else if (v.indexOf(FLOAT_STR) != -1) {
            result = v.replace(FLOAT_STR, "");
            type = FLOAT_STR;
        } else if (v.indexOf(BOOLEAN_STR) != -1) {
            result = v.replace(BOOLEAN_STR, "");
            type = BOOLEAN_STR;
        } else if (v.indexOf(STRING_STR) != -1) {
            result = v.replace(STRING_STR, "");
            type = STRING_STR;
        }
        return new String[]{result, type};
    }

}
