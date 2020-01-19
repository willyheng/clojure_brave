(ns brave.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

(defn treasure-location
  [{:keys [lat lng]}]
  (println (str "Treasure lat: " lat))
  (println (str "Treasure lng: " lng))
)

(treasure-location {:lat 25 :lng 21.5})

(map #(str "Hi " %) ["John" "Tim"])

(#(str "Please buy " %1 " and " %2) "eggs" "cheese")


(def asym-hobbit-body-parts [{:name "head" :size 3} 
                             {:name "left-eye" :size 1} 
                             {:name "left-ear" :size 1}
                             {:name "mouth" :size 1}
                             {:name "nose" :size 1}
                             {:name "neck" :size 2}
                             {:name "left-shoulder" :size 3}
                             {:name "left-upper-arm" :size 3}
                             {:name "chest" :size 10}
                             {:name "back" :size 10}
                             {:name "left-forearm" :size 3} 
                             {:name "abdomen" :size 6}
                             {:name "left-kidney" :size 1}
                             {:name "left-hand" :size 2}
                             {:name "left-knee" :size 2}
                             {:name "left-thigh" :size 4}
                             {:name "left-lower-leg" :size 3}
                             {:name "left-achilles" :size 1}
                             {:name "left-foot" :size 2}])

(defn matching-part
  [part]
  {:name (clojure.string/replace (:name part) #"^left-" "right-")
   :size (:size part)})

(matching-part {:name "left-arm" :size 1})

(defn symmetrize-body-parts
  "Expects a seq of maps that have :name and :size"
  [asym-body-parts]
  (loop [remaining-asym-parts asym-body-parts final-body-parts []]
    (if (empty? remaining-asym-parts)
      final-body-parts
      (let [[part & remaining] remaining-asym-parts]
        (recur remaining
               (into final-body-parts
                     (set [part (matching-part part)])))))))


(symmetrize-body-parts asym-hobbit-body-parts)


(defn better-symmetrize
  [asym-body-parts]
  (reduce (fn [final-body-parts part]
            (into final-body-parts (set [part (matching-part part)])))
          [] asym-body-parts))

(better-symmetrize asym-hobbit-body-parts)

(defn hit
 [asym-body-parts]
 (let [full-body-parts (better-symmetrize asym-body-parts)
       total-body-size (reduce + (map :size full-body-parts))
       target (rand total-body-size)]
   (loop [[part & remaining] full-body-parts
          accumulated-size (:size part)]
     (if (> accumulated-size target)
       part
       (recur remaining (+ accumulated-size (:size (first remaining))))))))

(hit asym-hobbit-body-parts)


;; Chapter 3 exercise

;Q1
(str "a" "b")
(vector 1 2 3)
(list 1 2 3)
(hash-map :a 1 :b 2 :c 3)
(hash-set 1 2 1 3)

;Q2
(defn inc100
  "Increments by 100"
  [x]
  (+ x 100))

;Q3
(defn dec-maker
  "Creates decrement functions"
  [n]
  #(- % n))

(def dec9 (dec-maker 9))
(dec9 100)


;Q4
(defn mapset
  "Map but returns set"
  [f inputs]
  (set (map f inputs)))

(mapset inc [1 1 2 2])

;Q5
(defn radial-parts
  [part]
  (map #(hash-map :name (clojure.string/replace (:name part) #"^left-" %)
               :size (:size part))
        ["left1-" "left2-" "centre-" "right1-" "right2-"]))

(radial-parts {:name "left-arm" :size 5})

(defn symmetrize-radial
  "Creates radial symmetry for body parts"
  [asym-body-parts]
  (map radial-parts asym-body-parts))

(symmetrize-radial asym-hobbit-body-parts)

;Q6
(defn n-parts
  [part n]
  (map #(hash-map :name (clojure.string/replace (:name part)
                                                 #"^left-"
                                                 (str "P" % "-"))
              :size (:size part))
            (range 1 (inc n))))

(n-parts {:name "left-arm" :size 3} 4)
(n-parts {:name "head" :size 5} 5)

(defn symmetrize-reduce
  [asym-body-parts n]
  (reduce (fn [final-body-parts part]
            (into final-body-parts (n-parts part n)))
          []
            asym-body-parts))

(defn symmetrize-map
  [asym-body-parts n]
  (reduce into (map #(n-parts % n) asym-body-parts)))


(symmetrize-map asym-hobbit-body-parts 3)
