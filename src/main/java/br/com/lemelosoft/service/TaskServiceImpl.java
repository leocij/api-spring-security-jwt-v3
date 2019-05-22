package br.com.lemelosoft.service;

import br.com.lemelosoft.model.Task;
import br.com.lemelosoft.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    private TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    @Transactional
    public void createTask(Task task) {
        this.taskRepository.save(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> findByUser(String name) {
        return this.taskRepository.findByUser(name);
    }

    @Transactional
    @Override
    public boolean deleteById(Long id) {
        if (this.taskRepository.existsById(id)) {
            this.taskRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    @Override
    public Boolean patch(Long id, String task) {
        if (!this.taskRepository.existsById(id)) {
            return false;
        }
        Optional<Task> optionalTask = this.taskRepository.findById(id);
        if (optionalTask.isEmpty()) {
            return false;
        }
        Task task1 = optionalTask.get();
        if (!StringUtils.isEmpty(task)) {
            task1.setTask(task);
        }
        this.taskRepository.save(task1);
        return true;
    }
}
