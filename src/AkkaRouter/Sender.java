package AkkaRouter;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.routing.ConsistentHashingRouter.ConsistentHashableEnvelope;

public class Sender extends UntypedActor {

  private ActorRef router;
  private Integer numberOfUniqueMessages;
  private Long test = 0L;

  public Sender(ActorRef router) {
    this.router = router;
    this.numberOfUniqueMessages =
        getContext().system().settings().config().getInt("numberOfUniqueMessages");
  }

  public void onReceive(Object msg) {
    if (msg instanceof Long) {
      test = (test % numberOfUniqueMessages) + 1;
      if (router.path().name().equals("sticky")) {
        router.tell(test, getSelf());
      } else {
        router.tell(new ConsistentHashableEnvelope(test, test), getSelf());
      }
    } else if (msg instanceof String) {
      StickyRoutingLogic.updateStickiness(Long.valueOf((String) msg),
          getContext().actorSelection(sender().path()));
      System.out.println("Sender " + getSelf().path().name() + " received response from "
          + sender().path().address().toString() + ": " + msg);
    } else {
      unhandled(msg);
    }
  }
}
