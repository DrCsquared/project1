package com.revature.colin.project1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class main {
	
	public static char tempChar;
	public static User tempUser;
	public static double tempDouble;
	public static boolean moveOn = true;
	public static User currentUser;
	public static String transactionNumber;
	
	public static void main(String[] args) 
	{
		
		boolean moveOn = true;
		
		//startMenu();
		//Scanner input = new Scanner(System.in);
		do {
		
		tempChar = Character.toUpperCase(startMenu());
		switch(tempChar) 
		{ 
		case 'L':
			System.out.println("You started to log in.");
			tempUser = userLogin();
			if(tempUser != null) 
			{
				System.out.println("You've logged in.");
				moveOn = false;
			}
			else 
			{
					System.out.println("Your username and password don't match anything in our database.");
			}
			break;
		case 'C' :
			System.out.println("You started to create an account");
			createUser();
			moveOn = false;
			break;
		case 'E' :
			System.out.println("You exited the code.");
			System.exit(0);
			//moveOn = false;
			break;
		default : 
			System.out.println("You didn't enter a valid input.");	
		}
			
		} while(moveOn);
		
		if(tempChar == 'L') 
		{
			do {
			if(isEmployee(tempUser)) 
			{
				tempChar = Character.toUpperCase(employeeMenu(tempUser));
				switch (tempChar) 
				{
				case 'U' :
					userApprovalMenu();
					moveOn = true;
					break;
				case 'A' :
					accountApprovalMenu();
					moveOn = true;
					break;
				case 'T' :
					System.out.println("These are all the transactions between customers");
					System.out.println();
					getAllTransactions();
					System.out.println();
					moveOn = true;
					break;
				case 'E' :
					System.out.println("You exited the code.");
					System.exit(0);
					break;
				default :
					System.out.println("You didn't enter a valid input.");
					moveOn = true;
					break;
				}
			}
			else 
			{
				tempChar = Character.toUpperCase(customerAccountMenu(tempUser));
				
				switch (tempChar) 
				{
				case 'A' :
					customerAccounts(tempUser);
					moveOn = true;
					break;
				case 'P' :
					customerPendingTransactions(tempUser);
					moveOn = true;
					break;
				case 'T' :
					customerTransfer(tempUser);
					moveOn= true;
					break;
					
				case 'C' :
					tempChar = Character.toUpperCase(createAccountMenu(tempUser));
					
					switch (tempChar) 
					{
					case 'C' :
						System.out.println("Enter an amount you want to start with your Checking Account.");
						tempDouble = ScannerInput.input.nextDouble();
						createCheckingAccountForApproval(tempUser, tempDouble);
						tempChar = 'L';
						moveOn = true;
						break;
					case 'S' :
						System.out.println("Enter an amount you want to start with your Savings Account.");
						tempDouble = ScannerInput.input.nextDouble();
						createSavingAccountForApproval(tempUser, tempDouble);
						tempChar = 'L';
						moveOn = true;
						break;
					case 'B' :
						moveOn = true;
						break;
					default :
						System.out.println("You didn't enter a valid input.");
						moveOn = true;
						tempChar = 'L';
						break;
						
					}
					
					break;
				case 'E' :
					System.out.println("You exited the code.");
					System.exit(0);
					break;
				default :
					System.out.println("You didn't enter a valid input.");
					moveOn = true;
					break;
				}
			}
			//customerAccountMenu(tempUser,'L');
			//checkIfEmployee();
		}while(moveOn);
		
		if(tempChar == 'C') 
		{
			
		}
	}
}
	
	public static void getAllTransactions() 
	{
		try(Connection connection = 
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
		
		PreparedStatement statement = connection.prepareStatement("select * from all_transactions");
		
		
		ResultSet resultSet = statement.executeQuery();
		if(!resultSet.next())
		{
			System.out.println("There are all the transactions at this time.");
			
		}
		else 
		{
			do 
			{	System.out.print("Transaction Number: " + resultSet.getInt("transaction_ID") + " - ");
				System.out.print("From User: " + resultSet.getInt("from_user") + " - ");
				System.out.print("To User: " + resultSet.getInt("to_user") +" - ");
				System.out.print("From Account: " + resultSet.getString("account_type_from") + " - ");
				System.out.print("To Account: " + resultSet.getString("account_type_to") +" - ");
				System.out.println("Amount: " + resultSet.getDouble("amount"));
				
			}while(resultSet.next());
		}
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void customerTransfer(User user)
	{
		char tempChar;
		char fromAccount;
		char toAccount;
		int tempID = user.getUserID();
		int tempID2;
		boolean tempBool = true;
		boolean internalTransferBool = true;
		boolean haveCheckingAccount = true;
		boolean haveSavingsAccount = true;
		double savingsAccountValue = -1.0;
		double checkingAccountValue = -1.0;
		double tempDouble = 0;
		int tempInt = 0;
		boolean moveOn = false;
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")){
		do {
		System.out.println("(I)nternal transfer.");
		System.out.println("(E)xternal transfer.");
		System.out.println("(B)ack to customer menu.");
		
		tempChar = ScannerInput.input.next().charAt(0);
		tempChar = Character.toUpperCase(tempChar);
		//haveCheckingAccount = true;
		//haveCheckingAccount = false;
		switch(tempChar) 
		{
		case 'I' :
		{
			PreparedStatement statement = connection.prepareStatement("select * from checking_accounts where user_id = ?");
			statement.setInt(1, tempID);
		
			ResultSet resultSet = statement.executeQuery();
			
			if(!resultSet.next()) 
			{
				haveCheckingAccount = false;
			}
			else
			{
					checkingAccountValue = resultSet.getDouble("account_value");
					System.out.println("Checking Account balance: " + checkingAccountValue);
					System.out.println();
			}
			
			PreparedStatement statement2 = connection.prepareStatement("select * from saving_accounts where user_id = ?");
			statement2.setInt(1, tempID);
		
			ResultSet resultSet2 = statement2.executeQuery();
			
			if(!resultSet2.next()) 
			{
				haveSavingsAccount = false;
			}
			else
			{
				savingsAccountValue = resultSet2.getDouble("account_value");
					System.out.println("Savings Account balance: " + savingsAccountValue);
					System.out.println();
			}
			
			if(!haveSavingsAccount || !haveCheckingAccount ) 
			{
				if(!haveSavingsAccount) 
				{
					System.out.println("You only have a Checking Account.");
				}
				else
					System.out.println("You only have a Savings Account");
			}
			else 
			{
			
			do 
			{
			{
			   System.out.println("Select account from: (C) or (S)");
			
				tempChar = ScannerInput.input.next().charAt(0);
				
				
				switch(Character.toUpperCase(tempChar)) 
				{
				case 'C' :
					do {
					System.out.println("How much do you want to transfer to Savings from Checking?");
					{
						tempDouble = ScannerInput.input.nextDouble();
						if(tempDouble > checkingAccountValue) 
						{
							System.out.println("You can't transfer that much from your Checking?");
						}
						
						else if(tempDouble <= 0) 
						{
							System.out.println("You can't transfer negative from your Checking?");
						}
						else 
						{
							addToSavingsAccount(user, tempDouble, savingsAccountValue);
							//addToCheckingAccount(user,tempDouble);
							deductFromCheckingAccount( user ,tempDouble , checkingAccountValue);
							haveCheckingAccount = false;
							internalTransferBool = false;
							
							PreparedStatement iCtoS = connection.prepareStatement("insert into self_transfers (user_id, account_to, account_from, amount) values(?,?,?,?)");
							iCtoS.setInt(1, tempID);
							iCtoS.setString(2, "S");
							iCtoS.setString(3, "C");
							iCtoS.setDouble(4, tempDouble);
							
							iCtoS.executeUpdate();
						}
					}
					}while(haveCheckingAccount);
					break;
					
				case 'S' :
					do {
						System.out.println("How much do you want to transfer to Checking from Savings?");
						{
							tempDouble = ScannerInput.input.nextDouble();
							if(tempDouble > savingsAccountValue) 
							{
								System.out.println("You can't transfer that much from your Savings?");
							}
							else if(tempDouble <= 0) 
							{
								System.out.println("You can't transfer negative from your Savings?");
							}
							else 
							{
								
								addToCheckingAccount(user, tempDouble, checkingAccountValue);
								//addToCheckingAccount(user,tempDouble);
								deductFromSavingsAccount( user ,tempDouble, savingsAccountValue);
								haveCheckingAccount = false;
								internalTransferBool = false;
								

								PreparedStatement iCtoS2 = connection.prepareStatement("insert into self_transfers (user_id, account_to, account_from, amount) values(?,?,?,?)");
								iCtoS2.setInt(1, tempID);
								iCtoS2.setString(2, "C");
								iCtoS2.setString(3, "S");
								iCtoS2.setDouble(4, tempDouble);
								
								iCtoS2.executeUpdate();
							}
						}
						}while(haveCheckingAccount);
						break;
				default :
					System.out.println("You didn't enter a valid input.");
					internalTransferBool = true;
					break;
				}
			}
			}while(internalTransferBool);
			}
		}
			break;
			
		case 'E' :
		{
			System.out.println("Which Account do you want to transfer from (C) or (S)?");

			tempChar = ScannerInput.input.next().charAt(0);
			
			switch(Character.toUpperCase(tempChar)) 
			{
			
			case 'C' :
				
				PreparedStatement statement = connection.prepareStatement("select * from checking_accounts where user_id = ?");
				statement.setInt(1, tempID);
			
				ResultSet resultSet = statement.executeQuery();
				
				if(!resultSet.next()) 
				{
					haveCheckingAccount = false;
				}
				else
				{
						checkingAccountValue = resultSet.getDouble("account_value");
						System.out.println("Checking Account balance: " + checkingAccountValue);
						System.out.println();
				}
				
				if(!haveCheckingAccount) 
				{
					System.out.println("You don't have a Checking Account");
				}
				else 
				{
					System.out.println("Enter the user_id number you which to transfter to");
					fromAccount= 'C';
					tempInt = ScannerInput.input.nextInt();
					
					tempBool = checkIfUserExists(tempInt);
					
					
					if(tempBool) 
					{
						
						if(checkIfUserHasCheckingAccount(tempInt) && checkIfUserHasSavingsAccount(tempInt)) 
						{
							do {
							System.out.println("Do you want to transfer to there (C) or (S)?");
								 tempChar = ScannerInput.input.next().charAt(0);
								 switch(Character.toUpperCase(tempChar)) 
								{
								 case 'C' :
								 
									 do {
									 System.out.println("How much do you want to transfer to user " + tempInt +" from your Checking?");
										
											tempDouble = ScannerInput.input.nextDouble();
											if(tempDouble > checkingAccountValue) 
											{
												System.out.println("You can't transfer that much from your Checking?");
											}
											
											else if(tempDouble <= 0) 
											{
												System.out.println("You can't transfer negative from your Checking?");
											}
											else 
											{
												sendTransactionToPending(tempID, tempInt, fromAccount, tempChar, tempDouble);
												tempBool = false;
											}
											}while(tempBool);
									
									 break;
								 case 'S' :
									 
									 do {
										 System.out.println("How much do you want to transfer to user " + tempInt +" to your Checking?");
											
												tempDouble = ScannerInput.input.nextDouble();
												if(tempDouble > checkingAccountValue) 
												{
													System.out.println("You can't transfer that much from your Checking?");
												}
												
												else if(tempDouble <= 0) 
												{
													System.out.println("You can't transfer negative from your Checking?");
												}
												else 
												{
													sendTransactionToPending(tempID, tempInt, fromAccount, tempChar, tempDouble);
													tempBool = false;
												}
												}while(tempBool);
									 break;
									 
								default :
									System.out.println("You didn't enter a valid input.");
									tempBool = true;
									break;
									}
								 
							}
							while(tempBool);
									
							
						}
						else if(checkIfUserHasSavingsAccount(tempInt) && !checkIfUserHasCheckingAccount(tempInt)) {
							do {
						
							System.out.println("User only has a (S)");
							System.out.println("How much do you want to transfer to user " + tempInt +" from your Checking?");
							
							tempDouble = ScannerInput.input.nextDouble();
							if(tempDouble > checkingAccountValue) 
							{
								System.out.println("You can't transfer that much from your Checking?");
							}
							
							else if(tempDouble <= 0) 
							{
								System.out.println("You can't transfer negative from your Checking?");
							}
							else 
							{
								sendTransactionToPending(tempID, tempInt, fromAccount, tempChar, tempDouble);
							}
							}while(tempBool);
						
							
						}
						else if(checkIfUserHasCheckingAccount(tempInt) && !checkIfUserHasSavingsAccount(tempInt)) {
							System.out.println("User only has a (C)");
							
							 do {
								 System.out.println("How much do you want to transfer to user " + tempInt +" from your Checking?");
									
										tempDouble = ScannerInput.input.nextDouble();
										if(tempDouble > checkingAccountValue) 
										{
											System.out.println("You can't transfer that much from your Checking?");
										}
										
										else if(tempDouble <= 0) 
										{
											System.out.println("You can't transfer negative from your Checking?");
										}
										else 
										{
											sendTransactionToPending(tempID, tempInt, fromAccount, tempChar, tempDouble);
											tempBool = false;
										}
										}while(tempBool);
						}
					}
					else 
					{
							System.out.println("User doesn't have accounts with this bank.");
					}
					
					
						
				}
				break;
				
			case'S' :
				
				PreparedStatement statement2 = connection.prepareStatement("select * from checking_accounts where user_id = ?");
				statement2.setInt(1, tempID);
			
				ResultSet resultSet2 = statement2.executeQuery();
				
				if(!resultSet2.next()) 
				{
					haveCheckingAccount = false;
				}
				else
				{
						checkingAccountValue = resultSet2.getDouble("account_value");
						System.out.println("Checking Account balance: " + checkingAccountValue);
						System.out.println();
				}
				
				if(!haveCheckingAccount) 
				{
					System.out.println("You don't have a Checking Account");
				}
				else 
				{
					System.out.println("Enter the user_id number you which to transfter to");
					fromAccount= 'S';
					tempInt = ScannerInput.input.nextInt();
					
					tempBool = checkIfUserExists(tempInt);
					
					
					if(tempBool) 
					{
						
						if(checkIfUserHasCheckingAccount(tempInt) && checkIfUserHasSavingsAccount(tempInt)) 
						{
							do {
							System.out.println("Do you want to transfer to there (C) or (S)?");
								 tempChar = ScannerInput.input.next().charAt(0);
								 switch(Character.toUpperCase(tempChar)) 
								{
								 case 'C' :
								 
									 do {
									 System.out.println("How much do you want to transfer to user " + tempInt +" from your Savings?");
										
											tempDouble = ScannerInput.input.nextDouble();
											if(tempDouble > checkingAccountValue) 
											{
												System.out.println("You can't transfer that much from your Savings?");
											}
											
											else if(tempDouble <= 0) 
											{
												System.out.println("You can't transfer negative from your Savings?");
											}
											else 
											{
												sendTransactionToPending(tempID, tempInt, fromAccount, tempChar, tempDouble);
												tempBool = false;
											}
											}while(tempBool);
									
									 break;
								 case 'S' :
									 
									 do {
										 System.out.println("How much do you want to transfer to user " + tempInt +" to your Savings?");
											
												tempDouble = ScannerInput.input.nextDouble();
												if(tempDouble > checkingAccountValue) 
												{
													System.out.println("You can't transfer that much from your Savings?");
												}
												
												else if(tempDouble <= 0) 
												{
													System.out.println("You can't transfer negative from your Savings?");
												}
												else 
												{
													sendTransactionToPending(tempID, tempInt, fromAccount, tempChar, tempDouble);
													tempBool = false;
												}
												}while(tempBool);
									 break;
									 
								default :
									System.out.println("You didn't enter a valid input.");
									tempBool = true;
									break;
									}
								 
							}
							while(tempBool);
									
							
						}
						else if(checkIfUserHasSavingsAccount(tempInt) && !checkIfUserHasCheckingAccount(tempInt)) {
							do {
						
							System.out.println("User only has a (S)");
							tempChar = 'S';
							System.out.println("How much do you want to transfer to user " + tempInt +" from your savings?");
							
							tempDouble = ScannerInput.input.nextDouble();
							if(tempDouble > checkingAccountValue) 
							{
								System.out.println("You can't transfer that much from your Savings?");
							}
							
							else if(tempDouble <= 0) 
							{
								System.out.println("You can't transfer negative from your Savings?");
							}
							else 
							{
								sendTransactionToPending(tempID, tempInt, fromAccount, tempChar, tempDouble);
							}
							}while(tempBool);
						
							
						}
						else if(checkIfUserHasCheckingAccount(tempInt) && !checkIfUserHasSavingsAccount(tempInt)) {
							System.out.println("User only has a (C)");
							tempChar = 'C';
							 do {
								 System.out.println("How much do you want to transfer to user " + tempInt +" from your Savings?");
									
										tempDouble = ScannerInput.input.nextDouble();
										if(tempDouble > checkingAccountValue) 
										{
											System.out.println("You can't transfer that much from your Savings?");
										}
										
										else if(tempDouble <= 0) 
										{
											System.out.println("You can't transfer negative from your Savings?");
										}
										else 
										{
											sendTransactionToPending(tempID, tempInt, fromAccount, tempChar, tempDouble);
											tempBool = false;
										}
										}while(tempBool);
						}
					}
					else 
					{
							System.out.println("User doesn't have accounts with this bank.");
					}
					
					
						
				}
				break;
			
				
			}
			break;
		}	
		case 'B' :
			moveOn = true;
	       break;
	    default :
	    	System.out.println("You didn't enter a valid input.");
	    	break;
		}
		
		}while(!moveOn);
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
	
	}
	
	public static void sendTransactionToPending(int from_user_id, int to_user_id, char from_account_type, char to_account_type,double amount)
	{
		String typeString_to = Character.toString(to_account_type);
		String typeString_from = Character.toString(from_account_type);
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")){
		PreparedStatement statement3 = connection.prepareStatement("insert into pending_transactions (from_user, to_user ,account_type_from, account_type_to, amount) values (?,?,?,?,?)");
		
		statement3.setInt(1,from_user_id);
		statement3.setInt(2, to_user_id);
		statement3.setString(3, typeString_from);
		statement3.setString(4, typeString_to);
		statement3.setDouble(5, amount);
	
		statement3.executeUpdate();
		
		System.out.println("You have sent your transaction to be accepted by user " + to_user_id);
		System.out.println();
		
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public static boolean checkIfUserExists(int user_id) 
	{
		
		boolean tempBool = true;
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")){
			
			PreparedStatement statement = connection.prepareStatement("select * from users where user_id = ?");
			statement.setInt(1, user_id);
			
			ResultSet resultSet = statement.executeQuery();
			
			if(!resultSet.next()) 
			{
				tempBool = false;
			}
			else
			{
				
				tempBool = true;
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
			return tempBool;
	}
	
	public static boolean checkIfUserHasCheckingAccount(int user_id) 
	{
		
		boolean tempBool = true;
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")){
			
			PreparedStatement statement = connection.prepareStatement("select * from checking_accounts where user_id = ?");
			statement.setInt(1, user_id);
			
			ResultSet resultSet = statement.executeQuery();
			
			if(!resultSet.next()) 
			{
				tempBool = false;
			}
			else
			{
				tempBool = true;
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
			return tempBool;
		
	}
	public static boolean checkIfUserHasSavingsAccount(int user_id) 
	{
		
		boolean tempBool = true;
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")){
			
			PreparedStatement statement = connection.prepareStatement("select * from saving_accounts where user_id = ?");
			statement.setInt(1, user_id);
			
			ResultSet resultSet = statement.executeQuery();
			
			if(!resultSet.next()) 
			{
				tempBool = false;
			}
			else
			{
				tempBool = true;
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
			return tempBool;
	}
	public static void addToCheckingAccount(User user, double addValue, double checkingAccountValue)
	{
		int tempID = user.getUserID();
		double tempValue = addValue + checkingAccountValue;
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")){
		PreparedStatement statement = connection.prepareStatement("update checking_accounts set account_value = ? where user_id = ? ");
		
		
		statement.setDouble(1, tempValue);
		statement.setInt(2, tempID);
		
		statement.executeUpdate();
		
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addToCheckingAccount(int user, double addValue)
	{
		double tempValue;
		double regularValue = 0;
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")){
			
			PreparedStatement statement2 = connection.prepareStatement("Select * from checking_accounts where user_id =?");
			statement2.setInt(1,user);
			
			ResultSet resultSet = statement2.executeQuery();
			
			
				regularValue = resultSet.getDouble("account_value");
			
			tempValue = regularValue + addValue;
				
			
		PreparedStatement statement = connection.prepareStatement("update checking_accounts set account_value = ? where user_id = ? ");
		statement.setDouble(1, tempValue);
		statement.setInt(2, user);
		
		statement.executeUpdate();
		
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public static void deductFromCheckingAccount(User user, double deductValue, double checkingAccountValue) 
	{
		
		int tempID = user.getUserID();
		double tempValue = checkingAccountValue - deductValue;
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")){
		PreparedStatement statement = connection.prepareStatement("Update checking_accounts set account_value = ? where user_id = ? ");
		
		statement.setDouble(1, tempValue);
		statement.setInt(2, tempID);
		
		statement.executeUpdate();
		
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void deductFromCheckingAccount(int user, double deductValue) 
	{
		
		double tempValue;
		double regularValue = 0;
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")){
			
			PreparedStatement statement2 = connection.prepareStatement("Select * from checking_accounts where user_id =?");
			statement2.setInt(1,user);
			
			ResultSet resultSet = statement2.executeQuery();
			
			regularValue = resultSet.getDouble("account_value");
				
			tempValue = regularValue - deductValue;
		PreparedStatement statement = connection.prepareStatement("Update checking_accounts set account_value = ? where user_id = ? ");
		
		statement.setDouble(1, tempValue);
		statement.setInt(2, user);
		
		statement.executeUpdate();
		
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	public static void addToSavingsAccount(User user, double addValue, double savingsAccountValue) 
	{
		
		int tempID = user.getUserID();
		double tempValue = addValue + savingsAccountValue;
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")){
		PreparedStatement statement = connection.prepareStatement("Update saving_accounts set account_value = ? where user_id = ? ");
		
		statement.setDouble(1, tempValue);
		statement.setInt(2, tempID);
		
		statement.executeUpdate();
		
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void addToSavingsAccount(int user, double addValue) 
	{
		
		int tempID = user;
		double tempValue = 0;
		double regularValue = 0;
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")){
			
			PreparedStatement statement2 = connection.prepareStatement("Select account_value from saving_accounts where user_id =?");
			statement2.setInt(1,user);
			
			ResultSet resultSet = statement2.executeQuery();
			
			while(resultSet.next()){
		
			regularValue = resultSet.getDouble("account_value");
			
			tempValue = regularValue + addValue;
			}
			
		PreparedStatement statement = connection.prepareStatement("Update saving_accounts set account_value = ? where user_id = ? ");
		
		statement.setDouble(1, tempValue);
		statement.setInt(2, user);
		
		statement.executeUpdate();
		
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	public static void deductFromSavingsAccount(User user, double deductValue, double savingsAccountValue) 
	{
		int tempID = user.getUserID();
		double tempValue = savingsAccountValue - deductValue;
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")){
		PreparedStatement statement = connection.prepareStatement("Update saving_accounts set account_value = ? where user_id = ? ");
		
		statement.setDouble(1, tempValue);
		statement.setInt(2, tempID);
		
		statement.executeUpdate();
		
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void deductFromSavingsAccount(int user, double deductValue) 
	{
		int tempID = user;
		double tempValue =0;
		double regularValue = 0;
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")){
			
			PreparedStatement statement2 = connection.prepareStatement("Select account_value from saving_accounts where user_id =?");
			statement2.setInt(1,user);
			
			ResultSet resultSet = statement2.executeQuery();
			
			while(resultSet.next()){
			regularValue = resultSet.getDouble("account_value");
			
			tempValue = regularValue - deductValue;
			}
			
		PreparedStatement statement = connection.prepareStatement("Update saving_accounts set account_value = ? where user_id = ? ");
		
		statement.setDouble(1, tempValue);
		statement.setInt(2, tempID);
		
		statement.executeUpdate();
		
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
		
	public static char createAccountMenu(User user) 
	{
		char tempChar;
		System.out.println("What type of account do you want to create");
		System.out.println("(C)hecking account.");
		System.out.println("(S)aving account.");
		System.out.println("(B)ack to customer menu.");
		
		tempChar = ScannerInput.input.next().charAt(0);
		
		return tempChar;
		
	}
	
	public static void customerAccounts(User user) 
	{
		boolean noCheckingAccount = false;
		boolean noSavingsAccount = false;
		boolean noPendingAccounts = false;
		boolean firstRunThru = true;
		
		int tempID = (int) user.getUserID();
		
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
			
			PreparedStatement statement = connection.prepareStatement("select * from checking_accounts where user_id = ?");
			statement.setInt(1, tempID);
		
			ResultSet resultSet = statement.executeQuery();
			
			if(!resultSet.next()) 
			{
				noCheckingAccount = true;
			}
			else
			{
					System.out.println("Checking Account balance: " + resultSet.getDouble("account_value"));
					System.out.println();
			}
			
			PreparedStatement statement2 = connection.prepareStatement("select * from saving_accounts where user_id = ?");
			statement2.setInt(1, tempID);
		
			ResultSet resultSet2 = statement2.executeQuery();
			
			if(!resultSet2.next()) 
			{	
				noSavingsAccount = true;
			}
			else 
			{
					System.out.println("Savings Account  balance: " + resultSet2.getDouble("account_value"));
					System.out.println();
			}
			
			PreparedStatement statement3 = connection.prepareStatement("select * from pending_accounts where user_id = ?");
			statement3.setInt(1, tempID);
		
			ResultSet resultSet3 = statement3.executeQuery();
			
			if(!resultSet3.next()) 
			{
				noPendingAccounts = true;
			}
			else 
			{
				do {
				
					if(firstRunThru) 
					{
					System.out.println("Below are the Pending Accounts");
					System.out.println();
					firstRunThru = false;
					}
					
					System.out.print("Account Type : " + resultSet3.getString("account_type") +" - ");
					System.out.println("Balance: " + resultSet3.getDouble("account_value"));
					
				}while(resultSet3.next());
			}
			if(noPendingAccounts && noCheckingAccount &&  noSavingsAccount) 
			{
				System.out.println("You don't have any accounts or pending account in the database.");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public static void accountApprovalMenu() 
	{
		boolean tempCheck = true;
		int tempID;
		
		int tempUserID = 0;
		double tempDouble = 0.0;
		String tempAT;
		String tempChar;
		
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
			
			PreparedStatement statement = connection.prepareStatement("select * from pending_accounts");
			
			
			ResultSet resultSet = statement.executeQuery();
			if(!resultSet.next())
			{
				System.out.println("There are no saving or checking accounts pending at this time.");
			}
			else 
			{
				do { 
				
					//System.out.print(resultSet.getInt(1) +" - ");
					System.out.print("User ID: " + resultSet.getInt("user_id") + " - ");
					System.out.print("Account type: " + resultSet.getString("account_type") +" - ");
					System.out.println("Amount: " + resultSet.getDouble("account_value") +" - ");
				}while(resultSet.next());
				
				do 
					{
				
						PreparedStatement statement2 = connection.prepareStatement("select * from pending_accounts where user_id = ? and account_type = ?");
						System.out.println("Enter the account you wish to approve by user_id.");
						tempID = ScannerInput.input.nextInt();
						statement2.setInt(1, tempID);
						System.out.println("Enter the account type you wish to aprove (C) or (S).");
						tempChar = ScannerInput.input.next();
						statement2.setString(2,tempChar);
			
						//statement.setString(2, tempPassword);
						//statement2.executeUpdate();
						ResultSet resultSet2 = statement2.executeQuery();
			
			if(!resultSet2.next())
			{
				System.out.println("You entered a user_id or account_type that isn't in the pending_accounts database");
				tempCheck = false;
			}
			
			else
			{
					tempUserID = resultSet2.getInt("user_id");
					tempAT = resultSet2.getString("account_type");
					tempDouble = resultSet2.getDouble("account_value");
				
				tempCheck = false;
				if(tempAT.equalsIgnoreCase("C"))
				{
					
				PreparedStatement statement3 = connection.prepareStatement("insert into checking_accounts (user_id, account_value) VALUES (?,?)");
				
				
				statement3.setInt(1, tempUserID);
				statement3.setDouble(2, tempDouble);
				
				statement3.executeUpdate();
				
				System.out.println("You've added \n user_ID " + tempUserID + "to the checking_accounts table with the amount of " + tempDouble);
				
				PreparedStatement statement4 = connection.prepareStatement("delete from pending_accounts where user_id = ? and account_type = ?");
					statement4.setInt(1,tempID);
					statement4.setString(2, tempAT);
					statement4.executeUpdate();
					
				}
				else
				{
					PreparedStatement statement3 = connection.prepareStatement("insert into saving_accounts (user_id, account_value) VALUES (?,?)");
					
					statement3.setInt(1, tempUserID);
					statement3.setDouble(2, tempDouble);
					
					statement3.executeUpdate();
					
					System.out.println("You've added \n user_ID " + tempUserID + "to the savings_accounts table with the amount of" + tempDouble);
					
					PreparedStatement statement4 = connection.prepareStatement("delete from pending_accounts where user_id = ? and account_type = ?");
						statement4.setInt(1,tempID);
						statement4.setString(2,tempAT);
						statement4.executeUpdate();
				}
			}
			}while(tempCheck);
			
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void userApprovalMenu() 
	{
		boolean tempCheck = true;
		int tempID;
		
		String tempUsername = null;
		String tempPassword = null;
		String tempFirstName = null;
		String tempLastName = null;
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
			
			PreparedStatement statement = connection.prepareStatement("select * from pending_users");
			
			
			ResultSet resultSet = statement.executeQuery();
			if(!resultSet.next())
			{
				System.out.println("There are no user accounts that are pending.");
			}
			else 
			{
			
			do
			{
			  System.out.print(resultSet.getInt(1) +" - ");
			  System.out.print(resultSet.getString("username") + " - ");
			  System.out.print(resultSet.getString("first_name") +" - "); 
			  System.out.println(resultSet.getString("last_name") +" - ");
			}while(resultSet.next());
				//resultSet = null;
			do 
			{
				
			PreparedStatement statement2 = connection.prepareStatement("select * from pending_users where user_id = ?");
			System.out.println("Enter the account you wish to approve by user_id.");
			tempID = ScannerInput.input.nextInt();
			statement2.setInt(1, tempID);
			//statement.setString(2, tempPassword);
			//statement2.executeUpdate();
			ResultSet resultSet2 = statement2.executeQuery();
			
			if(!resultSet2.next())
			{
				System.out.println("You entered a user_id that isn't in pending accounts database");
				tempCheck = true;
			}
			
			else
			{
				
				while(resultSet2.next()) {
					
					tempUsername = resultSet2.getString("username");
					tempPassword = resultSet2.getString("password");
					tempFirstName = resultSet2.getString("first_name");
					tempLastName = resultSet2.getString("last_name");
					
				}
				tempCheck = false;
				//System.out.println("Getting to put the pending_user into the users table?");
				PreparedStatement statement3 = connection.prepareStatement("insert into users (username, password, first_name, last_name) VALUES (?,?,?,?)");
				
				
				statement3.setString(1, tempUsername);
				statement3.setString(2, tempPassword);
				statement3.setString(3, tempFirstName);
				statement3.setString(4, tempLastName);
				
				statement3.executeUpdate();
				
				System.out.println("You've added " + tempFirstName + " " + tempLastName + " to the database");
				
				PreparedStatement statement4 = connection.prepareStatement("delete from pending_users where user_id = ?");
					statement4.setInt(1,tempID);
					statement4.executeUpdate();
					
			}
			}while(tempCheck);
			
			}
		}catch(SQLException e) 
		{
			e.printStackTrace();
		}
	}
	public static char startMenu() 
	{
		char tempChar;
		
		System.out.println("Welcome to Revature Bank.");
		System.out.println("(L)ogin");
		System.out.println("(C)reate Account");
		System.out.println("(E)xit");
		
		tempChar = ScannerInput.input.next().charAt(0);
		//System.out.println(tempChar);
		//tempCharStartMenu = (char) tempString.indexOf(0);
		return tempChar;
	}
	
	public static char customerAccountMenu(User user) 
	{
		char tempChar;
		System.out.println("Welcome customer " + user.getFirst_name() + " " + user.getLast_name());
		System.out.println("(A)ccounts Owened");
		System.out.println("(C)reate account");
		System.out.println("(P)ending Transactions");
		System.out.println("(T)ransfer");
		System.out.println("(E)xit");
		
		tempChar = ScannerInput.input.next().charAt(0);
		
		return tempChar;
	}
	
	public static void customerPendingTransactions(User user) 
	{
			boolean doneWithTransactions = false;
			int transactionValue;
			int sendFromUser;
			int sendToUser;
			String accountTypeFrom;
			String accountTypeTo;
			double amount;
			int tempID = (int)user.getUserID();
			
			try(Connection connection = 
					DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
				
				PreparedStatement statement = connection.prepareStatement("select * from pending_transactions where to_user=?");
				
				statement.setInt(1, tempID);
				
				ResultSet resultSet = statement.executeQuery();
				if(!resultSet.next())
				{
					System.out.println("There are no pending transactions at this time.");
					doneWithTransactions = true;
					
				}
				else 
				{
					do 
					{	System.out.print("Transaction Number: " + resultSet.getInt("pending_ID") + " - ");
						System.out.print("From User: " + resultSet.getInt("from_user") + " - ");
						System.out.print("To User: " + resultSet.getInt("to_user") +" - ");
						System.out.print("From Account: " + resultSet.getString("account_type_from") + " - ");
						System.out.print("To Account: " + resultSet.getString("account_type_to") +" - ");
						System.out.println("Amount: " + resultSet.getDouble("amount"));
						
					}while(resultSet.next());
				}
				if(!doneWithTransactions) {
				
				do {
					System.out.println("Which transaction do you want to approve? (Enter the Transaction Number)");
						transactionValue = ScannerInput.input.nextInt();
					
					PreparedStatement statement2 = connection.prepareStatement("select * from pending_transactions where pending_ID= ? and to_user = ?");
						statement2.setInt(1, transactionValue);
						statement2.setInt(2, tempID);
						
						ResultSet resultSet2 = statement.executeQuery();
						if(!resultSet2.next()) 
						{
							System.out.println("You entered a wrong transaction number please try again");
							
						}
						else
						{	
								 	sendFromUser = resultSet2.getInt("from_user");
									sendToUser = resultSet2.getInt("to_user");
									accountTypeFrom = resultSet2.getString("account_type_from");
									accountTypeTo = resultSet2.getString("account_type_to");
									amount = resultSet2.getDouble("amount");
									
									if(accountTypeFrom == "C") 
									{
										if(accountTypeTo == "C") 
										{
											deductFromCheckingAccount( sendFromUser, amount);
											addToCheckingAccount(sendToUser, amount);
											
										}
										else 
										{
											deductFromCheckingAccount( sendFromUser, amount);
											addToSavingsAccount(sendToUser, amount);
										}	
									}
									else 
									{
										if(accountTypeTo == "C") 
										{
											deductFromSavingsAccount( sendFromUser, amount);
											addToCheckingAccount(sendToUser, amount);
											
										}
										else 
										{
											deductFromSavingsAccount( sendFromUser, amount);
											addToSavingsAccount(sendToUser, amount);
										}	
										
									}
									
									PreparedStatement statement4 = connection.prepareStatement("insert into all_transactions (from_user, to_user, account_type_from, account_type_to,amount) values (?,?,?,?,?)");
									statement4.setInt(1,sendFromUser);
									statement4.setInt(2,sendToUser);
									statement4.setString(3,accountTypeFrom);
									statement4.setString(4,accountTypeTo);
									statement4.setDouble(5, amount);
									statement4.executeUpdate();
									
									PreparedStatement statement3 = connection.prepareStatement("delete from pending_transactions where from_user = ? and to_user = ? and account_type_from = ? and account_type_to = ? and amount = ?");
									statement3.setInt(1,sendFromUser);
									statement3.setInt(2,sendToUser);
									statement3.setString(3,accountTypeFrom);
									statement3.setString(4,accountTypeTo);
									statement3.setDouble(5, amount);
									statement3.executeUpdate();
							
							doneWithTransactions = true;
								
						}
						
				}while(!doneWithTransactions);
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
	}
	
	public static char employeeMenu(User user) 
	{
		char tempChar;
		System.out.println("Welcome Employee " + user.getFirst_name() + " " + user.getLast_name());
		System.out.println("(U)ser Approval");
		System.out.println("(A)ccount Apporval");
		System.out.println("(T)ransfer review");
		System.out.println("(E)xit");
		
		tempChar = ScannerInput.input.next().charAt(0);
		
		return tempChar;
	}
	
	public static User userLogin() 
	{
		User tempUser = null;
		String tempName;
		String tempPassword;
		
		System.out.println("Enter Username");
		tempName = ScannerInput.input.next();
		System.out.println("Enter Password");
		tempPassword = ScannerInput.input.next();
		
		
		try(Connection connection = 
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
			
			PreparedStatement statement = connection.prepareStatement("select * from users where username=? and password = ?");
			
			statement.setString(1,tempName);
			statement.setString(2, tempPassword);
				
			ResultSet resultSet = statement.executeQuery();
			
			//if(!resultSet.next())
			//{
				
			//}
			//else {
				
			while(resultSet.next()) 
			{
				tempUser = new User(resultSet.getInt("user_id"),resultSet.getString("username"),
						resultSet.getString("password"),resultSet.getString("first_name"), resultSet.getString("last_name"));	
			}
			//}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return tempUser;
	}
	
	
	
	public static void createUser() 
	{
		String tempUsername;
		String tempPassword;
		String tempFirstName;
		String tempLastName;
		
		
		
		try(Connection connection = 
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
			
			PreparedStatement statement = connection.prepareStatement("insert into pending_users (username, password, first_name, last_name) VALUES (?,?,?,?)");
			
			
			System.out.println("Enter Username");
			tempUsername = ScannerInput.input.next();
			statement.setString(1, tempUsername);
			
			System.out.println("Enter Password");
			tempPassword = ScannerInput.input.next();
			statement.setString(2, tempPassword);
			
			System.out.println("Enter first name");
			tempFirstName = ScannerInput.input.next();
			statement.setString(3, tempFirstName);
			
			System.out.println("Enter last name"); 
			tempLastName = ScannerInput.input.next();
			statement.setString(4, tempLastName);
			
			
			statement.executeUpdate();
			
			System.out.println("User has been created. Waiting on approval.");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void createCheckingAccountForApproval(User user , double value)
	{
		boolean tempBool = false;
		User tempUser = user;
		String tempAccount = "C";
		int tempID = user.getUserID();
		
		if(value > 0) 
		{
		try(Connection connection = 
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
			
			PreparedStatement statement = connection.prepareStatement("select * from checking_accounts where user_id=?");
			
			statement.setInt(1,tempID);
			
			ResultSet resultSet = statement.executeQuery();
			
			if(resultSet.next())
			{
				System.out.println("You already have a checking account.");
			}
			else
			{	
					PreparedStatement statement2 = connection.prepareStatement("select * from pending_accounts where user_id=? and accout_type = ?");
					
					tempID = (int)user.getUserID();
					statement2.setInt(1,tempID);		
					statement2.setString(2, tempAccount);
					ResultSet resultSet2 = statement.executeQuery();
					
					if(resultSet2.next())
					{
						System.out.println("You already have a checking account pending.");
						
					}
					else {
				PreparedStatement statement3 = connection.prepareStatement("insert into pending_accounts (user_id, account_type , account_value) values (?,?,?)");
				
				tempID = (int)user.getUserID();
				statement3.setInt(1,tempID);
				statement3.setString(2, tempAccount);
				statement3.setDouble(3, value);
			
				statement3.executeUpdate();
				
				System.out.println("You have applied for a checking account with the balance of : " + value);
				System.out.println();
				
					}
				
			}
			
			}catch(SQLException e) 
					{
					e.printStackTrace();
					}
		}
		else {
			System.out.println("You can't hava a account value 0 or lower.");
			tempBool = false;
		}
		
	}
	
	public static void createSavingAccountForApproval(User user, double value) 
	{
		User tempUser = user;
		String tempAccount = "S";
		int tempID;
		
		if(value > 0) {
		try(Connection connection = 
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
			
			PreparedStatement statement = connection.prepareStatement("select * from saving_accounts where user_id=?");
			tempID = (int)user.getUserID();
			statement.setInt(1,tempID);				
			ResultSet resultSet = statement.executeQuery();
			
			if(resultSet.next())
			{
				System.out.println("You already have a savings account.");
				
			}
					
			else
			{
				
				PreparedStatement statement2 = connection.prepareStatement("select * from pending_accounts where user_id=? and accout_type = ?");
				
				tempID = (int)user.getUserID();
				statement2.setInt(1,tempID);	
				statement2.setString(2, tempAccount);
				ResultSet resultSet2 = statement.executeQuery();
				
				if(resultSet2.next())
				{
					System.out.println("You already have a savings account pending.");
					
				}
				else 
				{
				PreparedStatement statement3 = connection.prepareStatement("insert into pending_accounts (user_id, account_type , account_value) values (?,?,?)");
				
				tempID = (int)user.getUserID();
				statement3.setInt(1,tempID);
				statement3.setString(2, tempAccount);
				statement3.setDouble(3, value);
			
				statement3.executeUpdate();
				
				System.out.println("You have applied for a saving account with the balance of : " + value);
				System.out.println();
			}
				
			}
			
			}catch(SQLException e) 
					{
					e.printStackTrace();
					}
		}
		else {
			System.out.println("You can't hava a account value 0 or lower.");
		}
	}
	
	
public static boolean isEmployee(User user)
{
	boolean tempCheck = false;
	int tempID;
	try(Connection connection = 
			DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
		
		PreparedStatement statement = connection.prepareStatement("select * from employee_list where user_id = ?");
		
		tempID = (int)user.getUserID();
		statement.setInt(1, tempID);
		//statement.setString(2, tempPassword);
			
		ResultSet resultSet = statement.executeQuery();
		
		if(!resultSet.next())
		{
			tempCheck = false;
		}
		
		else {
			tempCheck = true;
		}
	}catch(SQLException e) {
		e.printStackTrace();
	}
	
	return tempCheck;
	
}

//Region Web Methods
public static User userLoginWeb(String name, String password) throws ClassNotFoundException
{
	User tempUser = null;
	String tempName = name;
	String tempPassword = password;
	
	
	
		Class.forName("org.postgresql.Driver");
	try(Connection connection = 
			DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
		
		PreparedStatement statement = connection.prepareStatement("select * from users where username=? and password = ?");
		
		statement.setString(1,tempName);
		statement.setString(2, tempPassword);
			
		ResultSet resultSet = statement.executeQuery();
		
		//if(!resultSet.next())
		//{
			
		//}
		//else {
			
		while(resultSet.next()) 
		{
			tempUser = new User(resultSet.getInt("user_id"),resultSet.getString("username"),
					resultSet.getString("password"),resultSet.getString("first_name"), resultSet.getString("last_name"));	
		}
		//}
		
	}catch(SQLException e) {
		e.printStackTrace();
	}
	if(tempUser != null) 
	{
		currentUser = tempUser;
	}
	return tempUser;
}

public static User getCurrentUser() 
{
	return currentUser;
}

public static void setTransactionNumber(String number) 
{
	transactionNumber = number;
}

public static String getTransactionNumber() 
{
	return transactionNumber;
}

public static String customerAccountsWeb(User user) throws ClassNotFoundException
{
	boolean noCheckingAccount = false;
	boolean noSavingsAccount = false;
	boolean noPendingAccounts = false;
	boolean firstRunThru = true;
	String accountTypeSavings = "S";
	String buildString = "<table class=\"table table-hover table-stripped\">"
			+ "    <thead class=\"thead-dark\">"
			+ "        <tr>"
			+ "            <th>Account Type</th>"
			+ "            <th>Ammount</th>"
			+ "        </tr>"
			+ "    </thead><tbody>";
	
	int tempID = (int) user.getUserID();
	
		Class.forName("org.postgresql.Driver");
	try(Connection connection =
			DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
		
		PreparedStatement statement = connection.prepareStatement("select * from checking_accounts where user_id = ?");
		statement.setInt(1, tempID);
	
		ResultSet resultSet = statement.executeQuery();
		
		if(!resultSet.next()) 
		{
			noCheckingAccount = true;
		}
		else
		{
				buildString += "<tr>\r\n"
						+ "            <td>Checking</td>\r\n"
						+ "            <td>"+ resultSet.getDouble("account_value")+"</td>\r\n"
						+ "        </tr>";
		}
		
		PreparedStatement statement2 = connection.prepareStatement("select * from saving_accounts where user_id = ?");
		statement2.setInt(1, tempID);
	
		ResultSet resultSet2 = statement2.executeQuery();
		
		if(!resultSet2.next()) 
		{	
			noSavingsAccount = true;
		}
		else 
		{
			buildString += "<tr>\r\n"
					+ "            <td>Savings</td>\r\n"
					+ "            <td>"+ resultSet2.getDouble("account_value")+"</td>\r\n"
					+ "        </tr>";
		}
		
		if(!noCheckingAccount || !noSavingsAccount) 
		{
			buildString += "</tbody>\r\n"
					+ "</table>\r\n";
		}
		
		PreparedStatement statement3 = connection.prepareStatement("select * from pending_accounts where user_id = ?");
		statement3.setInt(1, tempID);
	
		ResultSet resultSet3 = statement3.executeQuery();
		
		if(!resultSet3.next()) 
		{
			noPendingAccounts = true;
		}
		else 
		{
			do {
			
				if(firstRunThru) 
				{
					
					buildString += "<br><br><br><br><table class=\"table table-hover table-stripped\">\r\n"
							+ "    <thead class=\"thead-dark\">\r\n"
							+ "        <tr>\r\n"
							+ "            <th>Pending Accounts</th>\r\n"
							+ "            <th>Ammount</th>\r\n"
							+ "        </tr>\r\n"
							+ "\r\n"
							+ "    </thead>\r\n"
							+ "    <tbody>";
				firstRunThru = false;
				}
				if(accountTypeSavings.equalsIgnoreCase(resultSet3.getString("account_type"))) 
				{
					buildString += "<tr>\r\n"
							+ "            <td>Savings</td>\r\n"
							+ "            <td>" + resultSet3.getDouble("account_value") +"</td>\r\n"
							+ "        </tr>";
				}
				else 
				{
					buildString += "<tr>\r\n"
							+ "            <td>Checking</td>\r\n"
							+ "            <td>" + resultSet3.getDouble("account_value") +"</td>\r\n"
							+ "        </tr>";
				}
			}while(resultSet3.next());
			
			buildString += "</tbody>"
					+ "</table>";
		}
		if(noPendingAccounts && noCheckingAccount &&  noSavingsAccount) 
		{
			
			buildString += "<div class=\"container\">"
					+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
					+ "            <div class=\"card-body\">"
					+ "            <blockquote class=\"card-blockquote\">"
					+ "                <p> You don't have any accounts or Pending accounts in the database."
					+ "                </p>"             
					+ "            </blockquote>"
					+ "        </div>"
					+ "    </div></div>";
//			System.out.println("You don't have any accounts or pending account in the database.");
		}
	}catch(SQLException e) {
		e.printStackTrace();
	}
	return buildString;
}

public static String employeeUserApprovalWeb() throws ClassNotFoundException
{
	boolean noPendingUsers = false;
	boolean submitHTML = true;
	int counter = 0;
	
	String buildString =  "";
	
		Class.forName("org.postgresql.Driver");
	try(Connection connection =
			DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
		
		PreparedStatement statement = connection.prepareStatement("select * from pending_users");
	
		ResultSet resultSet = statement.executeQuery();
		
		if(!resultSet.next()) 
		{
			System.out.println("you in here");
			noPendingUsers = true;
			submitHTML = false;
		}
		else
		{ 
			
			buildString += "<table class=\"table table-hover table-stripped\">"
					+ "    <thead class=\"thead-dark\">"
					+ "        <tr>"
					+ "            <th>user_id</th>"
					+ "            <th>Username</th>"
					+ "            <th>Password</th>"
					+ "            <th>First Name</th>"
					+ "            <th>Last Name</th>"
					+ "        </tr>"
					+ "    </thead><tbody>";
			
			do {
				counter += 1;
				buildString += "<tr>"
						+ "            <td>"+ resultSet.getInt("user_ID")+"</td>"
						+ "            <td>"+ resultSet.getString("username")+"</td>"
						+ "            <td>"+ resultSet.getString("password")+"</td>"
						+ "            <td>"+ resultSet.getString("first_name")+"</td>"
						+ "            <td>"+ resultSet.getString("last_name")+"</td>"
						+ "        </tr>";
				
			}while(resultSet.next());
			
			buildString += "</tbody>"
					+ "</table>";
		}
		
			
		
		if(noPendingUsers) 
		{
			
			buildString += "<div class=\"container\">"
					+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
					+ "            <div class=\"card-body\">"
					+ "            <blockquote class=\"card-blockquote\">"
					+ "                <p> There are no pending users at the moment."
					+ "                </p>"             
					+ "            </blockquote>"
					+ "        </div>"
					+ "    </div></div>";
		}
		
		if(submitHTML) {
			buildString += "<br> <br>"
					+ "<form action=\"userApprovalPage\" method=\"post\" class=\"form-inline\">"
					+ "<div class=\"container\" style=\"width: 35%\">"
					+ "    <div align=\"center\">"
					+ "        <p>Select which User you want to approve or decline.</p>"
					+ "    </div>\r\n"
					+ "    <div class=\"form-check\" >"
					+ "    <label class=\"form-check-label\">"
					+ "      <input type=\"radio\" class=\"form-check-input\" name=\"optradio\" value=\"Approve\">Approve"
					+ "    </label>"
					+ "  </div>"
					+ "  <div class=\"form-check\" >"
					+ "    <label class=\"form-check-label\">"
					+ "      <input type=\"radio\" class=\"form-check-input\" name=\"optradio\" value=\"Decline\">Decline"
					+ "    </label>"
					+ "  </div>"
					+ "    <label class=\"sr-only\" for=\"username\">Username</label>"
					+ "    <div class=\"input-group mb-2 mr-sm-2 mb-sm-0\">"
					+ "        <input type=\"text\" name=\"user_id\" class=\"form-control\" placeholder=\"User ID Number\">"
					+ "    </div>"
					+ "    <div align=\"center\">"
					+ "    <button type=\"submit\" class=\"btn btn-outline-success\">Submit</button>"
					+ "    </div>"
					+ "</div>"
					+ "</form>";
		}
	}catch(SQLException e) {
		e.printStackTrace();
	}
	return buildString;
}

public static String employeeCustomerAccountApprovalWeb() throws ClassNotFoundException
{
	boolean noPendingUsers = false;
	boolean submitHTML = true;
	String savingsAccount = "Saving";
	String checkingAccount = "Checking";
	int counter = 0;
	
	String buildString =  "";
	
		Class.forName("org.postgresql.Driver");
	try(Connection connection =
			DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
		
		PreparedStatement statement = connection.prepareStatement("select * from pending_accounts");
	
		ResultSet resultSet = statement.executeQuery();
		
		if(!resultSet.next()) 
		{
			noPendingUsers = true;
			submitHTML = false;
		}
		else
		{ 
			
			buildString += "<table class=\"table table-hover table-stripped\">"
					+ "    <thead class=\"thead-dark\">"
					+ "        <tr>"
					+ "            <th>Account ID</th>"
					+ "            <th>User ID</th>"
					+ "            <th>Account Type</th>"
					+ "            <th>Amount</th>"
					+ "        </tr>"
					+ "    </thead><tbody>";
			
			do {
				counter += 1;
				buildString += "<tr>"
						+ "            <td>"+ resultSet.getInt("account_id")+"</td>"
						+ "            <td>"+ resultSet.getString("user_id")+"</td>";
				if(resultSet.getString("account_type").equalsIgnoreCase("C")) 
				{
					buildString +=  "<td>"+ checkingAccount +"</td>"
							+ "      <td>"+ resultSet.getDouble("account_value")+"</td>"
							+ "   </tr>";	
				}
				else if(resultSet.getString("account_type").equalsIgnoreCase("S")) 
				{
					buildString +=  "<td>"+ savingsAccount +"</td>"
							+ "      <td>"+ resultSet.getDouble("account_value")+"</td>"
							+ "   </tr>";	
				}
				
				
			}while(resultSet.next());
			
			buildString += "</tbody>"
					+ "</table>";
		}
		
			
		
		if(noPendingUsers) 
		{
			
			buildString += "<div class=\"container\">"
					+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
					+ "            <div class=\"card-body\">"
					+ "            <blockquote class=\"card-blockquote\">"
					+ "                <p> There are no pending accounts at the moment."
					+ "                </p>"             
					+ "            </blockquote>"
					+ "        </div>"
					+ "    </div></div>";
		}
		
		if(submitHTML) {
			buildString += "<br> <br>"
					+ "<form action=\"accountApprovalPage\" method=\"post\" class=\"form-inline\">"
					+ "<div class=\"container\" style=\"width: 35%\">"
					+ "    <div align=\"center\">"
					+ "        <p>Select which Account do you want to approve or decline.</p>"
					+ "    </div>\r\n"
					+ "    <div class=\"form-check\" >"
					+ "    <label class=\"form-check-label\">"
					+ "      <input type=\"radio\" class=\"form-check-input\" name=\"optradio\" value=\"Approve\">Approve"
					+ "    </label>"
					+ "  </div>"
					+ "  <div class=\"form-check\" >"
					+ "    <label class=\"form-check-label\">"
					+ "      <input type=\"radio\" class=\"form-check-input\" name=\"optradio\" value=\"Decline\">Decline"
					+ "    </label>"
					+ "  </div>"
					+ "    <label class=\"sr-only\" for=\"username\">Username</label>"
					+ "    <div class=\"input-group mb-2 mr-sm-2 mb-sm-0\">"
					+ "        <input type=\"text\" name=\"account_ID\" class=\"form-control\" placeholder=\"Account ID Number\">"
					+ "    </div>"
					+ "    <div align=\"center\">"
					+ "    <button type=\"submit\" class=\"btn btn-outline-success\">Submit</button>"
					+ "    </div>"
					+ "</div>"
					+ "</form>";
		}
	}catch(SQLException e) {
		e.printStackTrace();
	}
	return buildString;
}

public static String customerPendingTransactionsWeb(User user) throws ClassNotFoundException
{
		String buildString ="";
		boolean hasPendingTransactions = false;
		int tempID = (int)user.getUserID();
		
			Class.forName("org.postgresql.Driver");
		try(Connection connection = 
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
			
			PreparedStatement statement = connection.prepareStatement("select * from pending_transactions where to_user=?");
			
			statement.setInt(1, tempID);
			
			ResultSet resultSet = statement.executeQuery();
			if(!resultSet.next())
			{
				buildString += "<div class=\"container\">\r\n"
						+ "        <div class=\"card bg-primary text-white mb-3 text-center\">\r\n"
						+ "            <div class=\"card-body\">\r\n"
						+ "            <blockquote class=\"card-blockquote\">\r\n"
						+ "                <p> You don't have any pending transactions in database.\r\n"
						+ "                </p>\r\n"             
						+ "\r\n"
						+ "            </blockquote>    \r\n"
						+ "        </div>\r\n"
						+ "\r\n"
						+ "    </div></div>";
				
				
//				System.out.println("There are no pending transactions at this time.");
				
			}
			else 
			{
				hasPendingTransactions = true;
				buildString +="<table class=\"table table-hover table-stripped\""
						+ "			    <thead class=\"thead-dark\">"
						+ "			        <tr>"
						+ "			            <th>Transaction Number</th>"
						+ "			            <th>From User </th>"
						+ "			            <th>To User</th>"
						+ "			            <th>From Account</th>"
						+ "			            <th>To Account</th>"
						+ "			            <th>Amount</th>"
						+ "			        </tr>"
						+ "			    </thead><tbody>";
				do 
				{	
					buildString += "<tr>"
							+ "            <td>"+ resultSet.getInt("pending_ID")+"</td>"
							+ "            <td>" + resultSet.getInt("from_user") +"</td>"
							+ "            <td>" + resultSet.getInt("to_user") +"</td>"
							+ "            <td>" + resultSet.getString("account_type_from") +"</td>"
							+ "            <td>" + resultSet.getString("account_type_to") +"</td>"
							+ "            <td>" + resultSet.getDouble("amount") +"</td>"
							+ "        </tr>"
							+ " ";
					
				}while(resultSet.next());
				
				buildString +="</tbody></table>";
			}
			
			if(hasPendingTransactions) 
			{
				buildString+="<form action=\"approveOrRejectPendingTransactions\" method=\"post\" class=\"form-inline\">"
						+ "<div class=\"container\" style=\"width: 35%\">"
					
						+ "    <div class=\"input-group mb-2 mr-sm-2 mb-sm-0\">"
						+ "        <input type=\"text\" name=\"transaction_Number\" class=\"form-control\" placeholder=\"Transaction Number\">"
						+ "    </div>"	
						+ "    <div align=\"center\">"
						+ "        <p>Select if you want to Approve or Reject the pending transaction.</p>"
						+ "    </div>"
						+ "    <div class=\"form-check\" >"
						+ "    <label class=\"form-check-label\">"
						+ "      <input type=\"radio\" class=\"form-check-input\" name=\"optradio1\" value=\"Approve\">Approve"
						+ "    </label>"
						+ "  </div>"
						+ "  <div class=\"form-check\" >"
						+ "    <label class=\"form-check-label\">"
						+ "      <input type=\"radio\" class=\"form-check-input\" name=\"optradio1\" value=\"Reject\">Reject"
						+ "    </label>"
						+ "  </div>"
						+ "  <br><br><br>"
						+ "    <div align=\"center\">"
						+ "    <button type=\"submit\" class=\"btn btn-outline-success\">Submit</button>"
						+ "    </div>"
						+ "</div>"
						+ "</form>";
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		
		
		
		return buildString;
}

public static String customerApproveTransactionWeb(int number) throws ClassNotFoundException, SQLException
{
	String buildString ="";
	String toAccount= "";
	double amount;
	double savingsAccountValue;
	double checkingAccountValue;
	
	User user = getCurrentUser();
	
	Class.forName("org.postgresql.Driver");
	try(Connection connection = 
			DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
		
		PreparedStatement statement = connection.prepareStatement("select * from pending_transactions where pending_ID= ? and to_user = ?");
		
		statement.setInt(1, number);
		statement.setInt(2, user.getUserID());
		
		ResultSet resultSet = statement.executeQuery();
		if(!resultSet.next())
		{
			buildString += "<div class=\"container\">"
					+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
					+ "            <div class=\"card-body\">"
					+ "            <blockquote class=\"card-blockquote\">"
					+ "                <p> You entered an invalide transaction number."
					+ "                </p>"             
					+ "            </blockquote>"
					+ "        </div>"
					+ "    </div></div>";
			
			
//			System.out.println("There are no pending transactions at this time.");
			
		}
		else 
		{
			
			
			do 
			{	
				toAccount = resultSet.getString("account_type_to");
				amount = resultSet.getDouble("amount");
				
				if(toAccount.equalsIgnoreCase("S")) 
				{
					
					PreparedStatement statement3 = connection.prepareStatement("select * from saving_accounts where user_id = ?");
					statement3.setInt(1, user.getUserID());
				
					ResultSet resultSet2 = statement3.executeQuery();
					
					
					savingsAccountValue = resultSet2.getDouble("account_value");
	
					PreparedStatement statement2 = connection.prepareStatement("Update saving_accounts set account_value = ? where user_id = ? ");
				
					statement2.setDouble(1, (savingsAccountValue + amount));
					statement2.setInt(2, number);
				
					statement2.executeUpdate();
					
					buildString += "<div class=\"container\">"
							+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
							+ "            <div class=\"card-body\">\r\n"
							+ "            <blockquote class=\"card-blockquote\">"
							+ "                <p> You have approved the transaction."
							+ "                </p>"             
							+ "            </blockquote>"
							+ "        </div>"
							+ "    </div></div>";
				}
				else
				{
					
					PreparedStatement statement4 = connection.prepareStatement("select * from checking_accounts where user_id = ?");
					statement4.setInt(1, user.getUserID());
				
					ResultSet resultSet3 = statement4.executeQuery();
					
					
					checkingAccountValue = resultSet3.getDouble("account_value");
	
					PreparedStatement statement5 = connection.prepareStatement("Update checking_accounts set account_value = ? where user_id = ? ");
				
					statement5.setDouble(1, (checkingAccountValue + amount));
					statement5.setInt(2, number);
				
					statement5.executeUpdate();
					
					buildString += "<div class=\"container\">"
							+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
							+ "            <div class=\"card-body\">\r\n"
							+ "            <blockquote class=\"card-blockquote\">"
							+ "                <p> You have approved the transaction."
							+ "                </p>"             
							+ "            </blockquote>"
							+ "        </div>"
							+ "    </div></div>";
				}
				
				
			}while(resultSet.next());
			
			
			
		}
	}catch(SQLException e) {
		e.printStackTrace();
	}
	
	return buildString;
}

public static String createAccountWeb(String amount, String radioOption) throws ClassNotFoundException

{
	String buildString ="";
	double value = Double.valueOf(amount);
	boolean tempBool = false;
	User tempUser = getCurrentUser();
	String tempAccountC = "C";
	String tempAccountS = "S";
	int tempID = tempUser.getUserID();
	System.out.println(radioOption);
	
	if(value > 0) 
	{
		Class.forName("org.postgresql.Driver");
	try(Connection connection = 
			DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
		if(radioOption.equalsIgnoreCase("Checking")) 
		{
			PreparedStatement statement = connection.prepareStatement("select * from checking_accounts where user_id=?");
			
			statement.setInt(1,tempID);
			
			ResultSet resultSet = statement.executeQuery();
			
			if(resultSet.next())
			{
				buildString += "<div class=\"container\">"
						+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
						+ "            <div class=\"card-body\">"
						+ "            <blockquote class=\"card-blockquote\">"
						+ "                <p> You already have a checking account."
						+ "                </p>"             
						+ "            </blockquote>"
						+ "        </div>"
						+ "    </div></div>";
				
			}
			else
			{	
					PreparedStatement statement2 = connection.prepareStatement("select * from pending_accounts where user_id=? and accout_type = ?");
					
					
					statement2.setInt(1,tempID);		
					statement2.setString(2, tempAccountC);
					ResultSet resultSet2 = statement.executeQuery();
					
					if(resultSet2.next())
					{
						buildString += "<div class=\"container\">"
								+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
								+ "            <div class=\"card-body\">"
								+ "            <blockquote class=\"card-blockquote\">"
								+ "                <p> You already have a checking account pending."
								+ "                </p>"             
								+ "            </blockquote>"
								+ "        </div>"
								+ "    </div></div>";
						
					}
					else {
				PreparedStatement statement3 = connection.prepareStatement("insert into pending_accounts (user_id, account_type , account_value) values (?,?,?)");
				
				statement3.setInt(1,tempID);
				statement3.setString(2, tempAccountC);
				statement3.setDouble(3, value);
			
				statement3.executeUpdate();
				
				buildString += "<div class=\"container\">"
						+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
						+ "            <div class=\"card-body\">"
						+ "            <blockquote class=\"card-blockquote\">"
						+ "                <p> You applied for a checking account with the amount of "
						+ value +"                </p>"             
						+ "            </blockquote>"
						+ "        </div>"
						+ "    </div></div>";
				
					}
				
			}
		}
		else if(radioOption.equalsIgnoreCase("Savings")) 
		{
			PreparedStatement statement = connection.prepareStatement("select * from saving_accounts where user_id=?");
			
			statement.setInt(1,tempID);
			
			ResultSet resultSet = statement.executeQuery();
			
			if(resultSet.next())
			{
				buildString += "<div class=\"container\">"
						+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
						+ "            <div class=\"card-body\">"
						+ "            <blockquote class=\"card-blockquote\">"
						+ "                <p> You already have a savings account."
						+ "                </p>"             
						+ "            </blockquote>"
						+ "        </div>"
						+ "    </div></div>";
				
			}
			else
			{	
					PreparedStatement statement2 = connection.prepareStatement("select * from pending_accounts where user_id=? and accout_type = ?");
					
					
					statement2.setInt(1,tempID);		
					statement2.setString(2, tempAccountS);
					ResultSet resultSet2 = statement.executeQuery();
					
					if(resultSet2.next())
					{
						buildString += "<div class=\"container\">"
								+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
								+ "            <div class=\"card-body\">"
								+ "            <blockquote class=\"card-blockquote\">"
								+ "                <p> You already have a savings account pending."
								+ "                </p>"             
								+ "            </blockquote>"
								+ "        </div>"
								+ "    </div></div>";
						
					}
					else {
				PreparedStatement statement3 = connection.prepareStatement("insert into pending_accounts (user_id, account_type , account_value) values (?,?,?)");
				
				statement3.setInt(1,tempID);
				statement3.setString(2, tempAccountS);
				statement3.setDouble(3, value);
			
				statement3.executeUpdate();
				
				buildString += "<div class=\"container\">"
						+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
						+ "            <div class=\"card-body\">"
						+ "            <blockquote class=\"card-blockquote\">"
						+ "                <p> You applied for a savings account with the amount of "
						+ value +"                </p>"             
						+ "            </blockquote>"
						+ "        </div>"
						+ "    </div></div>";
				
					}
				
			}
		}
		
		}catch(SQLException e) 
				{
				e.printStackTrace();
				}
	}
	else {
		buildString += "<div class=\"container\">"
				+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
				+ "            <div class=\"card-body\">"
				+ "            <blockquote class=\"card-blockquote\">"
				+ "                <p> You can't apply for an account with the value of "
				+ value +"                </p>"             
				+ "            </blockquote>"
				+ "        </div>"
				+ "    </div></div>";
	}
	return buildString;
}

public static String withdrawalAccountWeb(String amount, String radioOption) throws ClassNotFoundException

{
	String buildString ="";
	double value = Double.valueOf(amount);
	User tempUser = getCurrentUser();
	double checkingAccountValue;
	int tempID = tempUser.getUserID();
	System.out.println(radioOption);
	
	if(value > 0) 
	{
		Class.forName("org.postgresql.Driver");
	try(Connection connection = 
			DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
		if(radioOption.equalsIgnoreCase("Checking")) 
		{
			PreparedStatement statement = connection.prepareStatement("select * from checking_accounts where user_id=?");
			
			statement.setInt(1,tempID);
			
			ResultSet resultSet = statement.executeQuery();
			
			if(resultSet.next())
			{
				checkingAccountValue = resultSet.getDouble("account_value");
				
				if(checkingAccountValue - value < 0) 
				
				{
					buildString += "<div class=\"container\">"
							+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
							+ "            <div class=\"card-body\">"
							+ "            <blockquote class=\"card-blockquote\">"
							+ "                <p> You can't withdrawal that much from your checking account "
							+ value +"                </p>"             
							+ "            </blockquote>"
							+ "        </div>"
							+ "    </div></div>";
				}
				else {
				
				PreparedStatement statement2 = connection.prepareStatement("Update checking_accounts set account_value = ? where user_id = ? ");
				
				statement2.setDouble(1, (checkingAccountValue - value));
				statement2.setInt(2, tempID);
				
				statement2.executeUpdate();
				
				buildString += "<div class=\"container\">"
						+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
						+ "            <div class=\"card-body\">"
						+ "            <blockquote class=\"card-blockquote\">"
						+ "                <p> You withdrew " + value + " from your checking account "
						+ "                </p>"             
						+ "            </blockquote>"
						+ "        </div>"
						+ "    </div></div>";
				
				
				PreparedStatement iCtoW2 = connection.prepareStatement("insert into self_transfers (user_id, account_to, account_from, amount) values(?,?,?,?)");
				iCtoW2.setInt(1, tempID);
				iCtoW2.setString(2, "W");
				iCtoW2.setString(3, "C");
				iCtoW2.setDouble(4, value);
				
				iCtoW2.executeUpdate();
				
				}	
			}
			else 
			{
				buildString += "<div class=\"container\">"
						+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
						+ "            <div class=\"card-body\">"
						+ "            <blockquote class=\"card-blockquote\">"
						+ "                <p> You don't have a checking account."
						+ "                </p>"             
						+ "            </blockquote>"
						+ "        </div>"
						+ "    </div></div>";	
			}
		}
		else if(radioOption.equalsIgnoreCase("Savings")) 
		{
			
			PreparedStatement statement = connection.prepareStatement("select * from saving_accounts where user_id=?");
	
			statement.setInt(1,tempID);
			
			ResultSet resultSet = statement.executeQuery();
			
			if(resultSet.next())
			{
				checkingAccountValue = resultSet.getDouble("account_value");
				
				if(checkingAccountValue - value < 0) 
				
				{
					buildString += "<div class=\"container\">"
							+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
							+ "            <div class=\"card-body\">"
							+ "            <blockquote class=\"card-blockquote\">"
							+ "                <p> You can't withdrawal that much from your savings account "
							+ value +"                </p>"             
							+ "            </blockquote>"
							+ "        </div>"
							+ "    </div></div>";
				}
				else {
				
				PreparedStatement statement2 = connection.prepareStatement("Update saving_accounts set account_value = ? where user_id = ? ");
				
				statement2.setDouble(1, (checkingAccountValue - value));
				statement2.setInt(2, tempID);
				
				statement2.executeUpdate();
				
				buildString += "<div class=\"container\">"
						+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
						+ "            <div class=\"card-body\">"
						+ "            <blockquote class=\"card-blockquote\">"
						+ "                <p> You withdrew " + value + " from your savings account "
						+ "                </p>"             
						+ "            </blockquote>"
						+ "        </div>"
						+ "    </div></div>";
				
				PreparedStatement iStoW2 = connection.prepareStatement("insert into self_transfers (user_id, account_to, account_from, amount) values(?,?,?,?)");
				iStoW2.setInt(1, tempID);
				iStoW2.setString(2, "W");
				iStoW2.setString(3, "S");
				iStoW2.setDouble(4, value);
				
				iStoW2.executeUpdate();
				}	
			}
			else 
			{
				buildString += "<div class=\"container\">"
						+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
						+ "            <div class=\"card-body\">"
						+ "            <blockquote class=\"card-blockquote\">"
						+ "                <p> You don't have a savings account."
						+ "                </p>"             
						+ "            </blockquote>"
						+ "        </div>"
						+ "    </div></div>";	
			}
		}
		
		}catch(SQLException e) 
				{
				e.printStackTrace();
				}
	}
	else 
	{
		buildString += "<div class=\"container\">"
				+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
				+ "            <div class=\"card-body\">"
				+ "            <blockquote class=\"card-blockquote\">"
				+ "                <p> You can't withdrawal with a value of  "
				+ value +"                </p>"             
				+ "            </blockquote>"
				+ "        </div>"
				+ "    </div></div>";
	}
	return buildString;
}

public static String depositAccountWeb(String amount, String radioOption) throws ClassNotFoundException
{
	String buildString ="";
	double value = Double.valueOf(amount);
	User tempUser = getCurrentUser();
	double checkingAccountValue;
	int tempID = tempUser.getUserID();
	System.out.println(radioOption);
	
	if(value > 0) 
	{
		Class.forName("org.postgresql.Driver");
	try(Connection connection = 
			DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
		if(radioOption.equalsIgnoreCase("Checking")) 
		{
			PreparedStatement statement = connection.prepareStatement("select * from checking_accounts where user_id=?");
			
			statement.setInt(1,tempID);
			
			ResultSet resultSet = statement.executeQuery();
			
			if(resultSet.next())
			{
				checkingAccountValue = resultSet.getDouble("account_value");
				
				PreparedStatement statement2 = connection.prepareStatement("Update checking_accounts set account_value = ? where user_id = ? ");
				
				statement2.setDouble(1, (checkingAccountValue + value));
				statement2.setInt(2, tempID);
				
				statement2.executeUpdate();
				
				buildString += "<div class=\"container\">"
						+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
						+ "            <div class=\"card-body\">"
						+ "            <blockquote class=\"card-blockquote\">"
						+ "                <p> You deposited " + value + " to your checking account."
						+ "                </p>"             
						+ "            </blockquote>"
						+ "        </div>"
						+ "    </div></div>";
				
				
				PreparedStatement iCtoW2 = connection.prepareStatement("insert into self_transfers (user_id, account_to, account_from, amount) values(?,?,?,?)");
				iCtoW2.setInt(1, tempID);
				iCtoW2.setString(2, "D");
				iCtoW2.setString(3, "C");
				iCtoW2.setDouble(4, value);
				
				iCtoW2.executeUpdate();
				
				}	
			
			else 
			{
				buildString += "<div class=\"container\">"
						+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
						+ "            <div class=\"card-body\">"
						+ "            <blockquote class=\"card-blockquote\">"
						+ "                <p> You don't have a checking account."
						+ "                </p>"             
						+ "            </blockquote>"
						+ "        </div>"
						+ "    </div></div>";	
			}
		}
		else if(radioOption.equalsIgnoreCase("Savings")) 
		{
			
			PreparedStatement statement = connection.prepareStatement("select * from saving_accounts where user_id=?");
	
			statement.setInt(1,tempID);
			
			ResultSet resultSet = statement.executeQuery();
			
			if(resultSet.next())
			{
				checkingAccountValue = resultSet.getDouble("account_value");
				
				
				PreparedStatement statement2 = connection.prepareStatement("Update saving_accounts set account_value = ? where user_id = ? ");
				
				statement2.setDouble(1, (checkingAccountValue + value));
				statement2.setInt(2, tempID);
				
				statement2.executeUpdate();
				
				buildString += "<div class=\"container\">"
						+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
						+ "            <div class=\"card-body\">"
						+ "            <blockquote class=\"card-blockquote\">"
						+ "                <p> You deposited " + value + " to your savings account."
						+ "                </p>"             
						+ "            </blockquote>"
						+ "        </div>"
						+ "    </div></div>";
				
				PreparedStatement iStoW2 = connection.prepareStatement("insert into self_transfers (user_id, account_to, account_from, amount) values(?,?,?,?)");
				iStoW2.setInt(1, tempID);
				iStoW2.setString(2, "D");
				iStoW2.setString(3, "S");
				iStoW2.setDouble(4, value);
				
				iStoW2.executeUpdate();
					
			}
			else 
			{
				buildString += "<div class=\"container\">"
						+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
						+ "            <div class=\"card-body\">"
						+ "            <blockquote class=\"card-blockquote\">"
						+ "                <p> You don't have a savings account."
						+ "                </p>"             
						+ "            </blockquote>"
						+ "        </div>"
						+ "    </div></div>";	
			}
		}
		
		}catch(SQLException e) 
				{
				e.printStackTrace();
				}
	}
	else 
	{
		buildString += "<div class=\"container\">"
				+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
				+ "            <div class=\"card-body\">"
				+ "            <blockquote class=\"card-blockquote\">"
				+ "                <p> You can't deposit with a value of  "
				+ value +"                </p>"             
				+ "            </blockquote>"
				+ "        </div>"
				+ "    </div></div>";
	}
	return buildString;
}

public static String employeeApproveOrDeclineUserWeb(String user_id, String approveOrDecline) throws ClassNotFoundException {
	
	String buildString= "";
	int tempUser_id = Integer.parseInt(user_id);
	
	Class.forName("org.postgresql.Driver");
	try(Connection connection = 
			DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) 
	{
		PreparedStatement statement = connection.prepareStatement("select * from pending_users where user_id=?");
		
		statement.setInt(1,tempUser_id);
		
		ResultSet resultSet = statement.executeQuery();
		
		if(resultSet.next())
		{
			
			if(approveOrDecline.equalsIgnoreCase("Approve")) 
			{
				
			
				do {
				PreparedStatement statement2= connection.prepareStatement("insert into users(username, password, first_name, last_name) values (?,?,?,?)");
				
				statement2.setString(1,resultSet.getString("username"));
				statement2.setString(2,resultSet.getString("password"));
				statement2.setString(3,resultSet.getString("first_name"));
				statement2.setString(4,resultSet.getString("last_name"));
				
				statement2.executeUpdate();
				
				}while(resultSet.next());
				
				PreparedStatement statement4 = connection.prepareStatement("delete from pending_users where user_id=?");
				
				statement4.setInt(1, tempUser_id);
				
				statement4.executeUpdate();
				
				
				buildString += "<div class=\"container\">"
						+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
						+ "            <div class=\"card-body\">"
						+ "            <blockquote class=\"card-blockquote\">"
						+ "                <p> You approved the user with id " + tempUser_id 
						+ "                </p>"             
						+ "            </blockquote>"
						+ "        </div>"
						+ "    </div></div>";
				
				
			}
			
			else if(approveOrDecline.equalsIgnoreCase("Decline")) 
			{
				
				do {
					PreparedStatement statement3 = connection.prepareStatement("delete from pending_users where user_id=?");
					
					statement3.setInt(1, tempUser_id);
					
					statement3.executeUpdate();
					
					statement3.executeUpdate();
					
					}while(resultSet.next());
				
				buildString += "<div class=\"container\">"
						+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
						+ "            <div class=\"card-body\">"
						+ "            <blockquote class=\"card-blockquote\">"
						+ "                <p> You declined the user with id " + tempUser_id 
						+ "                </p>"             
						+ "            </blockquote>"
						+ "        </div>"
						+ "    </div></div>";
				
			}
			
		
		}
		else 
		{
			buildString += "<div class=\"container\">"
					+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
					+ "            <div class=\"card-body\">"
					+ "            <blockquote class=\"card-blockquote\">"
					+ "                <p> There is not an account with the id of " + tempUser_id
					+ "                </p>"             
					+ "            </blockquote>"
					+ "        </div>"
					+ "    </div></div>";
			
		}
		
	
	}catch(SQLException e) 
	{
	e.printStackTrace();
	}
	
	
	
	
	return buildString;
}

public static String employeeApproveOrDeclineAccountWeb(String account_id, String approveOrDecline) throws ClassNotFoundException {
	
	String buildString= "";
	int tempAccount_ID = Integer.parseInt(account_id);
	String accountType= "";
	int accountUserID = 0;
	double accountValue = 0.0;
	
	Class.forName("org.postgresql.Driver");
	try(Connection connection = 
			DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) 
	{
		PreparedStatement statement = connection.prepareStatement("select * from pending_accounts where account_id=?");
		
		statement.setInt(1,tempAccount_ID);
		
		ResultSet resultSet = statement.executeQuery();
		
		if(resultSet.next())
		{
			
			if(approveOrDecline.equalsIgnoreCase("Approve")) 
			{
				accountUserID = resultSet.getInt("user_id");
				accountType = resultSet.getString("account_type");
				accountValue = resultSet.getDouble("account_value");
				
				if(accountType.equalsIgnoreCase("C"))
				do {
				PreparedStatement statement2= connection.prepareStatement("insert into checking_accounts(user_id, account_value) values (?,?)");
				
				statement2.setInt(1, accountUserID);
				statement2.setDouble(2,accountValue);
				
				statement2.executeUpdate();
				
				}while(resultSet.next());
				
				else if(accountType.equalsIgnoreCase("S")) 
				{
					do {
						PreparedStatement statement3= connection.prepareStatement("insert into saving_accounts(user_id, account_value) values (?,?)");
						
						statement3.setInt(1, accountUserID);
						statement3.setDouble(2,accountValue);
						
						statement3.executeUpdate();
						
						}while(resultSet.next());
					
				}
				
				PreparedStatement statement4 = connection.prepareStatement("delete from pending_accounts where account_id=?");
				
				statement4.setInt(1, tempAccount_ID);
				
				statement4.executeUpdate();
				
				buildString += "<div class=\"container\">"
						+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
						+ "            <div class=\"card-body\">"
						+ "            <blockquote class=\"card-blockquote\">"
						+ "                <p> You approved the account with the id of " + tempAccount_ID 
						+ "                </p>"             
						+ "            </blockquote>"
						+ "        </div>"
						+ "    </div></div>";
				
				
			}
			
			else if(approveOrDecline.equalsIgnoreCase("Decline")) 
			{
				
				do {
					PreparedStatement statement3 = connection.prepareStatement("delete from pending_accounts where user_id=?");
					
					statement3.setInt(1, tempAccount_ID);
					
					statement3.executeUpdate();
					
					statement3.executeUpdate();
					
					}while(resultSet.next());
				
				buildString += "<div class=\"container\">"
						+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
						+ "            <div class=\"card-body\">"
						+ "            <blockquote class=\"card-blockquote\">"
						+ "                <p> You declined the account with the id of " + tempAccount_ID 
						+ "                </p>"             
						+ "            </blockquote>"
						+ "        </div>"
						+ "    </div></div>";
				
			}
		}
		else 
		{
			buildString += "<div class=\"container\">"
					+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
					+ "            <div class=\"card-body\">"
					+ "            <blockquote class=\"card-blockquote\">"
					+ "                <p> There is not an account with the id of " + tempAccount_ID
					+ "                </p>"             
					+ "            </blockquote>"
					+ "        </div>"
					+ "    </div></div>";
			
		}
	
	}catch(SQLException e) 
	{
	e.printStackTrace();
	}
	
	
	
	
	return buildString;
}

public static String allTransations() throws ClassNotFoundException
{
	String buildString= "";
	
	Class.forName("org.postgresql.Driver");
	try(Connection connection =
			DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
		
		PreparedStatement statement = connection.prepareStatement("select * from all_transactions");
	
		ResultSet resultSet = statement.executeQuery();
		
		if(!resultSet.next()) 
		{
			
		}
		else
		{ 
			buildString += "<div class=\"container bg-dark\" style=\"width: 55%\">\r\n"
					+ "            <h1 class=\"display-6\" align=\"center\" style=\"text-shadow: -1px 0 white, 0 1px white, 1px 0 white, 0 -1px white;\">External Transactions</h1>\r\n"
					+ "            </div>";
			
			buildString += "<table class=\"table table-hover table-stripped\">"
					+ "    <thead class=\"thead-dark\">"
					+ "        <tr>"
					+ "            <th>Transaction ID</th>"
					+ "            <th>From User</th>"
					+ "            <th>To User</th>"
					+ "            <th>From Account</th>"
					+ "            <th>To Account</th>"
					+ "            <th>Amount</th>"
					+ "        </tr>"
					+ "    </thead><tbody>";
			
			do {
				buildString += "<tr>"
						+ "            <td>"+ resultSet.getInt("transaction_ID")+"</td>"
						+ "            <td>"+ resultSet.getInt("from_user")+"</td>"
						+ "            <td>"+ resultSet.getInt("to_user")+"</td>"
						+ "            <td>"+ resultSet.getString("account_type_from")+"</td>"
						+ "            <td>"+ resultSet.getString("account_type_to")+"</td>"
						+ "            <td>"+ resultSet.getDouble("amount")+"</td>"
						+ "        </tr>";
				
			}while(resultSet.next());
			
			buildString += "</tbody>"
					+ "</table>";
		}
		
		PreparedStatement statement2 = connection.prepareStatement("select * from self_transfers");
		
		ResultSet resultSet2 = statement2.executeQuery();
		
		if(!resultSet2.next()) 
		{
			
		}
		else
		{ 
			buildString += "<br><br><div class=\"container bg-dark\" style=\"width: 55%\">\r\n"
					+ "            <h1 class=\"display-6\" align=\"center\" style=\"text-shadow: -1px 0 white, 0 1px white, 1px 0 white, 0 -1px white;\">Internal Transactions</h1>\r\n"
					+ "            </div>";
			
			buildString += "<table class=\"table table-hover table-stripped\">"
					+ "    <thead class=\"thead-dark\">"
					+ "        <tr>"
					+ "            <th>Transaction ID</th>"
					+ "            <th>User ID</th>"
					+ "            <th>To Account</th>"
					+ "            <th>From Account</th>"
					+ "            <th>Amount</th>"
					+ "        </tr>"
					+ "    </thead><tbody>";
			
			do {
				buildString += "<tr>"
						+ "            <td>"+ resultSet2.getInt("self_transfers_id")+"</td>"
						+ "            <td>"+ resultSet2.getInt("user_id")+"</td>"
						+ "            <td>"+ resultSet2.getString("account_to")+"</td>"
						+ "            <td>"+ resultSet2.getString("account_from")+"</td>"
						+ "            <td>"+ resultSet2.getDouble("amount")+"</td>"
						+ "        </tr>";
				
			}while(resultSet2.next());
			
			buildString += "</tbody>"
					+ "</table>";
		}
	}catch(SQLException e) {
		e.printStackTrace();
	}
	
	
	return buildString;
	
}

public static String sendUserToPendingList(String username, String password, String firstName, String lastName) throws ClassNotFoundException 
{
	String buildString= "";
	
	Class.forName("org.postgresql.Driver");
	try(Connection connection =
			DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
		
		PreparedStatement statement= connection.prepareStatement("insert into pending_users(username, password, first_name, last_name) values (?,?,?,?)");
		
		statement.setString(1, username);
		statement.setString(2,password);
		statement.setString(3,firstName);
		statement.setString(4,lastName);
		
		statement.executeUpdate();
		
	}catch(SQLException e) {
		e.printStackTrace();
	}
	
	buildString += "<div class=\"container\">"
			+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
			+ "            <div class=\"card-body\">"
			+ "            <blockquote class=\"card-blockquote\">"
			+ "                <p>"+ firstName + "  " + lastName + " you've created your account and it's waiting to be approved."
			+ "                </p>"             
			+ "            </blockquote>"
			+ "        </div>"
			+ "    </div></div>";
	return buildString;
	
}

public static String internalTransactionWeb(String amount, String fromAccount, String toAccount) throws ClassNotFoundException
{
	String buildString ="";
	double value = Double.valueOf(amount);
	User tempUser = getCurrentUser();
	double checkingAccountValue;
	double savingAccountValue;
	int tempID = tempUser.getUserID();
	
	
	if(value > 0) 
	{
		Class.forName("org.postgresql.Driver");
	try(Connection connection = 
			DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) 
	{
		if(toAccount.equalsIgnoreCase("Checking") && fromAccount.equalsIgnoreCase("Savings")) 
		{
			PreparedStatement statement = connection.prepareStatement("select * from checking_accounts where user_id=?");
			
			statement.setInt(1,tempID);
			
			ResultSet resultSet = statement.executeQuery();
			
			PreparedStatement statement3 = connection.prepareStatement("select * from saving_accounts where user_id=?");
			
			statement3.setInt(1,tempID);
			
			ResultSet resultSet2 = statement.executeQuery();
			
			if(resultSet.next() && resultSet2.next())
			{
				checkingAccountValue = resultSet.getDouble("account_value");
				savingAccountValue = resultSet2.getDouble("account_value");
				
				if(savingAccountValue - value > 0) 
				{
				
					PreparedStatement statement2 = connection.prepareStatement("Update checking_accounts set account_value = ? where user_id = ? ");
					
					double tempNumberAdd = checkingAccountValue + value;
					
					statement2.setDouble(1, tempNumberAdd);
					statement2.setInt(2, tempID);
					
					statement2.executeUpdate();
					
					PreparedStatement statement4 = connection.prepareStatement("Update saving_accounts set account_value = ? where user_id = ? ");
					
					double tempNumberMinus = savingAccountValue - value;
					
					statement4.setDouble(1, tempNumberMinus);
					statement4.setInt(2, tempID);
					
					statement4.executeUpdate();
					
					
					PreparedStatement iCtoW2 = connection.prepareStatement("insert into self_transfers (user_id, account_to, account_from, amount) values(?,?,?,?)");
					iCtoW2.setInt(1, tempID);
					iCtoW2.setString(2, "C");
					iCtoW2.setString(3, "S");
					iCtoW2.setDouble(4, value);
					
					iCtoW2.executeUpdate();
					
					buildString += "<div class=\"container\">"
							+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
							+ "            <div class=\"card-body\">"
							+ "            <blockquote class=\"card-blockquote\">"
							+ "                <p> You transfered  " + value + " from savings to checking."
							+ "                </p>"             
							+ "            </blockquote>"
							+ "        </div>"
							+ "    </div></div>";
					
				}
				else 
				{
					buildString += "<div class=\"container\">"
							+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
							+ "            <div class=\"card-body\">"
							+ "            <blockquote class=\"card-blockquote\">"
							+ "                <p> You don't have enough in your savings account the trasfer " + value
							+ "                </p>"             
							+ "            </blockquote>"
							+ "        </div>"
							+ "    </div></div>";
					
				}
				}	
			
			else if(!resultSet.next() && resultSet2.next())
			{
				buildString += "<div class=\"container\">"
						+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
						+ "            <div class=\"card-body\">"
						+ "            <blockquote class=\"card-blockquote\">"
						+ "                <p> You don't have a checking account."
						+ "                </p>"             
						+ "            </blockquote>"
						+ "        </div>"
						+ "    </div></div>";	
			}
			else if(resultSet.next() && !resultSet2.next())
			{
				buildString += "<div class=\"container\">"
						+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
						+ "            <div class=\"card-body\">"
						+ "            <blockquote class=\"card-blockquote\">"
						+ "                <p> You don't have a savings account."
						+ "                </p>"             
						+ "            </blockquote>"
						+ "        </div>"
						+ "    </div></div>";	
			}
		}
		else if(toAccount.equalsIgnoreCase("Savings") && fromAccount.equalsIgnoreCase("Checking")) 
		{
			PreparedStatement statement = connection.prepareStatement("select * from saving_accounts where user_id=?");
			
			statement.setInt(1,tempID);
			
			ResultSet resultSet = statement.executeQuery();
			
			PreparedStatement statement3 = connection.prepareStatement("select * from checking_accounts where user_id=?");
			
			statement3.setInt(1,tempID);
			
			ResultSet resultSet2 = statement3.executeQuery();
			
			if(resultSet.next() && resultSet2.next())
			{
				checkingAccountValue = resultSet2.getDouble("account_value");
				savingAccountValue = resultSet.getDouble("account_value");
				
				if(checkingAccountValue - value > 0) 
				{
				
					PreparedStatement statement2 = connection.prepareStatement("Update saving_accounts set account_value = ? where user_id = ? ");
					
					statement2.setDouble(1, (savingAccountValue + value));
					statement2.setInt(2, tempID);
					
					statement2.executeUpdate();
					
					PreparedStatement statement4 = connection.prepareStatement("Update checking_accounts set account_value = ? where user_id = ? ");
					
					statement4.setDouble(1, (checkingAccountValue - value));
					statement4.setInt(2, tempID);
					
					statement4.executeUpdate();
					
					
					PreparedStatement iCtoW2 = connection.prepareStatement("insert into self_transfers (user_id, account_to, account_from, amount) values(?,?,?,?)");
					iCtoW2.setInt(1, tempID);
					iCtoW2.setString(2, "S");
					iCtoW2.setString(3, "C");
					iCtoW2.setDouble(4, value);
					
					iCtoW2.executeUpdate();
					
					buildString += "<div class=\"container\">"
							+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
							+ "            <div class=\"card-body\">"
							+ "            <blockquote class=\"card-blockquote\">"
							+ "                <p> You transfered  " + value + " from checking to savings."
							+ "                </p>"             
							+ "            </blockquote>"
							+ "        </div>"
							+ "    </div></div>";
				}
				else 
				{
					buildString += "<div class=\"container\">"
							+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
							+ "            <div class=\"card-body\">"
							+ "            <blockquote class=\"card-blockquote\">"
							+ "                <p> You don't have enough in your checking account the trasfer " + value
							+ "                </p>"             
							+ "            </blockquote>"
							+ "        </div>"
							+ "    </div></div>";
					
				}
				}	
			
			else if(resultSet.next() && !resultSet2.next())
			{
				buildString += "<div class=\"container\">"
						+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
						+ "            <div class=\"card-body\">"
						+ "            <blockquote class=\"card-blockquote\">"
						+ "                <p> You don't have a checking account."
						+ "                </p>"             
						+ "            </blockquote>"
						+ "        </div>"
						+ "    </div></div>";	
			}
			else if(!resultSet.next() && resultSet2.next())
			{
				buildString += "<div class=\"container\">"
						+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
						+ "            <div class=\"card-body\">"
						+ "            <blockquote class=\"card-blockquote\">"
						+ "                <p> You don't have a savings account."
						+ "                </p>"             
						+ "            </blockquote>"
						+ "        </div>"
						+ "    </div></div>";	
			}
		}
		else 
		{
			buildString += "<div class=\"container\">"
					+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
					+ "            <div class=\"card-body\">"
					+ "            <blockquote class=\"card-blockquote\">"
					+ "                <p> You can't transfer to the same account"
					+ value +"                </p>"             
					+ "            </blockquote>"
					+ "        </div>"
					+ "    </div></div>";	
		}
		
		}catch(SQLException e) 
				{
				e.printStackTrace();
				}
	}
	else 
	{
		buildString += "<div class=\"container\">"
				+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
				+ "            <div class=\"card-body\">"
				+ "            <blockquote class=\"card-blockquote\">"
				+ "                <p> You can't transfer with a value of  "
				+ value +"                </p>"             
				+ "            </blockquote>"
				+ "        </div>"
				+ "    </div></div>";
	}
	return buildString;
}

public static String externalTransactionWeb(String fromAccount, String toAccount, String amount, String user_ID)  throws ClassNotFoundException
{
	String buildString ="";
	double value = Double.valueOf(amount);
	User tempUser = getCurrentUser();
	double checkingAccountValueYours;
	double checkingAccountValueTheirs;
	double savingAccountValueYours;
	double savingAccountValueTheirs;
	double tempNumberAdd = 0.0;
	double tempNumberMinus = 0.0;
	int tempID = tempUser.getUserID();
	int userID = Integer.valueOf(user_ID);
	
	
	
	if(value > 0) 
	{
		Class.forName("org.postgresql.Driver");
	try(Connection connection = 
			DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) 
	{
		
		if(toAccount.equalsIgnoreCase("Checking") && fromAccount.equalsIgnoreCase("Savings")) 
		{
			PreparedStatement statement = connection.prepareStatement("select * from checking_accounts where user_id=?");
			
			statement.setInt(1,userID);
			
			ResultSet resultSet = statement.executeQuery();
			
			PreparedStatement statement3 = connection.prepareStatement("select * from saving_accounts where user_id=?");
			
			statement3.setInt(1,tempID);
			
			ResultSet resultSet2 = statement.executeQuery();
			
			if(resultSet.next() && resultSet2.next())
			{
				checkingAccountValueTheirs = resultSet.getDouble("account_value");
				savingAccountValueYours = resultSet2.getDouble("account_value");
				
				
				if(savingAccountValueYours - value > 0) 
				{
				
					PreparedStatement statement2 = connection.prepareStatement("Update checking_accounts set account_value = ? where user_id = ?");
					
					tempNumberAdd += (checkingAccountValueTheirs + value);
					
					statement2.setDouble(1, tempNumberAdd);
					statement2.setInt(2, userID);
					
					statement2.executeUpdate();
					
					PreparedStatement statement4 = connection.prepareStatement("Update saving_accounts set account_value = ? where user_id = ?");
					
					tempNumberMinus += (savingAccountValueYours - value);
					
					statement4.setDouble(1, tempNumberMinus);
					statement4.setInt(2, tempID);
					
					statement4.executeUpdate();
					
					
					PreparedStatement iCtoW2 = connection.prepareStatement("insert into pending_transactions (from_user, to_user, account_type_from, account_type_to ,amount) values(?,?,?,?,?)");
					iCtoW2.setInt(1, tempID);
					iCtoW2.setInt(2, userID);
					iCtoW2.setString(3, "S");
					iCtoW2.setString(4, "C");
					iCtoW2.setDouble(5, value);
					
					iCtoW2.executeUpdate();
					
					buildString += "<div class=\"container\">"
							+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
							+ "            <div class=\"card-body\">"
							+ "            <blockquote class=\"card-blockquote\">"
							+ "                <p> You transfered  " + value + " from your savings account to user "+ userID + " checking account."
							+ "                </p>"             
							+ "            </blockquote>"
							+ "        </div>"
							+ "    </div></div>";
					
				}
				else 
				{
					buildString += "<div class=\"container\">"
							+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
							+ "            <div class=\"card-body\">"
							+ "            <blockquote class=\"card-blockquote\">"
							+ "                <p> You don't have enough in your savings account to trasfer " + value
							+ "                </p>"             
							+ "            </blockquote>"
							+ "        </div>"
							+ "    </div></div>";
					
				}
				}	
			
			else if(!resultSet.next() && resultSet2.next())
			{
				buildString += "<div class=\"container\">"
						+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
						+ "            <div class=\"card-body\">"
						+ "            <blockquote class=\"card-blockquote\">"
						+ "                <p> They don't have a checking account."
						+ "                </p>"             
						+ "            </blockquote>"
						+ "        </div>"
						+ "    </div></div>";	
			}
			else if(resultSet.next() && !resultSet2.next())
			{
				buildString += "<div class=\"container\">"
						+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
						+ "            <div class=\"card-body\">"
						+ "            <blockquote class=\"card-blockquote\">"
						+ "                <p> You don't have a savings account."
						+ "                </p>"             
						+ "            </blockquote>"
						+ "        </div>"
						+ "    </div></div>";	
			}
		}
		else if(toAccount.equalsIgnoreCase("Checking") && fromAccount.equalsIgnoreCase("Checking")) 
		{
			PreparedStatement statement = connection.prepareStatement("select * from checking_accounts where user_id=?");
			
			statement.setInt(1,userID);
			
			ResultSet resultSet = statement.executeQuery();
			
			PreparedStatement statement3 = connection.prepareStatement("select * from checking_accounts where user_id=?");
			
			statement3.setInt(1,tempID);
			
			ResultSet resultSet2 = statement.executeQuery();
			
			if(resultSet.next() && resultSet2.next())
			{
				checkingAccountValueTheirs = resultSet.getDouble("account_value");
				checkingAccountValueYours = resultSet2.getDouble("account_value");
				
				
				if(checkingAccountValueYours - value > 0) 
				{
				
					PreparedStatement statement2 = connection.prepareStatement("Update checking_accounts set account_value = ? where user_id = ?");
					
					tempNumberAdd += (checkingAccountValueTheirs + value);
					
					statement2.setDouble(1, tempNumberAdd);
					statement2.setInt(2, userID);
					
					statement2.executeUpdate();
					
					PreparedStatement statement4 = connection.prepareStatement("Update checking_accounts set account_value = ? where user_id = ?");
					
					tempNumberMinus += (checkingAccountValueYours - value);
					
					statement4.setDouble(1, tempNumberMinus);
					statement4.setInt(2, tempID);
					
					statement4.executeUpdate();
					
					
					PreparedStatement iCtoW2 = connection.prepareStatement("insert into pending_transactions (from_user, to_user, account_type_from, account_type_to ,amount) values(?,?,?,?,?)");
					iCtoW2.setInt(1, tempID);
					iCtoW2.setInt(2, userID);
					iCtoW2.setString(3, "C");
					iCtoW2.setString(4, "C");
					iCtoW2.setDouble(5, value);
					
					iCtoW2.executeUpdate();
					
					buildString += "<div class=\"container\">"
							+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
							+ "            <div class=\"card-body\">"
							+ "            <blockquote class=\"card-blockquote\">"
							+ "                <p> You transfered  " + value + " from your checking account to user "+ userID + " checking account."
							+ "                </p>"             
							+ "            </blockquote>"
							+ "        </div>"
							+ "    </div></div>";
					
				}
				else 
				{
					buildString += "<div class=\"container\">"
							+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
							+ "            <div class=\"card-body\">"
							+ "            <blockquote class=\"card-blockquote\">"
							+ "                <p> You don't have enough in your checking account to trasfer " + value
							+ "                </p>"             
							+ "            </blockquote>"
							+ "        </div>"
							+ "    </div></div>";
					
				}
				}	
			
			else if(!resultSet.next() && resultSet2.next())
			{
				buildString += "<div class=\"container\">"
						+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
						+ "            <div class=\"card-body\">"
						+ "            <blockquote class=\"card-blockquote\">"
						+ "                <p> They don't have a checking account."
						+ "                </p>"             
						+ "            </blockquote>"
						+ "        </div>"
						+ "    </div></div>";	
			}
			else if(resultSet.next() && !resultSet2.next())
			{
				buildString += "<div class=\"container\">"
						+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
						+ "            <div class=\"card-body\">"
						+ "            <blockquote class=\"card-blockquote\">"
						+ "                <p> You don't have a checking account."
						+ "                </p>"             
						+ "            </blockquote>"
						+ "        </div>"
						+ "    </div></div>";	
			}
		
			
		}
		else if(toAccount.equalsIgnoreCase("Savings") && fromAccount.equalsIgnoreCase("Checking")) 
		{
			PreparedStatement statement = connection.prepareStatement("select * from saving_accounts where user_id=?");
			
			statement.setInt(1,userID);
			
			ResultSet resultSet = statement.executeQuery();
			
			PreparedStatement statement3 = connection.prepareStatement("select * from checking_accounts where user_id=?");
			
			statement3.setInt(1,tempID);
			
			ResultSet resultSet2 = statement.executeQuery();
			
			if(resultSet.next() && resultSet2.next())
			{
				savingAccountValueTheirs = resultSet.getDouble("account_value");
				checkingAccountValueYours = resultSet2.getDouble("account_value");
				
				
				if(checkingAccountValueYours - value > 0) 
				{
				
					PreparedStatement statement2 = connection.prepareStatement("Update saving_accounts set account_value = ? where user_id = ?");
					
					tempNumberAdd += (savingAccountValueTheirs + value);
					
					statement2.setDouble(1, tempNumberAdd);
					statement2.setInt(2, userID);
					
					statement2.executeUpdate();
					
					PreparedStatement statement4 = connection.prepareStatement("Update checking_accounts set account_value = ? where user_id = ?");
					
					tempNumberMinus += (checkingAccountValueYours - value);
					
					statement4.setDouble(1, tempNumberMinus);
					statement4.setInt(2, tempID);
					
					statement4.executeUpdate();
					
					
					PreparedStatement iCtoW2 = connection.prepareStatement("insert into pending_transactions (from_user, to_user, account_type_from, account_type_to ,amount) values(?,?,?,?,?)");
					iCtoW2.setInt(1, tempID);
					iCtoW2.setInt(2, userID);
					iCtoW2.setString(3, "C");
					iCtoW2.setString(4, "S");
					iCtoW2.setDouble(5, value);
					
					iCtoW2.executeUpdate();
					
					buildString += "<div class=\"container\">"
							+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
							+ "            <div class=\"card-body\">"
							+ "            <blockquote class=\"card-blockquote\">"
							+ "                <p> You transfered  " + value + " from your checking to user "+ userID + " savings account."
							+ "                </p>"             
							+ "            </blockquote>"
							+ "        </div>"
							+ "    </div></div>";
					
				}
				else 
				{
					buildString += "<div class=\"container\">"
							+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
							+ "            <div class=\"card-body\">"
							+ "            <blockquote class=\"card-blockquote\">"
							+ "                <p> You don't have enough in your checking account the trasfer " + value
							+ "                </p>"             
							+ "            </blockquote>"
							+ "        </div>"
							+ "    </div></div>";
					
				}
				}	
			
			else if(!resultSet.next() && resultSet2.next())
			{
				buildString += "<div class=\"container\">"
						+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
						+ "            <div class=\"card-body\">"
						+ "            <blockquote class=\"card-blockquote\">"
						+ "                <p> They don't have a savings account."
						+ "                </p>"             
						+ "            </blockquote>"
						+ "        </div>"
						+ "    </div></div>";	
			}
			else if(resultSet.next() && !resultSet2.next())
			{
				buildString += "<div class=\"container\">"
						+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
						+ "            <div class=\"card-body\">"
						+ "            <blockquote class=\"card-blockquote\">"
						+ "                <p> You don't have a checking account."
						+ "                </p>"             
						+ "            </blockquote>"
						+ "        </div>"
						+ "    </div></div>";	
			}
		
			
		}
		
		else if(toAccount.equalsIgnoreCase("Savings") && fromAccount.equalsIgnoreCase("Savings")) 
		{
			PreparedStatement statement = connection.prepareStatement("select * from saving_accounts where user_id=?");
			
			statement.setInt(1,userID);
			
			ResultSet resultSet = statement.executeQuery();
			
			PreparedStatement statement3 = connection.prepareStatement("select * from saving_accounts where user_id=?");
			
			statement3.setInt(1,tempID);
			
			ResultSet resultSet2 = statement.executeQuery();
			
			if(resultSet.next() && resultSet2.next())
			{
				savingAccountValueTheirs = resultSet.getDouble("account_value");
				savingAccountValueYours = resultSet2.getDouble("account_value");
				
				
				if(savingAccountValueYours - value > 0) 
				{
				
					PreparedStatement statement2 = connection.prepareStatement("Update saving_accounts set account_value = ? where user_id = ?");
					
					tempNumberAdd += (savingAccountValueTheirs + value);
					
					statement2.setDouble(1, tempNumberAdd);
					statement2.setInt(2, userID);
					
					statement2.executeUpdate();
					
					PreparedStatement statement4 = connection.prepareStatement("Update saving_accounts set account_value = ? where user_id = ?");
					
					tempNumberMinus += (savingAccountValueYours - value);
					
					statement4.setDouble(1, tempNumberMinus);
					statement4.setInt(2, tempID);
					
					statement4.executeUpdate();
					
					
					PreparedStatement iCtoW2 = connection.prepareStatement("insert into pending_transactions (from_user, to_user, account_type_from, account_type_to ,amount) values(?,?,?,?,?)");
					iCtoW2.setInt(1, tempID);
					iCtoW2.setInt(2, userID);
					iCtoW2.setString(3, "S");
					iCtoW2.setString(4, "S");
					iCtoW2.setDouble(5, value);
					
					iCtoW2.executeUpdate();
					
					buildString += "<div class=\"container\">"
							+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
							+ "            <div class=\"card-body\">"
							+ "            <blockquote class=\"card-blockquote\">"
							+ "                <p> You transfered  " + value + " from your savings to user "+ userID + " savings account."
							+ "                </p>"             
							+ "            </blockquote>"
							+ "        </div>"
							+ "    </div></div>";
					
				}
				else 
				{
					buildString += "<div class=\"container\">"
							+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
							+ "            <div class=\"card-body\">"
							+ "            <blockquote class=\"card-blockquote\">"
							+ "                <p> You don't have enough in your savings account the trasfer " + value
							+ "                </p>"             
							+ "            </blockquote>"
							+ "        </div>"
							+ "    </div></div>";
					
				}
				}	
			
			else if(!resultSet.next() && resultSet2.next())
			{
				buildString += "<div class=\"container\">"
						+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
						+ "            <div class=\"card-body\">"
						+ "            <blockquote class=\"card-blockquote\">"
						+ "                <p> They don't have a savings account."
						+ "                </p>"             
						+ "            </blockquote>"
						+ "        </div>"
						+ "    </div></div>";	
			}
			else if(resultSet.next() && !resultSet2.next())
			{
				buildString += "<div class=\"container\">"
						+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
						+ "            <div class=\"card-body\">"
						+ "            <blockquote class=\"card-blockquote\">"
						+ "                <p> You don't have a savings account."
						+ "                </p>"             
						+ "            </blockquote>"
						+ "        </div>"
						+ "    </div></div>";	
			}
		}
	}catch(SQLException e) 
	{
	e.printStackTrace();
	}
	}
	else 
	{
		buildString += "<div class=\"container\">"
				+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
				+ "            <div class=\"card-body\">"
				+ "            <blockquote class=\"card-blockquote\">"
				+ "                <p> You can't transfer with a value of  "
				+ value +"                </p>"             
				+ "            </blockquote>"
				+ "        </div>"
				+ "    </div></div>";	
	}
	
	return buildString;
}

public static String externalApproveOrReject(String transactionNumber, String approveOrReject) 
{
	String buildString ="";
	double amount = 0.0;
	String accountFromString = "";
	String accountToString = "";
	int fromUser;
	int toUser;
	double fromAmount = 0.0;
	double toAmount = 0.0;
	
	int transactionNumberInt = Integer.valueOf(transactionNumber);
	
	try(Connection connection = 
			DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) 
	{
		
		PreparedStatement statement = connection.prepareStatement("select * pending_transactions where pending_ID = ?");
		statement.setInt(1, transactionNumberInt);
		
		ResultSet resultSet = statement.executeQuery();
		
		if(resultSet.next() && approveOrReject.equalsIgnoreCase("Approve")) 
		{
			
			do{
				PreparedStatement statement2= connection.prepareStatement("insert into all_transactions(from_user, to_user, account_type_from, account_type_to, amount) values (?,?,?,?,?)");
				statement2.setInt(1, resultSet.getInt("from_user"));
				statement2.setInt(2, resultSet.getInt("to_user"));
				statement2.setString(3, resultSet.getString("account_type_from"));
				statement2.setString(4, resultSet.getString("account_type_to"));
				statement2.setDouble(5, resultSet.getDouble("amount"));
				
				statement2.executeUpdate();
				
			}while(resultSet.next());
			PreparedStatement statement3 = connection.prepareStatement("delete from pending_transactions where pending_ID=?");
			
			statement3.setInt(1, transactionNumberInt);
			
			statement3.executeUpdate();
			
			buildString += "<div class=\"container\">"
					+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
					+ "            <div class=\"card-body\">"
					+ "            <blockquote class=\"card-blockquote\">"
					+ "                <p> You approved the transaction."
					+ "                </p>"             
					+ "            </blockquote>"
					+ "        </div>"
					+ "    </div></div>";
		}
		else if(resultSet.next() && approveOrReject.equalsIgnoreCase("Reject")) 
		{
			do
			{
				fromUser = resultSet.getInt("from_user");
				toUser = resultSet.getInt("to_user");
				accountFromString = resultSet.getString("account_type_from");
				accountToString = resultSet.getString("account_type_to");
				amount = resultSet.getDouble("amount");
				
				
				
				
			}while(resultSet.next());
			if(accountFromString.equalsIgnoreCase("S")) 
			{
				PreparedStatement statement4 = connection.prepareStatement("select * saving_accounts where user_id = ?");
				
				statement4.setInt(1,fromUser);
				
				ResultSet resultSet2 = statement.executeQuery();
				
				fromAmount += (resultSet2.getDouble("account_value") + amount);
				
				PreparedStatement statement5 = connection.prepareStatement("Update saving_accounts set account_value = ? where user_id = ?");
				
				statement5.setDouble(1, fromAmount);
				
				statement5.executeUpdate();	
			}
			else if(accountFromString.equalsIgnoreCase("C"))
			{
				PreparedStatement statement6 = connection.prepareStatement("select * checking_accounts where user_id = ?");
				
				statement6.setInt(1,fromUser);
				
				ResultSet resultSet3 = statement.executeQuery();
				
				fromAmount += (resultSet3.getDouble("account_value") + amount);
				
				PreparedStatement statement7 = connection.prepareStatement("Update checking_accounts set account_value = ? where user_id = ?");
				
				statement7.setDouble(1, fromAmount);
				
				statement7.executeUpdate();	
			}
			else if (accountToString.equalsIgnoreCase("C"))
			{
				PreparedStatement statement8 = connection.prepareStatement("select * checking_accounts where user_id = ?");
				
				statement8.setInt(1,toUser);
				
				ResultSet resultSet4 = statement.executeQuery();
				
				toAmount += (resultSet4.getDouble("account_value") - amount);
				
				PreparedStatement statement9 = connection.prepareStatement("Update checking_accounts set account_value = ? where user_id = ?");
				
				statement9.setDouble(1, toAmount);
				
				statement9.executeUpdate();	
			}
			else if(accountToString.equalsIgnoreCase("S"))
			{
				PreparedStatement statement10 = connection.prepareStatement("select * saving_accounts where user_id = ?");
				
				statement10.setInt(1,toUser);
				
				ResultSet resultSet5 = statement.executeQuery();
				
				toAmount += (resultSet5.getDouble("account_value") - amount);
				
				PreparedStatement statement11 = connection.prepareStatement("Update saving_accounts set account_value = ? where user_id = ?");
				
				statement11.setDouble(1, toAmount);
				
				statement11.executeUpdate();	
			}
			
			buildString += "<div class=\"container\">"
					+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
					+ "            <div class=\"card-body\">"
					+ "            <blockquote class=\"card-blockquote\">"
					+ "                <p> You rejected the transaction."
					+ "                </p>"             
					+ "            </blockquote>"
					+ "        </div>"
					+ "    </div></div>";
				
		}
		else if(!resultSet.next()) 
		{
			
		buildString += "<div class=\"container\">"
				+ "        <div class=\"card bg-primary text-white mb-3 text-center\">"
				+ "            <div class=\"card-body\">"
				+ "            <blockquote class=\"card-blockquote\">"
				+ "                <p> You entered in a invalid transaction number."
				+ "                </p>"             
				+ "            </blockquote>"
				+ "        </div>"
				+ "    </div></div>";
		}
	
	
	}catch(SQLException e) 
	{
	e.printStackTrace();
	}
	
	return buildString;
	
}




//EndRegion
}
