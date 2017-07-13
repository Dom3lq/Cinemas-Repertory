package Repositories;

import java.util.List;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import POJOs.Repertory;

@Repository
public interface RepertoriesRepository extends CrudRepository<Repertory, Long> {
	
	@Override
	@CachePut(cacheNames = "repertories", key = "#result.date + #result.cinema.name")
	<S extends Repertory> S save (S repertory);

	@Override
	@Cacheable(value = "repertories")
	List<Repertory> findAll();

	@Cacheable(value = "repertories")
	List<Repertory> findByDate(String date);

	@Cacheable(value = "repertories", key = "#p0 + #p1")
	Repertory findOneByDateAndCinemaName(String date, String name);
}