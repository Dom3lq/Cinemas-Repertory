package pl.cinemapp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import POJOs.Cinema;
import POJOs.Movie;
import POJOs.MovieShow;

@RestController
@RequestMapping("/client")
public class ClientController {

	@Autowired
	private DataOperator dataOperator;

	@RequestMapping("/moviedetails")
	@ResponseBody
	public Movie getMovieDetails(@RequestParam(value = "name") String name) {

		return dataOperator.getMovieDetailsFromRepository(name);
	}

	@RequestMapping("/cinemas")
	public List<Cinema> getAllCinemas(@RequestParam(value = "origin", defaultValue = "null") String origin,
			@RequestParam(value = "city", defaultValue = "null") String city) {

		return dataOperator.getAllCinemasFromRepository(origin, city);
	}

	@RequestMapping("/movies")
	public List<Movie> getRepertory(@RequestParam(value = "cinemaId", required = false) Long cinemaId,
			@RequestParam(value = "date", defaultValue = "null") String date,
			@RequestParam(value = "hot", defaultValue = "false") Boolean hot,
			@RequestParam(value = "mark", defaultValue = "false") Boolean mark) {

		if (mark == true || hot == true && cinemaId == null && date.equals("null")) {
			if (hot == true)
				return dataOperator.getHotMovies();
			else
				return dataOperator.getTopMarkMovies();
		} else
			return dataOperator.getCinemaMoviesFromRepository(cinemaId, date, hot, mark);

	}

	@RequestMapping("/movieshows")
	public List<MovieShow> getMovieShows(@RequestParam(value = "cinemaId") Long cinemaId,
			@RequestParam(value = "date", defaultValue = "null") String date,
			@RequestParam(value = "movieName") String movieName) {

		return dataOperator.getMovieShowsFromRepository(cinemaId, date, movieName);
	}

	@RequestMapping("/moviecinemas")
	public List<MovieShow> getMovieCinemas(@RequestParam(value = "moviename") String movieName) {

		return dataOperator.getMovieShowsFromRepository(movieName);
	}
}
