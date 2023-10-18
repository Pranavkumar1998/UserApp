# UserApp - A JavaFX Application

## This App allows user to perform the following

- Create an Account
- Login
- Edit account
- Change Password
- Add Social Media Post
- Delete Post
- View Post
- Export Post

## User can Subscribe to be a VIP Member.
### When they subscribe, they have access to the following advance Functionalities
- Visualize Data (They can visualize posts by likes count categorized by 0-100, 100 - 1000, 1000+)
- Bulk Import of CSV Data


## IDE Used
**Eclipe IDE - Version: 2023-09 (4.29.0)**

## Software and Packages used
- **Java version "21" 2023-09-19 LTS**
- **JavaFX21**
- **SQlite Database - sqlite-jdbc-3.40.0.0**


## Running this Application
**This application is a JavaFx application. You will have to go through this documentation for details explaination on how to run it**
<br>
<a href="https://openjfx.io/openjfx-docs/#install-javafx">Getting Started with JavaFX</a>

### Running the jar File
You can find the source file UserApp.jar in this repo. <br>
You can run the app using the command below. **Ensure** you also downloaded the database folder in this repo. It should be in the same path as the jar file.
<pre>
<code>
#Window
java -jar --module-path ${path_to_folder}\openjfx-21_windows-x64_bin-sdk\javafx-sdk-21\lib --add-modules javafx.controls,javafx.fxml  UserApp.jar

#Mac/Linux
java -jar --module-path /path/to/javafx-sdk-21.0.1/lib --add-modules javafx.controls,javafx.fxml UserApp.jar
</code>
</code>
</pre>

### Developer - Importing source:
- clone this repo,
- Import to your IDE environment
- Setup and Configure JAvaFx
- Setup and Configure Sqlite - sqlite-jdbc-3.40.0.0 is in the project source, You will have to add it to class path
- Create database file or run with already created database.  In the main class, there are two commented line which you can execute to create the database and tables. Then update the dataUrl path in  Model class
- Now you can run the application
