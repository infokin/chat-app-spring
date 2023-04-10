package org.infokin.chatapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.infokin.chatapp.models.Message;
import org.infokin.chatapp.services.MessageService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("messages")
public class MessageController {

  private final MessageService messageService;

  private final Sinks.Many<Message> messageEmitter = Sinks.many().multicast().directBestEffort();

  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  @Operation(
    summary = "Send a message",
    description = "This endpoint is used to send a message to the server."
  )
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public void receiveMessage(@RequestBody Message message) {
    log.debug("Received message: {}", message);
    messageService.addMessage(message);
    messageEmitter.tryEmitNext(message);
  }

  @Operation(
    summary = "Get all messages",
    description = "This endpoint is used to get all messages from the server."
  )
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Message> getMessages() {
    log.debug("Received request to get all messages");
    return messageService.getMessages();
  }

  @Operation(
    summary = "Get messages as stream",
    description = "This endpoint is used to get new messages as a stream from the server."
  )
  @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<Message> emitMessages() {
    log.debug("Received request to get messages as stream");
    return messageEmitter.asFlux();
  }

}
