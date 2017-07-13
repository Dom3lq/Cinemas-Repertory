package pl.cinemapp;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import POJOs.Cinema;
import POJOs.Movie;
import POJOs.PotentialMovies;
import Repositories.CinemasRepository;
import Repositories.MoviesRepository;
import Repositories.UserRolesRepository;

@Controller
public class MvcController {

	@Autowired
	private CinemasRepository cinemasRepository;

	@Autowired
	private MoviesRepository moviesRepository;

	@Autowired
	private UserRolesRepository userRolesRepository;
	
	@RequestMapping(value="/addMoviesProposal", method = RequestMethod.POST)
	public String addMoviesProposalSubmit(@ModelAttribute(value="potentialMovies") PotentialMovies potentialMovies, Model model){
		
		return "addMoviesProposal";
	}
	
	@RequestMapping(value = "/editCinema", method = RequestMethod.GET)
	public String editCinemaForm(@RequestParam(value = "cinemaId") Long cinemaId, Model model) {
		try {
			Cinema cinema = cinemasRepository.findOne(cinemaId);

			if (cinema.getUser() != null) {
				if (SecurityContextHolder.getContext().getAuthentication().getName()
						.equals(cinema.getUser().getName())) {
					model.addAttribute("cinema", cinema);
					model.addAttribute("userValidated", true);
				} else
					model.addAttribute("userValidated", false);
			} else
				model.addAttribute("userValidated", false);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "editCinema";
	}

	@RequestMapping(value = "/editCinema", method = RequestMethod.POST)
	public String editCinemaSubmit(@ModelAttribute Cinema cinema, Model model) {
		try {
			Cinema oldCinema = cinemasRepository.findOne(cinema.getId());
			System.out.println(cinema.getName());

			if (oldCinema.getUser() != null && cinema.getUser() != null) {
				if (SecurityContextHolder.getContext().getAuthentication().getName()
						.equals(cinema.getUser().getName())) {
					model.addAttribute("oldCinema", oldCinema);
					cinemasRepository.save(cinema);
					model.addAttribute("newCinema", cinema);
					model.addAttribute("userValidated", true);

				} else
					model.addAttribute("userValidated", false);

			} else
				model.addAttribute("userValidated", false);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "editCinemaSubmit";
	}


	@RequestMapping(value = "/editMovie", method = RequestMethod.GET)
	public String editMovieForm(@RequestParam(value = "movieId") String movieName, Model model) {
		try {
			Movie movie = moviesRepository.findOne(movieName);

			if (movie.getUser() != null) {
				if (SecurityContextHolder.getContext().getAuthentication().getName()
						.equals(movie.getUser().getName())) {
					model.addAttribute("movie", movie);
					model.addAttribute("userValidated", true);
				} else
					model.addAttribute("userValidated", false);
			} else
				model.addAttribute("userValidated", false);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "editMovie";
	}

	@RequestMapping(value = "/editMovie", method = RequestMethod.POST)
	public String editMovieSubmit(@ModelAttribute Movie movie, Model model) {
		try {
			Movie oldMovie = moviesRepository.findOne(movie.getName());
			System.out.println(movie.getName());

			if (oldMovie.getUser() != null && movie.getUser() != null) {
				if (SecurityContextHolder.getContext().getAuthentication().getName()
						.equals(movie.getUser().getName())) {
					model.addAttribute("oldMovie", oldMovie);
					moviesRepository.save(movie);
					model.addAttribute("newMovie", movie);
					model.addAttribute("userValidated", true);

				} else
					model.addAttribute("userValidated", false);

			} else
				model.addAttribute("userValidated", false);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "editMovieSubmit";
	}

	@RequestMapping("/greeting")
	public String greeting(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null)
			model.addAttribute("name", auth.getName());

		return "greeting";
	}

	@RequestMapping({"/home", " ", "/"})
	public String home(ModelMap model, HttpSession session) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth != null) {
			List<Movie> userMovies=moviesRepository.findByUserName(auth.getName());
			List<Movie> potentialMoviesList = moviesRepository.findAll().stream()
					.filter(m-> m.getUser() == null)
					.collect(Collectors.toList());
			
			model.addAttribute("potentialMoviesList", potentialMoviesList);
			model.addAttribute("username", auth.getName());
			model.put("user_roles", userRolesRepository.findByUserName(auth.getName()));
			model.put("cinemas", cinemasRepository.findByUserName(auth.getName()));
			model.put("movies", userMovies);
		}

		return "home";
	}

}
