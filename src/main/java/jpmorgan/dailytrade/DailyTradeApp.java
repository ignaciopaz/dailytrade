package jpmorgan.dailytrade;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Locale;

import jpmorgan.dailytrade.domain.InstructionEntry;
import jpmorgan.dailytrade.processor.InstructionsProcessor;
import jpmorgan.dailytrade.report.MarketReports;
import jpmorgan.dailytrade.repository.ClientEntityRepository;
import jpmorgan.dailytrade.repository.ClientEntityRepositoryMemImpl;
import jpmorgan.dailytrade.repository.DayResultsRepository;
import jpmorgan.dailytrade.repository.DayResultsRepositoryMemImpl;

public class DailyTradeApp {
	
	private DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
	
	public void generateReports(Collection<InstructionEntry> instructions) {
		
		ClientEntityRepository clientEntityRepository = new ClientEntityRepositoryMemImpl();
		DayResultsRepository dayResultsRepository = new DayResultsRepositoryMemImpl();
		//injecting depencencies here
		InstructionsProcessor processor = new InstructionsProcessor(clientEntityRepository, dayResultsRepository, dateFormat);
		
		try {
			processor.processInstructions(instructions);
			MarketReports reports = new MarketReports(clientEntityRepository, dayResultsRepository, dateFormat);
			reports.printDailyReport();
			reports.printIncomingEntiyRanking();
			reports.printOutgoingEntiyRanking();
		} catch (ParseException e) {
			System.out.println("Instructions have Errors.");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("There was an exception.");
			e.printStackTrace();
		}		
		
	}
}
