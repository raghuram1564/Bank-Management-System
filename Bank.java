package bankmanagementsystem;

import java.util.*;


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
					else{
						users.get(id).setLastLoginTime(new Date());
						return users.get(id).verifyPassword(password);
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
