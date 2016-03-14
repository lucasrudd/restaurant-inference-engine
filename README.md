# restaurant-inference-engine

## Usage

This code will read in a database from  a text file and organize
the information into a clojure record called Restaurant.
Each instance of Restaurant will initally be organized into a data base
in order of its appearance in the text file. The format of the text
file should follow the outline detailed in "Restaurant Attributes" 
which is located under restaurant-inference-engine/doc

The user will then input complete paramaters and form their own
instance of type Restaurant. The code will then sort through the 
database comparing the each of the resturant's attributes to the
restaurant created  by the user's input.

The code will generate a score for each restaurant based on how
it compared to the user's input. Each attribute is weighted
differently and is added to the total score if matched correctly.
Additionally, each attribute has different criteria it must meet
in order to "match" the user input. Some values must match exaclty, while
others (like the restaurant name) are given a partial score based on
how closely it matches the user input. For more information see
"Restaurant Attributes."

## Examples

Take the Restaurant record to be given by
(Restaurant [name       cusine
             rating     location
             price      BYOB
             atmosphere allergies
             age])

The user restaurant in this example is an instance of the
Restaurant record given by

(Restaurant "My-Restaurant" "Italian" 
            "1.0"           "1.0" 
            "$"             "true" 
            "Casual"        "None" 
            "1")


And the restaurant in question (the one that it is being compared to)
is an instance of the Restaurant record given by

(Restaurant "Not-My-Restaurant" "French" 
            "2.0"               "2.0" 
            "$$"                "false" 
            "Fine-dining"       "All" 
            "2")


Using the weighted attribute definitions detailed in "Restaurant Attributes"
the relevance value is given by:

relevance-value = 9*(13/17)    + 0 
                  + 0          + 0 
                  + 5*(3/4)    + 0
                  + 0          + 0
                  + 0

See restaurant-inference-engine/test/restaurant-inference-engine/core for
a variety of unit tests for each of the methods used in the functional portion
of this program. 

The "how-equal-test" provides a variety of input example senerios.


### Bugs

...