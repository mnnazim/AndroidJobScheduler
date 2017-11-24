package com.example.androidjobscheduler;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.media.MediaPlayer;
import android.widget.Toast;

//Require API Level 21
public class MyJobService extends JobService {

	public MyJobService() {
	}

	@Override
	public boolean onStartJob(JobParameters params) {
		Toast.makeText(this, 
			"MyJobService.onStartJob()", 
			Toast.LENGTH_SHORT).show();
		MediaPlayer mp=MediaPlayer.create(getApplicationContext(),R.raw.beep04);
		mp.start();
		/*
		 * True - if your service needs to process 
		 * the work (on a separate thread). 
		 * False - if there's no more work to be done for this job.
		 */
		return false;
	}

	@Override
	public boolean onStopJob(JobParameters params) {

		Toast.makeText(this,
				"MyJobService.onStopJob()", 
				Toast.LENGTH_SHORT).show();
		return false;
	}

}
