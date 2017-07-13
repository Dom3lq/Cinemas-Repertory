package Repositories;

import java.util.List;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import POJOs.Cinema;
import POJOs.Repertory;

@Repository
public interface CinemasRepository extends CrudRepository<Cinema, Long> {

	@Override
	@CachePut(cacheNames = "cinemas", key = "#result.name")
	<S extends Cinema> S save(S cinema);

	@Override
	@Cacheable(value = "cinemas")
	List<Cinema> findAll();

	@Cacheable(value = "cinemas", key = "#p0")
	Cinema findOneByName(String name);

	@Cacheable(value = "cinemas")
	List<Cinema> findByNameContaining(String name);

	@Cacheable(value = "cinemas")
	List<Cinema> findByUserIsNull();

	@Cacheable(value = "cinemas")
	List<Cinema> findByUserName(String name);

}