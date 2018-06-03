package com.jacobsandum.permitdrivinglog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Time;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {
    private ListView mListView;
    private ArrayList drives;
    private ArrayList convertedDrivesToTitles;
    private ArrayAdapter adapter;

    private TextView totalMileage;

    final Context c = this;

    final static String driving_Log_File_Name = "DrivingLog";

    final static int log_previous_drive_request_code = 1;
    final static int drive_details_request_code = 2;
    final static int drive_delete_request_code = 3;
    final static int drive_start_new_drive_request_code = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        totalMileage = (TextView) findViewById(R.id.total_Mileage);
        drives = new ArrayList<LoggedDrive>();
        load();
        convertedDrivesToTitles = new ArrayList<String>();
        mListView = (ListView) findViewById(R.id.logged_list_view);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, convertedDrivesToTitles);
        for(int i=0; i<drives.size(); i++){
            LoggedDrive driveInstance = (LoggedDrive) drives.get(i);
            String converted = driveInstance.getTitle();
            convertedDrivesToTitles.add(converted);
        }
        mListView.setAdapter(adapter);
        totalMileage.setText("Total Mileage: " + getTotalMileage() + " Miles");

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Go to details activity
                LoggedDrive theDriveToEdit;
                Integer RealPos = position;
                theDriveToEdit = (LoggedDrive) drives.get(RealPos);
                //Log.d("PermitDrivingLog", "Item at position: " + theDriveToEdit.getTitle());
                Intent theDriveToEditI = new Intent(getBaseContext(), DriveDetailsActivity.class);
                theDriveToEditI.putExtra("TheDriveToEdit", (Parcelable) theDriveToEdit);
                theDriveToEditI.putExtra("IndexData", position);
                startActivityForResult(theDriveToEditI, drive_delete_request_code);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbaritems, menu);
        return true;
    }
    @Override
    public void onStart() {
        super.onStart();  // Always call the superclass method first
        //Log.d("PermitDrivingLog", "On Start");
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        //Log.d("PermitDrivingLog", "On Resume");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                //Start A Drive
                Intent i = new Intent(getBaseContext(), CurrentDriveActivity.class);
                startActivityForResult(i, drive_start_new_drive_request_code);
                return true;
            case  R.id.action_add_previous:
                //Go to entering activity
                Intent i2 = new Intent(getBaseContext(), LogPreviousDriveActivity.class);
                startActivityForResult(i2,log_previous_drive_request_code);
                return  true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    void addPreviousDriveToList(Integer InputtedLength, GregorianCalendar InputtedStartingTime, GregorianCalendar InputtedEndingTime, GregorianCalendar InputtedDate) {
        LoggedDrive newDrive = new LoggedDrive(InputtedLength, InputtedStartingTime, InputtedEndingTime, InputtedDate);
        drives.add(newDrive);
        convertedDrivesToTitles.add(newDrive.getTitle());
        adapter.notifyDataSetChanged();

        totalMileage.setText("Total Mileage: " + getTotalMileage().toString() + " Miles");
        save();
    }

    void removeDrive(Integer index)
    {
        Log.d("PermitDrivingLog", "Size of drives array was = " + drives.size());
        Log.d("PermitDrivingLog", "Deleting at index: " + index.intValue());
        this.adapter.remove(index.intValue());
        this.drives.remove(index.intValue());
        this.convertedDrivesToTitles.remove(index.intValue());
        Log.d("PermitDrivingLog", "Size of drives array now = " + drives.size());

        totalMileage.setText("Total Mileage: " + getTotalMileage().toString() + " Miles");
        adapter.notifyDataSetChanged();
        save();
    }

    Integer getTotalMileage()
    {
        Integer count = 0;
        for (int i = 0; i < drives.size(); i++)
        {
            LoggedDrive drive = (LoggedDrive) drives.get(i);
            Integer driveMileage = drive.getLength();
            count = count + driveMileage;
        }
        return count;
    }

    private void save()
    {
        try {
            FileOutputStream fileOutputStream = openFileOutput(driving_Log_File_Name, Context.MODE_PRIVATE);
            ObjectOutputStream out = new ObjectOutputStream(fileOutputStream);
            out.writeObject(drives);
            out.close();
            fileOutputStream.close();
            Log.d("PermitDrivingLog", "Saved");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("PermitDrivingLog", "Save Exception");
        }
    }

    private void load()
    {
        FileInputStream fis;
        try {
            fis = openFileInput(driving_Log_File_Name);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<Object> returnlist = (ArrayList<Object>) ois.readObject();
            drives = returnlist;
            ois.close();
            Log.d("PermitDrivingLog", "Loaded");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("PermitDrivingLog", "Load Exception");
        }
    }

    private Date stringToDate(String aDate,String aFormat) {

        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == log_previous_drive_request_code) {
            if (resultCode == RESULT_OK) {
                Log.d("PermitDrivingLog", "Received Data");

                LoggedDrive previousDrv = data.getExtras().getParcelable("previousDriveKey");

                Integer lengthResult = previousDrv.getLength();
                GregorianCalendar startingTimeResult = previousDrv.getStartingTime();
                GregorianCalendar endingTimeResult = previousDrv.getEndingTime();
                GregorianCalendar dateResult = previousDrv.getDate();

                addPreviousDriveToList(lengthResult, startingTimeResult, endingTimeResult, dateResult);
                save();
            }
        }
        if (requestCode == drive_delete_request_code) {
            if (resultCode == RESULT_OK) {
                Log.d("PermitDrivingLog", "Received Request To Delete At Index: " + data.getIntExtra("DeleteAtIndex", -1));
                Integer IndexToDelete = data.getIntExtra("DeleteAtIndex", 0);
                removeDrive(IndexToDelete);
                save();
            }
            if (resultCode == 22){
                Log.d("PermitDrivingLog","Received Data Of Edit");
                LoggedDrive editData = (LoggedDrive) data.getExtras().getSerializable("SaveDrive");
                Integer IndexToEdit = data.getIntExtra("IndexToEdit", 0);
                Integer LengthEdit = editData.getLength();
                GregorianCalendar StartingTimeEdit = editData.getStartingTime();
                GregorianCalendar EndingTimeEdit = editData.getEndingTime();
                GregorianCalendar DateEdit = editData.getDate();
                LoggedDrive driveToEdit = new LoggedDrive(LengthEdit, StartingTimeEdit, EndingTimeEdit, DateEdit);
                drives.set(IndexToEdit, driveToEdit);

                String converted = driveToEdit.getTitle();
                convertedDrivesToTitles.set(IndexToEdit, converted);

                //Basically need to refresh the title of the drive that was edited.
                //IndexToEdit refresh here
                String driveToEditString = driveToEdit.getTitle();
                convertedDrivesToTitles.set(IndexToEdit, driveToEditString);
                adapter.notifyDataSetChanged();
                save();
            }
        }
        if (requestCode == drive_start_new_drive_request_code) {
            if (resultCode == RESULT_OK) {
                LoggedDrive newDrive = (LoggedDrive) data.getExtras().getSerializable("newDriveToAdd");
                Integer LengthResult = newDrive.getLength();
                GregorianCalendar StartingTimeResult = newDrive.getStartingTime();
                GregorianCalendar EndingTimeResult = newDrive.getEndingTime();
                GregorianCalendar DateResult = newDrive.getDate();
                addPreviousDriveToList(LengthResult, StartingTimeResult, EndingTimeResult, DateResult);
                save();

            }
        }
    }

    //void showLogPreviousDialogBox()
    /*{
        final LayoutInflater inflater = this.getLayoutInflater();
        final Context c = this;
        //First Dialog
        AlertDialog.Builder dialogBuilder1 = new AlertDialog.Builder(this);
        LayoutInflater inflater1 = this.getLayoutInflater();
        final View dialogView1 = inflater1.inflate(R.layout.user_input_previous_drive_1, null);
        dialogBuilder1.setView(dialogView1);
        dialogBuilder1.setTitle("Log Previous Drive");
        dialogBuilder1.setMessage("Enter Length of Drive");

        //Third Dialog
        AlertDialog.Builder dialogBuilder3 = new AlertDialog.Builder(this);
        LayoutInflater inflater3 = this.getLayoutInflater();
        final View dialogView3 = inflater2.inflate(R.layout.user_input_previous_drive_3, null);
        dialogBuilder3.setView(dialogView3);
        dialogBuilder3.setTitle("Log Previous Drive");
        dialogBuilder3.setMessage("Enter Ending Time of Drive");
        AlertDialog b3 = dialogBuilder3.create();

        //Fourth Dialog
        AlertDialog.Builder dialogBuilder4 = new AlertDialog.Builder(this);
        LayoutInflater inflater4 = this.getLayoutInflater();
        final View dialogView4 = inflater4.inflate(R.layout.user_input_previous_drive_4, null);
        dialogBuilder4.setView(dialogView4);
        dialogBuilder4.setTitle("Log Previous Drive");
        dialogBuilder4.setMessage("Enter Date of Drive");
        AlertDialog b4 = dialogBuilder4.create();

        //Values of the inputs
        final NumberPicker length = (NumberPicker) dialogView1.findViewById(R.id.userLengthInputDialog);
        final TimePicker startingDate = (TimePicker) dialogView2.findViewById(R.id.userStartingTimeInputDialog);
        final TimePicker endingDate = (TimePicker) dialogView3.findViewById(R.id.userEndingTimeInputDialog);
        final DatePicker date = (DatePicker) dialogView4.findViewById(R.id.userDateInputDialog);


        dialogBuilder1.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                //Second Dialog
                AlertDialog.Builder dialogBuilder2 = new AlertDialog.Builder(c);
                final View dialogView2 = inflater.inflate(R.layout.user_input_previous_drive_2, null);
                dialogBuilder2.setView(dialogView2);
                dialogBuilder2.setTitle("Log Previous Drive");
                dialogBuilder2.setMessage("Enter Starting Time of Drive");
                AlertDialog b2 = dialogBuilder2.create();

                dialogBuilder2.setPositiveButton("Done", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //Third Dialog
                        AlertDialog.Builder dialogBuilder3 = new AlertDialog.Builder(c);
                        final View dialogView3 = inflater.inflate(R.layout.user_input_previous_drive_3, null);
                        dialogBuilder3.setView(dialogView3);
                        dialogBuilder3.setTitle("Log Previous Drive");
                        dialogBuilder3.setMessage("Enter Ending Time of Drive");
                        AlertDialog b3 = dialogBuilder3.create();

                    }

                });
                b2.show();
            }
        });

        dialogBuilder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //hide?
            }
        });
        AlertDialog b1 = dialogBuilder1.create();
        b1.show();
    }*/
}
