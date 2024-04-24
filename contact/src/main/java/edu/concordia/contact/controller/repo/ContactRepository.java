package edu.concordia.contact.controller.repo;

import edu.concordia.contact.controller.entities.address.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends MongoRepository<Contact, String> {


    @Query("{createdBy : ?1,'$or':[{'firstName':{$regex:?0,$options:'i'}},{'lastName':{$regex:?0,$options:'i'}},{'email':{$regex:?0,$options:'i'}}]}")
    public Page<Contact> listContactsByCreatedBy(String searchWord, String userUuid, Pageable pageable) ;

    @Query("{createdBy : ?0}")
    public Page<Contact> listContactsByCreatedBy(String userUuid, Pageable pageable) ;

    @Query(value = "{}",count = true)
    public Long countContacts() ;

    @Query("{ 'uuid' : ?0 }")
    public Contact findByUuid(String uuid);

    public void deleteAllByUuid(String uuid);


}
