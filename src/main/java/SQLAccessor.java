import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;

public class SQLAccessor implements AutoCloseable
{
	private java.sql.Connection dbConn;
	
	//register new user
	public int registerNewUser(User newUser) throws SQLException
	{
		try(SQLAccessor s = getDefaultInstance()){
			
			
			PreparedStatement ps = dbConn.prepareStatement("INSERT INTO Users (email, username, password, balance) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, newUser.getEmail());
			ps.setString(2, newUser.getUsername());
			ps.setString(3, newUser.getPassword());
			ps.setInt(4, 50000);
			ps.execute();
			
			ResultSet rs = ps.getGeneratedKeys();
			
			if(rs.next()) {
				return rs.getInt(1);
			}
			return -1;
			
			
		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		return -1;
		
	}
	//get user balance
	
	public double getBalance(int user_id) 
	throws SQLException
	{
		PreparedStatement ps = dbConn.prepareStatement("SELECT balance FROM Users WHERE user_id=?");
		ps.setInt(1, user_id);
		
		ResultSet rs = ps.executeQuery();
		
		if(rs.next()) {
			return rs.getDouble("balance");
		}
		
		return -1;
	}
	
	//get users current portfolio
	
	public JSONArray getUserPortfolio(int user_id)  
	throws SQLException, IOException{
		
		PreparedStatement ps = dbConn.prepareStatement("SELECT * FROM Portfolio WHERE user_id = ? ");
		ps.setInt(1, user_id);
		ResultSet rs = ps.executeQuery();
		
		JSONArray result = new JSONArray();
		
		while(rs.next()) {
			Trade newTrade = new Trade();
			
			newTrade.setTotal_cost(rs.getDouble("totalCost"));
			newTrade.setNum_stock(rs.getInt("numStock"));
			newTrade.setTicker(rs.getString("ticker"));
			newTrade.setPrice(rs.getDouble("price"));
			newTrade.setTrade_id(rs.getInt("trade_id"));
			newTrade.setUser_id(user_id);
			
			String currStock = MainServlet.getFinhubjson(rs.getString("ticker"));
			System.out.println(currStock);
			JSONObject stockJson = new JSONObject(currStock);

	        // Add the JSON object to Trade object
	        newTrade.setC(stockJson.getDouble("c"));
	        newTrade.setD(stockJson.getDouble("d"));
	        newTrade.setExchangeName(stockJson.getStr("name"));
			
			result.add(newTrade);
		}
		
		return result;
		
		
	}
	
	
	//purchase stock
	public int buyStock(int user_id, String ticker, double price,  int numStock)
	throws SQLException
	{
		
		
		//still have to check if market open/close
		
		
		double buyPrice = price * numStock;
		double balance = 0;
		
		PreparedStatement ps = dbConn.prepareStatement("SELECT balance FROM Users WHERE user_id=?");
		ps.setInt(1, user_id);
		
		ResultSet rs = ps.executeQuery();
		
		
		
		if(rs.next()) {
			balance = rs.getDouble("balance");
		}
		else {
			System.out.println("User not found");
		}
		
		if(buyPrice > balance) {
			return 0;
		}
		else {
			balance -= buyPrice;
		}
		
		PreparedStatement alreadyExists = dbConn.prepareStatement("SELECT * FROM Portfolio WHERE user_id=? AND ticker=?", Statement.RETURN_GENERATED_KEYS);
		alreadyExists.setInt(1, user_id);
		alreadyExists.setString(2, ticker);
		ResultSet rs1 = alreadyExists.executeQuery();
		
		if(rs1.next()) {
			int alreadyContains = rs1.getInt("numStock");
			double currTotal = rs1.getDouble("totalCost");
			PreparedStatement ps1 = dbConn.prepareStatement("UPDATE Portfolio SET numStock=?, price=?, totalCost=? WHERE user_id = ? AND ticker=?");
			ps1.setInt(4, user_id);
			ps1.setString(5, ticker);
			ps1.setDouble(3, buyPrice + currTotal);
			ps1.setInt(1, numStock + alreadyContains);
			ps1.setDouble(2, price);
			ps1.executeUpdate();
		}
	
		else {
			PreparedStatement ps3 = dbConn.prepareStatement("INSERT INTO Portfolio(user_id, ticker, numStock, price, totalCost) VALUES(?,?,?,?,?)");
			ps3.setInt(1, user_id);
			ps3.setString(2, ticker);
			ps3.setInt(3, numStock);
			ps3.setDouble(4, price);
			ps3.setDouble(5, buyPrice);
			ps3.executeUpdate();
		}
		
		PreparedStatement ps2 = dbConn.prepareStatement("UPDATE Users SET balance=? WHERE user_id=?");
		ps2.setDouble(1, balance);
		ps2.setInt(2, user_id);
		ps2.executeUpdate();
		return 1;
		
		
	}
	
	//sell stock
	public int sellStock(int user_id, String ticker, double price,  int numStock)
	throws SQLException
	{
		
		double sellPrice = price * numStock;
		double balance = 0;
		
		PreparedStatement ps2 = dbConn.prepareStatement("SELECT balance FROM Users WHERE user_id=?");
		ps2.setInt(1, user_id);
		
		ResultSet rs1 = ps2.executeQuery();
		
		if(rs1.next()) {
			balance = rs1.getDouble("balance");
		}
		else {
			System.out.println("User not found");
		}
		
		PreparedStatement ps = dbConn.prepareStatement("SELECT * FROM Portfolio WHERE user_id=? and ticker=?", Statement.RETURN_GENERATED_KEYS);
		ps.setInt(1, user_id);
		ps.setString(2, ticker);
		
		ResultSet rs = ps.executeQuery();
		
		if(rs.next()) {
			int alreadyBought = rs.getInt("numStock");
			double currTotal = rs.getDouble("totalCost");
			if(alreadyBought > numStock) {
				PreparedStatement ps1 = dbConn.prepareStatement("UPDATE Portfolio SET numStock=?, price=?, totalCost=? WHERE user_id = ? AND ticker=?");
				ps1.setInt(4, user_id);
				ps1.setString(5, ticker);
				ps1.setInt(1, alreadyBought - numStock);
				ps1.setDouble(2, price);
				ps1.setDouble(3, currTotal - sellPrice);
				ps1.executeUpdate();
				balance += sellPrice;
			}
			else if(alreadyBought == numStock) {
				PreparedStatement ps21 = dbConn.prepareStatement("DELETE FROM Portfolio WHERE user_id=? AND ticker=?");
				ps21.setInt(1, user_id);
				ps21.setString(2, ticker);
				
				ps21.executeUpdate();
				balance += sellPrice;
			}
			else {
				return 0;
			}
		}
		else {
			return 0;
		}
		
		PreparedStatement ps3 = dbConn.prepareStatement("UPDATE Users SET balance=? WHERE user_id=?");
		ps3.setDouble(1, balance);
		ps3.setInt(2, user_id);
		ps3.executeUpdate();
		return 1;
		
		
	}
	//validate existing user
	public User loginUser(String username, String password) 
	throws SQLException
	{
		
		PreparedStatement ps = dbConn.prepareStatement("SELECT * FROM Users WHERE username=? AND password=?");
		ps.setString(1, username);
		ps.setString(2, password);
		
		ResultSet rs;
			rs = ps.executeQuery();
		
		if(rs.next()) {
			return getUserFromResultSet(rs);
		}
		return null;
	}
	
	public User getUserFromResultSet(ResultSet rs) {
		User user = new User();
		try {
			user.setEmail(rs.getString(4));
			user.setPassword(rs.getString(3));
			user.setBalance(rs.getInt(5));
			user.setUsername(rs.getString(2));
			user.setUserId(rs.getInt(1));
			return user;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
				
	}
	
	
	public void connect() throws SQLException, ClassNotFoundException
	{
		
		Class.forName("com.mysql.cj.jdbc.Driver");
		this.dbConn = DriverManager.getConnection("jdbc:mysql://localhost/Factory?user=root&password=password");
		
		
	}
	
	 public static SQLAccessor getDefaultInstance() throws SQLException, ClassNotFoundException
	 {
		 SQLAccessor d = new SQLAccessor();
		 d.connect();
		 return d;
	 }
	 
	@Override
	public void close() throws SQLException
	{
		dbConn.close();
	}
	
	public static void main(String [] args) throws ClassNotFoundException{
		
		
		Statement st = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
	}
}