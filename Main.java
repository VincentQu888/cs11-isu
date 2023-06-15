//necessary imports
import java.util.*;
import java.io.*;

public class Main {

  //define necessary static variables
  static PrintWriter out = new PrintWriter(System.out);
  
  static ArrayList<wall> wallList = new ArrayList<>();
  static int[][] cellIndices;
  static int[] parent;
  static int[] size;

  static ArrayList<char[]> gameState = new ArrayList<>();

  static int easyWins = 0;
  static int easyLosses = 0;
  static int mediumWins = 0;
  static int mediumLosses = 0;
  static int hardWins = 0;
  static int hardLosses = 0;


  
  //driver code
  public static void main(String[] args) { 
    Scanner sc = new Scanner(System.in);
    
    // greeting text
    System.out.println("\nWelcome to MAZE ESCAPE!!!");
    System.out.println("Type in your command to continue: ");
    System.out.println();
    System.out.println("S: Start");
    System.out.println("I: Instructions");
    System.out.println("D: Detailed Win/Loss Record");
    System.out.println("Q: Exit");
    System.out.println();
    System.out.println("Total Win/Loss Record: " + sumWins() + " Wins, " + sumLosses() + " Losses.");
    String cmd = sc.next().toLowerCase(); //input command, ignore case
    
    
    while(true){ //game should go on forever until command q is entered, conditional is not !cmd.equals("q") or else it would not print parting text
      if(cmd.equals("s")){
        flush();
        System.out.println("Select difficulty: ");
        System.out.println("---------------------------------");
        System.out.println("1: Easy Difficulty");
        System.out.println("2: Medium Difficulty");
        System.out.println("3: Hard Difficulty");
        System.out.println("R: Return to menu");
        cmd = sc.next().toLowerCase();
        flush();
        
        if(cmd.equals("1")){
          intializeStructures(11); //initialize data structures like game board
          char[][] maze = kruskals(defaultBoard(11)); //create maze
          maze[0][1] = '●'; //set player location
          
          boolean playerWin = easyBot(maze); //determine if player wins
          if(playerWin){
            System.out.println("You Won!!!");
            ++easyWins; //increment counter for easy bot
          }else{
            System.out.println("You Lost :(");
            ++easyLosses;
          }

          System.out.println("Enter any key to continue.");
          System.out.println();
          sc.next();
          flush();
        }
        else if(cmd.equals("2")){ //same logic as easy bot
          intializeStructures(17);
          char[][] maze = kruskals(defaultBoard(17));
          maze[0][1] = '●';
          
          boolean playerWin = mediumBot(maze);

          if(playerWin){
            System.out.println("You Won!!!");
            ++mediumWins;
          }else{
            System.out.println("You Lost :(");
            ++mediumLosses;
          }

          System.out.println("Enter any key to continue.");
          System.out.println();
          sc.next();
          flush();
        }
        else if(cmd.equals("3")){ //same logic as easy bot
          intializeStructures(23);
          char[][] maze = kruskals(defaultBoard(23));
          maze[0][1] = '●';
          
          boolean playerWin = hardBot(maze);

          if(playerWin){
            System.out.println("You Won!!!");
            ++hardWins;
          }else{
            System.out.println("You Lost :(");
            ++hardLosses;
          }

          System.out.println("Enter any key to continue.");
          System.out.println();
          sc.next();
          flush();
        }
        else if(cmd.equals("r")){ //will go into next iteration of game loop
          flush();
        }
        else{
          System.out.println("Unrecognized command, please try again.");

          System.out.println("Enter any key to return to menu.");
          System.out.println();
          sc.next(); //waits for input, discards because it doesnt matter
          flush();
        }
      }
      else if(cmd.equals("i")){ //instructions text, no logic
        flush();
        System.out.println("Instructions to play maze escape:");
        System.out.println("--------------------------------------");
        System.out.println();
        System.out.println("In maze escape the goal is to... escape the maze.");
        System.out.println("You will start at the top left, and will race the bot to reach the bottom right.");
        System.out.println("There are three levels of difficulty; easy, medium and hard.");
        System.out.println("The board size for each difficulty gets larger, and the bots get progressively smarter.");
        System.out.println("The bots also have the ability to travel back to anywhere they have previously been and take a step within one turn.");
        System.out.println();
        System.out.println("Controls:");
        System.out.println("w: move up");
        System.out.println("a: move left");
        System.out.println("s: move down");
        System.out.println("d: move right");
        System.out.println("q: exit mid-game");
        System.out.println("enter: submit move");
        System.out.println();
        System.out.println("Game Legend:");
        System.out.println("# - wall");
        System.out.println("  - travellable pathway");
        System.out.println("● - your character");
        System.out.println("○ - bot's character");
        System.out.println(" ");
        System.out.println();
        System.out.println("Have fun!!");
        System.out.println();
        System.out.println();

        System.out.println("Enter any key to return to menu.");
        System.out.println();
        sc.next();
        flush();
      }
      else if(cmd.equals("d")){
        flush();
        System.out.println("Easy Bot: ");
        System.out.println("Wins: " + easyWins);
        System.out.println("Losses: " + easyLosses);
        System.out.println();
        System.out.println("Medium Bot: ");
        System.out.println("Wins: " + mediumWins);
        System.out.println("Losses: " + mediumLosses);
        System.out.println();
        System.out.println("Hard Bot: ");
        System.out.println("Wins: " + hardWins);
        System.out.println("Losses: " + hardLosses);
        System.out.println();
        System.out.println();
        System.out.println("Enter any key to return to menu.");
        System.out.println();
        sc.next();
        flush();
      }
      else if(cmd.equals("q")){ //quit command, again no logic
        flush();
        System.out.println("Thanks for playing!");
        System.exit(0);
      }
      else{ //error handling, if command unrecognized wait for user input to continue
        System.out.println("Unrecognized command, please try again.");

        System.out.println("Enter any key to return to menu.");
        System.out.println();
        sc.next(); //wait for user input
        flush();
      }



      System.out.println("MAZE ESCAPE MENU"); //new menu for each iteration of game loop
      System.out.println("-----------------------------------");
      System.out.println("Type in your command to continue: ");
      System.out.println();
      System.out.println("S: Start");
      System.out.println("I: Instructions");
      System.out.println("D: Detailed Win/Loss Record");
      System.out.println("Q: Exit");
      System.out.println();
      System.out.println("Total Win/Loss Record: " + sumWins() + " Wins, " +   sumLosses() + " Losses.");
      cmd = sc.next().toLowerCase();
      flush();
    }
  }


  
  //object to store coordinates of walls to be removed in maze generation
  public static class wall{
    int x;
    int y;

