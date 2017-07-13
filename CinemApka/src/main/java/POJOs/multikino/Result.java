package POJOs.multikino;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {
	private String event_id;
	private String beginning_date;
	private String booking_disabled;
	private String slug;
	private String external_id;
	private String icons;
	private String genres;
	private String version_name;
	private String version_id;
	private String eurobilet_id;
	private String title;
	private String category_id;
	private String event_type_id;
	private String booking_enabled;
	private List<Seance> seances = new ArrayList<Seance>();
	private String corner;
	private String surfix;
	private String print_version;
	
	public String getEvent_id() {
		return event_id;
	}
	public void setEvent_id(String event_id) {
		this.event_id = event_id;
	}
	public String getBeginning_date() {
		return beginning_date;
	}
	public void setBeginning_date(String beginning_date) {
		this.beginning_date = beginning_date;
	}
	public String getBooking_disabled() {
		return booking_disabled;
	}
	public void setBooking_disabled(String booking_disabled) {
		this.booking_disabled = booking_disabled;
	}
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	public String getExternal_id() {
		return external_id;
	}
	public void setExternal_id(String external_id) {
		this.external_id = external_id;
	}
	public String getIcons() {
		return icons;
	}
	public void setIcons(String icons) {
		this.icons = icons;
	}
	public String getGenres() {
		return genres;
	}
	public void setGenres(String genres) {
		this.genres = genres;
	}
	public String getVersion_name() {
		return version_name;
	}
	public void setVersion_name(String version_name) {
		this.version_name = version_name;
	}
	public String getVersion_id() {
		return version_id;
	}
	public void setVersion_id(String version_id) {
		this.version_id = version_id;
	}
	public String getEurobilet_id() {
		return eurobilet_id;
	}
	public void setEurobilet_id(String eurobilet_id) {
		this.eurobilet_id = eurobilet_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	public String getEvent_type_id() {
		return event_type_id;
	}
	public void setEvent_type_id(String event_type_id) {
		this.event_type_id = event_type_id;
	}
	public String getBooking_enabled() {
		return booking_enabled;
	}
	public void setBooking_enabled(String booking_enabled) {
		this.booking_enabled = booking_enabled;
	}
	public List<Seance> getSeances() {
		return seances;
	}
	public void setSeances(List<Seance> seances) {
		this.seances = seances;
	}
	public String getCorner() {
		return corner;
	}
	public void setCorner(String corner) {
		this.corner = corner;
	}
	public String getSurfix() {
		return surfix;
	}
	public void setSurfix(String surfix) {
		this.surfix = surfix;
	}
	public String getPrint_version() {
		return print_version;
	}
	public void setPrint_version(String print_version) {
		this.print_version = print_version;
	}
	
}
