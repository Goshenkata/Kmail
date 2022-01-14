# Kmail
This project is made for both educational purposes and as my senior high school project, 
It emulates an email service, users can send messages and files (up to 25MB) to each other, all data is persisted in a database.
The application is secure, all passwords are hashed, user privileges have been thoroughly tested to assure that no user can access data which it
shouldn't be able to (like the emails of other users, that would be... bad). This project is internationalised, having support for both English and Bulgarian,
it automatically resolves the localization of the user when they visit for a first time, and remembers preferences too! 
The application also validates every user input on both the front and back end and gives appropriate feedback to the user,
as well as other kinds of error-handling (no ugly whitelabel pages!). The application has been tested (22 tests with ~90% code coverage) for most corner cases 
with both unit and integration tests.
# Author
Georgi Kolev, you can contact me at goshenkataklev@gmail.com
# Take a look
You need to have docker installed, do:
```
git clone https://github.com/Goshenkata/Kmail.git
cd Kmail
docker-compose -f docker-compose.yaml up
```
Then go to http://localhost:8080
# Used techologies
* Spring Boot
* Spring Validation
* Spring Data JPA
* MySQL
* H2 database
* JUnit5
* Mockito
* Spring Test
* Docker
# Looks cool?
Consider giving me a ⭐
