(ns restaurant-inference-engine.core
  (:require [clojure.string :as str]
            [restaurant-inference-engine.priority-map :refer :all]
            [comparative-restaurant-functions.comparative-restaurant-functions :refer :all]
            [restaurant-gui.core :refer :all]
            [clojure.core.async :as a 
                                :refer [>! <! >!! <!! go chan buffer close!]])
  (:import  (java.io BufferedReader FileReader)))

; :author Lucas Rudd
; :using material written by Mark Engelberg and obtained from
;        https://github.com/clojure/data.priority-map
;
; -----------------------------------------------------
;|                                                     |
;|  A Restaurant Selection Inference Engine.           |
;|  For more information see doc/Restaurant Attributes |
;|  and README.md                                      |
;|                                                     |
; -----------------------------------------------------
;
;
; -----------------------------
;|                             |  
;|  All variable declarations  |
;|                             |
; -----------------------------
(def ^:dynamic relevance-value)
(def ^:dynamic restaurant-vector)


;Below are some constants for the user-input array.
;These constants are used so accessing the user-input array
;is more descriptive, less ambiguous, and easier to alter
;if need be
(def ^:const attribute-keys [:restaurant-name :cuisine 
                             :rating          :location 
                             :price           :BYOB 
                             :atmosphere      :allergens 
                             :age])



; -----------------------------
;|                             |
;|  All function declarations  |
;|                             |
; -----------------------------

;Function declarations are ordered such
;that they tell the story of what happens
;in the program.
(declare process-file
         restaurant
         format-data
         create-restaurant-map
         search
         ;compare-restaurant-names
         ;compare-rating
         ;compare-locations
         calculate-relevance
         ;String->Number
         how-equal?
         print-top-ten)



; -----------------
;|                 |
;|  Start Program  |
;|                 |
; -----------------

;TODO: Refactor main
(defn -main
  [& args]
  
  (let [restaurant-data (process-file "resources/RestaurantDatabase.txt")
        window @window-setup]
    (draw-window window)
    (go
      (loop [input-values (map->Restaurant (create-restaurant-map (<! user-input)))]
        (let [ordered-restaurant-names (search restaurant-data input-values)]
          (print-top-ten ordered-restaurant-names)
          (recur (map->Restaurant (create-restaurant-map (<! user-input)))))))))


;TODO: Change binding, doseq, and set! functions to a single recursive loop
(defn process-file
  "This function takes a file and reads each line from it.
   The lines are then split by the commas and made into a Restaurant
   The Restaurant is then put at the end of the restaurant-vector.
   After the whole file is read restaurant-vector is returned"
  [filename]
  
  (binding [restaurant-vector (vector)]
    (with-open [reader (BufferedReader. (FileReader. filename))]
      (doseq [line (line-seq reader)] 
        (let [split-line (clojure.string/split line #",")]
          (set! restaurant-vector (conj restaurant-vector (restaurant split-line)))))     
    restaurant-vector)))


(defn restaurant
  "Works like a constructor to create a new restaurant from
   a line with all the restaurant data"
  [line-with-restaurant-data]
  
  (map->Restaurant (create-restaurant-map (format-data line-with-restaurant-data))))


(defn format-data
  "A function simply to trim the data gotten from 
   a text file and make sure it is of type 'string'"  
   [line]
  
   (loop [remaining-words line 
          formated-string []]
     (if (empty? remaining-words)
       formated-string
       (let [[head & remaining] remaining-words] 
         (recur remaining
                (conj formated-string (str (.trim head))))))))


(defn create-restaurant-map
  "Creates a map dynamically to put into the restaurant constructor"
  [value-vector]
  
  (loop [remaining-keys attribute-keys
         remaining-values value-vector
         record-map {}]
    (if (or (empty? remaining-keys) (empty? remaining-values))
      record-map
      (let [[key-head & remaining-key] remaining-keys
            [value-head & remaining-value] remaining-values]
        (recur remaining-key remaining-value 
             (conj record-map {key-head value-head}))))))


(defn search
  "This function takes a vector of restaurants and user inputs.
   Using this data, the function attempts to find the best 
   matches for a particular number of inputs. The function 
   creates a new variable 'ordered-restaurants' (which is a 
   priority-map from largest value to smallest value) and 
   organizes the results in order of most relevant to least relevant
   accessible by restaurant name."
  [restaurants user-restaurant]
  
  (loop [remaining-restaurants restaurants
         ordered-restaurants (priority-map-by >)]
    (let [[head & remaining] remaining-restaurants]
      (if (empty? head)
        ordered-restaurants
        (recur remaining
               (assoc ordered-restaurants (keyword (:restaurant-name head)) (calculate-relevance head user-restaurant)))))))

  



;TODO: Figure out a way to write the if statement embedded in the
;let statement as a single if-let statement.
(defn calculate-relevance
  "Calls how-equal? and makes sure
   that the response is valid."
  [restaurant user-restaurant]
  
  (let [restaurant-relevance-value (how-equal? restaurant user-restaurant)]
    (if 
      (neg? restaurant-relevance-value) (println "Error, relevance is less than zero")
       restaurant-relevance-value)))


;TODO: Figure out a way to write write the binding, doseq, and set! functions
;as a single recursive loop statment.
;TODO: At the moment how-equal? checks if things are EXACTLY equal.
;how-equal? needs to check to see if a restaurant is 'x' miles OR CLOSER
;if a restaurant is 'x' stars OR MORE and if a restaurant is 'x' price
;OR LESS. Also check to see if PART of the name matches, not the whole thing.
;For example, if I have a restaurant called My-Restaurant, a partial match should
;be made if I search "Restaurant." This will need to use the weight system
;and give 9 points for a total match, and 4.5 points for half a match
;2.75 for a quarter match etc. Consider a 100 points scale to avoid FP values
(defn how-equal?
  "The following two functions (how-equal? and calculate-relevance) work 
   in tandum to calculate how similar two restaurants are to one another. 
   This similarity is then mapped to a relevance value for sorting"
  [restaurant user-restaurant]
  
  (binding [relevance-value 0]
    (doseq [attribute restaurant]
      (cond
        (= :restaurant-name (key attribute)) (set! relevance-value (+ relevance-value (compare-restaurant-names (val attribute) ((key attribute)user-restaurant))))
        (= :rating (key attribute)) (set! relevance-value (+ relevance-value (compare-rating (val attribute) ((key attribute)user-restaurant))))
        (= :location (key attribute)) (set! relevance-value (+ relevance-value (compare-location (val attribute) ((key attribute)user-restaurant))))
        (= (val attribute) ((key attribute)user-restaurant)) (set! relevance-value (+ relevance-value ((key attribute)(get-weights))))))
      (double relevance-value)))



;TODO: Rewrite this so that it dosen't loop over an index
;Use the take function or a recurssion of first & rest operations
(defn print-top-ten
  "This simple function just puts
   the top ten restaurants, which
   are most relevant to the user criteria,
   into a vector and draws an additional
   temporary window which does not
   exit the program on close"
  [sorted-restaurant-names]
  
  (loop [i 0
         content-vector []]
    (if (> i 10)
      (draw-temp-window content-vector)
      (recur (inc i) (conj content-vector (name (key (nth (seq sorted-restaurant-names) i))))))))


