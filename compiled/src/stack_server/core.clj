
(ns stack-server.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [boot.core :refer :all]))

(deftask
  start-stack-editor!
  []
  (fn [next-handler]
    (fn [fileset]
      (let [stack-sepal-ref (atom
                              (read-string (slurp "stack-sepal.edn")))
            editor-handler (fn [request]
                             (next-handler fileset)
                             {:headers {"Content-Type" "text/edn"},
                              :status 200,
                              :body (pr-str @stack-sepal-ref)})]
        (run-jetty editor-handler {:port 7010, :join? false})
        (next-handler fileset)))))

(deftask
  only-println!
  []
  (fn [next-handler]
    (fn [fileset] (println "only print...") (next-handler fileset))))
