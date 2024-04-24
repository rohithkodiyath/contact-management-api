package edu.concordia.shared.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseModel<T> extends RepresentationModel<ResponseModel<T>> {

    private T data;

    public static <T> ResponseModel<T> create(T data){
        return new ResponseModel<>(data);
    }

}
