package jpmorgan.dailytrade.domain;

public class InstructionEntry {
	private String entity;
	private Operation operation;
	private Double agreedFx;
	private String currency;
	private String instructionDate; //not used
	private String settlementDate;
	private Integer units;
	private Double pricePerUnit;

	public InstructionEntry(String entity, String operation, Double agreedFx, String currency,
			String instructionDate, String settlementDate, Integer units, Double pricePerUnit) {
		super();
		if (operation == null) {
			throw new IllegalArgumentException("operation cannot be null");
		}
		if (entity == null || entity.isEmpty()) {
			throw new IllegalArgumentException("entity cannot be empty or null");
		}
		if (agreedFx == null || agreedFx < 0 || units == null || units <= 0 || pricePerUnit == null || pricePerUnit < 0) {
			//just generic message. could specify per each argument.
			throw new IllegalArgumentException("arguments cannot be empty, null or negative");
		}
		this.entity = entity;
		this.operation = Operation.valueOf(operation);
		this.agreedFx = agreedFx;
		this.currency = currency;
		this.instructionDate = instructionDate;
		this.settlementDate = settlementDate;
		this.units = units;
		this.pricePerUnit = pricePerUnit;
	}

	public Double getUsdAmount() {
		return pricePerUnit * units * agreedFx;
	}

	public String getEntity() {
		return entity;
	}

	public String getSettlementDate() {
		return settlementDate;
	}

	public String getCurrency() {
		return currency;
	}

	public Operation getOperation() {
		return operation;
	}
}
