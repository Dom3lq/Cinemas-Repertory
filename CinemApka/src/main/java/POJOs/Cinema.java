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

import POJOs.distanceMatrixPojos.MatrixDistance;

@Entity
@Table
public class Cinema{
	
	@Id
	@GeneratedValue
	private Long id;
	
    private String name;
    private String adress;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;
    
    private String locationId = new String();
    
    @Transient
    private MatrixDistance matrixDistance;
        
    @JsonIgnore
    @OneToMany(mappedBy = "cinema", fetch = FetchType.LAZY)
    private List<Repertory> repertories;
    
    @JsonIgnore
    @OneToMany(mappedBy = "cinema", fetch = FetchType.LAZY)
    private List<Track> tracks;

    public Cinema(){
    	repertories = new ArrayList<Repertory>();
    	tracks = new ArrayList<Track>();
    }
    
    public Cinema(Builder builder){
    	repertories = new ArrayList<Repertory>();
    	tracks = new ArrayList<Track>();
    	
    	this.adress = builder.adress;
    	this.id = builder.id;
    	this.name = builder.adress;
    	this.user = builder.user;
    	this.locationId = builder.locationId;
    	this.repertories = builder.repertories;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public List<Repertory> getRepertories() {
        return repertories;
    }
    

    public void addRepertory(Repertory repertory) {
        this.repertories.add(repertory);
    }
    
    public void setRepertories(List<Repertory> repertories){
    	this.repertories = repertories;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

	public MatrixDistance getMatrixDistance() {
		return matrixDistance;
	}

	public void setMatrixDistance(MatrixDistance matrixDistance) {
		this.matrixDistance = matrixDistance;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public static class Builder{
		private Long id;
		private String name;
		private String adress;
		private User user;
		private String locationId;
		private List<Repertory> repertories;
		
		public Builder(final String name){
			this.name = name;
		}
		
		public Builder id(final Long id){
			this.id = id;
			return this;
		}
		
		public Builder adress(final String adress){
			this.adress = adress;
			return this;
		}
		
		public Builder user(final User user){
			this.user = user;
			return this;
		}
		
		public Builder locationId(final String locationId){
			this.locationId = locationId;
			return this;
		}
		
		public Builder repertories(final List<Repertory> repertories){
			this.repertories = repertories;
			return this;
		}
		
		public Cinema build(){
			return new Cinema(this);
		}
	}

	
}