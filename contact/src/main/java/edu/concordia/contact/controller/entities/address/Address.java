package edu.concordia.contact.controller.entities.address;

import edu.concordia.contact.controller.model.AddressModel;
import edu.concordia.shared.entitiy.CoreEntity;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address{

    @Field("unitNumber")
    private String unitNumber;

    @Field("civicNumber")
    private String civicNumber;

    @Field("street")
    private String street;

    @Field("city")
    private String city;

    @Field("province")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Province must be 2 characters long")
    private String province;

    @Field("postalCode")
    @Pattern(regexp = "^[A-Z][0-9][A-Z] [0-9][A-Z][0-9]$", message = "Postal code must be in the format of A1A 1A1")
    private String postalCode;

    public AddressModel toModel(){
        return AddressModel.builder()
                .unitNumber(this.unitNumber)
                .civicNumber(this.civicNumber)
                .street(this.street)
                .city(this.city)
                .province(this.province)
                .postalCode(this.postalCode)
                .build();
    }



}
