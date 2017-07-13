package POJOs;

import java.time.Duration;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Method {
	
	@JsonIgnore
	private Duration duration;
	private String name;
	private String averageTime;
	
	public Method(String n, Duration d){
		this.name = n;
		this.setDuration(d);
	}
	
	public Method(String n, Duration d, String t){
		this.name = n;
		this.setDuration(d);
		this.setAverageTime(t);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Duration getDuration() {
		return duration;
	}

	public void setDuration(Duration duration) {
		this.duration = duration;
	}

	public String getAverageTime() {
		return averageTime;
	}

	public void setAverageTime(String averageTime) {
		this.averageTime = averageTime;
	}
}
