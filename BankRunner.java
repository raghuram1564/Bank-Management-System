package bankmanagementsystem;

import java.util.*;

abstract class User {
	private int id;
	private String password;
	private String mobNum;
	private double balance;
	private AccountStatus accStatus;
	private double monthlySalary;
	private Date lastLoginTime;
	private Date accLockTime;
	private Date chequeBookIssueDate;
	
	User(int id, String password, String mobNum, AccountStatus accStatus){
		this.id = id;
		this.password = password;
		this.mobNum = mobNum;
		this.accStatus = accStatus;
		balance = 500;
	}
	
	public boolean verifyPassword(String password) {
	    boolean checkPassword = this.password.equals(password);
	    if(checkPassword){
	        return true;
	    }
		System.out.println("Invalid user id or password");
		return false;
	}

	public void addBalance(double balance) {
		this.balance = this.getBalance()+balance;
	}
	public int getId() {
		return id;
	}
	public AccountStatus getAccStatus() {
		return this.accStatus;
	}
	public void setAccStatus(AccountStatus accStatus) {
		this.accStatus = accStatus;
	}
	public String getMobNum() {
		return mobNum;
	}
	public void setMobNum(String mobNum) {
		this.mobNum = mobNum;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public double getMonthlySalary(){
		return this.monthlySalary;
	}
	public void setMonthlySalary(double monthlySalary){
		this.monthlySalary = monthlySalary;
	}
	public void setAccLockTime(Date accLockTime){
		this.accLockTime = accLockTime;
	} 
	public Date getAccLockTime(){
		return this.accLockTime;
	}
	public void setLastLoginTime(Date lastLoginTime){
		this.lastLoginTime = lastLoginTime;
	}
	public Date getLastLoginTime(){
		return this.lastLoginTime;
	}
	public Date getChequeBookIssueDate(){
	    return this.chequeBookIssueDate;
	}
	public void setChequeBookIssueDate(Date chequeBookIssueDate){
	    this.chequeBookIssueDate = chequeBookIssueDate;
	}
	
	abstract void setAccType(AccountType accType);
	abstract AccountType getAccType();
}

class SavingsAccount extends User{

	private AccountType accType;
	

	SavingsAccount(int id, String password, String mobNum, AccountStatus accStatus, AccountType accType){
		super(id, password, mobNum, accStatus);
		this.accType = accType;
	}
	
	public void setAccType(AccountType accType){
		this.accType = accType;
	}
	public AccountType getAccType(){
		return this.accType;
	}
	
}

class CurrentAccount extends User{

	private AccountType accType;

	CurrentAccount(int id, String password, String mobNum, AccountStatus accStatus, AccountType accType){
		super(id, password, mobNum, accStatus);
		this.accType=accType;
	}

	public void setAccType(AccountType accType){
		this.accType = accType;
	}
	public AccountType getAccType(){
		return this.accType;
	}

}


class Bank {
	private static final int STUDENT = 1, NORMAL = 2, SALARY = 3;
	private Map<Integer, User> users;
	private Map<Integer, List<Transaction>> transactions;

	Bank(){
		users = new HashMap<>();
		User user1 = new SavingsAccount(12,"ab","6544766452",AccountStatus.ACTIVE,AccountType.STUDENT);
		User user2 = new SavingsAccount(34,"cd","9534766122",AccountStatus.ACTIVE,AccountType.NORMAL);
		User user3 = new SavingsAccount(56,"ef","8512456852",AccountStatus.ACTIVE,AccountType.SALARY_CAT1);
		User user4 = new SavingsAccount(78,"gh","9544764578",AccountStatus.ACTIVE,AccountType.SALARY_CAT2);
		User user5 = new CurrentAccount(90,"ij","7543764532",AccountStatus.ACTIVE,AccountType.CURRENT);
		users.put(12,user1);
		user1.setChequeBookIssueDate(new Date());
		users.put(34,user2);
		user2.setChequeBookIssueDate(new Date());
		users.put(56,user3);
		user3.setChequeBookIssueDate(new Date());
		users.put(78,user4);
		user4.setChequeBookIssueDate(new Date());
		users.put(90,user5);
		user5.setChequeBookIssueDate(new Date());
		transactions = new HashMap<>();
	}
	
