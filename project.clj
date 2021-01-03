(defproject soundcloud "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/data.json "0.2.7"]
                 [org.clojure/java.jdbc "0.7.11"]
                 [org.postgresql/postgresql "42.2.18.jre7"]
                 [enlive "1.1.6"]
                 [clj-http "3.11.0"]
                 [http-kit "2.5.0"]
                 [oksql "1.3.2"]]
  :main ^:skip-aot soundcloud.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
