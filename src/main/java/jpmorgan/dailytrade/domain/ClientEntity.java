package jpmorgan.dailytrade.domain;

/**
 * @author ignacio paz
 * extending from AbstractAccountable might be over engineering for the logic and scope,
 * but it is just an example of refactoring
 */
public class ClientEntity extends AbstractAccountable<String> {

	public ClientEntity(String id) {
		super(id);
	}
	public String getName() {
		return getId();
	}
	
}