    public wall(int x, int y){
      this.x = x;
      this.y = y;
    }
  }



  //game board generation
  public static char[][] defaultBoard(int length){
    char[][] board = new char[length][length]; //board setup for kruskals

    for(int i = 1; i < length-1; ++i){ //omit border of maze for kruskals or else kruskals may remove those walls
      for(int j = 1; j < length-1; ++j){
        board[i][j] = '#';
        wallList.add(new wall(i, j));
      }
    }
    
    int counter = 0; //assign each cell to a number for easier processing
    for(int i = 1; i < length-1; ++i){
      for(int j = 1; j < length-1; ++j){
        cellIndices[i][j] = counter;
        counter++;
      }
    }

    return board;
  }


  
  //kruskals
  //disjoint-set union data structure
  public static int find(int u){ //find ultimate parent of two cells, determines if they are in the same group
    if(parent[u] == u) return u;
    return parent[u] = find(parent[u]);
  }

  public static void union(int u, int v){ //combines two groups of cells by assigning their parents as each other, meaning that they are connected to each other
    u = find(u);
    v = find(v);

    if(u != v){ //size optimization, combining the smaller group into the larger group requires less traversal from the find() operation than the other way around, reduces time complexity from O(logn) for each union/find operation to O(log(log(n))), where log has base of 2
      if(size[u] > size[v]){
        parent[v] = u;
        size[u] += size[v];
      }else{
        parent[u] = v;
        size[v] += size[u];
      }
    }
  }

  
  //kruskals algorithm
  public static char[][] kruskals(char[][] maze){
    for(int i = 0; i < wallList.size(); ++i){ //shuffle list of walls by swapping randomly
      int randomIndex = (int)(Math.random()*wallList.size()); //random index to swap with

      wall temp = wallList.get(i); //basic swapping
      wallList.set(i, wallList.get(randomIndex));
      wallList.set(randomIndex, temp);
    } 


    //set certain cells near entrance/exit to usable to player or else maze may become impossible, because validity check does not include if top left or bottom right are walls or not
    maze[maze.length-2][maze.length-2] = ' '; 
    maze[1][1] = ' ';
    
    for(int i = 0; !valid(maze); ++i){ //continue until maze is valid
      int x = wallList.get(i).x; //find x and y coordinates
      int y = wallList.get(i).y;

      if(maze[x][y] == '#'){ //only if it is a wall currently
        maze[x][y] = ' ';
      
        if(x-1 >= 0){ //array index error handling
          if(find(cellIndices[x][y]) != find(cellIndices[x-1][y])){ //if not in same group
            union(cellIndices[x][y], cellIndices[x-1][y]); //unify by removing wall
          }
        }
        if(x+1 < maze.length){ //same logic for each cell up, down, left and right to chosen wall
          if(find(cellIndices[x][y]) != find(cellIndices[x+1][y])){
            union(cellIndices[x][y], cellIndices[x+1][y]);
          }
        }
        if(y-1 >= 0){
          if(find(cellIndices[x][y]) != find(cellIndices[x][y-1])){
            union(cellIndices[x][y], cellIndices[x][y-1]);
          }
        }
        if(y+1 < maze.length){
          if(find(cellIndices[x][y]) != find(cellIndices[x][y+1])){
            union(cellIndices[x][y], cellIndices[x][y+1]);
          }
        }
      }
    }
    
    

    //fill out edges
    for(int i = 0; i < maze.length; ++i){
      maze[i][0] = '#'; 
    }
    for(int i = 0; i < maze.length; ++i){
      maze[0][i] = '#'; 
    }
    for(int i = 0; i < maze.length; ++i){
      maze[i][maze.length-1] = '#'; 
    }
    for(int i = 0; i < maze.length; ++i){
      maze[maze.length-1][i] = '#'; 
    }
    maze[0][1] = ' ';
    maze[maze.length-1][maze.length-2] = ' ';

    return maze;
  }


  
  //other utility functions
  public static void printGameState(){ //prints game state
    System.out.println("Use WASD to move up, left, down and right respectively. Your board is on the top, the bot's is on the bottom. Use Q to exit.\n");
    for(int i = 0; i < gameState.size(); ++i){
      for(int j = 0; j < gameState.get(i).length; ++j){
        out.print(gameState.get(i)[j]);
      }
      out.println();
    }
    out.flush();

    gameState = new ArrayList<>();
  }

