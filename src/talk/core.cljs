(ns talk.core
    (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(println "This text is printed from src/talk/core.cljs. Go ahead and edit it and see reloading in action.")

(defn calc-pb [n]
  (case n
    8 0
    9 1
    10 2
    11 3
    12 4
    13 5
    14 7
    15 9
    100)
  )
(defn calc-total-pb [a]
  (->> a :stats vals (map calc-pb) (reduce +)))

(defn stat-pair [default kw]
  {kw default})
(def stats [:str :dex :con :int :wis :cha])

(def init-state {:stats (reduce merge (map (partial stat-pair 8) stats))})

;; define your app data so that it doesn't get over-written on reload
(defonce app-state (atom init-state :validator (fn [a] (pr-str (calc-total-pb a)) (<= (calc-total-pb a) 25))))

(swap! app-state (fn [a] (update-in a [:stats :str] (comp inc))))
(pr-str (calc-total-pb @app-state))


(def a (atom 0))
(swap! a (partial + 10))


(defn calculate-mod
  [n]
  (-> n (- 10) (/ 2) Math.floor int))
;(map calculate-mod (range 8 16))

(defn create-table-row
  [kvp]
  (let [k (key kvp)
        v (val kvp)]
    [:tr [:td k] [:td v] [:td (calculate-mod v)]
     [:td>div.btn-group
      [:button.btn.btn-sm {:on-click (fn [] (swap! app-state (fn [a] (update-in a [:stats k] (comp (partial max 8) dec)))))}"-"]
      [:button.btn.btn-sm {:on-click (fn [] (swap! app-state (fn [a] (update-in a [:stats k] (comp (partial min 15) inc)))))}"+"]
      ]
     [:td (calc-pb v)]

     ]
    ))



(defn hello-world []
  [:div.container
   [:h3 "Edit this and watch it change!"]
   [:table.table
    [:tr [:th "stat"] [:th "value"] [:th "mod"]]
    (map create-table-row (:stats @app-state))
    ]

   [:div "Total point buy: " (->> @app-state :stats vals (map calc-pb) (reduce +))]


   [:button.btn.btn-warning {:on-click (fn [] (reset! app-state init-state))} "Reset!"]

   [:div (pr-str @app-state)]
   ]
  )

(reagent/render-component [hello-world]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
