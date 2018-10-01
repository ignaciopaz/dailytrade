package jpmorgan.dailytrade.workingdays;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class WorkingDaySchedule {

	private int dayTo;
	private int dayFrom;

	public WorkingDaySchedule(int dayFrom, int dayTo) {
		this.dayFrom = dayFrom;
		this.dayTo = dayTo;
	}

	public Date getNextWorkingDate(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		int weekDay = cal.get(Calendar.DAY_OF_WEEK);
		if (weekDay >= dayFrom && weekDay <= dayTo) {
			return date;
		}
		if (weekDay < dayFrom) {
			cal.add(Calendar.DATE, dayFrom - weekDay);
		} else if (weekDay > dayTo) {
			cal.add(Calendar.DATE, 3-weekDay+dayTo);
		}
		
		return cal.getTime();
	}

}
