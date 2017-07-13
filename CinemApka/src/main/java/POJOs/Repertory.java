package POJOs;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table
public class Repertory {

	@Id
	@GeneratedValue
	private Long id;

	private String date;

	@Transient
	private String locationName;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "cinemaId")
	private Cinema cinema;

	@OneToMany(mappedBy = "repertory", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<MovieShow> movieShows;

	public Repertory() {
		movieShows = new ArrayList<MovieShow>();
	};

	public Repertory(String date, String locationName) {
		this.date = date;
		this.locationName = locationName;
		movieShows = new ArrayList<MovieShow>();
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<MovieShow> getMovieShows() {
		return movieShows;
	}

	public void setMovieShows(List<MovieShow> movieShows) {
		this.movieShows = movieShows;
	}
}