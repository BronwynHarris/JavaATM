package defaultPackage;

import java.util.ArrayList;
import java.util.Random;

public class Bank {
	
	private String name;
	
	private ArrayList<User> users;
	
	private ArrayList<Account> accounts;
	
//	Create a new Bank object with empty lists of users and accounts
	
	public Bank(String name) {
		this.name = name;
		this.users = new ArrayList<User>();
		this.accounts = new ArrayList<Account>();
		
	}
	
	public void addAccount(Account anAccount) {
		this.accounts.add(anAccount);
	}

	public String getNewUserUUID() {
		
		String uuid;
		Random rng = new Random();
		
		int len = 10;
		
		boolean nonUnique = false;
		
		do {
//			generate the number
			uuid = "";
			for(int i = 0; i < len; i++) {
				uuid += ((Integer)rng.nextInt(10)).toString();
			}
			
//			check to make sure it's unique
			
			for(User u : this.users) {
				if(uuid.compareTo(u.getUUID()) == 0){
					nonUnique = true;
					break;
				}
			}
		} while(nonUnique);
		
		return uuid;
	}
	
//	generate a new universally unique identifier for an account
	
	public String getNewAccountUUID() {
		
		String uuid;
		Random rng = new Random();
		
		int len = 10;
		
		boolean nonUnique = false;
		
		do {
//			generate the number
			uuid = "";
			for(int i = 0; i < len; i++) {
				uuid += ((Integer)rng.nextInt(10)).toString();
			}
			
//			check to make sure it's unique
			
			for(Account a : this.accounts) {
				if(uuid.compareTo(a.getUUID()) == 0){
					nonUnique = true;
					break;
				}
			}
		} while(nonUnique);
		
		return uuid;
		
	}
	
	public User addUser(String firstName, String lastName, String pin) {
//		create a new User object and add it to our list
		
		User newUser = new User(firstName, lastName, pin, this);
		
		this.users.add(newUser);
		
//		create a savings account for the user	
		Account newAccount = new Account("Savings", newUser, this);	
		newUser.addAccount(newAccount);
		this.addAccount(newAccount);

		return newUser;
	}
	
	public User userLogin(String userId, String pin) {
//		search through the list of users
		
		for(User u : this.users) {
			if(u.getUUID().compareTo(userId) == 0 && u.validatePin(pin)) {
				return u;
			}
		}
//		if we haven't found the user or the pin does not match then return null
		return null;
	}

	public String getName() {
		return this.name;
	}
}