  public static void addToGameState(char[][] maze){ //add certain board to game state
    for(int i = 0; i < maze.length; ++i){
      gameState.add(Arrays.copyOf(maze[i], maze.length));
    }
    gameState.add(new char[]{}); //new line
  }

  public static void flush(){ //clears console
    for(int i = 0; i < 5; i++){ //5 is arbitrary number, ensures everything is cleared
      System.out.print("\033[H\033[2J");  //ANSI escape codes
      System.out.flush(); 
    } 
  }

  public static void intializeStructures(int length){
    int parentSize = (int)Math.pow(length-2, 2); //-2 excludes borders, squared for # of cells
    
    parent = new int[parentSize]; //initializes all structures
    size = new int[parentSize];
    cellIndices = new int[length][length]; 
    wallList = new ArrayList<>();
    
    for(int i = 0; i < parentSize; i++){
      parent[i] = i; //initially parent of cell is itself
      size[i] = 1; //initially each group is size 1
    }
  }


  public static int sumWins(){ //sums all wins for total win loss
    return easyWins + mediumWins + hardWins;
  }

  public static int sumLosses(){ //sums all losses for total win loss
    return easyLosses + mediumLosses + hardLosses;
  }



  public static boolean easyBot(char[][] maze){ //easy mode game
    Scanner sc = new Scanner(System.in);

    char[][] botMaze = new char[maze.length][maze.length]; //seperate bot maze and maze
    for(int i = 0; i < maze.length; ++i){
      for(int j = 0; j < maze.length; ++j) botMaze[i][j] = maze[i][j];
    }
    
    int[] coords = {0, 1}; //array to store coords
    botMaze[coords[0]][coords[1]] = '○'; //bot character
    

    boolean[] visited = new boolean[(int)Math.pow(maze.length-2, 2)];
    visited[cellIndices[1][1]] = true;
    
    ArrayList<Integer> pathway = dfs(maze, new ArrayList<>(), visited, 1, 1); //finds pathway the bot took
    pathway.add(0, cellIndices[1][1]);
    
    
    int x = 0; //starting x, y position 
    int y = 1;
    int botCounter = 0; //for final two moves, doesnt work normally due to indices

    addToGameState(maze); //add so print will print game state
    addToGameState(botMaze);
    printGameState();
    char cmd = sc.next().charAt(0);
    while(true){
      flush();
      
      boolean commandUnrecognized = true; //loop until recognized command is inputted
      while(commandUnrecognized){
        switch(cmd){
          case 'a':
            flush();
            maze[x][y] = ' '; //replace old spot with empty spot, player has moved past that spot now
            if(maze[x][y-1] != '#') --y; //move left, dont move if it is a wall
            commandUnrecognized = false; //break loop
            break; //break switch case
          case 'd':
            flush(); //same logic as case 'a':
            maze[x][y] = ' ';
            if(maze[x][y+1] != '#') ++y;
            commandUnrecognized = false;
            break;
          case 'w':
            flush();
            maze[x][y] = ' ';
            if(x > 0 && maze[x-1][y] != '#') --x;
            commandUnrecognized = false;
            break;
          case 's':
            flush();
            maze[x][y] = ' ';
            if(maze[x+1][y] != '#') ++x;
            commandUnrecognized = false;
            break;
          case 'q':
            easyLosses--; //subtract one because user did not actually lose, however it will count as loss to the program
            return false;
          default: //if cmd does not match any other case, it is deemed unrecognized
            flush();
            addToGameState(maze);
            addToGameState(botMaze);
            printGameState();
            System.out.println("Unrecognized command, please try again.");
            cmd = sc.next().charAt(0); //wait for next cmd before continuing
        }
      }
      
      
      if(!pathway.isEmpty()){ //if theres no more pathway for bot, it has reached the end
        botMaze[coords[0]][coords[1]] = ' ';
        coords = getCoords(pathway.get(0)); //find coords of related cell
        pathway.remove(0);
        botMaze[coords[0]][coords[1]] = '○'; //move bot to that cell
      }else if(botCounter == 0){ //because for some reason the pathway does not like storing the final two moves, i have to implement a work around for them
        botMaze[coords[0]][coords[1]] = ' '; 
        botMaze[maze.length-2][maze.length-2] = '○';
        botCounter++;
      }else if(botCounter == 1){ //final move
        botMaze[maze.length-2][maze.length-2]  = ' ';
        botMaze[maze.length-1][maze.length-2] = '○';
        botCounter++;
      }
        

      maze[x][y] = '●'; //move player to new location
      if(x == maze.length-1 && y == maze.length-2){ //if player reached destination, they won
        return true; //return true because we want playerWin to equal true if they wont 
      }else if(botCounter == 2){ //else if the bot counter has reached 2, meaning it has performed its two final moves, and it has reached the end before the player
        return false;
      }
      //else the game continues

      addToGameState(maze);
      addToGameState(botMaze);
      printGameState();
      cmd = sc.next().charAt(0); //wait for next move
    }
  }

