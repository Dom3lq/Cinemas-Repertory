package Repositories;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import POJOs.Track;

@Repository
public interface TracksRepository extends CrudRepository<Track, Long> {

	@Override
	@Cacheable(value = "tracks")
	List<Track> findAll();
	
	@Cacheable(value = "tracks")
	List<Track> findByCinema_NameOrMovie_Name(String name, String name2);
	
	Long countByMovie_Name(String name);
}
