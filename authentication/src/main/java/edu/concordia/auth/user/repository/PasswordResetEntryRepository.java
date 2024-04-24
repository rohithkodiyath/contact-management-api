package edu.concordia.auth.user.repository;

import edu.concordia.auth.user.entity.AppUser;
import edu.concordia.auth.user.entity.PasswordResetEntry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface PasswordResetEntryRepository extends MongoRepository<PasswordResetEntry, String> {

    @Query("{ 'emailAddress' : ?0 }")
    public AppUser findByEmailAddress(String emailAddress);

    @Query("{ 'user' : ?0 }")
    public PasswordResetEntry findByUser(AppUser user);

    @Query(value = "{ 'user' : ?0 }", delete = true)
    public void deleteAllByUser(AppUser user);
}
