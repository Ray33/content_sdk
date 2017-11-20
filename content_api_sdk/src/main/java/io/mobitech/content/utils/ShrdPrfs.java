/*
 * Copyright (c) 2016. http://mobitech.io. All rights reserved.
 * Code is not permitted for commercial use w/o permission of Mobitech.io - support@mobitech.io .
 * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package io.mobitech.content.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.bluelinelabs.logansquare.LoganSquare;

import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShrdPrfs {

    private static final String TAG = ShrdPrfs.class.getPackage() + "." + ShrdPrfs.class.getSimpleName();

    private static final String DEFAULT = "DEFAULT";
    static Map<String, SharedPreferences> keyShardPreferences = new HashMap<>();
    Context mContext;

    public static SharedPreferences getAppSettings(Context context) {
        return getAppSettings(context, DEFAULT);
    }

    public static SharedPreferences getAppSettings(Context ctx, String storageKey) {
        if (keyShardPreferences.containsKey(storageKey)) {
            return keyShardPreferences.get(storageKey);
        } else {
            SharedPreferences appPreference = initialize(ctx, storageKey);
            return keyShardPreferences.put(storageKey, appPreference);
        }
    }

    public static synchronized SharedPreferences initialize(Context ctx, String storageKey) {
        if (storageKey == null || storageKey.isEmpty() || DEFAULT.equalsIgnoreCase(storageKey)) {
            return PreferenceManager.getDefaultSharedPreferences(ctx);
        } else {
            return ctx.getSharedPreferences(storageKey, Context.MODE_PRIVATE);
        }
    }

    public static <T> void putListObject(Context ctx, String sharedPreferenceKey, List<T> listObj, Class<T> objectClass) {
        putListObject(ctx, sharedPreferenceKey, listObj, objectClass, DEFAULT);
    }

    public static <T> void putListObject(Context ctx, String sharedPreferenceKey, List<T> listObj, Class<T> objectClass, String storageKey) {

        String json = null;
        try {
            json = LoganSquare.serialize(listObj, objectClass);
        } catch (IOException e) {
            Log.e(TAG, "Serialization failed! " + e.getMessage(), e);
        }
        putString(ctx, sharedPreferenceKey, json, storageKey);
    }

    public static <T> List<T> getListObject(Context ctx, String sharedPreferenceKey, Class<T> clazz) {
        return getListObject(ctx, sharedPreferenceKey, clazz, DEFAULT);
    }

    public static <T> List<T> getListObject(Context ctx, String sharedPreferenceKey, Class<T> clazz, String storageKey) {

        String strTojsonfy = getString(ctx, sharedPreferenceKey, storageKey);
        try {
            return strTojsonfy == null ? null : LoganSquare.parseList(strTojsonfy, clazz);
        } catch (IOException e) {
            Log.e(TAG, "failed to deserialized! " + e.getMessage(), e);
        }
        return null;
    }


    public static <T> void putObject(Context ctx, String sharedPreferenceKey, T object) {
        putObject(ctx, sharedPreferenceKey, object, DEFAULT);
    }

    public static <T> void putObject(Context ctx, String sharedPreferenceKey, T object, String storageKey) {

        String json = null;
        try {
            json = LoganSquare.serialize(object);
        } catch (IOException e) {
            Log.e(TAG, "Serialization failed! " + e.getMessage(), e);
        }
        putString(ctx, sharedPreferenceKey, json, storageKey);
    }

    public static void clearObject(Context ctx, String sharedPreferenceKey, String storageKey) {
        SharedPreferences settings = getAppSettings(ctx, storageKey);
        if (settings != null) {
            settings.edit().clear().apply();
        }

    }

    public static void clearObject(Context ctx, String sharedPreferenceKey) {
        clearObject(ctx, sharedPreferenceKey, DEFAULT);
    }

    public static <T> T getObject(Context ctx, String sharedPreferenceKey, Class<T> clazz) {
        return getObject(ctx, sharedPreferenceKey, clazz, DEFAULT);
    }

    public static <T> T getObject(Context ctx, String sharedPreferenceKey, Class<T> clazz, String storageKey) {

        String strTojsonfy = getString(ctx, sharedPreferenceKey, storageKey);
        try {
            return strTojsonfy == null ? null : LoganSquare.parse(strTojsonfy, clazz);
        } catch (IOException e) {
            Log.e(TAG, "failed to deserialized! " + e.getMessage(), e);
        }
        return null;
    }
//    public static <T> void putFlagsEnumObject(Context ctx, String sharedPreferenceKey, EnumSet flagOptionEnumSet) {
//        putString(ctx, sharedPreferenceKey, flagOptionEnumSet.toString(), DEFAULT);
//    }
//
//
//    public static EnumSet getFlagsEnumObject(Context ctx, String sharedPreferenceKey) {
//        return getFlagsEnumObject(ctx, sharedPreferenceKey, DEFAULT);
//    }
//
//    public static EnumSet getFlagsEnumObject(Context ctx, String sharedPreferenceKey, String storageKey) {
//        String strTojsonfy = getString(ctx, sharedPreferenceKey, storageKey);
//        return strTojsonfy == null ? null : parseValues(strTojsonfy, Flags.InitOption.class);
//    }

    private static <E extends Enum<E>> EnumSet<E> parseValues(String string, Class<E> clazz) {
        EnumSet<E> set = EnumSet.noneOf(clazz);
        if (string == null) {
            return set;
        }
        String[] elements = string.split(",");
        for (String element : elements) {
            element = element.trim();
            for (E type : EnumSet.allOf(clazz)) {
                if (type.name().equalsIgnoreCase(element)) {
                    set.add(type);
                    break;
                }
            }
            // ( Do we really want to ignore spurious values? )
        }
        return set;
    }

    public static String getString(Context ctx, String sharedPreferenceKey) {
        return getString(ctx, sharedPreferenceKey, DEFAULT);
    }

    public static String getString(Context ctx, String sharedPreferenceKey, String storageKey) {
        if (sharedPreferenceKey == null || getAppSettings(ctx, storageKey) == null) {
            return "";
        }
        return getAppSettings(ctx, storageKey).getString(sharedPreferenceKey, null);
    }

    public static void putString(Context ctx, String sharedPreferenceKey, String value) {
        putString(ctx, sharedPreferenceKey, value, DEFAULT);
    }

    public static void putString(Context ctx, String sharedPreferenceKey, String value, String storageKey) {
        if (sharedPreferenceKey == null || getAppSettings(ctx, storageKey) == null) {
            return;
        }
        getAppSettings(ctx, storageKey).edit().putString(sharedPreferenceKey, value).apply();
    }

    public static void remove(Context ctx, String sharedPreferenceKey) {
        remove(ctx, sharedPreferenceKey, DEFAULT);
    }

    public static void remove(Context ctx, String sharedPreferenceKey, String storageKey) {
        if (sharedPreferenceKey == null || getAppSettings(ctx, storageKey) == null) {
            return;
        }
        getAppSettings(ctx, storageKey).edit().remove(sharedPreferenceKey).apply();
    }

    public static int getInt(Context ctx, String sharedPreferenceKey) {
        return getInt(ctx, sharedPreferenceKey, DEFAULT);
    }

    public static int getInt(Context ctx, String sharedPreferenceKey, String storageKey) {
        if (sharedPreferenceKey == null || getAppSettings(ctx, storageKey) == null) {
            return -1;
        }
        return getAppSettings(ctx, storageKey).getInt(sharedPreferenceKey, -1);
    }

    public static void putInt(Context ctx, String sharedPreferenceKey, int value) {
        putInt(ctx, sharedPreferenceKey, value, DEFAULT);
    }

    public static void putInt(Context ctx, String sharedPreferenceKey, int value, String storageKey) {
        if (sharedPreferenceKey == null || getAppSettings(ctx, storageKey) == null) {
            return;
        }
        getAppSettings(ctx, storageKey).edit().putInt(sharedPreferenceKey, value).apply();
    }

    public static long getLong(Context ctx, String sharedPreferenceKey) {
        return getLong(ctx, sharedPreferenceKey, DEFAULT);
    }

    public static long getLong(Context ctx, String sharedPreferenceKey, String storageKey) {
        if (sharedPreferenceKey == null || getAppSettings(ctx, storageKey) == null) {
            return 0L;
        }
        return getAppSettings(ctx, storageKey).getLong(sharedPreferenceKey, 0L);
    }

    public static void putLong(Context ctx, String sharedPreferenceKey, long value) {
        putLong(ctx, sharedPreferenceKey, value, DEFAULT);
    }

    public static void putLong(Context ctx, String sharedPreferenceKey, long value, String storageKey) {
        if (sharedPreferenceKey == null || getAppSettings(ctx, storageKey) == null) {
            return;
        }
        getAppSettings(ctx, storageKey).edit().putLong(sharedPreferenceKey, value).apply();
    }

    public static Boolean getBool(Context ctx, String sharedPreferenceKey) {
        return getBool(ctx, sharedPreferenceKey, DEFAULT);
    }

    public static Boolean getBool(Context ctx, String sharedPreferenceKey, String storageKey) {
        return !(sharedPreferenceKey == null || getAppSettings(ctx, storageKey) == null) && getAppSettings(ctx, storageKey).getBoolean(sharedPreferenceKey, false);
    }

    public static void putBool(Context ctx, String sharedPreferenceKey, Boolean value) {
        putBool(ctx, sharedPreferenceKey, value, DEFAULT);
    }

    public static void putBool(Context ctx, String sharedPreferenceKey, Boolean value, String storageKey) {
        if (getAppSettings(ctx, storageKey) != null && getAppSettings(ctx, storageKey).edit() != null) {
            getAppSettings(ctx, storageKey).edit().putBoolean(sharedPreferenceKey, value).apply();
        }
    }

    public static int increase(Context ctx, String key) {
        int val = getInt(ctx, key);
        putInt(ctx, key, ++val);
        return val;
    }


    public static class StorageKey {
        public static final String DEFAULT = ShrdPrfs.DEFAULT;
        public static final String SHOPPING = "SHOPPING";
        public static final String CONFIG = "CONFIG";
    }

    public static class Search {
        public static final String LAST_SEARCH = "last_search";
        public static final String LAST_SEARCH_TIME = "last_search_time";
        public static final String LAST_POPUP_SHOW_TIME = "last_popup_show_time";
        public static final String ENGAGED_OFFERS = "ENGAGED_OFFERS";
        //public static final String COUPONS_BY_TERM = "coupons_by_term";
    }

    public static class Publisher {
        public static final String PUBLISHER_KEY = "PUBLISHER_KEY";
        public static final String USER_COUNTRY = "USER_COUNTRY";
    }

    public static class Settings {
        public static final String REMINDER_FREQ_AFTER_DISMISSAL = "reminder_freq_after_dismissal";
        public static final String USER_COUNTRY = "user_country";
        public static final String SHOULD_CRASH = "should_crash";
        public static final String DEV_MODE = "dev_mode";
        public static final String LAST_ACCESSIBILITY_ACTION = "LAST_ACCESSIBILITY_ACTION";
        public static final String ACCESSIBILITY_STATUS = "ACCESSIBILITY_STATUS";
        public static final String FIRST_TIME = "FIRST_TIME";
        public static final String SEE_IN_ACTION = "SEE_IN_ACTION";
        public static final String IS_RESTRICT_SHOPPING_BY_WHITE_LIST = "IS_RESTRICT_SHOPPING_BY_WHITE_LIST";
        public static final String IS_CARRIER_ON = "IS_CARRIER_ON";
        public static final String CONFIG_DATA = "CONFIG_DATA";
        public static final String BASE_URL = "BASE_URL";
        public static final String SESSION_ID = "SESSION_ID";
    }

    public static class Notification {
        public static final String NOTIFICATION_IDS = "notification_ids";
        public static final String DEMO_KEYWORDS = "DEMO_KEYWORDS";
        public static final String DEMO_SEARCH_KEYWORDS = "DEMO_SEARCH_KEYWORDS";
        public static final String DEMO_URL = "DEMO_URL";
        public static final String LAST_SCORE = "LAST_SCORE";
    }


}