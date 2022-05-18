package bankmanagementsystem;

import java.util.*;

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