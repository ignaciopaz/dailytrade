package jpmorgan.dailytrade.repository;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jpmorgan.dailytrade.domain.ClientEntity;

public class ClientEntityRepositoryMemImpl implements ClientEntityRepository {
	/* hashmap - O(1)
	 * An alternative with more space complexity depending on expected number of entities
	 * would be to keep two TreeMaps instead of the hashmap:
	 * SortedMap<String, ClientEntity> incomingSortedEntities = new TreeMap <String, ClientEntity>(incomingComparator);
	 * SortedMap<String, ClientEntity> outgoingSortedEntities = new TreeMap <String, ClientEntity>(outgoingComparator);
	 */
	private Map<String, ClientEntity> entities = new LinkedHashMap<String, ClientEntity>();
	
	private Comparator<ClientEntity> incomingComparator = new Comparator<ClientEntity>() {
										public int compare(ClientEntity c1, ClientEntity c2) {
											return c2.getIncoming().compareTo(c1.getIncoming());
										}
		
								};
	private Comparator<ClientEntity> outgoingComparator = new Comparator<ClientEntity>() {
									public int compare(ClientEntity c1, ClientEntity c2) {
										return c2.getOutgoing().compareTo(c1.getOutgoing());
									}
	
							};
	
	public ClientEntity getOne(String name) {
		return  entities.get(name); //as an alternative could throw a notFoundException
	}

	public void save(ClientEntity clientEntity) {
		entities.put(clientEntity.getName(), clientEntity);
		/* an alternative would be:
		 * incomingSortedEntities.put(clientEntity.getName(), clientEntity);
		 * outgoingSortedEntities.put(clientEntity.getName(), clientEntity);
		*/
	}

	public List<ClientEntity> findAllSortedByIncoming() {
		//mergesort O(n log n) to O(n)
		List<ClientEntity> entityList = new LinkedList<ClientEntity>(entities.values());
		Collections.sort(entityList, incomingComparator);
		return entityList;
		//Alternative: return incomingSortedEntities.values();
	}
	
	public List<ClientEntity> findAllSortedByOutgoing() {
		//mergesort O(n log n) to O(n)
		List<ClientEntity> entityList = new LinkedList<ClientEntity>(entities.values());
		Collections.sort(entityList, outgoingComparator);
		return entityList;
		//Alternative: return outgoingSortedEntities.values();
	}
}
