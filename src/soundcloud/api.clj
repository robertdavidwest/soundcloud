(ns soundcloud.api
  (:gen-class)
  (:require [clojure.data.json :as json])
  (:require [clj-http.client :as client])
  (:require [clojure.edn :as edn])
  (:require [clojure.string :as str])
  ;(:require [net.cgrand.enlive-html :as html])
  )
;(import [java.net URL])


(defn get-tracks-url
  [user-num client-id]
  (let [base-url "https://api-v2.soundcloud.com/users/"
        url (str base-url user-num "/tracks?client_id=" client-id)]
    url))

(defn get-tracks
  [user-num client-id]
  (let [url (get-tracks-url user-num client-id) 
        response (client/get url)
        status-code (:status response)
        body (:body response)
        _json (json/read-str body)
        collection (get _json "collection")
        ]
    (comment (when (= status-code 200)
               tracks))
    collection))
