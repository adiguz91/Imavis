package com.drone.imavis.data.repository;

import java.util.List;

/**
 * Created by adigu on 06.05.2017.
 */

/**
 * Repository CRUD
 * @param <T>
 */
public interface IRepository<T>{

    // create
    void add(T item);
    void add(Iterable<T> items);

    // read
    T get(long id);
    T get(T item);
    Iterable<T> getAll();

    // update
    void update(T item);

    // remove
    void remove(T item);
}
