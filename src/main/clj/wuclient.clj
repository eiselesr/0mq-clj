(ns wuclient
  (:require [zeromq.zmq :as zmq]
            [clojure.string :as str]))

(defn message->temperature [socket]
  (-> (zmq/receive-str socket)
      (str/split #" ")
      (second)
      (#(Long/parseLong %))))
      ;(printf)))


(defn -main [& args]
  (time
    ;(println "Collecting updates from weather server...")
    (let [filter (or (first args) "10001")
          context (zmq/zcontext)]
      (let [subscriber (doto
                        (zmq/socket context :sub)
                        (zmq/connect "tcp://127.0.0.1:5556")
                        (zmq/subscribe filter))]
          ;internally the above says
          ;(clojure.core/let
          ; [G__4902 (zmq/socket context_out :sub)]
          ; (zmq/connect G__4902 "tcp://127.0.0.1:5556")
          ; (zmq/subscribe G__4902 filter)
          ; G__4902)

        (try
          (let [times 10
                temps (repeatedly times
                        (partial message->temperature subscriber))
                avg (int (/ (apply + temps) (count temps)))]
            (apply println temps)
            (printf "Average temperature for zipcode is '%s' was %d\n"
                      filter avg))
         (finally
          (.destroySocket context subscriber)
          (.destroy context)))))))
