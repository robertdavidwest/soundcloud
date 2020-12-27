(ns soundcloud.api
  (:gen-class)
  (:require [clojure.data.json :as json])
  (:require [clj-http.client :as client])
  (:require [clojure.edn :as edn])
  (:require [clojure.string :as str])
)


(def base-url "https://api-v2.soundcloud.com/users/")

(defn get-user-url
  [user-num client-id]
  (str base-url user-num "?client_id=" client-id))

(defn get-tracks-url
  [user-num client-id]
  (str base-url user-num "/tracks?client_id=" client-id))

(defn get-user-info
  [user-num client-id]
  (let [url (get-user-url user-num client-id) 
        response (client/get url)
        status-code (:status response)
        body (:body response)
        _json (json/read-str body)
        ]
    _json))

(defn get-tracks
  [user-num client-id]
  (let [url (get-tracks-url user-num client-id) 
        response (client/get url)
        status-code (:status response)
        body (:body response)
        _json (json/read-str body)
        collection (get _json "collection")
        ]
    collection))
