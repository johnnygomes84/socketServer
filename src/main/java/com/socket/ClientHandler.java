package com.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import com.socket.utils.WeightedGraph;

public class ClientHandler extends Thread {
	private static Socket serverClient;
	private static UUID sessionId;
	private String name = "";
	private String type = "";
	private WeightedGraph graph;

	private final BufferedReader inStream;
	private final PrintWriter outStream;

	public ClientHandler(Socket serverClient, WeightedGraph graph) throws IOException {
		this.serverClient = serverClient;
		this.graph = graph;
		this.inStream = new BufferedReader(new InputStreamReader(serverClient.getInputStream()));
		this.outStream = new PrintWriter(serverClient.getOutputStream(), true);
	}

	@Override
	public void run() {
		LocalDateTime chatTime = LocalDateTime.now();
		int counterGreeting = 0;

		UUID sessionId = UUID.randomUUID();

		String clientMessage = "", serverMessage = "";
		try {
			serverClient.setSoTimeout(30000);

			while (true) {

				System.out.println(clientMessage);

				if (counterGreeting == 0) {
					type = "WELCOME";
					counterGreeting++;

				}

				if (clientMessage.contains("HI, I AM")) {
					type = "GREET";

				}
				if (clientMessage.contains("ADD NODE")) {
					type = "ADD_NODE";

				}
				if (clientMessage.contains("ADD EDGE")) {
					type = "ADD_EDGE";

				}
				if (clientMessage.contains("REMOVE EDGE")) {
					type = "REMOVE_EDGE";

				}
				if (clientMessage.contains("REMOVE NODE")) {
					type = "REMOVE_NODE";

				}
				if (clientMessage.contains("SHORTEST PATH")) {
					type = "SHORTEST_PATH";

				}

				if (clientMessage.equalsIgnoreCase("BYE MATE!")) {

					type = "GOODBYE";

				}

				switch (type) {

				case "WELCOME":

					serverMessage = ("HI, I AM " + sessionId);
					System.out.println(serverMessage);
					this.outStream.println(serverMessage);
					type = "";
					break;

				case "GREET":

					name = clientMessage.substring(9);
					serverMessage = "HI " + name;
					this.outStream.println(serverMessage);
					type = "";
					break;

				case "ADD_NODE":

					serverMessage = graph.addNode(clientMessage);
					this.outStream.println(serverMessage);
					type = "";
					break;

				case "ADD_EDGE":

					serverMessage = graph.addEgde(clientMessage);
					this.outStream.println(serverMessage);
					type = "";
					break;

				case "REMOVE_EDGE":

					serverMessage = graph.removeEdge(clientMessage);
					this.outStream.println(serverMessage);
					type = "";
					break;

				case "REMOVE_NODE":

					serverMessage = graph.removeNode(clientMessage);
					this.outStream.println(serverMessage);
					type = "";
					break;

				case "SHORTEST_PATH":

					serverMessage = graph.getShortestPath(clientMessage);
					this.outStream.println(serverMessage);
					type = "";
					break;

				case "GOODBYE":

					LocalDateTime chatTimeNow = LocalDateTime.now();

					Long timeTalking = chatTime.until(chatTimeNow, ChronoUnit.MILLIS);
					serverMessage = "BYE " + name + ", WE SPOKE FOR " + timeTalking + " MS";
					this.outStream.println(serverMessage);
					System.out.println("CONNECTION FOR " + sessionId + " IS CLOSED");
					inStream.close();
					this.outStream.close();
					serverClient.close();
					break;

				default:
					serverMessage = "SORRY, I DID NOT UNDERSTAND THAT";
					this.outStream.println(serverMessage);
					type = "";
					break;
				}

				clientMessage = inStream.readLine();

			}

		} catch (SocketTimeoutException ex) {

			try {
				LocalDateTime chatTimeNow = LocalDateTime.now();
				Long timeTalking = chatTime.until(chatTimeNow, ChronoUnit.MILLIS);
				String timeOut = "BYE " + name + ", WE SPOKE FOR " + timeTalking + " MS";
				System.out.println(timeOut);
				System.out.println("CONNECTION FOR " + sessionId + " IS CLOSED");
				serverClient.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception ex2) {
			// System.out.println(ex2);
		} finally {
			System.out.println("Client -" + sessionId + " exit!! ");
		}
	}

}
