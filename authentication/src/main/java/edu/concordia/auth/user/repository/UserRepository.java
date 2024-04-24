package edu.concordia.auth.user.repository;

import edu.concordia.auth.user.entity.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<AppUser, String> {

    @Query("{ 'emailAddress' : ?0 }")
    public AppUser findByEmailAddress(String emailAddress);


    @Query("{ 'uuid' : ?0 }")
    AppUser findUserByUuid(String userUuid);
}
