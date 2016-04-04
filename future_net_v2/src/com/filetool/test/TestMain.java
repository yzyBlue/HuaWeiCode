package com.filetool.test;

import java.util.ArrayList;
import java.util.List;

import com.filetool.model.Graph;
import com.filetool.model.Vertex;

public class TestMain {

	public static void main(String[] args) {
		List<Vertex> vertexs = new ArrayList<Vertex>();
		Vertex a = new Vertex("A", 0);
		Vertex b = new Vertex("B");
		Vertex c = new Vertex("C");
		Vertex d = new Vertex("D");
		Vertex e = new Vertex("E");
		Vertex f = new Vertex("F");
		vertexs.add(a);
		vertexs.add(b);
		vertexs.add(c);
		vertexs.add(d);
		vertexs.add(e);
		vertexs.add(f);
		int[][] edges = {
				{ Integer.MAX_VALUE, 6, 3, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE },
				{ 6, Integer.MAX_VALUE, 2, 5, Integer.MAX_VALUE,Integer.MAX_VALUE },
				{ 3, 2, Integer.MAX_VALUE, 3, 4, Integer.MAX_VALUE },
				{ Integer.MAX_VALUE, 5, 3, Integer.MAX_VALUE, 5, 3 },
				{ Integer.MAX_VALUE, Integer.MAX_VALUE, 4, 5,Integer.MAX_VALUE, 5 },
				{ Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 3,5, Integer.MAX_VALUE } 
				};
		Graph graph = new Graph(vertexs, edges, edges.length);
		graph.printGraph();
		graph.search();
		System.out.println(graph.getVertexList().toString());
	}

}
