package com.jacobsandum.permitdrivinglog;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class LogPreviousDriveActivity extends AppCompatActivity {

    DatePicker datePicker;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_previous_drive);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        editText = (EditText) findViewById(R.id.editText_length);
    }

    public void doneButtonClicked(View view)
    {
        String lengthText = editText.getText().toString();
        if (lengthText != "")
        {
            Intent data = new Intent();
            GregorianCalendar today = new GregorianCalendar();
            today.set(Calendar.DAY_OF_YEAR, 0);
            today.set(Calendar.MONTH, 0);
            today.set(Calendar.YEAR, 0);
            GregorianCalendar datePickerCal = new GregorianCalendar();
            datePickerCal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
            LoggedDrive previousDrv = new LoggedDrive(Integer.valueOf(editText.getText().toString()), today, today, datePickerCal);
            data.putExtra("previousDriveKey", (Parcelable) previousDrv);
            setResult(RESULT_OK, data);
            finish();
        }


    }

}
