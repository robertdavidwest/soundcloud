(ns soundcloud.update-config
  (:require [clojure.pprint :only (pprint)])
  (:require [clojure.edn :as edn])
  (:require [clojure.string :as str])
  (:require [clojure.java.io :as io])
  (:require [soundcloud.vanilla-scrape :as scrape]))

(def backup-dir "config-backups/")
(def config-path (io/resource "soundcloud/config.edn"))
(defn now [] (new java.util.Date))

(defn backup-config
  [config]
  (let [now-str (.toString (now))
        outpath (str/join [backup-dir now-str ".edn"])]
    (write-config config outpath)))

(defn get-config
  []
  (-> "soundcloud/config.edn"
      io/resource
      slurp
      edn/read-string))

(defn write-config
  [config path]
  (let [prepared (-> config
                     pprint
                     with-out-str)]
    (println prepared)
    (spit path prepared)))

(defn add-user-to-config
  [username]
  (let [user-id (scrape/get-user-id username)
        config (get-config)
        user-list (conj (:users config) (hash-map :username username :user-id user-id))
        new-config (assoc config :users user-list)]
    (backup-config config)
    (write-config new-config config-path)))

