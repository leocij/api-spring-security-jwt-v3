package br.com.lemelosoft.controller;

import br.com.lemelosoft.model.Task;
import br.com.lemelosoft.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<?> get(Authentication authentication) {

        String name = authentication.getName();

        return new ResponseEntity<>(this.taskService.findByUser(name), HttpStatus.OK);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void post(@RequestBody Task task) {
        this.taskService.createTask(task);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        boolean deleteById = this.taskService.deleteById(id);
        if (deleteById) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patch(@PathVariable Long id, @RequestBody Map<String, String> update) {
        boolean patchedById = this.taskService.patch(id, update.get("task"));
        if (patchedById) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
