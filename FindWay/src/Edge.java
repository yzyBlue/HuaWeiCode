
public class Edge {
	public String strSerialNo;//�����˽ṹ�бߵı��
	public int nSourceIdInMatrix;//���ڽӾ��������ı��
	public int nSourceIdInTopo;//���˽ṹ�����ı��
	public int nDestIdInMatrix;//���ڽӾ�����β�˶���ı��
	public int nDestIdInTopo;//�����˽ṹ��β�˶���ı��
	public int nWeight;//Ȩֵ
	
	public Edge()
	{
		this.Reset();
	}
	
	public void Reset()
	{
		strSerialNo="-1";
		nSourceIdInMatrix=-1;
		nSourceIdInTopo=-1;
		nDestIdInMatrix=-1;
		nDestIdInTopo=-1;
		nWeight=-1;
	}
}
