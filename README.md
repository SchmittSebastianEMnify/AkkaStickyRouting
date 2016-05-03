# AkkaStickyRouting

Start the Sender:

    mvn exec:java -Dexec.args=AkkaRouter.SenderApp

Start the Receiver:

    mvn exec:java -Dexec.args=AkkaRouter.ReceiverApp
 
 
 Switch between StickyRouter and consistent-hashing-group by setting the config "useStickyRouter"
 
 send.conf
 
    useStickyRouter = true

    akka {
      loggers = ["akka.event.slf4j.Slf4jLogger"]
      loglevel = "INFO"
      log-dead-letters = 0
      log-dead-letters-during-shutdown = off
      log-config-on-start = off
    
      actor {
        provider = "akka.cluster.ClusterActorRefProvider"
        deployment {
          /sticky {
            router = "AkkaRouter.StickyRoutingGroup"
            routees.paths = ["/user/rec"]
            cluster {
              enabled = on
              use-role = recthingy
            }
          }
          /cons {
            router = consistent-hashing-group
            routees.paths = ["/user/rec"]
            cluster {
              enabled = on
              use-role = recthingy
            }
          }
        }
      }
    
      remote {
        log-remote-lifecycle-events = off

        netty.tcp {
          hostname = localhost
          port = 2551
        }
      }

      cluster {
        seed-nodes = ["akka.tcp://stick@localhost:2551"]
        roles = [pubthingy]
    
        auto-down-unreachable-after = 5s
      }
    }
    
rec.conf

    akka {
      loggers = ["akka.event.slf4j.Slf4jLogger"]
      loglevel = "INFO"
      log-dead-letters = 0
      log-dead-letters-during-shutdown = off
      log-config-on-start = off
    
      actor {
        provider = "akka.cluster.ClusterActorRefProvider"
      }
    
      remote {
        log-remote-lifecycle-events = off
    
        netty.tcp {
          hostname = localhost
          port = 0
        }
      }
    
      cluster {
        seed-nodes = ["akka.tcp://stick@localhost:2551"]
        roles = [recthingy]
    
        auto-down-unreachable-after = 5s
      }
    }