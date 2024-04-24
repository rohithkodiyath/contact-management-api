package edu.concordia.contact.controller.model.response;

import edu.concordia.contact.controller.model.ContactModel;
import edu.concordia.shared.model.response.ResponseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListContactResponseModel {

    private List<ResponseModel<ContactModel>> contacts = new ArrayList<ResponseModel<ContactModel>>() ;

    private Integer totalPages;

    public void addContact(ResponseModel<ContactModel> contact){
        contacts.add(contact);
    }


}
