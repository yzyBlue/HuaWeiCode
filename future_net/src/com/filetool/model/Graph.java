package com.filetool.model;

import java.util.ArrayList;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Stack;

import com.filetool.util.LogUtil;

public class Graph {
	private ArrayList<Vertex> vertexList;
	private int[][] edgeMatrix;// 邻接矩阵
	private int edgeNum;// 边的数量

	public Graph(int nVNum) {
		this.vertexList = new ArrayList<Vertex>();
		this.edgeMatrix = new int[nVNum][nVNum];
		for (int i = 0; i < nVNum; ++i) {
			for (int j = 0; j < nVNum; ++j) {
				this.edgeMatrix[i][j] = 0;
			}
		}
		this.edgeNum = 0;

	}

	// 初始化建立图像,并且对边数组中边在邻接矩阵中的端点赋值
	public void init(ArrayList<Integer> arrayVertexId, ArrayList<Edge> arrayEdge) {
		// 插入顶点
		for (Integer n : arrayVertexId) {
			Vertex vertex = new Vertex();
			vertex.setValue(n);// 真实ID
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

	}

	public ArrayList<Vertex> GetVertexList() {
		return this.vertexList;
	}

	public int[][] GetEdgeMatrix() {
		return this.edgeMatrix;
	}

	// 获得某点的顶点值
	public Object GetVertexValueByIndex(int nIndex) {
		if (nIndex < this.vertexList.size()) {
			return ((Vertex) this.vertexList.get(nIndex)).getValue();
		}

		return null;
	}

	// 获得两点之间的权重值
	public int GetWeight(int nV1, int nV2) {
		if ((nV1 < this.edgeMatrix.length) && (nV2 < this.edgeMatrix.length)) {
			return this.edgeMatrix[nV1][nV2];
		}

		return -1;
	}

	public boolean addVertex(Vertex v) {
		if (v != null) {
			this.vertexList.add(v);
			return true;
		}

		return false;
	}

	public boolean addEdge(int nV1, int nV2, int nWeight) {
		if ((nWeight > 0) && (nV1 < this.edgeMatrix.length)
				&& (nV2 < this.edgeMatrix.length)) {
			this.edgeMatrix[nV1][nV2] = nWeight;
			this.edgeNum++;
			return true;
		}

		return false;
	}

	public boolean removeEdge(int nV1, int nV2) {
		if ((nV1 < this.edgeMatrix.length) && (nV2 < this.edgeMatrix.length)) {
			this.edgeMatrix[nV1][nV2] = 0;
			this.edgeNum--;
			return true;
		}

		return false;
	}

	// 找出source和target之间所有的路径
	public boolean findAllPath(int source, int target, ArrayList<Path> pathList) {
		// LogUtil.printLog("[findAllPath start]");
		if (source < 0 || target < 0) {
			// LogUtil.printLog("[findAllPath end]");
			return false;
		}

		if (pathList == null) {
			pathList = new ArrayList<Path>();
		} else {
			pathList.clear();
		}
		if (!(isConnected(source, target))) {
			System.out.println("起始点和终点之间不连通");
			// LogUtil.printLog("[findAllPath end]");
			return false;
		}

		// 初始化
		for (int i = 0; i < vertexList.size(); i++) {
			ArrayList<Integer> tempArray = new ArrayList<Integer>();
			for (int j = 0; j < vertexList.size(); j++) {
				tempArray.add(0);
			}
			vertexList.get(i).setVisitedVertexIdList(tempArray);
		}

		findPath(source, target, pathList);
		// LogUtil.printLog("[findAllPath end]");
		return true;
	}

	private void findPath(int source, int target, ArrayList<Path> pathList) {
		LogUtil.printLog("[findPath start]");
		Stack<Integer> stackPath = new Stack<Integer>();
		(this.vertexList.get(source)).setVisited(true);
		stackPath.push(source);

		while (!(stackPath.isEmpty())) {
			int nAdj = getAdjacentUnVisitedVertex(
					(stackPath.peek()).intValue(), stackPath);
			if (nAdj < 0) {
				ArrayList<Integer> tempArray = new ArrayList<Integer>();
				for (int i = 0; i < this.vertexList.size(); ++i) {
					tempArray.add(0);
				}
				(this.vertexList.get(stackPath.peek()))
						.setVisitedVertexIdList(tempArray);
				stackPath.pop();
			} else {
				stackPath.push(nAdj);

				if ((stackPath.isEmpty())
						|| (target != (stackPath.peek()).intValue()))
					continue;
				(this.vertexList.get(target)).setVisited(false);
				// 找出所有路径
				// this.PrintStack(stackPath);
				Path path = new Path();
				path.setListSerialVertexId(convertStackToList(stackPath));
				pathList.add(path);
				stackPath.pop();
			}

		}
		LogUtil.printLog("[findPath end]");
	}

	public boolean isConnected(int source, int target) {
		LogUtil.printLog("[isConnected start]");
		if (source < 0 || target < 0) {
			LogUtil.printLog("[isConnected end]");
			return false;
		}

		ArrayList<Integer> queue = new ArrayList<Integer>();
		ArrayList<Integer> visited = new ArrayList<Integer>();
		queue.add(Integer.valueOf(source));
		while (!(queue.isEmpty())) {
			for (int i = 0; i < this.vertexList.size(); ++i) {
				if ((this.edgeMatrix[source][i] <= 0)
						|| (visited.contains(Integer.valueOf(i))))
					continue;
				queue.add(i);
			}

			if (queue.contains(Integer.valueOf(target))) {
				LogUtil.printLog("[isConnected end]");
				return true;
			}

			visited.add((Integer) queue.get(0));
			queue.remove(0);
			if (queue.isEmpty())
				continue;
			source = ((Integer) queue.get(0)).intValue();
		}
		LogUtil.printLog("[isConnected end]");
		return false;
	}

	private int getAdjacentUnVisitedVertex(int nParent, Stack<Integer> stack) {
		// LogUtil.printLog("[getAdjacentUnVisitedVertex start]");
		if (nParent < 0 || stack == null) {
			// LogUtil.printLog("[getAdjacentUnVisitedVertex end]");
			return -1;
		}

		for (int i = 0; i < this.vertexList.size(); i++) {
			if (edgeMatrix[nParent][i] > 0
					&& this.vertexList.get(nParent).getVisitedVertexIdList()
							.get(i) == 0 && !stack.contains(i)) {
				this.vertexList.get(nParent).getVisitedVertexIdList().set(i, 1);
				// LogUtil.printLog("[getAdjacentUnVisitedVertex end]");
				return i;
			}

		}
		// LogUtil.printLog("[getAdjacentUnVisitedVertex end]");
		return -1;
	}

	private void printStack(Stack<Integer> stack) {
		System.out.print("找到一条路径:");
		for (Integer n : stack) {
			System.out.print(n);
			if (n != stack.peek()) {
				System.out.print("-->");
			} else {
				System.out.println(";");
			}
		}
	}

	private ArrayList<Integer> convertStackToList(Stack<Integer> stack) {
		ArrayList<Integer> tempArray = new ArrayList<Integer>();
		for (Integer n : stack) {
			tempArray.add(n);
		}
		return tempArray;
	}

	public void displayPath(ArrayList<Path> pathList) {

		if (pathList.size() <= 0) {
			System.out.println("没有路径");
		}
		for (Path path : pathList) {
			System.out.print("输出一条路径:");
			for (Integer n : path.getListSerialVertexId()) {
				System.out.print(vertexList.get(n).getValue());
				if (n != path.getListSerialVertexId().get(
						path.getListSerialVertexId().size() - 1)) {
					System.out.print("-->");
				} else {
					System.out.println(";");
				}
			}
		}
	}

	// 计算路径的长度
	public int getPathDistance(Path path) {
		if (path != null) {
			int nDistance = 0;
			for (int i = 0; i < path.getListSerialVertexId().size() - 1; i++) {
				nDistance += edgeMatrix[path.getListSerialVertexId().get(i)][path
						.getListSerialVertexId().get(i + 1)];
			}
			return nDistance;
		} else {
			return -1;
		}
	}

}
