import java.awt.TexturePaint;
import java.util.ArrayList;
import java.util.HashSet;

public class Path implements Cloneable {
	public ArrayList<Integer> listSerialVertexId;//��·���ϵĶ���
	public int  nPathDistance;//��·�����ܳ���
	
	public Path()
	{
		listSerialVertexId=new ArrayList<Integer>();
		nPathDistance=Integer.MAX_VALUE;//·������ʼ��Ϊ����Զ
	}
	
	public Object clone() throws CloneNotSupportedException
	{
		Path pathClone=(Path)super.clone();
		if(this.listSerialVertexId!=null)
			pathClone.listSerialVertexId=(ArrayList<Integer>)this.listSerialVertexId.clone();
		return pathClone;
	}
	
	//��ø�·�������˽ṹ�б�ʾ
	public String GetPathInTopo(ArrayList<Edge> edgeList)
	{
        if(edgeList!=null&&edgeList.size()>0)
        {
			ArrayList<String> edgeIdList=new ArrayList<String>();
			for(int i=0;i<this.listSerialVertexId.size()-1;i++)
			{
				for (Edge edge : edgeList) {
					if (listSerialVertexId.get(i)==edge.nSourceIdInMatrix&&listSerialVertexId.get(i+1)==edge.nDestIdInMatrix) 
					{
						edgeIdList.add(edge.strSerialNo);
						break;
					}
				}
			}
			String strPathInTopo="";
			for(String strEdgeId:edgeIdList)
			{
				strPathInTopo+=strEdgeId;
				if(strEdgeId!=edgeIdList.get(edgeIdList.size()-1))
				{
					strPathInTopo+="|";
				}
			}
			return strPathInTopo;
        }
        else
        {
        	return "";
        }
	
	}
	
	//·���Ƿ������
	public boolean ContainFixedVertexList(ArrayList<Integer> fixedVertexList)
	{
		if(fixedVertexList!=null)
		{
			ArrayList<Integer> queue=new  ArrayList<Integer>();
			  for(Integer n:this.listSerialVertexId)
			  {
				  if (fixedVertexList.contains(n)) 
				  {
					  queue.add(n);					
				  }					  
			  }
			  if(queue.size()==fixedVertexList.size())
			  {
				  return true;
			  }
			  else 
			  {
				  return false;
			  }
		}
		else
		{
			return false;
		}
	}
	
	public boolean CatPath(Path pathNext,Path pathComplete) throws CloneNotSupportedException
	{
		if(pathNext!=null
				&&pathNext.listSerialVertexId.get(0)==this.listSerialVertexId.get(this.listSerialVertexId.size()-1))
		{
			//����·����β��Ӳſ��Խ���ƴ��
			Path clonePath=(Path)this.clone();
			clonePath.listSerialVertexId.remove(clonePath.listSerialVertexId.size()-1);
			clonePath.listSerialVertexId.addAll(pathNext.listSerialVertexId);
			clonePath.nPathDistance+=pathNext.nPathDistance;
			pathComplete=clonePath;
			return true;
		}
		else
		{
			return false;
		}
	}
	
	//�ж�·���Ƿ����˻�ͷ·
	public boolean HasBackRoad()
	{
		HashSet<Integer> setVertexId=new HashSet<Integer>();
		for(Integer nVertexId:this.listSerialVertexId)
		{
			setVertexId.add(nVertexId);
		}
		if(setVertexId.size()>=this.listSerialVertexId.size())
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	

}
