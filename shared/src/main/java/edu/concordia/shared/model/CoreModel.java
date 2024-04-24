package edu.concordia.shared.model;

import edu.concordia.shared.entitiy.CoreEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;


public abstract class CoreModel <T extends CoreEntity> {

    public abstract T toEntity();

}
