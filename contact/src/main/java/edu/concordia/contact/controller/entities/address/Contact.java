package edu.concordia.contact.controller.entities.address;

import edu.concordia.contact.controller.model.ContactModel;
import edu.concordia.shared.entitiy.CoreEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "contacts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contact extends CoreEntity<ContactModel>{

    @Id
    private String id;

    @Field("firstName")
    private String firstName;

    @Field("lastName")
    private String lastName;

    @Field("company")
    private String company;

    @Field("phone")
    private String phone;

    @Field("email")
    private String email;

    @Field("website")
    private String website;

    @Field("address")
    private Address address;

    @Override
    public ContactModel toModel() {
        return ContactModel.builder()
                .firstName(this.firstName)
                .lastName(this.lastName)
                .email(this.email)
                .company(this.company)
                .website(this.website)
                .uuid(this.getUuid())
                .phone(this.phone)
                .address(this.address.toModel())
                .build();
    }
}
