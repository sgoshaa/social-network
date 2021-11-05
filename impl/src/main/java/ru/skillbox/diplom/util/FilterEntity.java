package ru.skillbox.diplom.util;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
public class FilterEntity {

    private final EntityManager entityManager;

    @Autowired
    public FilterEntity(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    private Session getSession() {
        return entityManager.unwrap(org.hibernate.Session.class);
    }

    public void enableFilter(String nameFilter, String nameParameter, Object value) {
        Filter filter = getSession().enableFilter(nameFilter);
        filter.setParameter(nameParameter, value);
    }

    public void disableFilter(String nameFilter) {
        getSession().disableFilter(nameFilter);
    }
}
