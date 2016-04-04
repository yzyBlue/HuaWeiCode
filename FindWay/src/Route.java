import java.awt.TexturePaint;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SpringLayout.Constraints;

public class Route {
	/**
     * 你需要完成功能的入口
     * 
     * @author YF
     * @since 2016-3-4
     * @version V1
     */
    public static String searchRoute(String graphContent, String condition)
    {
    	final String NO_ANSWER="NA";
    	ArrayList<Integer> arrayVertexId=new ArrayList<Integer>();
    	ArrayList<Edge> arrayEdge=new ArrayList<Edge>();
    	//解析图像信息
    	if(!ResolveGraphContent(graphContent, arrayVertexId, arrayEdge))
    	{
    		return NO_ANSWER;
    	}//进行条件判断，返回NA？
    	int nVertexNum=arrayVertexId.size();//图中顶点的数量
    	//解析题目条件
    	String[] arrayCondition=condition.split(",");
    	if(arrayCondition.length<3)
    	{
    		return NO_ANSWER;
    	}
    	int nTopoSourceId=Integer.parseInt(arrayCondition[0]);//在拓扑结构中的路径起点
    	int nTopoDestId=Integer.parseInt(arrayCondition[1]);//在拓扑结构中的路径终点
		int nSerialSourceId=arrayVertexId.lastIndexOf(nTopoSourceId);//在邻接矩阵中起点的标号
		int nSerialDestId=arrayVertexId.lastIndexOf(nTopoDestId);//在邻接矩阵中终点的标号
    	
		ArrayList<Integer> fixedVertexList=new ArrayList<Integer>();//必须经过的顶点列表
    	String fixedPointList=arrayCondition[2].replace("\n", "");//过定点的列表,去除换行符
		String arrayFixedPoint[]=fixedPointList.split("\\|");
		for(String strFixedPoint:arrayFixedPoint)
		{
			int nFixedPoint=Integer.parseInt(strFixedPoint);
			fixedVertexList.add(nFixedPoint);
		}
		
		Graph graph=new Graph(nVertexNum);
		graph.Init(arrayVertexId, arrayEdge);
		ArrayList<Path> allPathList=new ArrayList<Path>();//从起点到终点在图中所有的路径
		if(!graph.FindAllPath(nSerialSourceId, nSerialDestId, allPathList))
		{
			return NO_ANSWER;
		}
		ArrayList<Path> filterPathList=new ArrayList<Path>();//过定点的路径集合
		for(Path path:allPathList)
		{
			if(path.ContainFixedVertexList(fixedVertexList))
			{
				filterPathList.add(path);
			}
		}
		for(Path path : filterPathList)
		{
			path.nPathDistance=graph.GetPathDistance(path);
		}
		Path shortestPath=filterPathList.get(0);
		for(Path path:filterPathList)
		{
			if (path.nPathDistance<shortestPath.nPathDistance) {
				shortestPath=path;
			}
		}
		String strShortestPath=shortestPath.GetPathInTopo(arrayEdge);
		return strShortestPath;
		
        
    }
    
