(ns zmqVersion
  (:require [zeromq.zmq :as zmq])
  (:import [org.zeromq ZMQ]))

(defn -main []
  (println (:major zmq/version))
  (println (:minor zmq/version))
  (println (:patch zmq/version))
  (println (ZMQ/getFullVersion)))
