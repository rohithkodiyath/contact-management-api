package edu.concordia.shared.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenericResponseEntity<T> extends RepresentationModel<GenericResponseEntity<T>> {

    private T data;

    private Boolean isSuccess = true;

    private String message;

    public static <T> GenericResponseEntity<T> success(T data, String message){
        return new GenericResponseEntity<>(data, true, message);
    }

    public static <T> GenericResponseEntity<T> success(T data){
        return new GenericResponseEntity<>(data, true, null);
    }

}
