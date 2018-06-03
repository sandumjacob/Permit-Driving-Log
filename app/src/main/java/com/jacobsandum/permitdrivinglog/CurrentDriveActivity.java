package com.jacobsandum.permitdrivinglog;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CurrentDriveActivity extends AppCompatActivity {

    LoggedDrive newDrive;
    Long t0;
    GregorianCalendar StartingTime;
    GregorianCalendar EndingTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_drive);
        t0 = System.currentTimeMillis();
        StartingTime = new GregorianCalendar();
        StartingTime.set(Calendar.DAY_OF_YEAR, 0);
        StartingTime.set(Calendar.MONTH, 0);
        StartingTime.set(Calendar.YEAR, 0);
    }

    public void DriveDoneButtonClicked(View view)
    {
        //Might not even need the millis
        Long DurationInMillis = System.currentTimeMillis() - t0;
        int seconds = (int) (DurationInMillis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        //Date
        GregorianCalendar dateToday = new GregorianCalendar();
        dateToday.set(Calendar.HOUR, 0);
        dateToday.set(Calendar.SECOND, 0);
        dateToday.set(Calendar.MINUTE, 0);
        dateToday.set(Calendar.MILLISECOND, 0);

        //Ending Time
        EndingTime = new GregorianCalendar();
        EndingTime.set(Calendar.DAY_OF_YEAR, 0);
        EndingTime.set(Calendar.MONTH, 0);
        EndingTime.set(Calendar.YEAR, 0);

        newDrive = new LoggedDrive(0,StartingTime,EndingTime,dateToday);
        Intent addIntent = new Intent();
        addIntent.putExtra("newDriveToAdd", (Parcelable) newDrive);
        setResult(RESULT_OK, addIntent);
        finish();
    }
}
