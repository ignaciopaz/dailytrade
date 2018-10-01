package jpmorgan.dailytrade.processor;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import jpmorgan.dailytrade.domain.ClientEntity;
import jpmorgan.dailytrade.domain.DayResults;
import jpmorgan.dailytrade.domain.InstructionEntry;
import jpmorgan.dailytrade.repository.ClientEntityRepositoryMemImpl;
import jpmorgan.dailytrade.repository.DayResultsRepositoryMemImpl;

public class InstructionsProcessorTest {
	
	private static DateFormat dateFormat;
	
	private ClientEntityRepositoryMemImpl clientEntityRepository;
	private DayResultsRepositoryMemImpl dayResultsRepository;
	private InstructionsProcessor processor;

	private List<InstructionEntry> instructions;
	
	@BeforeClass public static void beforeClass() {
		dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.US);
	}
	
	@Before public void before() {
		clientEntityRepository = new ClientEntityRepositoryMemImpl();
		dayResultsRepository = new DayResultsRepositoryMemImpl();
		processor = new InstructionsProcessor(clientEntityRepository, dayResultsRepository, dateFormat);
		instructions = new ArrayList<InstructionEntry>();
	}
	
	@Test public void clientBuyOnlyInstruction() throws ParseException {
		InstructionEntry instruction = new InstructionEntry("foo", "B", 0.5, "SGP", "01 Jan 2016", "02 Jan 2016", 200, 10.0);
		processor.processInstruction(instruction);
		ClientEntity clientEntity = clientEntityRepository.getOne("foo");
		assertEquals(Double.valueOf(0), clientEntity.getIncoming());
		assertEquals(Double.valueOf(1000), clientEntity.getOutgoing());
	}
	
	@Test public void clientBuySellInstructions() throws ParseException {
		instructions.add(new InstructionEntry("foo", "S", 0.5, "SGP", "01 Jan 2016", "06 Oct 2018", 30, 10.0));
		instructions.add(new InstructionEntry("foo", "B", 0.5, "SGP", "01 Jan 2016", "07 Oct 2018", 40, 10.0));
		processor.processInstructions(instructions);
		ClientEntity clientEntity = clientEntityRepository.getOne("foo");
		assertEquals(Double.valueOf(150), clientEntity.getIncoming());
		assertEquals(Double.valueOf(200), clientEntity.getOutgoing());
	}
	
	@Test public void clientMultipleBuySellInstructions() throws ParseException {
		addClientInstruction("foo", "S", 0.5, 30, 10.0);
		addClientInstruction("foo", "B", 0.5, 40, 10.0);
		addClientInstruction("foo", "B", 1.5, 40, 1.1);
		addClientInstruction("foo", "S", 2.2, 10, 10.0);
		addClientInstruction("foo", "B", 0.5, 3, 1.4);
		processor.processInstructions(instructions);
		ClientEntity clientEntity = clientEntityRepository.getOne("foo");
		assertEquals(Double.valueOf(370), clientEntity.getIncoming());
		assertEquals(Double.valueOf(268.1), clientEntity.getOutgoing());
	}
	
	@Test public void clientMultipleBuySellInstructionsMultipleClients() throws ParseException {
		addClientInstruction("bar", "S", 0.5, 30, 10.0);
		addClientInstruction("bar", "B", 0.5, 40, 20.0);
		addClientInstruction("bar", "B", 1.5, 40, 1.1);
		addClientInstruction("foo", "B", 0.5, 40, 10.0);
		addClientInstruction("foo", "B", 1.5, 40, 1.1);
		addClientInstruction("bar", "S", 2.2, 10, 10.0);
		addClientInstruction("bar", "B", 0.5, 3, 1.4);
		processor.processInstructions(instructions);
		ClientEntity clientEntity = clientEntityRepository.getOne("bar");
		assertEquals(Double.valueOf(370), clientEntity.getIncoming());
		assertEquals(Double.valueOf(468.1), clientEntity.getOutgoing());
	}
	
	@Test public void clientOutgoingRankingIsValid() throws ParseException {
		loadSampleBuyClientInstructions();
		processor.processInstructions(instructions);
		List<ClientEntity> entities = clientEntityRepository.findAllSortedByOutgoing();
		for (int i=0; i < 10; i++) {
			assertEquals("B"+i, entities.get(i).getName());
		}
	}
	
	@Test public void clientIncomingRankingIsValid() throws ParseException {
		loadSampleSellClientInstructions();
		processor.processInstructions(instructions);
		List<ClientEntity> entities = clientEntityRepository.findAllSortedByIncoming();
		for (int i=0; i < 10; i++) {
			assertEquals("S"+i, entities.get(i).getName());
		}
	}
	
	@Test(expected=ParseException.class) public void wrongSettlementDateFormatNotAllowed() throws ParseException {
		instructions.add(new InstructionEntry("bar", "B", 0.5, "SGP", "01 Jan 2016", "07 01 2018", 40, 20.0));
		processor.processInstructions(instructions);
	}
	
	@Test public void workingDayMonToFriSumOutgoing() throws ParseException {
		instructions.add(new InstructionEntry("bar", "B", 0.5, "SGP", "01 Jan 2016", "28 Sep 2018", 40, 20.0));
		instructions.add(new InstructionEntry("bar", "B", 0.5, "SGP", "01 Jan 2016", "28 Sep 2018", 40, 20.0));
		processor.processInstructions(instructions);
		DayResults dayResults = dayResultsRepository.getOne(dateFormat.parse("28 Sep 2018"));
		assertEquals(Double.valueOf(800.0), dayResults.getOutgoing());
		assertEquals(Double.valueOf(0.0), dayResults.getIncoming());
	}
	@Test public void workingDayMonToFriSumIncoming() throws ParseException {
		instructions.add(new InstructionEntry("bar", "S", 0.5, "SGP", "01 Jan 2016", "28 Sep 2018", 40, 20.0));
		instructions.add(new InstructionEntry("bar", "S", 0.5, "SGP", "01 Jan 2016", "28 Sep 2018", 10, 20.0));
		processor.processInstructions(instructions);
		DayResults dayResults = dayResultsRepository.getOne(dateFormat.parse("28 Sep 2018"));
		assertEquals(Double.valueOf(0.0), dayResults.getOutgoing());
		assertEquals(Double.valueOf(500.0), dayResults.getIncoming());
	}
	
	@Test public void nonWorkingDayMonToFriSumIsValid() throws ParseException {
		instructions.add(new InstructionEntry("bar", "B", 0.5, "SGP", "01 Jan 2016", "27 Sep 2018", 40000, 20.0));
		instructions.add(new InstructionEntry("bar", "S", 0.5, "SGP", "01 Jan 2016", "27 Sep 2018", 40000, 20.0));
		instructions.add(new InstructionEntry("bar", "B", 0.5, "SGP", "01 Jan 2016", "29 Sep 2018", 40, 20.0));
		instructions.add(new InstructionEntry("bar", "B", 0.5, "SGP", "01 Jan 2016", "30 Sep 2018", 40, 20.0));
		instructions.add(new InstructionEntry("bar", "B", 1.0, "SGP", "01 Jan 2016", "01 Oct 2018", 1, 1.0));
		instructions.add(new InstructionEntry("bar", "S", 0.5, "SGP", "01 Jan 2016", "29 Sep 2018", 40, 20.0));
		instructions.add(new InstructionEntry("bar", "S", 0.5, "SGP", "01 Jan 2016", "30 Sep 2018", 10, 20.0));
		instructions.add(new InstructionEntry("bar", "S", 1.0, "SGP", "01 Jan 2016", "01 Oct 2018", 1, 1.0));
		processor.processInstructions(instructions);
		DayResults dayResults = dayResultsRepository.getOne(dateFormat.parse("01 Oct 2018"));
		assertEquals(Double.valueOf(801.0), dayResults.getOutgoing());
		assertEquals(Double.valueOf(501.0), dayResults.getIncoming());
	}
	
	@Test public void nonWorkingDaySunToThuSumIsValid() throws ParseException {
		instructions.add(new InstructionEntry("bar", "B", 0.5, "SAR", "01 Jan 2016", "27 Sep 2018", 40000, 20.0));
		instructions.add(new InstructionEntry("bar", "S", 0.5, "AED", "01 Jan 2016", "27 Sep 2018", 40000, 20.0));
		instructions.add(new InstructionEntry("bar", "B", 0.5, "AED", "01 Jan 2016", "29 Sep 2018", 40, 20.0));
		instructions.add(new InstructionEntry("bar", "B", 1.0, "SAR", "01 Jan 2016", "30 Sep 2018", 4, 1.0));
		instructions.add(new InstructionEntry("bar", "B", 0.5, "SAR", "01 Jan 2016", "30 Sep 2018", 40, 20.0));
		instructions.add(new InstructionEntry("bar", "B", 1.0, "SAR", "01 Jan 2016", "01 Oct 2018", 2, 1.0));
		instructions.add(new InstructionEntry("bar", "S", 0.5, "SAR", "01 Jan 2016", "29 Sep 2018", 40, 20.0));
		instructions.add(new InstructionEntry("bar", "S", 0.5, "AED", "01 Jan 2016", "30 Sep 2018", 10, 20.0));
		instructions.add(new InstructionEntry("bar", "S", 1.0, "SAR", "01 Jan 2016", "30 Sep 2018", 4, 1.0));
		instructions.add(new InstructionEntry("bar", "S", 1.0, "SAR", "01 Jan 2016", "01 Oct 2018", 2, 1.0));
		processor.processInstructions(instructions);
		DayResults dayResults = dayResultsRepository.getOne(dateFormat.parse("30 Sep 2018"));
		assertEquals(Double.valueOf(804.0), dayResults.getOutgoing());
		assertEquals(Double.valueOf(504.0), dayResults.getIncoming());
	}
	
	@Test public void mixedCurrenciesDaySumIsValid() throws ParseException {
		instructions.add(new InstructionEntry("bar", "B", 0.5, "SGP", "01 Jan 2016", "27 Sep 2018", 40000, 20.0));
		instructions.add(new InstructionEntry("bar", "S", 0.5, "SGP", "01 Jan 2016", "27 Sep 2018", 40000, 20.0));
		instructions.add(new InstructionEntry("bar", "B", 0.5, "SGP", "01 Jan 2016", "29 Sep 2018", 40, 20.0));
		instructions.add(new InstructionEntry("bar", "B", 0.5, "SGP", "01 Jan 2016", "30 Sep 2018", 40, 20.0));
		instructions.add(new InstructionEntry("bar", "B", 1.0, "SGP", "01 Jan 2016", "01 Oct 2018", 3, 1.0));
		instructions.add(new InstructionEntry("bar", "S", 0.5, "SGP", "01 Jan 2016", "29 Sep 2018", 40, 20.0));
		instructions.add(new InstructionEntry("bar", "S", 0.5, "SGP", "01 Jan 2016", "30 Sep 2018", 10, 20.0));
		instructions.add(new InstructionEntry("bar", "S", 1.0, "SGP", "01 Jan 2016", "01 Oct 2018", 2, 1.0));
		
		instructions.add(new InstructionEntry("bar", "B", 0.5, "SAR", "01 Jan 2016", "27 Sep 2018", 40000, 20.0));
		instructions.add(new InstructionEntry("bar", "S", 0.5, "AED", "01 Jan 2016", "27 Sep 2018", 40000, 20.0));
		instructions.add(new InstructionEntry("bar", "B", 0.5, "SAR", "01 Jan 2016", "29 Sep 2018", 40, 20.0));
		processor.processInstructions(instructions);
		DayResults dayResults = dayResultsRepository.getOne(dateFormat.parse("01 Oct 2018"));
		assertEquals(Double.valueOf(803.0), dayResults.getOutgoing());
		assertEquals(Double.valueOf(502.0), dayResults.getIncoming());
	}
	
	@Test public void dayIncomingSortIsValid() throws ParseException {
		instructions.add(new InstructionEntry("bar", "B", 1.0, "SAR", "01 Jan 2016", "21 Sep 2018", 1, 1.0));
		instructions.add(new InstructionEntry("bar", "B", 1.0, "AED", "01 Jan 2016", "21 Sep 2018", 1, 1.0));
		instructions.add(new InstructionEntry("bar", "B", 1.0, "AED", "01 Jan 2016", "22 Sep 2018", 1, 1.0));
		instructions.add(new InstructionEntry("bar", "B", 1.0, "JPY", "01 Jan 2016", "22 Sep 2018", 1, 1.0));
		instructions.add(new InstructionEntry("bar", "B", 1.0, "SGP", "01 Jan 2016", "24 Sep 2018", 1, 1.0));
		instructions.add(new InstructionEntry("bar", "B", 1.0, "SGP", "01 Jan 2016", "25 Sep 2018", 2, 1.0));
		instructions.add(new InstructionEntry("bar", "B", 1.0, "SGP", "01 Jan 2016", "26 Sep 2018", 3, 1.0));
		instructions.add(new InstructionEntry("bar", "B", 1.0, "SGP", "01 Jan 2016", "27 Sep 2018", 4, 1.0));
		instructions.add(new InstructionEntry("bar", "B", 1.0, "SGP", "01 Jan 2016", "28 Sep 2018", 5, 1.0));
		instructions.add(new InstructionEntry("bar", "B", 1.0, "SGP", "01 Jan 2016", "29 Sep 2018", 6, 1.0));
		instructions.add(new InstructionEntry("bar", "B", 1.0, "SGP", "01 Jan 2016", "30 Sep 2018", 7, 1.0));
		instructions.add(new InstructionEntry("bar", "B", 1.0, "AED", "01 Jan 2016", "29 Sep 2018", 6, 1.0));
		instructions.add(new InstructionEntry("bar", "B", 1.0, "SAR", "01 Jan 2016", "30 Sep 2018", 1, 1.0));
		processor.processInstructions(instructions);
		List<DayResults> daysResults = new LinkedList<DayResults>(dayResultsRepository.findAllSortedByDate());
		int i=0;
		assertEquals(dateFormat.parse("23 Sep 2018"), daysResults.get(i++).getDate());
		assertEquals(dateFormat.parse("24 Sep 2018"), daysResults.get(i++).getDate());
		assertEquals(dateFormat.parse("25 Sep 2018"), daysResults.get(i++).getDate());
		assertEquals(dateFormat.parse("26 Sep 2018"), daysResults.get(i++).getDate());
		assertEquals(dateFormat.parse("27 Sep 2018"), daysResults.get(i++).getDate());
		assertEquals(dateFormat.parse("28 Sep 2018"), daysResults.get(i++).getDate());
		assertEquals(dateFormat.parse("30 Sep 2018"), daysResults.get(i++).getDate());
		assertEquals(dateFormat.parse("01 Oct 2018"), daysResults.get(i++).getDate());
		assertEquals(i, daysResults.size());
	}
	
	//TODO: Should check if dates are less or equal than today?
	
	private void addClientInstruction(String clientName, String operation, Double agreedFx, Integer units, Double pricePerUnit) {
		instructions.add(new InstructionEntry(clientName, operation, agreedFx, "SGP", "01 Jan 2016", "07 Oct 2018", units, pricePerUnit));
	}
	
	private void loadSampleBuyClientInstructions() {
		for (int i = 0; i < 10; i++) {
			for (int j = i+1; j <= 10; j++) {
				addClientInstruction("B"+i, "B", 0.5*j, 40*(1+1/j), 20.0);
			}
		}	
	}
	private void loadSampleSellClientInstructions() {
		for (int i = 0; i < 10; i++) {
			for (int j = i+1; j <= 10; j++) {
				addClientInstruction("S"+i, "S", 0.2*(1+j/10), 40*(1+j/2), 0.1*j);
			}
		}	
	}
}
