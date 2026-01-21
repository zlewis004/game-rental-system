# game-rental-system

Implementation Description
Our goal was to add functionality and an overall better user interface by adding
functionality to the GameRental class to manage user profiles, view game catalogs,
place orders, and view orders in a PostgreSQL database using SQL queries executed
through JDBC. Thus, handling input and output operations while interacting with the
database to fetch and update data as required without any malfunctions. All together we
have thirteen queries. This updateprofile query updates the user's phone number,
password or favorite games based on their login. The viewrecentorders query allows the
user to retrieve a list of a certain number of recent orders from the RentalOrder table.
The viewcatalog query retrieves and displays all games in the catalog. The placeorder
query inserts a new order into the RentalOrder table. The viewAllOrders query retrieves
and displays all orders from the RentalOrder table in the database. The createuser
query inserts a new user into the Users table. The login query checks if the login
credentials entered exist in the Users table. The viewprofile query retrieves and displays
the users profile. The vieworderinfo query retrieves and displays the details of a specific
rental order. The viewtrackinginfo query retrieves and displays tracking information for a
specific rental order from the rental table. The updateTrackingInfo query updates
tracking information for a specific rental order from the rental table. The updateCatalog
query updates the details of a game in the catalog. The updateUser query updates the
role and number of overdue games for a user


<img width="2000" height="2588" alt="image" src="https://github.com/user-attachments/assets/c2e67f70-656b-4959-8b07-28afe382271f" />

<img width="2000" height="2588" alt="image" src="https://github.com/user-attachments/assets/a4dae296-e8f8-41b7-bc4f-66f95ece6ca8" />

<img width="2000" height="2588" alt="image" src="https://github.com/user-attachments/assets/372daa79-edb4-40fe-ae4b-bffe0a187afc" />


Problems/Findings
One of the difficulties we had was with making sure we were currently writing the sql
queries for the JDBC drivers in Java. We also had to take the time to properly
understand how all of the imported methods work interactively with our queries and the
database, as well as what's input and output for each. One problem that I did have was
actually with vscode in itself because it wasn’t allowing my text files and sql files which
needed to be added to execute our code. In the end, we simply shut down our database
and re-copied our files to the server. Another challenge was handling user input and
output smoothly while interacting with the database, which was what we used the
bufferedreader method and the catch exceptions for.

