# FlexDesk

The main functionality of the FlexDesk application is to provide a system for creating and booking desks. 

Some features are:
* Self-registry 
* Booking desks
* Cancel bookings
* Filtering for desks
* Creating new desks
* Editing desks
* Deleting desks
* Creating new user accounts
* ... and much more


# System requirements
**Operating system**  
The application should be runnable on most modern operating systems if the required deployment tools are installed 
and the instructions of the [manual](doc/Manual.md) are followed.
It was specifically tested on Windows 10 version 22H2 , Debian 11 with kernel 5.10*, Manjaro 22.0.0 sikaris
Ubuntu 22.04 and macOs Ventura 13.2.

**Required deployment tools**
- git, see https://git-scm.com/
- Java 11 OpenJDK, see https://jdk.java.net/java-se-ri/11 or a later version
- Docker, see https://www.docker.com/
- A tool for editing the contents of a database, like DBeaver (see https://dbeaver.io/)

**Further recommended tools for development**
- IntelliJ IDEA, see https://www.jetbrains.com/de-de/idea/
- Scene Builder, see https://gluonhq.com/products/scene-builder/#download
- node.js, see https://nodejs.org/en/download/

*see [Known bugs](#known-bugs) below

# Launching the application
Read the  [manual](doc/Manual.md) for a description of how to get started with the FlexDesk programm.

# Known bugs
* Restoring of a deleted desk causes an exception.
* A multideskbooking is not specific to one user, e.g. every Project Lead can see every multideskbooking
* If a desk is booked for a whole day, it is not removed from the view and can still be booked for that day
* A long deskname will cause an overlap in the visual representation of a desk in the Main- and Admin view
* It is possible to create rooms with the same name
* There may occur some graphical bugs with some buttons when specifically running on a debian machine.
