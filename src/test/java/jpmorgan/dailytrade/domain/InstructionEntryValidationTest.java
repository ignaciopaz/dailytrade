package jpmorgan.dailytrade.domain;

import org.junit.Test;

public class InstructionEntryValidationTest {
    
    @Test(expected=IllegalArgumentException.class) public void wrongOperationNotAllowed() {
    	InstructionEntry entry = new InstructionEntry("bar", "X", 0.5, "SGP", "01 Jan 2016", "07 Jan 2018", 40, 20.0);
	}
    @Test(expected=IllegalArgumentException.class) public void lowerCaseOperationNotAllowed() {
    	InstructionEntry entry = new InstructionEntry("bar", "s", 0.5, "SGP", "01 Jan 2016", "07 Jan 2018", 40, 20.0);
	}
    @Test(expected=IllegalArgumentException.class) public void emptyOperationNotAllowed() {
    	InstructionEntry entry = new InstructionEntry("bar", "", 0.5, "SGP", "01 Jan 2016", "07 Jan 2018", 40, 20.0);
	}
    @Test(expected=IllegalArgumentException.class) public void nullOperationNotAllowed() {
    	InstructionEntry entry = new InstructionEntry("bar", null, 0.5, "SGP", "01 Jan 2016", "07 Jan 2018", 40, 20.0);
	}
    @Test(expected=IllegalArgumentException.class) public void nullEntityNotAllowed() {
    	InstructionEntry entry = new InstructionEntry(null, "S", 0.5, "SGP", "01 Jan 2016", "07 Jan 2018", 40, 20.0);
	}
    @Test(expected=IllegalArgumentException.class) public void emptyEntityNotAllowed() {
    	InstructionEntry entry = new InstructionEntry("", "S", 0.5, "SGP", "01 Jan 2016", "07 Jan 2018", 40, 20.0);
	}
    
    @Test(expected=IllegalArgumentException.class) public void nullAgreedFxNotAllowed() {
    	InstructionEntry entry = new InstructionEntry("entity", "S", null, "SGP", "01 Jan 2016", "07 Jan 2018", 40, 20.0);
	}
    //I am assuming negative values are not allowed
    @Test(expected=IllegalArgumentException.class) public void negativeAgreedFxNotAllowed() {
    	InstructionEntry entry = new InstructionEntry("entity", "S", -1.0, "SGP", "01 Jan 2016", "07 Jan 2018", 40, 20.0);
	}
    @Test(expected=IllegalArgumentException.class) public void nullUnitsNotAllowed() {
    	InstructionEntry entry = new InstructionEntry("entity", "S", 1.0, "SGP", "01 Jan 2016", "07 Jan 2018", null, 20.0);
	}
    @Test(expected=IllegalArgumentException.class) public void zeronitsNotAllowed() {
    	InstructionEntry entry = new InstructionEntry("entity", "S", 1.0, "SGP", "01 Jan 2016", "07 Jan 2018", 0, 20.0);
	}
    @Test(expected=IllegalArgumentException.class) public void negativeUnitsNotAllowed() {
    	InstructionEntry entry = new InstructionEntry("entity", "S", 1.0, "SGP", "01 Jan 2016", "07 Jan 2018", -40, 20.0);
	}
    @Test(expected=IllegalArgumentException.class) public void nullPricePerUnitNotAllowed() {
    	InstructionEntry entry = new InstructionEntry("entity", "S", 1.0, "SGP", "01 Jan 2016", "07 Jan 2018", 2, null);
	}
    @Test(expected=IllegalArgumentException.class) public void negativePricePerUnitNotAllowed() {
    	InstructionEntry entry = new InstructionEntry("entity", "S", 1.0, "SGP", "01 Jan 2016", "07 Jan 2018", 40, -20.0);
	}
  
}
