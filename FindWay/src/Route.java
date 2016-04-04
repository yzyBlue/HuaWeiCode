import java.awt.TexturePaint;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.SpringLayout.Constraints;

public class Route {
	/**
     * ����Ҫ��ɹ��ܵ����
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
    	//����ͼ����Ϣ
    	if(!ResolveGraphContent(graphContent, arrayVertexId, arrayEdge))
    	{
    		return NO_ANSWER;
    	}//���������жϣ�����NA��
    	int nVertexNum=arrayVertexId.size();//ͼ�ж��������
    	//������Ŀ����
    	String[] arrayCondition=condition.split(",");
    	if(arrayCondition.length<3)
    	{
    		return NO_ANSWER;
    	}
    	int nTopoSourceId=Integer.parseInt(arrayCondition[0]);//�����˽ṹ�е�·�����
    	int nTopoDestId=Integer.parseInt(arrayCondition[1]);//�����˽ṹ�е�·���յ�
		int nSerialSourceId=arrayVertexId.lastIndexOf(nTopoSourceId);//���ڽӾ��������ı��
		int nSerialDestId=arrayVertexId.lastIndexOf(nTopoDestId);//���ڽӾ������յ�ı��
    	
		ArrayList<Integer> fixedVertexList=new ArrayList<Integer>();//���뾭���Ķ����б�
    	String fixedPointList=arrayCondition[2].replace("\n", "");//��������б�,ȥ�����з�
		String arrayFixedPoint[]=fixedPointList.split("\\|");
		for(String strFixedPoint:arrayFixedPoint)
		{
			int nFixedPoint=Integer.parseInt(strFixedPoint);
			fixedVertexList.add(nFixedPoint);
		}
		
		Graph graph=new Graph(nVertexNum);
		graph.Init(arrayVertexId, arrayEdge);
		ArrayList<Path> allPathList=new ArrayList<Path>();//����㵽�յ���ͼ�����е�·��
		if(!graph.FindAllPath(nSerialSourceId, nSerialDestId, allPathList))
		{
			return NO_ANSWER;
		}
		ArrayList<Path> filterPathList=new ArrayList<Path>();//�������·������
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
    	//����ͼ����Ϣ
    	if(!ResolveGraphContent(graphContent, arrayVertexId, arrayEdge))
    	{
    		return NO_ANSWER;
    	}//���������жϣ�����NA��
    	int nVertexNum=arrayVertexId.size();//ͼ�ж��������
    	//������Ŀ����
    	String[] arrayCondition=condition.split(",");
    	if(arrayCondition.length<3)
    	{
    		return NO_ANSWER;
    	}
    	int nTopoSourceId=Integer.parseInt(arrayCondition[0]);//�����˽ṹ�е�·�����
    	int nTopoDestId=Integer.parseInt(arrayCondition[1]);//�����˽ṹ�е�·���յ�
		int nSerialSourceId=arrayVertexId.lastIndexOf(nTopoSourceId);//���ڽӾ��������ı��
		int nSerialDestId=arrayVertexId.lastIndexOf(nTopoDestId);//���ڽӾ������յ�ı��
    	
		ArrayList<Integer> fixedVertexInMaList=new ArrayList<Integer>();//���뾭���Ķ���(�ڽӾ����е����)�б�
    	String fixedPointList=arrayCondition[2].replace("\n", "");//��������б�,ȥ�����з�
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
    		mapF.put(nFixed,pathFList);//��ʼ��f,��ֵΪ��ĳ�����㵽�յ�û�о������������
    	}
    	ArrayList<Path> pathListEligible=new ArrayList<Path>();//��������,�����ܲ������ŵ�·������
    	//����(������-1)��
    	for(int k=0;k<fixedVertexInMaList.size()-1;k++)
    	{

    		Map<Integer, ArrayList<Path>> oldMapF=new HashMap<Integer, ArrayList<Path>>();//�ݴ���һ�μ���õ���MapF
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
   		
    		//���پ���k������
    		for(Integer nFixSource:fixedVertexInMaList)
    		{
    			//��nFixSourceΪ���
    			if(mapF.get(nFixSource)==null)
    			{
    				continue;
    			}
    			ArrayList<Path> tempPathList=new ArrayList<Path>(); 
    			for(Integer nAdded:fixedVertexInMaList)
    			{
    				//nAdded��ʾ·���������ӵĶ���
    				
    				if(nFixSource==nAdded||mapF.get(nAdded)==null)
    					continue;
    				Path pathD=mapD.get(nFixSource).get(nAdded);//��nFixSource��nAdded�����·��
    				ArrayList<Path> oldPathFList=oldMapF.get(nAdded);
    				for(Path pathF:oldPathFList)
    				{
    					Path tempPath=new Path();
    					int f=pathD.nPathDistance+pathF.nPathDistance;
        				tempPath.nPathDistance=f;
        				tempPath.listSerialVertexId=(ArrayList<Integer>)pathD.listSerialVertexId.clone();
        				//ƴ������·��
        				if(tempPath.listSerialVertexId.get(tempPath.listSerialVertexId.size()-1)!=pathF.listSerialVertexId.get(0))
        				{
        					//����·����β�����
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
    				mapF.put(nFixSource,null);//��nFixSourceΪ��㾭�������޷������յ�
    				continue;
    			}
    			//���·�������ж���!!
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
	        					//·�������ж���
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
    			//��Map<nFixSource>��ֵ
    			mapF.put(nFixSource,pathMinList);
    		}
    	}
    	//���һ�ε��� 
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
			//ƴ������·��
			if(tempPath.listSerialVertexId.get(tempPath.listSerialVertexId.size()-1)!=pathF.listSerialVertexId.get(0))
			{
				//����·����β�����
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
    			//ƴ��·��
    			Path tempPath=new Path();
    			Integer nTempSource=path.listSerialVertexId.get(0);
    			Path pathD=mapD.get(nSerialSourceId).get(nTempSource);
    			int f=pathD.nPathDistance+path.nPathDistance;
    			tempPath.nPathDistance=f;
    			tempPath.listSerialVertexId=(ArrayList<Integer>)pathD.listSerialVertexId.clone();
    			//ƴ������·��
    			if(tempPath.listSerialVertexId.get(tempPath.listSerialVertexId.size()-1)!=path.listSerialVertexId.get(0))
    			{
    				//����·����β�����
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
    
    //����ͼ����Ϣ
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
		
		Collections.sort(arrayVertexId);//��������
		return true;
	}
    
    
    //�����������ݵĺϷ���
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
