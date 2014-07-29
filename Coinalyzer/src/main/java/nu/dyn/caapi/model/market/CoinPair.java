package nu.dyn.caapi.model.market;

public class CoinPair {
	private final String coinBase;
	private final String coinCounter;
	private final String coinPairId;
	
	public CoinPair(String currencyBase, String currencyCounter) {
		this.coinBase = currencyBase;
		this.coinCounter = currencyCounter;
		this.coinPairId = currencyBase+"_"+currencyCounter;
	}
	
	public String getCurrencyBase() {
		return coinBase;
	}
	
	public String getCurrencyCounter() {
		return coinCounter;
	}
	
	public String getCurrencyPairId() {
		return coinPairId;
	}
}
