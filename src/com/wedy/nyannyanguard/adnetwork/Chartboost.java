package com.wedy.nyannyanguard.adnetwork;

import com.wedy.nyannyanguard.Util;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class Chartboost {
	public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
		try {
			Class<?> adView = XposedHelpers.findClass("com.chartboost.sdk.Chartboost", lpparam.classLoader);
			XposedBridge.hookAllMethods(adView, "showInterstitial", new XC_MethodHook() {
				
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					Util.log(packageName, "Detect Chartboost showInterstitial in " + packageName);
					
					if(!test) {
						param.setResult(new Object());
					}
				}
				
			});
			Util.log(packageName, packageName + " uses Chartboost");
		}
		catch(ClassNotFoundError e) {
			return false;
		}
		return true;
	}
}
