package ru.skillbox.diplom.mappers;

import javax.annotation.processing.Generated;
import ru.skillbox.diplom.model.User;
import ru.skillbox.diplom.model.api.request.RegisterRequest;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-09-22T18:26:27+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.11 (Amazon.com Inc.)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUserEntity(RegisterRequest registerRequest, String encryptedPassword) {
        if ( registerRequest == null && encryptedPassword == null ) {
            return null;
        }

        User user = new User();

        if ( registerRequest != null ) {
            user.setFirstName( registerRequest.getFirstName() );
            user.setEmail( registerRequest.getEmail() );
        }
        if ( encryptedPassword != null ) {
            user.setPassword( encryptedPassword );
        }
        user.setType( ru.skillbox.diplom.model.enums.ModerationType.USER );

        return user;
    }
}
