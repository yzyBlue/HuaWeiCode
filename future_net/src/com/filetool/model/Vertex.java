package com.filetool.model;

import java.util.ArrayList;

public class Vertex {
	private boolean visited;// 是否被访问过
	private Object value;// 顶点的值
	private ArrayList<Integer> visitedVertexIdList;

	public Vertex() {
		this.visited = false;
		this.value = "";
		this.visitedVertexIdList = new ArrayList<Integer>();
	}

	public Vertex(String name) {
		this.visited = false;
		this.value = name;
		this.visitedVertexIdList = new ArrayList<Integer>();
	}

	public boolean getVisited() {
		return this.visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public ArrayList<Integer> getVisitedVertexIdList() {
		return visitedVertexIdList;
	}

	public void setVisitedVertexIdList(ArrayList<Integer> visitedVertexIdList) {
		this.visitedVertexIdList = visitedVertexIdList;
	}

	public void resetVisitedIdList() {
		if (this.visitedVertexIdList != null) {
			for (Integer vertexId : visitedVertexIdList) {
				vertexId = 0;
			}
		} else {
			this.visitedVertexIdList = new ArrayList<Integer>();
		}
	}
}
