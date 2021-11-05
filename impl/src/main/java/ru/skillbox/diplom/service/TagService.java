package ru.skillbox.diplom.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.model.Post;
import ru.skillbox.diplom.model.Tag;
import ru.skillbox.diplom.repository.PostToTagRepository;
import ru.skillbox.diplom.repository.TagRepository;
import ru.skillbox.diplom.repository.specification.ToSpecification;
import ru.skillbox.diplom.service.enums.LoggerLevel;
import ru.skillbox.diplom.service.enums.LoggerValue;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService implements SocialNetworkService {

    private final Class<TagService> loggerClass = TagService.class;
    private final ToSpecification<Tag> toSpecTag;
    private final TagRepository tagRepository;
    private final PostToTagRepository postToTagRepository;

    @Autowired
    public TagService(ToSpecification<Tag> toSpecTag,
                      TagRepository tagRepository,
                      PostToTagRepository postToTagRepository) {
        this.toSpecTag = toSpecTag;
        this.tagRepository = tagRepository;
        this.postToTagRepository = postToTagRepository;
    }

    @Transactional
    public void saveTags(List<String> tags, Post post) {
        if (tags.isEmpty()) {
            return;
        }
        List<Tag> tagsFound = findByTags(tags);
        tags.forEach(t -> {
            Tag tag = new Tag();
            tag.setTag(t);
            tag.setPost(new ArrayList<>());
            if (!tagsFound.contains(tag)) {
                tagsFound.add(tag);
            }
        });
        tagsFound.forEach(tag -> tag.getPost().add(post));
        tagRepository.saveAll(tagsFound);
        log(loggerClass, LoggerLevel.INFO, "saveTags", LoggerValue.TAG_SAVE,tagsFound.toString());
    }

    public List<Tag> findByTags(List<String> tags) {
        Specification<Tag> tagSpecification = tags.stream()
                .map(tag -> toSpecTag.equals("tag", tag))
                .reduce(Specification::or)
                .orElse(toSpecTag.disjunctionOrConjunction(Predicate.BooleanOperator.OR));
        return tagRepository.findAll(tagSpecification);
    }

    @Transactional
    public void editTags(List<String> newTags, Post oldPost) {
        List<String> oldTags = oldPost.getTags()
                .stream()
                .map(Tag::getTag)
                .collect(Collectors.toList());
        List<String> addingTags = newTags
                .stream()
                .filter(tag -> !oldTags.contains(tag))
                .collect(Collectors.toList());
        List<String> deleteTags = oldTags.stream()
                .filter(tag -> !newTags.contains(tag))
                .collect(Collectors.toList());
        if (!addingTags.isEmpty()) {
            saveTags(addingTags, oldPost);
            log(loggerClass, LoggerLevel.INFO, "editTags", LoggerValue.TAG_EDIT,addingTags.toString());
        }
        if (!deleteTags.isEmpty()) {
            List<Tag> tags = findByTags(deleteTags);
            postToTagRepository.deleteTags(oldPost.getId(), tags);
            log(loggerClass, LoggerLevel.INFO, "editTags", LoggerValue.TAG_DELETE,deleteTags.toString());
        }
    }
}
