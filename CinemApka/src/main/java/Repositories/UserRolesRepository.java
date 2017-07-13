package Repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import POJOs.UserRole;

@Repository
public interface UserRolesRepository extends CrudRepository<UserRole, Long>{
	
	List<UserRole> findByUserName(String name);
}
