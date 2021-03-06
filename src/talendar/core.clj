(ns talendar.core
  (:use [quil.core]
        [talendar.grid]
        [talendar.toda :as toda]
        [talendar.videoloader :as vl])

  (:import [javax.swing JFileChooser]
           [codeanticode.gsvideo GSMovie])
;  (:require [clojure.core.async :as async :refer [<! >! <!! timeout chan alt! go]])
  )





(declare  vl-1 setup raton key-press draw)
(defsketch example
  :title "j"
  :setup setup
  :draw draw
  :size [size-w size-h]
  :mouse-pressed raton
  :key-typed key-press
  )
(def  img (atom nil))
(def click (atom [-1 -1]))

(defn cdata []
  (comment
    {:peli "/Users/juanantonioruz/Desktop/hoy/misPelis/giro en bici.MOV"
     :month 1
     :soft [15 150 250 200]
     :intensive [ 255 255 0]
     :text-month [15 150 250 200]}
    {:peli "/Users/juanantonioruz/Desktop/hoy/misPelis/canoa.MOV"
     :month 2
     :soft [15 150 250 100]
     :intensive [255 50 0]
     :text-month [255 50 0]}
    {:peli "/Users/juan_mini/Desktop/discoLacie_copia_unica/JUANITU/misPelis/P1040687.MOV"
     :month 3
     :soft [255 150 80 100]
     :intensive [75 ]
     :text-month [255 150 0]}
    {:peli "/Users/juan_mini/Desktop/discoLacie_copia_unica/JUANITU/misPelis/giro 360 de palmera.MOV"
     :month 4
     :soft [255 50 130 170]
     :intensive [255 ]
     :text-month [255 50 130]}
    {:peli "/Users/juan_mini/Desktop/discoLacie_copia_unica/JUANITU/misPelis/P1040689.MOV"
     :month 5
     :soft [255  170]
     :intensive [255 ]
     :text-month [100]}
    {:peli "/Users/juan_mini/Desktop/discoLacie_copia_unica/JUANITU/misPelis/P1040733.MOV"
     :month 6
     :soft [0 100 255  70]
     :intensive [255 255 0 180]
     :text-month [100]}
      {:peli "/Users/juan_mini/Desktop/discoLacie_copia_unica/JUANITU/misPelis/P1040668.MOV"
     :month 7
     :soft [50 200 250  170]
     :intensive [50  100]
       :text-month [100 200 0]}
      {:peli "/Users/juan_mini/Desktop/discoLacie_copia_unica/JUANITU/misPelis/P1040734.MOV"
     :month 8
     :soft [50 200 250  170]
     :intensive [50  100]
       :text-month [100 200 0]}
      {:peli "/Users/juan_mini/Desktop/discoLacie_copia_unica/JUANITU/misPelis/P1030637.MOV"
     :month 9
     :soft [50 200 250  170]
     :intensive [250 100]
       :text-month [50 200 250]}
      {:peli "/Users/juan_mini/Desktop/discoLacie_copia_unica/JUANITU/misPelis/patada a naranja.MOV"
     :month 10
     :soft [255 1 200  90]
     :intensive [255]
       :text-month [150]}
      {:peli "/Users/juan_mini/Desktop/discoLacie_copia_unica/JUANITU/misPelis/lapiz.MOV"
     :month 11
     :soft [55 155 200  200]
     :intensive [255]
     :text-month [150]}
      {:peli "/Users/juan_mini/Desktop/discoLacie_copia_unica/JUANITU/misPelis/P1040663.MOV"
     :month 12
     :soft [100 100 255  70]
     :intensive [0 255 255 180]
     :text-month [100]}
    {:peli "/Users/juan_mini/Desktop/discoLacie_copia_unica/JUANITU/misPelis/eric cuenta como una vieja.MOV"
     :month 12
     :soft [200 100 55  170]
     :intensive [0]
     :text-month [100 100]}
    )
      {:peli "/Users/juan_mini/Desktop/discoLacie_copia_unica/JUANITU/misPelis/marco arena 1.MOV"
     :month 2
     :soft [55 155 200  100]
     :intensive [255]
     :text-month [150]}
  )

