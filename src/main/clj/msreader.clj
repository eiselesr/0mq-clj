;; Reading from multiple sockets
;; This version uses a simple recv loop

(ns msreader
  (:require [zeromq.zmq :as zmq]))

(defn -main []
  (let [context (zmq/zcontext)]
    (with-open [receiver (doto (zmq/socket context :pull)
                           (zmq/connect "tcp://127.0.0.1:5557"))
                subscriber (doto (zmq/socket context :sub)
                             (zmq/connect "tcp://127.0.0.1:5556")
                             (zmq/subscribe "10001"))]
      (while (not (.. Thread currentThread isInterrupted))
        (while (zmq/receive receiver zmq/dont-wait)
          (println "process task"))
        ;(let [wu-message (zmq/receive subscriber zmq/don)])
        (while (let [wu-message (zmq/receive subscriber zmq/dont-wait)]
                  (if wu-message
                    (println "process weather update" wu-message))
                  wu-message))
        (Thread/sleep 1)))))

;  (let [string (zmq/receive-str receiver)]
