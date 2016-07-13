;; Hello World client in Clojure
;; Connects REQ socket to tcp://localhost:5555
;; Sends "Hello" to server, expects "World" back

(ns rrclient
  (:require [zeromq.zmq :as zmq]))

(defn -main []
  (let [context (zmq/context 1)]
    (println "Connecting to hello world server…")
    (with-open [requester (doto (zmq/socket context :req)
                            (zmq/connect "tcp://127.0.0.1:5559"))]
      (println "ready")
      (dotimes [i 10]
        (let [request "Hello"]
          (println "Sending hello " i "…")
          (zmq/send-str requester request)
          (let [string (zmq/receive-str requester)]
            (printf "Received reply %d [%s]\n" i string)))))))