	public void lockUserAccount(int id) {
		User user = users.get(id);
		if(user!=null) {
			user.setAccStatus(AccountStatus.LOCKED);
			user.setAccLockTime(new Date((System.currentTimeMillis() + 120000)));
		}
	}
	
	public boolean verifyUserId(int id){
		if(users.containsKey(id)){
			return true;
		}
		return false;
	}
	
	public boolean verifyCredentials(int id, String password) {
		if(users.containsKey(id)) {
			if(AccountStatus.ACTIVE == users.get(id).getAccStatus()){
				if(users.get(id).getLastLoginTime()!=null){
					Date lastLogin = users.get(id).getLastLoginTime();
					Date presentLogin = new Date();
					long lastLoginInMilliSecs = lastLogin.getTime();
					long presentLoginInMilliSecs = presentLogin.getTime();
					if(presentLoginInMilliSecs - lastLoginInMilliSecs > 300000){
						users.get(id).setAccStatus(AccountStatus.INACTIVE);
						System.out.println("Your account is currently Inactive, as you are not logged in for more than 30 days");
						return false; 
					}
				}
				else{
					users.get(id).setLastLoginTime(new Date());
					return users.get(id).verifyPassword(password);
				}
				
			}
			else if(AccountStatus.LOCKED == users.get(id).getAccStatus()){
				if(users.get(id).verifyPassword(password)){
					Date presentTime = new Date();
					long accLockTimeInMilliSecs = users.get(id).getAccLockTime().getTime();
					long presentTimeInMilliSecs = presentTime.getTime();
					if(accLockTimeInMilliSecs - presentTimeInMilliSecs > 0){
						System.out.println("Please login after "+ (accLockTimeInMilliSecs - presentTimeInMilliSecs)/1000 +" seconds"); 
						return false;
					}
					else{
						users.get(id).setAccStatus(AccountStatus.ACTIVE);
						return true;
					}
				}
				else{
					return false;
				}
			}
		}
		else{
			System.out.println("Invalid user id or password");
		}
		return false;
	}
	
	public int addUser(int id, String password, String mobNum, int accountType, double monthlySalary) {
		if(users.containsKey(id)) {
			return 1;
		}
		else {
		    User newUser;
		    
			if(accountType == STUDENT){
				newUser = new SavingsAccount(id,password,mobNum,AccountStatus.ACTIVE,AccountType.STUDENT);
				
			}
			else if(accountType == NORMAL){
				newUser = new SavingsAccount(id,password,mobNum,AccountStatus.ACTIVE,AccountType.NORMAL);
				newUser.setChequeBookIssueDate(new Date());
			}
			else if(accountType == SALARY){
				if(monthlySalary < 50000){
					newUser = new SavingsAccount(id,password,mobNum,AccountStatus.ACTIVE,AccountType.SALARY_CAT1);
					newUser.setChequeBookIssueDate(new Date());
				}
				else{
					newUser = new SavingsAccount(id,password,mobNum,AccountStatus.ACTIVE,AccountType.SALARY_CAT2);
					newUser.setChequeBookIssueDate(new Date());
				}
			}
			else{
				newUser = new CurrentAccount(id,password,mobNum,AccountStatus.ACTIVE,AccountType.CURRENT);
				newUser.setChequeBookIssueDate(new Date());
			}
			users.put(id,newUser);
			return 0;
		}
	}
	
	public void sendMoney(int id, String password, int destId, double amount) {
		User user = users.get(id);
		User destUser = users.get(destId); ;
		if(amount <= user.getBalance()) {
			destUser.setBalance(destUser.getBalance()+amount);
			user.setBalance(user.getBalance()-amount);
			System.out.println("Transaction successfull");
		}
		else{
			System.out.println("No sufficient balance");
		}
		Transaction transac = new Transaction(id, amount, new Date(), TransactionType.DEBIT, user.getBalance());
		if(transactions.containsKey(id)){
			transactions.get(id).add(transac);
		}
		else{
			List<Transaction> transaction = new ArrayList<Transaction>();
			transaction.add(transac);
			transactions.put(id, transaction);
		}
	}
	
