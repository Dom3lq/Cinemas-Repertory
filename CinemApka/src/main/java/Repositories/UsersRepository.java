package Repositories;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import POJOs.User;

@Repository
@Cacheable("users")
public interface UsersRepository extends CrudRepository<User, String> {

	@Override
	User findOne(String id);
}
