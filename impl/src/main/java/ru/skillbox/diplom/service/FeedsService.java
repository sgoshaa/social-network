package ru.skillbox.diplom.service;

import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.mappers.PersonMapper;
import ru.skillbox.diplom.mappers.PostMapper;
import ru.skillbox.diplom.model.Post;
import ru.skillbox.diplom.model.PostDTO;
import ru.skillbox.diplom.model.api.response.FeedsResponse;
import ru.skillbox.diplom.repository.PostRepository;
import ru.skillbox.diplom.repository.specification.ToSpecification;
import ru.skillbox.diplom.repository.specification.enums.FieldName;
import ru.skillbox.diplom.service.enums.LoggerLevel;
import ru.skillbox.diplom.service.enums.LoggerValue;
import ru.skillbox.diplom.util.FilterEntity;
import ru.skillbox.diplom.util.Paging;
import ru.skillbox.diplom.util.enums.ParametersFilter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedsService implements SocialNetworkService {

    private final ToSpecification<Post> postToSpecification;
    private final PostRepository postRepository;
    private final PostMapper postMapper = Mappers.getMapper(PostMapper.class);
    private final PersonMapper personMapper = Mappers.getMapper(PersonMapper.class);
    private final FilterEntity filterEntity;

    @Autowired
    public FeedsService(ToSpecification<Post> postToSpecification, PostRepository postRepository, FilterEntity filterEntity) {
        this.postToSpecification = postToSpecification;
        this.postRepository = postRepository;
        this.filterEntity = filterEntity;
    }

    public FeedsResponse feeds(String name, Integer offSet, Integer itemPerPage) {
        Specification<Post> specification = postToSpecification
                .between(FieldName.TIME.getValue(), null, LocalDateTime.now())
                .and(postToSpecification.isFalse(FieldName.IS_DELETED.getValue()));
        log(FeedsService.class, LoggerLevel.INFO, "getFeeds", LoggerValue.FEED_REQUEST, name.isEmpty() ? "empty name" : name);
        Pageable page = Paging.getPaging(offSet, itemPerPage, Sort.by(FieldName.TIME.getValue()).descending());
        filterEntity.enableFilter(ParametersFilter.IS_DELETED.getName(), ParametersFilter.IS_DELETED.getNameParameter(), false);
        Page<Post> postsPage = postRepository.findAll(specification, page);
        List<PostDTO> postDTOS = postsPage.stream().map(postMapper::convert)
                .collect(Collectors.toList());
        filterEntity.disableFilter(ParametersFilter.IS_DELETED.getName());

        return new FeedsResponse()
                .applied(postDTOS, itemPerPage, postsPage.getTotalElements(), offSet);
    }

}



