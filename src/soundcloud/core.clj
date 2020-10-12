(ns soundcloud.core
  (:require [clojure.edn :as edn])
  (:require [clojure.string :as str])
  (:require [net.cgrand.enlive-html :as html]))
(import [java.net URL])

(def base-url "https://soundcloud.com")

(defn get-followers2
  [username]
  (let [url (str base-url username)
        page (html/html-resource (URL. url))
        data (html/select page [:html :head :meta])
        description (first (for [x data
                                :let [item (get-in x [:attrs :content])]
                                :when (= (:name (:attrs x)) "description")] 
                           item))] 
    description))

(defn get-followers
  [username]
  (def url (str "https://soundcloud.com" username))
  (def page (html/html-resource (URL. url)))
  (def data (html/select page [:html :head :meta]))
  (def description (first (for [x data
                               :let [item (get-in x [:attrs :content])]
                               :when (= (:name (:attrs x)) "description")] 
                           item)))
  (def description-list (str/split description #"\."))
  description-list
)

(get-followers "lovecraftofficial")
(get-followers "catorimusica")
(get-followers "pashageskin")

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "running main..."))

