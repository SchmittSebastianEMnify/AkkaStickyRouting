package AkkaRouter;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSystem;
import akka.kernel.Bootable;

@SuppressWarnings("deprecation")
public class ReceiverApp implements Bootable {

  public ReceiverApp() {
    String name = System.getProperty("name", "receiver");

    Config config = ConfigFactory.load("rec");
    ActorSystem system = ActorSystem.create("stick", config);
    system.actorOf(Receiver.props(), name);
  }

  @Override
  public void shutdown() {}

  @Override
  public void startup() {}

}
