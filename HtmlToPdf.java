import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;

public class HtmlToPdf {
  private String root;
	public HtmlToPdf(String root) {
		this.root = root;
	}
	//指定输入html转换为pdf到输出位置
	public void convert(String inputFile, String outputFile) {
		FileInputStream in = null;
		Tidy tidy = new Tidy();
		tidy.setInputEncoding("UTF-8");// 设置输入的编码
		try {
			tidy.setErrout(new PrintWriter("error.txt")); // 输出错误与警告信息,默认输出到stdout
			// 需要转换的文件，当然你也可以转换URL的内容
			in = new FileInputStream(inputFile + ".html");
			FileOutputStream out = new FileOutputStream(inputFile + ".xhtml"); // 输出的文件
			tidy.parse(in, out);
			out.close(); // 转换完成关闭输入输出流
			in.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(outputFile);

			ITextRenderer renderer = new ITextRenderer();
			String url = new File(inputFile + ".xhtml").toURI().toURL()
					.toString();
			renderer.setDocument(url);
			// 解决中文支持问题
			ITextFontResolver fontResolver = renderer.getFontResolver();
			fontResolver.addFont("C:/Windows/Fonts/simsun.ttc",
					BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			fontResolver.addFont("C:/Windows/Fonts/simkai.ttf",
					BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			// 解决图片的相对路径问题
			renderer.getSharedContext().setBaseURL(root + "\\" + "~temp");
			renderer.layout();
			renderer.createPDF(os);
			os.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
