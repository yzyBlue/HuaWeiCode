package com.filetool.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Graph {
	/*
	 * 顶点
	 */
	private List<Vertex> vertexs;
	/*
	 * 边
	 */
	private int[][] edges;
	/*
	 * 没有访问的顶点
	 */
	private Queue<Vertex> unVisited;
	/*
	 * 边的数量
	 */
	private int edgeNum;

	public Graph(int vertexNum) {
		this.vertexs = new ArrayList<Vertex>();
		this.edges = new int[vertexNum][vertexNum];
		for (int i = 0; i < vertexNum; ++i) {
			for (int j = 0; j < vertexNum; ++j) {
				this.edges[i][j] = 0;
			}
		}
		this.edgeNum = 0;
		this.unVisited=new PriorityQueue<Vertex>();
	}

	public Graph(List<Vertex> vertexs, int[][] edges, int edgeNum) {
		this.vertexs = vertexs;
		this.edges = edges;
		this.edgeNum = edgeNum;
		initUnVisited();
	}

	// 初始化建立图像,并且对边数组中边在邻接矩阵中的端点赋值
	public void init(ArrayList<Integer> arrayVertexId, ArrayList<Edge> arrayEdge,Integer serialSourceId) {
		// 插入顶点
		for (Integer n : arrayVertexId) {
			Vertex vertex = new Vertex(Integer.toString(n));
			vertex.setName(Integer.toString(n));// 真实ID
			//初始化时，起点距离为0，其他节点距离为无穷大
			if(n==serialSourceId){
				System.out.println("[Start Vertex]:"+vertex.getName());
				vertex.setPath(0);
			}
			this.addVertex(vertex);
		}
		// 插入边
		for (Edge edge : arrayEdge) {
			edge.setSourceIdInMatrix(arrayVertexId.lastIndexOf(edge
					.getSourceIdInTopo()));
			edge.setDestIdInMatrix(arrayVertexId.lastIndexOf(edge
					.getDestIdInTopo()));
			this.addEdge(edge.getSourceIdInMatrix(), edge.getDestIdInMatrix(),
					edge.getWeight());
		}

		initUnVisited();

	}

	/*
	 * 添加邻接矩阵边信息
	 */
	public boolean addEdge(int nV1, int nV2, int nWeight) {
		if ((nWeight > 0) && (nV1 < this.edges.length)
				&& (nV2 < this.edges.length)) {
			this.edges[nV1][nV2] = nWeight;
			this.edgeNum++;
			return true;
		}
		return false;
	}

	/*
	 * 添加顶点信息
	 */
	public boolean addVertex(Vertex v) {
		if (v != null) {
			this.vertexs.add(v);
			return true;
		}
		return false;
	}

	/*
	 * 搜索各顶点最短路径
	 */
	public void search() {
		while (!unVisited.isEmpty()) {
			Vertex vertex = unVisited.element();
			System.out.println("[search] ----  "+vertex.getName());
			// 顶点已经计算出最短路径，设置为"已访问"
			vertex.setMarked(true);
			// 获取所有"未访问"的邻居
			List<Vertex> neighbors = getNeighbors(vertex);
			// 更新邻居的最短路径
			List<Vertex> neighborsUpdate=updatesDistance(vertex, neighbors);
			//Collections.sort(neighborsUpdate);
			//System.out.println(" --> "+neighborsUpdate==null?neighborsUpdate.get(0).getName():"null");
			pop();
		}
		System.out.println("search over");
	}

	/*
	 * 更新所有邻居的最短路径
	 */
	private List<Vertex> updatesDistance(Vertex vertex, List<Vertex> neighbors) {
		for (Vertex neighbor : neighbors) {
			updateDistance(vertex, neighbor);
		}
		return neighbors;
	}

	/*
	 * 更新邻居的最短路径
	 */
	private void updateDistance(Vertex vertex, Vertex neighbor) {
		int distance = getDistance(vertex, neighbor) + vertex.getPath();
		if (distance < neighbor.getPath()) {
			neighbor.setPath(distance);
		}
	}

	/*
	 * 初始化未访问顶点集合
	 */
	private void initUnVisited() {
		if(unVisited == null){
			unVisited = new PriorityQueue<Vertex>();
		}
		for (Vertex v : vertexs) {
			unVisited.add(v);
		}
	}

	/*
	 * 从未访问顶点集合中删除已找到最短路径的节点
	 */
	private void pop() {
		unVisited.poll();
	}

	/*
	 * 获取顶点到目标顶点的距离
	 */
	private int getDistance(Vertex source, Vertex destination) {
		int sourceIndex = vertexs.indexOf(source);
		int destIndex = vertexs.indexOf(destination);
		return edges[sourceIndex][destIndex];
	}

	/*
	 * 获取顶点所有(未访问的)邻居
	 */
	private List<Vertex> getNeighbors(Vertex v) {
		List<Vertex> neighbors = new ArrayList<Vertex>();
		int position = vertexs.indexOf(v);
		Vertex neighbor = null;
		int distance;
		for (int i = 0; i < vertexs.size(); i++) {
			if (i == position) {
				// 顶点本身，跳过
				continue;
			}
			distance = edges[position][i]; // 到所有顶点的距离
			if (distance < Integer.MAX_VALUE) {
				// 是邻居(有路径可达)
				neighbor = getVertex(i);
				if (!neighbor.isMarked()) {
					// 如果邻居没有访问过，则加入list;
					neighbors.add(neighbor);
				}
			}
		}
		return neighbors;
	}

	/*
	 * 根据顶点位置获取顶点
	 */
	private Vertex getVertex(int index) {
		return vertexs.get(index);
	}

	public List<Vertex> getVertexList(){
		return this.vertexs;
	}
	/*
	 * 打印图
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
