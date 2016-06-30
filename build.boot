(set-env!
    :resource-paths #{"src/main/clj"}
    :source-paths #{"src/main/clj"}
    :dependencies
      '[[me.raynes/conch "0.8.0"]
        [org.clojure/clojure "1.8.0"]
        [org.zeromq/jeromq "0.3.5"]
        [org.zeromq/cljzmq "0.1.4"
               :exclusions [org.zeromq/jzmq]]
        [proto-repl "0.1.2"]
        [proto-repl-charts "0.2.0"]]
    :jvm-opts ["-Djava.library.path=/usr/lib:/usr/local/lib"])

(task-options!
    pom {:project 'zmp-clj
         :version "0.1.0"}
    jar {:manifest {"Foo" "bar"}})

(require 'hwserver)
(deftask run-server []
  (with-pass-thru _
    (hwserver/-main)))

(require 'hwclient)
(deftask run-client []
  (with-pass-thru _
    (hwclient/-main)))

(require 'wuserver)
(deftask run-wuserver []
  (with-pass-thru _
    (wuserver/-main)))

(require 'wuclient)
(deftask run-wuclient []
  (with-pass-thru _
    (wuclient/-main)))

(deftask dev ; I don't really understand this task
	"Profile setup for development.
	Starting the repl with the dev profile...
	boot dev repl "
	[]
	(set-env!
		:source-paths #(into % ["dev"])
		:dependencies #(conj % '[org.clojure/tools.namespace "0.2.11"]))
  ;; Makes clojure.tools.namespace.repl work per https://github.com/boot-clj/boot/wiki/Repl-reloading
  ;(require 'clojure.tools.namespace.repl)
  ;(eval '(apply clojure.tools.namespace.repl/set-refresh-dirs
  ;              (get-env :directories)))
	;identity;Why is this identity line here?
  )
