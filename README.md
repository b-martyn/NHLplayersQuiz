NHL Players Quiz
==============
~Developed by Brian Martyn


#Overview

This project contains two applications.

1.  Desktop application that displays either a full list of NHL players on a selected team, or a flashcard like game that quizes users on players and their assigned number.
	1.  main class: `PlayerQuiz` in package **_quiz_**
	2.  Display is created using a **Model-View-Controller** design pattern.
		1.  Model
			1.  `Franchise`
			2.  `Player`
			3.  `TeamName`
		2.  View
			1.  `FranchiseView`
			2.  `PlayerView`
			3.  `TeamNameView`
		3.  Controller
			1.  `Controller`
	3.  `Franchise` creation uses **Factory design pattern** in `FranchiseFactory`.  Given an Enum `TeamName` value it returns the results of a MySQL stored procedure that provides a list of all players on a given team.
2.  Routine that pulls information from www.nhl.com and stores it into a MySQL database.
	1.  main class: `Updater` in package **_updater_**
	3.  **Data Access Object** design patter for updating model information
		1.  `FranchiseDAO`
		2.  `PlayerDAO`
	2.  Use of **multithreading** with thread pools to increase performance due to time required for a HTTP GET method call to convert a url into a jsoup Document
  3.  Replacment of some Collection data structure with implementated **sorting and searching Algorithms** in **_util_** package as part of Coursera course learning.


#Packages

###  **_franchise_**

    represents a NHL team


###  **_player_**

    represents a player on a NHL team


###  **_quiz_**

    Desktop application that displays the players on a NHL team to the user or quizes them on the players of that team.


###  **_screenScraper_**

    defines what's needed to find, parse, and convert a url into a desired piece of information as a String from the interface DataExtractor


###  **_teamName_**

    supplement package to franchise


###  **_update_**

    routine that updates MySQL database with player information from www.nhl.com by using the screenScraper package.


###  **_util_**

    Generic methods that combine, sort, and search arrays.
