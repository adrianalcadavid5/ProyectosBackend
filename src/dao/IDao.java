package dao;

import java.util.List;

public interface IDao<T>{
    T guardar (T t);
    T buscarPorId(Integer id);
    List<T> buscarTodos();
}
