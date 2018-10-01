package jpmorgan.dailytrade.workingdays;
import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class WorkingDayScheduleTest {
	
	private static DateFormat format;
	
	private WorkingDaySchedule workingDaySchedule;
	private Date input;
	private Date expected;

	public WorkingDayScheduleTest (WorkingDaySchedule workingDaySchedule, Date input, Date expected) {
		this.workingDaySchedule = workingDaySchedule;
		this.input = input;
		this.expected = expected;
	}
	
	@Parameters
    public static Collection<Object[]> data() throws ParseException {
    	DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    	Date sat22092018, sun23092018;
    	Date mon24092018, tue25092018, wed26092018, thu27092018, fri28092018, sat29092018, sun30092018, mon01102018;
    	WorkingDaySchedule allWeek = new WorkingDaySchedule(Calendar.SUNDAY, Calendar.SATURDAY);
    	WorkingDaySchedule monToFri = new WorkingDaySchedule(Calendar.MONDAY, Calendar.FRIDAY);
    	WorkingDaySchedule sunToThu = new WorkingDaySchedule(Calendar.SUNDAY, Calendar.THURSDAY);
  
    	sat22092018 = format.parse("22/09/2018");
    	sun23092018 = format.parse("23/09/2018");
    	
		mon24092018 = format.parse("24/09/2018");
		tue25092018 = format.parse("25/09/2018");
		wed26092018 = format.parse("26/09/2018");
		thu27092018 = format.parse("27/09/2018");
		fri28092018 = format.parse("28/09/2018");
		sat29092018 = format.parse("29/09/2018");
		sun30092018 = format.parse("30/09/2018");
		mon01102018 = format.parse("01/10/2018");
		return Arrays.asList(new Object[][] {     
            { allWeek, mon24092018, mon24092018 },
            { allWeek, tue25092018, tue25092018 },
            { allWeek, wed26092018, wed26092018 },
            { allWeek, thu27092018, thu27092018 },
            { allWeek, fri28092018, fri28092018 },
            { allWeek, sat29092018, sat29092018 },
            { allWeek, sun30092018, sun30092018 }, 
            
            { monToFri, sat22092018, mon24092018 },
            { monToFri, sun23092018, mon24092018 },
            { monToFri, mon24092018, mon24092018 },
            { monToFri, tue25092018, tue25092018 },
            { monToFri, wed26092018, wed26092018 },
            { monToFri, thu27092018, thu27092018 },
            { monToFri, fri28092018, fri28092018 },
            { monToFri, sat29092018, mon01102018 },
            { monToFri, sun30092018, mon01102018 },             
           
            { sunToThu, sat29092018, sun30092018 },
            { sunToThu, sun23092018, sun23092018 },
            { sunToThu, mon24092018, mon24092018 },
            { sunToThu, tue25092018, tue25092018 },
            { sunToThu, wed26092018, wed26092018 },
            { sunToThu, thu27092018, thu27092018 },
            { sunToThu, fri28092018, sun30092018 },
            { sunToThu, sat22092018, sun23092018 },
            
		});
		      
    }
       
    @Test public void testWorkingSchedule() {
    	Date actual = workingDaySchedule.getNextWorkingDate(input);
    	assertEquals(expected, actual);
    }
}
