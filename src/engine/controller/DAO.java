package engine.controller;

import java.util.List;

public interface DAO<T> {

    T get(int id);

    List<T> getAll();

    Quiz save(Quiz quiz);

//    void update(T t, String[] params);
//
//    void delete(T t);
}