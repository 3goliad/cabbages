(ns leaf.core
  (:require ))

(enable-console-print!)

(def canvas 
  (js/document.getElementById "illustration"))
(def ctx
  (.getContext canvas "2d"))

(defn convert-to-screen-coords [point]
  (let [[x y] point]
    [(* (+ x 1) (/ (.-width canvas) 2))
     (* (+ (* -1 y) 1) (/ (.-height canvas) 2))]))

(defn plot-polar [[x y] mag theta]
  [(+ x (* mag (js/Math.cos theta)))
   (+ y (* mag (js/Math.sin theta)))])

(defn draw-line [origin mag theta]
  (let [[x1 y1] (convert-to-screen-coords origin)
        [x2 y2] (convert-to-screen-coords
                  (plot-polar origin mag theta))]
    (set! (.-lineWidth ctx) 2)
    (set! (.-strokeStyle ctx) "green")
    (.beginPath ctx)
    (.moveTo ctx x1 y1)
    (.lineTo ctx x2 y2)
    (.stroke ctx)))

(defn draw-midrib []
  (draw-line [0 -0.75] 1.25 (/ (.-PI js/Math) 2)))

(defn draw-margin []
  (let [[x1 y1] (convert-to-screen-coords [0 -0.75])
        ctl-1 [0.7 0.3]
        ctl-2 [0.2 0.75]
        [cp1x cp1y] (convert-to-screen-coords ctl-1)
        [cp2x cp2y] (convert-to-screen-coords ctl-2)
        [cp3x] (convert-to-screen-coords 
                 [(* -1 (nth ctl-1 0)) 0])
        [cp4x] (convert-to-screen-coords 
                 [(* -1 (nth ctl-2 0)) 0])
        [x2 y2] (convert-to-screen-coords [0 0.75])]
    (.moveTo ctx x1 y1)
    (.bezierCurveTo 
      ctx 
      cp1x cp1y
      cp2x cp2y
      x2 y2)
    (.bezierCurveTo
      ctx
      cp4x cp2y
      cp3x cp1y
      x1 y1)
    (.stroke ctx)))

(defn get-space []
  nil)

(defn split-space [_]
  nil)

(defn draw-veins-space [space]
  nil)

(defn draw-veins []
  (let [space get-space
        [left right] (split-space space)]
    (draw-veins-space left)
    (draw-veins-space right)))

(def pi (.-PI js/Math))

(defn guidelines []
  (let [angle (/ pi 3)]
    (draw-line [0 -0.75] 1.25 angle)
    (draw-line [0 -0.75] 1.25 (+ pi (* -1 angle)))))

(defn draw-leaf []
  (draw-margin)
  (draw-midrib)
  (guidelines)
  #_ (draw-veins))


(defn reset []
  (.setTransform ctx 1 0 0 1 0.5 0.5)
  (.clearRect ctx 0 0 (.-height canvas) (.-width canvas)))

(defn render []
  (reset)
  (draw-leaf))

(defn resize-and-render [event]
  (let [new-height (.-innerHeight js/window)
        new-width (.-innerWidth js/window)
        new-dim (min new-height new-width)
        margin (str (/ (- new-width new-dim) 2)
                    "px")]
    (set! (.-height canvas) new-dim)
    (set! (.-width canvas) new-dim)
    (set! (.. canvas -style -marginLeft) margin)
    (set! (.. canvas -style -marginRight) margin)
    (render)))
(set! (.-onresize js/window) resize-and-render)
(resize-and-render false)

(defn on-js-reload []
  (render))
