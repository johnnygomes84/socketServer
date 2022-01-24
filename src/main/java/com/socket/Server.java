package com.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.socket.utils.WeightedGraph;

public class Server {

	static final int PORT = 12345;

	public static void main(String[] args) throws IOException {

		ServerSocket ss = new ServerSocket(PORT);

		while (true) {

			Socket server = ss.accept();
			try {

				WeightedGraph graph = new WeightedGraph();

				// create a new thread object
				Thread t = new ClientHandler(server, graph);

				// Invoking the start() method
				t.start();

			} catch (Exception e) {
				server.close();
				e.printStackTrace();
			}
		}
	}
}
