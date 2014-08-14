package nu.dyn.caapi.market;

import org.joda.time.DateTime;

import eu.verdelhan.ta4j.Tick;

public class CoinTick implements Tick {
	private DateTime beginTime;

	private DateTime endTime;

	private double openPrice = -1;

	private double closePrice = -1;

	private double maxPrice = -1;

	private double minPrice = -1;

	private double amount = 0d;

	private double volume = 0d;

	private int trades = 0;

	/**
	 * @param beginTime
	 *            the end time of the tick period
	 * @param openPrice
	 *            the open price of the tick period
	 * @param highPrice
	 *            the highest price of the tick period
	 * @param lowPrice
	 *            the lowest price of the tick period
	 * @param closePrice
	 *            the close price of the tick period
	 * @param volume
	 *            the volume of the tick period
	 */
	public CoinTick(DateTime beginTime, double openPrice, double highPrice,
			double lowPrice, double closePrice, double volume) {
		this.beginTime = beginTime;
		this.openPrice = openPrice;
		this.maxPrice = highPrice;
		this.minPrice = lowPrice;
		this.closePrice = closePrice;
		this.volume = volume;
	}

	/**
	 * @return the close price of the period
	 */
	public double getClosePrice() {
		return closePrice;
	}

	/**
	 * @return the open price of the period
	 */
	public double getOpenPrice() {
		return openPrice;
	}

	/**
	 * @return the number of trades in the period
	 */
	public int getTrades() {
		return trades;
	}

	/**
	 * @return the max price of the period
	 */
	public double getMaxPrice() {
		return maxPrice;
	}

	/**
	 * @return the whole traded amount of the period
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @return the whole traded volume in the period
	 */
	public double getVolume() {
		return volume;
	}

	/**
	 * @return the min price of the period
	 */
	public double getMinPrice() {
		return minPrice;
	}

	/**
	 * @return the begin timestamp of the tick period
	 */
	public DateTime getBeginTime() {
		return beginTime;
	}

	/**
	 * @return the end timestamp of the tick period
	 */
	public DateTime getEndTime() {
		return endTime;
	}

	@Override
	public String toString() {
		return minPrice+"<"+openPrice+"-"+closePrice+">"+maxPrice+"@"+volume;
	}

	/**
	 * @param timestamp
	 *            a timestamp
	 * @return true if the provided timestamp is between the begin time and the
	 *         end time of the current period, false otherwise
	 */
	public boolean inPeriod(DateTime timestamp) {
		return timestamp == null ? false
				: (!timestamp.isBefore(beginTime) && timestamp
						.isBefore(endTime));
	}

	/**
	 * @return a human-friendly string of the end timestamp
	 */
	public String getDateName() {
		return endTime.toString("hh:mm dd/MM/yyyy");
	}

	/**
	 * @return a even more human-friendly string of the end timestamp
	 */
	public String getSimpleDateName() {
		return endTime.toString("dd/MM/yyyy");
	}
}
