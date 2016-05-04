package AkkaRouter;

import com.typesafe.config.Config;

import akka.actor.ActorSystem;
import akka.dispatch.Dispatchers;
import akka.routing.GroupBase;
import akka.routing.Router;

import java.util.List;

public class StickyRoutingGroup extends GroupBase {

  private static final long serialVersionUID = 1L;
  private final List<String> paths;

  public StickyRoutingGroup(List<String> paths) {
    this.paths = paths;
  }

  public StickyRoutingGroup(Config config) {
    this(config.getStringList("routees.paths"));
  }

  @Override
  public Router createRouter(ActorSystem arg0) {
    return new Router(new StickyRoutingLogic());
  }

  @Override
  public String routerDispatcher() {
    return Dispatchers.DefaultDispatcherId();
  }

  @Override
  public Iterable<String> getPaths(ActorSystem arg0) {
    return paths;
  }

}
