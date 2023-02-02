package org.infokin.chatapp.controllers;

import com.github.javafaker.Faker;
import org.infokin.chatapp.models.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MessageControllerTest {

  private static final String messagesUri = "/messages";

  @Autowired
  private Faker faker;

  @Autowired
  private WebTestClient webTestClient;

  private Message createMessage() {
    String quote = faker.yoda().quote();
    return new Message(quote);
  }

  /**
   * Sends a message to the server and verifies the response.
   */
  private void sendMessage(Message message) {
    webTestClient.post()
      .uri(messagesUri)
      .contentType(MediaType.APPLICATION_JSON)
      .body(Mono.just(message), Message.class)
      .exchange()
      .expectStatus().isOk()
      .expectBody().isEmpty();
  }

  /**
   * Creates a basic listener that receives a message stream from the
   * server and verifies each response.
   */
  private Mono<Void> createBasicListener(int numberOfMessages) {
    AtomicInteger receivedMessagesCount = new AtomicInteger();
    return Mono.<Void>fromRunnable(() ->
      webTestClient.get()
        .uri(messagesUri)
        .accept(MediaType.TEXT_EVENT_STREAM)
        .exchange()
        .expectStatus().isOk()
        .returnResult(Message.class)
        .getResponseBody()
        .take(numberOfMessages)
        .doOnNext(receivedMessage -> {
          assertNotNull(receivedMessage);
          receivedMessagesCount.getAndIncrement();
        })
        .doOnComplete(() -> assertEquals(numberOfMessages, receivedMessagesCount.get()))
        .blockLast()
    ).publishOn(Schedulers.boundedElastic());
  }

  @Test
  @DisplayName("Should send a message to the server")
  void testSendMessage() {
    Message message = createMessage();
    sendMessage(message);
  }

  @Test
  @DisplayName("Should get all messages from the server")
  void testReceiveAllMessages() {

    // Prepare messages to be sent
    List<Message> messages = IntStream.range(0, 5)
      .mapToObj(i -> createMessage())
      .toList();

    // Send messages
    messages.forEach(this::sendMessage);

    // Get all messages and verify each response
    List<Message> receivedMessages = webTestClient.get()
      .uri(messagesUri)
      .accept(MediaType.APPLICATION_JSON)
      .exchange()
      .expectStatus().isOk()
      .expectHeader().contentType(MediaType.APPLICATION_JSON)
      .expectBodyList(Message.class)
      .returnResult()
      .getResponseBody();

    // Verify that received messages are the ones previously sent
    assertNotNull(receivedMessages);
    assertEquals(messages.size(), receivedMessages.size());

    for (int i = 0; i < receivedMessages.size(); i++) {
      Message expectedMessage = messages.get(i);
      Message actualMessage = receivedMessages.get(i);
      assertEquals(expectedMessage, actualMessage);
      assertEquals(expectedMessage.getContent(), actualMessage.getContent());
    }
  }

  @Test
  @DisplayName("Should get new messages as a stream")
  void testReceiveMessagesAsStream() {

    final int NUMBERS_OF_MESSAGES = 15;

    // Create messages
    List<Message> messages = IntStream.range(0, NUMBERS_OF_MESSAGES)
      .mapToObj(i -> createMessage())
      .toList();

    // Set up message receiver
    Mono<Void> listener = createBasicListener(NUMBERS_OF_MESSAGES);

    // Send messages
    Mono<Object> sendMessages = Mono.fromRunnable(() -> messages.forEach(this::sendMessage))
      .publishOn(Schedulers.boundedElastic());

    // Execute requests
    Flux.merge(listener, sendMessages)
      .blockLast();
  }

  @Test
  @DisplayName("Should get new messages as a stream for multiple listeners")
  void testReceiveMessagesAsStreamWithMultipleListeners() {

    final int NUMBERS_OF_MESSAGES = 10;

    // Create messages
    List<Message> messages = IntStream.range(0, NUMBERS_OF_MESSAGES)
      .mapToObj(i -> createMessage())
      .toList();

    // Set up message receivers
    Mono<Void> listener1 = createBasicListener(NUMBERS_OF_MESSAGES);
    Mono<Void> listener2 = createBasicListener(NUMBERS_OF_MESSAGES);
    Mono<Void> listener3 = createBasicListener(NUMBERS_OF_MESSAGES);

    // Send messages
    Mono<Object> sendMessages = Mono.fromRunnable(() -> messages.forEach(this::sendMessage))
      .publishOn(Schedulers.boundedElastic());

    // Execute requests
    Flux.merge(listener1, listener2, listener3, sendMessages)
      .blockLast();
  }

}
