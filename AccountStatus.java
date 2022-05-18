package bankmanagementsystem;

import java.util.*;

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
