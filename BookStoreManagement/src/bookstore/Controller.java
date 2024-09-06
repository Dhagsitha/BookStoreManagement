package bookstore;
import java.util.*;
public class Controller {
     Model model=new Model();
     public int signUpData(String email,String username,String password,String role) {
 		int val=model.insertSignUpData(email,username,password,role);
 		return val;
 	}
	public int viewBookByID(int bookId) {
		int k=model.viewBookById(bookId);
       return k;
	}
	public int viewBookByCategory(String category) {
		int k=model.viewBookByCategory(category);
		return k;
	}
	public int viewBookByTitle(String title) {
		int k=model.viewBookByTitle(title);
		return k;
	}
	public int loginDataBuyer(String s, String pass) {
	int k=model.checkLoginDataBuyer(s,pass);
	return k;
	}
	public int addtoCart(int bId, String mail_id,int c_quantity) {
		int k=model.addToCart(bId,mail_id,c_quantity);
		return k;
	}
	public int placeOrderBuyer(String s) {
		return model.placeOrder(s);
		
	}
	public int getUserIdByUname(String name)
	{
		return model.getUserIdByUname(name);
	}
	public int deleteBookByID(int bookId) {
		int k=model.deleteBookByID(bookId);
		return k;
	}
	public int deleteBookByTitle(String title) {
		int k=model.deleteBookBytitle(title);
		return k;
	}
	public int deleteBookByCategory(String category) {
		int k=model.deleteBookByCategory(category);
		return k;
	}
	public int insertBook(String title, String author, String category, double price, int quantity) {
		int k=model.insertBook(title, author, category, price, quantity);
		return k;
	}
	public int loginDataSeller(String s, String pass) {
		int k=model.checkLoginDataSeller(s, pass);
		return k;
	}
	public int loginDataAdmin(String s, String pass) {
		int k=model.checkLoginDataAdmin(s, pass);
		return k;
	}
	public int updateBookByID(int bookId, int newQuantity) {
	    return model.updateBookById(bookId,newQuantity);
	}
	
	public int updateBookByCategory(String category) {
		// TODO Auto-generated method stub
		return 0;
	}
	public int updateBookByTitle(String title) {
		// TODO Auto-generated method stub
		return 0;
	}
	public int upadateBookByID(int bookId) {
		// TODO Auto-generated method stub
		return 0;
	}
	public int insertBookSeller(String mailId, String title, String author, String category, double price,
			int quantity) {
		return model.insertBookSeller(mailId,title,author,category,price,quantity);
	}
	public int myBooks(String mail) {
		return model.sellerBooks(mail);
	}
	public int loginDataCashier(String s, String pass) {
		return model.checkLoginDataCashier(s, pass);
	}
	public int calculateBill(String emailID) {
	
		return model.findTotalPrice(emailID);
	}
	

}
