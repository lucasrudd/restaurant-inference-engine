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
(declare String->Number)




(defmulti compare-attribute
  "This mulit method takes in an attribute key and value pair
   and a single user attribute. Then, based on the type of
   attribute that is passed, the multimethods execute the
   corresponding code. The restaurant-name method
   checks to see if the restaurant name contains 
   the string the user is searching for. If the string is 
   found then the function returns a fraction of the total 
   amount of points depending on how much of the restaurant
   name matches the user's input.
   That is, if the user types in 'China' for the restaurant
   name, and a restaurant named 'China town' is found,
   then the function will return the the number of points
   proportional to the amount of the restaurant's name
   that matches the user input. In this case it would return
   9 * (length(China) / length(China town)  i.e. 9 * (1/2)
   This way at most the function would return 9, but can
   intellectually account for partial points for partial correction
   
   The rating method simply checks to see if the restaurant rating
   is greater than or equal to the user's input (i.e. x stars OR MORE)
   If so, then a match is made.
   
   For the location method, the program checks to see if the
   location of the restaurant is less than or equal to the 
   user input. That is, the restaurant is no further (but can
   be closer) than the distance the user inputs.
   
   The default method is for things like BYOB, and it checks for 
   a direct equality."
  (fn[attribute user-attribute] (key attribute)))


(defmethod compare-attribute :restaurant-name
  [restaurant-name user-name]
  
  (let [restaurant-length (count (val restaurant-name))
       user-length (count  user-name)]
    (if (.contains (val restaurant-name) user-name)
      (* (double (/ user-length restaurant-length)) (:restaurant-name weights))
      0.0)))


(defmethod compare-attribute :rating
  [restaurant-rating user-rating]
  
  (let [user-rating-num       (String->Number user-rating)
        restaurant-rating-num (String->Number (val restaurant-rating))]
    (cond
      ;(or (nil? user-rating-num) (nil? restaurant-rating-num)) 0.0
      (== user-rating-num restaurant-rating-num) (:rating weights)
      (or (== user-rating-num (+ 0.8 restaurant-rating-num)) (== user-rating-num (- 0.8 restaurant-rating-num))) (double (* (/ 3 4) (:rating weights)))
      :else 0.0)))


(defmethod compare-attribute :location
  [restaurant-location user-location]
  
  (let [user-location-num (String->Number user-location)
        restaurant-location-num (String->Number (val restaurant-location))]
    (cond
      ;(or (nil? user-location-num) (nil? restaurant-location-num)) 0
      (<= restaurant-location-num user-location-num) (:location weights)
      :else 0.0)))


(defmethod compare-attribute :price
  [restaurant-price user-price]
  
  (let [user-dollars (count user-price)
        restaurant-dollars (count (val restaurant-price))]
   (cond
      ;(or (> user-dollars 4) (> restaurant-dollars 4)) 0.0
      (== user-dollars restaurant-dollars) (:price weights)
      (or (== user-dollars (inc restaurant-dollars)) (== user-dollars (dec restaurant-dollars))) (double (* (/ 3 4) (:price weights)))
      :else 0.0)))

(defmethod compare-attribute :default
  [restaurant-attribute user-attribute]
  
  (if (= (val restaurant-attribute)user-attribute)
    ((key restaurant-attribute)weights)
    0))


(defn String->Number
  "Converts a string to a double"
  [s]
  
  (let [num (Double. (re-find #"\d+" s))]
    (if (number? num)
      num
      nil)))

