(ns app.core
  (:require [reagent.core :as r]
            [app.ui :as ui]
            [app.snake :as snake]
            [clojure.core.async :as async]))

(def state  (r/atom {:snake (snake/initial-model)}))

(defmulti step (fn [{ :keys [msg] }] (:type msg)))

(defmethod step :time-passed [{ :keys [msg, model] }]
    {:snake (snake/step msg (:snake model))})

(defmethod step :time-passed [{ :keys [msg, model] }]
    {:snake (snake/step msg (:snake model))})


(defn step [msg model]
  {:snake (snake/step msg (:snake model))})




(defn dispatch! [msg]
  ;; (println)
  ;; (println (str "before " @state))
  ;; (println msg)
  (swap! state (fn [model] (step msg model)))
  ;; (println (str "after " @state))
  ;; (println)
  )

(defn view []
  (let [model @state]
    [ui/theme-provider ui/dark-theme
     [snake/view (:snake model) dispatch!]]))


(async/go
  (while true
    (let [snake-msg (async/<! snake/msg-chan)]
      (dispatch! snake-msg))))


(defn ^:dev/after-load render
  "Render the toplevel component for this app."
  []
  (r/render [view] (.getElementById js/document "app")))


(defn ^:export main
  "Run application startup logic."
  []
  (render))



(defrecord User [user-id])


(defrecord UserDb)

(defprotocol UserStore
  (get-one-by-id [user-id])
  (search-by-name)
  )

