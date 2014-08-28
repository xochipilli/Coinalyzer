package nu.dyn.caapi.coinalyzer.market;

public class CoinPairInfo {
	private final String coinBase;
	private final String coinCounter;
	private final String coinPairId;
	
	public CoinPairInfo(String coinBase, String coinCounter) {
		this.coinBase = coinBase;
		this.coinCounter = coinCounter;
		this.coinPairId = coinBase+"_"+coinCounter;
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
