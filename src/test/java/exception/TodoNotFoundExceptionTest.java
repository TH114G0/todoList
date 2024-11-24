package exception;

import br.com.todo.api.exception.TodoNotFoundException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TodoNotFoundExceptionTest {
    @Test
    void testTodoNotFoundException_Message() {
        String errorMessage = "Todo not found";

        TodoNotFoundException exception = assertThrows(TodoNotFoundException.class, () -> {
            throw new TodoNotFoundException(errorMessage);
        });

        // Verificando se a mensagem da exceção é a mesma que foi passada
        assertEquals(errorMessage, exception.getMessage(), "A mensagem da exceção não é a esperada");
    }
}
