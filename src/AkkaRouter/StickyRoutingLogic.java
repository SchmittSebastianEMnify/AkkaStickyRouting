package AkkaRouter;

import akka.actor.ActorSelection;
import akka.routing.ActorSelectionRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.RoutingLogic;
import scala.collection.immutable.IndexedSeq;

import java.util.HashMap;

public class StickyRoutingLogic implements RoutingLogic {
  private static final HashMap<Long, Routee> ipToGi = new HashMap<Long, Routee>();
  private final RoutingLogic routingLogic;

  public StickyRoutingLogic() {
    this.routingLogic = new RoundRobinRoutingLogic();
  }

  @Override
  public Routee select(Object message, IndexedSeq<Routee> routees) {
    if (message instanceof Long) {
      Long msg = (Long) message;
      Routee routee = ipToGi.get(msg);
      if (routee == null) {
        routee = routingLogic.select(msg, routees);
        ipToGi.put(msg, routee);
      } else if (!routees.toList().contains(routee)) {
        routee = routingLogic.select(msg, routees);
        ipToGi.put(msg, routee);
      }
      return routee;
    } else {
      return routingLogic.select(message, routees);
    }
  }

  public static void updateStickiness(Long num, ActorSelection sel) {
    Routee newRoutee = new ActorSelectionRoutee(sel);
    Routee oldRoutee = ipToGi.get(num);
    if (!oldRoutee.equals(newRoutee)) {
      ipToGi.put(num, newRoutee);
      System.out.println("Updated Stickiness for " + sel.anchorPath().address().toString()
          + " and value " + num);
    }
  }
}
