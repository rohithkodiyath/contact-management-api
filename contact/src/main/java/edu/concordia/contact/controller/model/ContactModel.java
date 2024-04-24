package edu.concordia.contact.controller.model;

import edu.concordia.contact.controller.entities.address.Address;
import edu.concordia.contact.controller.entities.address.Contact;
import edu.concordia.shared.model.CoreModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@Builder
public final class ContactModel extends CoreModel<Contact> {


    private String uuid;

    @NotBlank(message = "First Name is mandatory")
    @Size(min = 2, max = 50, message = "First Name must be between 2 and 50 characters long")
    private String firstName;

    @NotBlank(message = "Last name is mandatory")
    @Size(min = 2, max = 50, message = "Last Name must be between 2 and 50 characters long")
    private String lastName;

    @NotBlank(message = "Email is mandatory")
    @Size(min = 11, max = 50, message = "Email length be between 2 and 50 characters long")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Phone is mandatory")
    @Pattern(regexp = "^\\+1\\s\\([0-9]{3}\\)\\s[0-9]{3}-[0-9]{4}$", message = "Phone should be in the format +1 (xxx) xxx-xxxx")
    private String phone;

    @Valid
    private AddressModel address;


    private String company;


    @Pattern(regexp = "^(http|https):\\/\\/.*$", message = "Website should be a valid URL")
    private String website;

    public static enum SortKey {
        FIRST_NAME ("firstName"),
        LAST_NAME("lastName"),
        EMAIL("email"),
        CREATED_DATE("createdDate");

        private final String value;

        SortKey(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }

        public static SortKey fromValue(String value) {
            for (SortKey sortKey : SortKey.values()) {
                if (sortKey.value.equals(value)) {
                    return sortKey;
                }
            }
            return null;
        }
    }

    @Override
    public Contact toEntity() {
        return Contact.builder()
                .firstName(this.firstName)
                .lastName(this.lastName)
                .email(this.email)
                .website(this.website)
                .company(this.company)
                .phone(this.phone)
                .address(this.address.convertToEntity())
                .build();
    }
}
