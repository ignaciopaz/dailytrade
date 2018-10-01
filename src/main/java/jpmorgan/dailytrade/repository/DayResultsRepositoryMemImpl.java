package jpmorgan.dailytrade.repository;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import jpmorgan.dailytrade.domain.DayResults;

public class DayResultsRepositoryMemImpl implements DayResultsRepository {
	/* An alternative would be to use a Treemap (red-black tree) with natural order by ascending date
	 * put and get methods are O(log n). In that case dates will be sorted when added to the tree.
	 * //private SortedMap<Date, DayResults> entities = new TreeMap<Date, DayResults>();
	 * Assuming a large list of dates, I prefer to use a simple and not sorted HashMap with get and put O(1) and sort
	 * in the findAllSortedByDate() method using Collections.sort which is mergesort - O(n log n) to O(n)
	 * It would be interesting to know if the instructions come unsorted, mostly sorted by ascending or descending date.
	 */	
	private Map<Date, DayResults> entities = new LinkedHashMap<Date, DayResults>();
	
	public DayResults getOne(Date settlementDate) {
		return  entities.get(settlementDate); //Alternative: if not exists, could throw a notFoundException
	}

	public void save(DayResults day) {
		entities.put(day.getDate(), day);
	}

	public Collection<DayResults> findAllSortedByDate() {
		List<DayResults> result = new LinkedList<DayResults>(entities.values());
		Collections.sort(result);
		return result;
		//If a treemap was used, this would be the only line for this method:
		// return entities.values();
		
	}
}
