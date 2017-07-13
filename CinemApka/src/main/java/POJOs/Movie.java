package POJOs;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table
public class Movie{
	
	@Id
	@Column(unique = true, length = 100)
    private String name;
    
    private String featureLink;
    
    @Transient
    private String featureCode;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId")
    @JsonIgnore
    private User user;
    
    private String ageAllowed;
    private String languageInformation;
    private String length;

        
    @Column(length = 65535,columnDefinition="Text")    
    private String imgSrc;
    private String type;
    private String prime;
    private String director;
    
    @Column(length = 65535,columnDefinition="Text")    
    private String cast;
    
    private String production;
    
    @Column(length = 65535,columnDefinition="Text")    
    private String description;
   
    @JsonIgnore
    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    private List<MovieShow> movieShows;
    
    @JsonIgnore
    @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
    private List<Track> tracks;
    
    @Column(unique = false)
    private float filmwebAverage;
    
    public Movie(){
    	movieShows = new ArrayList<MovieShow>();
    	tracks = new ArrayList<Track>();
    };
    
    public Movie(String name)
    {
        this.name = name;
        movieShows = new ArrayList<MovieShow>();
    	tracks = new ArrayList<Track>();
    }
    
    public Movie(final Builder builder){
    	this.name=builder.name;
    	this.ageAllowed=builder.ageAllowed;
    	this.cast=builder.cast;
    	this.description=builder.description;
    	this.director=builder.director;
    	this.featureCode=builder.featureCode;
    	//this.id=builder.id;
    	this.imgSrc=builder.imgSrc;
    	this.featureLink=builder.featureLink;
    	this.languageInformation=builder.languageInformation;
    	this.length=builder.length;
    	this.type=builder.type;
    	this.prime=builder.prime;
    	this.production=builder.production;
    	this.filmwebAverage=builder.filmwebAverage;
    	movieShows = new ArrayList<MovieShow>();
    	tracks = new ArrayList<Track>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgeAllowed() {
        return ageAllowed;
    }

    public void setAgeAllowed(String ageAllowed) {
        this.ageAllowed = ageAllowed;
    }

    public String getFeatureLink() {
        return featureLink;
    }

    public void setFeatureLink(String featureLink) {
        this.featureLink = featureLink;
    }

    public String getLanguageInformation() {
        return languageInformation;
    }

    public void setLanguageInformation(String languageInformation) {
        this.languageInformation = languageInformation;
    }

	public String getFeatureCode() {
		return featureCode;
	}

	public void setFeatureCode(String featureCode) {
		this.featureCode = featureCode;
	

	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getImgSrc() {
		return imgSrc;
	}

	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPrime() {
		return prime;
	}

	public void setPrime(String prime) {
		this.prime = prime;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getCast() {
		return cast;
	}

	public void setCast(String cast) {
		this.cast = cast;
	}

	public String getProduction() {
		return production;
	}

	public void setProduction(String production) {
		this.production = production;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<MovieShow> getMovieShows() {
		return movieShows;
	}

	public void setMovieShows(List<MovieShow> movieShows) {
		this.movieShows = movieShows;
	}

/*	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
*/
	public List<Track> getTracks() {
		return tracks;
	}

	public void setTracks(List<Track> tracks) {
		this.tracks = tracks;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public float getFilmwebAverage() {
		return filmwebAverage;
	}

	public void setFilmwebAverage(float filmwebAverage) {
		this.filmwebAverage = filmwebAverage;
	}

	public static class Builder{
		//private Long id;
		private float filmwebAverage;
	    private final String name;
	    private String featureLink;
	    private String featureCode;
	    private String ageAllowed;
	    private String languageInformation;
	    private String length;
	    private String imgSrc;
	    private String type;
	    private String prime;
	    private String director;
	    private String cast;
	    private String production;
	    private String description;
	   
	    public Builder(final String name)
	    {
	    	this.name = name;
	    }
	    
	    public Builder filmwebAverage(final float filmwebAverage)
	    {
	    	this.filmwebAverage=filmwebAverage;
	    	return this;
	    }
	    
	    public Builder featureLink(final String featureLink){
	    	this.featureLink=featureLink;
	    	return this;
	    }
	    
	    public Builder featureCode(final String featureCode){
	    	this.featureCode=featureCode;
	    	return this;
	    }
	    
	    public Builder ageAllowed(final String ageAllowed){
	    	this.ageAllowed=ageAllowed;
	    	return this;
	    }
	    
	    public Builder languageInformation(final String languageInformation){
	    	this.languageInformation=languageInformation;
	    	return this;
	    }
	    
	    public Builder length(final String length){
	    	this.length=length;
	    	return this;
	    }
	    
	    public Builder imgSrc(final String imgSrc){
	    	this.imgSrc=imgSrc;
	    	return this;
	    }
	    
	    public Builder type(final String type){
	    	this.type=type;
	    	return this;
	    }
	    
	    public Builder prime(final String prime){
	    	this.prime=prime;
	    	return this;
	    }
	    
	    public Builder director(final String director){
	    	this.director=director;
	    	return this;
	    }
	    
	    public Builder cast(final String cast){
	    	this.cast=cast;
	    	return this;
	    }
	    
	    public Builder production(final String production){
	    	this.production=production;
	    	return this;
	    }
	    
	    public Builder description(final String description){
	    	this.description=description;
	    	return this;
	    }
	    
	    public Movie build(){
	    	return new Movie(this);
	    }

	}
	
	
}
