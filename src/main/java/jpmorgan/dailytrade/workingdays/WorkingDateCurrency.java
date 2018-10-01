package jpmorgan.dailytrade.workingdays;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ignacio paz
 * Dev note: the currencies and more working schedule could be parametrized and configurable but for the scope of
 * the problem this is enough. It can evolve refactored if there are new requirements in the future.
 * A unit test should be added with new requirement should be added before implementing such features.
 */
public class WorkingDateCurrency {
	private WorkingDaySchedule sunToThu = new WorkingDaySchedule(Calendar.SUNDAY, Calendar.THURSDAY);
	private WorkingDaySchedule monToFri = new WorkingDaySchedule(Calendar.MONDAY, Calendar.FRIDAY);
	private Map<String, WorkingDaySchedule> currenciesWorkingDaySchedule = new HashMap<String, WorkingDaySchedule>();
	
	public WorkingDateCurrency() {
		currenciesWorkingDaySchedule.put("SAR", sunToThu);
		currenciesWorkingDaySchedule.put("AED", sunToThu);
	}
	
	public Date fixWorkingDate(Date settlementDate, String currency) {
		WorkingDaySchedule schedule = getWorkingDateSchedule(currency);
		return schedule.getNextWorkingDate(settlementDate);
	}

	private WorkingDaySchedule getWorkingDateSchedule(String currency) {
		if (currenciesWorkingDaySchedule.containsKey(currency)) {
			return currenciesWorkingDaySchedule.get(currency);
		}
		return monToFri;		
	}
}
