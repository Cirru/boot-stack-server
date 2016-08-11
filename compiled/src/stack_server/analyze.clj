
(ns stack-server.analyze
  (:require [clojure.string :as string] [cirru.sepal :as sepal]))

(defn generate-file [ns-line definitions procedure-line]
  (let [var-names (map
                    (fn [var-name]
                      (last (string/split var-name (re-pattern "/"))))
                    (keys definitions))
        referred-names (->>
                         (vals definitions)
                         (map (fn [tree] (subvec tree 2)))
                         (flatten)
                         (distinct)
                         (map
                           (fn [token]
                             (if (= (first token) "@")
                               (subs token 1)
                               token)))
                         (filter
                           (fn [token]
                             (println "filtering:" token var-names)
                             (contains?
                               (into (hash-set) var-names)
                               token))))
        declarations (->>
                       referred-names
                       (map (fn [var-name] ["declare" var-name]))
                       (into []))
        definition-lines (map last definitions)
        tree (into
               []
               (concat
                 [ns-line]
                 declarations
                 definition-lines
                 [procedure-line]))]
    (println "definitions:")
    (println definitions)
    (println (sepal/make-code tree)))
  "file....")

(defn collect-files [collection]
  (let [namespace-names (keys (:namespaces collection))
        file-names (map
                     (fn [namespace-name]
                       (-> namespace-name
                        (string/replace (re-pattern "\\.") "/")
                        (string/replace (re-pattern "-") "_")))
                     namespace-names)
        namespace-names' (distinct
                           (map
                             (fn [definition-name]
                               (first
                                 (string/split
                                   definition-name
                                   (re-pattern "/"))))
                             (keys (:definitions collection))))]
    (if (= (sort namespace-names) (sort namespace-names'))
      (doall
        (->>
          namespace-names
          (map
            (fn [ns-name]
              (generate-file
                (get-in collection [:namespaces ns-name])
                (->>
                  (:definitions collection)
                  (filter
                    (fn [entry]
                      (string/starts-with?
                        (key entry)
                        (str ns-name "/"))))
                  (into {}))
                (or (get-in collection [:procedures ns-name]) []))))))
      (do
        (println "Error: spaces not match!")
        (println "from definitions:" namespace-names)
        (println "from namespaces:" namespace-names')
        {}))))
