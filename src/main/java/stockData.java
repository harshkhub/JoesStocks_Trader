import com.google.gson.annotations.SerializedName;

public class stockData {

    public double c; // Current price
    public double d; // Change
    public double dp; // Percent change
    public double h; // High price of the day
    public double l; // Low price of the day
    public double o; // Open price of the day
    public double pc; // Previous close price

    // Additional fields
    @SerializedName("country")
    public String country;
    public String currency;
    public String exchange;
    public String ipo;
    public double marketCapitalization;
    public String name;
    public String phone;
    public double shareOutstanding;
    public String ticker;
    public String weburl;
    public String logo;
    public String finnhubIndustry;

    // Constructor matching the specified attributes
    public stockData(double c, double d, double dp, double h, double l, double o, double pc,
                     String country, String currency, String exchange, String ipo,
                     double marketCapitalization, String name, String phone, double shareOutstanding,
                     String ticker, String weburl, String logo, String finnhubIndustry) {
        this.c = c; // Current price
        this.d = d; // Change
        this.dp = dp; // Percent change
        this.h = h; // High price of the day
        this.l = l; // Low price of the day
        this.o = o; // Open price of the day
        this.pc = pc; // Previous close price

        // Additional fields
        
        this.country = country;
        this.currency = currency;
        this.exchange = exchange;
        this.ipo = ipo;
        this.marketCapitalization = marketCapitalization;
        this.name = name;
        this.phone = phone;
        this.shareOutstanding = shareOutstanding;
        this.ticker = ticker;
        this.weburl = weburl;
        this.logo = logo;
        this.finnhubIndustry = finnhubIndustry;
    }

    // Getters (and setters if needed) for each field

    // Getters and setters for additional fields
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getIpo() {
        return ipo;
    }

    public void setIpo(String ipo) {
        this.ipo = ipo;
    }

    public double getMarketCapitalization() {
        return marketCapitalization;
    }

    public void setMarketCapitalization(double marketCapitalization) {
        this.marketCapitalization = marketCapitalization;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getShareOutstanding() {
        return shareOutstanding;
    }

    public void setShareOutstanding(double shareOutstanding) {
        this.shareOutstanding = shareOutstanding;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getWeburl() {
        return weburl;
    }

    public void setWeburl(String weburl) {
        this.weburl = weburl;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getFinnhubIndustry() {
        return finnhubIndustry;
    }

    public void setFinnhubIndustry(String finnhubIndustry) {
        this.finnhubIndustry = finnhubIndustry;
    }

}
