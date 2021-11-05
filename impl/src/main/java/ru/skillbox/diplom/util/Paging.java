package ru.skillbox.diplom.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class Paging {

    public static Pageable getPaging(int offset, int itemPerPage, Sort sort) {
        Pageable paging;
        int pageNumber = offset / itemPerPage;
        paging = PageRequest.of(pageNumber, itemPerPage, sort);

        return paging;
    }

    public static Pageable getPaging(int offset, int itemPerPage) {
        return getPaging(offset, itemPerPage, Sort.by("id").descending());
    }
}
