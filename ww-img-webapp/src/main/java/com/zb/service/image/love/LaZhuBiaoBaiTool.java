package com.zb.service.image.love;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;

import com.zb.service.cloud.StorageService;
import com.zb.service.image.BaseTool;
import com.zb.service.image.SimplePositions;

public class LaZhuBiaoBaiTool extends BaseTool {

	public LaZhuBiaoBaiTool(StorageService storageService) {
		super(storageService);
	}

	public static void main(String[] args) throws IOException {
		String tmpPath2 = "http://imgzb.zhuangdianbi.com/5714943c0cf2df2e739ecd31";
		StorageService storageService = new StorageService();
		LaZhuBiaoBaiTool tyt = new LaZhuBiaoBaiTool(storageService);
		tyt.isDebug(true);

		System.out.println(tyt.drawImg("装点逼啊", tmpPath2));
	}

	/***
	 * @param name
	 *            姓名
	 * @param tmpPath
	 *            背景图路径
	 * @return 目标图片地址
	 * @throws IOException
	 */
	public String drawImg(String name, String tmpPath) throws IOException {
		int fontSize = 20;
		Color color = new Color(0, 0, 0);
		String fontStyle = "黑体";
		int fontType = Font.PLAIN;
		int left = 310 - (name.length() * 10);
		// 姓名
		SimplePositions nameSP = new SimplePositions();
		nameSP.buildHorizontalOffset(left).buildVerticalOffset(185);
		BufferedImage nameBI = drawText(name, fontStyle, fontType, fontSize,
				color, 240, 40, 0, true);
		SimplePositions[] sp = { nameSP };
		BufferedImage[] bis = { nameBI };

		return super.getSaveFile(sp, bis, 0.8f, tmpPath);
	}

}
