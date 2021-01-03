(ns soundcloud.db
  (:require [clojure.java.jdbc :as j]
            [clojure.string :as str]))

(def db {
         :dbtype "postgresql"
         :dbname "soundcloud_stats"
         :host "localhost"
         })

(defn query 
  [qry-string]
  (j/query db [qry-string]))

(defn insert-row
  [table-key payload]
  (j/insert! db table-key payload))


;; example code 

(comment 
  (println
   (sql/query db ["select * from soundcloud.users"])))

(comment 
  (def state-sql (sql/create-table-ddl :state [[:state_id :serial "PRIMARY KEY"]]))
  (println state-sql)
  (sql/execute! db state-sql))

(comment
  (sql/insert! db :soundcloud.users {
                                     :soundcloud_user_id "1234"
                                     :username "dave" 
                                     :first_name "david"
                                     :full_name "david smith"
                                     :last_name "smith"
                                     :avatar_url "test.co.uk"
                                     :city "testas"
                                     :country_code "101"
                                        ;:created_at "12/31/2020"
                                     :description "test-user"
                                     :urn ""
                                     :uri ""
                                     :permalink ""
                                     :permalink_url ""
                                     :visual_url ""}))

