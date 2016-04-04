
public class Main {

	public static void main(String[] args) {
		if (args.length != 3)
        {
            System.err.println("please input args: graphFilePath, conditionFilePath, resultFilePath");
            return;
        }

        String graphFilePath = args[0];
        String conditionFilePath = args[1];
        String resultFilePath = args[2];

        LogUtil.printLog("Begin");

        // ��ȡ�����ļ�
        String graphContent = FileUtil.read(graphFilePath, null);
        String conditionContent = FileUtil.read(conditionFilePath, null);

        // ����ʵ�����
       //String resultStr = Route.searchRoute(graphContent, conditionContent);
       String resultStr = Route.searchRouteNew(graphContent, conditionContent);

        // д������ļ�
        FileUtil.write(resultFilePath, resultStr, false);

        LogUtil.printLog("End");

	}

}
