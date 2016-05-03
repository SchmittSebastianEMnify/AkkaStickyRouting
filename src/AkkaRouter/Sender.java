package AkkaRouter;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class Sender extends UntypedActor {

  ActorRef router;
  Long test = 0L;

  public Sender(ActorRef router) {
    this.router = router;
  }

  public void onReceive(Object msg) {
    if (msg instanceof Long) {
      router.tell(++test, getSelf());
    } else if (msg instanceof String) {
      System.out.println("Sender " + getSelf().path().name() + " received response from "
          + sender().path().address().toString() + ": " + msg);
    } else {
      unhandled(msg);
    }
  }
}
