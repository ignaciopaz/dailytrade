package jpmorgan.dailytrade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jpmorgan.dailytrade.domain.InstructionEntry;

/**
 * @author ignacio paz
 * TODO: output template in test should be improved and refactored to avoid repetitive code
 */
public class DailyTradeAppTest {
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	private final PrintStream originalErr = System.err;
	private DailyTradeApp app;
	private LinkedList<InstructionEntry> instructions;

	@Before public void setUpStreams() {
		app= new DailyTradeApp();
		instructions = new LinkedList<InstructionEntry>();
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}

	@After public void restoreStreams() {
	    System.setOut(originalOut);
	    System.setErr(originalErr);
	}
	
	@Test public void reportIsPrinted() {
		instructions.add(new InstructionEntry("foo", "B", 0.5, "SGP", "01 Jan 2016", "24 Sep 2018", 200, 10.0));
		instructions.add(new InstructionEntry("bar", "S", 1.0, "AED", "05 Jan 2016", "22 Sep 2018", 200, 10.0));
		app.generateReports(instructions);
		String expected = "#### Daily incoming/outgoing report ####\r\n"+
		"23 Sep 2018 - Incoming: $2,000.00 - Outgoing: $0.00\r\n"+
		"24 Sep 2018 - Incoming: $0.00 - Outgoing: $1,000.00\r\n"+
		"#### Ranking of entities based on incoming ####\r\n"+
		"bar $2,000.00\r\n"+
		"foo $0.00\r\n"+
		"#### Ranking of entities based on Outgoing ####\r\n"+
		"foo $1,000.00\r\n"+
		"bar $0.00\r\n";
		assertEquals(expected, outContent.toString());
	}
	
	@Test public void longInstructionsBigAmountsArePrinted() {
		long lines = 10000;
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance(Locale.US);
		int multiplier = 10000;
		for (long i=0 ; i<lines;i++) {
			instructions.add(new InstructionEntry("foo", "B", 0.5, "SGP", "01 Jan 2016", "24 Sep 2018", 2*multiplier, 1.0*multiplier));
			instructions.add(new InstructionEntry("bar", "S", 1.0, "AED", "05 Jan 2016", "22 Sep 2018", 2*multiplier, 1.0*multiplier));
		}
		String value1 = numberFormat.format(1*multiplier*multiplier*lines);
		String value2 = numberFormat.format(2*multiplier*multiplier*lines);
		
		app.generateReports(instructions);
		String expected = "#### Daily incoming/outgoing report ####\r\n"+
		"23 Sep 2018 - Incoming: "+value2+" - Outgoing: $0.00\r\n"+
		"24 Sep 2018 - Incoming: $0.00 - Outgoing: "+value1+"\r\n"+
		"#### Ranking of entities based on incoming ####\r\n"+
		"bar "+value2+"\r\n"+
		"foo $0.00\r\n"+
		"#### Ranking of entities based on Outgoing ####\r\n"+
		"foo "+value1+"\r\n"+
		"bar $0.00\r\n";
		assertEquals(expected, outContent.toString());
	}
	
	@Test public void wrongDateShouldCatchParseException() {
		instructions.add(new InstructionEntry("foo", "B", 0.5, "SGP", "01 Jan 2016", "24 Sep 2018", 200, 10.0));
		instructions.add(new InstructionEntry("bar", "S", 1.0, "AED", "05 Jan 2016", "WRONG DATE", 200, 10.0));
		app.generateReports(instructions);
		String expected = "Instructions have Errors.\r\n";
		assertEquals(expected, outContent.toString());
		assertTrue(errContent.toString().contains("java.text.ParseException"));
	}
}
