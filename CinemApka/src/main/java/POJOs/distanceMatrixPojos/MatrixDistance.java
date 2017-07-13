package POJOs.distanceMatrixPojos;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class MatrixDistance {
	private List<String> destinationAddresses = new ArrayList<String>();
	private List<String> originAddresses = new ArrayList<String>();
	private List<Row> rows = new ArrayList<Row>();
	private String status;
		
	public List<String> getDestinationAddresses() {
		return destinationAddresses;
	}
	public void setDestinationAddresses(List<String> destinationAddresses) {
		this.destinationAddresses = destinationAddresses;
	}
	public List<String> getOriginAddresses() {
		return originAddresses;
	}
	public void setOriginAddresses(List<String> originAddresses) {
		this.originAddresses = originAddresses;
	}
	public List<Row> getRows() {
		return rows;
	}
	public void setRows(List<Row> rows) {
		this.rows = rows;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