  public static boolean mediumBot(char[][] maze){ //same logic as easy bot other than bot pathing
    Scanner sc = new Scanner(System.in);

    char[][] botMaze = new char[maze.length][maze.length];
    for(int i = 0; i < maze.length; ++i){
      for(int j = 0; j < maze.length; ++j) botMaze[i][j] = maze[i][j];
    }
    
    int[] coords = {0, 1};
    botMaze[coords[0]][coords[1]] = '○';
    

    int[] pathway = bfs(maze); //use bfs instead of dfs because even though its not necessary to deviate from one choice of path-finding algorithm, i coded bfs first and dfs second for the easybot, when i could have just coded dfs. i already had bfs though so i kept it
    ArrayList<Integer> processedPathway = new ArrayList<>();
    int cur = pathway[cellIndices[maze.length-2][maze.length-2]]; //array of all cells which the path to each cell came from

    processedPathway.add(cur); //backtrack to find direct pathway
    while(cur != 0){
      processedPathway.add(pathway[cur]); //add to pathway each cell in pathway
      cur = pathway[cur];
    }
    //this pathway is reversed, so in the bot algorithm below it will use the last item in arraylist rather than first like in easy bot
    
    
    int x = 0;
    int y = 1;
    int botCounter = 0; //for final two moves, doesnt work normally due to indices

    addToGameState(maze);
    addToGameState(botMaze);
    printGameState();
    char cmd = sc.next().charAt(0);
    while(true){
      flush();
      
      boolean commandUnrecognized = true;
      while(commandUnrecognized){
        switch(cmd){
          case 'a':
            flush();
            maze[x][y] = ' ';
            if(maze[x][y-1] != '#') --y;
            commandUnrecognized = false;
            break;
          case 'd':
            flush();
            maze[x][y] = ' ';
            if(maze[x][y+1] != '#') ++y;
            commandUnrecognized = false;
            break;
          case 'w':
            flush();
            maze[x][y] = ' ';
            if(x > 0 && maze[x-1][y] != '#') --x;
            commandUnrecognized = false;
            break;
          case 's':
            flush();
            maze[x][y] = ' ';
            if(maze[x+1][y] != '#') ++x;
            commandUnrecognized = false;
            break;
          case 'q':
            mediumLosses--; //subtract one because user did not actually lose, however it will count as loss to the program
            return false;
          default:
            flush();
            addToGameState(maze);
            addToGameState(botMaze);
            printGameState();
            System.out.println("Unrecognized command, please try again.");
            cmd = sc.next().charAt(0);
        }
      }
      
      
      if(!processedPathway.isEmpty()){
        botMaze[coords[0]][coords[1]] = ' ';
        coords = getCoords(processedPathway.get(processedPathway.size()-1));
        processedPathway.remove(processedPathway.size()-1);
        botMaze[coords[0]][coords[1]] = '○';
      }else if(botCounter == 0){
        botMaze[coords[0]][coords[1]] = ' ';
        botMaze[maze.length-2][maze.length-2] = '○';
        botCounter++;
      }else if(botCounter == 1){
        botMaze[maze.length-2][maze.length-2]  = ' ';
        botMaze[maze.length-1][maze.length-2] = '○';
        botCounter++;
      }
        

      maze[x][y] = '●';
      if(x == maze.length-1 && y == maze.length-2){
        return true;  
      }else if(botCounter == 2){
        return false;
      }

      addToGameState(maze);
      addToGameState(botMaze);
      printGameState();
      cmd = sc.next().charAt(0);
    }
  } 

