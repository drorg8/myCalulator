package com.drorg8.calculatoractivity;

import com.google.analytics.tracking.android.EasyTracker;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

	private String mMathString;
	private SQLUtils mySQL=new SQLUtils(this);

	//States:////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//0= Int state -  waiting for number 0-9 to be press, going there in the bagging of the program or after "buttonBackspace"
	//1= odd number of numbers,
	//2= connector state,
	//3= Even number of numbers,
	//4= equal state - going to this stater after the user pressed "=" button,
	//5= Point odd - getting to this state only from state 1,
	//6= Point even - getting to this state only from state 3.
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private int state;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Checking if the phone orientation
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) 
		{
			setContentView(R.layout.layout_main);

		} else {
			setContentView(R.layout.layout_main_landscape);
		}

		//getting saved global parameters
		mMathString=((caltulatorApplication) this.getApplication()).getScreanshot();
		state=((caltulatorApplication) this.getApplication()).getState();
		Log.d("Calculator", "mMathString ="+mMathString+" state="+state);

		//Setting ClickLisener for each button
		int idList[] = { R.id.button0, R.id.button1, R.id.button2,
				R.id.button3, R.id.button4, R.id.button5, R.id.button6,
				R.id.button7, R.id.button8, R.id.button9,
				R.id.buttonHistory, R.id.buttonPlus, R.id.buttonMinus,
				R.id.buttonDivide, R.id.buttonTimes, R.id.buttonDecimal,
				R.id.buttonBackspace, R.id.buttonClear, R.id.buttonEquals };

		for(int id : idList) 
		{
			View v = findViewById(id);
			v.setOnClickListener(this);
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.layout_main, menu);

		return true;
	}




	@Override
	public void onClick(View v) {

		TextView tv = (TextView)findViewById(R.id.tv);

		switch (v.getId()) {
		case R.id.buttonBackspace:
		case R.id.buttonClear:
			if(mMathString.length() > 0)
			{
				mMathString="";
				tv.setText(mMathString);
			}

			//reset the state machine
			state=0;

			Log.d("Calculator", "onClick - buttonBackspace");
			break;
			//case R.id.buttonClear:

			//if(mMathString.length() > 0)
			//{
			//				mMathString=mMathString.substring(0, mMathString.length()-1);
			//tv.setText(mMathString);
			//onClick(v);
			//}
			//Log.d("Calculator", "onClick - buttonClear");
			//break;

		case R.id.buttonEquals:
			switch (state){
			case 1:
			case 3:
				if(mMathString.length() > 0)
				{
					String tmpStr=mMathString;
					double result=calculate(mMathString);
					tv.setText("="+result);
					mMathString=Double.toString(result);
					mySQL.insertData(tmpStr+" = "+mMathString);
					//mySQL.insertData(((Button) v).getText().toString());

					state=4;
				}
			}
			break;

		case R.id.buttonTimes:
		case R.id.buttonDivide:
		case R.id.buttonMinus:
		case R.id.buttonPlus:
			switch (state){
			case 1:
			case 3:
			case 4:
				mMathString+=(((Button) v).getText().toString());
				tv.setText(mMathString);
				state=2;
				break;	
			}
			break;	
		case R.id.buttonDecimal:
			switch (state){
			case 1:
				mMathString+=(((Button) v).getText().toString());
				tv.setText(mMathString);
				state=5;
				break;
			case 3:
				mMathString+=(((Button) v).getText().toString());
				tv.setText(mMathString);
				state=6;
				break;
			}
			break;
		case R.id.buttonHistory:
			Intent intent=new Intent(getApplicationContext(),HistoryListActivity.class);
			startActivityForResult(intent, 8);
			break;
		default:
			switch (state){
			case 0:
				state=1;
				break;
			case 2:
				state=3;
				break;
			case 4:
				mMathString="";
				state=1;
				break;
			case 5:
				state=1;
				break;
			case 6:
				state=3;
				break;

			}

			mMathString+=(((Button) v).getText().toString());
			tv.setText(mMathString);
			//	mySQL.update(history, args, KEY_ROWID + "=" + rowId, null)
		}
	}


	private double calculate(String stringTOcalculate) {
		MathEval mySolver =new MathEval();
		double answer = mySolver .evaluate(stringTOcalculate);
		return answer;
		// TODO Auto-generated method stub

	}


	@Override
	protected void onResume() {
		if (mMathString.length() > 0){
			TextView tv = (TextView)findViewById(R.id.tv);
			tv.setText(mMathString);
		}

		super.onResume();
	}

	@Override
	protected void onPause() {
		((caltulatorApplication) this.getApplication()).setScreanshot(mMathString);
		((caltulatorApplication) this.getApplication()).setState(state);

		super.onPause();
	}


	@Override
	public void onStart() {
		super.onStart();
		//EasyTracker.getInstance().activityStart(this); // Add this method.
	}

	@Override
	public void onStop() {
		super.onStop();
		//EasyTracker.getInstance().activityStop(this); // Add this method.
	}

}






