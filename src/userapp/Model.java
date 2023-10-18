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

	private String databaseUrl = "jdbc:sqlite:database/UserApp.db";
	private Connection connection;

	public Model() {

		try {
			// Connect to the SQLite database
			connection = DriverManager.getConnection(databaseUrl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startTransaction() {
		
		try {
			connection.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void rollback() {
		
		try {
			connection.rollback();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void commit(){
			try {
				connection.commit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	
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

	public boolean insertPost(int userId, int postId, String postContent, String author, int likes, int shares, String createdAt) {
		
		String query;
		if(createdAt.equals("")) {
			query = "INSERT INTO posts (user_id, post_id, content, author, likes, shares) VALUES (?, ?, ?, ?, ?, ?)";
		}
		else {
			query = "INSERT INTO posts (user_id, post_id, content, author, likes, shares, date_time) VALUES (?, ?, ?, ?, ?, ?, ?)";
		}
		

		try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
			preparedStatement.setInt(1, userId);
			preparedStatement.setInt(2, postId);
			preparedStatement.setString(3, postContent);
			preparedStatement.setString(4, author);
			preparedStatement.setInt(5, likes);
			preparedStatement.setInt(6, shares);
			
			if(! createdAt.equals("")) {
				preparedStatement.setString(7, createdAt);
			}

			preparedStatement.executeUpdate();

			return true;
		} catch (SQLException e) {
			e.printStackTrace(); // Handle the exception as per your application's requirements

			return false;
		}
	}

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

				// Create and return a User object
				return new Post(postId, postContent, author, likes, shares, dateTime);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null; // User not found
	}

	
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
	 * Code to Kickstart the database creation
	 *
	 * @param fileName the database file name
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
	 * Code to create all tables
	 *
	 */
	public static void createTables(String fileName) {

		String url = "jdbc:sqlite:database/" + fileName;

		// SQL statements to create tables
		String createUsersTableSQL = "CREATE TABLE IF NOT EXISTS users ("
				+ "user_id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "vip_user INTEGER DEFAULT 0, "
				+ "username VARCHAR(30) COLLATE NOCASE UNIQUE NOT NULL, " + "first_name VARCHAR(50) NOT NULL, "
				+ "last_name VARCHAR(50) NOT NULL, " + "password VARCHAR(20) NOT NULL" + ");";

		String createPostsTableSQL = "CREATE TABLE IF NOT EXISTS posts (" + "post_id INTEGER PRIMARY KEY, "
				+ "user_id INTEGER, " + "content TEXT NOT NULL, " + "author VARCHAR(100) NOT NULL, "
				+ "likes INTEGER DEFAULT 0, " + "shares INTEGER DEFAULT 0, "
				+ "date_time DATETIME DEFAULT CURRENT_TIMESTAMP," + "FOREIGN KEY (user_id) REFERENCES users(user_id)"
				+ ");";

		Connection connection = null;
		Statement statement = null;

		try {

			// Connect to the database
			connection = DriverManager.getConnection(url);

			// Create statements
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
