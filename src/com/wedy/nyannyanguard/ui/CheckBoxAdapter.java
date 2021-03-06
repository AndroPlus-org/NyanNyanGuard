package com.wedy.nyannyanguard.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.wedy.nyannyanguard.R;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class CheckBoxAdapter extends BaseAdapter {
	
	
	private List<Map<String, Object>> mItemList, oriItemList;
	private Context mContext;
	private LayoutInflater mInflater;
	private SharedPreferences pref;
	private Filter mFilter;
	
	public CheckBoxAdapter(Context context, List<Map<String, Object>> itemList) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		oriItemList = mItemList = itemList;
		pref = PreferenceManager.getDefaultSharedPreferences(mContext);
		mFilter = new MyFilter();
	}
	
	@Override
	public int getCount() {
		return mItemList.size();
	}

	@Override
	public Object getItem(int position) {
		return mItemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mItemList.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(convertView == null) {
			convertView = mInflater.inflate(R.layout.preference_checkbox, null);
		}
		
		final TextView title = (TextView) convertView.findViewById(R.id.title);
		final TextView summary = (TextView) convertView.findViewById(R.id.summary);
		final ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
		final CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.chkCheckBox);
		final ImageButton imgEdit = (ImageButton) convertView.findViewById(R.id.edit);
		
		final String sTitle = (String) mItemList.get(position).get("title");
		final String sSummary = (String) mItemList.get(position).get("summary");
		final String key = (String) mItemList.get(position).get("key");
		final Drawable dIcon = (Drawable) mItemList.get(position).get("icon");
		
		title.setText(sTitle);
		summary.setText(sSummary);
		icon.setImageDrawable(dIcon);
		
		if(pref.getBoolean(key, false)) {
		    checkBox.setChecked(true);
		    imgEdit.setVisibility(View.VISIBLE);
		}
		else {
		    checkBox.setChecked(false);
		    imgEdit.setVisibility(View.GONE);
		}
		
		imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View checkBoxView = View.inflate(mContext, R.layout.per_app_settings, null);
                CheckBox urlFilter = (CheckBox) checkBoxView.findViewById(R.id.enable_url_filter);
                CheckBox log = (CheckBox) checkBoxView.findViewById(R.id.enable_log);
                CheckBox rec_remove = (CheckBox) checkBoxView.findViewById(R.id.enable_recursive_remove);
                
                urlFilter.setChecked(pref.getBoolean(key + "_url", true));
                log.setChecked(pref.getBoolean(key + "_log", false));
                rec_remove.setChecked(pref.getBoolean(key + "_recursive", false));
                
                urlFilter.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        boolean value = cb.isChecked();
                        pref.edit()
                            .putBoolean(key + "_url", value)
                            .commit();
                    }
                });
                
                log.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        boolean value = cb.isChecked();
                        pref.edit()
                            .putBoolean(key + "_log", value)
                            .commit();
                    }
                });
                
                rec_remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        boolean value = cb.isChecked();
                        pref.edit()
                            .putBoolean(key + "_recursive", value)
                            .commit();
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle(mContext.getString(R.string.title_settings))
                           .setIcon(R.drawable.ic_launcher)
                           .setView(checkBoxView)
                           .setCancelable(false)
                           .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dlg, int id) {
                                    dlg.dismiss();
                                }
                           })
                           .show();
            }
        });
		
		checkBox.setOnClickListener(new View.OnClickListener() {
			
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                
                boolean value = cb.isChecked();
                pref.edit()
                    .putBoolean(key, value)
                    .commit();
                if(value) {
                    imgEdit.setVisibility(View.VISIBLE);
                }
                else {
                    imgEdit.setVisibility(View.GONE);
                }
            }
		});
		
		return convertView;
	}
	
	public Filter getFilter() {
	    return mFilter;
	}
	
	class MyFilter extends Filter {

        @SuppressLint("DefaultLocale")
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            
            constraint = constraint.toString().toLowerCase();
            
            FilterResults results = new FilterResults();
            
            if(constraint.length() == 0) {
                results.values = oriItemList;
                results.count = oriItemList.size();
            }
            else {
                List<Map<String, Object>> filteredList = new ArrayList<Map<String, Object>>();
                
                for(Map<String, Object> app : oriItemList) {
                    String title = ((String) app.get("title")).toLowerCase();
                    if(title.indexOf((String) constraint) == 0) {
                        filteredList.add(app);
                    }
                }
                
                results.values = filteredList;
                results.count = filteredList.size();
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            
            mItemList = (List<Map<String, Object>>) results.values;
            notifyDataSetChanged();
        }

	    
	}
	
}