  public static boolean hardBot(char[][] maze){ //same logic as medium bot
    Scanner sc = new Scanner(System.in);

    char[][] botMaze = new char[maze.length][maze.length];
    for(int i = 0; i < maze.length; ++i){
      for(int j = 0; j < maze.length; ++j) botMaze[i][j] = maze[i][j];
    }
    
    int[] coords = {0, 1};
    botMaze[coords[0]][coords[1]] = '○';
    

    int[] pathway = bfs(maze);
    ArrayList<Integer> processedPathway = new ArrayList<>();
    int cur = pathway[cellIndices[maze.length-2][maze.length-2]];

    processedPathway.add(cur); //backtrack to find direct pathway
    while(cur != 0){
      processedPathway.add(pathway[cur]);
      cur = pathway[cur];
    }
    
    
    int x = 0;
    int y = 1;
    int botCounter = 0; //for final two moves, doesnt work normally due to indices

    addToGameState(maze);
    addToGameState(botMaze);
    printGameState();
    char cmd = sc.next().charAt(0);
    while(true){
      flush();
      
      boolean commandUnrecognized = true;
      while(commandUnrecognized){
        switch(cmd){
          case 'a':
            flush();
            maze[x][y] = ' ';
            if(maze[x][y-1] != '#') --y;
            commandUnrecognized = false;
            break;
          case 'd':
            flush();
            maze[x][y] = ' ';
            if(maze[x][y+1] != '#') ++y;
            commandUnrecognized = false;
            break;
          case 'w':
            flush();
            maze[x][y] = ' ';
            if(x > 0 && maze[x-1][y] != '#') --x;
            commandUnrecognized = false;
            break;
          case 's':
            flush();
            maze[x][y] = ' ';
            if(maze[x+1][y] != '#') ++x;
            commandUnrecognized = false;
            break;
          case 'q':
            hardLosses--; //subtract one because user did not actually lose, however it will count as loss to the program
            return false;
          default:
            flush();
            addToGameState(maze);
            addToGameState(botMaze);
            printGameState();
            System.out.println("Unrecognized command, please try again.");
            cmd = sc.next().charAt(0);
        }
      }
      
      
      if(!processedPathway.isEmpty()){
        botMaze[coords[0]][coords[1]] = ' ';
        coords = getCoords(processedPathway.get(processedPathway.size()-1));
        processedPathway.remove(processedPathway.size()-1);
        botMaze[coords[0]][coords[1]] = '○';
      }else if(botCounter == 0){
        botMaze[coords[0]][coords[1]] = ' ';
        botMaze[maze.length-2][maze.length-2] = '○';
        botCounter++;
      }else if(botCounter == 1){
        botMaze[maze.length-2][maze.length-2]  = ' ';
        botMaze[maze.length-1][maze.length-2] = '○';
        botCounter++;
      }
        

      maze[x][y] = '●';
      if(x == maze.length-1 && y == maze.length-2){
        return true;  
      }else if(botCounter == 2){
        return false;
      }

      addToGameState(maze);
      addToGameState(botMaze);
      printGameState();
      cmd = sc.next().charAt(0);
    }
  } 