    public static String searchRouteNew(String graphContent, String condition)
    {
    	final String NO_ANSWER="NA";
    	ArrayList<Integer> arrayVertexId=new ArrayList<Integer>();
    	ArrayList<Edge> arrayEdge=new ArrayList<Edge>();
    	//解析图像信息
    	if(!ResolveGraphContent(graphContent, arrayVertexId, arrayEdge))
    	{
    		return NO_ANSWER;
    	}//进行条件判断，返回NA？
    	int nVertexNum=arrayVertexId.size();//图中顶点的数量
    	//解析题目条件
    	String[] arrayCondition=condition.split(",");
    	if(arrayCondition.length<3)
    	{
    		return NO_ANSWER;
    	}
    	int nTopoSourceId=Integer.parseInt(arrayCondition[0]);//在拓扑结构中的路径起点
    	int nTopoDestId=Integer.parseInt(arrayCondition[1]);//在拓扑结构中的路径终点
		int nSerialSourceId=arrayVertexId.lastIndexOf(nTopoSourceId);//在邻接矩阵中起点的标号
		int nSerialDestId=arrayVertexId.lastIndexOf(nTopoDestId);//在邻接矩阵中终点的标号
    	
		ArrayList<Integer> fixedVertexInMaList=new ArrayList<Integer>();//必须经过的顶点(邻接矩阵中的序号)列表
    	String fixedPointList=arrayCondition[2].replace("\n", "");//过定点的列表,去除换行符
		String arrayFixedPoint[]=fixedPointList.split("\\|");
		for(String strFixedPoint:arrayFixedPoint)
		{
			int nFixedPoint=Integer.parseInt(strFixedPoint);
			int nFixedPointInMatrix=arrayVertexId.lastIndexOf(nFixedPoint);
			fixedVertexInMaList.add(nFixedPointInMatrix);
		}
		
		
		Graph graph=new Graph(nVertexNum);
		graph.Init(arrayVertexId, arrayEdge);
    	Map<Integer, ArrayList<Path>> mapD=new HashMap<Integer, ArrayList<Path>>();
    	Map<Integer, ArrayList<Path>> mapF=new HashMap<Integer,ArrayList<Path>>();
    	mapD.put(nSerialSourceId, graph.FindShortestPathByDijkstra(nSerialSourceId));
    	for(Integer nFixed:fixedVertexInMaList)
    	{
    		ArrayList<Path> pathList=graph.FindShortestPathByDijkstra(nFixed);
    		mapD.put(nFixed,pathList);
    		ArrayList<Path> pathFList=new ArrayList<Path>();
    		pathFList.add(pathList.get(nSerialDestId));
    		mapF.put(nFixed,pathFList);//初始化f,初值为从某个定点到终点没有经过定点的限制
    	}
    	ArrayList<Path> pathListEligible=new ArrayList<Path>();//符合条件,但可能不是最优的路径集合
    	//迭代(定点数-1)次
    	for(int k=0;k<fixedVertexInMaList.size()-1;k++)
    	{

    		Map<Integer, ArrayList<Path>> oldMapF=new HashMap<Integer, ArrayList<Path>>();//暂存上一次计算得到的MapF
    		for(Integer key:mapF.keySet())
    		{
    			if(mapF.get(key)==null||mapF.get(key).isEmpty())
    				continue;
    			ArrayList<Path> tempPathList=mapF.get(key);
    			ArrayList<Path> clonePathList=new ArrayList<>();
    			for(Path path:tempPathList)
    			{
    				Path clonePath=new Path();
    				clonePath.nPathDistance=path.nPathDistance;
    				clonePath.listSerialVertexId=(ArrayList<Integer>)path.listSerialVertexId.clone();
    				clonePathList.add(clonePath);
    			}
    			oldMapF.put(key, tempPathList);
    		}
   		
    		//至少经过k个定点
    		for(Integer nFixSource:fixedVertexInMaList)
    		{
    			//以nFixSource为起点
    			if(mapF.get(nFixSource)==null)
    			{
    				continue;
    			}
    			ArrayList<Path> tempPathList=new ArrayList<Path>(); 
    			for(Integer nAdded:fixedVertexInMaList)
    			{
    				//nAdded表示路径中新增加的定点
    				
    				if(nFixSource==nAdded||mapF.get(nAdded)==null)
    					continue;
    				Path pathD=mapD.get(nFixSource).get(nAdded);//从nFixSource到nAdded的最短路径
    				ArrayList<Path> oldPathFList=oldMapF.get(nAdded);
    				for(Path pathF:oldPathFList)
    				{
    					Path tempPath=new Path();
    					int f=pathD.nPathDistance+pathF.nPathDistance;
        				tempPath.nPathDistance=f;
        				tempPath.listSerialVertexId=(ArrayList<Integer>)pathD.listSerialVertexId.clone();
        				//拼接两条路径
        				if(tempPath.listSerialVertexId.get(tempPath.listSerialVertexId.size()-1)!=pathF.listSerialVertexId.get(0))
        				{
        					//两条路径首尾不相接
        					return NO_ANSWER;
        				}
        			    tempPath.listSerialVertexId.remove(tempPath.listSerialVertexId.size()-1);
        			    tempPath.listSerialVertexId.addAll(pathF.listSerialVertexId);
        			    if(tempPath.HasBackRoad())
        			    {
        			    	continue;
        			    }
        				tempPathList.add(tempPath);

    				}    								
    			}
    			if(tempPathList.isEmpty())
    			{
    				mapF.put(nFixSource,null);//以nFixSource为起点经过定点无法到达终点
    				continue;
    			}
    			//最短路径可能有多条!!
    			int nMinDistance=tempPathList.get(0).nPathDistance;
    			ArrayList<Path> pathMinList=new ArrayList<Path>();
    			pathMinList.add(tempPathList.get(0));
    			for(Path path:tempPathList)
    			{

    				if(path.nPathDistance==nMinDistance&&!pathMinList.get(0).listSerialVertexId.equals(path.listSerialVertexId))
    				{
    					pathMinList.add(path);
    				}
    				else
    				{
	    				if(path.nPathDistance<nMinDistance)
	    				{
	    					pathMinList.clear();
	    					pathMinList.add(path);
	    					nMinDistance=path.nPathDistance;
	    				}
	    				else
	    				{
	        				if(path.ContainFixedVertexList(fixedVertexInMaList))
	        				{
	        					//路径过所有定点
	        					boolean bContain=false;
	        					for(Path eligiblePath:pathListEligible)
	        					{
	        						if(eligiblePath.listSerialVertexId.equals(path.listSerialVertexId))
	        						{
	        							bContain=true;
	        							break;
	        						}
	        					}
	        					if(!bContain)
	        					{
	        						pathListEligible.add(path);
	        					}
	        				}
	    				}
    				}
    			}
    			//求Map<nFixSource>的值
    			mapF.put(nFixSource,pathMinList);
    		}
    	}
    	//最后一次迭代 
    	ArrayList<Path> tempPathList=new ArrayList<Path>();
    	for(Integer key:mapF.keySet())
    	{
    		if(mapF.get(key)==null||mapF.get(key).isEmpty())
    		{
    			continue;
    		}
    		Path tempPath=new Path();
    		Path pathD=mapD.get(nSerialSourceId).get(key);
    		Path pathF=mapF.get(key).get(0);
    		int f=pathD.nPathDistance+pathF.nPathDistance;
			tempPath.nPathDistance=f;
			tempPath.listSerialVertexId=(ArrayList<Integer>)pathD.listSerialVertexId.clone();
			//拼接两条路径
			if(tempPath.listSerialVertexId.get(tempPath.listSerialVertexId.size()-1)!=pathF.listSerialVertexId.get(0))
			{
				//两条路径首尾不相接
				return NO_ANSWER;
			}
		    tempPath.listSerialVertexId.remove(tempPath.listSerialVertexId.size()-1);
		    tempPath.listSerialVertexId.addAll(pathF.listSerialVertexId);
		    if(tempPath.HasBackRoad())
		    {
		    	continue;
		    }
			tempPathList.add(tempPath);   		
    	}
    	for(Path path:pathListEligible)
    	{
    		if(path.listSerialVertexId.size()>0)
    		{
    			//拼接路径
    			Path tempPath=new Path();
    			Integer nTempSource=path.listSerialVertexId.get(0);
    			Path pathD=mapD.get(nSerialSourceId).get(nTempSource);
    			int f=pathD.nPathDistance+path.nPathDistance;
    			tempPath.nPathDistance=f;
    			tempPath.listSerialVertexId=(ArrayList<Integer>)pathD.listSerialVertexId.clone();
    			//拼接两条路径
    			if(tempPath.listSerialVertexId.get(tempPath.listSerialVertexId.size()-1)!=path.listSerialVertexId.get(0))
    			{
    				//两条路径首尾不相接
    				return NO_ANSWER;
    			}
    		    tempPath.listSerialVertexId.remove(tempPath.listSerialVertexId.size()-1);
    		    tempPath.listSerialVertexId.addAll(path.listSerialVertexId);
    		    if(tempPath.HasBackRoad())
    		    {
    		    	continue;
    		    }
    			tempPathList.add(tempPath);
    		}
    	}
    	if(tempPathList.isEmpty())
    	{
    		return NO_ANSWER;
    	}
    	Path shortestPath=tempPathList.get(0);
    	for(Path path:tempPathList)
    	{
    		if(path.nPathDistance<=shortestPath.nPathDistance)
    		{
    			shortestPath=path;
    		}
    	}
    	
    	String strShortestPath=shortestPath.GetPathInTopo(arrayEdge);
		return strShortestPath;
       	
    }
    
