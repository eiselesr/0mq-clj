(ns wuserver
  (:require [zeromq.zmq :as zmq]))

(defn main[]
  (let [context (zmq/zcontext)]()))
    ;(with-open [publisher])))
