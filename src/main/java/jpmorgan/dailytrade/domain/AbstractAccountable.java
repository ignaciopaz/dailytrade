package jpmorgan.dailytrade.domain;

/**
 * @author ignacio paz
 * This class might be over engineering for the logic and scope of the problem,
 * but it is just an example of refactoring in a larger scope.
 */
public class AbstractAccountable<ID> implements Accountable {	
	private ID id;
	private Double incoming; //for big amounts should be changed to BigDecimal
	private Double outgoing; //for big amounts should be changed to BigDecimal
	
	public AbstractAccountable(ID id) {
		this.id = id;
		incoming = 0D;
		outgoing = 0D;
	}

	public ID getId() {
		return id;
	}

	public void addIncomingAmount(Double amount) {
		incoming += amount;		
	}
	public void addOutgoingAmount(Double amount) {
		outgoing += amount;		
	}

	public Double getIncoming() {
		return incoming;
	}
	public Double getOutgoing() {
		return outgoing;
	}

	public void processAmount(Double amount, Operation operation) {
		if (operation.isIncoming()) {
			addIncomingAmount(amount);
		} else {
			addOutgoingAmount(amount);
		}
	}
}
