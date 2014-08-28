package nu.dyn.caapi.coinalyzer.db.entities;
 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
 
@Entity
@Table(name = "CONFIGURATION")
public class CoinOHLC {
	     
	@Column(name="COINPAIR")
    private String coinPair;

 	 public String getRequestor() {
 		return coinPair;
 	}

 	public void setRequestor(String requestor) {
 		this.coinPair = requestor;
 	}
	@Override
    public String toString() {
        // TODO:
		return ""+this;
    }
 
}