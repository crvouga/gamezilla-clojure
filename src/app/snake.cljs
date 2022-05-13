(ns app.snake)

;; 
;; 
;; model
;; 
;; 

(defn initial-model []
  {:max-x 15
   :max-y 15
   :head {:x 5 :y 6}
   :tail #{{:x 4 :y 6} {:x 3 :y 6}}
   :apple {:x 10 :y 6}})

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

(defn view-tiles [model]
  [:g
   (for [x (range 0 (:max-x model))
         y (range 0 (:max-y model))]
     [:rect {:x x
             :y y
             :width 1
             :height 1
             :stroke "transparent"
             :fill (theme (to-tile-color x y))}])])

(defn to-snake [model]
  (conj (:tail model) (:head model)))

(defn view-snake [model]
  [:g
   (for [position (to-snake model)]
     [:rect {:x (:x position)
             :y (:y position)
             :width 1
             :height 1
             :stroke "transparent"
             :fill (theme :snake)}])])

(defn view-apple [model]
  [:g [:rect {:x (-> model :apple :x) :y (-> model :apple :y) :width 1 :height 1 :fill :red}]])

(defn view [model dispatch]
  [:svg {:x 0
         :y 0
         :width :100%
         :height :100%
         :viewBox [0 0 (:max-x model) (:max-y model)]}
   [view-tiles model]
   [view-snake model]
   [view-apple model]])


;; 
;; 
;; 
;; 
;; 

