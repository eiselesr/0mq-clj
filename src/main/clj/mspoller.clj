;; Reading from multiple sockets
;; This version uses zmq_poll()

(ns mspoller
  (:require [zeromq.zmq :as zmq]
            [clojure.string :as str]))

(defn message->temperature [socket]
  (-> (zmq/receive-str socket)
      (str/split #" ")
      (second)
      (#(Long/parseLong %))))

(defn -main []
  (let [context (zmq/zcontext)
        poller (zmq/poller context 2)]
    (with-open [receiver (doto (zmq/socket context :pull)
                           (zmq/connect "tcp://127.0.0.1:5557"))
                subscriber (doto (zmq/socket context :sub)
                             (zmq/connect "tcp://127.0.0.1:5556")
                             (zmq/subscribe "10001"))]
      (zmq/register poller receiver :pollin)
      (zmq/register poller subscriber :pollin)
      (println "start polling")
      (while (not (.. Thread currentThread isInterrupted))
        (zmq/poll poller); why are we doing this?
        (when (zmq/check-poller poller 0 :pollin)
          ;(let [msg (zmq/receive receiver)])
          (let [string (zmq/receive-str receiver)]
            (println string ".")
            (Thread/sleep (Long/parseLong string))
            (println "done")))
            ;; Process task

        (when (zmq/check-poller poller 1 :pollin)
          (let [msg (message->temperature subscriber)]
            (println "process weather" msg)))))))
            ;; Process weather update
