package br.com.todo.api.controller;

import br.com.todo.api.entity.Todo;
import br.com.todo.api.exception.TodoNotFoundException;
import br.com.todo.api.repository.TodoRepository;
import br.com.todo.api.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;
    private final TodoRepository todoRepository;

    public TodoController(TodoService todoService, TodoRepository todoRepository) {
        this.todoService = todoService;
        this.todoRepository = todoRepository;
    }

    @PostMapping()
    ResponseEntity<Todo> create(@RequestBody @Valid Todo todo) {
        Todo createdTodo = todoService.create(todo);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTodo);
    }

    @GetMapping
    List<Todo> list() {
        return todoService.list();
    }

    @PutMapping("{id}")
    ResponseEntity<Todo> update(@PathVariable("id") Long id, @RequestBody @Valid Todo todo) {
        try {
            todo.setId(id);
            Todo updateTodo = todoService.update(todo);
            return ResponseEntity.ok(updateTodo);
        } catch (TodoNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @DeleteMapping("{id}")
    ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        try {
            todoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (TodoNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
