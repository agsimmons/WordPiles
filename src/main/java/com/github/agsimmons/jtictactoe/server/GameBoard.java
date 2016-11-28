package com.github.agsimmons.jtictactoe.server;

public class GameBoard {

    private final int DEFAULT_DIMENSION = 3;

    private int[][] board;

    public GameBoard() {
        board = new int[DEFAULT_DIMENSION][DEFAULT_DIMENSION];
    }

    public GameBoard(int dimension) {
        board = new int[dimension][dimension];
    }

    public String drawBoardState() {
        String boardState = " 012\n";

        for (int y = 0; y < board.length; y++) {
            boardState += y;
            for (int x = 0; x < board.length; x++) {
                switch (board[x][y]) {
                    case 1:
                        boardState += "X";
                        break;
                    case 2:
                        boardState += "O";
                        break;
                    default:
                        boardState += " ";
                        break;
                }
            }
            boardState += "\n";
        }

        return boardState;
    }

    public void makeMove(int player, int x, int y) {
        board[x][y] = player;
    }

    public int checkForWinner() {

        // Player One
        // Horizontal
        if (board[0][0] == 1 && board[1][0] == 1 && board[2][0] == 1) {
            return 1;
        } else if (board[0][1] == 1 && board[1][1] == 1 && board[2][1] == 1) {
            return 1;
        } else if (board[0][2] == 1 && board[1][2] == 1 && board[2][2] == 1) {
            return 1;
        }
        // Vertical
        if (board[0][0] == 1 && board[0][1] == 1 && board[0][2] == 1) {
            return 1;
        } else if (board[1][0] == 1 && board[1][1] == 1 && board[1][2] == 1) {
            return 1;
        } else if (board[2][0] == 1 && board[2][1] == 1 && board[2][2] == 1) {
            return 1;
        }
        // Diagonal
        if (board[0][0] == 1 && board[1][1] == 1 && board[2][2] == 1) {
            return 1;
        } else if (board[2][0] == 1 && board[1][1] == 1 && board[0][2] == 1) {
            return 1;
        }

        // Player Two
        // Horizontal
        if (board[0][0] == 2 && board[1][0] == 2 && board[2][0] == 2) {
            return 2;
        } else if (board[0][1] == 2 && board[1][1] == 2 && board[2][1] == 2) {
            return 2;
        } else if (board[0][2] == 2 && board[1][2] == 2 && board[2][2] == 2) {
            return 2;
        }
        // Vertical
        if (board[0][0] == 2 && board[0][1] == 2 && board[0][2] == 2) {
            return 2;
        } else if (board[1][0] == 2 && board[1][1] == 2 && board[1][2] == 2) {
            return 2;
        } else if (board[2][0] == 2 && board[2][1] == 2 && board[2][2] == 2) {
            return 2;
        }
        // Diagonal
        if (board[0][0] == 2 && board[1][1] == 2 && board[2][2] == 2) {
            return 2;
        } else if (board[2][0] == 2 && board[1][1] == 2 && board[0][2] == 2) {
            return 2;
        }

        return -1;
    }
}
