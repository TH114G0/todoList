package br.com.todo.api.service;

import br.com.todo.api.entity.Todo;
import br.com.todo.api.exception.TodoNotFoundException;
import br.com.todo.api.repository.TodoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TodoService {
    private TodoRepository todoRepository;
    private final List<Todo> todos = new ArrayList<>();

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public Todo create(Todo todo) {
        todoRepository.save(todo);
        return todo;
    }

    public List<Todo> list() {
        Sort sort = Sort.by("prioridade").descending().and(
                Sort.by("nome").ascending());
        return todoRepository.findAll(sort);
    }

    public Todo update(Todo todo) {
        Optional<Todo> optional = todoRepository.findById(todo.getId());
        if (optional.isPresent()) {
            todoRepository.save(todo);
            return todo;
        }else {
            throw new TodoNotFoundException("Todo not found");
        }
    }

    public void delete(Long id) {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (optionalTodo.isPresent()) {
            todoRepository.deleteById(id);
        } else {
            throw new TodoNotFoundException("Todo not found");
        }
    }

}