package com.wedy.nyannyanguard.adnetwork;

import static de.robv.android.xposed.XposedHelpers.findClass;
import com.wedy.nyannyanguard.Main;
import com.wedy.nyannyanguard.Util;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class AdmobGms {
	public static boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean test) {
		try {
			
			Class<?> admobBanner = findClass("com.google.android.gms.ads.AdView", lpparam.classLoader);
			Class<?> admobInter = findClass("com.google.android.gms.ads.InterstitialAd", lpparam.classLoader);
			
			XposedBridge.hookAllMethods(admobBanner, "loadAd", new XC_MethodHook() {
				
						@Override
						protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
							
							Util.log(packageName, "Detect AdmobGms Banner loadAd in " + packageName);
							
							if(!test) {
								param.setResult(new Object());
								Main.removeAdView((View) param.thisObject, packageName, true);
							}
						}
					
					});
			
			XposedBridge.hookAllMethods(admobInter, "loadAd",  new XC_MethodHook() {
				
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					
					Util.log(packageName, "Detect AdmobGms InterstitialAd loadAd in " + packageName);
					
					if(!test) {
						param.setResult(new Object());
					}
				}
			});
			
			XposedBridge.hookAllMethods(admobInter, "show",  new XC_MethodHook() {
				
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					
					Util.log(packageName, "Detect AdmobGms InterstitialAd show in " + packageName);
					
					if(!test) {
						param.setResult(new Object());
					}
				}
			});
			
			Util.log(packageName, packageName + " uses AdmobGms");
		}
		catch(ClassNotFoundError e) {
			return false;
		}
		return true;
	}
}
