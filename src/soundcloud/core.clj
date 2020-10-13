(ns soundcloud.core
  (:require [soundcloud.vanilla-scrape :as scrape]))

(def users 
  '("lovecraftofficial" 
    "catorimusica" 
    "pashageskin"))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Soundcloud user profiles:")
  (pprint (map scrape/get-user-info users)))
