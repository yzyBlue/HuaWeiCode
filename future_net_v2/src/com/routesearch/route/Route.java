package com.routesearch.route;

import java.util.ArrayList;
import java.util.Collections;

import com.filetool.model.Edge;
import com.filetool.model.Graph;

public class Route {
	/**
	 * 你需要完成功能的入口
	 * 
	 * @author Yuan.Ziyang
	 * @since 2016-4-3
	 * @version V1
	 */
	public static String searchRoute(String graphContent, String condition) {
		final String NO_ANSWER = "NA";
		ArrayList<Integer> arrayVertexId = new ArrayList<Integer>();
		ArrayList<Edge> arrayEdge = new ArrayList<Edge>();
		// 解析图像信息
		if (!resolveGraphContent(graphContent, arrayVertexId, arrayEdge)) {
			return NO_ANSWER;
		} // 进行条件判断，返回NA？

		int vertexNum = arrayVertexId.size();// 图中顶点的数量

		// 解析题目条件
		String[] arrayCondition = condition.split(",");
		if (arrayCondition.length < 3) {
			return NO_ANSWER;
		}
		int topoSourceId = Integer.parseInt(arrayCondition[0]);// 在拓扑结构中的路径起点
		int topoDestId = Integer.parseInt(arrayCondition[1]);// 在拓扑结构中的路径终点
		int serialSourceId = arrayVertexId.lastIndexOf(topoSourceId);// 在邻接矩阵中起点的标号
		int serialDestId = arrayVertexId.lastIndexOf(topoDestId);// 在邻接矩阵中终点的标号

		// 初始化图
		Graph graph = new Graph(vertexNum);
		graph.init(arrayVertexId, arrayEdge,serialSourceId);
		
		
		ArrayList<Integer> fixedVertexList = new ArrayList<Integer>();// 必须经过的顶点列表
		String fixedPointList = arrayCondition[2].replace("\n", "");// 过定点的列表,去除换行符
		String arrayFixedPoint[] = fixedPointList.split("\\|");
		for (String strFixedPoint : arrayFixedPoint) {
			int fixedPoint = Integer.parseInt(strFixedPoint);
			fixedVertexList.add(fixedPoint);
		}

		return "";
	}

	// 解析图像信息
	private static boolean resolveGraphContent(String graphContent,
			ArrayList<Integer> arrayVertexId, ArrayList<Edge> arrayEdge) {
		if (arrayVertexId == null) {
			arrayVertexId = new ArrayList<Integer>();
		} else {
			arrayVertexId.clear();
		}
		if (arrayEdge == null) {
			arrayEdge = new ArrayList<Edge>();
		} else {
			arrayEdge.clear();
		}

		String[] groupEdgeInfo = graphContent.split("\n");

		for (String strEdgeInfo : groupEdgeInfo) {
			String[] unit = strEdgeInfo.split(",");
			if (unit.length < 4) {
				return false;
			}
			Integer source = Integer.parseInt(unit[1]);
			Integer dest = Integer.parseInt(unit[2]);
			if (!arrayVertexId.contains(source)) {
				arrayVertexId.add(source);
			}
			if (!arrayVertexId.contains(dest)) {
				arrayVertexId.add(dest);
			}

			Edge tempEdge = new Edge();
			tempEdge.setSerialNo(unit[0]);
			tempEdge.setSourceIdInTopo(Integer.parseInt(unit[1]));
			tempEdge.setDestIdInTopo(Integer.parseInt(unit[2]));
			tempEdge.setWeight(Integer.parseInt(unit[3]));
			if (!CheckEdgeInfoData(tempEdge)) {
				return false;
			}
			arrayEdge.add(tempEdge);

		}
		Collections.sort(arrayVertexId);// 升序排序
		return true;
	}

	// 检验输入数据的合法性
	private static boolean CheckEdgeInfoData(Edge edge) {
		if (edge != null) {
			if (edge.getSourceIdInTopo() < 0 || edge.getDestIdInTopo() < 0
					|| edge.getWeight() < 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

}
