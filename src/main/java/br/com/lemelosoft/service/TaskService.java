package br.com.lemelosoft.service;

import br.com.lemelosoft.model.Task;

import java.util.List;

public interface TaskService {
    void createTask(Task task);

    List<Task> findByUser(String name);

    boolean deleteById(Long id);

    Boolean patch(Long id, String task);
}
