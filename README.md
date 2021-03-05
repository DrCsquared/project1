# Project 1 - Banking Application
**Project Description**

The Banking API will manage the bank accounts of its users. It will be managed by the Bank's employees and admins. Employees and Admins count as Standard users with additional abilities. All users must be able to update their personal information, such as username, password, first and last names, as well as email. Accounts owned by users must support withdrawal, deposit, and transfer. Transfer of funds should be allowed between accounts owned by the same user, as well as between accounts owned by different users. Standard users should be able to register and login to see their account information. They can have either Checking or Savings accounts. Employees can view all customer information, but not modify in any way. Admins can both view all user information, as well as directly modify it.

**Technologies Used**
* Eclipse EE 
* PostgreSQL - Version 12.0
* pgAdmin - Version 4.0
* Apache-Tomcat - Version 9.0.41

**Freatures**

User Features
* Login as a User.
* Transfer money from one account type to another(checking or savings).
* Transfer money to other user accounts.
* Approve or Reject transactions from other users.
* Apply for a checking or savings account.
* Apply for a user account.

Employee Features.
* Approve or reject user accounts.
* Approve or reject checking or savings accounts.
* See all transactions. 

**To-do list:**
* Be able to create Employee Accounts.
* Clean up the UI/UX.

**Getting Started**
* Download Eclipse, PostgreSQL, pgAdmin, and Apache-Tomcat. 
* Clone project1 from git to your PC. 


**Usage**
* Start up the PostgreSQL and connect to pgAdmin so you can connect to the database.
* Start up Eclipse and launch the Tomcat server.(Under the source. Click on servers -> click on Tomcat v9.0 -> Click the green play button.)
* Run project through the Tomcat server and not as a java program. (right click project1 -> Run as -> Run on Server.)

