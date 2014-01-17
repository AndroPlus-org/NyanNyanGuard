package tw.fatminmin.xposed.minminguard.ui;

import java.io.File;

import tw.fatminmin.xposed.minminguard.R;
import tw.fatminmin.xposed.minminguard.Util;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class Settings extends SherlockFragmentActivity {
	
	private PrefsFragment prefFragment;
	private LogFragment logFragment;
	private Fragment fragment;
	
	private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private boolean usingPrefFragment, replaced;
    
    static public SharedPreferences pref, uiPref;
    
	@SuppressLint("WorldReadableFiles")
    @SuppressWarnings("deprecation")
    @Override
	public void onCreate(Bundle savedInstanceState) {
	    
	    uiPref = getSharedPreferences("ui_preference", MODE_PRIVATE);
	    boolean themeDark = uiPref.getBoolean("theme_dark", false);
	    if(themeDark) {
	        setTheme(com.actionbarsherlock.R.style.Theme_Sherlock);
	    }
		super.onCreate(savedInstanceState);
		
		// this is important because although the handler classes that read these settings
        // are in the same package, they are executed in the context of the hooked package
		pref = getSharedPreferences(getPackageName() + "_preferences", MODE_WORLD_READABLE);
		
		LogFragment.mHandler = new Handler();
		setContentView(R.layout.activity_main);
		
		prefFragment = new PrefsFragment();
		logFragment = new LogFragment();
		
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);        
		
        
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		
		String[] drawer_items = {getString(R.string.drawer_app_settings), 
		                         getString(R.string.drawer_log),
		                         getString(R.string.drawer_about)}; 
		
		mDrawerList.setAdapter(new ArrayAdapter<String>(Settings.this,
				android.R.layout.simple_list_item_1,
				drawer_items));
		
		mDrawerList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
			    fragment = null;
			    
				switch(position) {
				case 0:
				    fragment = prefFragment;
				    usingPrefFragment = true;
				    replaced = false;
				    break;
				case 1:
				    fragment = logFragment;
				    usingPrefFragment = false;
				    replaced = false;
				    break;
				case 2:
					optionAbout();
					break;
				}
				if(fragment != null) {
				    mDrawerLayout.closeDrawer(mDrawerList);
				}
			}
		});
		
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                
                if(!replaced) {
                    replaced = true;
                    getSupportFragmentManager().beginTransaction()
                        .replace(R.id.content_frame, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .addToBackStack(null)
                        .commit();
                }
                
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
		
//        mDrawerToggle.setDrawerIndicatorEnabled(true);
        
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
        // Display the fragment as the main content.
        replaced = true;
        usingPrefFragment = true;
        fragment = prefFragment;
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.content_frame, fragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .addToBackStack(null)
            .commit();
        supportInvalidateOptionsMenu();
	}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		
		mDrawerToggle.syncState();
		
		super.onPostCreate(savedInstanceState);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    getSupportMenuInflater().inflate(R.menu.main, menu);
	    return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
	    
	    if(usingPrefFragment) {
	        menu.findItem(R.id.select_all).setVisible(true);
            menu.findItem(R.id.save_log).setVisible(false);
	    }
	    else {
	        menu.findItem(R.id.select_all).setVisible(false);
            menu.findItem(R.id.save_log).setVisible(true);
	    }
	    
	    return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
	    switch(item.getItemId()) {
	    case android.R.id.home:
	        if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                mDrawerLayout.openDrawer(mDrawerList);
            }
	        break;
	    case R.id.switch_theme:
	        boolean themeDark = uiPref.getBoolean("theme_dark", false);
	        uiPref.edit()
	              .putBoolean("theme_dark", !themeDark)
	              .commit();
	        finish();
	        startActivity(getIntent());
	        break;
	    case R.id.refresh:
	        if(usingPrefFragment) {
	            prefFragment.refresh();
	        }
	        else {
	            logFragment.refresh();
	        }
	        break;
	    case R.id.select_all:
	        
	        String key = "select_all";
	        boolean value = !pref.getBoolean(key, false);
	        pref.edit()
	            .putBoolean(key, value)
	            .commit();
	        
	        SelectAllAsyncTask.setup(Settings.this, new Handler());
	        new SelectAllAsyncTask(value).execute(new Object());
	        break;
	    case R.id.save_log:
	        
	        File logFile = new File(Environment.getExternalStorageDirectory(), "MinMinGuard.log");
	        Util.saveLog(logFile, this, new Handler());
	        
	        break;
	    }
		
		return super.onOptionsItemSelected(item);
	}
	
	
	private void optionAbout() {
		
		Dialog dlgAbout = new Dialog(this);
		dlgAbout.requestWindowFeature(Window.FEATURE_LEFT_ICON);
		dlgAbout.setTitle(getString(R.string.title_about));
		dlgAbout.setContentView(R.layout.about);
		dlgAbout.setCancelable(true);
		dlgAbout.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_launcher);
		
		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			TextView tv = (TextView) dlgAbout.findViewById(R.id.tvVersion);
			tv.setText(String.format(getString(R.string.app_version), pInfo.versionName));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		dlgAbout.show();
		
	}
}