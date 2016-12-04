/*
 * The MIT License
 *
 * Copyright 2016 Andrew Simmons
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.agsimmons.jtictactoe.server;

public class GameBoard {

    private static final int DEFAULT_DIMENSION = 3;

    private int[][] board;

    public GameBoard() {
        board = new int[DEFAULT_DIMENSION][DEFAULT_DIMENSION];
    }

    public GameBoard(int dimension) {
        board = new int[dimension][dimension];
    }

    public String drawBoardState() {
        String boardState = "  012\n";

        for (int y = 0; y < board.length; y++) {
            boardState += y + " ";
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

    public int getCellState(int firstNum, int secondNum) {
        return board[firstNum][secondNum];
    }

    public int checkForWinner() {

        // Tie
        if (board[0][0] != 0 && board[1][0] != 0 && board[2][0] != 0
                && board[0][1] != 0 && board[1][1] != 0 && board[2][1] != 0
                && board[0][2] != 0 && board[1][2] != 0 && board[2][2] != 0) {

            return 0;

        }

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
