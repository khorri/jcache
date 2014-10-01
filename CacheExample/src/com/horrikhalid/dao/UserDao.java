/**
 * 
 */
package com.horrikhalid.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.horrikhalid.model.User;

/**
 * @author horri
 *
 */
public class UserDao {
	private static Connection connection;
	/**
	 * 
	 */
	private static final long serialVersionUID = 2195153822742898777L;
	
	public static void initConnection(){
		if(connection==null){
			try {
				Class.forName("com.mysql.jdbc.Driver");
				connection =  DriverManager.getConnection("jdbc:mysql://localhost/jcache","root","root");
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
	public static User save(User user){
		try {
			PreparedStatement ps= connection.prepareStatement("insert into user(firstname,lastname) values (?,?)");
			//ps.setInt(1, user.getId());
			ps.setString(1, user.getFirstname());
			ps.setString(2, user.getFirstname());
			ps.execute();
			return user;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static User load(Integer id){
		try {
			Statement st= connection.createStatement();
			st.execute("Select * from user where id="+id);
			ResultSet rs = st.getResultSet();
			if(rs != null && rs.next()){
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setFirstname(rs.getString("firstname"));
				user.setLastname(rs.getString("lastname"));
				return user;
			}
			return null;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
