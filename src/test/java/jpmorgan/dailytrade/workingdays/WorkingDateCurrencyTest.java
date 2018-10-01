package jpmorgan.dailytrade.workingdays;
import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class WorkingDateCurrencyTest {
	
	private static WorkingDateCurrency workingDateCurrency;
	
	private String currency;
	private Date input;
	private Date expected;

	public WorkingDateCurrencyTest (String currency, Date input, Date expected) {
		this.currency = currency;
		this.input = input;
		this.expected = expected;
	}
	
	@BeforeClass public static void beforeClass() {
		workingDateCurrency = new WorkingDateCurrency();
	}
	
	@Parameters
    public static Collection<Object[]> data() throws ParseException {
    	DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    	Date sat22092018, sun23092018;
    	Date mon24092018, tue25092018, wed26092018, thu27092018, fri28092018, sat29092018, sun30092018, mon01102018;
    	
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
                       
            { "SGP", sat22092018, mon24092018 },
            { "SGP", sun23092018, mon24092018 },
            { "SGP", mon24092018, mon24092018 },
            { "SGP", tue25092018, tue25092018 },
            { "SGP", wed26092018, wed26092018 },
            { "SGP", thu27092018, thu27092018 },
            { "SGP", fri28092018, fri28092018 },
            { "SGP", sat29092018, mon01102018 },
            { "SGP", sun30092018, mon01102018 },             
           
            { "SAR", sat29092018, sun30092018 },
            { "SAR", sun23092018, sun23092018 },
            { "SAR", mon24092018, mon24092018 },
            { "SAR", tue25092018, tue25092018 },
            { "SAR", wed26092018, wed26092018 },
            { "SAR", thu27092018, thu27092018 },
            { "SAR", fri28092018, sun30092018 },
            { "SAR", sat22092018, sun23092018 },
            
            { "AED", sat29092018, sun30092018 },
            { "AED", sun23092018, sun23092018 },
            { "AED", mon24092018, mon24092018 },
            { "AED", tue25092018, tue25092018 },
            { "AED", wed26092018, wed26092018 },
            { "AED", thu27092018, thu27092018 },
            { "AED", fri28092018, sun30092018 },
            { "AED", sat22092018, sun23092018 },
            
		});
		      
    }
       
    @Test public void testWorkingDayCurrency() {
    	Date actual = workingDateCurrency.fixWorkingDate(input, currency);
    	assertEquals(expected, actual);
    }
}
