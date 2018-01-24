package AkkaRouter;

import akka.actor.ActorSelection;
import akka.routing.ActorSelectionRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.RoutingLogic;
import scala.collection.immutable.IndexedSeq;

import java.util.HashMap;

public class StickyRoutingLogic implements RoutingLogic {

  private static final HashMap<Long, Routee> routeeMapping = new HashMap<Long, Routee>();
  private final RoutingLogic routingLogic;

  public StickyRoutingLogic() {
    this.routingLogic = new RoundRobinRoutingLogic();
  }

  public static void updateStickiness(Long num, ActorSelection sel) {
    Routee newRoutee = new ActorSelectionRoutee(sel);
    ActorSelectionRoutee existing = (ActorSelectionRoutee) routeeMapping.get(num);
    if (existing == null || !existing.selection().anchor().path().address()
        .equals(((ActorSelectionRoutee) newRoutee).selection().anchor().path().address())) {

      routeeMapping.put(num, newRoutee);
      System.out.println("Updated Stickiness for " + sel.anchorPath().address().toString()
          + " and value " + num);
    }
  }

  @Override
  public Routee select(Object message, IndexedSeq<Routee> routees) {
    if (message instanceof Long) {
      Long msg = (Long) message;
      Routee routee = routeeMapping.get(msg);
      if (routee == null) {
        routee = routingLogic.select(msg, routees);
        routeeMapping.put(msg, routee);
      } else if (!routees.toList().contains(routee)) {
        routee = routingLogic.select(msg, routees);
        routeeMapping.put(msg, routee);
        System.out.println("Updated Stickiness for " + routee
            + " and value " + msg);
      }
      return routee;
    } else {
      return routingLogic.select(message, routees);
    }
  }
}
