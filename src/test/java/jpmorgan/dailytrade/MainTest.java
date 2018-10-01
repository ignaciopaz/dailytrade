package jpmorgan.dailytrade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class MainTest {
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	private final PrintStream originalOut = System.out;
	private final PrintStream originalErr = System.err;

	@Before public void setUpStreams() {
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}

	@After public void restoreStreams() {
	    System.setOut(originalOut);
	    System.setErr(originalErr);
	}
	
	@Test public void reportIsPrinted() {
		Main.main(null);
		String expected = "#### Daily incoming/outgoing report ####\r\n"+
		"04 Jan 2016 - Incoming: $0.00 - Outgoing: $10,025.00\r\n"+
		"07 Jan 2016 - Incoming: $14,899.50 - Outgoing: $0.00\r\n"+
		"09 Sep 2018 - Incoming: $0.00 - Outgoing: $10,025.00\r\n"+
		"30 Sep 2018 - Incoming: $6,782.50 - Outgoing: $1.50\r\n"+
		"08 Oct 2018 - Incoming: $5,012.50 - Outgoing: $10,025.00\r\n"+
		"#### Ranking of entities based on incoming ####\r\n"+
		"bar $21,682.00\r\n"+
		"foo $5,012.50\r\n"+
		"bar1 $0.00\r\n"+
		"foo2 $0.00\r\n"+
		"#### Ranking of entities based on Outgoing ####\r\n"+
		"foo $15,037.50\r\n"+
		"bar1 $10,025.00\r\n"+
		"foo2 $5,012.50\r\n"+
		"bar $1.50\r\n";
		assertEquals(expected, outContent.toString());
	}
	
	@Test public void reportTitlesArePrinted() {
		Main.main(null);
		String result = outContent.toString();
		assertTrue(result.startsWith("#### Daily incoming/outgoing report ####"));
		assertTrue(result.contains("#### Ranking of entities based on incoming ####"));
		assertTrue(result.contains("#### Ranking of entities based on Outgoing ####"));
	}
}
