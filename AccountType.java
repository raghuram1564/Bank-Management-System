package bankmanagementsystem;

import java.util.*;


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