(defn gcolor
  ([]
     (gcolor :intensive))
  ([kkey]
     (if (= kkey :soft)
       (:soft (cdata) )
       (:intensive (cdata)))))
(def jan
  ((vec (toda/get-year-data 2014)) (dec ((cdata) :month))) )
(defn setup []

  (comment let [fc (JFileChooser.)
        rv (.showOpenDialog fc example) ]
    (when (zero? rv)
      (let [file (.getSelectedFile fc)]
        (reset! img (load-image "/Users/juan_mini/load.png"))
        )))

  (def vl-1 (vl/set-up example ((cdata) :peli) (:days-on-month jan)))
  (println (:number vl-1))

  )

(defn text-day
  ([n x y c]
     (text-size 10)
     (push-style)
     (apply fill c)
     (text (str n) x y)
     (pop-style))
  ([n x y]
     (text-day n x y [0])
     )
  )

(defn gtitle []
  [((cdata) :month) (- size-w 80) (- size-h 50)  ((cdata) :text-month)])

(defn draw []

  (while (vl/is-loading-movie? (:movie vl-1) (:frames vl-1) (:number vl-1))

    (println "loadding frames"))
  (frame-rate 15)

;  (stroke 250)
  (no-stroke)
  (if (not= (count @(:frames vl-1)) (count (:number vl-1)))

      (println "loading")
      (let [limit-grid (:days-on-month jan)]
        (dorun (map (fn [[x y]]
                (let [current-rect (coords x y)
                      rect-number (get-rect-number x y)
                      rect-number-clicked  (dec (get-rect-number @click))
                      cal-day (- rect-number rect-number-clicked)
                      possible-day? (and (> cal-day 0) (<= cal-day limit-grid))
                      ]
                  (if (>= 36 (+ rect-number-clicked limit-grid))
                    (if possible-day?
                      (do               ;selected
                        (if (is-sunday? cal-day (:month (cdata)) 2014)
                          (fill 255)
                          (fill 255)
                          )
                           (paint current-rect)
                        )
                      (do
                        (fill 255)
                        (paint current-rect)
                        ))
                    (do
                      (fill 255)
                      (paint current-rect))
                    )
                  (when possible-day?


                    (image (:img ((vec (sort-by :nframe @(:frames vl-1))) (dec cal-day))) (current-rect :xx) (current-rect :yy) r-w r-h)
                    (when (is-sunday? cal-day (:month (cdata)) 2014)
                      (apply fill (gcolor :soft))
                      (paint current-rect)
                      )


                    (text-day cal-day (+ (:xx current-rect) (dec (/ r-w 2))) (+  (:yy current-rect) (- r-h 6)) (gcolor :intensive)) )

                  ) ) data-36-days ))))
 (apply text-day (gtitle) )
  )

(defn get-selected-rect [[x y]]
  "returns a vector indexed in 0 [0 0] indicating col and row"
  [ (dec (first (filter #(and (>= (* % r-w) x) (<=  (* (dec %) r-w) x)) (range 1 7))))
    (dec (first  (filter #(and (>= (* % r-h) y) (<=  (* (dec %) r-h) y)) (range 1 7))))]
  )

(defn raton []
  (reset! click (get-selected-rect [(mouse-x) (mouse-y)]))

  (comment (let [fc (JFileChooser.)
         rv (.showOpenDialog fc example) ]
     (when (zero? rv)
       (let [file (.getSelectedFile fc)]
         (reset! img (load-image (.getPath file)))
         ))))
  (println @click)

  )
(defn key-press []
  (println (str "Key pressed: " (raw-key)))
  (save "jolin3.jpg")
)
