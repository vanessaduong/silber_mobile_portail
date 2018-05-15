package esipe.fisa.silber.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class TunnelSettings {

    private static final String PREFS_NAME = "silber";
    private static final String PREF_KEY_URL = "url";

    private static final String DEFAULT_URL = "vanduong";

    static void setUrl(Context context, String url) {
        final SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(PREF_KEY_URL, url).apply();
    }

    static String getUrl(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString("url", DEFAULT_URL);
    }
}
