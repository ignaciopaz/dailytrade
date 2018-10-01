package jpmorgan.dailytrade;

import java.util.Collection;
import java.util.LinkedList;

import jpmorgan.dailytrade.domain.InstructionEntry;

/**
 * @author ignacio paz
 * This class represents a client program calling the DailyTradeApp with the desired data to process.
 * Below is some mock data.
 */
public class Main {

	public static void main(String args[]) {
		Collection<InstructionEntry> instructions = loadInstructions();
		DailyTradeApp app = new DailyTradeApp();
		app.generateReports(instructions);
	}

	/**
	 * This method would read the data from the source (CSV, text file) or data that is received to a service.
	 * If the data is too big or it cannot be read all together, it may be fine to call
	 * InstructorProcessor.processInstruction(InstructionEntry instruction) for each line read from file or received
	 * Assumption: To simplify, I just provide a collection of instructions with data to process as a batch.
	 */
	private static Collection<InstructionEntry> loadInstructions() {
		Collection<InstructionEntry> instructions = new LinkedList<InstructionEntry>();
		instructions.add(new InstructionEntry("foo", "B", 0.5, "SGP", "01 Jan 2016", "02 Jan 2016", 200, 100.25));
		instructions.add(new InstructionEntry("bar", "S", 0.22, "AED", "05 Jan 2016", "07 Jan 2016", 450, 150.5));
		instructions.add(new InstructionEntry("bar", "S", 0.1, "AED", "28 Sep 2018", "28 Sep 2018", 450, 150.5));
		instructions.add(new InstructionEntry("bar", "B", 1.0, "AED", "28 Sep 2018", "28 Sep 2018", 1, 1.5));
		instructions.add(new InstructionEntry("bar", "S", 1.0, "AED", "28 Sep 2018", "29 Sep 2018", 10, 1.0));
		instructions.add(new InstructionEntry("foo", "S", 0.5, "SGP", "01 Jan 2016", "06 Oct 2018", 100, 100.25));
		instructions.add(new InstructionEntry("foo", "B", 0.5, "SGP", "01 Jan 2016", "07 Oct 2018", 100, 100.25));
		instructions.add(new InstructionEntry("bar1", "B", 0.5, "SAR", "08 Sep 2018", "08 Sep 2018", 100, 100.25));
		instructions.add(new InstructionEntry("bar1", "B", 0.5, "SAR", "08 Sep 2018", "09 Sep 2018", 100, 100.25));
		instructions.add(new InstructionEntry("foo2", "B", 0.5, "SGP", "01 Jan 2016", "07 Oct 2018", 100, 100.25));	
		return instructions;
	}
	
	
}
