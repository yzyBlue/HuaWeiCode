package com.filetool.model;

public class Edge {
	private String serialNo;// 在拓扑结构中边的标号
	private int sourceIdInMatrix;// 在邻接矩阵中起点的编号
	private int sourceIdInTopo;// 拓扑结构中起点的编号
	private int destIdInMatrix;// 在邻接矩阵中尾端顶点的编号
	private int destIdInTopo;// 在拓扑结构中尾端顶点的编号
	private int weight;// 权值

	public Edge() {
		this.reset();
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public int getSourceIdInMatrix() {
		return sourceIdInMatrix;
	}

	public void setSourceIdInMatrix(int sourceIdInMatrix) {
		this.sourceIdInMatrix = sourceIdInMatrix;
	}

	public int getSourceIdInTopo() {
		return sourceIdInTopo;
	}

	public void setSourceIdInTopo(int sourceIdInTopo) {
		this.sourceIdInTopo = sourceIdInTopo;
	}

	public int getDestIdInMatrix() {
		return destIdInMatrix;
	}

	public void setDestIdInMatrix(int destIdInMatrix) {
		this.destIdInMatrix = destIdInMatrix;
	}

	public int getDestIdInTopo() {
		return destIdInTopo;
	}

	public void setDestIdInTopo(int destIdInTopo) {
		this.destIdInTopo = destIdInTopo;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public void reset() {
		serialNo = "-1";
		sourceIdInMatrix = -1;
		sourceIdInTopo = -1;
		destIdInMatrix = -1;
		destIdInTopo = -1;
		weight = -1;
	}
}
