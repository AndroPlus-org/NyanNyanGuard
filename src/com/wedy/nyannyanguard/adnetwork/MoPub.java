package com.wedy.nyannyanguard.adnetwork;

import static de.robv.android.xposed.XposedHelpers.findClass;

import com.wedy.nyannyanguard.Main;
import com.wedy.nyannyanguard.Util;

import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class MoPub {
	public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
		try {
					
			Class<?> adview = findClass("com.mopub.mobileads.MoPubView", lpparam.classLoader);
			
			XposedBridge.hookAllMethods(adview, "loadAd", new XC_MethodHook() {
				
						@Override
						protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
							
							Util.log(packageName, "Detect MoPub loadAd in " + packageName);
							
							if(!test) {
								param.setResult(new Object());
								Main.removeAdView((View) param.thisObject, packageName, true);
							}
						}
					
					});
			Class<?> banner = findClass("com.mopub.mobileads.MraidBanner", lpparam.classLoader);
			XposedBridge.hookAllMethods(banner, "loadBanner", new XC_MethodHook() {
				
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					
					Util.log(packageName, "Detect MoPub loadBanner in " + packageName);
					
					if(!test) {
						param.setResult(new Object());
						Main.removeAdView((View) param.thisObject, packageName, true);
					}
				}
			
			});
			Util.log(packageName, packageName + " uses MoPub");
		}
		catch(ClassNotFoundError e) {
			return false;
		}
		return true;
	}
}
