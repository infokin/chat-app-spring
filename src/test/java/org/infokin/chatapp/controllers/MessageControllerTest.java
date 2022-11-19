package org.infokin.chatapp.controllers;

import com.github.javafaker.Faker;
import org.infokin.chatapp.models.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MessageControllerTest {

  private final Faker faker = new Faker();

  private static final String messagesUri = "/messages";

  @Autowired
  private WebTestClient webTestClient;

  @Test
  @DisplayName("Test sending and receiving messages")
  void testSendingAndReceivingMessages() {
    int numberOfMessages = 3;

    List<Message> messages = IntStream.range(0, numberOfMessages)
      .mapToObj(i -> {
        String quote = faker.yoda().quote();
        return new Message(quote);
      }).toList();

    for (Message message : messages) {
      webTestClient.post()
        .uri(messagesUri)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(message), Message.class)
        .exchange()
        .expectStatus().isOk()
        .expectBody().isEmpty();
    }

    List<Message> receivedMessages = webTestClient.get()
      .uri(messagesUri)
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON)
      .expectBodyList(Message.class)
      .returnResult()
      .getResponseBody();

    assertNotNull(receivedMessages);
    assertEquals(numberOfMessages, receivedMessages.size());

    for (int i = 0; i < numberOfMessages; i++) {
      Message expectedMessage = messages.get(i);
      Message actualMessage = receivedMessages.get(i);
      assertEquals(expectedMessage, actualMessage);
      assertEquals(expectedMessage.getContent(), actualMessage.getContent());
    }
  }

}
