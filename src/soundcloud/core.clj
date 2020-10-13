(ns soundcloud.core
  (:require [clojure.edn :as edn])
  (:require [clojure.string :as str])
  (:require [net.cgrand.enlive-html :as html]))
(import [java.net URL])

(defn extract-data-from-str
  "When a string includes the 'lookup' word 
  then take the first word from that string"
  [lookup item]
  (when (str/includes? item lookup)
    (-> item
        (str/trim)
        (str/split #" ")
        first)))

(defn extract-metric-from-description
  "For a given metric, extract that metric from the 
  user description"
  [metric description-list]
    (->> description-list
         (map (partial extract-data-from-str metric))
         (remove nil?)
         first))

(defn convert-description-to-map
  [username description]
  (let [description-list (str/split description #"\.")
        followers (extract-metric-from-description "Followers" description-list)
        tracks (extract-metric-from-description "Tracks" description-list)
        metrics {:username username :tracks tracks :followers followers}]
    metrics))

(defn get-user-info
  [username]
  (let [url (str base-url username)
        page (html/html-resource (URL. url))
        data (html/select page [:html :head :meta])
        description (first (for [x data
                                :let [item (get-in x [:attrs :content])]
                                :when (= (get-in x [:attrs :name]) "description")] 
                           item))
        user-info (convert-description-to-map username description)]
    user-info))

(def base-url "https://soundcloud.com/")

(def users 
  '("lovecraftofficial" 
    "catorimusica" 
    "pashageskin"))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Soundcloud user profiles:")
  (pprint (map get-user-info users)))
