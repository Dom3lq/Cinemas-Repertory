package Repositories;

import java.util.List;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import POJOs.Movie;

@Repository
public interface MoviesRepository extends CrudRepository<Movie, String> {
	
	@Override
	@CachePut(cacheNames = "movies", key = "#result.name")
	<S extends Movie> S save(S movie);
	
	@Cacheable(value = "movies")
	@Query("SELECT movie FROM Movie movie ORDER BY movie.tracks.size DESC")
	List<Movie> findAllOrderByTracksCountDesc();
	    
	@Override
	@Cacheable(value = "movies")
    List<Movie> findAll();
    
    @Override
	@Cacheable(value = "movies", key = "#p0")
    Movie findOne(String id);
        
    @Cacheable(value = "movies")
    List<Movie> findByMovieShows_Repertory_CinemaIdAndMovieShows_Repertory_date(Long cinemaId, String date);
    
    @Cacheable(value = "movies")
    List<Movie> findByMovieShows_Repertory_CinemaId(Long cinemaId);
    
    @Cacheable(value = "movies")
    List<Movie> findByMovieShows_Repertory_Date(String date);
    
    @Cacheable(value = "movies")
    List<Movie> findByUserIsNull();
    
    @Cacheable(value = "movies", key = "#p0")
    List<Movie> findByUserName(String name);

    @Cacheable(value = "movies")
    @Query("SELECT movie FROM Movie movie ORDER BY movie.filmwebAverage DESC")
	List<Movie> findAllOrderByfilmwebAverage();       
}
