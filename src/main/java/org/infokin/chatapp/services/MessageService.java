package org.infokin.chatapp.services;

import lombok.extern.slf4j.Slf4j;
import org.infokin.chatapp.models.Message;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MessageService {

  private final List<Message> messages = new ArrayList<>();

  public void addMessage(Message message) {
    log.debug("Adding message: " + message);
    messages.add(message);
  }

  public List<Message> getMessages() {
    return messages;
  }

}
