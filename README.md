# AkkaStickyRouting

* Start-up the Sender App (starts 2 Senders):

```bash
    mvn exec:java -Dexec.args=AkkaRouter.SenderApp
```

* Start a Receiver with the given _name_ (name must start with "rec"):

```bash
    mvn exec:java -Dexec.args=AkkaRouter.ReceiverApp -Dname=receiver1
 ```
 
----

** Helper scripts **

- Start 3 Receivers:

```bash
    ./startReceivers.sh
```

- Stop all running Akka nodes:

```bash
    ./stopNodes.sh
```

 
----
 
 Switch between StickyRouter and consistent-hashing-group by setting the config "useStickyRouter"
 
 * send.conf
 
    useStickyRouter = true
    numberOfUniqueMessages = 20

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
            routees.paths = ["/user/rec*"]
            cluster {
              enabled = on
              use-role = recthingy
            }
          }
          /cons {
            router = consistent-hashing-group
            routees.paths = ["/user/rec*"]
            virtual-nodes-factor = 10
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
    
* rec.conf

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