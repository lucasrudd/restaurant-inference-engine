(ns comparative-restaurant-functions.comparative-restaurant-functions)


; ------------------------------------------
;|                                          |
;|  Comparative functions for the           |
;|  restaurant-selection-inference-engine.  |
;|  All functions in this file are          |
;|  used to compare restaurant attributes   |
;|  to one another.                         |
;|                                          |
; ------------------------------------------
;
; -------------------
;|                   |
;|  Data-structures  |
;|                   |
; -------------------

;The restaurant record definition
(defrecord Restaurant [restaurant-name  cuisine 
                       rating           location 
                       price            BYOB 
                       atmosphere       allergens 
                       age])
  

;Each value has a weight associated to it signaling its
;relative importance in the search algorithm. Below are all
;the definitions for the weights.
;The weights are formated as a Restaurant for ease of access
;but perhaps would make more sense formatted slightly differently  
(def ^:private ^:const weights (Restaurant. 9 8 7 6 5 4 3 2 1))


; --------------------------
;|                          |
;|  Functions Declarations  |
;|                          |
; --------------------------
(declare compare-restaurant-names
         compare-rating
         String->Number
         compare-location)



(defn get-weights
  []
  
  weights)


(defn compare-restaurant-names
  "This function checks to see if the restaurant
   name contains the string the user is searching for.
   If the string is found then the function returns a
   fraction of the total amount of points depending on
   how much of the name matches the user's input.
   That is, if the user types in 'China' for the restaurant
   name, and a restaurant named 'China town' is found,
   then the function will return the the number of points
   proportional to the amount of the restaurant's name
   that matches the user input. In this case it would return
   9 * (length(China) / length(China town)  i.e. 9 * (1/2)
   This way at most the function would return 9, but can
   intellectually account for partial points for partial correction"
  [restaurant-name user-name]
  
  (let [restaurant-length (count restaurant-name)
        user-length (count user-name)]
    (if (.contains restaurant-name user-name)
      (* (double (/ user-length restaurant-length)) (:restaurant-name weights))
      0.0)))


(defn compare-rating
  "If the restaurant rating is greater than or equal
   to the user's input (i.e. x stars OR MORE) then
  a match is made."
   [restaurant-rating user-rating]
  
  (if (>= (String->Number restaurant-rating) (String->Number user-rating))
    (:rating weights)
    0))


(defn String->Number
  "Converts a string to a double"
  [s]
  
  (let [num (Double. (re-find #"\d+" s))]
    (if (number? num)
      num
      nil)))


(defn compare-location
  "Checks to see if the location of the restaurant
   is less than or equal to the user input.
   That is, the restaurant is no further than the
   distance the user inputs"
  [restaurant-location user-location]
  
  (if (<= (String->Number restaurant-location) (String->Number user-location))
    (:location weights)
    0))


