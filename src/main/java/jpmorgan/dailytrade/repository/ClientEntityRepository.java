package jpmorgan.dailytrade.repository;

import java.util.List;

import jpmorgan.dailytrade.domain.ClientEntity;

public interface ClientEntityRepository extends Repository<String, ClientEntity> {

	public List<ClientEntity> findAllSortedByIncoming();
	
	public List<ClientEntity> findAllSortedByOutgoing();
}
