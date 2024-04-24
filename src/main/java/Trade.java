public class Trade {
    private int trade_id;
    private int user_id;
    private String ticker;
    private int num_stock;
    private double price;
    private double total_cost;
    private String exchangeName;
    private double c;
    private double d;
    
    
    
    public Trade() {
	}

    public Trade(int trade_id, int user_id, String ticker, int num_stock, double price, double total_cost) {
        this.trade_id = trade_id;
        this.user_id = user_id;
        this.ticker = ticker;
        this.num_stock = num_stock;
        this.price = price;
        this.total_cost = total_cost;
    }

    // Getters
    public int getTrade_id() {
        return trade_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getTicker() {
        return ticker;
    }

    public int getNum_stock() {
        return num_stock;
    }

    public double getPrice() {
        return price;
    }

    public double getTotal_cost() {
        return total_cost;
    }

    // Setters
    public void setTrade_id(int trade_id) {
        this.trade_id = trade_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public void setNum_stock(int num_stock) {
        this.num_stock = num_stock;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setTotal_cost(double total_cost) {
        this.total_cost = total_cost;
    }

	public String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	public double getD() {
		return d;
	}

	public void setD(double d) {
		this.d = d;
	}

	public double getC() {
		return c;
	}

	public void setC(double c) {
		this.c = c;
	}
}
