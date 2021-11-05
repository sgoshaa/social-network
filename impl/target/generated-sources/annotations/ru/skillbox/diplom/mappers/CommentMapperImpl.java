package ru.skillbox.diplom.mappers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import ru.skillbox.diplom.mappers.converter.DateConverter;
import ru.skillbox.diplom.model.CommentDTO;
import ru.skillbox.diplom.model.Person;
import ru.skillbox.diplom.model.PersonDTO;
import ru.skillbox.diplom.model.Post;
import ru.skillbox.diplom.model.PostComment;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-09-22T18:26:26+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.11 (Amazon.com Inc.)"
)
public class CommentMapperImpl implements CommentMapper {

    private final DateConverter dateConverter = new DateConverter();

    @Override
    public CommentDTO convert(PostComment comment) {
        if ( comment == null ) {
            return null;
        }

        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setPostId( commentPostId( comment ) );
        commentDTO.setTime( dateConverter.convertRegDate( comment.getTime() ) );
        commentDTO.setAuthor( personToPersonDTO( comment.getPerson() ) );
        commentDTO.setParentId( commentParentIdId( comment ) );
        commentDTO.setCommentText( comment.getCommentText() );
        if ( comment.getId() != null ) {
            commentDTO.setId( comment.getId() );
        }
        commentDTO.setDeleted( comment.isDeleted() );
        commentDTO.setSubComments( postCommentListToCommentDTOList( comment.getSubComments() ) );

        commentDTO.setLikes( comment.getLikesAmount() );

        return commentDTO;
    }

    private Integer commentPostId(PostComment postComment) {
        if ( postComment == null ) {
            return null;
        }
        Post post = postComment.getPost();
        if ( post == null ) {
            return null;
        }
        Integer id = post.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected PersonDTO personToPersonDTO(Person person) {
        if ( person == null ) {
            return null;
        }

        PersonDTO personDTO = new PersonDTO();

        personDTO.setId( person.getId() );
        personDTO.setFirstName( person.getFirstName() );
        personDTO.setLastName( person.getLastName() );
        personDTO.setRegDate( person.getRegDate() );
        personDTO.setBirthDate( person.getBirthDate() );
        personDTO.setEmail( person.getEmail() );
        personDTO.setPhone( person.getPhone() );
        personDTO.setPhoto( person.getPhoto() );
        personDTO.setAbout( person.getAbout() );
        if ( person.getMessagesPermission() != null ) {
            personDTO.setMessagesPermission( person.getMessagesPermission().name() );
        }
        personDTO.setLastOnlineTime( person.getLastOnlineTime() );

        return personDTO;
    }

    private Integer commentParentIdId(PostComment postComment) {
        if ( postComment == null ) {
            return null;
        }
        PostComment parentId = postComment.getParentId();
        if ( parentId == null ) {
            return null;
        }
        Integer id = parentId.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected List<CommentDTO> postCommentListToCommentDTOList(List<PostComment> list) {
        if ( list == null ) {
            return null;
        }

        List<CommentDTO> list1 = new ArrayList<CommentDTO>( list.size() );
        for ( PostComment postComment : list ) {
            list1.add( convert( postComment ) );
        }

        return list1;
    }
}
