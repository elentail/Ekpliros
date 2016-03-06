package org.roadking.repository;

import org.roadking.model.Users;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Qualifier(value = "userRepository")
public interface UserRepository extends CrudRepository<Users, Long> {

	public Users findByUsername(String username);
}
