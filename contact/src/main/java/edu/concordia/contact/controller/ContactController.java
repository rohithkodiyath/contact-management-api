package edu.concordia.contact.controller;

import edu.concordia.auth.user.services.UserService;
import edu.concordia.contact.controller.entities.address.Contact;
import edu.concordia.contact.controller.model.ContactModel;
import edu.concordia.contact.controller.model.response.ListContactResponseModel;
import edu.concordia.contact.service.ContactService;
import edu.concordia.shared.errors.ContactNotFoundException;
import edu.concordia.shared.model.response.GenericResponseEntity;
import edu.concordia.shared.model.response.ResponseModel;
import jakarta.validation.Valid;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static edu.concordia.shared.entitiy.constants.Constants.API_VERSION;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController("contact-management")
@RequestMapping("/"+API_VERSION+"/contact")
public class ContactController {

    @Autowired
    private  ContactService contactService;

    @Autowired
    private UserService userService;



    /**
     * Creates a new contact.
     * <p>
     * This method takes a ContactModel object as input and returns a response entity.
     * </p>
     *
     * @param contactModel The contact model.
     * @return A response entity with the created contact's UUID.
     * @throws Exception if any error occurs during the creation of the contact.
     * @see ContactModel
     */
    @SneakyThrows
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public GenericResponseEntity<Map<String,String>> createContact(@Valid @RequestBody ContactModel contactModel) {

        Contact contact = contactService.saveContact(contactModel.toEntity());
        GenericResponseEntity<Map<String,String>> returnModel =
                GenericResponseEntity.success(Collections.singletonMap("uuid", contact.getUuid()));
        returnModel.add(linkTo(methodOn(ContactController.class).createContact(contactModel)).withSelfRel());
        returnModel.add(linkTo(methodOn(ContactController.class).getContact(contactModel.getUuid())).withRel("get_contact"));
        returnModel.add(linkTo(methodOn(ContactController.class).deleteContact(contactModel.getUuid())).withRel("delete_contact"));
        return returnModel;
    }

    /**
     * Updates an existing contact.
     * <p>
     * This method takes a ContactModel object as input and returns a response entity.
     * </p>
     *
     * @param contactModel The contact model.
     * @return A response entity with the updated contact's UUID.
     * @throws ContactNotFoundException if the contact to be updated is not found.
     * @see ContactModel
     */
    @PutMapping()
    @ResponseStatus(HttpStatus.ACCEPTED)
    public GenericResponseEntity<Map<String,String>> updateContact(@Valid @RequestBody ContactModel contactModel) throws ContactNotFoundException {
        Contact contact = contactService.updateContact(contactModel);
        Map<String, String> map = new HashMap<>();
        System.out.println("Contact updated successfully"+contact.getUuid());
        map.put("uuid", contact.getUuid());
        map.put("message", "Contact updated successfully");
        GenericResponseEntity<Map<String,String>> returnModel =
                GenericResponseEntity.success(map);
        returnModel.setMessage("Contact updated successfully");
        returnModel.add(linkTo(methodOn(ContactController.class).updateContact(contactModel)).withSelfRel());
        returnModel.add(linkTo(methodOn(ContactController.class).getContact(contact.getUuid())).withRel("get_contact"));
        returnModel.add(linkTo(methodOn(ContactController.class).deleteContact(contact.getUuid())).withRel("delete_contact"));
        return returnModel;
    }

    /**
     * Lists all contacts.
     * <p>
     * This method takes page number and size as input and returns a list of contacts.
     * </p>
     *
     * @param page The page number.
     * @param size The page size.
     * @return A list of contacts.
     * @throws Exception if any error occurs during the retrieval of the contacts.
     */
    @GetMapping("/list")
    public ListContactResponseModel listContact(@RequestParam(value = "p",defaultValue = "0") int page,
                                                @RequestParam(value = "q",defaultValue = "") String key,
                                                @RequestParam(value = "sortKey",defaultValue = "") String sortKey,
                                                @RequestParam(value = "sortOrder",defaultValue = "asc") String sortOrder,
                                                @RequestParam(value = "s",defaultValue = "10") int size) throws Exception {

        System.out.println("Logged user: "+userService.loggedInUser());
        ContactModel.SortKey sortKeyEnum = sortKey.isEmpty() ?  ContactModel.SortKey.CREATED_DATE :ContactModel.SortKey.fromValue(sortKey);

        var contacts = contactService.listContacts(key, sortKeyEnum, sortOrder, page,size);
        //var count = contactService.countContacts();
        var models = contacts.map(Contact::toModel).toList();
        var listModel = new ListContactResponseModel();
        for (var contactModel : models) {
            var contactResponseModel = new ResponseModel<>(contactModel);
            contactResponseModel.add(linkTo(methodOn(ContactController.class).getContact(contactModel.getUuid())).withRel("get_contact"));
            contactResponseModel.add(linkTo(methodOn(ContactController.class).deleteContact(contactModel.getUuid())).withRel("delete_contact"));
            contactResponseModel.add(linkTo(methodOn(ContactController.class).updateContact(contactModel)).withRel("update_contact"));
            listModel.addContact(contactResponseModel);
        }
        listModel.setTotalPages(contacts.getTotalPages());
        return listModel;
    }

    /**
     * Gets a specific contact by its UUID.
     * <p>
     * This method takes UUID as input and returns the contact.
     * </p>
     *
     * @param uuid The UUID of the contact.
     * @return The contact model.
     * @throws ContactNotFoundException if the contact is not found.
     * @see ContactModel
     */
    @GetMapping("/{uuid}")
    public ResponseModel<ContactModel> getContact(@PathVariable("uuid") String uuid) throws ContactNotFoundException {
        Contact contact = contactService.findByUuid(uuid);
        if(contact == null){
            throw new ContactNotFoundException(uuid);
        }
        var returnModel = ResponseModel.create(contact.toModel());
        returnModel.add(linkTo(methodOn(ContactController.class).getContact(contact.getUuid())).withSelfRel());
        return returnModel;
    }

    /**
     * Deletes a specific contact by its UUID.
     * <p>
     * This method takes UUID as input and returns a response entity.
     * </p>
     *
     * @param uuid The UUID of the contact.
     * @return A response entity indicating the deletion result.
     * @throws ContactNotFoundException if the contact to be deleted is not found.
     */
    @DeleteMapping("/{uuid}")
    public GenericResponseEntity<Map<String,String>> deleteContact(@PathVariable("uuid") String uuid) throws ContactNotFoundException {
        Contact contact = contactService.findByUuid(uuid);
        if(contact == null){
            throw new ContactNotFoundException(uuid);
        }
        contactService.deleteContact(uuid);
        GenericResponseEntity<Map<String,String>> returnModel =
                GenericResponseEntity.success(null);
        returnModel.setMessage("Contact deleted successfully");
        returnModel.add(linkTo(methodOn(ContactController.class).updateContact(null)).withSelfRel());
        returnModel.add(linkTo(methodOn(ContactController.class).getContact(null)).withRel("get_contact"));
        returnModel.add(linkTo(methodOn(ContactController.class).getContact(null)).withRel("delete_contact"));
        return returnModel;
    }
}
