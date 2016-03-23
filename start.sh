#!/bin/bash
DIR="$(cd -P "$( dirname "${BASH_SOURCE[0]}" )" && pwd)"
cd "$DIR"

DO_LOOP="no"

while getopts "p:f:l" OPTION 2> /dev/null; do
	case ${OPTION} in
		f)
			NUKKIT_FILE="$OPTARG"
			;;
		l)
			DO_LOOP="yes"
			;;
		\?)
			break
			;;
	esac
done

if [ "$NUKKIT_FILE" == "" ]; then
	if [ -f ./nukkit.jar ]; then
		NUKKIT_FILE="./nukkit.jar"
	elif [ -f ./Nukkit.jar ]; then
		NUKKIT_FILE="./Nukkit.jar"
	elif [ -f ./nukkit-1.0-SNAPSHOT.jar ]; then
		NUKKIT_FILE="./nukkit-1.0-SNAPSHOT.jar"
	else
		echo "Couldn't find a valid Nukkit installation."
		exit 1
	fi
fi

LOOPS=0

set +e
while [ "$LOOPS" -eq 0 ] || [ "$DO_LOOP" == "yes" ]; do
	if [ "$DO_LOOP" == "yes" ]; then
		java -jar "$NUKKIT_FILE" $@
	else
		exec java -jar "$NUKKIT_FILE" $@
	fi
	((LOOPS++))
done

if [ ${LOOPS} -gt 1 ]; then
	echo "Restarted $LOOPS times"
fi
