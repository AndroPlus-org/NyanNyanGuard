package com.wedy.nyannyanguard.adnetwork;

import com.wedy.nyannyanguard.Main;
import com.wedy.nyannyanguard.Util;

import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class MdotM {
    public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
        try {
            
            Class<?> adView = XposedHelpers.findClass("com.mdotm.android.view.MdotMAdView", lpparam.classLoader);
            
            XposedBridge.hookAllMethods(adView, "loadBannerAd" ,new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            
                            Util.log(packageName, "Detect MdotMAdView loadBannerAd in " + packageName);
                            
                            if(!test) {
                                param.setResult(new Object());
                                Main.removeAdView((View) param.thisObject, packageName, true);
                            }
                        }
                    });
            
            Class<?> InterAds = XposedHelpers.findClass("com.mdotm.android.view.MdotMInterstitial", lpparam.classLoader);
            XposedBridge.hookAllMethods(InterAds, "loadInterstitial" ,new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    
                    Util.log(packageName, "Detect MdotMInterstitial loadInterstitial in " + packageName);
                    
                    if(!test) {
                        param.setResult(new Object());
                    }
                }
            });
            
            Util.log(packageName, packageName + " uses MdotM");
        }
        catch(ClassNotFoundError e) {
            return false;
        }
        return true;
    }
}
