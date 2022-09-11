package org.infokin.chatapp.services;

import org.infokin.chatapp.models.Message;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {

  private final List<Message> messages = new ArrayList<>();

  public void addMessage(Message message) {
    this.messages.add(message);
  }

}
