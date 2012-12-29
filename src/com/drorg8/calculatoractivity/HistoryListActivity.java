package com.drorg8.calculatoractivity;

import java.util.ArrayList;

import com.google.analytics.tracking.android.EasyTracker;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

public class HistoryListActivity extends ListActivity {

	private SQLUtils mySQL=new SQLUtils(this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history_list);

		ArrayList<String> historys=	mySQL.getReverseHistoryLines();
		ListAdapter adapter= new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, historys);
		setListAdapter(adapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_history_list, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId())
		{
		case R.id.del_history:
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			builder.setMessage("Are You Sure?");
			builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() 
			{

				@Override
				public void onClick(DialogInterface dialog, int which) 
				{
					mySQL.clearHistory();
					Intent intent= new Intent();
					setResult(Activity.RESULT_OK, intent);
					finish();
				}
			});
			builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			
			AlertDialog alert=builder.create();
			alert.show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onStart() {
		super.onStart();
		//EasyTracker.getInstance().activityStart(this); // Add this method.
	}

	@Override
	public void onStop() {
		super.onStop();
	//	EasyTracker.getInstance().activityStop(this); // Add this method.
	}

}
