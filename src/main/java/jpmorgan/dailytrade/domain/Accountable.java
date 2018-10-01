package jpmorgan.dailytrade.domain;

public interface Accountable {

	public void processAmount(Double amount, Operation operation);
}
