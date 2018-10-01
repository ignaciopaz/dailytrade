package jpmorgan.dailytrade.processor;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Collection;
import java.util.Date;

import jpmorgan.dailytrade.domain.Accountable;
import jpmorgan.dailytrade.domain.ClientEntity;
import jpmorgan.dailytrade.domain.DayResults;
import jpmorgan.dailytrade.domain.InstructionEntry;
import jpmorgan.dailytrade.repository.ClientEntityRepository;
import jpmorgan.dailytrade.repository.DayResultsRepository;
import jpmorgan.dailytrade.workingdays.WorkingDateCurrency;

public class InstructionsProcessor {
	private ClientEntityRepository clientEntityRepository;
	private DayResultsRepository dayResultsRepository;
	private DateFormat dateFormat;
	private WorkingDateCurrency workingDateCurrency;
	
	public InstructionsProcessor(ClientEntityRepository clientEntityRepository,	DayResultsRepository dayResultsRepository, DateFormat dateFormat) {
		this.clientEntityRepository = clientEntityRepository;
		this.dayResultsRepository = dayResultsRepository;
		this.dateFormat = dateFormat;
		this.workingDateCurrency = new WorkingDateCurrency();
	}

	public void processInstructions(Collection<InstructionEntry> instructions) throws ParseException {
		for (InstructionEntry instruction : instructions) {
			processInstruction(instruction);
		}		
	}
	
	public void  processInstruction(InstructionEntry instruction) throws ParseException {
		Accountable day = processDate(instruction.getCurrency(), instruction.getSettlementDate());
		Accountable entity = getOrCreateClientEntity(instruction.getEntity());
		
		processAccountability(new Accountable[]{day, entity}, instruction);
		
		/* Dev note: it might needed to save the instruction somewhere, but I assume it is not necessary for the challenge as the requirements are for reports.
		i.e:
		1) update settlementDate and maybe save original settlementDate in instruction
		2) instructionDao.save(instruction);
		*/
	}

	private void processAccountability(Accountable[] accountables, InstructionEntry instruction) {
		for(Accountable accountable : accountables) {
			accountable.processAmount(instruction.getUsdAmount(), instruction.getOperation());
		}
	}

	private Accountable processDate(String currency, String settlementDateString) throws ParseException {;
		Date settlementDate = dateFormat.parse(settlementDateString);		
		settlementDate = workingDateCurrency.fixWorkingDate(settlementDate, currency);
		
		Accountable dayResults = getOrCreateDayResults(settlementDate);
		return dayResults;
	}

	private Accountable getOrCreateClientEntity(String entityName) {
		ClientEntity clientEntity = clientEntityRepository.getOne(entityName);
		if (clientEntity == null) {
			clientEntity = new ClientEntity(entityName);
			clientEntityRepository.save(clientEntity);
		}
		return clientEntity;
	}

	private Accountable getOrCreateDayResults(Date settlementDate) {
		DayResults dayResults = dayResultsRepository.getOne(settlementDate);
		if (dayResults == null) {
			dayResults = new DayResults(settlementDate);
			dayResultsRepository.save(dayResults);
		}
		return dayResults;
	}

}
