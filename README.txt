Author: Brendan Thoeung | (619)370-1843 | Student ID: 007494550 | September 15, 2022

Scheduling Software - 
Add, modify, or deleted scheduled appointments. Add, modify, or delete customers for appointments.
View reports of all upcoming appointments and see all customers that have an appointment.
The purpose of the program is to create appointments for customers. The program is general purpose,
meaning the appointments are not specific. The program allows for easy creation of appointments without
worrying about overlapping appointment times.

IDE Used: IntelliJ IDEA Community Edition 2021.1.3
JDK Used: 11.0.16.1
JavaFx:	  18.0.2

Directions: To use the program please see steps below:
1.) Enter a valid username and password on the login page that corresponds to
one of the user accounts within the SQL database. Once logged in, the program
will take you to the main menu.

2.) View appointments or view customers using the radio buttons at the top of the main menu screen.
The add, modify, and delete buttons on the left side of the screen change depending on whethered you're
viewing appointments or viewing customers.
	.Add, modify, or delete the corresponding appointment or customer depending on your selection.
	.When viewing appointments, you can filter by Month or Week depending on the radio selection that
		appears on the top right of the screen. Simply select the date you would like to filter to
		using the date selection widget that appears to the right of the "view by month" radio button.

3.) To Add Appointment, verify the "view appointments" radio button is selected at the top of the screen.
Select the add appointment button on the left side of the screen. The program
will take you to the add appointments page. Once there, fill out all of the text fields and select the customer
you would like to make an appointment for. Select a start date, and the end date will automatically be filled.
A pop up window will appear displaying the time frame for when the establishment is in operation in your local time zone.
Select a start time and end time that corresponds to EST time in your local time zone. Once all fields are filled, select save
the appointment.

3A) To modify an appointment, verify the "view appointments" radio button is selected at the top of the screen. Then, select the appointment you would like to modify from the table view and then select on the 
modify appointment button on the left side of the screen. Then fill the fields accordingly as seen in step 3.

3B) To delete an appointment, verify the "view appointments" radio button is selected at the top of the screen. Then,
select the appointment you would like to delete from the table view and then select delete appointment from the left
side of the screen to delete that appointment.

4.) To add a customer, verify the "view customers" radio button is selected at the top of the screen.
Select the add customer button of the left side of the screen. The program will take you to the add customers page.
Once there, fill out all of the fields with appropriate information and click on save to save the customer.

4A) To modify a customer, verify the "view customers" radio button is selected at the top of the screen. 
Then, select the customer you would like to modify from the table view and then select on the 
modify customer button on the left side of the screen. Then fill the fields accordingly as seen in step 4.

4B) To delete customer, verify the "view customers" radio button is selected at the top of the screen. Then,
select the customer you would like to delete from the table view and then select delete appointment from the left
side of the screen to delete that customer. The customer will not delete if the customer is associated with
an appointment. To delete an appointment, refer to step 3B.

5.) To review reports, select on reports on the left side of the screen. The program will take you to the reports page. 
Select on either the "contact schedule" or "total customers" radio buttons at the top of the screen. Then, choose an option
from the options drop down menu and make an appropriate selection to view the corresponding information within the table view and
the total count at the bottom left of the screen. To go back, select the back button on the bottom right side of the screen. 
When viewing total customers, you can view the customers that made an appointment either by type, month, and by project
requirements A3F, by country. 

	A3F) I decided to add an option to view all customers that made an appointment, by country. The user
	will be able to display all customers who made an appointment that corresponds to their country within
	the table view.

mysql-connector-java-8.0.30 

  