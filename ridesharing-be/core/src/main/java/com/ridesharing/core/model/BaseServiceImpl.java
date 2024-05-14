package com.ridesharing.core.model;

import java.util.List;

public interface BaseServiceImpl<T, I> {
    List<T> findAll();
    T save(T entity);
    List<T> saveAll(List<T> entities);
    T findById(I id);
    void deleteById(I id);
}