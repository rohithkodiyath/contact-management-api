package edu.concordia.contact.controller.model;

import edu.concordia.contact.controller.entities.address.Address;
import edu.concordia.shared.model.CoreModel;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AddressModel  {

    @Pattern(regexp = "^[0-9]{1,5}$", message = "Unit number must be between 1 and 5 digits long")
    private String unitNumber;

    @NotNull(message = "Civic number is mandatory")
    @Pattern(regexp = "^[0-9]{1,5}$", message = "Civic number must be between 1 and 5 digits long")
    private String civicNumber;

    @NotBlank(message = "Street is mandatory")
    @Size(min = 2, max = 50, message = "Street must be between 2 and 50 characters long")
    private String street;

    @NotBlank(message = "City is mandatory")
    @Size(min = 2, max = 50, message = "City must be between 2 and 50 characters long")
    private String city;

    @NotBlank(message = "Province is mandatory")
    @Size(min = 2, max = 50, message = "Province must be between 2 and 50 characters long")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Province must be 2 characters long and in uppercase")
    private String province;

    @NotBlank(message = "Postal Code is mandatory")
    @Size(min = 7, max = 7, message = "Postal code must be 6 characters long")
    @Pattern(regexp = "^[A-Z]\\d[A-Z] \\d[A-Z]\\d$", message = "Postal code must be in the format A1A 1A1")
    private String postalCode;


    public Address convertToEntity() {
        return Address.builder()
                .unitNumber(this.unitNumber)
                .civicNumber(this.civicNumber)
                .street(this.street)
                .city(this.city)
                .province(this.province)
                .postalCode(this.postalCode)
                .build();
    }
}
