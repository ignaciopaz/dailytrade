package jpmorgan.dailytrade.repository;

import java.util.Collection;
import java.util.Date;

import jpmorgan.dailytrade.domain.DayResults;

public interface DayResultsRepository extends Repository<Date, DayResults> {
	public Collection<DayResults> findAllSortedByDate();
}
