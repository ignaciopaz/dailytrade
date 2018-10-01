package jpmorgan.dailytrade.domain;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
@RunWith(Parameterized.class)
public class InstructionEntryAmountTest {
	private Double agreedFx;	
	private Integer units;
	private Double pricePerUnit;
	private Double expected;
	
	public InstructionEntryAmountTest(Double agreedFx, Integer units, Double pricePerUnit, Double expected) {
		super();
		this.agreedFx = agreedFx;
		this.units = units;
		this.pricePerUnit = pricePerUnit;
		this.expected = expected;
	}
	
	@Parameters
    public static Collection<Object[]> data() throws ParseException {
    	
		return Arrays.asList(new Object[][] {     
            { 1.0, 1, 1.0, 1.0},
            { 1.0, 1, 2.0, 2.0},
            { 1.0, 2, 1.0, 2.0},
            { 2.0, 1, 1.0, 2.0},
            { 0.5, 1, 1.0, 0.5},
            { 0.5, 1, 0.5, 0.25},
            { 0.5, 2, 0.5, 0.5},
            { 0.5, 20, 0.5, 5.0},
            { 1.5, 20, 0.5, 15.0},
            
		});
		      
    }
    
    @Test public void testInstruction() {
		InstructionEntry entry = new InstructionEntry("foo", "B", agreedFx, "SGP", "01 Jan 2016", "02 Jan 2016", units, pricePerUnit);
		assertEquals(expected, entry.getUsdAmount());
	}
  
}
