(ns restaurant-gui.core
  (:gen-class)
  (:require [clojure.core.async :as a 
                                :refer [>! <! >!! <!! go chan buffer close!]]
            [seesaw.core :refer :all]
            [seesaw.font :refer :all]))


;A channel to communicate between threads
(def user-input (chan))


;TODO: Fix the create-restaurant-label function
(defn create-restaurant-label
  [top-ten-restaurant-vector]
  
  (let [my-font (font :name "Arial"
                      :size 22)]
    (loop [remaining-restaurants top-ten-restaurant-vector
           restaurant-labels (label :text ""
                                    :font my-font)]
      (if (empty? remaining-restaurants)
        restaurant-labels
        (let [[head & remaining] remaining-restaurants]
          (recur remaining (config! restaurant-labels :text (str (config restaurant-labels :text) head))))))))
  

(defn draw-window
  [frame]
  
  (if (= nil frame)
    (alert "Error in draw-window.\n Frame cannot be nil")
    [(pack! frame)
    (show! frame)]))
  

(defn draw-temp-window
  [temp-content]
  
  (let [contents    (vertical-panel :items temp-content) 
        temp-window (frame
                     :title "Please wait..."
                     :content contents
                     :width 500
                     :height 500)]
  (draw-window temp-window)))


(defn submit-data
  [string]
  
  (draw-temp-window string))


(defn update-user-input
  [new-input]
  
  (>!! user-input new-input))


(defn to-boolean
  [bool]
  
  (cond 
    (= "Yes" bool) true
    (= "No"  bool) false
    :else nil))


(defn restaurant-selection-window 
  [content]
  
  (frame
    :title "Resturant Selector"
    :content content
    :width 500
    :height 500
    :on-close :exit))
  
  

(def window-setup  
  
  (future 
    (let [my-font          (font :name "Arial"
                                 :size 22)
        
          restaurant-name  (text :text "Restaurant name..."
                                 :font my-font)
        
          cuisine-label    (label    :text "Cuisine: "
                                     :font my-font
                                     :border [20 10])
        
          cuisine       (combobox :model["Any cuisine"
                                         "American"
                                         "Barbecue"
                                         "Chinese"
                                         "French"
                                         "Grill"
                                         "Hamburger"
                                         "Indian" 
                                         "Italian"
                                         "Japanese" 
                                         "Mexican"
                                         "Pizza"
                                         "Seafood"
                                         "Steak"
                                         "Sushi"
                                         "Thai"])
        
          rating-label   (label    :text "Rating: "
                                   :font my-font
                                   :border [20 10])
        
          rating         (combobox :model ["1 star and up"
                                           "2 stars and up"
                                           "3 stars and up"
                                           "4 stars and up"
                                           "5 stars"])
         
          location-label (label :text "Distance in miles: "
                                :font my-font
                                :border [20 10])
        
          location       (slider 
                           :value 5 :min 0 :max 20 
                           :minor-tick-spacing 1 :major-tick-spacing 2 
                           :snap-to-ticks? true 
                           :paint-ticks? true :paint-labels? true)
         
          price-label    (label :text "Price: "
                                :font my-font
                                :border [20 10])
          
          price          (combobox :model ["$"
                                           "$$"
                                           "$$$"
                                           "$$$$"])
        
          BYOB-label      (label :text "BYOB?"
                                 :font my-font
                                 :border [20 10])
          
          BYOB            (combobox :model ["No Preference" "Yes" "No"])
          
          atmosphere-label (label :text "Atmosphere: "
                                  :font my-font
                                  :border [20 10])
          
          atmosphere      (combobox :model ["Any"
                                            "Brewpub"
                                            "Buffet"
                                            "Cafe"
                                            "Casual"
                                            "Diner"
                                            "Fast Casual"
                                            "Fine-dining"
                                            "Modern"
                                            "Trendy"])
          
          allergy-label   (label :text "Allergies: "
                                 :font my-font
                                 :border [20 10])
          
          allergy         (combobox :model ["Lactose"
                                            "Mussels"
                                            "None"
                                            "Nuts"
                                            "Shellfish"])
          
          age-label       (label :text "Est: "
                                 :font my-font
                                 :border [20 10])
          
          age             (text :text "Established..."
                                :font my-font)
                                                                    
          
          confirm-button  (button
                            :text "Confirm"
                            :listen [:action (fn [event] event (update-user-input [(text restaurant-name) (selection cuisine) (selection rating) (str (value location)) (to-boolean (selection BYOB)) (selection atmosphere) (selection allergy)]))])
                         
          window-contents (vertical-panel :items [restaurant-name cuisine-label cuisine rating-label rating location-label location price-label price BYOB-label BYOB atmosphere-label atmosphere allergy-label allergy confirm-button])]
    
      (restaurant-selection-window window-contents))))

