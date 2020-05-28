## How to import into your IDE

[Eclipse](http://javapapers.com/java/import-maven-project-into-eclipse/)

[Intellij](https://www.jetbrains.com/help/idea/2016.2/importing-project-from-maven-model.html)

## SETUP DB:

1. Install & download https://www.apachefriends.org/index.html
2. ![](https://i.gyazo.com/eba9e0adf7124a7f482a46d2476917a7.png?1) enable php, phpmyadmin, mysql
3. Open xampp control panel and start apache and mysql
4. Press the admin button of the mysql server, this should open phpmyadmin
5. On the left side of your screen press "new" to create a new DB
6. Enter the name "gogreen" and press create
7. Now go to the application.properties file in the server resources folder and change "none" to "create" on the first line.
8. Now run the server. When you look at the gogreen DB now it should display some tables. (Person, log, action)s
9. Close the server and change the first line in the application.properties file back to "none"
10. Everything should work now!

## Team members:

**Balys Morkūnas**

**Wessel Oosterbroek**

**Luuk Haarman**

**Ka Fui Yang**

**Alexander Serraris**



Ported from GitLab to GitHub.
