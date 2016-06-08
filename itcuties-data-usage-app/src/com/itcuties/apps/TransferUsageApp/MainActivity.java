package com.itcuties.apps.TransferUsageApp;

import java.security.Permission;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import android.app.ActivityManager;
import  android.app.ActivityManager.RunningServiceInfo;
import android.app.AppOpsManager;
import android.app.Application;
import android.app.KeyguardManager;
import android.app.ListActivity;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.itcuties.apps.TransferUsageApp.adapters.ListAdapter;

public class MainActivity extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get running processes
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningTask = manager.getRunningServices(Integer.MAX_VALUE);

		if (runningTask != null && runningTask.size() > 0) {
			// Set data to the list adapter
			setListAdapter(new ListAdapter(this, runningTask));
		} else {
			// In case there are no processes running (not a chance :))
			Toast.makeText(getApplicationContext(), "No application is running", Toast.LENGTH_LONG).show();
		}

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		long send 		= 0;
		long recived 	= 0;

		// Get UID of the selected process

		int uid = ((RunningServiceInfo)getListAdapter().getItem(position)).uid;
		int pid = ((RunningServiceInfo)getListAdapter().getItem(position)).pid;
		String ParentName = "non";

			ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
	List<RunningServiceInfo> runningTask = manager.getRunningServices(Integer.MAX_VALUE);
	if (runningTask != null && runningTask.size() > 0) {
		// Set data to the list adapter
		setListAdapter(new ListAdapter(this, runningTask));
	}
		for(int j = 0; j > runningTask.size() ; j++)
		{
			int uids = ((RunningServiceInfo)getListAdapter().getItem(j)).uid;
			if (pid == uids)
				ParentName = runningTask.get(j).service.getPackageName();
		}

		// Get traffic data
		recived = TrafficStats.getUidRxBytes(uid);
//		send = TrafficStats.getUidTxBytes(uid);


		Semaphore sem = new Semaphore(10074);


		// Display data
		Toast.makeText(getApplicationContext(), "UID " + sem.getQueueLength()  + "\n" + uid + "\n Parent " + ParentName + " details...\n RAM: "  + recived/(2*1024*1024) + "%", Toast.LENGTH_LONG).show();
	}

}
