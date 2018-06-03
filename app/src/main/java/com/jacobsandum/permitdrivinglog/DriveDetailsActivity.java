package com.jacobsandum.permitdrivinglog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.sax.StartElementListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import org.w3c.dom.Text;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DriveDetailsActivity extends AppCompatActivity {

    private LoggedDrive theDrive;

    private EditText lengthTextBox;

    private Integer deleteIndex;

    private Intent intent;

    private TextView textStartingTime;
    private TextView textEndingTime;
    private TextView textDate;

    GregorianCalendar StartingTime;
    GregorianCalendar EndingTime;
    GregorianCalendar DateThing;
    //UI
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_details);
        //The data from the MainActivity
        Bundle bundle = getIntent().getExtras();
        intent = getIntent();

        //Setup UI for the logged drive
        lengthTextBox = (EditText) findViewById(R.id.editText_length);
        textStartingTime = (TextView) findViewById(R.id.textStartingTime);
        textEndingTime = (TextView) findViewById(R.id.textEndingTime);
        textDate = (TextView) findViewById(R.id.textDate);

        LoggedDrive drv = getIntent().getExtras().getParcelable("TheDriveToEdit");

        StartingTime = drv.getStartingTime();
        EndingTime = drv.getEndingTime();
        DateThing = drv.getDate();

        Integer lengthFromIntent = drv.getLength();
        lengthTextBox.setText(lengthFromIntent.toString());


       /* Integer StartingTimeHour = drv.getStartingTime().get(Calendar.HOUR);
        Integer StartingTimeMinute = drv.getStartingTime().get(Calendar.MINUTE);
        String StartingTimeMeridian = "";
        if (drv.getStartingTime().get(Calendar.AM_PM) == Calendar.AM)
        {
            StartingTimeMeridian = "AM";
        }
        if (drv.getStartingTime().get(Calendar.AM_PM) == Calendar.PM)
        {
            StartingTimeMeridian = "PM";
        }
        String startingTimeFromIntentAsString = "" + StartingTimeHour + ":" + StartingTimeMinute + " " + StartingTimeMeridian;*/
        SimpleDateFormat formatter=new SimpleDateFormat("h:mm a");
        textStartingTime.setText(formatter.format(StartingTime.getTime()));

        /*Integer EndingTimeHour = drv.getEndingTime().get(Calendar.HOUR);
        Integer EndingTimeMinute = drv.getEndingTime().get(Calendar.MINUTE);
        String EndingTimeMeridian = "";
        if (drv.getStartingTime().get(Calendar.AM_PM) == Calendar.AM)
        {
            EndingTimeMeridian = "AM";
        }
        if (drv.getStartingTime().get(Calendar.AM_PM) == Calendar.PM)
        {
            EndingTimeMeridian = "PM";
        }
        String endingTimeFromIntentAsString = "" + EndingTimeHour + ":" + EndingTimeMinute + " " + EndingTimeMeridian;*/
        textEndingTime.setText(formatter.format(EndingTime.getTime()));


        Integer Day = drv.getDate().get(Calendar.DAY_OF_MONTH);
        Integer Month = drv.getDate().get(Calendar.MONTH);
        Integer Year = drv.getDate().get(Calendar.YEAR);
        String dateFromIntentAsString = "" + Month + "/" + Day + "/" + Year;
        textDate.setText(dateFromIntentAsString);

        deleteIndex = intent.getIntExtra("IndexData", -50); //If you see -50, you know you screwed up
        Log.d("PermitDrivingLog", "Delete index = " + deleteIndex);
    }

    @Override
    public void onStart() {
        super.onStart();  // Always call the superclass method first

        Log.d("PermitDrivingLog", "On Start");

    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        Log.d("PermitDrivingLog", "On Resume");

    }

    void editStartingTimeButtonClicked(View view) {
        Log.d("PermitDrivingLog", "Edit Starting Time Button Clicked");
        final LayoutInflater inflater = this.getLayoutInflater();
        final Context c = this;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View dialogView = inflater.inflate(R.layout.user_input_previous_drive_2, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Edit Details");
        dialogBuilder.setMessage("Enter Starting Time");

        final TimePicker startingDate = (TimePicker) dialogView.findViewById(R.id.userStartingTimeInputDialog);

        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                GregorianCalendar cal = new GregorianCalendar();
                cal.set(Calendar.DAY_OF_YEAR, 0);
                cal.set(Calendar.MONTH, 0);
                cal.set(Calendar.YEAR, 0);
                Log.d("PermitDrivingLog", "Today is: " + cal.toString());
                Integer HourOfTM = Integer.valueOf(startingDate.getHour());
                Integer MinuteOfTM = Integer.valueOf(startingDate.getMinute());

                cal.set(Calendar.HOUR_OF_DAY, HourOfTM);
                cal.set(Calendar.MINUTE, MinuteOfTM);
                Log.d("PermitDrivingLog", "Modified: " + cal.toString());
                String Meridian = "";
                if (cal.get(Calendar.AM_PM) == Calendar.AM)
                {
                    Meridian = "AM";
                }
                if (cal.get(Calendar.AM_PM) == Calendar.PM)
                {
                    Meridian = "PM";
                }
                SimpleDateFormat formatter=new SimpleDateFormat("h:mm a");
                //String HourReadable = String.valueOf(cal.get(Calendar.HOUR));
                //String MinuteReadable = String.valueOf(cal.get(Calendar.MINUTE));
                //String TimeAsString = "" + HourReadable + ":" + MinuteReadable + " " + Meridian;
                String TimeAsString = formatter.format(StartingTime.getTime());
                textStartingTime.setText(TimeAsString);
                StartingTime = cal;
            }
        }); dialogBuilder.show();
    }

    void editEndingTimeButtonClicked(View view)
    {
        Log.d("PermitDrivingLog", "Edit Ending Time Button Clicked");
        final LayoutInflater inflater = this.getLayoutInflater();
        final Context c = this;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View dialogView = inflater.inflate(R.layout.user_input_previous_drive_3, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Edit Details");
        dialogBuilder.setMessage("Enter Ending Time");

        final TimePicker endingDate = (TimePicker) dialogView.findViewById(R.id.userEndingTimeInputDialog);

        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                GregorianCalendar cal = new GregorianCalendar();
                cal.set(Calendar.DAY_OF_YEAR, 0);
                cal.set(Calendar.MONTH, 0);
                cal.set(Calendar.YEAR, 0);
                Log.d("PermitDrivingLog", "Today is: " + cal.toString());
                Integer HourOfTM = Integer.valueOf(endingDate.getHour());
                Integer MinuteOfTM = Integer.valueOf(endingDate.getMinute());

                cal.set(Calendar.HOUR_OF_DAY, HourOfTM);
                cal.set(Calendar.MINUTE, MinuteOfTM);
                Log.d("PermitDrivingLog", "Modified: " + cal.toString());
                /*String Meridian = "";
                if (cal.get(Calendar.AM_PM) == Calendar.AM)
                {
                    Meridian = "AM";
                }
                if (cal.get(Calendar.AM_PM) == Calendar.PM)
                {
                    Meridian = "PM";
                }*/
                SimpleDateFormat formatter=new SimpleDateFormat("h:mm a");
                //String HourReadable = String.valueOf(cal.get(Calendar.HOUR));
                //String MinuteReadable = String.valueOf(cal.get(Calendar.MINUTE));
                //String TimeAsString = "" + HourReadable + ":" + MinuteReadable + " " + Meridian;
                String TimeAsString = formatter.format(EndingTime.getTime());
                textEndingTime.setText(TimeAsString);
                EndingTime = cal;
            }
        }); dialogBuilder.show();
    }

    void editDateButtonClicked(View view)
    {
        Log.d("PermitDrivingLog", "Edit Date Button Clicked");
        final LayoutInflater inflater = this.getLayoutInflater();
        final Context c = this;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View dialogView = inflater.inflate(R.layout.user_input_previous_drive_4, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle("Edit Details");
        dialogBuilder.setMessage("Enter Starting Time");

        final DatePicker date = (DatePicker) dialogView.findViewById(R.id.userDateInputDialog);

        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                GregorianCalendar newDate = new GregorianCalendar();
                newDate.set(date.getYear(), date.getMonth(), date.getDayOfMonth());

                DateThing = newDate;

                String Year = String.valueOf(date.getYear());
                String Month = String.valueOf(date.getMonth());
                String Day = String.valueOf(date.getDayOfMonth());
                String Conjoined = Year + "/" + Month + "/" + Day;
                textDate.setText(Conjoined);
            }
        }); dialogBuilder.show();
    }

    void saveButtonClicked(View view)
    {
        Log.d("PermitDrivingLog", "Save Button Clicked");
        Intent saveIntent = new Intent();
        Log.d("PermitDrivingLog", "SaveLength = " + lengthTextBox.getText().toString());
        Integer saveLength = Integer.valueOf(lengthTextBox.getText().toString());
        GregorianCalendar saveStartingTime = StartingTime;
        GregorianCalendar saveEndingTime = EndingTime;
        GregorianCalendar saveDate = DateThing;
        saveIntent.putExtra("IndexToEdit", deleteIndex);
        LoggedDrive savedDrive = new LoggedDrive(saveLength,saveStartingTime,saveEndingTime,saveDate);
        saveIntent.putExtra("SaveDrive", (Parcelable) savedDrive);
        setResult(22, saveIntent);
        finish();
    }


    void deleteButtonClicked(View view)
    {
        Log.d("PermitDrivingLog", "Delete Button Clicked");
        Intent deleteIntent = new Intent();
        deleteIntent.putExtra("DeleteAtIndex", deleteIndex);
        setResult(RESULT_OK, deleteIntent);
        finish();
    }
}
