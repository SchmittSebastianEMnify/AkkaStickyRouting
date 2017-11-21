package AkkaRouter;

import akka.actor.Props;
import akka.actor.UntypedActor;
import java.io.Serializable;

public class Receiver extends UntypedActor {

  @Override
  public void onReceive(Object msg) throws Exception {
    if (msg instanceof Long) {
      Response resp = new Response((Long) msg, getSelf().path().name());
      sender().tell(resp, getSelf());
    } else {
      unhandled(msg);
    }
  }

  public static Props props() {
    return Props.create(Receiver.class);
  }

  public static class Response implements Serializable {
    private static final long serialVersionUID = 1L;
    private final long msgId;
    private final String rName;

    /**
     * @param msgId long
     * @param rName String
     */
    public Response(long msgId, String rName) {
      this.msgId = msgId;
      this.rName = rName;
    }

    /**
     * @return the msgId
     */
    public long getMsgId() {
      return msgId;
    }

    /**
     * @return the rName
     */
    public String getrName() {
      return rName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
      return "Response [msg=" + msgId + ", " + (rName != null ? "Receiver=" + rName : "") + "]";
    }

  }

}
