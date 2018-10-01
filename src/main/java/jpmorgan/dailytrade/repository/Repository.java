package jpmorgan.dailytrade.repository;

public interface Repository<ID, T> {
	public T getOne(ID id);
	public void save(T t);
}
