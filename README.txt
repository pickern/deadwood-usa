Deadwood Studios, USA
A text-based implementation for CSCI 345, Assignment 3
Nick Pickering and Chris Speckhardt, June 8th, 2018

To play, compile and run Deadwood.java in the command line, providing a single int argument with
desired number of players.

Files:

Deadwood.java
  Contains main method, runs the game

GameSystem.java
  Contains methods handling core gameplay loops and coordinates the model-view-controller

Player.java
  A class for storing player-related information and handling some turn actions

SceneCard.java
  A class for storing Scene information

SceneCardManager.java
  Creates and manages a deck of Scene Cards

Room.java
  A class for storing Room and board information, and for handling movement
  and Scene-related sequences

Role.java
  A class for containing Role information
  
BoardDisplay.java
  Processes user input and updates view

See Class Diagram for more information on class relationships, attributes and methods
