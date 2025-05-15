import java.util.*;

public class Minesweeper {
    static Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        int boardSize = 9;
        int mines = 10;
    
        int[][] board = new int[boardSize][boardSize];
        boolean[][] explored = new boolean[boardSize][boardSize];
    
        setMines(board, mines);
        countAdjacentMines(board);

    
        while (isGameOver(board, explored) == false) {
            showBoard(board, explored);

            int[] choice = getUserChoice(explored);
            int boardRow = choice[0];
            int boardColumn = choice[1];
    
            explored[boardRow][boardColumn] = true;
    
            if (isGameOver(board, explored) == true && board[boardRow][boardColumn] == -1) {
                // This changes how to display the mines, and then shows the board after the for loop
                for (int row = 0; row < boardSize; row++) {
                    for (int column = 0; column < boardSize; column++) {
                        if (board[row][column] == -1) {
                            board[row][column] = -1;
                            explored[row][column] = true;
                        }
                    }
                }
                showBoard(board, explored);

                System.out.println("YOU LOSE!");
            } else if (isGameOver(board, explored) == true) {
                for (int row = 0; row < boardSize; row++) {
                    for (int column = 0; column < boardSize; column++) {
                        if (board[row][column] == -1) {
                            board[row][column] = -2;
                            explored[row][column] = true;
                        }
                    }
                }
                showBoard(board, explored);

                // I had trouble defining what the win state might look like. The game will play to completion, but upon completion, the program will simply end without showing the win board.
                System.out.println("YOU WIN!");
            } else if (board[boardRow][boardColumn] == 0) {
                floodFill(board, explored, boardRow, boardColumn);
            }
        }
    }
    // This method, showBoard, was created by Dr. Matthew Eicholtz of Florida Southern College, for use in CSC2290's "Project 2: Minesweeper"
    public static void showBoard(int[][] board, boolean[][] explored) {
        /*Display the board throughout the game using specific characters.*/
        int spacing = 2;  // how many spaces to use for each cell in the board?

        // Print column number
        System.out.println();
        System.out.print(" ".repeat(3));
        for (int i = 0; i < board.length; i++) {
            System.out.printf("%" + spacing + "d", i);
        }

        // Print dashed line to separate header from board
        System.out.println("\n" + "-".repeat(board.length * spacing + 4));

        // Use a nested loop to show contents of board
        for (int i = 0; i < board.length; i++) {
            // Print row number
            System.out.printf("%d |", i);

            // Print contents of row
            for (int j = 0; j < board[i].length; j++) {
                if (explored[i][j]) {
                    if (board[i][j] == 0) {
                        System.out.printf("%" + spacing + "c", ' ');
                    }
                    else if (board[i][j] == -1) {
                        System.out.printf("%" + spacing + "c", 'x');
                    }
                    else if (board[i][j] == -2) {
                        System.out.printf("%" + spacing + "c", '*');
                    }
                    else {
                        System.out.printf("%" + spacing + "d", board[i][j]);
                    }
                }
                else {
                    System.out.printf("%" + spacing + "c", '.');
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    // This method, floodFill, was created by Dr. Matthew Eicholtz of Florida Southern College, for use in CSC2290's "Project 2: Minesweeper"
    public static void floodFill(int[][] board, boolean[][] explored, int row, int col) {
        /*Recursively explore the board from a starting cell that is not adjacent to any 
        mines to find the boundary of connected cells that are adjacent to mines. This 
        method should be called when the player selects a cell that is not adjacent to 
        any mines. For any given cell at a specific row and column, there are eight 
        adjacent cells to search. Each adjacent cell will either have a value of 0, which 
        means we should recursively search from that cell, or a value of 1-8, which means
        we have found part of the boundary.
        */

        // Check upper-left cell
        if (row > 0 && col > 0 && !explored[row-1][col-1]) {
            explored[row-1][col-1] = true;
            if (board[row-1][col-1] == 0) {
                floodFill(board, explored, row-1, col-1);
            }
        }

        // Check upper cell
        if (row > 0 && !explored[row-1][col]) {
            explored[row-1][col] = true;
            if (board[row-1][col] == 0) {
                floodFill(board, explored, row-1, col);
            }
        }
        
        // Check upper-right cell
        if (row > 0 && col < board[row].length - 1 && !explored[row-1][col+1]) {
            explored[row-1][col+1] = true;
            if (board[row-1][col+1] == 0) {
                floodFill(board, explored, row-1, col+1);
            }
        }

        // Check left cell
        if (col > 0 && !explored[row][col-1]) {
            explored[row][col-1] = true;
            if (board[row][col-1] == 0) {
                floodFill(board, explored, row, col-1);
            }
        }

        // Check right cell
        if (col < board[row].length - 1 && !explored[row][col+1]) {
            explored[row][col+1] = true;
            if (board[row][col+1] == 0) {
                floodFill(board, explored, row, col+1);
            }
        }

        // Check bottom-left cell
        if (row < board.length - 1 && col > 0 && !explored[row+1][col-1]) {
            explored[row+1][col-1] = true;
            if (board[row+1][col-1] == 0) {
                floodFill(board, explored, row+1, col-1);
            }
        }

        // Check bottom cell
        if (row < board.length - 1 && !explored[row+1][col]) {
            explored[row+1][col] = true;
            if (board[row+1][col] == 0) {
                floodFill(board, explored, row+1, col);
            }
        }

        // Check bottom-right cell
        if (row < board.length - 1 && col < board[row].length - 1 && !explored[row+1][col+1]) {
            explored[row+1][col+1] = true;
            if (board[row+1][col+1] == 0) {
                floodFill(board, explored, row+1, col+1);
            }
        }
    }

    public static int[][] setMines(int[][] board, int numberOfMines) {
        Random random = new Random();
        int[][] mines = new int[numberOfMines][2];
        int count = 0;
    
        while (count < numberOfMines) {
            int row = random.nextInt(board.length);
            int column = random.nextInt(board[0].length);
    
            // Checks if there's already an existing mine where the program is trying to place it
            if (board[row][column] != -1) {
                board[row][column] = -1;
                mines[count][0] = row;
                mines[count][1] = column;
                count++;
            }
        }
        return mines;
    }
    
    public static void countAdjacentMines(int[][] board) {
        // These variables are provide data pairs for whatever surrounds the current selection (i.e. pairs of [-1, -1] or [-1, 0])
        int[] surroundingRow = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] surroundingColumn = {-1, 0, 1, -1, 1, -1, 0, 1};
    
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[0].length; column++) {
                if (board[row][column] == -1) continue;
    
                int count = 0;
                for (int i = 0; i < surroundingRow.length; i++) {
                    int newRow = row + surroundingRow[i];
                    int newColumn = column + surroundingColumn[i];
    
                    // This limits the programs search within the bounds of the board, and then counts the mines
                    if (newRow >= 0 && newRow < board.length && newColumn >= 0 && newColumn < board[0].length && board[newRow][newColumn] == -1) {
                        count++;
                    }
                }
                board[row][column] = count;
            }
        }
    }

    public static int[] getUserChoice(boolean[][] explored) {
        while (true) {
            System.out.print("Select a valid cell (row, col): ");
            int row = in.nextInt();
            int col = in.nextInt();
    
            if (row >= 0 && row < explored.length && col >= 0 && col < explored[0].length && !explored[row][col]) {
                return new int[]{row, col};
            } else if (explored[row][col]) {
                System.out.println("That cell has already been swept! Try again.");
            } else {
                System.out.println("Sorry, one or more of those inputs are invalid. Try again.");
            }
        }
    }
    
    public static boolean isGameOver(int[][] board, boolean[][] explored) {
        // This is initialized to true as default because the method will set the bool to false if the game isn't over
        boolean over = true;

        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[0].length; column++) {
                // Catches when the user selects a mine
                if (board[row][column] == -1 && explored[row][column]) {
                    return true;
                }
                // Checks to see if all the mines have NOT been explored
                if (board[row][column] != -1 && !explored[row][column]) {
                    over = false;
                }
            }
        }
        return over;
    }    
}