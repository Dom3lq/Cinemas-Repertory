package pl.cinemapp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import POJOs.Cinema;
import POJOs.Movie;

@RestController
public class PartnersController {
	
	@Autowired
	private DataOperator dataOperator;
	
	@RequestMapping("/getAvailableCinemas")
	public List<Cinema> getAvailableCinemas() {
		
		return dataOperator.getCinemasWithNoUser();
	}

	@RequestMapping("/getAvailableMovies")
	public List<Movie> getAvailableMovies() {
		
		return dataOperator.getMoviesWithNoUser();
	}

	
	@RequestMapping("/getMyCinemas")
	public List<Cinema> getMyCinemas() {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return dataOperator.getUserCinemas(auth.getName());
	}

	@RequestMapping("/getMyMovies")
	public List<Movie> getMyMovies() {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return dataOperator.getPartnerMovies(auth.getName());
	}

}
