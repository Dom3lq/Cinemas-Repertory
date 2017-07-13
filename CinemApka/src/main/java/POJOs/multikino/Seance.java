package POJOs.multikino;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Seance {
	private String external_id;
	private String beginning_date;
	private Boolean is_active;
	private String id;

	public String getExternal_id() {
		return external_id;
	}

	public void setExternal_id(String external_id) {
		this.external_id = external_id;
	}

	public String getBeginning_date() {
		return beginning_date;
	}

	public void setBeginning_date(String beginning_date) {
		this.beginning_date = beginning_date;
	}

	public Boolean getIs_active() {
		return is_active;
	}

	public void setIs_active(Boolean is_active) {
		this.is_active = is_active;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
