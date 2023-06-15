# Maze Escape
This was made by me in grade 10 so if the explanations below are not good sorry about that!  
<br/>
<br/>
Group Members: Vincent

Game Title: Maze Escape

Game Idea: A small game where the user attempts to escape a randomly generated maze before a bot does.  

Character List:   
User  
Bot  

Rules:  
You will start at the top left, and will race the bot to reach the bottom right.
There are three levels of difficulty; easy, medium and hard.
The board size for each difficulty gets larger, and the bots get progressively smarter.
The bots also have the ability to travel back to anywhere they have previously been and take a step within one turn.

Controls:  
w: move up  
a: move left  
s: move down  
d: move right  
q: exit mid-game  
enter: submit move  

Legend:  
\# - wall (not travellable)  
" " - travellable pathway  
● - your character  
○ - bot's character   

  
<br/>
<br/>


Although I definitely could have made a higher quality game, the marking criteria was based on difficulty to create the game, so the game is just more difficult to create than anything. Sorry!


Game Logic Explanation:  
There are a few main parts to the game, namely:  
Random Maze Generation  
Bot Movement and Difficulty  
Player Movement  
Menu  

This explanation will cover each of these parts individually, and how we combine them to create the final product.   

Maze Generation:  
The first part I worked on, and arguably most important, is the random maze generation. The generation works by using a modified version of Kruskal’s algorithm for minimum spanning tree (https://en.wikipedia.org/wiki/Kruskal%27s_algorithm) for maze generation (https://en.wikipedia.org/wiki/Maze_generation_algorithm#Randomized_Kruskal's_algorithm). To perform this algorithm, we must first define a few terms:  
Wall: wall is a standard wall in a maze, neither the player nor bot can pass through a wall. Walls are represented by # characters in the game.  
Cell: a cell is an individual coordinate set on a grid, for example there is a cell at position (2, 3) on the game board.   
Set: a set is a group of unified and connected cells. If two cells are connected we define them as part of the same set.  
Parent: a parent is a cell which another cell is connected to. For example, if we connect (2, 3) to (3, 3), (3, 3) is the parent of (2, 3).  
Ultimate Parent: the cell with no parents is the ultimate parent of the set, because it is parent to all other cells in the set by some relation  


First, we store each wall cell in an ArrayList, named wallList in our program. We do this to later shuffle the ArrayList using a for loop by selecting a random index in the ArrayList and swapping it with the item at the current index.   

Now that we have a randomized list of wall positions, we can proceed with the algorithm that will now generate random mazes because of the shuffled wall list. The process of the algorithm is as follows:  

While there is no valid path, verified by the valid() function which returns a boolean, true if there is a valid pathway from start to end and false if not, continue the following:  
1. Select next wall in wall list  
2. Check if it is in the same set as any of the cells directly adjacent to it by checking if their ultimate parent is the same using the find() function  
3. If it is not in the same set as any of the cells directly adjacent to it  
	a) Combine the cells into the same set by setting their ultimate parent to the same cell  
	b) Set the current wall to an empty pathway  

Eventually, this is guaranteed to create a valid pathway. In the worst case scenario it will take down every wall and every pathway will be valid. The algorithm combines cells into the same set to prevent repeats of step 3.  


Once we have created a working maze, we can worry about the player and bot movement in the maze.  

The player's controls are created by simply monitoring the input of the player into the console and a switch case inside a while loop. While the command is not a valid command, meaning while the input is not an input the switch case has a case for, the loop will continue. This handles errors by waiting for a valid player input. If the input is valid, the switch case will adjust the current X, Y coordinates of the player, stored by two variables. The default case tells the player that the command is unrecognized, and continues the while loop.  

The bot movement is slightly more complex. The easy bot, medium bot and hard bot all use different logic to make them seem more or less “intelligent” to the player. The easy bot uses a depth-first search (https://www.geeksforgeeks.org/depth-first-search-or-dfs-for-a-graph/), to simulate a less intelligent player searching every possible pathway, which works like this starting at the top left:
1. For each adjacent cell, check if:  
	a) The cell is not out of bounds for the maze  
	b) The bot has not visited this cell before (stored in boolean array visited[])  
2. If all conditions are true, send the bot along this pathway by calling the search function with the new coordinates. Also add the new cell to the pathway ArrayList.  
3. Continue until all pathways have been visited  

This search tries out all pathways in the maze, which will then be added to the pathway ArrayList. The easyBot() function then uses this ArrayList of each cell the bot has travelled in order to display the path of the bot.  

On the other hand, the medium and hard bots both use a breadth-first search (https://www.geeksforgeeks.org/breadth-first-search-or-bfs-for-a-graph/), to find the shortest possible path the bot or player could take starting at the top left.   
1. For each adjacent cell, check if:  
	a) The cell is not out of bounds for the maze  
	b) The bot has not visited this cell before  
2. If all conditions are true, send the bot along this pathway by adding the new coordinates to an ArrayList which stores all the cells which the bot needs to visit next. Rather than adding this cell to a pathway arraylist, set the parent of the next cell (same definition, but different parent to Kruskal’s) to the current cell. This will allow us to backtrack from the parent of the destination cell all the way to the starting cell, to construct a shortest path.  
3. Continue until all pathways have been visited  

Returning the list of parents of each cell will allow us to construct a path in our medium and hard bot functions. The valid() function uses the same approach to check if it is possible to even complete the maze; if the function has visited the destination cell, we know it is possible to complete the maze and vice versa.   

The menu does not require much logic to create. We create the menu by printing out each of the menu options, and storing the user input in a String or char variable cmd. We compare the user input to the options, if it does not match any of the options we can handle the error by informing the user that their input was not valid and waiting for a new input. If it does match any of the commands, we can print out the respective menus or features for that option within an if statement.  

Finally, to combine each of the parts together, there are a few utility functions and parts that glue the project together. Each of the game's difficulties have their own function, which returns a boolean true or false based on whether the user won against the bot. This is called in the game menu when a difficulty is selected. The bot’s pathfinding is also called within these functions. To print the game state, there is a printGameState() function, which is usually accompanied by addToGameState() which adds the current board to the game state, and flush() which flushes the console to better the user experience. Moreover, to ease the programming of the algorithms, each cell is mapped to a certain number in the cellIndices[][] array, and the coordinates of each number is found using the getCoords() function.  

By using these utility functions to combine everything together, we create our finished product.
