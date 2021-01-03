(ns soundcloud.core
  (:require [clojure.edn :as edn])
  (:require [clojure.java.io :as io])
  (:require [soundcloud.vanilla-scrape :as scrape])
  (:require [soundcloud.api :as api])
  (:require [clojure.string :as str])
  (:require [soundcloud.db :as sql])
  (:require [clojure.set :as set])
)

;; === CONFIGURATION ===


(def config
  (-> "soundcloud/config.edn"
      io/resource
      slurp
      edn/read-string))


(defn user-not-in-db
  [user-info]
  (let [id (get user-info "id")
        query-str (str/join [
                             "select * from soundcloud.users where soundcloud_user_id="
                             id])
        result (sql/query query-str)]
        (if (> (count result) 0) false true)))


(defn add-user-to-db
  [user-info]
  (let [user-fields [
                     "full_name"
                     "city"
                     "username"
                     "uri"
                     "urn"
                     "avatar_url"
                     "id"
                     "country_code"
                     "last_name"
                     "first_name"
                     "created_at"
                     "permalink"
                     "permalink_url"
                     "description"]
        user-info (select-keys user-info user-fields)
        user-info (set/rename-keys user-info {"id" "soundcloud_user_id"})]
        (sql/insert-row :soundcloud.users user-info)))


(defn add-row-to-usertimeline
  [user-info]
  (let [query (str/join ["select user_id from soundcloud.users where soundcloud_user_id="
                         (get user-info "id")])
        user_id (:user_id (first (sql/query query)))
        user-timeline-fields [
                              "comments_count"
                              "followers_count"
                              "followings_count"
                              "likes_count"
                              "groups_count"
                              "playlist_likes_count"
                              "playlist_count"
                              "reposts_count"
                              "track_count"]
        user-info (select-keys user-info user-timeline-fields)
        user-info (assoc user-info "user_id" user_id)]
    (if (nil? user_id)
      (throw (Exception. "Cannot add user info to soundcloud.users_timeline if user not in
                          soundcloud.users table")))
    (sql/insert-row :soundcloud.users_timeline user-info)))


(defn send-user-data-to-db
  [user-info]
  (if (user-not-in-db user-info)
    (add-user-to-db user-info))
  (add-row-to-usertimeline user-info)
)


(defn -main
  [& args]
  (let [client-id (:client-id config)
        user-ids (map #(:user-id %) (:users config))
        all-user-info (map #(api/get-user-info % client-id) user-ids)]
        (map send-user-data-to-db all-user-info))

  ;(def tracks (api/get-tracks user-num client-id))
 )
