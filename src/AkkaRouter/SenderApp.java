package AkkaRouter;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import akka.actor.ActorSystem;
import akka.kernel.Bootable;

@SuppressWarnings("deprecation")
public class SenderApp implements Bootable {

  private final Config config;
  private ActorSystem system;

  public SenderApp() {
    config = ConfigFactory.load("send");
    system = ActorSystem.create("stick", config);

    system.actorOf(SenderSup.props(), "sendsup");
  }

  @Override
  public void shutdown() {}

  @Override
  public void startup() {}

}
