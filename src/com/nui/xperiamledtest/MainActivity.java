package com.nui.xperiamledtest;

import java.io.DataOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	static final int LED_RED = 0;
	static final int LED_GREEN = 1;
	static final int LED_BLUE = 2;
	
    Process su;
    DataOutputStream outputStream;
    
	EditText redt;
	EditText greent;
	EditText bluet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		redt = (EditText) findViewById(R.id.editText1);
		greent = (EditText) findViewById(R.id.editText2);
		bluet = (EditText) findViewById(R.id.editText3);
		
		redt.setText("0");
		greent.setText("0");
		bluet.setText("0");
		
		try {
			su = Runtime.getRuntime().exec("su");
			outputStream = new DataOutputStream(su.getOutputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void setled(View v) {
		setXperiaMLED(LED_RED, Integer.valueOf(redt.getText().toString()));
		setXperiaMLED(LED_GREEN, Integer.valueOf(greent.getText().toString()));
		setXperiaMLED(LED_BLUE, Integer.valueOf(bluet.getText().toString()));
	}
	
	public void turnoff(View v) {
		setXperiaMLED(LED_RED,0);
		setXperiaMLED(LED_GREEN,0);
		setXperiaMLED(LED_BLUE,0);
	}
	
	void setXperiaMLED(int id, int brightness) {
		if(brightness != 0) {
			int br = getXMledValue(brightness);
			doExec("echo 1 "+id+" 0 4 "+id+" "+br+" 31 "+id+" 1 1 "+id+" 1 > /sys/class/led/fih_led/control\n");
		} else {
			doExec("echo 1 "+id+" 0 > /sys/class/led/fih_led/control\n");
		}
	}
	
	static int getXMledValue(int brightness) {
		return (brightness*2 - 1);
	}
	
	void doExec(String s) {
		try {
			outputStream.writeBytes(s);
		    outputStream.flush();
		} catch (IOException e) {
			Toast.makeText(this, "ERROR: "+e.toString(), Toast.LENGTH_SHORT).show();
		}
	}
}
