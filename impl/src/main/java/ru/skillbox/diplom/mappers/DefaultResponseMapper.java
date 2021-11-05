package ru.skillbox.diplom.mappers;

import ru.skillbox.diplom.mappers.converter.DateConverter;
import ru.skillbox.diplom.model.api.enums.Errors;
import ru.skillbox.diplom.model.api.response.Response;

import java.time.LocalDateTime;
import java.util.Collection;

public class DefaultResponseMapper<T> {

    private final DateConverter dateConverter = new DateConverter();

    public Response<T> convert (Collection<T> collection, long total, int itemPerPage) {
        Response<T> response = new Response<>();
        response.setTotal(total);
        response.setPerPage(itemPerPage);
        response.setData(collection);
        response.setError(Errors.string);
        response.setTimestamp(dateConverter.convertRegDate(LocalDateTime.now()));
        return response;
    }


}
