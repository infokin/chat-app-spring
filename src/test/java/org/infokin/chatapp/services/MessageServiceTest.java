package org.infokin.chatapp.services;

import com.github.javafaker.Faker;
import org.infokin.chatapp.models.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MessageServiceTest {

  @Autowired
  private Faker faker;

  @Autowired
  private MessageService messageService;

  @Test
  @DisplayName("Test storage and retrieval of messages")
  void testStorageAndRetrievalOfMessages() {
    List<Message> inputMessages = IntStream.range(0, 10)
      .mapToObj(i -> {
        String quote = faker.yoda().quote();
        return new Message(quote);
      }).toList();

    inputMessages.forEach(messageService::addMessage);

    List<Message> outputMessages = messageService.getMessages();
    assertEquals(inputMessages.size(), outputMessages.size());

    for (int i = 0; i < outputMessages.size(); i++) {
      Message expectedMessage = inputMessages.get(i);
      Message actualMessage = outputMessages.get(i);
      assertEquals(expectedMessage, actualMessage);
      assertEquals(expectedMessage.getContent(), actualMessage.getContent());
    }
  }

}
