package AkkaRouter;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSystem;
import akka.kernel.Bootable;

@SuppressWarnings("deprecation")
public class ReceiverApp implements Bootable {

  public ReceiverApp() {
    Config config = ConfigFactory.load("rec");
    ActorSystem system = ActorSystem.create("stick", config);
    system.actorOf(Receiver.props(), "rec");
  }

  @Override
  public void shutdown() {}

  @Override
  public void startup() {}

}
