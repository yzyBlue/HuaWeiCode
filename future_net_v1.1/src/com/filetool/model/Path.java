package com.filetool.model;

import java.util.ArrayList;
import java.util.HashSet;


public class Path {
	private ArrayList<Integer> listSerialVertexId;// 该路径上的顶点
	private int pathDistance;// 该路径的总长度

	public Path() {
		listSerialVertexId = new ArrayList<Integer>();
		pathDistance = Integer.MAX_VALUE;// 路径长初始认为无穷远
	}

	public ArrayList<Integer> getListSerialVertexId() {
		return listSerialVertexId;
	}

	public void setListSerialVertexId(ArrayList<Integer> listSerialVertexId) {
		this.listSerialVertexId = listSerialVertexId;
	}

	public int getPathDistance() {
		return pathDistance;
	}

	public void setPathDistance(int pathDistance) {
		this.pathDistance = pathDistance;
	}

	// 获得该路径在拓扑结构中表示
	public String getPathInTopo(ArrayList<Edge> edgeList) {
		if (edgeList != null && edgeList.size() > 0) {
			ArrayList<String> edgeIdList = new ArrayList<String>();
			for (int i = 0; i < this.listSerialVertexId.size() - 1; i++) {
				for (Edge edge : edgeList) {
					if (listSerialVertexId.get(i) == edge.getSourceIdInMatrix()
							&& listSerialVertexId.get(i + 1) == edge
									.getDestIdInMatrix()) {
						edgeIdList.add(edge.getStrSerialNo());
						break;
					}
				}
			}
			String strPathInTopo = "";
			for (String strEdgeId : edgeIdList) {
				strPathInTopo += strEdgeId;
				if (strEdgeId != edgeIdList.get(edgeIdList.size() - 1)) {
					strPathInTopo += "|";
				}
			}
			return strPathInTopo;
		} else {
			return "";
		}

	}

	// 路径是否过定点
	public boolean containFixedVertexList(ArrayList<Integer> fixedVertexList) {
		if (fixedVertexList != null) {
			ArrayList<Integer> queue = new ArrayList<Integer>();
			for (Integer n : this.listSerialVertexId) {
				if (fixedVertexList.contains(n)) {
					queue.add(n);
				}
			}
			if (queue.size() == fixedVertexList.size()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	// 判断路径是否走了回头路
	public boolean HasBackRoad() {
		HashSet<Integer> setVertexId = new HashSet<Integer>();
		for (Integer nVertexId : this.listSerialVertexId) {
			setVertexId.add(nVertexId);
		}
		if (setVertexId.size() >= this.listSerialVertexId.size()) {
			return false;
		} else {
			return true;
		}
	}

}
