package com.drorg8.calculatoractivity;

import android.app.Application;

public class caltulatorApplication extends Application{
	private String Screanshot="";
	int state=0;

    public String getScreanshot() {
        return Screanshot;
    }
    public void setScreanshot(String newScreanshot) {
        this.Screanshot = newScreanshot;
    }
    
    public int getState() {
		return state;
	}
    public void setState(int state) {
		this.state = state;
	}
    
}
