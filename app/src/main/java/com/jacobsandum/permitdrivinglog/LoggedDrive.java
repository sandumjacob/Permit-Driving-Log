package com.jacobsandum.permitdrivinglog;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.Serializable;
import javax.xml.datatype.Duration;
import java.util.Calendar;
import java.lang.Object;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/**
 * Created by Jacob S on 4/22/2017.
 */

public class LoggedDrive implements Serializable, Parcelable{
    Integer Length; //In Miles
    GregorianCalendar StartingTime;
    GregorianCalendar EndingTime;
    String Duration; //In Minutes
    String Title;
    GregorianCalendar Date;

    public LoggedDrive(Integer LengthInput, GregorianCalendar StartingTimeInput, GregorianCalendar EndingTimeInput, GregorianCalendar DateInput)
    {

        Length = LengthInput; //In Miles
        StartingTime = StartingTimeInput;
        EndingTime = EndingTimeInput;
        Duration = calculateDifferenceOfTimes(StartingTime, EndingTime);
        String DateReadable = DateInput.getTime().toString();
        Date = DateInput;
        //Todo: Make title only include the date.
        Title =  Duration + " minutes, " + DateReadable;
    }

    /*public LoggedDrive(LoggedDrive LoggedDriveInput)
    {
        Length = LoggedDriveInput.getLength();
        StartingTime = LoggedDriveInput.getStartingTime();
        EndingTime = LoggedDriveInput.getEndingTime();
        Duration = calculateDifferenceOfTimes(StartingTime, EndingTime);
        Date = LoggedDriveInput.getDate();
        Title = Length + " miles, " + Date;
    }*/

    public String calculateDifferenceOfTimes(GregorianCalendar StartingTimeInput, GregorianCalendar EndingTimeInput) //Returns Minutes
    {
        GregorianCalendar t1 = StartingTimeInput;
        GregorianCalendar t2 = EndingTimeInput;

        Log.d("PermitDrivingLog", t1.toString());
        Log.d("PermitDrivingLog", t2.toString());

        Long DurationLong = (t2.getTimeInMillis() - t1.getTimeInMillis());

        Long Minutes = (DurationLong / 1000) / 60;

        return Minutes.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Length);
        dest.writeSerializable(StartingTime);
        dest.writeSerializable(EndingTime);
        dest.writeString(Duration);
        dest.writeString(Title);
        dest.writeSerializable(Date);
    }

    // Creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public LoggedDrive createFromParcel(Parcel in) {
            return new LoggedDrive(in);
        }

        public LoggedDrive[] newArray(int size) {
            return new LoggedDrive[size];
        }
    };

    // Parcel Constructor
    public LoggedDrive(Parcel in) {
        //Length
        Length = in.readInt();
        //Starting Time
        StartingTime = (GregorianCalendar) in.readSerializable();
        //Ending Time
        EndingTime = (GregorianCalendar) in.readSerializable();
        //Duration
        Duration = in.readString();
        //Title
        Title = in.readString();
        //Date
        Date = (GregorianCalendar) in.readSerializable();
    }

    public String getDuration() {
        return Duration;
    }

    public Integer getLength() {
        return Length;
    }

    public GregorianCalendar getStartingTime() {
        return StartingTime;
    }

    public GregorianCalendar getEndingTime() {
        return EndingTime;
    }

    public String getTitle() {
        //String DateReadable = Date.getTime().toString();
        //Duration = calculateDifferenceOfTimes(EndingTime, StartingTime);
        //Title =  Duration + " minutes, " + DateReadable;
        return Title;
    }

    public GregorianCalendar getDate() {
        return Date;
    }

}
