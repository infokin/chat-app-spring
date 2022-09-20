package org.infokin.chatapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.infokin.chatapp.models.Message;
import org.infokin.chatapp.services.MessageService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("messages")
public class MessageController {

  private final MessageService messageService;

  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  @Operation(
    summary = "Send a message",
    description = "Send a chat message to the server."
  )
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public void receiveMessage(@RequestBody Message message) {
    this.messageService.addMessage(message);
  }

}
