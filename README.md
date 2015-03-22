# README #

This project aims to show a simple example on how to start using Akka with Java.

It consists on:

* Two actors: Stringer (Main actor) and Reverser (Secondary actor)
* Tests for both actors
* A type of Message to indicate if the actor must Split the string or its work is done
* A main class to start the system

The Stringer Actor, receives a String from the Main class, splits it and sends each of the chunks to a Reverser Actor.
The Reverser Actor, prints in console the chunk in reverse order and sends a message to its Sender Actor to notify that its work is done.

When all of the Reverser Actors has finished their work, the Stringer Actor shutdown the system.

