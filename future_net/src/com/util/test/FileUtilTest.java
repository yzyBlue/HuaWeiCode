/**
 * 
 */
package com.util.test;





import org.junit.Test;

import com.filetool.util.FileUtil;
import com.filetool.util.LogUtil;

/**
 * @author Yuan.Ziyang
 *
 */
public class FileUtilTest {
	@Test
	public void readTest() {
		LogUtil.printLog("Begin");
		String filePath = "resource/graph.csv";
		String content = FileUtil.read(filePath, null);
		System.out.println(content.toString());
		LogUtil.printLog("End");
	}
}
