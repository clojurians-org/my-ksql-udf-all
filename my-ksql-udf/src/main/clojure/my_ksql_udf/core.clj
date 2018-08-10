(ns my-ksql-udf.core)

(def save-ns *ns*)
(do
  (require '[clojure.data.codec.base64 :as b64])
  (require '[clojure.java.io :as io])
  (require '[clojure.string :as s])
  (require '[clojure.edn :as edn])
  (import '[java.io ByteArrayOutputStream])
  (import '[java.util.zip GZIPInputStream GZIPOutputStream])
  )

(defn larluo [s] (-> s first str))
(def read-edn edn/read-string)
(def str2bs (memfn getBytes))
(defn bs2str [bs] (String. bs))
(def b64-encode b64/encode)
(def b64-decode b64/decode)
(defn gzip [bs] 
  (with-open [buf (ByteArrayOutputStream.)
              gout (GZIPOutputStream. buf)]
    (doto gout (.write bs) .finish .flush)
    (-> buf .toByteArray) )
  )
(defn gunzip [bs] 
  (with-open [gin (-> bs io/input-stream GZIPInputStream.)
              buf (ByteArrayOutputStream.)]
    (io/copy gin buf)
    (-> buf .toByteArray))
  )

#_(defn mk-fn [fn-str] (->> fn-str symbol (ns-resolve 'my-ksql-udf.core)))
(defn mk-fn [fn-str] (->> fn-str symbol (ns-resolve save-ns)))
(gen-class :name my_ksql_udf.Comp :main false :prefix "comp-"
           :methods [^:static [compile [String] Object]
                     ^:static [run [Object String] String]])
(defn comp-compile [fns-str] (delay (apply comp (map mk-fn (s/split fns-str #"\." )) )) )
(defn comp-run [comp-obj args-str]  (-> args-str (@comp-obj)  str))

(comment
  (def comp1 (comp-compile "bs2str.b64-encode.gzip.str2bs")) 
  (def comp2 (comp-compile "bs2str.gunzip.b64-decode.str2bs"))
  (->> "larluo"
       (comp-run comp1)
       (comp-run comp2)
       )
  (import '[my_ksql_udf Comp])
  (def comp3 (Comp/compile "bs2str.b64-encode.gzip.str2bs")) 
  (Comp/run comp3 "larluo")
  ;; SELECT comp("bs2str.b64-encode.gzip.str2bs", "larluo") 
  (compile 'my-ksql-udf.core)
  )
