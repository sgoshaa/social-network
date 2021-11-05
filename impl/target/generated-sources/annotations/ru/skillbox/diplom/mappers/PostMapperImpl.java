package ru.skillbox.diplom.mappers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.mapstruct.factory.Mappers;
import ru.skillbox.diplom.mappers.converter.DateConverter;
import ru.skillbox.diplom.model.CommentDTO;
import ru.skillbox.diplom.model.Notification;
import ru.skillbox.diplom.model.Person;
import ru.skillbox.diplom.model.PersonDTO;
import ru.skillbox.diplom.model.Post;
import ru.skillbox.diplom.model.PostComment;
import ru.skillbox.diplom.model.PostDTO;
import ru.skillbox.diplom.model.Tag;
import ru.skillbox.diplom.model.User;
import ru.skillbox.diplom.model.UserSetting;
import ru.skillbox.diplom.model.api.request.PostRequest;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-09-22T18:26:27+0300",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.11 (Amazon.com Inc.)"
)
public class PostMapperImpl implements PostMapper {

    private final DateConverter dateConverter = new DateConverter();
    private final CommentMapper commentMapper = Mappers.getMapper( CommentMapper.class );
    private final PersonMapper personMapper = Mappers.getMapper( PersonMapper.class );
    private final TagMapper tagMapper = Mappers.getMapper( TagMapper.class );

    @Override
    public PostDTO convert(Post post, PersonDTO personDTO) {
        if ( post == null && personDTO == null ) {
            return null;
        }

        PostDTO postDTO = new PostDTO();

        if ( post != null ) {
            postDTO.setId( post.getId() );
            postDTO.setTime( dateConverter.convertRegDate( post.getTime() ) );
            postDTO.setTitle( post.getTitle() );
            postDTO.setPostText( post.getPostText() );
            postDTO.setTags( tagListToStringList( post.getTags() ) );
            postDTO.setPostComments( postCommentListToCommentDTOList( post.getPostComments() ) );
        }
        if ( personDTO != null ) {
            postDTO.setAuthor( personDTO );
        }
        postDTO.setBlocked( post.isBlocked() );
        postDTO.setLikes( post.getLikesAmount() );

        return postDTO;
    }

    @Override
    public PostDTO convert(Post post) {
        if ( post == null ) {
            return null;
        }

        PostDTO postDTO = new PostDTO();

        postDTO.setTime( dateConverter.convertRegDate( post.getTime() ) );
        postDTO.setId( post.getId() );
        postDTO.setAuthor( personMapper.toPersonDTO( post.getAuthor() ) );
        postDTO.setTitle( post.getTitle() );
        postDTO.setPostText( post.getPostText() );
        postDTO.setBlocked( post.isBlocked() );
        postDTO.setTags( tagListToStringList( post.getTags() ) );
        postDTO.setPostComments( postCommentListToCommentDTOList( post.getPostComments() ) );

        postDTO.setLikes( post.getLikesAmount() );

        return postDTO;
    }

    @Override
    public Post convert(PostRequest request, User person, LocalDateTime localDateTime) {
        if ( request == null && person == null && localDateTime == null ) {
            return null;
        }

        Post post = new Post();

        if ( request != null ) {
            post.setTitle( request.getTitle() );
            post.setPostText( request.getPostText() );
        }
        if ( person != null ) {
            post.setAuthor( userToPerson( person ) );
            List<Notification> list = person.getNotifications();
            if ( list != null ) {
                post.setNotifications( new ArrayList<Notification>( list ) );
            }
        }
        if ( localDateTime != null ) {
            post.setTime( localDateTime );
        }

        return post;
    }

    protected List<String> tagListToStringList(List<Tag> list) {
        if ( list == null ) {
            return null;
        }

        List<String> list1 = new ArrayList<String>( list.size() );
        for ( Tag tag : list ) {
            list1.add( tagMapper.tagToString( tag ) );
        }

        return list1;
    }

    protected List<CommentDTO> postCommentListToCommentDTOList(List<PostComment> list) {
        if ( list == null ) {
            return null;
        }

        List<CommentDTO> list1 = new ArrayList<CommentDTO>( list.size() );
        for ( PostComment postComment : list ) {
            list1.add( commentMapper.convert( postComment ) );
        }

        return list1;
    }

    protected Person userToPerson(User user) {
        if ( user == null ) {
            return null;
        }

        Person person = new Person();

        person.setId( user.getId() );
        List<Notification> list = user.getNotifications();
        if ( list != null ) {
            person.setNotifications( new ArrayList<Notification>( list ) );
        }
        person.setEmail( user.getEmail() );
        person.setPassword( user.getPassword() );
        person.setType( user.getType() );
        person.setIsDeleted( user.getIsDeleted() );
        List<UserSetting> list1 = user.getSettings();
        if ( list1 != null ) {
            person.setSettings( new ArrayList<UserSetting>( list1 ) );
        }
        person.setFirstName( user.getFirstName() );

        return person;
    }
}
