import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.htmlparser.Parser;
import org.htmlparser.visitors.TextExtractingVisitor;

public class Searcher {
  private HttpResponse response;
	private DefaultHttpClient httpClient;
	private String userName;// 登陆账号
	private String password;// 登陆密码
	private String name;// 登陆姓名
	private String uid;// 登陆账号uid
	private String root;
	private HtmlToPdf htmlToPdf;
	private int count;
	private HashSet<Album> albumTitle;// 相册清单 相册名-id
	private HashSet<Blog> blogTitle;// 日志清单 日志名-id
	private HashSet<AlbumDetail> albumDetail;// 相册内相片详情
	private HashSet<Friend> friendList;// 好友名单 姓名,id,头像地址
	public HashSet<Friend> getFriendList() {
		return friendList;
	}
	public HtmlToPdf getHtmlToPdf() {
		return htmlToPdf;
	}
	public HashSet<AlbumDetail> getAlbumDetail() {
		return albumDetail;
	}
	public HashSet<Album> getAlbumTitle() {
		return albumTitle;
	}
	public HashSet<Blog> getBlogTitle() {
		return blogTitle;
	}
	public String getUserName() {
		return userName;
	}
	public String getUid() {
		return uid;
	}
	public String getName() {
		return name;
	}
	public Searcher() {
	};
	public Searcher(String userName, String password) {
		this.userName = userName;
		this.password = password;
		albumTitle = new HashSet<Album>();
		albumDetail = new HashSet<AlbumDetail>();
		blogTitle = new HashSet<Blog>();
		friendList = new HashSet<Friend>();
		root = System.getProperty("user.dir");// 文件根目录
		htmlToPdf = new HtmlToPdf(root);
		File file = new File(root + "\\" + "~temp");
		file.mkdir();
		file = new File(root + "\\" + "~friend");
		file.mkdir();
		file = new File(root + "\\" + "~album");
		file.mkdir();
	}
	// 登陆
	public String login() {
		this.httpClient = new DefaultHttpClient();
		String loginForm = "http://www.renren.com/PLogin.do";
		String origURL = "http://www.renren.com/Home.do";
		String domain = "renren.com";
		HttpPost httpPost = new HttpPost(loginForm);
		// 将要发送的数据封包
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("email", userName));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("origURL", origURL));
		params.add(new BasicNameValuePair("domain", domain));
		// 封包添加到Post请求
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		response = postMethod(httpPost);
		// 读取跳转的地址
		String redirectUrl = response.getFirstHeader("Location").getValue();
		// 查看一下跳转过后，都出现哪些内容.
		response = getMethod(redirectUrl);
		String code = new String(checkHtml("http://www.renren.com/Home.do"));
		uid = regular(code,
				"XN\\.namespace\\( 'user' \\);[\\s]*XN\\.user\\.id = '(.*)';");
		if (uid == null)
			return "err";
		//name = regular(code, "<title>人人网 - (.*)</title>");
		name = regular(code, "<div class=\"info\">[\\s]*<div class=\"user-name\">[\\s]*<a href=\".*?\"[\\s]*class=\"name\"[\\s]*.*?>(.*?)</a>[\\s]*</div>");
		// System.err.println("欢迎" + name + ":" + uid + "登陆");
		return response.getStatusLine().toString();
	}
	// 检查链接是否有效
	private synchronized URLConnection isConnect(String url) {
		URL urlStr = null;
		HttpURLConnection httpConnection = null;
		URLConnection connection = null;
		int state = -1;
		int counts = 0;
		if (url == null || url.length() <= 0) {
			return null;
		}
		for (; counts < 7; ++counts) {
			try {
				urlStr = new URL(url);
				connection = urlStr.openConnection();
				httpConnection = (HttpURLConnection) connection;
				state = httpConnection.getResponseCode();
				if (state == 200) {
					return connection;
				}
				break;
			} catch (Exception ex) {
				++counts;
				continue;
			}
		}
		return null;
	}
	// unicode转中文
	public static String unicodeToGB(String s) {
		int len = 0;
		StringBuffer out = new StringBuffer();
		String[] re = s.split("\\\\u");
		out.append(re[0]);
		for (int i = 1; i < re.length; ++i) {
			len = re[i].length();
			if (len >= 4) {
				out.append((char) Integer.parseInt(re[i].substring(0, 4), 16));
				if (len > 4) {
					out.append(re[i].substring(4, len));
				}
			}
		}
		return out.toString();
	}
	// 将html里面的转义字符还原
	public String htmlToReal(String s) {
		s = s.replaceAll("&amp;", "&");
		s = s.replaceAll("&quot;", "\"");
		s = s.replaceAll("&nbsp;", " ");
		s = s.replaceAll("&lt;", "<");
		s = s.replaceAll("&gt;", ">");
		s = s.replaceAll("&#39;", "'");
		return s;
	}
	// 嗅探指定页面的代码
	public String checkHtml(String url) {
		HttpGet get = new HttpGet(url);
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String txt = null;
		try {
			txt = httpClient.execute(get, responseHandler);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			get.abort();
		}
		return txt;
	}
	//删除单个文件
	public void deleteFile(String sPath) {  
	    File file = new File(sPath);  
	    // 路径为文件且不为空则进行删除  
	    if (file.isFile() && file.exists()) {  
	        file.delete(); 
	    }  
	}  
	//删除文件 夹
	public void deleteDirectory(String sPath) {  
	    //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
	    if (!sPath.endsWith(File.separator)) {  
	        sPath = sPath + File.separator;  
	    }  
	    File dirFile = new File(sPath);  
	    //如果dir对应的文件不存在，或者不是一个目录，则退出  
	    if (!dirFile.exists() || !dirFile.isDirectory()) {  
	        return;  
	    }   
	    //删除文件夹下的所有文件(包括子目录)  
	    File[] files = dirFile.listFiles();  
	    for (int i = 0; i < files.length; i++) {  
	        //删除子文件  
	        if (files[i].isFile()) {  
	            deleteFile(files[i].getAbsolutePath());   
	        } //删除子目录  
	        else {  
	            deleteDirectory(files[i].getAbsolutePath());   
	        }  
	    }  
	}  
	// 用post方法向服务器请求 并获得响应
	public HttpResponse postMethod(HttpPost post) {
		HttpResponse resp = null;
		try {
			resp = httpClient.execute(post);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			post.abort();
		}
		return resp;
	}

	// 用get方法向服务器请求 并获得响应
	public HttpResponse getMethod(String url) {
		HttpGet get = new HttpGet(url);
		HttpResponse resp = null;
		try {
			resp = httpClient.execute(get);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			get.abort();
		}
		return resp;
	}
	// 正则匹配
	public String regular(String line, String reg) {
		// Pattern p = Pattern.compile("\\.do\\?id=([0-9]{9})");
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(line);
		String s = null;
		if (m.find()) {
			s = m.group(1);
		}
		return s;
	}

	// 访问好友日志列表(时间轴版)
	public void readNewBlog(String friendId) {
		// System.err.println("时间轴版");
		blogTitle.clear();
		String code = new String(checkHtml("http://blog.renren.com/blog/"
				+ friendId + "/friends?gal=buh"));
		Pattern p = Pattern
				.compile("<strong>[\\s]*<a[\\s]*href=\"(.*?)\">(.*?)</a>[\\s]*(<a[\\s]*href=\"http://i.renren.com/privilege/letter.action\" class=\"letter-icon\" title=\"附有信纸\">信纸</a>)?[\\s]*</strong>[\\s]*<span[\\s]*class=\"timestamp\">");
		Pattern t = Pattern
				.compile("data-wiki=.*?>[\\s]*([u4e00-u9fa5|\\s|\\d|\\w|\\S|\\D|\\W]*?)[\\s]*</div>");
		Pattern nextPage = Pattern
				.compile("<li><a class=\"chn\" title=\"(.*)\" href=\"(.*)\">下一页</a></li></ol>");
		Matcher m = p.matcher(code);
		Matcher n = t.matcher(code);
		String blogName = null;
		for (; m.find() && n.find();) {
			blogName = htmlToReal(m.group(2));
			// System.err.println(blogName + "  地址为:" + m.group(1));
			blogTitle.add(new Blog(blogName, m.group(1), n.group(1)));
		}
		m = nextPage.matcher(code);
		for (; m.find();) {
			code = new String(checkHtml(m.group(2)));
			m = p.matcher(code);
			n = t.matcher(code);
			for (; m.find() && n.find();) {
				blogName = htmlToReal(m.group(2));
				// System.err.println(blogName + "  地址为:" + m.group(1));
				blogTitle.add(new Blog(blogName, m.group(1), n.group(1)));
			}
			m = nextPage.matcher(code);
		}
		// System.err.println(blogTitle.size());
		// downloadBlog(blogTitle.get(s), root + "\\" + s);
		// htmlToPdf.convert(root + "\\" + s, s + ".pdf");
	}
	// 去除html中的标签，返回文本
	public String html2Str(String html) {
		try {
			Parser parser = Parser.createParser(html, "utf-8");
			TextExtractingVisitor visitor = new TextExtractingVisitor();
			parser.visitAllNodesWith(visitor);
			return visitor.getExtractedText();
		} catch (Exception ex) {
			return null;
		}
	}
	//下载单个图片文件 （文件保存目录，文件名，文件下载地址）
	public void downSingleFile(String savePath,String fileName,String downloadUrl){
		File f=new File(savePath);
		if(!f.exists())
			f.mkdir();
		downloadAlbum(downloadUrl,savePath+fileName);
	}
	// 返回日志主要内容
	public String returnBlog(String urlString) {
		String code = new String(checkHtml(urlString));
		String title = null;
		StringBuffer out = new StringBuffer();
		Pattern p = Pattern
				.compile("<input id=\"largeurl\" type=\"hidden\" name=\"largeurl\" value=\"\"/>[\\s]*-->[\\s]*<strong>(.*?)</strong>");
		Matcher m = p.matcher(code);
		if (m.find()) {
			title = m.group(1);
		}
		p = Pattern
				.compile("<div id=\"blogContent\" class=\"text-article\"[\\s]*data-wiki=\".*?\">[\\s]*(.*?)[\\s]*</div>");
		m = p.matcher(code);
		if (m.find()) {
			code = m.group(1);
			code = htmlToReal(code);
			code = code.replaceAll("<p>", "\n");
			code = code.replaceAll("<br>", "\n");
			code = code.replaceAll("</p>", "\n");
			code = code.replaceAll("<br/>", "\n");
		}
		return html2Str(out.append(title).append("\n").append(code).toString());
	}
	// 下载日志文件
	public void downloadBlog(String urlString, String filename) {
		String code = new String(checkHtml(urlString));
		String[] tempSplit = null;// 分析日志中插入文件名的缓冲
		String pictureName = null;// 日志中插图文件名
		String type = ".html";
		String title = null;
		StringBuffer out = new StringBuffer();
		Pattern p = Pattern
				.compile("<input id=\"largeurl\" type=\"hidden\" name=\"largeurl\" value=\"\"/>[\\s]*-->[\\s]*<strong>(.*?)</strong>");
		Matcher m = p.matcher(code);
		if (m.find()) {
			title = m.group(1);
			// System.err.println("找到标题: " + m.group(1));
			out.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\n<head>\n<title>"
					+ m.group(1)
					+ "</title>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></meta>\n");
			out.append("<style type=\"text/css\">\nbody {\nfont-family: SimSun;\nfont-size:22px;\nfont-style:normal;\nfont-weight:bold;\ncolor:#111111;\n}");
			out.append("</style>\n</head>\n<body>\n");
		}
		// p =
		// Pattern.compile("<div id=\"blogContent\"[\\s]*class=\"text-article\"[\\s]*data-wiki=\"\\{\\}\">(.*?)[\\s]*class=\"stat-article\"><span class=\"a-nav clearfix\">");
		p = Pattern
				.compile("<div id=\"blogContent\" class=\"text-article\"[\\s]*data-wiki=\".*?\">[\\s]*(.*?)[\\s]*</div>");
		m = p.matcher(code);
		if (m.find()) {
			// System.err.println("找到主体");
			code = m.group(1);
			p = Pattern.compile("<img.*?src=\"(.*?)\".*?\">");
			m = p.matcher(code);
			for (; m.find();) {
				tempSplit = m.group(1).split("/");
				pictureName = tempSplit[tempSplit.length - 1];
				code.replaceAll(m.group(1), pictureName);
				downloadPicture(m.group(1), pictureName, "~temp");
			}
			out.append("<strong>" + title + "</strong>" + code
					+ "\n</body>\n</html>");
		}
		try {
			File file = new File(filename + type);
			BufferedWriter write = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(file), "UTF-8"));
			String body = out.toString();
			body = body.replaceAll("<span style=\"(.*?)\">", "");
			body = body.replaceAll("</span>", "");
			write.write(body);
			write.flush();
			write.close();
			// System.err.println(filename + "下载完成");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// 下载好友头像和日志中插图
	public void downloadPicture(String urlString, String fileName, String folder) {
		URL url = null;
		InputStream is = null;
		BufferedOutputStream os = null;
		// File file = new File(fileName);
		// if(file.exists()) return;
		if (isConnect(urlString) == null) {
			System.err.println(fileName + "未找到");

			return;
		}
		try {
			url = new URL(urlString);
			URLConnection con = url.openConnection();
			// System.err.println(con);
			is = con.getInputStream();
			byte[] bs = new byte[1024];
			int len;
			os = new BufferedOutputStream(new FileOutputStream(root + "\\"
					+ folder + "\\" + fileName));
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	// 访问好友相册列表
	public void readAlbum(String friendId) {
		albumTitle.clear();
		String fileName = null;
		// String code = new
		// String(checkHtml("http://www.renren.com/"+friendId+"/profile?portal=homeFootprint&ref=home_footprint"));
		// System.err.println("进入"+regular(code,"<title>人人网 - (.*)")+"主页");
		String code = new String(checkHtml("http://photo.renren.com/photo/"
				+ friendId + "/album/relatives/profile"));
		StringBuffer comp = new StringBuffer();
		comp.append("<img src=\".*?\"[\\s]*data-src=\"(.*?)\"[\\s]*/>[\\s]*<div class=\"photo-num\">[\\s]*(.*?)[\\s]*</div>[\\s]*</a>[\\s]*");// 相册封面地址，相册内相片数量
		comp.append("<a href=\"(.*?)\" class=\"album-title\">[\\s]*<div class=\"infor\">[\\s]*<span class=\"album-name\">[\\s]*(<span class=\".*?\"></span>)?[\\s]*?(.*?)</span>[\\s]*</div>");// 相册地址，是否加密,相册名
		// 无法匹配出加密相册
		Pattern p = Pattern.compile(comp.toString());
		Matcher m = p.matcher(code);
		for (boolean lock = false; m.find(); lock = false) {
			fileName = m.group(5)
					.replaceAll("[.|\\\\|\\/|:|\\*|\\?|\"|<|>|\\|\\|]", " ")
					.trim();// 去除不能当做文件名的编号和前后空格
			fileName = htmlToReal(fileName);
			if (m.group(4) != null && m.group(4).contains("password"))
				lock = true;
			// System.err.println(fileName +"  num:"+m.group(2)+ ":" +
			// m.group(3));
			albumTitle.add(new Album(m.group(1), m.group(2), m.group(3), lock,
					fileName));
		}
		// System.err.println(albumTitle.size());
	}
	// 一键下载选中的相册
	public void selectAlbum(String path, String s, String downloadURL) {
		File file = new File(path + "\\" + s);
		String fileName = null;
		file.mkdir();
		String code = new String(checkHtml(downloadURL));
		Pattern p = Pattern
				.compile("\\{ptype:'photo',large:'(.*)',owner:'[\\d]*',id:'[\\d]*'\\}\"/>[\\s]*</a>[\\s]*<div class=\"photo-oper\">[\\s]*</div>[\\s]*<div class=\"myphoto-info\">[\\s]*<span class=\"descript\">(.*)</span>");
		// Pattern p =
		// Pattern.compile("data-src=\"(.*?)\"[\\s]*?data-.*?photo=\"\\{ptype:'photo',large:'(.*?)',owner:'[\\d]*',id:'[\\d]*'\\}\"/>[\\s]*</a>[\\s]*<div class=\"photo-oper\">[\\s]*</div>[\\s]*<div class=\"myphoto-info\">[\\s]*<span class=\"descript\">(.*?)<?/s");
		Matcher m = p.matcher(code);
		for (; m.find();) {
			fileName = m.group(2);
			fileName = fileName.replaceAll("<a.*</a>", "");// 去除可能的超链接
			fileName = fileName.replaceAll(
					"[.|\\\\|\\/|:|\\*|\\?|\"|<|>|\\|\\|]", " ").trim();// 去除不能当做文件名的编号和前后空格
			fileName = htmlToReal(fileName);
			downloadAlbum(m.group(1), path + "\\" + s + "\\" + fileName);
		}
	}
	// 显示相册内相片缩略图
	public void showAlbumDetail(String downloadURL) {
		albumDetail.clear();
		deleteDirectory(root + "\\~temp\\");
		String code = new String(checkHtml(downloadURL));
		String fileName = null;
		// Pattern p =
		// Pattern.compile("\\{ptype:'photo',large:'(.*)',owner:'[\\d]*',id:'[\\d]*'\\}\"/>[\\s]*</a>[\\s]*<div class=\"photo-oper\">[\\s]*</div>[\\s]*<div class=\"myphoto-info\">[\\s]*<span class=\"descript\">(.*)</span>");
		Pattern p = Pattern
				.compile("data-src=\"(.*?)\"[\\s]*?data-.*?photo=\"\\{ptype:'photo',large:'(.*?)',owner:'[\\d]*',id:'[\\d]*'\\}\"/>[\\s]*</a>[\\s]*<div class=\"photo-oper\">[\\s]*</div>[\\s]*<div class=\"myphoto-info\">[\\s]*<span class=\"descript\">(.*?)<?/s");
		Matcher m = p.matcher(code);
		for (; m.find();) {
			fileName = m.group(3);
			fileName = fileName.replaceAll("<a.*</a>", "");// 去除可能的超链接
			fileName = fileName.replaceAll(
					"[.|\\\\|\\/|:|\\*|\\?|\"|<|>|\\|\\|]", " ").trim();// 去除不能当做文件名的编号和前后空格
			fileName = htmlToReal(fileName);
			downloadStaticAlbum(m.group(1), fileName, m.group(1), m.group(2));
		}
	}
	// 相册内相片缩略图
	public void downloadStaticAlbum(String urlString, String fileName,
			String sUrl, String bUrl) {
		URL url = null;
		InputStream is = null;
		BufferedOutputStream os = null;
		String path = root + "\\~temp\\";
		String type = ".jpg";
		File file = new File(path + fileName + type);
		try {
			url = new URL(urlString);
			URLConnection con = url.openConnection();
			is = con.getInputStream();
			byte[] bs = new byte[1024];
			int len;
			// 判断文件是否存在
			if (!file.exists()) {
				os = new BufferedOutputStream(new FileOutputStream(path
						+ fileName + type));
				while ((len = is.read(bs)) != -1) {
					os.write(bs, 0, len);
				}
				albumDetail.add(new AlbumDetail(fileName, sUrl, bUrl, ""));
				// System.err.println("相片" + filename + type + "下载完成！");
			} else {
				int i = 0;
				for (i = 1; i < 5005; ++i) {
					if (!new File(path + fileName + "(" + i + ")" + type)
							.exists()) {
						os = new BufferedOutputStream(new FileOutputStream(path
								+ fileName + "(" + i + ")" + type));
						while ((len = is.read(bs)) != -1) {
							os.write(bs, 0, len);
						}
						albumDetail.add(new AlbumDetail(fileName, sUrl, bUrl,
								"(" + i + ")"));
						// System.err.println("相片" + filename + "(" + i + ")"+
						// type + "下载完成！");
						break;
					}
				}
				if (i >= 5005)
					System.err.println("出现重名文件,且出现覆盖保存!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	// 下载图片文件
	public void downloadAlbum(String urlString, String filename) {
		URL url = null;
		InputStream is = null;
		BufferedOutputStream os = null;
		String type = null;
		if (urlString.endsWith("gif"))
			type = ".gif";// 为动态图片
		else
			type = ".jpg";// 为静态图片
		File file = new File(filename + type);
		try {
			url = new URL(urlString);
			URLConnection con = url.openConnection();
			is = con.getInputStream();
			byte[] bs = new byte[1024];
			int len;
			// 判断文件是否存在
			if (!file.exists()) {
				os = new BufferedOutputStream(new FileOutputStream(filename
						+ type));
				while ((len = is.read(bs)) != -1) {
					os.write(bs, 0, len);
				}
				// System.err.println("相片" + filename + type + "下载完成！");
			} else {
				int i = 0;
				for (i = 1; i < 5005; ++i) {
					if (!new File(filename + "(" + i + ")" + type).exists()) {
						os = new BufferedOutputStream(new FileOutputStream(
								filename + "(" + i + ")" + type));
						while ((len = is.read(bs)) != -1) {
							os.write(bs, 0, len);
						}
						// System.err.println("相片" + filename + "(" + i + ")"+
						// type + "下载完成！");
						break;
					}
				}
				if (i >= 5005)
					System.err.println("出现重名文件,且出现覆盖保存!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	// 获取好友名单
	public void getFriendsList() {
		String code = new String(
				checkHtml("http://friend.renren.com/myfriendlistx.do"));
		Pattern p = Pattern
				.compile("\\{\"id\":([\\d]*),\"vip\":[\\w]*,\"selected\":[\\w]*,\"mo\":[\\w]*,\"name\":\"([\\w\\\\\\s]*)\",\"head\":\"([\\w\\\\/_.:]*)\",\"groups\"");
		Matcher m = p.matcher(code);
		String name = null;
		String image = null;
		for (count = 0; m.find(); ++count) {
			name = unicodeToGB(m.group(2));
			// System.err.println(name + ":" + m.group(1));
			image = m.group(3).replaceAll("\\\\", "");
			friendList.add(new Friend(name, m.group(1), image));
			// downloadPicture(image, count+name + ".jpg", "~friend"); //下载好友头像
		}
	}
}
