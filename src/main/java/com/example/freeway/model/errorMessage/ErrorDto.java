package com.example.freeway.model.errorMessage;

import com.example.freeway.db.entity.ErrorMessage;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDto {

    String field;
    String ru;
    String kg;
    String eng;

    public static ErrorDto from(ErrorMessage errorMessage) {
        return ErrorDto.builder()
                .field(errorMessage.getField())
                .ru(errorMessage.getMessageRu())
                .kg(errorMessage.getMessageKg())
                .eng(errorMessage.getMessageEng())
                .build();
    }

}
