#!/bin/bash

# Default options
DATABASE=mysql
LOCAL=yes
HOST=127.0.0.1
PORT=3306
DBNAME=sahara
USERNAME=sahara
PASSWORD=

function getDatabaseOption {
    DBOPT=""
    # Database options
    echo "Database options:"
    echo "   1. MySQL (default)"
    echo "   2. PostgreSQL"
    echo 
    read -p "Which database option? [1] " DBOPT

    case "$DBOPT" in
	1)
	    echo
	    echo "### Using MySQL ###############"
	    echo
	    DATABASE="mysql"
	    ;;
	2)
	    echo
	    echo "### Using PostgreSQL ##########"
	    echo
	    DATABASE="postgresql"
	    PORT=5432
	    ;;
	
	"")
	    echo
	    echo "### Using MySQL ###############"
	    echo
	    DATABASE="mysql"
	    ;;
	*)
	    echo
	    echo "ERROR: The options are '1' or '2'"
	    echo
	    getDatabaseOption
    esac
}

function populatePostgreSQL {
    AUTHTYPE=""
    # PostgreSQL authentication
    echo "The create the database a super user connection must be created to the"
    echo "database."
    
    if [[ HOST == "127.0.0.1" ]] ; then
	echo "As the database server is hosted locally, the authentication options are:"
	echo "  1. Ident sameuser "
    fi
}

pushd . &> /dev/null
cd `dirname $0`

echo "Setting up the Scheduling Server"
echo "--------------------------------"
echo

# Database server type
getDatabaseOption

# Database server hostname
DBOPT=""
read -p "What is the database server hostname? [localhost] " DBOPT
if [[ ! $DBOPT == "" ]] ; then
    HOST=$DBOPT
fi

# Database server port number
DBOPT=""
read -p "What is the database server port number? [$PORT] " DBOPT
if [[ ! $DBOPT == "" ]] ; then
    PORT=$DBOPT
fi

# Database name
DBOPT=""
read -p "What is the Sahara database name? [sahara] " DBOPT
if [[ ! $DBOPT == "" ]] ; then
    DBNAME=$DBOPT
fi

# Database username
DBOPT=""
read -p "What is the Sahara database user name? [sahara] " DBOPT
if [[ ! $DBOPT == "" ]] ; then
    USERNAME=$DBOPT
fi

# Database password 
DBOPT=""
read -p "What is the Sahara database password? [generated] " DBOPT
if [[ $DBOPT == "" ]] ; then
    PASSWORD=`cat /dev/urandom | tr -dc "a-zA-Z0-9" | fold -w 8 | head -n 1`
    echo
    echo "### Generated password is $PASSWORD"
else
    PASSWORD=$DBOPT
fi


echo 
echo "The next option allows the Sahara database to be created and populated"
echo "with the Sahara database table structure. If the database already exists"
echo "it will be dropped and recreated. The default is to not create the "
echo "database."
echo
read -p "Create database? [y|N] " CREATEDB

while [[ $CREATEDB != 'y' && $CREATEDB != 'n' && $CREATEDB != "" ]] ; do
    read -p "The options are 'y' or 'n': " CREATEDB
done;

ROOTUSER=
ROOTPASSWD=

if [[ $CREATEDB == 'y' && $DATABASE == "mysql" ]] ; then # Import the MySQL database
    
    # Root user
    read -p "What is the database server root user name? [root] " ROOTUSER
    if [[ ROOTUSER == "" ]] ; then
	ROOTUSER="root"
    fi

    # Root password 
    read -p "What is the database server root password? [no password] " ROOTPASSWORD

elif [[ $CREATEDB == 'y' && $DATABASE == "postgresql" ]] ; then # Import the PostgreSQL database
    if [[ $HOST == "127.0.0.1" || $HOST == "localhost" ]] ; then
        # First make sure the server is running.
	if [[ `ps -C postgres | grep postgres | awk -F' ' '{ printf $1 }'` -gt 0 ]] ; then
	    echo
	    echo "Detected PostgreSQL is running..."
	else
	    echo
	    echo "Attempting to start PostgreSQL..."
	    if [[ -f /etc/init.d/postgresql ]] ; then
		/etc/init.d/postgresql start
		sleep 2
	    elif [[ PINIT=`ls /etc/init.d/postgresql-*` && $? -eq 0 ]] ; then
                # Some distrubutions use a init script with the version number
	        # appended to it.
		PINIT=`ls /etc/init.d/postgresql-*`
		if [[ $? == 0 ]] ; then
		    $PINIT start
		    sleep 2
		fi
	    else
		echo "Local PostgreSQL database is not running and cannot be started."
		echo "Please check PostgreSQL is installed and start it manually."
		exit 1
	    fi
	fi

	# Check the server actually started
	if [[ `ps -C postgres | grep postgres | awk -F' ' '{ printf $1 }'` -eq 0 ]] ; then
	    echo 
	    echo "Unable to start database, failing. Please manually start the database"
	    echo "server, then rerun this script."
	    exit 1
	fi
	echo
    fi
    
    populatePostgreSQL
fi

popd &> /dev/null
