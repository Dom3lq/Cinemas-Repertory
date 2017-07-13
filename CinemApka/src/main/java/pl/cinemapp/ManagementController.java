package pl.cinemapp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import POJOs.Track;

@RestController
@RequestMapping("/admin")
public class ManagementController {

	@Autowired
	private DataOperator dataOperator;

	@RequestMapping("/getTracks")
	public List<Track> getTracks(@RequestParam(value = "name") String name) {

		return dataOperator.getTracksFromRepository(name);
	}

	@RequestMapping("/getTracksInformation")
	public List<Integer> getTracksInformation(@RequestParam(value = "name") String name) {

		return dataOperator.getTracksInformationsFromRepository(name);
	}

	@RequestMapping("/countCinemasInDb")
	public String countCinemasInDb() {

		return dataOperator.countCinemasInRepository();
	}

	@RequestMapping("/setCinemaUser")
	public String setCinemaUser(@RequestParam(value = "cinemaId") Long cinemaId,
			@RequestParam(value = "userId") String userId) {

		return dataOperator.setCinemaUserInRepository(cinemaId, userId);

	}

	@RequestMapping("/setMovieUser")
	public String setMovieUser(@RequestParam(value = "movieName") String movieName,
			@RequestParam(value = "userId") String userId) {

		return dataOperator.setMovieUserInRepository(movieName, userId);
	}

	@RequestMapping("/getUpdatingProgress")
	public int getUpdatingProgress() {

		return dataOperator.getUpdatingProgress();
	}
}
