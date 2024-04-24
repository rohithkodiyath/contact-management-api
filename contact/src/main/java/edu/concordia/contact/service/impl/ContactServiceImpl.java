package edu.concordia.contact.service.impl;

import edu.concordia.auth.user.services.UserService;
import edu.concordia.contact.controller.entities.address.Contact;
import edu.concordia.contact.controller.model.ContactModel;
import edu.concordia.contact.controller.repo.ContactRepository;
import edu.concordia.contact.service.ContactService;
import edu.concordia.shared.errors.ContactNotFoundException;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserService userService;

    @Override
    public Page<Contact> listContacts(String key, ContactModel.SortKey sortKey, String sortOrder, int page, int size) {

        var sortBy = Sort.by(sortKey.getValue());
        var sort = sortOrder.equals("asc") ? sortBy.ascending() : sortBy.descending();

        PageRequest pageRequest = PageRequest.of(page, size, sort);
        if (key == null || key.isEmpty()) {
            return contactRepository.listContactsByCreatedBy(userService.loggedInUser().getUuid(), pageRequest);
        }
        return contactRepository.listContactsByCreatedBy(key, userService.loggedInUser().getUuid(), pageRequest);
    }

    @Override
    public Contact findByUuid(String uuid) {
        return contactRepository.findByUuid(uuid);
    }

    @Override
    public Contact saveContact(Contact contact) {
        return contactRepository.save(contact);
    }

    @Override
    public Contact updateContact(ContactModel contactModel) throws ContactNotFoundException {
        Contact contact = this.findByUuid(contactModel.getUuid());
        if(contact == null){
            throw new ContactNotFoundException(contact.getUuid());
        }
        contact.setFirstName(contactModel.getFirstName());
        contact.setLastName(contactModel.getLastName());
        contact.setEmail(contactModel.getEmail());
        contact.setPhone(contactModel.getPhone());
        contact.setAddress(contactModel.getAddress().convertToEntity());
        contact.setCompany(contactModel.getCompany());
        contact.setWebsite(contactModel.getWebsite());
        return contactRepository.save(contact);
    }

    @Override
    public Long countContacts() {
        return contactRepository.countContacts();
    }

    @Override
    public void deleteContact(String uuid) {
         contactRepository.deleteAllByUuid(uuid);
    }

}
