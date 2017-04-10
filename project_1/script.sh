#!/bin/bash

rm -r Peer*
javac */*.java 

echo "Compiling..."


sleep 1

echo "LAUNCHING PEERS"

gnome-terminal -e "java -cp . peer/Peer 1 224.0.0.3 4444 224.0.0.3 4445 224.0.0.3 4446"

gnome-terminal -e "java -cp . peer/Peer 2 224.0.0.3 4444 224.0.0.3 4445 224.0.0.3 4446"

gnome-terminal -e "java -cp . peer/Peer 3 224.0.0.3 4444 224.0.0.3 4445 224.0.0.3 4446"

echo "PEERS LAUNCHED"





