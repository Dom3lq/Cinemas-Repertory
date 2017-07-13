package POJOs;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table
public class MovieShow {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String time;
	
	private String showCode;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "movieId")
	@JsonIgnore
	private Movie movie;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "repertoryId")
	//@JsonIgnore
	private Repertory repertory;
	
	public MovieShow(){};
	
	public MovieShow(String time, String showCode, Movie movie)
	{
		setTime( time );
		setCode( showCode ); 
		setMovie( movie );
	}
	
	public MovieShow(Builder builder) {
		this.id = builder.id;
		this.time = builder.time;
		this.movie = builder.movie;
		this.repertory = builder.repertory;
		this.showCode = builder.showCode;
	}

	public String getCode() {
		return showCode;
	}
	public void setCode(String code) {
		this.showCode = code;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String string) {
		this.time = string;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Repertory getRepertory() {
		return repertory;
	}

	public void setRepertory(Repertory repertory) {
		this.repertory = repertory;
	}
	
	public static class Builder{
		private Long id;
		private String time;
		private String showCode;
		private Movie movie;
		private Repertory repertory;
		
		public Builder(){}
		
		public MovieShow build() {
			return new MovieShow(this);
		}
		
		public Builder time(final String time){
			this.time = time;
			return this;
		}
		
		public Builder showCode(final String showCode){
			this.showCode = showCode;
			return this;
		}
		
		public Builder movie(final Movie movie){
			this.movie = movie;
			return this;
		}
		
		public Builder repertory(final Repertory repertory){
			this.repertory = repertory;
			return this;
		}
		
		public Builder id(final Long id){
			this.id = id;
			return this;
		}
	}
}