	public void addMoney(int id, String password, double amount) {
		User user = users.get(id);
		user.addBalance(amount);
		Transaction transac1 = new Transaction(id, amount, new Date(), TransactionType.CREDIT, user.getBalance());
		if(transactions.containsKey(id)){
			transactions.get(id).add(transac1);
		}
		else{
			List<Transaction> transaction1 = new ArrayList();
			transaction1.add(transac1);
			transactions.put(id, transaction1);
		}
		System.out.println("Amount succesfully added");
		System.out.println("Your current Balance:"+" "+user.getBalance());
	}

	public void checkBalance(int id, String password) {
		System.out.println("Your current Balance:"+" "+users.get(id).getBalance());
	}

	public void withDrawl(int id, String password, double amount) {
		User user = users.get(id);

		if(user.getBalance() - amount >= user.getAccType().getMinBalance()){

			if(AccountType.CURRENT == user.getAccType()){

				if(user.getAccType().getFreeWithdrawls() > 0){
		   			user.getAccType().setFreeWithdrawls(user.getAccType().getFreeWithdrawls() - 1);
		    			user.setBalance(user.getBalance() - amount);
				}
				else{
		    			user.setBalance(user.getBalance() - amount - user.getAccType().getWithdrawlCharge());
				}
			}
			else{
				if(user.getMonthlySalary() > 50000){
					user.setBalance(user.getBalance() - amount);
				}
				else{
					if(user.getAccType().getFreeWithdrawls() > 0){
		   				user.getAccType().setFreeWithdrawls(user.getAccType().getFreeWithdrawls() - 1);
		    				user.setBalance(user.getBalance() - amount);
					}
					else{
		    				user.setBalance(user.getBalance() - amount - user.getAccType().getWithdrawlCharge());
					}
				}
			}
			System.out.println("Withdrawl Successfull");
			System.out.println("Remaining free withdrawls: "+ user.getAccType().getFreeWithdrawls());
			System.out.println("Your current Balance: "+" "+user.getBalance());

			Transaction transac2 = new Transaction(id, amount, new Date(), TransactionType.DEBIT, user.getBalance());
			if(transactions.containsKey(id)){
				transactions.get(id).add(transac2);
			}
			else{
				List<Transaction> transaction2 = new ArrayList();
				transaction2.add(transac2);
				transactions.put(id, transaction2);
			}
		}
		else{
			System.out.println("No Sufficient Balance");
		}
	}

	public void miniStatements(int id){
		System.out.println("==================================================================================================================");
		if(transactions.containsKey(id)){
			System.out.print('\n');
			int size = transactions.get(id).size();
			if(size > 5){
			    System.out.println("Last 5 transactions of user with id: "+ id+"  ");
			}
			else{
			    System.out.println("Last "+size+" transactions of user with id: "+ id+"  ");
			}
			System.out.print('\n');
			System.out.println("Transaction Date"+"                 "+"Transaction Amount"+"    "+"Transaction Type"+"   "+"Balance"+" ");
			System.out.print('\n');
			if(size <= 5){    
    				for(int i=0;i<size;i++){
    					System.out.println(transactions.get(id).get(i).getDetails());
    				}
			}
			else{
			    List<Transaction> miniStatementsList = transactions.get(id).subList(size-5, size);
			    for(int i=0;i<5;i++){
			        System.out.println(miniStatementsList.get(i).getDetails());
			    }
			}
		}
		else{
			System.out.println("There is no transaction history for this user id");
		}
		System.out.print('\n');
		System.out.println("==================================================================================================================");	
	}

	public void deleteAcc(int id, String password) {
		users.remove(id);
		System.out.println("Account succesfully deleted");
	}

	public void chequeBookIssue(int id){
		User user = users.get(id);
		if(user.getAccType().getIssueOfChequeBook()){
		    Date presentDate = new Date(); 
		    long lastChequeBookIssueTime = user.getChequeBookIssueDate().getTime();
		    long presentTime = presentDate.getTime();
		    if(presentTime - lastChequeBookIssueTime > Integer.MAX_VALUE){
    			if(user.getBalance() - user.getAccType().getChequeBookIssueCharges() > user.getAccType().getMinBalance()){
    				user.setBalance(user.getBalance() - user.getAccType().getChequeBookIssueCharges());
    				System.out.println("NOTE: Cheque Book issued");
    			}
    			else{
    				System.out.println("There is no enough balance to issue cheque book. Please add sufficient amount to issue cheque book"); 
    			}
		    }
		    else{
		        System.out.println("New cheque book cannot be issued till 90 days from the last cheque book issue date i.e. "+user.getChequeBookIssueDate());
		    }
		}
		else{
			System.out.println("NOTE: Cheque book cannot be issued for "+ user.getAccType() +" Account Type");
		}
	}

}

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

enum AccountType {
	STUDENT("Student",0,5,10,10000,false,0,0), NORMAL("Normal",1000,5,20,50000,true,500,0), SALARY_CAT1("Salary_cat1",0,5,10,100000,true,250,10), SALARY_CAT2("Salary_cat2",0,5,0,200000,true,250,20), CURRENT("Current",25000,10,5,0,true,0,0);
	
