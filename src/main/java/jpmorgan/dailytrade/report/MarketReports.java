package jpmorgan.dailytrade.report;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import jpmorgan.dailytrade.domain.ClientEntity;
import jpmorgan.dailytrade.domain.DayResults;
import jpmorgan.dailytrade.repository.ClientEntityRepository;
import jpmorgan.dailytrade.repository.DayResultsRepository;

public class MarketReports {
	private ClientEntityRepository clientEntityRepository;
	private DayResultsRepository dayResultsRepository;
	private DateFormat dateFormat;
	//this formatter can be refactored to be configurable or injected
	private NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.US);	

	public MarketReports(ClientEntityRepository clientEntityRepository, DayResultsRepository dayResultsRepository, DateFormat dateFormat) {
		this.clientEntityRepository = clientEntityRepository;
		this.dayResultsRepository = dayResultsRepository;
		this.dateFormat = dateFormat;
	}

	public void printDailyReport() {
		Collection<DayResults> days = dayResultsRepository.findAllSortedByDate();
		System.out.println("#### Daily incoming/outgoing report ####");
		for(DayResults day : days) {
			System.out.println(dateFormat.format(day.getDate()) + " - Incoming: " + formatter.format(day.getIncoming()) + " - Outgoing: " + formatter.format(day.getOutgoing()));
		}
	}
	
	public void printIncomingEntiyRanking() {		
		List<ClientEntity> entities = clientEntityRepository.findAllSortedByIncoming();
		System.out.println("#### Ranking of entities based on incoming ####");
		for(ClientEntity clientEntity : entities) {
			System.out.println(clientEntity.getName() + " " + formatter.format(clientEntity.getIncoming()));
		}
	}
	
	public void printOutgoingEntiyRanking() {		
		List<ClientEntity> entities = clientEntityRepository.findAllSortedByOutgoing();
		System.out.println("#### Ranking of entities based on Outgoing ####");
		for(ClientEntity clientEntity : entities) {
			System.out.println(clientEntity.getName() + " " + formatter.format(clientEntity.getOutgoing()));
		}
	}
}
