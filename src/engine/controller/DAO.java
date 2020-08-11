package engine.controller;

import java.util.List;
import java.util.Optional;

public interface DAO<T>  {

    T get(int id);

    List<T> getAll();

    T save(T t);

//    void update(T t, String[] params);
//
//    void delete(T t);
}