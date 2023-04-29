# FlexDesk - Manual

## Starting the application
To start the application, make sure the database is running, or start the database.
To do this, navigate into `/flexDesk` and run 
`docker-compose up database`<br>
This command my vary for different operating systems. A variation could be
`docker compose up database`<br>
Now start the application with `./gradlew bootRun`.


## Login
To log in, start the application and enter your credentials. Now press `Enter` or click `login`.


## Register
To register a new account, start the application and press `Register` in the Login View. 
A new window will open where you can enter your unique username. If done correctly, your account will be created by clicking `Registrieren` or pressing `Enter`.


## Booking of a desk
To book a desk, simply log in and find a desk you want to book. Now click `Buchen`.
A new window will open and display details about the desk. You can now select a day and a timeframe for when you want to book the desk.
By clicking `Buchen` in this window, the desk will be booked for the selected time.


## Cancel your booking
To cancel a booking you made, log in and  click the button `Löschen` of the corresponding booking. After confirming, the booking will be canceled.


## Create a multidesk booking (Project lead only)
To create a multideskbooking (in the following "**mdb**"), click the button `Multidesk modus`.
Two new buttons next to this button will appear and one of them allows to choose a timeframe.
Now select all the desks you want to book. By clicking `Buchen`, a window will open up and list all selected desks.
You can click on a desk to see details about it. Create the mdb by clicking `Buchen`.


## Cancel a multidesk booking (Project lead only)
Canceling a mdb is the same as [canceling a normal booking](#cancel-your-booking).


## Filtering for desks
To filter for desks, log in and select the checkboxes on the left which fit to your needs.
You can also select a date in the datepicker to check for available desks for a specific day.


## Creating an Admin Account for the first time
To get started, we will create an admin account once this way. 
You will be presented a Login view. From there, click on the text `Register`. Fill in the required fields and press enter or click
`Registrieren`.
Now close the application and open DBeaver. Connect DBeaver to the database, navigate to `users` and set the role of your user to `2`.
We can now log in with the admin account. From now on, we are able to create new admin accounts via the UI.


## Navigate to the admin view (Admin only)
For accessing admin functionalities, log in with an admin account and click the button `Admin` 
on the top right.


## Navigate to overall settings (Admin only)
For navigating to the overall settings, navigate to the admin view and click the button `Overall settings`
on the top right


## Create new users (Admin only)
To create new users with different roles, navigate to the overall settings and click the button `+hinzufügen`
in the `Nutzer` box on the bottom left. A new window will open. You can now fill in the required fields.
Not selecting a role will choose the role "user" by default. A user has the lowest "rights" and can only
book desks, filter for desks and cancel bookings. Selecting `Project lead` will allow the user to make multidesk bookings.


## Add new locations/ buildings
Open up DBeaver and connect it to the database if not already done. Navigate to `gebaeude`. Simply add a new building by choosing an ID
and a name and add them in the corresponding fields. Save the changes and close DBeaver.


## Create a new room (Admin only)
For creating a new room, navigate to the overall settings and click the button `+hinzufügen`
in the `Räume` box at the top right. A new window will open. There you can enter the name for the room
and select a building where the room should be in. The room will be created by clicking `hinzufügen` in that window.


## Create a feature (Admin only)
To create a new feature, navigate to the Admin view and click the button `+ Add Feature`.
A new window will open where you can enter the name of the feature and add it by clicking `Hinzufügen` in this window.


## Create a desk (Admin only)
To create a new desk, navigate to the Admin view and click the button `Add Desk`. A new window will open.
In this window, specify select the building by clicking `Gebäude`, the room by clicking `Raum` 
and enter a name for the desk.
You can set the Internet speed for the desk but this is not required. 
You can also add features by clicking `Features`. To remove a feature from the desk click the button `Remove` of the corresponding feature.
By clicking `Tisch hinzufügen`, the desk will be created and ready to be booked.


## Edit a desk (Admin only)
To edit a desk, navigate to the Admin view and click the button `Bearbeiten` for the corresponding desk.
A new window will open where you can edit the desk properties. Any changes will be saved by clicking `Bestätigen`.
**Note that this will affect any kind of booking which is connected to that desk.**


## Cancel any existing booking (Admin only)
To cancel any existing booking, navigate to the Admin View and click the button `Löschen` 
in the column `Gebuchte Tische` of the booking you want to cancel.
After confirming, the booking will be canceled.


## Delete a desk (Admin only)
To delete a desk, navigate to the Admin view and click the button `Löschen` in the column `Alle Tische`.
After confirming, the desk will be deleted. **Note that every booking connected to the desk is deleted as well.**


## Restore a deleted desk (Admin only, currently not working)
To restore a deleted desk, navigate to the overall settings.
Find the desk you want to restore in the bottom right and click `wiederherstellen`. This (should) restore the desk.


## Set how far in advance bookings can be made (Admin only)
To set how far in advance bookings can be made, navigate to the overall settings.
Enter the desired timeframe in the corresponding fields on the top left and click the corresponding `Speichern` button.



## Set minimal password length (Admin only)
To set the minimal password length, navigate to the overall settings.
On the top left, enter the length, a password should be in the corresponding field. Save the changes by clicking the corresponding `Speichern` - button


## Activate/ deactivate parental mode (Admin only)
Activating the parental mode will allow users to make bookings on moring and afternoon.
When deactivated, only bookings for a full day can be made.
To change the state of the parental mode, navigate to the overall settings.
On the top left box, select or un-select the checkbox  `Parental mode` and click the corresponding `Speichern` button.

