package com.example.androidjobscheduler;

import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	
	Chronometer chronometer;
	Button btnStartJob, btnCancelJobs;
	int period=0;
	JobScheduler jobScheduler;
	private static final int MYJOBID = 1;
	EditText ettime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
		ettime=(EditText)findViewById(R.id.ettime);
        chronometer = (Chronometer)findViewById(R.id.chronometer);
        btnStartJob = (Button)findViewById(R.id.startjob);
        btnCancelJobs =  (Button)findViewById(R.id.canceljobs);

        jobScheduler = (JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
        
        btnStartJob.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {

				if(ettime.getText().toString()==""){
					period=10000;
				}else{

					period=Integer.parseInt(ettime.getText().toString());
					if(period<=0){
						period=10000;
						Toast.makeText(getApplicationContext(),"Time set to 10000 mils",Toast.LENGTH_SHORT).show();
					}
				}
				chronometer.setBase(SystemClock.elapsedRealtime());
				chronometer.start();

				
				ComponentName jobService = 
					new ComponentName(getPackageName(), MyJobService.class.getName());
				JobInfo jobInfo = 
					new JobInfo.Builder(MYJOBID, jobService).setPeriodic(period*1000).build();
				/*
				 * setPeriodic(long intervalMillis)
				 * Specify that this job should recur with the provided interval, 
				 * not more than once per period.
				 */
				
				int jobId = jobScheduler.schedule(jobInfo);
				if(jobScheduler.schedule(jobInfo)>0){
					Toast.makeText(MainActivity.this, 
						"Successfully scheduled job: " + jobId, 
						Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(MainActivity.this, 
							"RESULT_FAILURE: " + jobId, 
							Toast.LENGTH_SHORT).show();
				}
							
			}});
        
        btnCancelJobs.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				chronometer.stop();
				
				List<JobInfo> allPendingJobs = jobScheduler.getAllPendingJobs();
				String s = "";
				for(JobInfo j : allPendingJobs){
					int jId = j.getId();
					jobScheduler.cancel(jId);
					s += "jobScheduler.cancel(" + jId + " )";
				}
				Toast.makeText(MainActivity.this, 
						s, 
						Toast.LENGTH_SHORT).show();
				
				//or
				//jobScheduler.cancelAll();
				
				
			}});
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater=getMenuInflater();
		inflater.inflate(R.menu.main,menu);
    	return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==R.id.mstatus){

			return true;
		}
		return false;
	}
}
