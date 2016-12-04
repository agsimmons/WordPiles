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
package com.github.agsimmons.jtictactoe.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 8886;

    private Socket clientSocket;

    private DataInputStream recieve;
    private DataOutputStream send;
    private Scanner input;

    public Client() {
        createSocket();
        initializeIOStreams();
        gameLoop();
    }

    private void createSocket() {
        Scanner scanner = new Scanner(System.in);
        String serverHost;
        String serverPort;
        int port = 0;

        boolean isValidAddress = false;
        do {
            boolean addressChosen = false;
            do {

                System.out.print("Server IP (" + DEFAULT_HOST + "): ");
                serverHost = scanner.nextLine();
                if (serverHost.equals("")) {
                    serverHost = DEFAULT_HOST;
                }

                System.out.print("Server Port (" + DEFAULT_PORT + "): ");
                serverPort = scanner.nextLine();
                if (serverPort.equals("")) {
                    port = DEFAULT_PORT;
                    addressChosen = true;
                } else {
                    try {
                        port = Integer.parseInt(serverPort);
                        addressChosen = true;
                    } catch (NumberFormatException e) {
                        System.out.println("ERROR: Invalid port!");
                    }
                }

            } while (!addressChosen);

            try {
                clientSocket = new Socket(serverHost, port);
                isValidAddress = true;
            } catch (IOException ex) {
                System.out.println("ERROR: Could not create socket to server!");
            }

        } while (!isValidAddress);
    }

    private void initializeIOStreams() {
        try {
            recieve = new DataInputStream(clientSocket.getInputStream());
            send = new DataOutputStream(clientSocket.getOutputStream());
            input = new Scanner(System.in);
        } catch (IOException ex) {
            System.out.println("ERROR: Could not create IO streams!");
            System.exit(1);
        }
    }

    private void gameLoop() {
        boolean gameInProgress = true;
        while (gameInProgress) {
            gameInProgress = recieveMessage();
        }
    }

    private boolean recieveMessage() {
        try {
            String returnMessage = recieve.readUTF();

            switch (returnMessage) {
                case "RESPOND":
                    respond();
                    break;
                case "END":
                    return false;
                default:
                    System.out.println(returnMessage);
                    break;
            }

        } catch (IOException ex) {
            System.out.println("ERROR: Could not recieve message!");
            System.exit(1);
        }

        return true;
    }

    private void respond() {
        try {
            System.out.print("Response: ");
            send.writeUTF(input.nextLine());
        } catch (IOException ex) {
            System.out.println("ERROR: Could not send response!");
            System.exit(1);
        }
    }

}
