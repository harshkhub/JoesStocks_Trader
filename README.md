# JoesStocks_Trader



This Stock Trading Platform is a full-stack web application allowing users to authenticate, purchase stocks, sell them, and view their portfolio. It pulls live stock data from the Finnhub API and utilizes Java Servlets for backend operations, with a simple front-end built with HTML, CSS, and JavaScript. User data is managed in an SQL database.

## Features

- **User Authentication**: Secure login and registration system to manage user access.
- **Buy Stocks**: Interface to search and buy stocks using real-time data from Finnhub API.
- **Sell Stocks**: Functionality for users to sell stocks and manage their holdings.
- **View Portfolio**: Dashboard for users to view their current stock holdings and their performance.
- **Responsive Design**: Basic yet functional interface compatible with various devices.

## Prerequisites

Before you begin, ensure you have met the following requirements:
- Java JDK 8 or newer
- Apache Tomcat Server 9 or newer
- MySQL Server 5.7 or newer
- Maven for dependency management

## Setup and Installation

1. **Clone the repository**
2. **Clone the repository**
- Add your own free API key from finnhub to the mainServlet.java class

3. **Database Configuration**
- Import the initial setup SQL script located in `create.sql`.

4. **Update Configuration Files**
- Navigate to `src/main/resources` and update the `config.properties` file with your database credentials and Finnhub API key.

5. **Build the project**
  
6. **Deploy the WAR file**
- Locate the generated WAR file in the `target` directory.
- Deploy this file to your Apache Tomcat server.

7. **Access the Application**
- Open your web browser and go to `http://localhost:8080/stock-trading-platform`.

## Usage

After logging in, you will be able to:
- Buy or sell stocks via the market interface.
- View the real-time performance of your investments on your dashboard.

## Contributing

Contributions are welcome! For major changes, please open an issue first to discuss what you would like to change.


