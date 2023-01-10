package org.infokin.chatapp;

import org.infokin.chatapp.models.Message;
import reactor.core.publisher.FluxSink;

import java.util.function.Consumer;

public class MessageEmitter implements Consumer<FluxSink<Message>> {

  private FluxSink<Message> fluxSink;

  @Override
  public void accept(FluxSink<Message> fluxSink) {
    this.fluxSink = fluxSink;
  }

  public void emit(Message message) {
    if (fluxSink != null) {
      fluxSink.next(message);
    }
  }

}