	private String name;
	private double minBalance;
	private int freeWithdrawls;
	private double withdrawlCharge;
	private double maxWithdrawlAmountPerDay;
	private boolean issueOfChequeBook;
	private double chequeBookIssueCharges;
	private int freeChequeLeafsPerQuarter;
	
	
	private AccountType(String name, double minBalance, int freeWithdrawls, double withdrawlCharge, double maxWithdrawlAmountPerDay, boolean issueOfChequeBook, double chequeBookIssueCharges, int freeChequeLeafsPerQuarter) {
		this.name = name;
		this.minBalance = minBalance;
		this.freeWithdrawls = freeWithdrawls;
		this.withdrawlCharge = withdrawlCharge;
		this.maxWithdrawlAmountPerDay = maxWithdrawlAmountPerDay;
		this.issueOfChequeBook = issueOfChequeBook;
		this.chequeBookIssueCharges = chequeBookIssueCharges;
		this.freeChequeLeafsPerQuarter = freeChequeLeafsPerQuarter;
	}
	
	public String getName(){
	    return this.name;
	}

	public double getMinBalance(){
		return this.minBalance;
	}
	public void setMinBalance(double minBalance) {
		this.minBalance = minBalance;
	}
	public int getFreeWithdrawls(){
		return this.freeWithdrawls;
	}
	public void setFreeWithdrawls(int freeWithdrawls) { 
		this.freeWithdrawls = freeWithdrawls;
	}
	public double getWithdrawlCharge(){
		return this.withdrawlCharge;
	}
	public void setWithdrawlCharge(double withdrawlCharge) {
		this.withdrawlCharge = withdrawlCharge;
	}
	public double getMaxWithdrawlAmountPerDay(){
		return this.maxWithdrawlAmountPerDay;
	}
	public void setMaxWithdrawlAmountPerDay(double maxWithdrawlAmountPerDay) {
		this.maxWithdrawlAmountPerDay = maxWithdrawlAmountPerDay;
	}
	public boolean getIssueOfChequeBook(){
		return this.issueOfChequeBook;
	}
	public double getChequeBookIssueCharges(){
		return this.chequeBookIssueCharges;
	}
	public int getFreeChequeLeafsPerQuarter(){
		return this.freeChequeLeafsPerQuarter;
	}
}

enum AccountStatus {
	ACTIVE("Active"), INACTIVE("Inactive"), LOCKED("Locked");
	
	private String name;
	
	private AccountStatus(String name) {
		this.name=name;
	}
	
	public String getName(){
	    return this.name;
	}
}

class Transaction {

	private int id;
	private double amount;
	private Date transactionDate;
	private TransactionType transactionType;
	private double balance;

	Transaction(int id, double amount, Date transactionDate, TransactionType transactionType, double balance){
		this.id = id;
		this.amount = amount;
		this.transactionDate = transactionDate;
		this.transactionType = transactionType;
		this.balance = balance;
	}
	
	public int getId(){
		return this.id;
	}
	public Date getTransactionDate(){
		return this.transactionDate;
	}
	public TransactionType getTransactionType(){
		return this.transactionType;
	}
	public double getAmount(){
		return this.amount;
	}
	public double getBalance(){
		return this.balance;
	}
	public String getDetails(){
		return this.getTransactionDate()+"       "+this.getAmount()+"              "+this.getTransactionType()+"            "+this.getBalance()+" ";
	}
}

enum TransactionType{
	DEBIT("Debit"), CREDIT("Credit");
	
	private String name;
	
	private TransactionType(String name){
		this.name = name;
	}

	public String getName(){
		return this.name;
	}
}
























