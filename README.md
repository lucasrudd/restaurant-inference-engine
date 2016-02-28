# restaurant-inference-engine

## Usage

This code will read in a database from  a textfile and organize
the restaurants. Each restaurant will be represented as a struct
(or class) and organized into a data base alphabetically.

The user will then type in various parameters (see "Restaurant
Attributes" in restaurant-inference-engine/resources) and the
code will sort through the database comparing the resturant's
attributes to the user's input.

The code will generate a score for each restaurant based on how
it compared to the user's input. Each attribute is weighted
differently and is added to the total score if matched correctly.

## Example

If the user has the following desires:

Chinese food
4 stars
within 20 miles
Price of $$
Casual Atmosphere
No allergies
Established >10 years

Then a 4 star restaurant which sells chinese food but is far away 
would be recommended over a chinese food place which is >10 years 
old.
Likewise, a Japanese resturant which matches all other criteria 
may be recommeded if no other Chinese places match the
criteria are found.


### Bugs

...