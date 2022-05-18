package bankmanagementsystem;

import java.util.*;


public class BankRunner {

	public static void main(String[] args) {
			startApplication();
	}

	private static Bank SBIbank = new Bank();
	private static Scanner sc = new Scanner(System.in);
	private static int id;
	private static String password;
	private static final String USER_RESPONSE_YES = "YES";
	private static final String USER_RESPONSE_NO = "NO";
	private static final int MAX_LOGIN_ATTEMPTS = 5;
	private static double monthlySalary = 0;
	
	public static boolean promptUserLogin() {
		boolean login = false;
		int attempts=0;
		while(!login &&  attempts < MAX_LOGIN_ATTEMPTS) {
			System.out.println("Enter your User id to login");
			id = sc.nextInt();
			System.out.println("Enter password");
			password = sc.next();
			login = SBIbank.verifyCredentials(id,password);
			attempts++;
		}
		if(SBIbank.verifyUserId(id) && attempts == MAX_LOGIN_ATTEMPTS) {
			System.out.println("User exceeded maximum login attempts. Your account has been locked for 120 seconds for safety purpose. Please try to login after 120 seconds.");
			SBIbank.lockUserAccount(id);
		}
		return login;
	}
	
	public static void startApplication() {
		System.out.println("Welcome to SBIbank");
		System.out.println("Are you an existing user ? ");
		System.out.println("Enter 'yes' if you are an existing user or 'No' ");
		String userResponse = sc.next();
		boolean loginSuccess = false;
		int addUserResponse=1;
		
		if(USER_RESPONSE_YES.equalsIgnoreCase(userResponse)) {
			loginSuccess = promptUserLogin();
		}
		else if(USER_RESPONSE_NO.equalsIgnoreCase(userResponse)) {
			while(addUserResponse==1) {
				System.out.println("Enter your new User id");
				int newId = sc.nextInt();
				System.out.println("Enter new password");
				String newPassword = sc.next();
				if(!SBIbank.verifyUserId(newId)) {
					System.out.println("Enter your mobile number");
					String newMobNum = sc.next();
					System.out.println("Choose account type");
					System.out.println("1. Savings account for Student");
					System.out.println("2. Savings account for Normal user");
					System.out.println("3. Savings account for Salary");
					System.out.println("4. Current account");
					int accountType = sc.nextInt();
					if(accountType==3){
						System.out.println("What is your monthly salary?");
						monthlySalary = sc.nextInt();
					}
					SBIbank.addUser(newId, newPassword, newMobNum, accountType, monthlySalary);
					if(accountType == 1){
						System.out.println("NOTE: You are not eligible for isuue of cheque book");
					}
					else{
						System.out.println("NOTE: You are eligible for issue of free cheque book");
					}
					System.out.println("Account successfully created ");
					loginSuccess = promptUserLogin();
					addUserResponse = 0;
				}
				else {
					System.out.println("User id already exists. please enter unique user id");
				}
			}
		}
		
		
		if(loginSuccess) {
			System.out.println("Choose the option");
			System.out.println("1. Send money to another account");
			System.out.println("2. Add amount to account");
			System.out.println("3. Check balance");
			System.out.println("4. Withdraw money");
			System.out.println("5. Mini Statement");
			System.out.println("6. New Cheque Book"); 
			System.out.println("7. Delete account");
			System.out.println("8. Exit");
			int option;
			double amount;

			while(sc.hasNextInt()) {
				option=sc.nextInt();
				if(option==8) {
					startApplication();
				}
				else {
					switch(option) {
					case 1:
						System.out.println("Please enter the money to be sent");
						amount = sc.nextDouble();
						System.out.println("Please enter the user id of the destination account");
						int destId = sc.nextInt();
						SBIbank.sendMoney(id, password, destId, amount);
						break;
						
					case 2:
						System.out.println("Please enter the money to be added");
						amount = sc.nextDouble();
						SBIbank.addMoney(id,password,amount);
						break;
						
					case 3:
						SBIbank.checkBalance(id,password);
						break;
						
					case 4:
						System.out.println("Enter your account type");
						System.out.println("1. Savings");
						System.out.println("2. Current");
						int accountType = sc.nextInt();
						System.out.println("Please enter the money to withdraw");
						amount = sc.nextDouble();
						SBIbank.withDrawl(id,password,amount);
						break;
	
					case 5:
						SBIbank.miniStatements(id);
						break;

					case 6: 
						SBIbank.chequeBookIssue(id);
						break;
					
					case 7:
						SBIbank.deleteAcc(id,password);
						break;
					}
				}
			}
		} 
		else{
			startApplication();
		}
	}
	
}






























