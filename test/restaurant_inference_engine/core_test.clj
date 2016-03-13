(ns restaurant-inference-engine.core-test
  (:require [clojure.test :refer :all]
            [restaurant-inference-engine.core :refer :all]
            [comparative-restaurant-functions.comparative-restaurant-functions :refer :all]
            [restaurant-inference-engine.priority-map :refer :all])
  (:import  [restaurant-inference-engine.core]
            [comparative-restaurant-functions.comparative-restaurant-functions]
            [restaurant-inference-engine.priority-map]))

(def p (priority-map))

(def ^:const test-restaurants
       [(->Restaurant "My-Restaurant" "Italian" "1.0" "1.0" "$" "true" "Casual" "None" "1")
        (->Restaurant "My-Restaurant" "Italian" "1.0" "1.0" "$" "true" "Casual" "None" "2")
        (->Restaurant "My-Restaurant" "Italian" "1.0" "1.0" "$" "true" "Casual" "All" "2")
        (->Restaurant "My-Restaurant" "Italian" "1.0" "1.0" "$" "true" "Fine-dining" "All" "2")
        (->Restaurant "My-Restaurant" "Italian" "1.0" "1.0" "$" "false" "Fine-dining" "All" "2")
        (->Restaurant "My-Restaurant" "Italian" "1.0" "1.0" "$$" "false" "Fine-dining" "All" "2")
        (->Restaurant "My-Restaurant" "Italian" "1.0" "2.0" "$$" "false" "Fine-dining" "All" "2")
        (->Restaurant "My-Restaurant" "Italian" "2.0" "2.0" "$$" "false" "Fine-dining" "All" "2")
        (->Restaurant "My-Restaurant" "French" "2.0" "2.0" "$$" "false" "Fine-dining" "All" "2")
        (->Restaurant "Not-My-Restaurant" "French" "2.0" "2.0" "$$" "false" "Fine-dining" "All" "2")])

(def ^:const default-restaurant (->Restaurant "Restaurant Name..." "Any Cuisine" "Any Stars" "Any Location" "Any Price" "No Preference" "Any atmosphere" "No preference" "0"))

(def ^:const unformated-test-data ["    the    " "quick  " "brown  " "   fox " "   jumped   " " over  " " the" "  lazy" "   dog "])
(def ^:const formated-test-data ["the" "quick" "brown" "fox" "jumped" "over" "the" "lazy" "dog"])

(def ^:const test-file-contents ["this" " is" " a" " test" " file" " and this" " should" " create" " a" " vector"])

(def ^:const test-file "test/resources/TestDatabase.txt")

(deftest how-equal-test
  (testing "Testing how-equal? function..."
    
    (is (== 45    (how-equal? (nth test-restaurants 0) (nth test-restaurants 0))))
    (is (== 44    (how-equal? (nth test-restaurants 0) (nth test-restaurants 1))))
    (is (== 42    (how-equal? (nth test-restaurants 0) (nth test-restaurants 2))))
    (is (== 39    (how-equal? (nth test-restaurants 0) (nth test-restaurants 3))))
    (is (== 35    (how-equal? (nth test-restaurants 0) (nth test-restaurants 4))))
    (is (== 33.75 (how-equal? (nth test-restaurants 0) (nth test-restaurants 5))))
    (is (== 33.75 (how-equal? (nth test-restaurants 0) (nth test-restaurants 6))))
    (is (== 26.75 (how-equal? (nth test-restaurants 0) (nth test-restaurants 7))))
    (is (== 18.75 (how-equal? (nth test-restaurants 0) (nth test-restaurants 8))))
    (is (== 9.75  (how-equal? (nth test-restaurants 0) (nth test-restaurants 9))))
           
    (is (== (+ 3.75 (* 9 (double (/ 13 17)))) (how-equal? (nth test-restaurants 9) (nth test-restaurants 0))))))
    ;(is (== 0 (how-equal? (nth test-restaurants 9) default-restaurant)))


;(deftest compare-restaurant-names-test
;  (testing "Testing compare-restaurant-names function..."
    
;    (is (== 0.0 (compare-attribute {:restaurant-name "No Match"} "Search")))  
;    (is (== 4.5 (compare-attribute "The Name" "Name")))
;    (is (== 9.0 (compare-attribute "100 percent" "100 percent")))))


(deftest String->Number-test
  (testing "Testing String-Number function..."
    
    (is (== 10 (String->Number "10")))
    (is (== 10 (String->Number "10xxx")))
    (is (== 10 (String->Number "psx10")))))


;(deftest process-file-test
;  (testing "Testing process-file function..."
;    (is  (= (process-file test-file) test-file-contents))))


(deftest format-data-test
  (testing "Testing format-data function..."
    
    (is (not= formated-test-data unformated-test-data))
    (is (= formated-test-data (format-data unformated-test-data)))))


;(deftest compare-attribute-test
;  (testing "Testing compare-attribute function..."
;  
;  (is (== 9 (compare-attribute (nth test-restaurants 0) "Hello")))))


;(deftest create-restaurant-map-test
;  (testing "Testing create-restaurant-map function"
;                  )) 
  
  
  
