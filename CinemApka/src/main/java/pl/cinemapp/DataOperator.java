package pl.cinemapp;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import POJOs.Cinema;
import POJOs.Movie;
import POJOs.MovieShow;
import POJOs.Repertory;
import POJOs.Track;
import POJOs.User;
import POJOs.distanceMatrixPojos.MatrixDistance;
import POJOs.multikino.MultikinoRepertory;
import POJOs.multikino.Result;
import POJOs.multikino.Seance;
import Repositories.CinemasRepository;
import Repositories.MovieShowsRepository;
import Repositories.MoviesRepository;
import Repositories.RepertoriesRepository;
import Repositories.TracksRepository;
import Repositories.UsersRepository;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Component
public class DataOperator implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	private CinemasRepository cinemasRepository;

	@Autowired
	private RepertoriesRepository repertoriesRepository;

	@Autowired
	private MoviesRepository moviesRepository;

	@Autowired
	private MovieShowsRepository movieShowsRepository;

	@Autowired
	private TracksRepository tracksRepository;

	@Autowired
	private UsersRepository usersRepository;

	private int updatingProgress;

	public void updateCinemas() throws InterruptedException {

		getCinemaCityData();

		getMultikinoData();

		getFilmwebData();

		this.updateCinemas();

	}

	private void getFilmwebData() throws InterruptedException {

		List<Movie> allMovies = moviesRepository.findAll();

		int updatingMoviesProgressIterator = 0;
		for (Movie m : allMovies) {
			getMovieDetailsFromFilmweb(m, 0);

			updatingMoviesProgressIterator++;
			setUpdatingProgress(updatingMoviesProgressIterator * 10 / allMovies.size() + 90);
		}
	}

	private void getCinemaCityData() {
		try {
			Elements mainpageRepertoirElements = getCinemaCityWebpageRepertoirElements();

			Elements maipageCinemaElements = getCinemaCityWebpageCinemaElements();

			System.out.println("Cinemma City:");

			int updatingCinemasProgressIterator = 0;
			for (Element cinemaElement : maipageCinemaElements) {

				Cinema cinema = cinemasRepository.findOneByName(
						"Cinema-City " + cinemaElement.select("div.cinema_name").select("a[href]").text());

				if (cinema == null) {
					cinema = parseCinemaCityCinemaFromElement(cinema, cinemaElement, mainpageRepertoirElements);
					cinema = cinemasRepository.save(cinema);
					System.out.println("	C NEW: " + cinema.getName());
				} else
					System.out.println("	C OLD: " + cinema.getName());

				for (LocalDate date = LocalDate.now(); true; date = date.plusDays(1)) {

					Document repertuarDocument = getCinemaCityCinemaRepertuarDocument(cinema, date);

					if (repertuarDocument.select("tbody").select("tr.odd, tr.even").size() == 0
							&& date.isAfter(LocalDate.now().plusDays(5)))
						break;
					else {
						Repertory repertory = repertoriesRepository.findOneByDateAndCinemaName(
								parseDate(repertuarDocument.select("div.header > label.date").text()),
								cinema.getName());

						if (repertory == null) {
							repertory = parseCinemaCityRepertoirFromDocument(repertory, cinema, repertuarDocument);
							repertory = repertoriesRepository.save(repertory);
							System.out.println("		R NEW " + repertory.getDate());
						} else
							System.out.println("		R OLD " + repertory.getDate());

						for (Element e : repertuarDocument.select("tbody").select("tr.odd, tr.even")) {

							Movie movie = moviesRepository.findOne(e.select("td.featureName").select("a").text());

							if (movie == null) {
								movie = parseCinemaCityMovieFromElement(movie, e);
								movie = moviesRepository.save(movie);
								System.out.println("			M NEW " + movie.getName());
							} else
								System.out.println("			N OLD " + movie.getName());

							for (Element t : e.select("a.presentationLink")) {
								String time = t.text().split(" ")[0];

								MovieShow movieShow = movieShowsRepository.findOneByTimeAndRepertoryIdAndMovie_name(
										time, repertory.getId(), movie.getName());

								if (movieShow == null) {
									movieShow = getMovieShow(movieShow, repertory, movie, t.attr("data-prsnt_code"),
											time);
									movieShow = movieShowsRepository.save(movieShow);
									System.out.println("				S NEW " + movieShow.getTime());
								} else
									System.out.println("				S OLD " + movieShow.getTime());

							}
						}
					}
				}

				updatingCinemasProgressIterator++;
				setUpdatingProgress((updatingCinemasProgressIterator * 45 / maipageCinemaElements.size()) + 45);
			}
		} catch (Exception e) {
			e.printStackTrace();
			getCinemaCityData();
		}
	}

	private void getMultikinoData() {
		try {
			Elements cinemaElements = getCinemaElementsFromMultikinoWebpage();
			Document cinemasListDocument = getCinemaListFromMultikinoWebpage();

			int updatingCinemasProgressIterator = 0;

			for (Element ce : cinemaElements) {

				Cinema cinema = cinemasRepository.findOneByName("Multikino " + ce.text());

				if (cinema == null) {
					cinema = parseMultikinoCinemaFromDocument(cinema, cinemasListDocument, ce);
					cinema = cinemasRepository.save(cinema);
					System.out.println("	C NEW " + cinema.getName());
				} else
					System.out.println("	C OLD " + cinema.getName());

				for (LocalDate date = LocalDate.now(); true; date = date.plusDays(1)) {

					MultikinoRepertory multikinoRepertory = getMultikinoRepertoryFromMultikinoController(
							"https://multikino.pl/pl/repertoire/cinema/seances?id=" + cinema.getLocationId() + "&from="
									+ date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + "&eventId=");

					if (multikinoRepertory.getResults().isEmpty() && !date.equals(LocalDate.now()))
						break;
					else {

						Repertory repertory = repertoriesRepository.findOneByDateAndCinemaName(date.toString(),
								cinema.getName());

						if (repertory == null) {
							repertory = parseMultikinoRepertory(repertory, cinema, date.toString());
							repertory = repertoriesRepository.save(repertory);
							System.out.println("		R NEW " + repertory.getDate());
						} else
							System.out.println("		R OLD " + repertory.getDate());

						for (Result m : multikinoRepertory.getResults()) {

							Movie movie = moviesRepository.findOne(m.getTitle());

							if (movie == null) {
								movie = parseMovieFromMultikinoMovieObject(movie, m);
								movie = moviesRepository.save(movie);
								System.out.println("			M NEW " + movie.getName());
							} else {
								System.out.println("			M OLD " + movie.getName());
							}

							for (Seance s : m.getSeances()) {

								String time = LocalTime.parse(s.getBeginning_date().split(" ")[1])
										.format(DateTimeFormatter.ofPattern("kk:mm"));

								MovieShow movieShow = movieShowsRepository.findOneByTimeAndRepertoryIdAndMovie_name(
										time, repertory.getId(), movie.getName());

								if (movieShow == null) {
									movieShow = getMovieShow(movieShow, repertory, movie, s.getExternal_id(), time);

									movieShow = movieShowsRepository.save(movieShow);
									System.out.println("				S NEW " + movieShow.getTime());
								} else
									System.out.println("				S OLD " + movieShow.getTime());

							}

						}
					}
				}
				updatingCinemasProgressIterator++;
				setUpdatingProgress(updatingCinemasProgressIterator * 45 / cinemaElements.size());
			}

		} catch (Exception e) {
			e.printStackTrace();
			getMultikinoData();

		}

	}

	/*
	 * private Cinema getCinema(Cinema cinema, String name) { Instant start =
	 * Instant.now();
	 * 
	 * if (cinema == null) { cinema = new Cinema.Builder(name).build();
	 * 
	 * if (name.contains("Cinema-City")) { // cinema.setLocationId(); } else if
	 * (name.contains("Multikino")) { // cinema.setLocationId(); }
	 * 
	 * System.out.println("	C NEW: " + cinema.getName()); } else
	 * System.out.println("	C OLD: " + cinema.getName());
	 * 
	 * methodDuration.add(new
	 * POJOs.Method(Thread.currentThread().getStackTrace()[1].getMethodName(),
	 * Duration.between(start, Instant.now()))); return cinema; }
	 */

	private Cinema parseCinemaCityCinemaFromElement(Cinema cinema, Element cinemaElement, Elements repertuarElements) {

		cinema = new Cinema();
		cinema.setName("Cinema-City " + cinemaElement.select("div.cinema_name").select("a[href]").text());

		String adress = cinemaElement.text();
		adress = adress.substring(0, adress.indexOf("Tel"));
		if (adress.contains("ul"))
			adress = adress.substring(adress.indexOf("ul"));
		else if (adress.contains("al"))
			adress = adress.substring(adress.indexOf("al"));

		adress = deleteSpacesAtTheEndOfTheGivenString(adress);

		cinema.setAdress(adress);

		cinema = getCinemaCityCinemasLocationIds(repertuarElements, cinema);

		return cinema;
	}

	private Cinema parseMultikinoCinemaFromDocument(Cinema cinema, Document cinemasListDocument, Element ce)
			throws InterruptedException {

		try {

			cinema = new Cinema();
			cinema.setLocationId(ce.attr("rel"));
			cinema.setName("Multikino " + ce.text());

			Element e = Jsoup.connect("https://multikino.pl" + ce.attr("href")).get().select("div.address").first();

			if (e != null) {
				e.select(" > *").remove();
				cinema.setAdress(e.text());
			} else {
				String[] cinemaSubs = cinema.getName().split(" ", 2);

				Element ae = cinemasListDocument.select("strong:contains(" + cinemaSubs[1] + ")").first();

				Element ae2 = ae.parent().nextElementSibling();

				if (ae2 != null) {
					ae2.select(" > *").remove();

					List<String> adresSplit = Arrays.asList(ae2.text().split("adres"));
					cinema.setAdress(adresSplit.get(0));
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			Thread.sleep(500);
			cinema = parseMultikinoCinemaFromDocument(cinema, cinemasListDocument, ce);
		}
		return cinema;
	}

	private MovieShow getMovieShow(MovieShow movieShow, Repertory repertory, Movie movie, String showCode,
			String time) {

		movieShow = new MovieShow.Builder().time(time).movie(movie).repertory(repertory).showCode(showCode).build();

		return movieShow;
	}

	private Document getCinemaListFromMultikinoWebpage() throws InterruptedException {
		Document cinemasListDocument;
		try {
			cinemasListDocument = Jsoup.connect("https://multikino.pl/pl/wszystkie-kina").get();
		} catch (Exception e) {
			e.printStackTrace();
			Thread.sleep(500);
			cinemasListDocument = getCinemaListFromMultikinoWebpage();
		}
		return cinemasListDocument;
	}

	private Movie parseMovieFromMultikinoMovieObject(Movie movie, Result m) throws InterruptedException {

		movie = new Movie.Builder(m.getTitle()).languageInformation(m.getVersion_name())
				.featureLink("https://multikino.pl/pl/filmy/" + m.getSlug()).build();

		getMultikinoMovieDetails(movie);

		return movie;
	}

	private MultikinoRepertory getMultikinoRepertoryFromMultikinoController(String multikinoRepertoryUrl)
			throws InterruptedException {
		MultikinoRepertory multikinoRepertory;
		try {
			multikinoRepertory = new RestTemplate().getForObject(multikinoRepertoryUrl, MultikinoRepertory.class);
		} catch (Exception e) {
			e.printStackTrace();
			Thread.sleep(500);
			multikinoRepertory = getMultikinoRepertoryFromMultikinoController(multikinoRepertoryUrl);
		}
		return multikinoRepertory;
	}

	private Repertory parseMultikinoRepertory(Repertory repertory, Cinema cinema, String date) {

		repertory = new Repertory();

		repertory.setCinema(cinema);

		repertory.setDate(date);

		return repertory;
	}

	private Elements getCinemaElementsFromMultikinoWebpage() {
		Elements cinemaElements;

		try {
			cinemaElements = Jsoup.connect("https://multikino.pl/pl/nasze-kina").get().select("a.title");
		} catch (Exception e) {
			e.printStackTrace();
			cinemaElements = getCinemaElementsFromMultikinoWebpage();
		}
		return cinemaElements;
	}

	private Movie parseCinemaCityMovieFromElement(Movie movie, Element e) throws InterruptedException {

		movie = new Movie.Builder(e.select("td.featureName").select("a").text())
				.featureLink(e.select("td.featureName").select("a").attr("href"))
				.featureCode(e.select("td.featureName").select("a").attr("data-feature_code"))
				.ageAllowed(e.select("td.rating").text())
				.languageInformation(e.getElementsByIndexEquals(3).text() + " " + e.getElementsByIndexEquals(2).text())
				.build();

		getMovieDetailsFromCinemaCityWebpage(movie);

		return movie;
	}

	private Repertory parseCinemaCityRepertoirFromDocument(Repertory repertory, Cinema cinema,
			Document repertuarDocument) {
		String date = parseDate(repertuarDocument.select("div.header > label.date").text());

		repertory = new Repertory(date, repertuarDocument.select("div.header > label.locationName").text());
		repertory.setCinema(cinema);

		return repertory;
	}

	private Document getCinemaCityCinemaRepertuarDocument(Cinema cinema, LocalDate date) throws InterruptedException {
		Document repertuarDocument;
		try {
			repertuarDocument = Jsoup.connect("http://www.cinema-city.pl/scheduleInfo?locationId="
					+ cinema.getLocationId() + "&date=" + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))).get();
		} catch (Exception e) {
			e.printStackTrace();
			Thread.sleep(500);
			repertuarDocument = getCinemaCityCinemaRepertuarDocument(cinema, date);
		}

		return repertuarDocument;
	}

	private Elements getCinemaCityWebpageCinemaElements() throws InterruptedException {
		Elements cinemaElements;
		try {
			cinemaElements = Jsoup.connect("https://www.cinema-city.pl/cinemas").validateTLSCertificates(false).get()
					.select("td.cinema_info");
		} catch (Exception e) {
			e.printStackTrace();
			Thread.sleep(500);
			cinemaElements = getCinemaCityWebpageCinemaElements();
		}

		return cinemaElements;
	}

	private Elements getCinemaCityWebpageRepertoirElements() throws InterruptedException {
		Elements repertuarElements;
		try {
			repertuarElements = Jsoup.connect("https://www.cinema-city.pl/").validateTLSCertificates(false).get()
					.select("a:contains(Repertuar)");

		} catch (Exception e) {
			Thread.sleep(500);
			e.printStackTrace();
			repertuarElements = getCinemaCityWebpageRepertoirElements();
		}

		return repertuarElements;
	}

	private void getMovieDetailsFromFilmweb(Movie movie, int iterator) throws InterruptedException {
		try {
			Element searchFirstHitDesc = Jsoup.connect(URI
					.create("http://www.filmweb.pl/search/film?q=" + movie.getName().replaceAll("3D", "")
							.replaceAll("IMAX", "").replaceAll("VIP", "").replaceAll("4DX", "").replaceAll(" ", "%20"))
					.toASCIIString()).get().select("div.hitDesc").first();

			if (searchFirstHitDesc != null) {
				String link = "http://www.filmweb.pl" + searchFirstHitDesc.select("a").first().attr("href");
				;

				Document filmwebMovieDocument = Jsoup.connect(link).get();

				String mark = filmwebMovieDocument.getElementsByAttributeValue("property", "v:average").first().text()
						.replaceAll(",", ".");

				if (movie.getDescription().isEmpty())
					movie.setDescription(filmwebMovieDocument.select("div.filmMainHeaderParent")
							.select("div.filmPlot.bottom-15 > p.text").text());

				if (movie.getLength() == null)
					movie.setLength(filmwebMovieDocument.select("div.filmTime").text());

				movie.setFilmwebAverage(Float.valueOf(mark));

			}

		} catch (Exception e) {
			iterator++;
			if (iterator < 3) {
				Thread.sleep(500);
				getMovieDetailsFromFilmweb(movie, iterator);
			}
			if (iterator == 3) {
				System.out.println("Problem with: " + movie.getName());
				e.printStackTrace();
			}
		}
	}

	private void getMultikinoMovieDetails(Movie movie) throws InterruptedException {
		try {
			Document movieInformationsPage = Jsoup.connect(movie.getFeatureLink()).get();

			for (Element detail : (movieInformationsPage.select("div.film-details tbody").first().select("td"))) {
				switch (detail.previousElementSibling().text()) {
				case "Premiera:":
					movie.setPrime(detail.text());
					break;
				case "Wiek:":
					movie.setAgeAllowed(detail.text());
					break;
				case "Czas trwania:":
					movie.setLength(detail.text());
					break;
				case "Produkcja:":
					movie.setProduction(detail.text());
					break;
				case "Wersja:":
					movie.setLanguageInformation(detail.text());
					break;
				case "Reżyseria:":
					movie.setDirector(detail.text());
					break;
				case "Obsada:":
					movie.setCast(detail.text());
				}
			}

			movie.setDescription(movieInformationsPage.select("div[itemprop=description] p").text());
			movie.setImgSrc("https:" + movieInformationsPage.select("div.film-cover > img").attr("src"));

		} catch (Exception e) {
			e.printStackTrace();
			Thread.sleep(500);
			getMultikinoMovieDetails(movie);
		}

	}

	private Cinema getCinemaCityCinemasLocationIds(Elements repertoirElements, Cinema cinema) {
		Elements containingSequence = repertoirElements.select("a[onclick*=openScheduleInfo");

		Elements matchingName = new Elements();

		for (Element e : containingSequence) {
			if (e.text().contains(cinema.getName()) || cinema.getName().contains(e.text()))
				matchingName.add(e);
		}

		Elements elementsToRemove = new Elements();

		if (matchingName.size() > 1) {
			for (Element e : matchingName) {
				if (!cinema.getName().contains(e.parent().parent().parent().select("li > a").first().text()))
					elementsToRemove.add(e);
			}
		}

		matchingName.removeAll(elementsToRemove);

		for (Element e : matchingName) {
			String link = e.attr("onclick");
			if (link.contains("(") && link.contains(","))
				link = link.substring(link.indexOf('(') + 1, link.indexOf(","));
			cinema.setLocationId(link);
		}

		return cinema;
	}

	private Movie getMovieDetailsFromCinemaCityWebpage(Movie movie) throws InterruptedException {
		String url = "http://www.cinema-city.pl/featureInfo?featureCode=" + movie.getFeatureCode()
				+ "&groupByDistributorCode=true&openedFromPopup=1";

		OkHttpClient client = new OkHttpClient();

		try {
			Request request = new Request.Builder().url(url).build();

			Response response = client.newCall(request).execute();

			Document movieDoc = Jsoup.parse(response.body().string());

			Element mainFrame = movieDoc.select("div.feature_info_container").first();

			movie.setImgSrc(mainFrame.select("div.poster_holder").first().select("img").first().attr("src"));
			movie.setPrime(mainFrame.select("div.feature_info_row ").select("div.pre_label:contains(Premiera:)").first()
					.nextElementSibling().text());
			movie.setDirector(mainFrame.select("div.feature_info_row ").select("div.pre_label:contains(Reżyseria:)")
					.first().nextElementSibling().text());
			movie.setCast(mainFrame.select("div.feature_info_row ").select("div.pre_label:contains(Obsada:)").first()
					.nextElementSibling().select("p").text());
			movie.setProduction(mainFrame.select("div.feature_info_row ")
					.select("div.pre_label:contains(Produkcja / Rok:)").first().nextElementSibling().text());
			movie.setType(mainFrame.select("div.feature_info_row ").select("div.pre_label:contains(Gatunek:)").first()
					.nextElementSibling().text());
			movie.setLength(mainFrame.select("div.feature_info_row ").select("div.pre_label:contains(Długość:)").first()
					.nextElementSibling().text());
			movie.setDescription(mainFrame.select("jspContainer").select("div.jspPane").select("p").text());

		} catch (OutOfMemoryError e) {
			Thread.sleep(500);
			e.printStackTrace();
			getMovieDetailsFromCinemaCityWebpage(movie);

		} catch (IOException ie) {
			Thread.sleep(500);
			ie.printStackTrace();
			getMovieDetailsFromCinemaCityWebpage(movie);

		}

		return movie;
	}

	public List<Track> getTracksFromRepository(String name) {
		return tracksRepository.findByCinema_NameOrMovie_Name(name, name);
	}

	public List<Integer> getTracksInformationsFromRepository(String name) {
		List<Track> tracks = tracksRepository.findByCinema_NameOrMovie_Name(name, name);
		List<Integer> informations = new ArrayList<Integer>();

		if (tracks != null) {
			Long todayTracks = tracks.stream().filter(t -> t.getDate() == LocalDate.now().toString()).count();
			informations.add(tracks.size()); // total number
			informations.add(todayTracks.intValue() / tracks.size() * 100); // percent
			informations.add(todayTracks.intValue());
			informations.add(tracks.stream().filter(t -> t.getDate() == LocalDate.now().minusDays(1).toString())
					.collect(Collectors.toList()).size());
			informations.add(tracks.stream().filter(t -> t.getDate() == LocalDate.now().minusDays(2).toString())
					.collect(Collectors.toList()).size());
			informations.add(tracks.stream().filter(t -> t.getDate() == LocalDate.now().minusDays(3).toString())
					.collect(Collectors.toList()).size());
			informations.add(tracks.stream().filter(t -> t.getDate() == LocalDate.now().minusDays(4).toString())
					.collect(Collectors.toList()).size());
			informations.add(tracks.stream().filter(t -> t.getDate() == LocalDate.now().minusDays(5).toString())
					.collect(Collectors.toList()).size());
			informations.add(tracks.stream().filter(t -> t.getDate() == LocalDate.now().minusDays(6).toString())
					.collect(Collectors.toList()).size());
		}
		return informations;
	}

	public String countCinemasInRepository() {
		return "Cinemas: " + cinemasRepository.count() + "<br> Repertories: " + repertoriesRepository.count()
				+ "<br> Movies: " + moviesRepository.count() + "<br> Shows: " + movieShowsRepository.count();
	}

	public String setCinemaUserInRepository(Long cinemaId, String userId) {
		Cinema cinema = cinemasRepository.findOne(cinemaId);
		User user = usersRepository.findOne(userId);

		if (user != null && cinema != null) {
			cinema.setUser(user);
			// user.getCinemas().add(cinema);
			cinema = cinemasRepository.save(cinema);
			user = usersRepository.save(user);

			return "Succesfull setting: " + cinema.getName() + " owner:" + user.getName();
		} else if (user == null)
			return "No user with id " + userId;
		else
			return "No cinema with id " + cinemaId;
	}

	public String setMovieUserInRepository(String movieName, String userId) {
		Movie movie = moviesRepository.findOne(movieName);
		User user = usersRepository.findOne(userId);

		if (user != null && movie != null) {
			movie.setUser(user);
			// user.getMovies().add(movie);
			movie = moviesRepository.save(movie);
			user = usersRepository.save(user);

			return "Succesfull setting: " + movie.getName() + " owner:" + user.getName();
		} else if (user == null)
			return "No user with id " + userId;
		else
			return "No movie with id " + movieName;
	}

	public Movie getMovieDetailsFromRepository(String name) {
		Movie movie = moviesRepository.findOne(name);
		tracksRepository.save(new Track(movie, "get movie details"));
		System.out.println(tracksRepository.countByMovie_Name(name));

		return movie;
	}

	public List<Cinema> getAllCinemasFromRepository(String origin, String city) {
		List<Cinema> allCinemas;

		if (city.equals("null"))
			allCinemas = cinemasRepository.findAll();
		else
			allCinemas = cinemasRepository.findByNameContaining(city);

		if (!origin.equals("null")) {
			allCinemas = allCinemas.stream().peek(c -> {

				RestTemplate rt = new RestTemplate();

				String url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" + origin
						+ "&destinations=" + c.getAdress() + "&key=AIzaSyAuHrswGn4HBW1NqT2Fng0Vo7Za7DOc52M";

				try {
					c.setMatrixDistance(rt.getForObject(url, MatrixDistance.class));
				} catch (Exception e) {
					e.printStackTrace();
				}

			}).sorted((c1, c2) -> Integer.compare(
					c1.getMatrixDistance().getRows().get(0).getElements().get(0).getDistance().getValue(),
					c2.getMatrixDistance().getRows().get(0).getElements().get(0).getDistance().getValue()))
					.collect(Collectors.toList());
		}
		return allCinemas;
	}

	public String parseDate(String date) {
		try {
			date = LocalDate.parse(date).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		} catch (DateTimeParseException e) {

		} catch (Exception e) {
			e.printStackTrace();
		}

		return date;
	}

	public List<Movie> getCinemaMoviesFromRepository(Long cinemaId, String date, Boolean hot, Boolean mark) {

		List<Movie> movies = new ArrayList<Movie>();

		if (Objects.isNull(cinemaId)) {
			if (date.equals("null")) {
				movies.addAll(moviesRepository.findAll());
			} else {
				movies.addAll(moviesRepository.findByMovieShows_Repertory_Date(date));
			}
		} else {
			tracksRepository.save(new Track(cinemasRepository.findOne(cinemaId), "get cinema movies"));

			if (date.equals("null")) {
				movies.addAll(moviesRepository.findByMovieShows_Repertory_CinemaId(cinemaId));
			} else {
				movies.addAll(moviesRepository.findByMovieShows_Repertory_CinemaIdAndMovieShows_Repertory_date(cinemaId,
						parseDate(date)));
			}

		}

		if (mark)
			movies.sort((m2, m1) -> Float.compare(m1.getFilmwebAverage(), m2.getFilmwebAverage()));
		else if (hot) {
			movies = movies.stream().sorted((m2, m1) -> Long.compare(tracksRepository.countByMovie_Name(m1.getName()),
					tracksRepository.countByMovie_Name(m2.getName()))).collect(Collectors.toList());
			movies.stream().forEach(
					m -> System.out.println(m.getName() + " " + tracksRepository.countByMovie_Name(m.getName())));
		}

		return movies;
	}

	public List<MovieShow> getMovieShowsFromRepository(Long cinemaId, String date, String movieName) {
		tracksRepository.save(
				new Track(cinemasRepository.findOne(cinemaId), moviesRepository.findOne(movieName), "get movie shows"));

		return movieShowsRepository.findByRepertory_CinemaIdAndRepertory_DateAndMovieName(cinemaId, date, movieName);
	}

	public List<Cinema> getCinemasWithNoUser() {
		return cinemasRepository.findByUserIsNull();
	}

	public List<Movie> getMoviesWithNoUser() {
		return moviesRepository.findByUserIsNull();
	}

	public List<Cinema> getUserCinemas(String name) {
		return cinemasRepository.findByUserName(name);
	}

	public List<Movie> getPartnerMovies(String name) {
		return moviesRepository.findByUserName(name);
	}

	public int getUpdatingProgress() {
		return updatingProgress;
	}

	public void setUpdatingProgress(int updatingProgress) {
		this.updatingProgress = updatingProgress;
	}

	public List<MovieShow> getMovieShowsFromRepository(String movieName) {

		return movieShowsRepository.findByMovie(moviesRepository.findOne(movieName));

	}

	public List<Movie> getHotMovies() {
		return moviesRepository.findAllOrderByTracksCountDesc();
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {

		/*
		 * try { this.updateCinemas(); } catch (InterruptedException e) {
		 * e.printStackTrace(); }
		 */
	}

	public List<Movie> getTopMarkMovies() {
		return moviesRepository.findAllOrderByfilmwebAverage();
	}

	public String deleteSpacesAtTheEndOfTheGivenString(String string) {

		if (string != null) {
			try {
				for (int i = (string.length() - 1); i >= 0; i--) {
					if (string.charAt(i) == ' ')
						string = string.substring(0, i);
					else
						break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return string;
	}

}
