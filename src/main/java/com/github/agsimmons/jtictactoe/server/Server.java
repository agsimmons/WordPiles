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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    private final int DEFAULT_PORT = 8886;

    private int port;

    private ServerSocket serverSocket;
    private Socket playerOne;
    private Socket playerTwo;

    private DataInputStream p1recieve;
    private DataOutputStream p1send;
    private DataInputStream p2recieve;
    private DataOutputStream p2send;

    private GameBoard gameBoard;

    public Server() {
        chooseServerPort();
        createServerSocket();
        connectClients();
        initializeIOStreams();
        gameLoop();
    }

    private void chooseServerPort() {
        Scanner scanner = new Scanner(System.in);
        String input;

        boolean validPort = false;
        do {

            boolean portChosen = false;
            do {

                System.out.print("Server Port (" + DEFAULT_PORT + "): ");
                input = scanner.nextLine();
                if (input.equals("")) {
                    port = DEFAULT_PORT;
                    portChosen = true;
                } else {
                    try {
                        port = Integer.parseInt(input);
                        portChosen = true;
                    } catch (NumberFormatException e) {
                        System.out.println("ERROR: Invalid port!");
                    }
                }

            } while (!portChosen);

            // Check if port is already in use
            try {
                (new ServerSocket(port)).close();
                validPort = true;
            } catch (IOException ex) {
                System.out.println("ERROR: Could not bind to port! It could be in use or reserved. Please choose a different port");
            }

        } while (!validPort);
    }

    private void createServerSocket() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            System.out.println("ERROR: Could not create server socket!");
            System.out.println("This error is not handled yet. Shutting Down!");
            System.exit(1);
        }
    }

    private void connectClients() {
        boolean playerOneConnected = false;
        do {
            try {
                System.out.println("Waiting for playerOne to connect...");
                playerOne = serverSocket.accept();
                System.out.println("playerOne connected!");
                playerOneConnected = true;
            } catch (IOException ex) {
                System.out.println("ERROR: Could not connect to playerOne");
            }
        } while (!playerOneConnected);

        boolean playerTwoConnected = false;
        do {
            try {
                System.out.println("Waiting for playerTwo to connect...");
                playerTwo = serverSocket.accept();
                System.out.println("playerTwo connected!");
                playerTwoConnected = true;
            } catch (IOException ex) {
                System.out.println("ERROR: Could not connect to playerTwo");
            }
        } while (!playerTwoConnected);
    }

    private void initializeIOStreams() {
        try {
            p1recieve = new DataInputStream(playerOne.getInputStream());
            p1send = new DataOutputStream(playerOne.getOutputStream());

            p2recieve = new DataInputStream(playerTwo.getInputStream());
            p2send = new DataOutputStream(playerTwo.getOutputStream());
        } catch (IOException ex) {
            System.out.println("ERROR: Could not create IO streams!");
            System.exit(1);
        }
    }

    private void gameLoop() {
        initialize();

        while (true) {
            playerOneMove();
            displayBoardState();
            if (gameBoard.checkForWinner() == 1) {
                playerOneWins();
                break;
            } else if (gameBoard.checkForWinner() == 0) {
                tie();
                break;
            }

            playerTwoMove();
            displayBoardState();
            if (gameBoard.checkForWinner() == 2) {
                playerTwoWins();
                break;
            } else if (gameBoard.checkForWinner() == 0) {
                tie();
                break;
            }
        }

        disconnectPlayers();

        System.out.println("Game Over. Thanks for playing!");
        System.exit(0);
    }

    private void initialize() {
        createBoard();
        sendWelcomeMessage();
        askNames();
    }

    private void createBoard() {
        gameBoard = new GameBoard();
    }

    private void sendWelcomeMessage() {
        try {
            p1send.writeUTF("Welcome to jTic-Tac-Toe! You are Player 1");
            p2send.writeUTF("Welcome to jTic-Tac-Toe! You are Player 2");
        } catch (IOException ex) {
            System.out.println("ERROR: Could not send welcome message!");
            System.exit(1);
        }
    }

    private void askNames() {
        try {
            p1send.writeUTF("What is your name?");
            p1send.writeUTF("RESPOND");
            String playerOneName = p1recieve.readUTF();

            p2send.writeUTF("What is your name?");
            p2send.writeUTF("RESPOND");
            String playerTwoName = p2recieve.readUTF();

            p1send.writeUTF("Your opponent is " + playerTwoName);
            p2send.writeUTF("Your opponent is " + playerOneName);
        } catch (IOException ex) {
            System.out.println("ERROR: Failed to ask names!");
            System.exit(1);
        }
    }

    private void playerOneMove() {
        try {
            p1send.writeUTF("Make a move (x y): ");
            p1send.writeUTF("RESPOND");
            String response = p1recieve.readUTF();
            if (!isValidMove(response)) {
                p1send.writeUTF("Invalid Move! Try Again!");
                playerOneMove(); // This isn't designed well. Possible stack overflow
                throw new IllegalArgumentException(); // I'm so sorry
            }
            String[] responseArray = response.split(" ");
            gameBoard.makeMove(1, Integer.parseInt(responseArray[0]), Integer.parseInt(responseArray[1]));
        } catch (IOException ex) {
            System.out.println("ERROR: Could not process player one's move!");
            System.exit(1);
        } catch (IllegalArgumentException ex) {
            // Sorry...
        }
    }

    private void playerTwoMove() {
        try {
            p2send.writeUTF("Make a move (x y): ");
            p2send.writeUTF("RESPOND");
            String response = p2recieve.readUTF();
            if (!isValidMove(response)) {
                p2send.writeUTF("Invalid Move! Try Again!");
                playerTwoMove(); // This isn't designed well. Possible stack overflow
                throw new IllegalArgumentException(); // I'm so sorry
            }
            String[] responseArray = response.split(" ");
            gameBoard.makeMove(2, Integer.parseInt(responseArray[0]), Integer.parseInt(responseArray[1]));
        } catch (IOException ex) {
            System.out.println("ERROR: Could not process player two's move!");
            System.exit(1);
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid move. Asking again...");
        }
    }

    private boolean isValidMove(String response) {
        String responses[] = response.split(" ");

        if (responses.length != 2) {
            return false;
        }

        int firstNum;
        int secondNum;
        try {
            firstNum = Integer.parseInt(responses[0]);
            secondNum = Integer.parseInt(responses[1]);
        } catch (NumberFormatException ex) {
            return false;
        }

        if (firstNum > 2 || firstNum < 0) {
            return false;
        } else if (secondNum > 2 || secondNum < 0) {
            return false;
        }

        if (gameBoard.getCellState(firstNum, secondNum) != 0) {
            return false;
        }

        return true;
    }

    private void displayBoardState() {
        String boardState = "\n" + gameBoard.drawBoardState() + "\n";
        try {
            p1send.writeUTF(boardState);
            p2send.writeUTF(boardState);
        } catch (IOException ex) {
            System.out.println("ERROR: Could not send board state!");
            System.exit(1);
        }
    }

    private void playerOneWins() {
        try {
            p1send.writeUTF("Player One Wins!");
            p2send.writeUTF("Player One Wins!");
        } catch (IOException ex) {
            System.out.println("ERROR: playerOneWins could not be executed!");
            System.exit(1);
        }
    }

    private void playerTwoWins() {
        try {
            p1send.writeUTF("Player Two Wins!");
            p2send.writeUTF("Player Two Wins!");
        } catch (IOException ex) {
            System.out.println("ERROR: playerOneWins could not be executed!");
            System.exit(1);
        }
    }

    private void tie() {
        try {
            p1send.writeUTF("It's a tie!");
            p2send.writeUTF("It's a tie!");
        } catch (IOException ex) {
            System.out.println("ERROR: tie could not be executed!");
            System.exit(1);
        }
    }

    private void disconnectPlayers() {
        try {
            p1send.writeUTF("Goodbye!");
            p1send.writeUTF("END");
            p1send.close();
            p1recieve.close();
            playerOne.close();

            p2send.writeUTF("Goodbye!");
            p2send.writeUTF("END");
            p2send.close();
            p2recieve.close();
            playerTwo.close();
        } catch (IOException ex) {
            System.out.println("ERROR: Could not disconnect players!");
            System.exit(1);
        }
    }

}
