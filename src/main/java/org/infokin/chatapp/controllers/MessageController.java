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
    summary = "Send a message",
    description = "This endpoint is used to send a message to the server."
  )
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public void receiveMessage(@RequestBody Message message) {
    messageService.addMessage(message);
  }

  @Operation(
    summary = "Get all messages",
    description = "This endpoint is used to get all messages from the server."
  )
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Message> getMessages() {
    return messageService.getMessages();
  }

}
