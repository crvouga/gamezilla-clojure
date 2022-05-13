(ns app.core
  (:require [reagent.core :as r]
            [app.ui :as ui]
            [app.snake :as snake]))

(def state  (r/atom {:snake (snake/initial-model)}))

(defn view [model]
  [ui/theme-provider ui/dark-theme
   [snake/view (:snake model)]])

(defn ^:dev/after-load render
  "Render the toplevel component for this app."
  []
  (r/render [view @state] (.getElementById js/document "app")))


(defn ^:export main
  "Run application startup logic."
  []
  (render))
