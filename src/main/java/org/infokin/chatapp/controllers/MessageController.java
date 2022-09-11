package org.infokin.chatapp.controllers;

import org.infokin.chatapp.models.Message;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/messages")
public class MessageController {

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public @ResponseBody void receiveMessage(@RequestBody Message message) {
    // TODO (jhillert): meaningfully process message (add service, store message in array)
    System.out.println(message.getContent());
  }

}
