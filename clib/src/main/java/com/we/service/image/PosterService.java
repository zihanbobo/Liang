package com.we.service.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.we.common.Constant.ReCode;
import com.we.core.conf.Config;
import com.we.core.web.ReMsg;
import com.we.service.BaseService;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;

@Service
public class PosterService extends BaseService {
	static final Logger log = LoggerFactory.getLogger(PosterService.class);

	private static final String TEMPLATE_PATH = "/Users/walkerbao/Downloads/hb/";
	private static final String POSTER_DIR = "poster/";

	private static final String SAVE_PATH = TEMPLATE_PATH;

	public static final String DEF_IMG_FORMAT_NAME = "png";

	public static final String SHARE = "https://www.candy.club/i/";

	public String getDefFormatName() {
		return DEF_IMG_FORMAT_NAME;
	}

	public static void main(String[] args) {
		PosterService ps = new PosterService();
		try {
			ps.createPoster("AVCD");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<String> createPoster(String code) throws Exception {

		String template = Config.cur().get("poster.template.path", TEMPLATE_PATH);
		String saveRoot = Config.cur().get("poster.save.root.path", SAVE_PATH);
		List<String> pics = new ArrayList<String>();

		String path = saveRoot +File.separator+ POSTER_DIR + code;
		String[] fileNames = { "1.jpg", "2.jpg" };
		for (String fn : fileNames) {
			File file = new File(path + File.separator + fn);
			if (!file.exists()) {
				File fileDir = new File(path);
				fileDir.mkdirs();
				BufferedImage txt = drawText(code, "微软雅黑", 0, 36, new Color(0, 0, 0), 200, 50, 0, true);
				SimplePositions txtSP = new SimplePositions();
				txtSP.buildHorizontalOffset(357).buildVerticalOffset(403);

				BufferedImage qrcode = QRCodeUtil.getEncodeBuf(SHARE + code, 200);
				SimplePositions qrcodeSP = new SimplePositions();
				qrcodeSP.buildHorizontalOffset(509).buildVerticalOffset(1125);
				SimplePositions[] sp = { txtSP, qrcodeSP };
				BufferedImage[] bis = { txt, qrcode };
				getSaveFile(sp, bis, 0.8f, template + File.separator + fn, path + File.separator + fn);

			}
			pics.add("/"+POSTER_DIR +code+"/"+ fn);
		}
		return pics;
	}

	Map<String, BufferedImage> bis = new HashMap<String, BufferedImage>();

	private InputStream getFile(String path) throws FileNotFoundException {
		return new FileInputStream(new File(path));
	}

	protected static final GraphicsConfiguration GC = getGraphicsConfiguration();

	private static GraphicsConfiguration getGraphicsConfiguration() {
		Graphics2D _2D = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).createGraphics();
		GraphicsConfiguration config = _2D.getDeviceConfiguration();
		_2D.dispose();// TODO add dispose
		return config;
	}

	/***
	 * 
	 * @param txt
	 * @param fontStyle
	 * @param fontType
	 * @param fontSize
	 * @param color
	 * @param maxWidth
	 * @param theta
	 *            字体旋转角度
	 * @param isRenderingHint
	 *            字体是否去除锯齿
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage drawText(String txt, String fontStyle, int fontType, int fontSize, Color color,
			int maxWidth, int maxHeight, double theta, boolean isRenderingHint) throws IOException {
		txt = txt.replaceAll("\n|\r", "");
		Font f = new Font(fontStyle, fontType, fontSize);
		int textWeight = maxWidth;
		int tmpTextWeight = txt.length() * (fontSize + 2);
		int textHeight = 0;

		if (maxHeight != 0) {
			textHeight = maxHeight;
		} else {
			textHeight = fontSize * 2;
			if (tmpTextWeight > textWeight) {
				int line = tmpTextWeight % textWeight == 0 ? (tmpTextWeight / textWeight)
						: tmpTextWeight / textWeight + 1;
				textHeight = line * textHeight;
				// System.out.println("aaa:" + textHeight);
			}
		}
		// System.out.println("sss:" + textHeight);
		BufferedImage buffImg = GC.createCompatibleImage(textWeight, textHeight, Transparency.TRANSLUCENT);
		Graphics2D graphics = buffImg.createGraphics();
		graphics.clipRect(1, 1, textWeight - 2, textHeight - 2);
		graphics.setColor(color);
		graphics.setFont(f);
		if (isRenderingHint) {
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		if (theta != 0) {
			// 旋转中心
			double x = tmpTextWeight / 2;
			double y = textHeight / 2;
			// 旋转角度
			graphics.setTransform(AffineTransform.getRotateInstance(theta, x, y));
		}
		FontMetrics fm = graphics.getFontMetrics(f);
		int fontHeight = fm.getHeight();
		int offsetLeft = 0;
		int rowIndex = 1;
		for (int i = 0; i < txt.length(); i++) {
			char c = txt.charAt(i);
			int charWidth = fm.charWidth(c); // 字符的宽度
			// 另起一行
			if (Character.isISOControl(c)) {
				rowIndex++;
				offsetLeft = 0;
				continue;
			}
			if (offsetLeft >= (textWeight - charWidth)) {
				rowIndex++;
				offsetLeft = 0;
			}
			graphics.drawString(String.valueOf(c), offsetLeft, rowIndex * fontHeight);
			offsetLeft += charWidth;
		}
		graphics.dispose();// TODO add dispose
		return buffImg;
	}

	/***
	 * 
	 * @param sp
	 *            图片位置数组
	 * @param bis
	 *            图片数组
	 * @param opacity
	 *            透明度值
	 * @param tmpPath
	 *            模板路径
	 * @return 返回生成图片的地址
	 */
	public String getSaveFile(SimplePositions[] sp, BufferedImage[] bis, float opacity, String tmpPath,
			String savePath) {
		System.out.println(savePath);
		InputStream in = null;
		BufferedImage bi = null;
		try {
			in = getFile(tmpPath);
			Builder<?> builder = Thumbnails.of(in).scale(1.0d).outputQuality(1.0d);
			for (int i = 0; i < sp.length; i++) {
				builder.watermark(sp[i], bis[i], opacity);
			}
			bi = builder.outputFormat("png").asBufferedImage();

			File f = new File(savePath);
			Thumbnails.of(bi).scale(1d).toFile(f);

		} catch (FileNotFoundException e) {
			log.error("ERROR:" + e);
		} catch (IOException e) {
			log.error("ERROR:" + e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (bi != null) {
					bi.flush();
				}
				for (BufferedImage cbi : bis) {
					cbi.flush();
				}
			} catch (IOException e) {
				log.error("ERROR:" + e);
			}
		}
		return null;
	}

}
