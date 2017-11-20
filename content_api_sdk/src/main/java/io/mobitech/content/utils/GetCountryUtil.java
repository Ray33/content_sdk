package io.mobitech.content.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.util.Locale;

/**
 * Utility class to determine user country by cellular network
 *
 * @author Sergey Pogoryelov
 */

public class GetCountryUtil {

    /**
     * Determines user country by cellular network
     * @param context android context
     * @return User country
     */
    public static String getUserCountryByCellularNetwork(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(
                    Context.TELEPHONY_SERVICE);

            if (tm != null) {
                final String simCountry = tm.getSimCountryIso();
                if (simCountry != null && simCountry.length() == 2) {
                    return simCountry.toUpperCase(Locale.US);
                } else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) {
                    String networkCountry = tm.getNetworkCountryIso();
                    if (networkCountry != null && networkCountry.length() == 2) {
                        return networkCountry.toUpperCase(Locale.US);
                    }
                }
            }
        } catch (Exception ignored) {
            // Ignore exception
        }
        return Locale.getDefault().getCountry().toLowerCase(Locale.US);
    }
}
