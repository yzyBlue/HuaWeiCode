import java.util.ArrayList;

public class Vertex {
	  public boolean bVisited;//�Ƿ񱻷��ʹ�
	  public Object oValue;//�����ֵ
	  public ArrayList<Integer> visitedVertexIdList;

	  public Vertex()
	  {
	    this.bVisited = false;
	    this.oValue = "";
	    this.visitedVertexIdList = new ArrayList<Integer>();
	  }

	  public Vertex(String name)
	  {
	    this.bVisited = false;
	    this.oValue = name;
	    this.visitedVertexIdList = new ArrayList<Integer>();
	  }
	  
	  public void ResetVisitedIdList()
	  {	  
		  if(this.visitedVertexIdList!=null)
		  {
			  for(Integer nVertexId:visitedVertexIdList)
			  {
				  nVertexId=0;
			  }
		  }
		  else
		  {
			  this.visitedVertexIdList=new ArrayList<Integer>();
		  }
	  }
}
