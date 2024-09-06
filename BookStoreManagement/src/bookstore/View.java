package bookstore;
import java.util.*;
import java.sql.ResultSet;
public class View {
   Scanner sc=new Scanner(System.in);
   private boolean isAuthenticated = false;
   public View()
   {
       controller = new Controller();
   }
   Controller controller=new Controller();
   public void displayMenu() {
	    int c;
	    do {
	        System.out.println("1. Buyer");
	        System.out.println("2. Cashier");
	        System.out.println("3. Admin");
	        System.out.println("4. Exit");
	        System.out.println("Enter your choice: ");
	        c = sc.nextInt();
	        sc.nextLine();
	        switch (c) {
	            case 1:
	                buyerLoginMenu();
	                break;
	            case 2:
	                cashierLoginMenu();
	                break;
	            case 3:
	                loginAdmin();
	                break;
	            case 4:
	                exitPage();
	                break;
	            default:
	                System.out.println("Invalid choice. Please enter a valid option.");
	        }
	    } while (c != 4 || c!=2);
	}

private void buyerLoginMenu() 
{
	int c;
	 do 
	 {
		 System.out.println("1.SignUp");
		 System.out.println("2. Login");
		 System.out.println("3.Exit");
		 System.out.println("Enter your choice: ");
	        c= sc.nextInt();
	        sc.nextLine(); 
	       switch(c)
	       {
	       case 1:
	    	  signUp();
	       break;
	       case 2: 
	    	loginBuyer();
	    	break;
	       case 3:
	    	goBackHome();
	       break;
	       }
	 } while(c!=3||isAuthenticated);
}
private void cashierLoginMenu() 
{
	    System.out.println("Hey Cashier!!! Enter Your Email");
	    String s = sc.nextLine();
	    if (!s.contains("@gmail.com")) {
	        while (!s.contains("@gmail.com")) {
	            System.out.println();
	            System.out.println("Invalid Email\nTry Again");
	            s = null;
	            System.out.println("Enter Your Email:");
	            s = sc.nextLine();
	            System.out.println();
	        }
	    }
	    System.out.println();
	    System.out.println("Enter Password");
	    String pass = sc.nextLine();
	    sc.nextLine();
	    int val = controller.loginDataCashier(s, pass);
	    if (val == 1) {
	        System.out.println("Login Successful");
	        isAuthenticated = true;
	        cashierMenu(); // Directly call cashierMenu() after successful login
	    } else {
	        System.out.println("Login not Successful, Retry");
	    }
	}

public void cashierMenu() {
	System.out.println("To Generate Bill Of User,click 1");
	int c=sc.nextInt();
	sc.nextLine();
	if(c==1)
	{
		System.out.println("Enter Email-Id");
		String s=sc.nextLine();
		int k=controller.calculateBill(s);
		System.out.println("User With EmailId "+s+" has to Pay Rs:"+k);
		System.exit(1);
	}
}
public void buyerMenu()
{
	int c;
	do {

		 System.out.println("1.View Books");
         System.out.println("2. Add to Cart");
         System.out.println("3. Place Order");
         System.out.println("4. Go back");
         System.out.println("Enter your choice: ");
        c= sc.nextInt();
        sc.nextLine(); 
        switch (c) {
            case 1:
                viewAllBookDetails();
                break;
            case 2:
            	addCart();
            	break;
            case 3:
            	placeOrder();
            	break;
            case 4:
            	goBackHome();
            	break;
            default:
                System.out.println("Invalid choice. Please enter a valid option.");
        }
    } while (c!= 4);
}	
private void goBackHome() 
{
	System.out.println("---Back to Home page---");
}
private void addCart() {
	if(!isAuthenticated)
	{
		System.out.println("Login Or Signup first");
		return;
	}
	else
	{
 System.out.println("Enter the Book Id that you wish to add into cart");
 int bId=sc.nextInt();
 sc.nextLine();
 System.out.println("Enter your Email Id");
 String email_id=sc.nextLine();
 sc.nextLine();
 System.out.println("Enter the Quantity");
 int q=sc.nextInt();
 int k=controller.addtoCart(bId,email_id,q);
 if(k==1)
 {
	 System.out.println("The Item Added to Cart Successfully!!!");
 }
 else
 {
	 System.out.println("Item Not Added,Try Again");
 }
	}
}
private void signUp() 
  {

	System.out.println("Enter Your Email:");
	String s=sc.nextLine();
	if(!s.contains("@gmail.com"))
	{
		while(!s.contains("@gmail.com"))
		{
			System.out.println();
			System.out.println("Invalid Email\nTry Again");
			s=null;
			System.out.println("Enter Your Email:");
			s=sc.next();
			System.out.println();
		}
	}
	System.out.println();
	System.out.println("Enter Your Username:");
	String user=sc.next();
	System.out.println();
	System.out.println("Enter Your Password:");
	String pass=sc.next();
	System.out.println();
	System.out.println("Enter Role(Buyer/Seller):");
	String role=sc.next();
	int v=controller.signUpData(s, user, pass, role);
	if(v==1)
	{
		System.out.println("Registered Successfully");
		isAuthenticated=true;
	}
	else
		System.out.println("Registration Not Successful");
	if(isAuthenticated)
	{
	if(role.equalsIgnoreCase("buyer"))
	{
		buyerMenu();
	}
	}
	
  }
private void loginBuyer()
  {
	System.out.println("Enter Your Email");
	String s=sc.next();
	if(!s.contains("@gmail.com"))
	{
		while(!s.contains("@gmail.com"))
		{
			System.out.println();
			System.out.println("Invalid Email\nTry Again");
			s=null;
			System.out.println("Enter Your Email:");
			s=sc.next();
			System.out.println();
		}
	}
	System.out.println();
	System.out.println("Enter Password");
	String pass=sc.next();
	int val=controller.loginDataBuyer(s,pass);
	if(val==1)
	{
		System.out.println("Login Successfull");
		isAuthenticated=true;
	}
	else
		System.out.println("Login not Successfull,Retry");
	if(isAuthenticated)
	{
		buyerMenu();
	}
  }
private void placeOrder()
{
	if(!isAuthenticated)
	{
		System.out.println("Login Or Signup first");
		return;
	}
	else
	{
System.out.print("Enter your email Id to place order");	
String s=sc.nextLine();
int k=controller.placeOrderBuyer(s);
if(k==1)
{
	 System.out.println("Item Ordered Successfully!!!");
}
else
{
	 System.out.println("Item Not Ordered,Try Again");
}
	}
}

private void exitPage()
{
  System.out.println("Thank You for visiting...Have a Good Day!!!");	
}
private void viewAllBookDetails() {
	int c;
	if(!isAuthenticated)
	{
		System.out.println("Login Or Signup first");
		return;
	}
	else
	{
	 do {
       System.out.println("1.View Books By Id");
       System.out.println("2. View Books By Title");
       System.out.println("3. View Books By Category");
       System.out.println("4. Go back");
       System.out.println("Enter your choice: ");
       c= sc.nextInt();
       sc.nextLine(); 
       switch (c) {
           case 1:
        	 System.out.println("Enter the Book ID");
        	 int bookId=sc.nextInt();
           	int k=controller.viewBookByID(bookId);
               break;
           case 2:
        	   System.out.println("Enter the Title of the Book");
          	 String title=sc.nextLine();
           	int v=controller.viewBookByTitle(title);
               break;
           case 3:
        	   System.out.println("Enter the Category of the Book");
            	 String category=sc.nextLine();
             	int m=controller.viewBookByCategory(category);
                 break;
           case 4:
        	   exitPage();
               break;
           default:
               System.out.println("Invalid choice. Please enter a valid option.");
       }
   } while (c != 4);
	}
	
}



private void sellerMenu() {
	int c;
	 do {
		 System.out.println("1.Sell Books");
		 System.out.println("2.View My Books");
        System.out.println("3. Go back to Home Page");
        System.out.println("Enter your choice: ");
       c= sc.nextInt();
       sc.nextLine(); 
       switch (c) {
           case 1:
           	addBooksSeller();
               break;
           case 2:
           	myBooksSeller();
               break;
            case 3:
        	   goBackHome();
           	break;
           default:
               System.out.println("Invalid choice. Please enter a valid option.");
       }
   } while (c != 3);
}	

	
private void myBooksSeller() {
	System.out.println("Enter Your MailId");
	String mail=sc.nextLine();
	int k=controller.myBooks(mail);
}
private void addBooksSeller() {
	 if (!isAuthenticated) {
	        System.out.println("Login or sign up first.");
	        return;
	    }
	    
	    System.out.println("Enter Your Email-Id:");
	    String mailId=sc.nextLine();
	    System.out.println("Enter the Details of the Book to Insert");
	    System.out.print("Title: ");
	    String title = sc.nextLine();
	    System.out.print("Author: ");
	    String author = sc.nextLine();
	    System.out.print("Category: ");
	    String category = sc.nextLine();
	    System.out.print("Price: ");
	    double price = sc.nextDouble();
	    System.out.print("Quantity: ");
	    int quantity = sc.nextInt();
	    int rowsInserted = controller.insertBookSeller(mailId,title, author, category, price, quantity);
	    if (rowsInserted > 0) {
	        System.out.println("Book inserted successfully.");
	    } else {
	        System.out.println("Failed to insert the book.");
	    }
	
}

private void adminMenu() {
	
	int c;
	 do {
		 System.out.println("1. Login");
		 System.out.println("2.Add Books");
       System.out.println("3.Update Books");
       System.out.println("4.Delete Books");
       System.out.println("5.View All Books");
       System.out.println("6.Back to Home Page");
      c= sc.nextInt();
      sc.nextLine(); 
      switch (c) {
          case 1:
          	loginAdmin();
              break;
          case 2:
              addBooks();
              break;
          case 3:
              updateBooks();
              break;
          case 4:
              deleteBooks();
              break;
          case 5:
        	  viewAllBookDetails();
        	  break;
           case 6:
       	   goBackHome();
          	break;
          default:
              System.out.println("Invalid choice. Please enter a valid option.");
      }
  } while (c != 6);
	
}
private void deleteBooks() {
	int c;
	if(!isAuthenticated)
	{
		System.out.println("Login Or Signup first");
		return;
	}
	else
	{
	 do {
       System.out.println("1.Delete Books By Id");
       System.out.println("2. Delete Books By Title");
       System.out.println("3. Delete Books By Category");
       System.out.println("4. Go back");
       System.out.println("Enter your choice: ");
       c= sc.nextInt();
       sc.nextLine(); 
       switch (c) {
           case 1:
        	 System.out.println("Enter the Book ID");
        	 int bookId=sc.nextInt();
           	int k=controller.deleteBookByID(bookId);
           	if(k>=1)
           	{
           		System.out.println("Book With that ID deleted Successfully");
           	}
           	else
           		System.out.println("Deletion Failed\nTry Again :(");
               break;
           case 2:
        	   System.out.println("Enter the Title of the Book");
          	 String title=sc.nextLine();
           	int v=controller.deleteBookByTitle(title);
           	if(v>=1)
           	{
           		System.out.println("Books With that Title deleted Successfully");
           	}
           	else
           		System.out.println("Deletion Failed\nTry Again :(");
               break;
           case 3:
        	   System.out.println("Enter the Category of the Book");
            	 String category=sc.nextLine();
            	 
             	int m=controller.deleteBookByCategory(category);
             	if(m>=1)
               	{
               		System.out.println("Books With that Category deleted Successfully");
               	}
               	else
               		System.out.println("Deletion Failed\nTry Again :(");
                 break;
           case 4:
        	   exitPage();
               break;
           default:
               System.out.println("Invalid choice. Please enter a valid option.");
       }
   } while (c != 3);
	}
}
private void updateBooks() {
	int c;
	if(!isAuthenticated)
	{
		System.out.println("Login Or Signup first");
		return;
	}
	else
	{
	 do {
       System.out.println("1.Update Books By Id");
       System.out.println("2. Update Books By Title");
       System.out.println("3. Update Books By Category");
       System.out.println("4. Go back");
       System.out.println("Enter your choice: ");
       c= sc.nextInt();
       sc.nextLine(); 
       switch (c) {
           case 1:
        	 System.out.println("Enter the ID of the book that you Want to Update");
        	 int bookId=sc.nextInt();
           	int k=controller.upadateBookByID(bookId);
           	if(k>=1)
           	{
           		System.out.println("Book With that ID updated Successfully");
           	}
           	else
           		System.out.println("Updation Failed\nTry Again :(");
               break;
           case 2:
        	   System.out.println("Enter the Title of the Book that you want to Update");
          	 String title=sc.nextLine();
           	int v=controller.updateBookByTitle(title);
           	if(v>=1)
           	{
           		System.out.println("Books With that Title updated Successfully");
           	}
           	else
           		System.out.println("Updation Failed\nTry Again :(");
               break;
           case 3:
        	   System.out.println("Enter the Category of the Book that you want to Update");
            	 String category=sc.nextLine();
            	 
             	int m=controller.updateBookByCategory(category);
             	if(m>=1)
               	{
               		System.out.println("Books With that Category updted Successfully");
               	}
               	else
               		System.out.println("Updation Failed\nTry Again :(");
                 break;
           case 4:
        	   exitPage();
               break;
           default:
               System.out.println("Invalid choice. Please enter a valid option.");
       }
   } while (c != 3);
	}
	
	
}
private void addBooks() {
    if (!isAuthenticated) {
        System.out.println("Login or sign up first.");
        return;
    }
    
    // Prompt the user to enter details of the book
    System.out.println("Enter the Details of the Book to Insert");
    System.out.print("Title: ");
    String title = sc.nextLine();
    System.out.print("Author: ");
    String author = sc.nextLine();
    System.out.print("Category: ");
    String category = sc.nextLine();
    System.out.print("Price: ");
    double price = sc.nextDouble();
    System.out.print("Quantity: ");
    int quantity = sc.nextInt();
    int rowsInserted = controller.insertBook(title, author, category, price, quantity);
    if (rowsInserted > 0) {
        System.out.println("Book inserted successfully.");
    } else {
        System.out.println("Failed to insert the book.");
    }
}

private void loginAdmin() {
	System.out.println("Hey Admin!!!Enter Your Email");
	String s=sc.next();
	if(!s.contains("@gmail.com"))
	{
		while(!s.contains("@gmail.com"))
		{
			System.out.println();
			System.out.println("Invalid Email\nTry Again");
			s=null;
			System.out.println("Enter Your Email:");
			s=sc.next();
			System.out.println();
		}
	}
	System.out.println();
	System.out.println("Enter Password");
	String pass=sc.next();
	int val=controller.loginDataAdmin(s,pass);
	if(val==1)
	{
		System.out.println("Login Succsefull");
		isAuthenticated=true;
		adminMenu();
	}
	else
	{
		System.out.println("Login not Successfull,Retry");
	  displayMenu();
  }
}
}

