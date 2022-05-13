(ns app.core
  (:require [reagent.core :as r]
            [app.ui :as ui]
            [app.snake :as snake]))

(def state  (r/atom {:snake (snake/initial-model)}))

(defn step [msg model]
  {:snake (snake/step msg (:snake model))})

(defn dispatch! [msg]
  (swap! state (fn [model] (step msg model))))

(defn view []
  (let [model @state]
    [ui/theme-provider ui/dark-theme
     [snake/view (:snake model) dispatch!]]))


(defn ^:dev/after-load render
  "Render the toplevel component for this app."
  []
  (r/render [view] (.getElementById js/document "app")))


(defn ^:export main
  "Run application startup logic."
  []
  (render))