  public static ArrayList<Integer> dfs(char[][] maze, ArrayList<Integer> pathway, boolean[] visited, int curX, int curY){ //pathfinding for easybot
    if(curX == maze.length-1 && curY == maze.length-2) return pathway; //if reached end, we know we have found a path for the bot to the end of the maze

    //four if statements to look at all four possible pathways, each move either up, down, left, or right is a possibility for the bot to visit
    if(curY > 0 && maze[curX][curY-1] != '#' && !visited[cellIndices[curX][curY-1]]){ //array index error handling, also if we havent visited the next cell yet then we will visit it, this makes the bot less intelligent than the other bots because it tries every possibility regardless of path length
      visited[cellIndices[curX][curY-1]] = true; //mark as visited so that the bot does not infinitely visit the same cells
      pathway.add(cellIndices[curX][curY-1]); //add to pathway
      dfs(maze, pathway, visited, curX, curY-1); //call pathfinding as if the next cell is the current cell
    }
    if(curY < maze.length - 1 && maze[curX][curY + 1] != '#' && !visited[cellIndices[curX][curY+1]]){ //same logic as above block
      visited[cellIndices[curX][curY+1]] = true;
      pathway.add(cellIndices[curX][curY+1]);
      dfs(maze, pathway, visited, curX, curY+1);
    }
    if(curX > 0 && maze[curX - 1][curY] != '#' && !visited[cellIndices[curX-1][curY]]){
      visited[cellIndices[curX-1][curY]] = true;
      pathway.add(cellIndices[curX-1][curY]);
      dfs(maze, pathway, visited, curX-1, curY);
    }
    if(curX < maze.length - 1 && maze[curX + 1][curY] != '#' && !visited[cellIndices[curX+1][curY]]){
      visited[cellIndices[curX+1][curY]] = true;
      pathway.add(cellIndices[curX+1][curY]);
      dfs(maze, pathway, visited, curX+1, curY);
    }

    return pathway;
  }
  
  
  public static int[] bfs(char[][] maze){ //same logic as "dfs", but uses an ArrayList to store X and Y coordinates of the next cell to visit rather than calling the function again
    ArrayList<Integer> queueX = new ArrayList<>(); //queue for both x and y coordinate
    ArrayList<Integer> queueY = new ArrayList<>();
    int[] step = new int[(int)Math.pow(maze.length-2, 2)]; //also has a "step" array to keep track of the distance of the shortest possible path to each cell so far, if any path is shorter than the current shortest path we know we have found a shorter path which the bot can take
    boolean[] visited = new boolean[(int)Math.pow(maze.length-2, 2)];
    int[] pathway = new int[(int)Math.pow(maze.length-2, 2)]; //cell where each cell has come from to get the shortest path to that cell
    
    Arrays.fill(step, Integer.MAX_VALUE);

    queueX.add(1);
    queueY.add(1);
    step[cellIndices[1][1]] = 0;
    while(!queueX.isEmpty()){ //continue until there are no more potential cells to visit which are candidates for a shorter possible path
      int curX = queueX.get(0);
      queueX.remove(0); //remove because they do not need to be potentially visited again after being visited once
      int curY = queueY.get(0);
      queueY.remove(0);

      //the rest is same as dfs
      if(curX == maze.length-1 && curY == maze.length-2) break;
      
      if(curY > 0 && maze[curX][curY-1] != '#' && !visited[cellIndices[curX][curY-1]]){
        visited[cellIndices[curX][curY-1]] = true;
        step[cellIndices[curX][curY-1]] = step[cellIndices[curX][curY]]+1;
        pathway[cellIndices[curX][curY-1]] = cellIndices[curX][curY];
        queueX.add(curX);
        queueY.add(curY-1);
      }
      if (curY < maze.length - 1 && maze[curX][curY + 1] != '#' && !visited[cellIndices[curX][curY+1]]) {
        visited[cellIndices[curX][curY+1]] = true;
        step[cellIndices[curX][curY + 1]] = step[cellIndices[curX][curY]] + 1;
        pathway[cellIndices[curX][curY + 1]] = cellIndices[curX][curY];
        queueX.add(curX);
        queueY.add(curY + 1);
      }
      if (curX > 0 && maze[curX - 1][curY] != '#' && !visited[cellIndices[curX-1][curY]]) {
        visited[cellIndices[curX-1][curY]] = true;
        step[cellIndices[curX][curY]] = step[cellIndices[curX - 1][curY]] + 1;
        pathway[cellIndices[curX - 1][curY]] = cellIndices[curX][curY];
        queueX.add(curX - 1);
        queueY.add(curY);
      }
      if (curX < maze.length - 1 && maze[curX + 1][curY] != '#' && !visited[cellIndices[curX+1][curY]]) {
        visited[cellIndices[curX+1][curY]] = true;
        step[cellIndices[curX][curY]] = step[cellIndices[curX + 1][curY]] + 1;
        pathway[cellIndices[curX + 1][curY]] = cellIndices[curX][curY];
        queueX.add(curX + 1);
        queueY.add(curY);
      }
    }

    return pathway;
  }

