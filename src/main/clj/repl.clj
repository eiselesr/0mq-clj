(cons 1 (+ 1 1))

(lazy-seq [1 2 3])


(loop [a 1 b 1]
  (recur b (+ a b)))

(lazy-seq (cons a (fib b (+ b a))))

(defn fib [a b]
          (cons a (lazy-seq (fib b (+ a b)))))

(take 5 (fib 1 1))

(take 5
  ((fn fib [a b]
    (cons a (lazy-seq (fib b (+ a b))))) 1 1))

((fn [x]
  (reverse
    (filter #(> % 0)
      (loop [cnt (- x 1) l '(1 0)]
        (if (zero? cnt)
          l
          (recur (dec cnt)
                 (#(conj %
                        (+ (first %) (second %)))
                  l)))))))
 8)

(loop [lst [1 1]]
      (if (>= (count lst) 3)
        lst
        (recur (conj lst (+' (inc (last lst)) (dec (last (butlast lst))))))))

(macroexpand-1 (doto "hi" .toUpperCase))

(use 'clojure.pprint)
(clojure.pprint/pprint
  (macroexpand-1 '(doto "hi" .toUpperCase)))

(seq {:key 4 :key2 8})
