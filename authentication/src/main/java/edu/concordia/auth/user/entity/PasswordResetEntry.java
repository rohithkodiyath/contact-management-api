package edu.concordia.auth.user.entity;

import edu.concordia.auth.user.model.UserModel;
import edu.concordia.shared.entitiy.CoreEntity;
import edu.concordia.shared.model.CoreModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Document
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PasswordResetEntry extends CoreEntity{

    @Id
    private String id;

    @DBRef
    private AppUser user;

    @Field("token")
    private String token;

    @Override
    public CoreModel toModel() {
        return null;
    }
}
