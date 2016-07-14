(ns msgqueue
  (:require [zeromq
              [zmq :as zmq]
              [device :as dev]]))

(defn -main []
  (let [context (zmq/context)
        poller (zmq/poller context 2)]
    (with-open [frontend (doto (zmq/socket context :router)
                               (zmq/bind "tcp://*:5559"))
                backend (doto (zmq/socket context :dealer)
                              (zmq/bind "tcp://*:5560"))]
      (dev/proxy context frontend backend))))
