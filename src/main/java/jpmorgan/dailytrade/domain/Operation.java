package jpmorgan.dailytrade.domain;

public enum Operation {
	B, S;
	
	public boolean isIncoming() {
		return Operation.S == this;
	}
}
