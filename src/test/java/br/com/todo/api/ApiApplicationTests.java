package br.com.todo.api;

import br.com.todo.api.entity.Todo;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.reactive.server.WebTestClient;



@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiApplicationTests {
	@Autowired
	private WebTestClient webTestClient;

	@Test
	void stestCreateTodoSucess() {
		var todo = new Todo("todo 1", "desc todo 1", false, 1);

		webTestClient
				.post()
				.uri("/todos")
				.bodyValue(todo)
				.exchange()
				.expectStatus().isCreated()
				.expectBody()
				.jsonPath("$.nome").isEqualTo(todo.getNome())
				.jsonPath("$.descricao").isEqualTo(todo.getDescricao())
				.jsonPath("$.realizado").isEqualTo(todo.isRealizado())
				.jsonPath("$.prioridade").isEqualTo(todo.getPrioridade());
	}

	@Test
	void testCreateTodoFailure() {
		webTestClient
				.post()
				.uri("/todos")
				.bodyValue(
						new Todo("", "", false, 0)
				).exchange()
				.expectStatus().isBadRequest();
	}

	@Test
	void testListTodoSuccess() {
		var todo1 = new Todo("todo 1", "desc todo 1", false, 1);
		var todo2 = new Todo("todo 2", "desc todo 2", false, 2);

		// Criar o primeiro Todo
		var createdTodo1 = webTestClient
				.post()
				.uri("/todos")
				.bodyValue(todo1)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Todo.class)
				.returnResult()
				.getResponseBody();

		assert createdTodo1 != null;

		// Criar o segundo Todo
		var createdTodo2 = webTestClient
				.post()
				.uri("/todos")
				.bodyValue(todo2)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Todo.class)
				.returnResult()
				.getResponseBody();

		assert createdTodo2 != null;

		webTestClient
				.get()
				.uri("/todos")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$").isArray()
				.jsonPath("$.length()").isEqualTo(2);


		webTestClient
				.get()
				.uri("/todos")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$[?(@.nome=='todo 1')].nome").isEqualTo(createdTodo1.getNome());


		webTestClient
				.get()
				.uri("/todos")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$[?(@.nome=='todo 2')].nome").isEqualTo(createdTodo2.getNome());
	}


	@Test
	void testListTodoFailure() {
		webTestClient
				.get()
				.uri("/todos")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$").isArray()
				.jsonPath("$.length()").isEqualTo(0);
	}

	@Test
	void testUpdateTodoSuccess() {
		var todo = new Todo("todo", "desc todo", false, 1);
		var updatedTodo = new Todo("todo atualizado", "desc todo atualizado", false, 1);

		// Criar o item todo inicial
		var createdTodo = webTestClient
				.post()
				.uri("/todos")
				.bodyValue(todo)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Todo.class)
				.returnResult()
				.getResponseBody();

		assert createdTodo != null;

		updatedTodo.setId(createdTodo.getId());

		webTestClient
				.put()
				.uri("/todos/{id}", createdTodo.getId())
				.bodyValue(updatedTodo)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.nome").isEqualTo(updatedTodo.getNome())
				.jsonPath("$.descricao").isEqualTo(updatedTodo.getDescricao());
	}

	@Test
	void testUpdateTodoFailure_invalidDescription() {

		var invalidTodo = new Todo("todo", "", false, 1);


		var createdTodo = webTestClient
				.post()
				.uri("/todos")
				.bodyValue(new Todo("todo", "descricao inicial", false, 1))
				.exchange()
				.expectStatus().isCreated() // Verifica se o Todo foi criado com sucesso
				.expectBody(Todo.class)
				.returnResult()
				.getResponseBody();


		webTestClient
				.put()
				.uri("/todos/{id}", createdTodo.getId())
				.bodyValue(invalidTodo)
				.exchange()
				.expectStatus().isBadRequest()
				.expectBody()
				.jsonPath("$.status").isEqualTo(400)
				.jsonPath("$.error").isEqualTo("Bad Request")
				.jsonPath("$.path").value(Matchers.containsString("/todos/"));
	}

	@Test
	void testDeleteTodoSuccess() {
		var todo = new Todo("todo", "desc todo", false, 1);


		var createdTodo = webTestClient
				.post()
				.uri("/todos")
				.bodyValue(todo)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Todo.class)
				.returnResult()
				.getResponseBody();

		assert createdTodo != null;

		webTestClient
				.delete()
				.uri("/todos/{id}", createdTodo.getId())
				.exchange()
				.expectStatus().isNoContent();
	}

}