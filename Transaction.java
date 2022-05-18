package bankmanagementsystem;

import java.util.*;

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