package com.wedy.nyannyanguard.custom_mod;

import android.view.View;
import de.robv.android.xposed.callbacks.XC_InitPackageResources.InitPackageResourcesParam;
import de.robv.android.xposed.callbacks.XC_LayoutInflated;

public class Habitbrowser {
    public static void handleInitPackageResources(InitPackageResourcesParam resparam) {
        if(!resparam.packageName.equals("jp.ddo.pigsty.HabitBrowser")) {
                return;
        }
        
        resparam.res.hookLayout("jp.ddo.pigsty.HabitBrowser", "layout", "activity_bookmark_layout", new XC_LayoutInflated() {
                
                @Override
                public void handleLayoutInflated(LayoutInflatedParam liparam) throws Throwable {
  
                        View ad = (View) liparam.view.findViewById(
                                        liparam.res.getIdentifier("AdPanel", "id", "jp.ddo.pigsty.HabitBrowser"));
                        
                        ad.setVisibility(View.GONE);                                        
                }
        });
    }
}
