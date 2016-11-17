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
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {

    private static final int DEFAULT_PORT = 8888;

    private Socket clientSocket;

    private DataInputStream recieve;
    private DataOutputStream send;

    public Client() {
        createSocket();
        initializeIOStreams();
        gameLoop();
    }

    private void createSocket() {
        Scanner scanner = new Scanner(System.in);
        String serverIP;
        String serverPort;
        int port = 0;

        boolean isValidAddress = false;
        do {
            boolean addressChosen = false;
            do {

                System.out.print("Server IP: ");
                serverIP = scanner.nextLine();

                System.out.print("Server Port (8888): ");
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
                clientSocket = new Socket(serverIP, port);
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
        } catch (IOException ex) {
            System.out.println("ERROR: Could not create IO streams!");
            System.exit(1);
        }
    }

    private void gameLoop() {
        System.out.println(recieveMessage());
    }

    private String recieveMessage() {
        String returnMessage = "";

        try {
            returnMessage = recieve.readUTF();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

        return returnMessage;
    }

}
