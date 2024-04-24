package edu.concordia.contact.service;

import edu.concordia.contact.controller.entities.address.Contact;
import edu.concordia.contact.controller.model.ContactModel;
import edu.concordia.shared.errors.ContactNotFoundException;
import org.springframework.data.domain.Page;

public interface ContactService {

    /**
     * Lists all contacts.
     *
     * @param key
     * @param sortKey
     * @param sortOrder
     * @param page      The page number.
     * @param size      The page size.
     * @return A page of contacts.
     */
    public Page<Contact> listContacts(String key, ContactModel.SortKey sortKey, String sortOrder, int page, int size);

    /**
     * Finds a contact by its UUID.
     *
     * @param uuid The UUID of the contact.
     * @return The contact.
     */
    public Contact findByUuid(String uuid);

    /**
     * Saves a contact.
     *
     * @param contact The contact to save.
     * @return The saved contact.
     */
    public Contact saveContact(Contact contact);

    /**
     * Updates a contact.
     *
     * @param contactModel The contact model.
     * @return The updated contact.
     * @throws ContactNotFoundException if the contact to be updated is not found.
     */
    Contact updateContact(ContactModel contactModel) throws ContactNotFoundException;

    /**
     * Counts all contacts.
     *
     * @return The total number of contacts.
     */
    public Long countContacts();

    /**
     * Deletes a contact by its UUID.
     *
     * @param uuid The UUID of the contact.
     */
    public void deleteContact(String uuid);

}
