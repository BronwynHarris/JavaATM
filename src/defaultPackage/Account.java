package defaultPackage;

import java.util.ArrayList;

public class Account {
	
	private String name;

	private String uuid;
	
	private User holder;
	
	private ArrayList<Transaction> transactions;
	
	public Account(String name, User holder, Bank theBank) {
		
		this.name = name;
		this.holder = holder;

		this.uuid = theBank.getNewAccountUUID();
		
		this.transactions = new ArrayList<Transaction>(); 
		
	}

	public String getUUID() {
		return this.uuid;
	}

	public String getSummaryLine() {
//		get the account balance
		double balance = this.getBalance();
		
//		format the summary line   if the balance is negative (meaning overdrawn)
		
		if(balance >= 0) {
			return String.format("%s: $%.02f: %s", this.uuid, balance, this.name);
//			System.out.println("Hello");
		} else {
			return String.format("%s: $(%.02f): %s", this.uuid, balance, this.name);
//			System.out.println("Goodbye");
		}
	}

	public double getBalance() {
		double balance = 0;
		for(Transaction t: this.transactions) {
			balance += t.getAmount();
		}
		return balance;
	}

	public void printTransHistory() {
		System.out.printf("\nTransaction history for account %s\n", this.uuid);
		for(int t = this.transactions.size() - 1; t>= 0; t--) {
			System.out.printf(this.transactions.get(t).getSummaryLine());
		}
		System.out.println();
	}

	  void addTransaction(double amount, String memo) {
		
//		  create a new transaction object and add it to our list
		  
		  Transaction newTrans = new Transaction(amount, memo, this);
		  
		  this.transactions.add(newTrans);
		
	}


}
