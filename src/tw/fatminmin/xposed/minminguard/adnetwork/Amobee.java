package tw.fatminmin.xposed.minminguard.adnetwork;

import tw.fatminmin.xposed.minminguard.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Amobee {
    public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
        try {
            Class<?> adView = XposedHelpers.findClass("com.amobee.adsdk.AdManager", lpparam.classLoader);
            XposedBridge.hookAllMethods(adView, "getAd", new XC_MethodHook() {
                
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Util.log(packageName, "Detect Amobee getAd in " + packageName);
                    if(!test) {
                        param.setResult(new Object());
                    }
                }
                
            });
        }
        catch(ClassNotFoundError e) {
            Util.log(packageName, packageName + " does not use Amobee");
            return false;
        }
        return true;
    }
}
