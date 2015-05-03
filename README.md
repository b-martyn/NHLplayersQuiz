NHL Players Quiz
==============
~Developed by Brian Martyn

Connection to MySQL database that stores players in the NHL and creates a flashcard-like game that shows the number of a player on a team and present their first and last name and position on a click of the mouse on the screen.

Package quiz

  	Classes
  
    Controller - controls views of each model in a card layout
    
    PlayerQuiz - main class


Package updater

  	Classes
  
    DOAHelper - controls the data access for each model
    
    EntryCreator - callable class that returns a map entry
    
      implements Callable<Entry<TeamName, Player>>, Entry<TeamName, Player>
      
    Updater = main class
