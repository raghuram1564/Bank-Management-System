package bankmanagementsystem;

import java.util.*;


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
