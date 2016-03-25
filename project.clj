(defproject restaurant-inference-engine "0.1.0"
  :description "FIXME: write description"
  :url "https://github.com/lucasrudd/restaurant-inference-engine"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.async "0.2.374"]
                 [com.github.insubstantial/substance "7.1"]
                 [seesaw "1.4.4"]]
  :main restaurant-inference-engine.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
