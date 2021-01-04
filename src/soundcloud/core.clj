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
  [soundcloud-user-id]
  (let [query-str (str/join [
                             "select * from soundcloud.users where soundcloud_user_id="
                             soundcloud-user-id])
        result (sql/query query-str)]
        (if (> (count result) 0) false true)))


(defn track-not-in-db
  [soundcloud-track-id]
  (let [query-str (str/join [
                             "select * from soundcloud.tracks where soundcloud_track_id="
                             soundcloud-track-id])
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


(defn add-user-timeline-to-db
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


(defn add-track-to-db
  [track-info]
  (let [query (str/join ["select user_id from soundcloud.users where soundcloud_user_id="
                         (get track-info "user_id")])
        user_id (:user_id (first (sql/query query)))
        track-fields [
                      "id"
                      "title"
                      "genre"
                      "duration"
                      "track_format"
                      "tag_list"
                      "created_at"
                      "steamable"
                      "commentable"
                      "downloadable"
                      "had_downloads_left"
                      "description"
                      "urn"
                      "uri"
                      "permalink"
                      "permalink_url"
                      "artwork_url"
                      "waveform_url"]
        track-info (-> track-info
                       (select-keys track-fields)
                       (set/rename-keys {"id" "soundcloud_track_id"})
                       (assoc "user_id" user_id))]
    (if (nil? user_id)
      (throw (Exception. "Cannot add track info to soundcloud.tracks if user not in
                          soundcloud.users table")))
    (sql/insert-row :soundcloud.tracks track-info)))


(defn add-track-timeline-to-db
  [track-info]
  (let [query1 (str/join ["select user_id from soundcloud.users where soundcloud_user_id="
                         (get track-info "user_id")])
        user_id (:user_id (first (sql/query query1)))
        query2 (str/join ["select track_id from soundcloud.tracks where soundcloud_track_id="
                          (get track-info "id")])
        track_id (:track_id (first (sql/query query2)))
        track-timeline-fields [
                               "comment_count"
                               "playback_count"
                               "likes_count"
                               "download_count"
                               "reposts_count"]
        track-info (-> track-info
                       (select-keys track-timeline-fields)
                       (assoc "user_id" user_id "track_id" track_id))]
    (if (nil? user_id)
      (throw (Exception. "Cannot add track info to soundcloud.tracks_timeline if user not in
                          soundcloud.users table")))
    (if (nil? track_id)
      (throw (Exception. "Cannot add track info to soundcloud.tracks_timeline if track not in
                          soundcloud.tracks table")))
    (sql/insert-row :soundcloud.tracks_timeline track-info)))


(defn send-user-data-to-db
  [user-info]
  (if (user-not-in-db (get user-info "id"))
    (add-user-to-db user-info))
  (add-user-timeline-to-db user-info)
  "added user")


(defn send-track-data-to-db
  [track-info]
  (if (user-not-in-db (get track-info "user_id"))
    (throw (Exception. "Cannot add track info to soundcloud.tracks if user not in
                        soundcloud.users table")))

  (if (track-not-in-db (get track-info "id"))
    (add-track-to-db track-info))
  (add-track-timeline-to-db track-info))


(defn send-tracks-data-to-db
  [tracks]
  (map send-track-data-to-db tracks)
  "added tracks")


(defn scrape-and-write-user-data 
  [user-id client-id]
  (let [job1 (-> user-id
                 (api/get-user-info client-id)
                 send-user-data-to-db)
        job2 (-> user-id
                 (api/get-tracks client-id)
                 send-tracks-data-to-db)
        result (str/join [job1 ", " job2])]
    result))


(defn -main
  [& args]
  (let [client-id (:client-id config)
        user-ids (map #(:user-id %) (:users config))]
    (for [user-id user-ids]
      (scrape-and-write-user-data user-id client-id))))


  ;(def client-id (:client-id config))
  ;(def user-id (:user-id (first (:users config))))
  ;(def tracks (api/get-tracks user-id client-id))
  ;(add-track-to-db (first tracks))
  ;(add-track-timeline-to-db (first tracks))

