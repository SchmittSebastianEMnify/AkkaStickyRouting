package AkkaRouter;

import akka.actor.Props;
import akka.actor.UntypedActor;

public class Receiver extends UntypedActor {

  @Override
  public void onReceive(Object msg) throws Exception {
    if (msg instanceof Long) {
      sender().tell(String.valueOf(msg), getSelf());
    } else {
      unhandled(msg);
    }
  }

  public static Props props() {
    return Props.create(Receiver.class);
  }
}
