package com.socket.utils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class WeightedGraph {

	private static final String NODE_ERROR_MSG = "ERROR: NODE NOT FOUND";
	static LinkedList<Edge> adjacencylist = new LinkedList();;
	static HashSet<String> node = new HashSet<String>();;

	static class Edge {
		String source;
		String destination;
		int weight;

		public Edge(String source, String destination, int weight) {
			this.source = source;
			this.destination = destination;
			this.weight = weight;
		}

		public int getWeight() {
			return weight;
		}

	}

	public static String addEgde(String addEdge) {

		try {

			String[] paramns = addEdge.substring(9).split(" ");
			String source = paramns[0];
			String destination = paramns[1];
			int weight = Integer.valueOf(paramns[2]);

			if (node.contains(source) && node.contains(destination)) {
				System.out.println("ADICIONA EDGE");
				Edge edge = new Edge(source, destination, weight);
				adjacencylist.addFirst(edge); // for directed graph
				return "EDGE ADDED";
			}

		} catch (IndexOutOfBoundsException e) {
			// TODO: handle exception
		}

		return NODE_ERROR_MSG;
	}

	public static String removeEdge(String removeEdge) {

		String[] paramns = removeEdge.substring(12).split(" ");
		String source = paramns[0];
		String destination = paramns[1];

		Edge edgeRemove = adjacencylist.stream()
				.filter(l -> l.source.equalsIgnoreCase(source) && l.destination.equalsIgnoreCase(destination))
				.findFirst().orElse(null);
		if (edgeRemove != null) {
			adjacencylist.removeFirstOccurrence(edgeRemove);
			return "EDGE REMOVED";
		}

		return NODE_ERROR_MSG;
	}

	public static String addNode(String nodeAdd) {

		String nodeToAdd = nodeAdd.substring(9);

		if (!node.contains(nodeToAdd)) {
			node.add(nodeToAdd);
			System.out.println("node adicionado");
			return "NODE ADDED";
		}

		System.out.println("node existe");
		return "ERROR: NODE ALREADY EXISTS";

	}

	public static String removeNode(String remNode) {

		String nodeToRemove = remNode.substring(12);

		if (node.contains(nodeToRemove)) {

			adjacencylist.removeIf(l -> l.source.equals(nodeToRemove));
			node.removeIf(n -> n.equals(nodeToRemove));
			System.out.println("node removido");
			return "NODE REMOVED";
		}

		return NODE_ERROR_MSG;
	}

	public static String getShortestPath(String pathToFind) {

		// Não passa

		try {

			String[] paramns = pathToFind.substring(14).split(" ");
			String source = paramns[0];
			String destination = paramns[1];

			if (node.contains(source) && node.contains(destination)) {

				Edge edgeRemove = adjacencylist.stream()
						.filter(l -> l.source.equalsIgnoreCase(source) && l.destination.equalsIgnoreCase(destination))
						.findFirst().orElseThrow(NoSuchElementException::new);

				List<String> destinationList = adjacencylist.stream().filter(dl -> dl.source.equalsIgnoreCase(source))
						.map(md -> md.destination).distinct().collect(Collectors.toList());

				return String.valueOf(edgeRemove.weight);
			} else {
				return NODE_ERROR_MSG;
			}

		} catch (NoSuchElementException e) {
			int lowestDistanceError = Integer.MAX_VALUE;

			return String.valueOf(lowestDistanceError);
		}
	}

}
