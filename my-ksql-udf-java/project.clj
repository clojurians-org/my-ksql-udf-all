(require 'cemerick.pomegranate.aether)
(cemerick.pomegranate.aether/register-wagon-factory!
   "http" #(org.apache.maven.wagon.providers.http.HttpWagon.))

(defproject my-ksql-udf-java "0.0.1"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories [["confluent" "http://packages.confluent.io/maven/"]
                 ["confluent-snapshots" "https://s3-us-west-2.amazonaws.com/confluent-snapshots/"]]
  :plugin-repositories [["confluent" "http://packages.confluent.io/maven/"]
                        ["confluent-snapshots" "https://s3-us-west-2.amazonaws.com/confluent-snapshots/"]]
  :source-paths ["src/main/clojure"]
  :java-source-paths ["src/main/java"]

  :profiles {:provided {:dependencies [[io.confluent.ksql/ksql-common "5.0.0"]
                                       [io.confluent.ksql/ksql-udf "5.0.0"]]}
             :uberjar {:aot :all}}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/data.codec "0.1.1"]
                 [my-ksql-udf "0.0.1"]])
