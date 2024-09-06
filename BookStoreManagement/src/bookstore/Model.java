// Model.java
package bookstore;
import java.sql.*;
import com.mysql.*;
public class Model {
    private Connection conn;
    private Controller controller;

    public Model() {
        this.controller = controller;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bookstore", "root", "Dhagsi1106*123");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setController(Controller controller) {
        this.controller = controller;
    }
    public int insertSignUpData(String Uemail, String Uusername, String Upassword, String Urole) 
	{
		String query="INSERT INTO users(email,username,password,role)VALUES(?,?,?,?)";
		PreparedStatement pst;
		try
		{
			pst=conn.prepareStatement(query);
			pst.setString(1, Uemail);
			pst.setString(2,Uusername);
			pst.setString(3, Upassword);
			pst.setString(4,Urole);
			int p=pst.executeUpdate();
			if(p!=0)
				return 1;
		}
		catch(SQLException e)
		{
			e.printStackTrace();	
		}
		return 0;
	}

	public int viewBookById(int bookId) {
		String query="Select * from book where b_id=?";
		PreparedStatement ps;
		int c=0;
		try {
			ps=conn.prepareStatement(query);
		   ps.setInt(1, bookId);
		   ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				c++;
				System.out.println();
				System.out.println("Book ID: "+rs.getInt(1)+"| Title:"+rs.getString(2)+"| Author:"+rs.getString(3)+"| Category:"+rs.getString(4)+"| Price:"+rs.getDouble(5)+"| Quantity Available:"+rs.getInt(6));
			}
		}
		catch(Exception e)
		{
			
		}
		return c;
	}

	public int viewBookByCategory(String category) {
		String query="Select * from book where b_category=?";
		PreparedStatement ps;
		int c=0;
		try {
			ps=conn.prepareStatement(query);
		   ps.setString(1, category);
		   ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				c++;
				System.out.println();
				System.out.println("Book ID: "+rs.getInt(1)+" | Title:"+rs.getString(2)+" | Author:"+rs.getString(3)+" | Category:"+rs.getString(4)+" | Price:"+rs.getDouble(5)+" | Quantity Available:"+rs.getInt(6));
			}
		}
		catch(Exception e)
		{
			
		}
		return c;
	}
	

	public int viewBookByTitle(String title) {
		String query="Select * from book where b_title=?";
		PreparedStatement ps;
		int c=0;
		try {
			ps=conn.prepareStatement(query);
		   ps.setString(1, title);
		   ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				c++;
				System.out.println();
				System.out.println("Book ID: "+rs.getInt(1)+" | Title:"+rs.getString(2)+" | Author:"+rs.getString(3)+" | Category:"+rs.getString(4)+" | Price:"+rs.getDouble(5)+" | Quantity Available:"+rs.getInt(6));
			}
		}
		catch(Exception e)
		{
			
		}
		return c;
	}

	public int checkLoginDataBuyer(String em, String pass) {
	    String query = "SELECT password FROM users WHERE role='buyer' and email=? ";
	    PreparedStatement pst;
	    try {
	        pst = conn.prepareStatement(query);
	        pst.setString(1, em);
	        ResultSet res = pst.executeQuery();
	        if (res.next()) {
	            String password = res.getString("password");
	            if (password.equals(pass)) {
	                return 1; 
	            } else {
	                return -1;
	            }
	        } else {
	            return 0; 
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return -2; 
	    }
	    
	}

	public int addToCart(int bId, String mail_id,int c_quantity) {
	    String selectQuery = "SELECT b_price FROM book WHERE b_id = ?";
	    String bQuery="Select b_title FROM book WHERE  b_id=?";
	    String insertQuery = "INSERT INTO cart (user_email, book_id, quantity, c_price,title) VALUES (?, ?, ?, ?,?)";
	    
	    try {
	        // Retrieve price of the book
	        PreparedStatement selectStatement = conn.prepareStatement(selectQuery);
	        selectStatement.setInt(1, bId);
	        ResultSet resultSet = selectStatement.executeQuery();
	        PreparedStatement selectStatementBook = conn.prepareStatement(bQuery);
	        selectStatementBook.setInt(1, bId);
	        ResultSet rs = selectStatementBook.executeQuery();
	        
	        if (resultSet.next() && rs.next()) {
	            double price = resultSet.getDouble("b_price");
	            String name=rs.getString("b_title");
	            double totalPrice = price * c_quantity; // Assuming quantity is always 1 for now
	            PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
	            insertStatement.setString(1, mail_id);
	            insertStatement.setInt(2, bId);
	            insertStatement.setInt(3, c_quantity); // Assuming quantity is always 1 for now
	            insertStatement.setDouble(4, totalPrice);
	            insertStatement.setString(5, name);
	            int rowsInserted = insertStatement.executeUpdate();
	            insertStatement.close();
	            
	            if (rowsInserted != 0) {
	                return 1;
	            }
	        }
	        
	        selectStatement.close();
	    } catch (SQLException e) {
	        // Handle exception
	        e.printStackTrace();
	    }
	    
	    return 0;
	}

	public int placeOrder(String mail_id) {
	    String selectCartQuery = "SELECT * FROM cart WHERE user_email = ?";
	    String insertOrderQuery = "INSERT INTO orders (user_email, total_price) VALUES (?, ?)";
	    String deleteCartItemsQuery = "DELETE FROM cart WHERE user_email = ?";
	    String updateStockQuery = "UPDATE book SET b_quantity = b_quantity - ? WHERE b_id = ?";
	    
	    try {
	        // Retrieve cart items for the specified user
	        PreparedStatement selectCartStatement = conn.prepareStatement(selectCartQuery);
	        selectCartStatement.setString(1, mail_id);
	        ResultSet cartResultSet = selectCartStatement.executeQuery();
	        
	        double totalPrice = 0;
	        while (cartResultSet.next()) {
	            int bId = cartResultSet.getInt("book_id");
	            int quantity = cartResultSet.getInt("quantity");
	            double c_price = cartResultSet.getDouble("c_price");
	            totalPrice += c_price;
	            
	            // Deduct the quantity from the available stock
	            PreparedStatement updateStockStatement = conn.prepareStatement(updateStockQuery);
	            updateStockStatement.setInt(1, quantity);
	            updateStockStatement.setInt(2, bId);
	            updateStockStatement.executeUpdate();
	            updateStockStatement.close();
	        }
	        
	        // Insert order details into the orders table
	        PreparedStatement insertOrderStatement = conn.prepareStatement(insertOrderQuery);
	        insertOrderStatement.setString(1, mail_id);
	        insertOrderStatement.setDouble(2, totalPrice);
	        System.out.println("Your Total Price is:"+totalPrice);
	        int rowsInserted = insertOrderStatement.executeUpdate();
	        
	        // Remove cart items for the user from the cart table
	        PreparedStatement deleteCartItemsStatement = conn.prepareStatement(deleteCartItemsQuery);
	        deleteCartItemsStatement.setString(1, mail_id);
	        int rowsDeleted = deleteCartItemsStatement.executeUpdate();
	        
	        // Close all resources
	        selectCartStatement.close();
	        insertOrderStatement.close();
	        deleteCartItemsStatement.close();
	        cartResultSet.close();
	        
	        if (rowsInserted != 0 && rowsDeleted != 0) {
	            return 1; // Success
	        }
	    } catch (SQLException e) {
	        // Handle exception
	        e.printStackTrace();
	    }
	    
	    return 0; // Failure
	}


	public int getUserIdByUname(String name) {
		 int userId = -1; // Default value if user is not found
		    String query = "SELECT id FROM users WHERE username = ?";
		    try {
		        PreparedStatement statement = conn.prepareStatement(query);
		        statement.setString(1, name);
		        ResultSet resultSet = statement.executeQuery();
		        if (resultSet.next()) {
		            userId = resultSet.getInt("id");
		        }
		        statement.close();
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }
		    return userId;
	}

	public int deleteBookByID(int bookId) {
	    int rowsDeleted = 0;
	    String query = "DELETE FROM book WHERE b_id = ?";
	    try {
	        PreparedStatement statement = conn.prepareStatement(query);
	        statement.setInt(1, bookId);
	        rowsDeleted = statement.executeUpdate();
	        statement.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return rowsDeleted;
	}

	public int deleteBookBytitle(String title) {
		int rowsDeleted = 0;
	    String query = "DELETE FROM book WHERE b_title= ?";
	    try {
	        PreparedStatement statement = conn.prepareStatement(query);
	        statement.setString(1, title);
	        rowsDeleted = statement.executeUpdate();
	        statement.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return rowsDeleted;
	}
	public int deleteBookByCategory(String category) {
		int rowsDeleted = 0;
	    String query = "DELETE FROM book WHERE b_category= ?";
	    try {
	        PreparedStatement statement = conn.prepareStatement(query);
	        statement.setString(1, category);
	        rowsDeleted = statement.executeUpdate();
	        statement.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return rowsDeleted;
	}
	public int insertBook(String title, String author, String category, double price, int quantity) {
	    int rowsInserted = 0;
	    String query = "INSERT INTO book (b_title, b_author, b_category, b_price, b_quantity) VALUES (?, ?, ?, ?, ?)";
	    try {
	        PreparedStatement statement = conn.prepareStatement(query);
	        statement.setString(1, title);
	        statement.setString(2, author);
	        statement.setString(3, category);
	        statement.setDouble(4, price);
	        statement.setInt(5, quantity);
	        rowsInserted = statement.executeUpdate();
	        statement.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return rowsInserted;
	}

	public int checkLoginDataSeller(String s, String pass) {
		 String query = "SELECT password FROM users WHERE role='seller' and email=? ";
		    PreparedStatement pst;
		    try {
		        pst = conn.prepareStatement(query);
		        pst.setString(1, s);
		        ResultSet res = pst.executeQuery();
		        if (res.next()) {
		            String password = res.getString("password");
		            if (password.equals(pass)) {
		                return 1; 
		            } else {
		                return -1;
		            }
		        } else {
		            return 0; 
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		        return -2; 
		    }
		    
	}

	public int checkLoginDataAdmin(String email, String password) {
	    String query = "SELECT * FROM users WHERE email=? AND password=? AND role='admin'";
	    try {
	        PreparedStatement pst = conn.prepareStatement(query);
	        pst.setString(1, email);
	        pst.setString(2, password);
	        ResultSet res = pst.executeQuery();
	        if (res.next()) {
	            return 1; // Successful login
	        } else {
	            return 0; // Invalid email or password
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return -1; // Error occurred
	    }
	}
	public int updateBookById(int bookId, int newQuantity) {
	    int rowsUpdated = 0;
	    String query = "UPDATE book SET b_quantity=? WHERE b_id=?";
	    try {
	        PreparedStatement statement = conn.prepareStatement(query);
	        statement.setInt(1, newQuantity);
	        statement.setInt(2, bookId);
	        rowsUpdated = statement.executeUpdate();
	        statement.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return rowsUpdated;
	}
	public int insertBookSeller(String mailId, String title, String author, String category, double price, int quantity) {
	    int rowsInserted = 0;
	    String selectquery = "SELECT id FROM users WHERE email=?";
	    String insertBooksQuery = "INSERT INTO book (b_title, b_author, b_category, b_price, b_quantity) VALUES (?, ?, ?, ?, ?)";
	    String insertSellerBooksQuery = "INSERT INTO seller_books (s_id, s_email, s_bTitle, s_bAuthor, s_bCategory, s_bPrice, s_bQuantity) VALUES (?, ?, ?, ?, ?, ?, ?)";

	    try {
	        // Prepare statement to retrieve the seller's ID
	        PreparedStatement selectStatement = conn.prepareStatement(selectquery);
	        selectStatement.setString(1, mailId);
	        ResultSet resultSet = selectStatement.executeQuery();
	        
	        // If the seller exists, proceed with insertion
	        if (resultSet.next()) {
	            int sellerId = resultSet.getInt("id");
	            
	            // Insert into the book table
	            PreparedStatement insertBooksStatement = conn.prepareStatement(insertBooksQuery, Statement.RETURN_GENERATED_KEYS);
	            insertBooksStatement.setString(1, title);
	            insertBooksStatement.setString(2, author);
	            insertBooksStatement.setString(3, category);
	            insertBooksStatement.setDouble(4, price);
	            insertBooksStatement.setInt(5, quantity);
	            rowsInserted = insertBooksStatement.executeUpdate();
	            ResultSet generatedKeys = insertBooksStatement.getGeneratedKeys();
	            int bookId = -1;
	            if (generatedKeys.next()) {
	                bookId = generatedKeys.getInt(1);
	            }
	            if (bookId != -1) { 
	                PreparedStatement insertSellerBooksStatement = conn.prepareStatement(insertSellerBooksQuery);
	                insertSellerBooksStatement.setInt(1, sellerId);
	                insertSellerBooksStatement.setString(2, mailId);
	                insertSellerBooksStatement.setString(3, title);
	                insertSellerBooksStatement.setString(4, author);
	                insertSellerBooksStatement.setString(5, category);
	                insertSellerBooksStatement.setDouble(6, price);
	                insertSellerBooksStatement.setInt(7, quantity);
	                rowsInserted = insertSellerBooksStatement.executeUpdate();
	                insertSellerBooksStatement.close();
	            }
	            insertBooksStatement.close();
	        }
	        selectStatement.close();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return rowsInserted;
	}

	public int sellerBooks(String mail) {
		String query="Select * from book where s_email=?";
		PreparedStatement ps;
		int c=0;
		try {
			ps=conn.prepareStatement(query);
		   ps.setString(1, mail);
		   ResultSet rs=ps.executeQuery();
			while(rs.next())
			{
				c++;
				System.out.println();
				System.out.println("Book ID: "+rs.getInt(1)+" | Seller ID:"+rs.getInt(2)+" | Email Id:"+rs.getString(3)+" | Book Title:"+rs.getString(4)+" | Author"+rs.getString(5)+" | Category:"+rs.getString(6)+" | Price: "+rs.getDouble(7)+" | Quantity: "+rs.getInt(8));
			}
		}
		catch(Exception e)
		{
			
		}
		return c;
		
	}
	public int checkLoginDataCashier(String s, String pass) 
	{
		String query = "SELECT password FROM users WHERE role='cashier' and email=? ";
	    PreparedStatement pst;
	    try {
	        pst = conn.prepareStatement(query);
	        pst.setString(1, s);
	        ResultSet res = pst.executeQuery();
	        if (res.next()) {
	            String password = res.getString("password");
	            if (password.equals(pass)) {
	                return 1; 
	            } else {
	                return -1;
	            }
	        } else {
	            return 0; 
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return -2; 
	    }
	}
	public int findTotalPrice(String emailID) {
		    String query = "SELECT total_price FROM orders WHERE user_email = ?";
		    int totalPrice = 0;
		    
		    try (PreparedStatement stmt = conn.prepareStatement(query)) {
		        stmt.setString(1, emailID);
		        ResultSet rs = stmt.executeQuery();
		        if (rs.next()) {
		            totalPrice = rs.getInt("total_price");
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    } finally {
		        try {
		            conn.close();
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }
		    
		    return totalPrice;
		}
	
	}






