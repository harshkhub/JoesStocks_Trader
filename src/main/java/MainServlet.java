
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cn.hutool.core.util.ReUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;



/**
 * Servlet implementation class MainServlet
 */
@WebServlet(name = "MainServlet", urlPatterns = {
		"/getTrade", "/registerUser", "/loginUser", "/isLoggedIn", "/logoutUser", 
		"/getLoggedinTrade", "/getPortfolio", "/buyTrade","/sellTrade", "/marketOpen"
		
}, loadOnStartup = 1)
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String API_KEY = "ADD_YOUR_OWN_FREE_KEY";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String path = request.getServletPath();
		
		switch(path) {
			
		case "/getTrade":
			processTradeGET(request, response);
			break;
		case "/isLoggedIn":
			processisLoggedInGET(request, response);
			break;
		case "/logoutUser":
			request.getSession().removeAttribute("user");
			break;
	/*	case "/getLoggedinTrade":
			processLoggedinTrade(request, response);
			break;*/
		case "/marketOpen":
			processmarketOpenGET(request, response);
			break;
		case "/getPortfolio":
			processportfolioGET(request, response);
			break;
		
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String path = request.getServletPath();
		
		switch(path) {
		
		case "/registerUser":
			processUserRegisterPOST(request, response);
			break;
		case "/loginUser":
			processUserLoginPOST(request, response);
			break;
		case "/buyTrade":
			processbuyTradePOST(request, response);
			break;
		case "/sellTrade":
			processsellTradePOST(request, response);
			break;
		}
		
	}
	
	//return users portfolio
	protected void processportfolioGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
		
		User user = (User)session.getAttribute("user");
		response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    PrintWriter out = response.getWriter();
		
		
		try(SQLAccessor sql = SQLAccessor.getDefaultInstance()){
			
			Double balance = sql.getBalance(user.getUserId());
			
			JSONArray portfolio = sql.getUserPortfolio(user.getUserId());
			JSONObject jsonResp = JSONUtil.createObj();
			
			jsonResp.set("data", portfolio);
			jsonResp.set("balance", balance);
			jsonResp.write(out);
			out.flush();
			
		}
		catch (ClassNotFoundException | SQLException e) {

	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        out.println("{\"status\":\"error\", \"message\":\"An error occurred.\"}");
	        e.printStackTrace(); 
	    }
		
		
	}
	
	//check if market is open
	protected void processmarketOpenGET(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		JsonElement marketOpen = checkMarketStatus();
		
		response.setContentType("application/json");
		
		PrintWriter out = response.getWriter();
		
		JsonObject jsonResponse = new JsonObject();
		
		jsonResponse.add("marketOpen", marketOpen);
		
		out.write(jsonResponse.toString());
		
		out.flush();
	}
	
	//sell trade 
	protected void processsellTradePOST(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
		{
		
			HttpSession session = request.getSession();
			
			User user = (User)session.getAttribute("user");
			
			String ticker = request.getParameter("ticker");
			String numStock = request.getParameter("numStock");
			String price = request.getParameter("price");
			PrintWriter out = response.getWriter();
			
			try(SQLAccessor sql = SQLAccessor.getDefaultInstance()){
				int success = sql.sellStock(user.getUserId(), ticker, Double.parseDouble(price), Integer.parseInt(numStock));
				
				if(success == 1) {
					
					out.println("{\"status\": true, \"message\": \"Trade successful.\"}");
					
				}
				else {
					response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					out.println("{\"status\": false, \"message\": \"Trade not successful.\"}");
				}

				
				
			} catch (ClassNotFoundException | SQLException e) {

		        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		        out.println("{\"status\":\"error\", \"message\":\"An error occurred. Trade not completed.\"}");
		        e.printStackTrace(); 
		    }
			
			
		
		}
	
	//buy trade for logged in user
	protected void processbuyTradePOST(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
		{
			HttpSession session = request.getSession();
			
			User user = (User)session.getAttribute("user");
			
			String ticker = request.getParameter("ticker");
			String numStock = request.getParameter("numStock");
			String price = request.getParameter("price");
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
			
			try(SQLAccessor sql = SQLAccessor.getDefaultInstance()){
				int success = sql.buyStock(user.getUserId(), ticker, Double.parseDouble(price), Integer.parseInt(numStock));
				
				if (success == 1) {
				    out.write("{\"status\": true, \"message\": \"Trade successful.\"}");
				} else {
				    out.write("{\"status\": false, \"message\": \"Trade not successful.\"}");
				}
				
			} catch (ClassNotFoundException | SQLException e) {

		        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		        out.println("{\"status\":\"error\", \"message\":\"An error occurred. Trade not completed.\"}");
		        e.printStackTrace(); 
		    }
			
			
			
			
		}
	protected void processTradeGET(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
		{

		
			JsonElement marketOpen = checkMarketStatus();
			boolean open = marketOpen.getAsBoolean();

			String ticker = request.getParameter("ticker");
			
			String currStock = getFinhubjson(ticker);

		    response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    JsonObject jsonResp = new Gson().fromJson(currStock, JsonObject.class);
		    jsonResp.addProperty("marketStatus", open);
		    
		    response.getWriter().write(jsonResp.toString());
		    
		    
		}
	//register new user
	
	protected void processUserRegisterPOST(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
		{
		
			HttpSession session = request.getSession();
			String email = request.getParameter("email");
			String password = request.getParameter("password");
			String username = request.getParameter("password");
			
			
			if (email == ""|| username == ""|| password == "" || !isEmail(email)) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "The 'requiredParam' parameter is missing or invalid.");
				return;
				
			}
			
			
			User newUser = new User();
			
			newUser.setBalance(50000);
			newUser.setEmail(email);
			newUser.setPassword(password);
			newUser.setUsername(username);
			
			try(SQLAccessor sql = SQLAccessor.getDefaultInstance()){
				
				int userId = sql.registerNewUser(newUser);
				
				newUser.setUserId(userId);
				
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				session.setAttribute("user", newUser);
				
				JSONObject jsonr = JSONUtil.createObj();
		        jsonr.set("status", "OK");
		        jsonr.set("data", newUser);
		        response.setContentType("application/json;charset=utf-8");
		        response.setCharacterEncoding("UTF-8");
		        response.setStatus(200);
		        try (PrintWriter out = response.getWriter())
		        {
		            jsonr.write(out);
		            out.flush();
		        }
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				switch(e.getErrorCode()) {
				case 1062:
					response.sendError(HttpServletResponse.SC_BAD_REQUEST, "The 'requiredParam' parameter is missing or invalid.");
					return;
				}
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	
	//login existing user
	protected void processUserLoginPOST(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException{
		
		HttpSession session = request.getSession();
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		if (password == null || username == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "The 'requiredParam' parameter is missing or invalid.");
			return;
		}
		
		
		User currUser = new User();
		
		try(SQLAccessor sql = SQLAccessor.getDefaultInstance()){
			
			currUser = sql.loginUser(username, password);
			
			if(currUser == null) {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "The username password combination does not exist!");
				return;
			}
			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			session.setAttribute("user", currUser);
			
			JSONObject jsonr = JSONUtil.createObj();
	        jsonr.set("status", "OK");
	        jsonr.set("data", currUser);
	        response.setContentType("application/json;charset=utf-8");
	        response.setCharacterEncoding("UTF-8");
	        response.setStatus(200);
	        try (PrintWriter out = response.getWriter())
	        {
	            jsonr.write(out);
	            out.flush();
	        }
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//check if user is logged in through http session
	protected void processisLoggedInGET(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		
		HttpSession session = request.getSession(false);
		response.setContentType("application/json;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		if((session != null) && (session.getAttribute("user") != null)) {
			out.print("{\"userLoggedIn\": true}");
	    } else {
	        // Session does not exist or "user" attribute is not set
	        out.print("{\"userLoggedIn\": false}");
	    }
	    out.flush();
		
	}
	//get data from Finnhub API
	
	public static String getFinhubjson(String ticker) throws IOException {
	    // Fetch data from the /quote endpoint
	    JsonObject quoteData = fetchJsonFromUrl(String.format("https://finnhub.io/api/v1/quote?symbol=%s&token=%s", ticker, API_KEY));
	    
	    // Fetch data from the /stock/profile2 endpoint
	    JsonObject profileData = fetchJsonFromUrl(String.format("https://finnhub.io/api/v1/stock/profile2?symbol=%s&token=%s", ticker, API_KEY));
	    
	    // Combine the two JsonObjects into one
	    JsonObject combinedData = new JsonObject();
	    quoteData.entrySet().forEach(entry -> combinedData.add(entry.getKey(), entry.getValue()));
	    profileData.entrySet().forEach(entry -> combinedData.add(entry.getKey(), entry.getValue()));
	    
	    // Convert the combined JsonObject back into a string
	    String combinedJsonString = combinedData.toString();
	    
	    return combinedJsonString;
	}

	public static JsonObject fetchJsonFromUrl(String urlString) throws IOException {
	    URL url = new URL(urlString);
	    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	    connection.setRequestMethod("GET");
	    
	    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	    JsonObject jsonResponse = new Gson().fromJson(in, JsonObject.class);
	    in.close();
	    
	    return jsonResponse;
	}
	
	//check if market is open
	
	private JsonElement checkMarketStatus() throws IOException{
		
		URL url = new URL("https://finnhub.io/api/v1/stock/market-status?exchange=US&token=cntlpl9r01qt3uhjdtb0cntlpl9r01qt3uhjdtbg");
		HttpURLConnection connection  = (HttpURLConnection) url.openConnection();
		
		connection.setRequestMethod("GET");
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	    JsonObject jsonResponse = new Gson().fromJson(in, JsonObject.class);
	    in.close();
	    
	    return jsonResponse.get("isOpen");
		
	}
	
	//check if email is valid format
	public static boolean isEmail(String s)
	    {
	        return ReUtil.isMatch("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", s);
	    }

	

}
