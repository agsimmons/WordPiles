/*
 * The MIT License
 *
 * Copyright 2016 andrew.
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
package com.github.agsimmons.wordpiles.client;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private final int DEFAULT_PORT = 8888;

    private Socket clientSocket;

    public Client() {
        createSocket();
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

}
