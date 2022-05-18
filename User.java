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
