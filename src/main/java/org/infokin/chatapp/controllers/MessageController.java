package org.infokin.chatapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.infokin.chatapp.models.Message;
import org.infokin.chatapp.services.MessageService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("messages")
public class MessageController {

  private final MessageService messageService;

  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  @Operation(
    summary = "Store a message",
    description = "This endpoint stores the received message on the server."
  )
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public void receiveMessage(@RequestBody Message message) {
    messageService.addMessage(message);
  }

  @Operation(
    summary = "Retrieve all messages",
    description = "This endpoint returns all messages that are currently stored on the server."
  )
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Message> getMessages() {
    return messageService.getMessages();
  }

}
