package AkkaRouter;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.FromConfig;
import scala.concurrent.duration.FiniteDuration;

import java.util.concurrent.TimeUnit;

public class SenderSup extends UntypedActor {

  public SenderSup() {
    // create the sticky router
    ActorRef sticky = getContext().system().actorOf(FromConfig.getInstance().props(), "sticky");

    // create two sender
    ActorRef send1 = getContext().system().actorOf(Props.create(Sender.class, sticky), "send1");
    ActorRef send2 = getContext().system().actorOf(Props.create(Sender.class, sticky), "send2");


    // send every 2 seconds beginning after 1 second
    getContext()
        .system()
        .scheduler()
        .schedule(FiniteDuration.create(1, TimeUnit.SECONDS),
            FiniteDuration.create(2, TimeUnit.SECONDS), send1, 1L,
            getContext().system().dispatcher(), null);

    // send every 2 seconds beginning after 2 second
    getContext()
        .system()
        .scheduler()
        .schedule(FiniteDuration.create(2, TimeUnit.SECONDS),
            FiniteDuration.create(2, TimeUnit.SECONDS), send2, 2L,
            getContext().system().dispatcher(), null);
  }

  @Override
  public void onReceive(Object msg) throws Exception {
    if (msg instanceof String) {
      System.out.println(msg);
    } else {
      unhandled(msg);
    }
  }

  public static Props props() {
    return Props.create(SenderSup.class);
  }
}
