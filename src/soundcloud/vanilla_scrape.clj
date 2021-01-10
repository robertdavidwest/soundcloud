(ns soundcloud.vanilla-scrape
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
  [metric description]
    (let [description-list (str/split description #"\.")]
          (->> description-list
              (map (partial extract-data-from-str metric))
              (remove nil?)
              first)))

(defn get-user-info
  [username]
  (let [base-url "https://soundcloud.com/"
        url (str base-url username)
        page (html/html-resource (URL. url))
        data (html/select page [:html :head :meta])
        desc (first (for [x data
                                :let [item (get-in x [:attrs :content])]
                                :when (= (get-in x [:attrs :name]) 
                                         "description")] 
                           item))
        followers (extract-metric-from-description "Followers" desc)
        tracks (extract-metric-from-description "Tracks" desc)
        metrics {:username username :tracks tracks :followers followers}]
    metrics))

(defn get-user-id
  [username]
  (let [base-url "https://soundcloud.com/"
        url (str base-url username)
        page (html/html-resource (URL. url))
        data (html/select page [:html :head :meta])
        has-user-id (first (for [x data
                                :let [item (get-in x [:attrs :content])]
                                :when (= (get-in x [:attrs :property]) 
                                      "twitter:app:url:googleplay")]
                             item))
        user-id (last (str/split has-user-id #":"))]
    user-id))



