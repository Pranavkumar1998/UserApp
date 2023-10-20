package userapp;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

public class Model {

	// Define the SQLite database URL. It specifies the connection to the "UserApp.db" database.
	private String databaseUrl = "jdbc:sqlite:database/UserApp.db";

	// Database connection object to interact with the SQLite database.
	private Connection connection;

	/**
	 * Constructor for the Model class. Initializes a database connection.
	 */
	public Model() {
	    try {
	        // Attempt to establish a connection to the SQLite database.
	        connection = DriverManager.getConnection(databaseUrl);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	/**
	 * Start a database transaction.
	 */
	public void startTransaction() {
	    try {
	        // Set the database connection to manual commit mode to initiate a transaction.
	        connection.setAutoCommit(false);
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	/**
	 * Roll back the current database transaction.
	 */
	public void rollback() {
	    try {
	        // Roll back any changes made during the transaction.
	        connection.rollback();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	/**
	 * Commit the current database transaction, persisting changes.
	 */
	public void commit() {
	    try {
	        // Commit and finalize the transaction, saving changes to the database.
	        connection.commit();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	
	/**
	 * Check if a user with the given username exists in the database.
	 *
	 * @param username The username to check for existence.
	 * @return True if the username exists, false otherwise.
	 */
	public boolean usernameExist(String username) {
	    boolean exists = false;
	    String sql = "SELECT COUNT(*) FROM users WHERE username = ?";

	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        preparedStatement.setString(1, username);

	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            if (resultSet.next()) {
	                int count = resultSet.getInt(1);
	                exists = count > 0;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return exists;
	}

	/**
	 * Check if a user with the given username exists in the database, excluding a specific user ID.
	 *
	 * @param userId   The ID of the user to exclude from the check.
	 * @param username The username to check for existence.
	 * @return True if the username exists (excluding the specified user), false otherwise.
	 */
	public boolean usernameExist(int userId, String username) {
	    boolean exists = false;
	    String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND user_id != ?";

	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        preparedStatement.setString(1, username);
	        preparedStatement.setInt(2, userId);

	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            if (resultSet.next()) {
	                int count = resultSet.getInt(1);
	                exists = count > 0;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return exists;
	}

	/**
	 * Insert a new user into the database.
	 *
	 * @param username   The username of the new user.
	 * @param firstName  The first name of the new user.
	 * @param lastName   The last name of the new user.
	 * @param password   The password of the new user.
	 * @return True if the user was successfully inserted, false otherwise.
	 */
	public boolean insertUser(String username, String firstName, String lastName, String password) {
	    String sql = "INSERT INTO users (username, first_name, last_name, password) VALUES (?, ?, ?, ?)";

	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        preparedStatement.setString(1, username);
	        preparedStatement.setString(2, firstName);
	        preparedStatement.setString(3, lastName);
	        preparedStatement.setString(4, password);
	        preparedStatement.executeUpdate();

	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	/**
	 * Update user information (username, first name, last name) in the database.
	 *
	 * @param userId    The ID of the user to update.
	 * @param username  The new username for the user.
	 * @param firstName The new first name for the user.
	 * @param lastName  The new last name for the user.
	 * @return True if the user information was successfully updated, false otherwise.
	 */
	public boolean updateUser(int userId, String username, String firstName, String lastName) {
	    String sql = "UPDATE users SET username = ?, first_name = ?, last_name = ? WHERE user_id = ?";

	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        preparedStatement.setString(1, username);
	        preparedStatement.setString(2, firstName);
	        preparedStatement.setString(3, lastName);
	        preparedStatement.setInt(4, userId);
	        preparedStatement.executeUpdate();

	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	/**
	 * Update the user's password in the database.
	 *
	 * @param userId   The ID of the user whose password will be updated.
	 * @param password The new password for the user.
	 * @return True if the password was successfully updated, false otherwise.
	 */
	public boolean updatePassword(int userId, String password) {
	    String sql = "UPDATE users SET password = ?  WHERE user_id = ?";

	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        preparedStatement.setString(1, password);
	        preparedStatement.setInt(2, userId);
	        preparedStatement.executeUpdate();
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	/**
	 * Update a user's VIP status in the database.
	 *
	 * @param userId  The ID of the user whose VIP status will be updated.
	 * @param status  The new VIP status (e.g., 0 for non-VIP, 1 for VIP).
	 * @return True if the VIP status was successfully updated, false otherwise.
	 */
	public boolean updateUserVip(int userId, int status) {
	    String sql = "UPDATE users SET vip_user = ?  WHERE user_id = ?";

	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        preparedStatement.setInt(1, status);
	        preparedStatement.setInt(2, userId);
	        preparedStatement.executeUpdate();
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	
	
	/**
	 * Retrieve a user's information from the database based on their username.
	 *
	 * @param username The username of the user to retrieve.
	 * @return A User object if the user is found, or null if the user does not exist.
	 */
	public User getUser(String username) {
	    String sql = "SELECT * FROM users WHERE username = ?";

	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        preparedStatement.setString(1, username);

	        ResultSet resultSet = preparedStatement.executeQuery();
	        if (resultSet.next()) {
	            int userId = resultSet.getInt("user_id");
	            String thisUsername = resultSet.getString("username");
	            String firstName = resultSet.getString("first_name");
	            String lastName = resultSet.getString("last_name");
	            boolean vipUser = resultSet.getBoolean("vip_user");

	            // Create and return a User object
	            return new User(userId, thisUsername, firstName, lastName, vipUser);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return null; // User not found
	}

	/**
	 * Retrieve a user's information from the database based on their username and password.
	 *
	 * @param username The username of the user.
	 * @param password The password of the user.
	 * @return A User object if the user is found and the password matches, or null if not found or password is incorrect.
	 */
	public User getUser(String username, String password) {
	    String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        preparedStatement.setString(1, username);
	        preparedStatement.setString(2, password);

	        ResultSet resultSet = preparedStatement.executeQuery();
	        if (resultSet.next()) {
	            int userId = resultSet.getInt("user_id");
	            String thisUsername = resultSet.getString("username");
	            String firstName = resultSet.getString("first_name");
	            String lastName = resultSet.getString("last_name");
	            boolean vipUser = resultSet.getBoolean("vip_user");

	            // Create and return a User object
	            return new User(userId, thisUsername, firstName, lastName, vipUser);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return null; // User not found
	}

	/**
	 * Retrieve an array of usernames from the database.
	 *
	 * @return An array of usernames ordered by username, or null if there is an error.
	 */
	public String[] getUsernames() {
	    String sql = "SELECT username FROM users ORDER BY username LIMIT 5000";

	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        ResultSet resultSet = preparedStatement.executeQuery();

	        List<String> usernameList = new ArrayList<>();
	        usernameList.add("All User");

	        while (resultSet.next()) {
	            String username = resultSet.getString("username");
	            usernameList.add(username);
	        }

	        return usernameList.toArray(new String[0]);
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	/**
	 * Check if a post with a given post ID exists in the database.
	 *
	 * @param postId The post ID to check.
	 * @return true if a post with the given ID exists, false otherwise.
	 */
	public boolean postIdExist(int postId) {
	    boolean exists = false;
	    String sql = "SELECT COUNT(*) FROM posts WHERE post_id = ?";

	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        preparedStatement.setInt(1, postId);

	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            if (resultSet.next()) {
	                int count = resultSet.getInt(1);
	                exists = count > 0;
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return exists;
	}

	/**
	 * Insert a new post into the database.
	 *
	 * @param userId       The user ID associated with the post.
	 * @param postId       The post ID of the new post.
	 * @param postContent  The content of the post.
	 * @param author       The author of the post.
	 * @param likes        The number of likes for the post.
	 * @param shares       The number of shares for the post.
	 * @param createdAt    The creation date and time of the post.
	 * @return true if the post is successfully inserted, false if there's an error.
	 */
	public boolean insertPost(int userId, int postId, String postContent, String author, int likes, int shares, String createdAt) {
	    String query;
	    if (createdAt.equals("")) {
	        query = "INSERT INTO posts (user_id, post_id, content, author, likes, shares) VALUES (?, ?, ?, ?, ?, ?)";
	    } else {
	        query = "INSERT INTO posts (user_id, post_id, content, author, likes, shares, date_time) VALUES (?, ?, ?, ?, ?, ?, ?)";
	    }

	    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	        preparedStatement.setInt(1, userId);
	        preparedStatement.setInt(2, postId);
	        preparedStatement.setString(3, postContent);
	        preparedStatement.setString(4, author);
	        preparedStatement.setInt(5, likes);
	        preparedStatement.setInt(6, shares);

	        if (!createdAt.equals("")) {
	            preparedStatement.setString(7, createdAt);
	        }

	        preparedStatement.executeUpdate();
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace(); // Handle the exception as per your application's requirements
	        return false;
	    }
	}

	/**
	 * Retrieve a post based on its post ID.
	 *
	 * @param postId The post ID to retrieve.
	 * @return A Post object if the post is found, or null if the post does not exist.
	 */
	public Post getPost(int postId) {
	    String sql = "SELECT * FROM posts WHERE post_id = ?";

	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        preparedStatement.setInt(1, postId);

	        ResultSet resultSet = preparedStatement.executeQuery();
	        if (resultSet.next()) {
	            String postContent = resultSet.getString("content");
	            String author = resultSet.getString("author");
	            int likes = resultSet.getInt("likes");
	            int shares = resultSet.getInt("shares");
	            String dateTime = resultSet.getString("date_time");

	            // Create and return a Post object
	            return new Post(postId, postContent, author, likes, shares, dateTime);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return null; // Post not found
	}

	/**
	 * Retrieve an array of posts based on the specified number of posts and the user's username.
	 *
	 * @param noPost    The number of posts to retrieve.
	 * @param postUser  The username of the user whose posts are being retrieved.
	 * @return An array of Post objects or null if there is an error.
	 */
	public Post[] getPosts(int noPost, String postUser) {
	    String sql = "";
	    if ("All User".equals(postUser)) {
	        sql = "SELECT * FROM posts ORDER BY likes DESC LIMIT ?";
	    } else {
	        sql = "SELECT * FROM posts WHERE user_id IN (SELECT user_id FROM users WHERE username = ?) ORDER BY likes DESC LIMIT ?";
	    }

	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        if ("All User".equals(postUser)) {
	            preparedStatement.setInt(1, noPost);
	        } else {
	            preparedStatement.setString(1, postUser);
	            preparedStatement.setInt(2, noPost);
	        }

	        ResultSet resultSet = preparedStatement.executeQuery();

	        List<Post> postList = new ArrayList<>();
	        while (resultSet.next()) {
	            int postId = resultSet.getInt("post_id");
	            String postContent = resultSet.getString("content");
	            String author = resultSet.getString("author");
	            int likes = resultSet.getInt("likes");
	            int shares = resultSet.getInt("shares");
	            String dateTime = resultSet.getString("date_time");

	            Post post = new Post(postId, postContent, author, likes, shares, dateTime);
	            postList.add(post);
	        }

	        return postList.toArray(new Post[0]);
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	
	/**
	 * Get the count of posts with shares within a specified range.
	 *
	 * @param min The minimum number of shares.
	 * @param max The maximum number of shares (use -1 for no maximum limit).
	 * @return The count of posts that match the specified share range.
	 */
	public int getPostShare(int min, int max) {
	    String sql = "";
	    if (max == -1) { // No limit for max bound
	        sql = "SELECT COUNT(*) AS total_count FROM posts WHERE shares >= ?";
	    } else {
	        sql = "SELECT COUNT(*) AS total_count FROM posts WHERE shares BETWEEN ? AND ?";
	    }

	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        if (max == -1) {
	            preparedStatement.setInt(1, min);
	        } else {
	            preparedStatement.setInt(1, min);
	            preparedStatement.setInt(2, max);
	        }

	        ResultSet resultSet = preparedStatement.executeQuery();

	        if (resultSet.next()) {
	            return resultSet.getInt("total_count");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return 0;
	}

	/**
	 * Delete a post from the database based on its post ID.
	 *
	 * @param postId The post ID of the post to be deleted.
	 * @return true if the post is successfully deleted, false if there's an error or the post doesn't exist.
	 */
	public boolean deletePost(int postId) {
	    String sql = "DELETE FROM posts WHERE post_id = ?";

	    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
	        preparedStatement.setInt(1, postId);

	        int rowsDeleted = preparedStatement.executeUpdate();

	        return rowsDeleted > 0; // Return true if at least one row was deleted
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return false;
	}

	/**
	 * Close the database connection. Call this method when you are done using the database.
	 */
	public void close() {
	    try {
	        if (connection != null) {
	            connection.close();
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}


	/**
	 * Create a new SQLite database with the provided file name.
	 *
	 * @param fileName The name of the database file.
	 */
	public static void createNewDatabase(String fileName) {
	    String url = "jdbc:sqlite:database/" + fileName;

	    try (Connection conn = DriverManager.getConnection(url)) {
	        if (conn != null) {
	            DatabaseMetaData meta = conn.getMetaData();
	            System.out.println("The driver name is " + meta.getDriverName());
	            System.out.println("A new database has been created.");
	        }
	    } catch (SQLException e) {
	        System.out.println(e.getMessage());
	    }
	}

	/**
	 * Create tables for users and posts in the specified database file.
	 *
	 * @param fileName The name of the database file where tables will be created.
	 */
	public static void createTables(String fileName) {
	    String url = "jdbc:sqlite:database/" + fileName;

	    // SQL statements to create tables
	    String createUsersTableSQL = "CREATE TABLE IF NOT EXISTS users ("
	            + "user_id INTEGER PRIMARY KEY AUTOINCREMENT, "
	            + "vip_user INTEGER DEFAULT 0, "
	            + "username VARCHAR(30) COLLATE NOCASE UNIQUE NOT NULL, "
	            + "first_name VARCHAR(50) NOT NULL, "
	            + "last_name VARCHAR(50) NOT NULL, "
	            + "password VARCHAR(20) NOT NULL"
	            + ");";

	    String createPostsTableSQL = "CREATE TABLE IF NOT EXISTS posts ("
	            + "post_id INTEGER PRIMARY KEY, "
	            + "user_id INTEGER, "
	            + "content TEXT NOT NULL, "
	            + "author VARCHAR(100) NOT NULL, "
	            + "likes INTEGER DEFAULT 0, "
	            + "shares INTEGER DEFAULT 0, "
	            + "date_time DATETIME DEFAULT CURRENT_TIMESTAMP, "
	            + "FOREIGN KEY (user_id) REFERENCES users(user_id)"
	            + ");";

	    Connection connection = null;
	    Statement statement = null;

	    try {
	        // Connect to the database
	        connection = DriverManager.getConnection(url);

	        // Create statements and execute SQL to create tables
	        statement = connection.createStatement();
	        statement.executeUpdate(createUsersTableSQL);
	        statement.executeUpdate(createPostsTableSQL);

	        System.out.println("Tables created successfully.");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (statement != null) {
	                statement.close();
	            }
	            if (connection != null) {
	                connection.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}



}
