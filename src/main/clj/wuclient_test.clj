(ns wuclient
  (:require [zeromq.zmq :as zmq]
            [clojure.string :as str]))

(defn message->temperature [socket]
  (-> (zmq/receive-str socket)
      (str/split #" ")
      (second)
      (#(Long/parseLong %))))
      ;(printf)))



(let [filter (or (first nil) "10001")
      context (zmq/zcontext)]
     (let [subscriber (doto (zmq/socket context :sub)
                            (zmq/connect "tcp://127.0.0.1:5556")
                            (zmq/subscribe filter))]
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
                       (.destroy context)))))

(macroexpand-1 '(let [a (doto (+ 1 2 3)
                              (println "abc"))]
                  (identity a)))

(macroexpand-1 '(doto (+ 1 2 3)
                      (println "abc")))
(doto (str "abc")
      (println "abc"))

(let [a (doto (+ 1 2 3)
              (str "abc"))]
  (identity a))

(macroexpand-1 '(doto (+ 1 2 3)
                      (println "abc")))

(comment
 "end comment")

(def context_out
  (zmq/zcontext))

(macroexpand-1
  '(doto (zmq/socket context_out :sub)
         (zmq/connect "tcp://127.0.0.1:5556")
         (zmq/subscribe filter)))
