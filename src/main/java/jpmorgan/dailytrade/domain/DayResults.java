package jpmorgan.dailytrade.domain;

import java.util.Date;

/**
 * @author ignacio paz
 */
public class DayResults extends AbstractAccountable<Date> implements Comparable<DayResults> {
	
	public DayResults(Date date) {
		super(date);
	}

	public Date getDate() {
		return getId();
	}

	public int compareTo(DayResults o) {
		return getDate().compareTo(o.getDate());
	}
}
