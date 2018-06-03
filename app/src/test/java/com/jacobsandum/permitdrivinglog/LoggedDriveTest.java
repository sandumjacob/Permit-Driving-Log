package com.jacobsandum.permitdrivinglog;

/**
 * Created by Jacob S on 5/4/2017.
 */
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import com.jacobsandum.permitdrivinglog.LoggedDrive;
import java.util.regex.Pattern;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class LoggedDriveTest {

    @Test
    public void testLoggedDriveCalculator()
    {
        GregorianCalendar now = new GregorianCalendar();
        now = (GregorianCalendar) GregorianCalendar.getInstance();
        GregorianCalendar later = new GregorianCalendar();
        later = (GregorianCalendar) GregorianCalendar.getInstance();
        later.add(Calendar.MINUTE, 10);
        assertThat(LoggedDrive
    }
}
