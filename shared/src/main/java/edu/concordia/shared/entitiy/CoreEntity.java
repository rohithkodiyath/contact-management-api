package edu.concordia.shared.entitiy;


import edu.concordia.shared.model.CoreModel;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;


@Data
@ToString
public abstract class CoreEntity <M extends CoreModel> implements Serializable {

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    @Field("uuid")
    @Indexed(unique = true, name = "uuid_index")
    private String uuid = UUID.randomUUID().toString();

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;

    public abstract M toModel();

//    @LastModifiedDate
//    private User updatedAt;





}
