package com.dijkstra.impl.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Graph {
	/*
	 * ����
	 */
	private List<Vertex> vertexs;
	/*
	 * ��
	 */
	private int[][] edges;
	/*
	 * û�з��ʵĶ���
	 */
	private Queue<Vertex> unVisited;

	public Graph(List<Vertex> vertexs, int[][] edges) {
		this.vertexs = vertexs;
		this.edges = edges;
		initUnVisited();
	}

	/*
	 * �������������·��
	 */
	public void search() {
		while (!unVisited.isEmpty()) {
			Vertex vertex = unVisited.element();
			// �����Ѿ���������·��������Ϊ"�ѷ���"
			vertex.setMarked(true);
			// ��ȡ����"δ����"���ھ�
			List<Vertex> neighbors = getNeighbors(vertex);
			// �����ھӵ����·��
			updatesDistance(vertex, neighbors);
			pop();
		}
		System.out.println("search over");
	}

	/*
	 * ���������ھӵ����·��
	 */
	private void updatesDistance(Vertex vertex, List<Vertex> neighbors) {
		for (Vertex neighbor : neighbors) {
			updateDistance(vertex, neighbor);
		}
	}

	/*
	 * �����ھӵ����·��
	 */
	private void updateDistance(Vertex vertex, Vertex neighbor) {
		int distance = getDistance(vertex, neighbor) + vertex.getPath();
		if (distance < neighbor.getPath()) {
			neighbor.setPath(distance);
		}
	}

	/*
	 * ��ʼ��δ���ʶ��㼯��
	 */
	private void initUnVisited() {
		unVisited = new PriorityQueue<Vertex>();
		for (Vertex v : vertexs) {
			unVisited.add(v);
		}
	}

	/*
	 * ��δ���ʶ��㼯����ɾ�����ҵ����·���Ľڵ�
	 */
	private void pop() {
		unVisited.poll();
	}

	/*
	 * ��ȡ���㵽Ŀ�궥��ľ���
	 */
	private int getDistance(Vertex source, Vertex destination) {
		int sourceIndex = vertexs.indexOf(source);
		int destIndex = vertexs.indexOf(destination);
		return edges[sourceIndex][destIndex];
	}

	/*
	 * ��ȡ��������(δ���ʵ�)�ھ�
	 */
	private List<Vertex> getNeighbors(Vertex v) {
		List<Vertex> neighbors = new ArrayList<Vertex>();
		int position = vertexs.indexOf(v);
		Vertex neighbor = null;
		int distance;
		for (int i = 0; i < vertexs.size(); i++) {
			if (i == position) {
				// ���㱾������
				continue;
			}
			distance = edges[position][i]; // �����ж���ľ���
			if (distance < Integer.MAX_VALUE) {
				// ���ھ�(��·���ɴ�)
				neighbor = getVertex(i);
				if (!neighbor.isMarked()) {
					// ����ھ�û�з��ʹ��������list;
					neighbors.add(neighbor);
				}
			}
		}
		return neighbors;
	}

	/*
	 * ���ݶ���λ�û�ȡ����
	 */
	private Vertex getVertex(int index) {
		return vertexs.get(index);
	}

	/*
	 * ��ӡͼ
	 */
	public void printGraph() {
		int verNums = vertexs.size();
		for (int row = 0; row < verNums; row++) {
			for (int col = 0; col < verNums; col++) {
				if (Integer.MAX_VALUE == edges[row][col]) {
					System.out.print("X");
					System.out.print(" ");
					continue;
				}
				System.out.print(edges[row][col]);
				System.out.print(" ");
			}
			System.out.println();
		}
	}
}
