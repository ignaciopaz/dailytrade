package jpmorgan.dailytrade.domain;

import java.util.Date;

/**
 * @author ignacio paz
 * DayResults can be refactored to extend AbstractAccountable (just like ClientEntity),
 * but in this case I am keeping the simple not over engineered version but some duplicated code
 */
public class DayResults implements Comparable<DayResults>, Accountable {
	private Date date;
	private Double incoming; //for big amounts should be changed to BigDecimal
	private Double outgoing;
	
	public DayResults(Date date) {
		this.date = date;
		incoming=0D;
		outgoing=0D;
	}

	public Date getDate() {
		return date;
	}

	public void addIncomingAmount(Double amount) {
		incoming += amount;
	}

	public void addOutgoingAmount(Double amount) {
		outgoing += amount;
	}
	
	public void processAmount(Double amount, Operation operation) {
		if (operation.isIncoming()) {
			addIncomingAmount(amount);
		} else {
			addOutgoingAmount(amount);
		}
	}
	
	public Double getIncoming() {
		return incoming;
	}

	public Double getOutgoing() {
		return outgoing;
	}

	public int compareTo(DayResults o) {
		return date.compareTo(o.getDate());
	}
}