  public static boolean valid(char[][] maze){ //same logic as bfs, except has no shortest path optimization element, it only exists to check if it is at all possible to get to the end from the start
    Queue<Integer> queueX = new LinkedList<>();
    Queue<Integer> queueY = new LinkedList<>();
    boolean[] visited = new boolean[(int)Math.pow(maze.length-2, 2)];

    queueX.add(1);
    queueY.add(1);
    
    while(!queueX.isEmpty()){
      int curX = queueX.poll();
      int curY = queueY.poll();
      
      if(curY > 0 && maze[curX][curY-1] != '#' && !visited[cellIndices[curX][curY-1]]){
        visited[cellIndices[curX][curY-1]] = true;
        queueX.add(curX);
        queueY.add(curY-1);
      }
      if (curY < maze.length - 1 && maze[curX][curY + 1] != '#' && !visited[cellIndices[curX][curY+1]]) {
        visited[cellIndices[curX][curY+1]] = true;
        queueX.add(curX);
        queueY.add(curY + 1);
      }
      if (curX > 0 && maze[curX - 1][curY] != '#' && !visited[cellIndices[curX-1][curY]]) {
        visited[cellIndices[curX-1][curY]] = true;
        queueX.add(curX - 1);
        queueY.add(curY);
      }
      if (curX < maze.length - 1 && maze[curX + 1][curY] != '#' && !visited[cellIndices[curX+1][curY]]) {
        visited[cellIndices[curX+1][curY]] = true;
        queueX.add(curX + 1);
        queueY.add(curY);
      }
    }
    
    return visited[cellIndices[maze.length-2][maze.length-2]]; //if we have visited the bottom right cell, we know for sure that there is a path to that cell, meaning it is possible to complete the maze
  }


  public static int[] getCoords(int cellIndicesIndex){ //get coordinates of a cell from its related number assigned to it previously
    for(int i = 1; i < cellIndices.length-1; ++i){ //loop until we find a row with cellIndex greater than our number, then we know it must be within that row, because it is greater than all numbers in the row below, but less than some number in this row
      if(cellIndices[i][cellIndices.length-2] >= cellIndicesIndex){
        for(int j = 1; j < cellIndices.length-1; ++j){ //loop until we find that exact index
          if(cellIndices[i][j] == cellIndicesIndex) return new int[]{i, j}; //return cell indices as array
        }
        break;
      }
    }

    return null;
  }
}
//end of program!!
