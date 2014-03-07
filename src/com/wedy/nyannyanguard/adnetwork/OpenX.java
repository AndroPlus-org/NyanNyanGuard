package com.wedy.nyannyanguard.adnetwork;

import com.wedy.nyannyanguard.Main;
import com.wedy.nyannyanguard.Util;

import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class OpenX {
	public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
		try {
			Class<?> adView = XposedHelpers.findClass("com.openx.ad.mobile.sdk.interfaces.OXMAdBannerView", lpparam.classLoader);
			XposedBridge.hookAllMethods(adView, "loadAd", new XC_MethodHook() {
				
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					Util.log(packageName, "Detect OXMAdBannerView loadAd in " + packageName);
					
					if(!test) {
						param.setResult(new Object());
						Main.removeAdView((View) param.thisObject, packageName, true);
					}
				}
				
			});
			Util.log(packageName, packageName + " uses OpenX");
		}
		catch(ClassNotFoundError e) {
			return false;
		}
		return true;
	}
}
