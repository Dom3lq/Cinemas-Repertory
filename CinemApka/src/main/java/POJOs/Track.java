package POJOs;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class Track {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "movieId")
	private Movie movie;
	
	@ManyToOne
	@JoinColumn(name = "cinemaId")
	private Cinema cinema;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;
	
	private String date;
	
	private String action;
	
	public Track(){};
	
	public Track(Cinema cinema, String action){
		this.cinema = cinema;
		this.date = LocalDate.now().toString();
		this.action = action;
	};

	public Track(Movie movie, String action){
		this.movie = movie;
		this.date = LocalDate.now().toString();
		this.action = action;
	};
	
	public Track(Cinema cinema, Movie movie, String action){
		this.cinema = cinema;
		this.movie = movie;
		this.date = LocalDate.now().toString();
		this.action = action;
	};


	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	};
		
}
