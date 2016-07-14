(set-env!
    :resource-paths #{"src/main/clj"}
    :source-paths #{"src/main/clj"}
    :repositories #(conj % '["sonatype"
                              {:url "https://oss.sonatype.org/content/repositories/snapshots"}])
                               ;:update :always}])
    ;:repositories {"sonatype-oss-public" "https://oss.sonatype.org/content/repositories/snapshots"}
    :dependencies
      '[[me.raynes/conch "0.8.0"]
        [org.clojure/clojure "1.8.0"]
        [org.zeromq/jeromq "0.3.5"]
        [org.zeromq/cljzmq "0.1.5-SNAPSHOT"
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

(require 'zmqVersion)
(deftask get-zmqVersion []
  (with-pass-thru _
    (zmqVersion/-main)))

(require 'wuserver)
(deftask run-wuserver []
  (with-pass-thru _
    (wuserver/-main)))

(require 'wuclient)
(deftask run-wuclient [z zipcode VALUE int "the zip code"]
  (with-pass-thru _
    (wuclient/-main zipcode)))

(require 'taskvent)
(deftask run-taskvent []
  (with-pass-thru _
    (taskvent/-main)))

(require 'taskwork)
(deftask run-taskwork []
  (with-pass-thru _
    (taskwork/-main)))

(require 'tasksink)
(deftask run-tasksink []
  (with-pass-thru _
    (tasksink/-main)))

(require 'msreader)
(deftask run-msreader []
  (with-pass-thru _
    (msreader/-main)))

(require 'mspoller)
(deftask run-mspoller []
  (with-pass-thru _
    (mspoller/-main)))

(require 'rrclient)
(deftask run-rrclient []
  (with-pass-thru _
    (rrclient/-main)))

(require 'rrbroker)
(deftask run-rrbroker []
  (with-pass-thru _
    (rrbroker/-main)))

(require 'rrworker)
(deftask run-rrworker []
  (with-pass-thru _
    (rrworker/-main)))

(require 'msgqueue)
(deftask run-msgqueue []
  (with-pass-thru _
    (msgqueue/-main)))

(deftask dev ; I don't really understand this task
  "Profile setup for development.
	Starting the repl with the dev profile...
	boot dev repl "
  []
  (set-env!
    :source-paths #(into % ["dev"])
    :dependencies #(conj % '[org.clojure/tools.namespace "0.2.11"])))
  ;; Makes clojure.tools.namespace.repl work per https://github.com/boot-clj/boot/wiki/Repl-reloading
  ;(require 'clojure.tools.namespace.repl)
  ;(eval '(apply clojure.tools.namespace.repl/set-refresh-dirs
  ;              (get-env :directories)))
  ;identity;Why is this identity line here?
