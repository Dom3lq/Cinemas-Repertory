package Repositories;

import java.util.List;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import POJOs.Movie;
import POJOs.MovieShow;

@Repository
public interface MovieShowsRepository extends CrudRepository<MovieShow, Long> {
	
	@Override
	@CachePut(cacheNames = "movieShows", key = "#result.time + #result.repertory.id + #result.movie.name")
	<S extends MovieShow> S save(S movieShow);
	
	@Override
	@Cacheable(value = "movieShows")
    List<MovieShow> findAll();
    
    @Cacheable(value = "movieShows")
    List<MovieShow> findByRepertory_CinemaIdAndRepertory_DateAndMovieName(Long cinemaId, String date, String name2);
    
    Long countByRepertory_Date(String date);
    
    void deleteByRepertory_Date(String date);
    
    @Cacheable(value = "movieShows", key = "#p0 + #p1 + #p2")
    MovieShow findOneByTimeAndRepertoryIdAndMovie_name(String time, Long repertoryId, String name);
    
    @Cacheable(value = "movieShows")
	List<MovieShow> findByMovie(Movie movie);
}