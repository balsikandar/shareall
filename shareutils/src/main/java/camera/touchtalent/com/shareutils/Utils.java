package camera.touchtalent.com.shareutils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by bali on 13/04/17.
 */

public class Utils {

    //this class is not instantiable
    private Utils() {
    }

    /**
     * @param context     used to get PackageManeger instance
     * @param intent      to retrieve all activities that can be performed for the given intent
     * @param packageName The name of the package that the component exists in.  Can
     *                    not be null.
     * @return Identifier for a specific application component
     */
    public static ComponentName getComponentName(Context context, Intent intent, @NonNull String packageName) {
        ComponentName returnValue = null;
        List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfoList) {
            if (resolveInfo.activityInfo.applicationInfo.packageName.equalsIgnoreCase(packageName)) {
                returnValue = new ComponentName(resolveInfo.activityInfo.applicationInfo.packageName, resolveInfo.activityInfo.name);
            }
        }
        return returnValue;
    }
}
