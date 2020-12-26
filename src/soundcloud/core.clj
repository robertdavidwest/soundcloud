(ns soundcloud.core
  (:require [clojure.edn :as edn])
  (:require [clojure.java.io :as io])
  (:require [soundcloud.vanilla-scrape :as scrape])
  (:require [soundcloud.api :as api]))

;; === CONFIGURATION ===



(def config
  (-> "soundcloud/config.edn"
      io/resource
      slurp
      edn/read-string))

(def users 
  '("lovecraftofficial" 
    "catorimusica" 
    "pashageskin"))

(comment 
  (defn -main
    [& args]
    (println "Soundcloud user profiles:")
    (pprint (map scrape/get-user-info users))))

(defn -main
  [& args]
  (def tracks 
    (let [first-user (first (:users config))
        user-num (:user-id first-user)
        client-id (:client-id config)
        tracks (api/get-tracks user-num client-id)]
    tracks))

  (println (keys (first tracks)))
)
