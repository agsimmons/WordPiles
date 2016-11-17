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

    private final int DEFAULT_PORT = 8888;

    private int port;

    private ServerSocket serverSocket;
    private Socket playerOne;
    private Socket playerTwo;

    private static DataInputStream p1recieve;
    private static DataOutputStream p1send;
    private static DataInputStream p2recieve;
    private static DataOutputStream p2send;

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
        sendWelcomeMessage();

        while (true) {

        }
    }

    // Currently does not work
    private void sendWelcomeMessage() {
        try {
            p1send.writeUTF("Welcome to WordPiles! You are Player 1");
            p2send.writeUTF("Welcome to WordPiles! You are Player 2");
        } catch (IOException ex) {
            System.out.println("ERROR: Could not send welcome message!");
            System.exit(1);
        }
    }

}
