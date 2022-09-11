package org.infokin.chatapp.controllers;

import org.infokin.chatapp.models.Message;
import org.infokin.chatapp.services.MessageService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/messages")
public class MessageController {

  private final MessageService messageService;

  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public @ResponseBody void receiveMessage(@RequestBody Message message) {
    this.messageService.addMessage(message);
  }

}
