(ns app.snake
  (:require [clojure.core.async :as async]
            [clojure.core.match :refer [match]]
            [goog.events :as events]
            [goog.events.KeyCodes]
            [goog.events.EventType]))

;; 
;; 
;; model
;; 
;; 

(defn initial-model []
  {:max-x 15
   :max-y 15
   :head {:x 5 :y 6}
   :tail '({:x 4 :y 6} {:x 3 :y 6})
   :apple {:x 10 :y 6}
   :tail-length 4
   :direction :right})


;; 
;; 
;; step
;; 
;; 


(def direction->vector
  {:left {:x -1 :y 0}
   :right {:x 1 :y 0}
   :up {:x 0 :y -1}
   :down {:x 0 :y 1}})

(defn to-next-head [model]
  (merge-with + (:head model) (direction->vector (:direction model))))

(defn to-next-tail [model]
  (take (:tail-length model) (conj (:tail model) (:head model))))

(defn to-next-snake [model]
  {:head (to-next-head model) :tail (to-next-tail model)})

(defn tick [model]
  (merge model (to-next-snake model)))

(defn board-clicked [model position]
  (assoc model :tail (conj (:tail model) position)))

(defn step [msg model]
  (match msg
    [:board-clicked position]
    model
    ;; (board-clicked model position)

    [:keyboard-pressed]
    model

    [:tick]
    (tick model)

    :else
    model))




;; 
;; 
;; view
;; 
;; 

(def theme
  {:light  "#AAD751"
   :dark "#A2D149"
   :snake "#4573E8"})

(defn to-tile-color [x y]
  (if (or (and (odd? x) (even? y))
          (and (even? x) (odd? y)))
    :light
    :dark))

(defn view-tiles [model dispatch!]
  [:g
   (for [x (range 0 (:max-x model))
         y (range 0 (:max-y model))]
     ^{:key {:x x :y y}}
     [:rect {:x x
             :y y
             :width 1
             :height 1
             :stroke :none
             :onMouseDown (fn [] (dispatch! [:board-clicked {:x x :y y}]))
             :fill (theme (to-tile-color x y))}])])

(defn to-snake [model]
  (conj (:tail model) (:head model)))

(defn view-snake [model]
  [:g
   (for [position (to-snake model)]
     ^{:key position}
     [:rect {:x (:x position)
             :y (:y position)
             :width 1
             :height 1
             :stroke :none
             :fill (theme :snake)}])])

(defn view-apple [model]
  [:g
   [:rect {:stroke :none
           :x (-> model :apple :x)
           :y (-> model :apple :y)
           :width 1
           :height 1
           :fill :red}]])

(defn view [model dispatch!]
  [:svg {:x 0
         :y 0
         :width "100%"
         :height "100%"
         :onMouseDown (fn [event] (println event))
         :viewBox [0 0 (:max-x model) (:max-y model)]}
   [view-tiles model dispatch!]
   [view-snake model]
   [view-apple model]])


;; 
;; 
;; msg
;; 
;; 

(def msg-chan (async/chan))

(events/listen
 js/document
 goog.events.EventType.KEYDOWN
 (fn [event]
   (async/put! msg-chan [:keyboard-pressed event])))

(async/go
  (while true
    (async/<! (async/timeout 1000))
    (async/>! msg-chan [:tick])))