#args: <peer_ap> <operation> <operands>

echo $2

if [ "$2" != "BACKUP" -a "$2" != "DELETE" -a "$2" != "RESTORE" -a "$2" != "RECLAIM" -a "$2" -!= "STATE"]; then
	echo "Operation not existent"
	exit 1;
fi


if [ "$2" = "BACKUP" ]; then
	echo "received backup"
	if [ "$#" -ne 4]; then
		echo "Wrong number of arguments for BACKUP"
		exit 1;
	fi
fi

if [ "$2" = "DELETE" ]; then
	if [ "$#" -ne 3]; then
		echo "Wrong number of arguments for DELETE"
		exit 1;
	fi
fi

if [ "$2" = "RESTORE" ]; then
	if [ "$#" -ne 3]; then
		echo "Wrong number of arguments for RESTORE"
		exit 1;
	fi
fi

if [ "$2" = "RECLAIM" ]; then
	if [ "$#" -ne 3]; then
		echo "Wrong number of arguments for RECLAIM"
		exit 1;
	fi
fi

if [ "$2" = "STATE" ]; then
	if [ "$#" -ne 3]; then
		echo "Wrong number of arguments for STATE"
		exit 1;
	fi
fi

gnome-terminal -e "java -cp . UI/testApp $1 $2 $3 $4"
