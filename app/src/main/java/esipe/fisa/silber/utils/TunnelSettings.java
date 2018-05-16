package esipe.fisa.silber.utils;


import android.content.Context;
import android.content.SharedPreferences;

public class TunnelSettings {

    private static final String PREFS_NAME = "silber";

    private static final String DEFAULT_ACCOUNT = "vanduong";

    static String getAccount(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString("account", DEFAULT_ACCOUNT);
    }
}
