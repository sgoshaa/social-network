package ru.skillbox.diplom.mappers;

import javax.annotation.processing.Generated;
import ru.skillbox.diplom.model.PersonDTO;
import ru.skillbox.diplom.model.api.response.LoginResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-09-22T18:26:27+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.11 (Amazon.com Inc.)"
)
public class LoginResponseMapperImpl implements LoginResponseMapper {

    @Override
    public LoginResponse toLoginResponse(PersonDTO data) {
        if ( data == null ) {
            return null;
        }

        LoginResponse loginResponse = new LoginResponse();

        loginResponse.setData( data );

        loginResponse.setTimestamp( java.time.LocalDateTime.now() );

        return loginResponse;
    }
}