    //解析图像信息
    private static boolean ResolveGraphContent(String graphContent,ArrayList<Integer> arrayVertexId,ArrayList<Edge> arrayEdge) 
    {    	
    	if(arrayVertexId==null)
    	{
    		arrayVertexId=new ArrayList<Integer>();
    	}
    	else
    	{
    		arrayVertexId.clear();
    	}
    	if(arrayEdge==null)
    	{
    		arrayEdge=new ArrayList<Edge>();
    	}
    	else
    	{
    		arrayEdge.clear();
    	}
    	
    	String[] groupEdgeInfo=graphContent.split("\n");
    	
		for(String strEdgeInfo:groupEdgeInfo)
		{
			String[] unit=strEdgeInfo.split(",");
			if(unit.length<4)
			{
				return false;
			}
			Integer nSource=Integer.parseInt(unit[1]);
			Integer nDest=Integer.parseInt(unit[2]);
			if(!arrayVertexId.contains(nSource))
			{
				arrayVertexId.add(nSource);
			}
			if (!arrayVertexId.contains(nDest))
			{
				arrayVertexId.add(nDest);
			}
			
			Edge tempEdge=new Edge();
			tempEdge.strSerialNo=unit[0];
			tempEdge.nSourceIdInTopo=Integer.parseInt(unit[1]);
			tempEdge.nDestIdInTopo=Integer.parseInt(unit[2]);
			tempEdge.nWeight=Integer.parseInt(unit[3]);
			if(!CheckEdgeInfoData(tempEdge))
			{
				return false;
			}
			arrayEdge.add(tempEdge);
			
		}
		
		Collections.sort(arrayVertexId);//升序排序
		return true;
	}
    
    
    //检验输入数据的合法性
    private static boolean CheckEdgeInfoData(Edge edge)
    {
    	if(edge!=null)
    	{
    		if(edge.nSourceIdInTopo<0||edge.nDestIdInTopo<0||edge.nWeight<0)
    		{
    			return false;
    		}
    		else
    		{
    			return true;
    		}
    	}
    	else
    	{
    		return false;
    	}
    }

}
