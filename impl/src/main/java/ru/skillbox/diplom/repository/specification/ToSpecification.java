package ru.skillbox.diplom.repository.specification;

import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.skillbox.diplom.model.Person;
import ru.skillbox.diplom.repository.specification.enums.FieldName;
import ru.skillbox.diplom.util.UtilSplit;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

@Component
@NoArgsConstructor
public class ToSpecification<T> {

    private static final String PERCENT = "%";

    private Specification<T> contains(Path<String> key, String value) {
        return (root, query, builder) -> Objects.isNull(value) ? builder.conjunction() :
                builder.like(builder.lower(key), PERCENT + value.toLowerCase() + PERCENT);
    }

    public Specification<T> contains(String key, String value) {
        return (root, query, builder) -> contains(root.get(key), value).toPredicate(root, query, builder);
    }

    private Specification<T> contains(Path<String> key, Predicate.BooleanOperator booleanOperator, String... values) {
        return Arrays.stream(values)
                .map(v -> contains(key, v))
                .reduce((spec1, spec2) -> spec1 = booleanOperator == Predicate.BooleanOperator.OR ? spec1.or(spec2)
                        : spec1.and(spec2))
                .orElse(disjunctionOrConjunction(Predicate.BooleanOperator.AND));
    }

    public Specification<T> contains(String key, Predicate.BooleanOperator booleanOperator, String... values) {
        return (root, query, builder) -> contains(root.get(key), booleanOperator, values)
                .toPredicate(root, query, builder);
    }

    public Specification<T> equals(String key, Object value) {
        return (root, query, builder) -> Objects.isNull(value) ? builder.conjunction() :
                builder.equal(root.get(key), value);
    }

    public Specification<T> noEquals(String key, Object value) {
        return (root, query, builder) -> Objects.isNull(value) ? builder.conjunction() :
                builder.notEqual(root.get(key), value);
    }

    public Specification<T> disjunctionOrConjunction(Predicate.BooleanOperator booleanOperator) {
        return (root, query, builder) -> booleanOperator == Predicate.BooleanOperator.OR ?
                builder.disjunction() : builder.conjunction();
    }

    public Specification<T> between(String key, LocalDateTime valueFrom, LocalDateTime valueTo) {
        LocalDateTime dateFrom = Objects.isNull(valueFrom) ?
                LocalDateTime.of(1900, 1, 1, 0, 0, 0) : valueFrom;
        LocalDateTime dateTo = Objects.isNull(valueTo) ? LocalDateTime.now() : valueTo;
        return (root, query, builder) -> builder.between(root.get(key), dateFrom, dateTo);
    }

    private Specification<T> getPredicateNameField(Path<String> firstNameField, Path<String> lastNameField, String value) {
        return contains(firstNameField, Predicate.BooleanOperator.OR, UtilSplit.split(value))
                .or(contains(lastNameField, Predicate.BooleanOperator.OR, UtilSplit.split(value)));
    }

    public Specification<T> getPredicateNameField(String firstNameField, String lastNameField, String value) {
        return (root, query, builder) -> getPredicateNameField(root.get(firstNameField), root.get(lastNameField), value)
                .toPredicate(root, query, builder);
    }

    public Specification<T> isFalse(String key) {
        return (root, query, builder) -> builder.isFalse(root.get(key));
    }

    public Specification<T> isTrue(String key) {
        return (root, query, builder) -> builder.isTrue(root.get(key));
    }

    public Specification<T> joinAuthor(String key, String value) {
        return (root, query, builder) -> {
            Join<T, Person> joinPerson = root.join(key);
            return getPredicateNameField(joinPerson.get(FieldName.FIRST_NAME.getValue()),
                    joinPerson.get(FieldName.LAST_NAME.getValue()), value)
                    .toPredicate(root, query, builder);
        };
    }

    public Specification<T> greaterThan(String key, LocalDateTime date) {
        return (root, query, builder) -> Objects.isNull(date) ? builder.conjunction() :
                builder.greaterThan(root.get(key), date);
    }

}
