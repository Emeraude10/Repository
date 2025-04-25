package repository;

import java.util.List;

public interface Repository<T, ID> {
    void create(T entity);
    T read(ID id);
    void update(T entity);
    void delete(ID id);
    List<T> findAll();
}