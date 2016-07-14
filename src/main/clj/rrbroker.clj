(ns rrbroker
  (:require [zeromq
              [device :as dev]
              [zmq :as zmq]])
  (:import [org.zeromq ZMQ]))

(defn -main []
  (let [context (zmq/zcontext)
        poller (zmq/poller context 2)]
    (with-open [frontend (doto (zmq/socket context :router)
                           (zmq/bind "tcp://*:5559"))
                backend (doto (zmq/socket context :dealer)
                          (zmq/bind "tcp://*:5560"))]
      (println "index "(zmq/register poller frontend :pollin))
      (println "index "(zmq/register poller backend :pollin))
      (println "what is an item? " (type (.getItem poller 0)))
        ;org.zeromq.ZMQ$PollItem
      (println "ready")
      (while (not (.. Thread currentThread isInterrupted))
        (zmq/poll poller); is this necessary? It is. One of the things it does is to check the PollItem to see if they are "ready". https://github.com/zeromq/jeromq/blob/master/src/main/java/zmq/PollItem.java
        (println "check-poller 0 " (zmq/check-poller poller 0 :pollin))
        (println "is poller.items[0] readable "
                    (.isReadable (.getItem poller 0)));this does essentially the same as check-poller.  It doesn't do the case check.
        (println "check-poller 1 " (zmq/check-poller poller 1 :pollin))
        (when (zmq/check-poller poller 0 :pollin)
          (println "frontend")
          (loop [part (zmq/receive frontend)]
            (let [more? (zmq/receive-more? frontend)]
              (println "more?" more?)
              (zmq/send backend part (if more? zmq/send-more 0))
             ;(zmq/send socket buffer flag (like send-more))
              (when more?
                (recur (zmq/receive frontend))))));sends the whole message? 
        (when (zmq/check-poller poller 1 :pollin)
          (println "backend")
          (loop [part (zmq/receive backend)]
            (let [more? (zmq/receive-more? backend)]
              (zmq/send frontend part (if more? zmq/send-more 0))
              (when more?
                (recur (zmq/receive backend))))))))))
