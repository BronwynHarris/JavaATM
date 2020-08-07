package defaultPackage;

import java.util.Scanner;

public class ATM {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		Bank theBank = new Bank("American Express");

//		add a user which adds a savings acct
		User aUser = theBank.addUser("John", "Smith", "0445");

//		add a checking acct for this user
		Account newAccount = new Account("Checking", aUser, theBank);
		aUser.addAccount(newAccount);
		theBank.addAccount(newAccount);

		User curUser;

		while (true) {
//			stay in the log in prompt until log in is successful
			curUser = ATM.mainMenuPrompt(theBank, sc);
//			stay in the main menu until the user quits
			ATM.printUserMenu(curUser, sc);

		}

	}

	public static User mainMenuPrompt(Bank theBank, Scanner sc) {
		String userId;
		String pin;
		User authUser;
//		prompt the user for userId and pin combo until it is correct
		do {
			System.out.printf("\n\nWelcome to %s\n\n", theBank.getName());
			System.out.print("Enter userId: ");
			userId = sc.nextLine();
			System.out.print("Enter pin: ");
			pin = sc.nextLine();

//			try to get the user object corresponding to the id and pin combo

			authUser = theBank.userLogin(userId, pin);

			if (authUser == null) {
				System.out.println("Incorrect userId/pin combination. " + "Please try again.");
			}
		} while (authUser == null); // continue looping until successful login

		return authUser;

	}

	public static void printUserMenu(User theUser, Scanner sc) {
//		print a summary of the user's accounts

		theUser.printAccountsSummary();

//		init

		int choice;

//		user menu

		do {
			System.out.printf("Welcome %s, what would you like to do?\n", theUser.getFirstName());
			System.out.println("  1) Show transaction history ");
			System.out.println("  2) Make a withdrawl ");
			System.out.println("  3) Make a deposit ");
			System.out.println("  4) Make a transfer ");
			System.out.println("  5) Quit ");
			System.out.println("");
			System.out.println("Enter choice: ");
			choice = sc.nextInt();

			if (choice < 1 || choice > 5) {
				System.out.println("Invalid choice. Please choose 1-5");
			}
		} while (choice < 1 || choice > 5);

//		process the choice

		switch (choice) {
		case 1:
			ATM.showTransHistory(theUser, sc);
			break;
		case 2:
			ATM.withdrawFunds(theUser, sc);
			break;
		case 3:
			ATM.depositFunds(theUser, sc);
			break;
		case 4:
			ATM.transferFunds(theUser, sc);
			break;
		case 5:
			ATM.quit();
			break;
		}
		
		if(choice != 5) {
			ATM.printUserMenu(theUser, sc);
		}
//		redisplay the menu unless the user wants to quit
	}

	private static void quit() {
		return;
	}

	private static void depositFunds(User theUser, Scanner sc) {
		int toAccount;
		double amount;
		double balance = 0;

//		get the account to transfer to

		do {
			System.out.printf("Enter the number (1-%d) of the account\n" + "to deposit into: ", theUser.numAccounts() );
			toAccount = sc.nextInt() - 1;
			if (toAccount < 0 || toAccount >= theUser.numAccounts()) {
				System.out.println("Please enter a valid number");
			}
		} while (toAccount < 0 || toAccount >= theUser.numAccounts());

//		get the amount to transfer

		do {
			System.out.printf("Enter the amount to deposit: $", balance);
			amount = sc.nextDouble();
			if (amount < 0) {
				System.out.println("Amount must be greater than zero");
			}
		} while (amount < 0);

//		do the transfer

		theUser.addAccountTransaction(toAccount, amount,
				String.format("Transfer to account %s", theUser.getAccountUUID(toAccount)));

	}

	private static void withdrawFunds(User theUser, Scanner sc) {
		int fromAccount;
		double amount;
		double balance;
		String memo;

//		get the account to transfer from

		do {
			System.out.printf("Enter the number (1-%d) of the account\n" + "to withdraw from: ", theUser.numAccounts());
			fromAccount = sc.nextInt() - 1;
			if (fromAccount < 0 || fromAccount >= theUser.numAccounts()) {
				System.out.println("Please enter a valid number");
			}
		} while (fromAccount < 0 || fromAccount >= theUser.numAccounts());

		balance = theUser.getAccountBalance(fromAccount);

//		get the amount to transfer

		do {
			System.out.printf("Enter the amount to withdraw  (max $%.02f): $", balance);
			amount = sc.nextDouble();
			if (amount < 0) {
				System.out.println("Amount must be greater than zero");
			} else if (amount > balance) {
				System.out.printf("Amount must not be greater than\nbalance of $%.02f.\n", balance);
			}
		} while (amount < 0 || amount > balance);

//		consume the rest of the previous input

		sc.nextLine();

//		get a memo

		System.out.println("Enter a memo: ");
		memo = sc.nextLine();

//		do the withdrawl

		theUser.addAccountTransaction(fromAccount, -1 * amount, memo);

	}

	private static void transferFunds(User theUser, Scanner sc) {
		int fromAccount;
		int toAccount;
		double amount;
		double balance;

//		get the account to transfer from

		do {
			System.out.printf("Enter the number (1-%d) of the account\n" + "to transfer from: ", theUser.numAccounts());
			fromAccount = sc.nextInt() - 1;
			if (fromAccount < 0 || fromAccount >= theUser.numAccounts()) {
				System.out.println("Please enter a valid number");
			}
		} while (fromAccount < 0 || fromAccount >= theUser.numAccounts());

		balance = theUser.getAccountBalance(fromAccount);

//		get the account to transfer to

		do {
			System.out.printf("Enter the number (1-%d) of the account\n" + "to transfer to: ", theUser.numAccounts());
			toAccount = sc.nextInt() - 1;
			if (toAccount < 0 || toAccount >= theUser.numAccounts()) {
				System.out.println("Please enter a valid number");
			}
		} while (toAccount < 0 || toAccount >= theUser.numAccounts());

//		get the amount to transfer

		do {
			System.out.printf("Enter the amount to transfer (max $%.02f): $", balance);
			amount = sc.nextDouble();
			if (amount < 0) {
				System.out.println("Amount must be greater than zero");
			} else if (amount > balance) {
				System.out.printf("Amount must not be greater than\nbalance of $%.02f.\n", balance);
			}
		} while (amount < 0 || amount > balance);

//		do the transfer

		theUser.addAccountTransaction(fromAccount, -1 * amount,
				String.format("Transfer to account %s", theUser.getAccountUUID(toAccount)));

		theUser.addAccountTransaction(toAccount, amount,
				String.format("Transfer to account %s", theUser.getAccountUUID(fromAccount)));
	}

	public static void showTransHistory(User theUser, Scanner sc) {
		int theAcct;

		do {
			System.out.printf("Enter the number (1-%d) of the account\n" + "whose transactions you want to see: ",
					theUser.numAccounts());

			theAcct = sc.nextInt() - 1;

			if (theAcct < 0 || theAcct >= theUser.numAccounts()) {
				System.out.println("Please enter a valid number");
			}
		} while (theAcct < 0 || theAcct >= theUser.numAccounts());

		theUser.printAccountsTransactionHistory(theAcct);

	}
}
