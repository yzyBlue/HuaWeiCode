package com.util.test;

import org.junit.Test;

import com.filetool.model.Graph;
import com.filetool.util.LogUtil;

public class DFSTest {
	@Test
	public void testDFS() {
		Graph theGraph = new Graph();
		theGraph.addVertex('A'); // 0 (start for dfs)
		theGraph.addVertex('B'); // 1
		theGraph.addVertex('C'); // 2
		theGraph.addVertex('D'); // 3
		theGraph.addVertex('E'); // 4

		theGraph.addEdge(0, 1); // AB
		theGraph.addEdge(1, 2); // BC
		theGraph.addEdge(0, 3); // AD
		theGraph.addEdge(3, 4); // DE

		System.out.print("Visits: ");
		LogUtil.printLog("Begin");
		theGraph.dfs(); // depth-first search
		LogUtil.printLog("End");
		System.out.println();
	}
}
