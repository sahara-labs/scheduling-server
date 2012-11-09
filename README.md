Sahara Labs Scheduling Server
===========

Remote laboratory framework (as deployed at https://remotelabs.eng.uts.edu.au)

What is Sahara?
-----------
Sahara Labs is a software suite developed by UTS Remote Labs that helps to enable remote access to computer controlled laboratories. It is designed to be a scalable, stable platform that enables the use and sharing of a variety of types of remote laboratories and maximises remote lab usage by implementing queuing and booking (or reservations) for users over a group of identical laboratories.

Scheduling Server
-----------
The Scheduling Server is one component of the Sahara Remote Labs framework (along with the Web Interface and Rig Client). Provides the majority of Sahara’s core functionality. It is developed using Java and requires an SQL database (either MySQL or PostgreSQL).  It manages users, gives access to rigs (with queuing or reservations) and helps to administrate the rigs associated with the remote lab. Additional development may be needed for the Scheduling Server if the functionality of Sahara is to be extended.

More information and installation files can be found at: http://sourceforge.net/projects/labshare-sahara/