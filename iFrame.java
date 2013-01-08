import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

/**
 * 
 * @author __USER__
 */
class hashx {
  private String a;
	private String b;
	public hashx(String a, String b) {
		this.a = a;
		this.b = b;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((b == null) ? 0 : b.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		hashx other = (hashx) obj;
		if (a == null) {
			if (other.a != null)
				return false;
		} else if (!a.equals(other.a))
			return false;
		if (b == null) {
			if (other.b != null)
				return false;
		} else if (!b.equals(other.b))
			return false;
		return true;
	}
	public String getA() {
		return a;
	}
	public String getB() {
		return b;
	}
}
public class iFrame extends javax.swing.JFrame {
	private Searcher searcher;
	private JFrame jFrame;
	private String root;
	private String nowUid;// 现在选择的好友uid
	private HashMap<Integer, hashx> link;
	private HashMap<Integer, hashx> albumD;
	private HashSet<JRadioButton> radioButtonHash;
	private JDialog message;
	private JLabel messageLabel;
	/** Creates new form Frame */
	/*
	 * public Frame(Searcher searcher) { this.searcher = searcher;
	 * initComponents(); }
	 */
	public iFrame(Searcher searcher) {
		jFrame = this;
		this.setTitle("人人");
		this.searcher = searcher;
		this.link = new HashMap<Integer, hashx>();
		this.albumD = new HashMap<Integer, hashx>();
		this.radioButtonHash = new HashSet<JRadioButton>();
		this.message = new JDialog(jFrame,"处理中...");
		this.messageLabel = new JLabel("等待一会儿,精彩马上继续~");
		this.message.getContentPane().add(messageLabel);
		this.message.setSize(200, 100);
		//this.message.setUndecorated(true);
		root = System.getProperty("user.dir");// 文件根目录
		setDragable();
		initComponents();
		this.setLocation(200, 90);
	}
	Point loc = null;
	Point tmp = null;
	boolean isDragged = false;
	private void setDragable() {
		this.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseReleased(java.awt.event.MouseEvent e) {
				isDragged = false;
				jFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
			public void mousePressed(java.awt.event.MouseEvent e) {
				tmp = new Point(e.getX(), e.getY());
				isDragged = true;
				jFrame.setCursor(new Cursor(Cursor.MOVE_CURSOR));
			}
		});
		this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
			public void mouseDragged(java.awt.event.MouseEvent e) {
				if (isDragged) {
					loc = new Point(jFrame.getLocation().x + e.getX() - tmp.x,
							jFrame.getLocation().y + e.getY() - tmp.y);
					jFrame.setLocation(loc);
				}
			}
		});
	}
	private void showFriendList() {
		searcher.getFriendsList();
		HashSet<Friend> friendList = searcher.getFriendList();
		// jPanel8.setPreferredSize(new java.awt.Dimension(90, 35000));
		jPanel8.setPreferredSize(new java.awt.Dimension(90, 35 * friendList
				.size()));
		int friendNum = 0;
		for (Iterator<Friend> iterator = friendList.iterator(); iterator
				.hasNext(); ++friendNum) {
			Friend f = (Friend) iterator.next();
			jLabel8 = new javax.swing.JLabel();
			jLabel8.setFont(new java.awt.Font("微软雅黑", 0, 14));
			jLabel8.setText(f.getName() + "(" + f.getId() + ")");
			jLabel8.setPreferredSize(new java.awt.Dimension(160, 29));
			jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent evt) {
					showFriend(evt);
				}
			});
			jPanel8.add(jLabel8);
		}

		jScrollPane2.setViewportView(jPanel8);
		jLabel6.setText("(好友数" + friendNum + ")");
	}
	private void showFriendList(String name) {
		Point mainLocal = jFrame.getLocation();
		message.setLocation((int) mainLocal.getX() + 420,
				(int) mainLocal.getY() + 280);
		message.setVisible(true);
		searcher.getFriendsList();
		HashSet<Friend> friendList = searcher.getFriendList();
		// jPanel8.setPreferredSize(new java.awt.Dimension(90, 35000));
		jPanel8.removeAll();
		int showFriendNum = 0;
		for (Iterator<Friend> iterator = friendList.iterator(); iterator
				.hasNext();) {
			Friend f = (Friend) iterator.next();
			if (f.getName().contains(name)) {
				jLabel8 = new javax.swing.JLabel();
				jLabel8.setFont(new java.awt.Font("微软雅黑", 0, 14));
				jLabel8.setText(f.getName() + "(" + f.getId() + ")");
				jLabel8.setPreferredSize(new java.awt.Dimension(160, 29));
				jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
					public void mouseClicked(java.awt.event.MouseEvent evt) {
						showFriend(evt);
					}
				});
				jPanel8.add(jLabel8);
				++showFriendNum;
			}
		}
		jPanel8.setPreferredSize(new java.awt.Dimension(90, 35 * showFriendNum));
		jScrollPane2.setViewportView(jPanel8);
		message.setVisible(false);
	}
	// GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {
		jFrame.setIconImage(new ImageIcon(root+ "\\~image\\main.jpg").getImage());
		jPanel6 = new javax.swing.JPanel();
		jPanel4 = new javax.swing.JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				ImageIcon img = new javax.swing.ImageIcon(root
						+ "\\~image\\topLeft.jpg");
				// ImageIcon img = new
				// javax.swing.ImageIcon(this.getClass().getResource("/image/topLeft.jpg"));
				g.drawImage(img.getImage(), 0, 0, null);
			}
		};
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jLabel3 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		jPanel3 = new javax.swing.JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				ImageIcon img = new javax.swing.ImageIcon(root
						+ "\\~image\\bottomLeft.jpg");
				// ImageIcon img = new
				// javax.swing.ImageIcon(this.getClass().getResource("/image/bottomLeft.jpg"));
				g.drawImage(img.getImage(), 0, 0, null);
			}
		};
		showBlogPanel =  new javax.swing.JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				ImageIcon img = new javax.swing.ImageIcon(root
						+ "\\~image\\blogPanel.jpg");
				// ImageIcon img = new
				// javax.swing.ImageIcon(this.getClass().getResource("/image/bottomLeft.jpg"));
				g.drawImage(img.getImage(), 0, 0, null);
			}
		};
		doingPage = new javax.swing.JPanel();
		jLabel5 = new javax.swing.JLabel();
		jScrollPane2 = new javax.swing.JScrollPane();
		jPanel8 = new javax.swing.JPanel();
		jLabel6 = new javax.swing.JLabel();
		jLabel9 = new javax.swing.JLabel();
		jLabel10 = new javax.swing.JLabel();
		jTextField1 = new javax.swing.JTextField();
		jButton4 = new javax.swing.JButton();
		jPanel1 = new javax.swing.JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				ImageIcon img = new javax.swing.ImageIcon(root
						+ "\\~image\\mainTop.jpg");
				// ImageIcon img = new
				// javax.swing.ImageIcon(this.getClass().getResource("/image/mainTop.jpg"));
				g.drawImage(img.getImage(), 0, 0, null);
			}
		};
		jButton1 = new javax.swing.JButton();
		jPanel2 = new javax.swing.JPanel();
		jPanel7 = new javax.swing.JPanel();
		shortButton = new javax.swing.JButton();
		jButton3 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();
		jPanel5 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		blogPage = new javax.swing.JScrollPane();
		showBlogPage = new javax.swing.JScrollPane();
		albumPage = new javax.swing.JScrollPane();
		showAlbumPage = new javax.swing.JScrollPane();
		jPanel9 = new javax.swing.JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				ImageIcon img = new javax.swing.ImageIcon(root+ "\\~image\\mainPanel.jpg");
				//ImageIcon img = new javax.swing.ImageIcon(this.getClass().getResource("/image/Login.jpg"));
				g.drawImage(img.getImage(), 0, 0, null);
			}
		};
		albumPanel = new javax.swing.JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				ImageIcon img = new javax.swing.ImageIcon(root+ "\\~image\\Album.jpg");
				//ImageIcon img = new javax.swing.ImageIcon(this.getClass().getResource("/image/Login.jpg"));
				g.drawImage(img.getImage(), 0, 0, null);
			}
		};
		blogPanel = new javax.swing.JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				ImageIcon img = new javax.swing.ImageIcon(root+ "\\~image\\Blog.jpg");
				//ImageIcon img = new javax.swing.ImageIcon(this.getClass().getResource("/image/Login.jpg"));
				g.drawImage(img.getImage(), 0, 0, null);
			}
		};
		jLabel7 = new javax.swing.JLabel();
		jLabel11 = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		jScrollPane1.setViewportView(jPanel9);
		jPanel6.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(0, 0, 0)));
		jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

		jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(
				0, 0, 0), 1, true));
		jPanel4.setOpaque(false);
		jPanel4.setLayout(new java.awt.GridLayout(2, 2));

		jLabel1.setFont(new java.awt.Font("楷体", 1, 18));
		jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
		jLabel1.setText("\u7528\u6237\u540d\uff1a");
		jLabel1.setToolTipText("");
		jLabel1.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
		jPanel4.add(jLabel1);

		jLabel2.setFont(new java.awt.Font("楷体", 1, 18));
		jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		jLabel2.setText(searcher.getName());
		jLabel2.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
		jPanel4.add(jLabel2);

		jLabel3.setFont(new java.awt.Font("楷体", 1, 18));
		jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel3.setText("UID\uff1a");
		jLabel3.setVerticalAlignment(javax.swing.SwingConstants.TOP);
		jPanel4.add(jLabel3);

		jLabel4.setFont(new java.awt.Font("楷体", 1, 18));
		jLabel4.setText(searcher.getUid());
		jLabel4.setVerticalAlignment(javax.swing.SwingConstants.TOP);
		jPanel4.add(jLabel4);

		jPanel6.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(
				0, 30, 210, 80));

		jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(
				0, 0, 0), 1, true));
		jPanel3.setOpaque(false);
		jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

		jLabel5.setFont(new java.awt.Font("宋体", 1, 18));
		jLabel5.setText("\u597d\u53cb\u5217\u8868\uff1a");
		jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(
				20, 10, 100, 30));

		jScrollPane2
				.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		jScrollPane2.setOpaque(false);
		showFriendList();

		jPanel3.add(jScrollPane2,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 190,
						360));

		jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
		jTextField1.setText("\u68c0\u7d22\u597d\u53cb\u59d3\u540d");
		jTextField1.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				searchFriend(evt);
			}
		});
		jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				if (evt.getKeyCode() == KeyEvent.VK_ENTER)
					showFriendList(jTextField1.getText());
			}
		});
		jPanel3.add(jTextField1,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 110,
						-1));

		jButton4.setText("\u641c\u7d22");
		jButton4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton4ActionPerformed(evt);
			}
		});
		jPanel3.add(jButton4,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 60,
						20));
		jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(
				120, 20, -1, -1));

		jPanel6.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(
				0, 110, 210, 470));

		jPanel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(
				0, 0, 0), 1, true));
		jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

		jButton1.setIcon(new javax.swing.ImageIcon(root + "\\~image\\exit.jpg")); // NOI18N
		jButton1.setOpaque(false);
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});
		jPanel1.add(jButton1,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 0, 40,
						30));
		shortButton.setIcon(new javax.swing.ImageIcon(root+"\\~image\\short.jpg")); 
		//shortButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/sun/java/swing/plaf/motif/icons/ScrollDownArrowActive.gif"))); // NOI18N
		shortButton.setOpaque(false);
		shortButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jFrame.setExtendedState(jFrame.ICONIFIED);
			}
		});
		jPanel1.add(shortButton,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 0, 40,
						30));
		jPanel6.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(
				0, 0, 850, 30));

		jPanel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(
				0, 0, 0), 1, true));
		jPanel2.setOpaque(false);
		jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

		jPanel7.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(
				0, 0, 0), 1, true));
		jPanel7.setOpaque(false);
		jPanel7.setLayout(new java.awt.GridLayout(1, 0));

		jButton3.setText("\u65e5\u5fd7");
		jButton3.setEnabled(false);
		jButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				showBlog(evt);
			}
		});
		jPanel7.add(jButton3);

		jButton2.setText("\u76f8\u518c");
		jButton2.setEnabled(false);
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				showAlbum(evt);
			}
		});
		jPanel7.add(jButton2);

		jPanel2.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(
				0, 0, 640, 40));

		jPanel5.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(0, 0, 0)));
		card = new java.awt.CardLayout();
		jPanel5.setLayout(card);

		jPanel5.add(jScrollPane1, "主页");
		jPanel5.add(blogPage, "日志");
		jPanel5.add(showBlogPage, "显示日志");
		jPanel5.add(albumPage, "相册");
		jPanel5.add(showAlbumPage, "显示相册");
		jPanel5.add(doingPage, "操作中");
		card.show(jPanel5, "主页");

		jPanel2.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(
				0, 40, 640, 510));

		jPanel6.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(
				210, 30, 640, 550));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				jPanel6, javax.swing.GroupLayout.Alignment.TRAILING,
				javax.swing.GroupLayout.DEFAULT_SIZE,
				javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				jPanel6, javax.swing.GroupLayout.Alignment.TRAILING,
				javax.swing.GroupLayout.DEFAULT_SIZE,
				javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		this.setUndecorated(true);
		pack();
	}// </editor-fold>
		// GEN-END:initComponents
	private void intoAlbum(java.awt.event.MouseEvent evt) {
		Point mainLocal = jFrame.getLocation();
		message.setLocation((int) mainLocal.getX() + 420,
				(int) mainLocal.getY() + 280);
		message.setVisible(true);
		albumD.clear();
		radioButtonHash.clear();
		JButton downSelectedButton = new JButton();
		JButton downAllButton = new JButton();
		JPanel showAlbumGrid = null;
		JRadioButton checkRadioButton = null;
		JLabel showAlbumImage = null;
		JLabel showAlbumName = null;
		Icon icon = null;
		showAlbumPanel = new JPanel();
		backButton = new JButton();
		showAlbumTopPanel = new JPanel();
		JButton id = (JButton) evt.getSource();
		String s = id.getText();
		Pattern p = Pattern.compile("\\((\\d*)\\)");
		Matcher m = p.matcher(s);
		if (m.find())
			s = m.group(1);
		showAlbumPanel.setPreferredSize(new java.awt.Dimension(620, 35));
		showAlbumPanel.setLayout(new java.awt.FlowLayout(
				java.awt.FlowLayout.LEFT));
		showAlbumTopPanel.setPreferredSize(new java.awt.Dimension(620, 30));
		showAlbumTopPanel.setLayout(new java.awt.GridLayout(1, 3));

		backButton.setText("返回相册");
		backButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				card.show(jPanel5, "相册");
			}
		});
		showAlbumTopPanel.add(backButton);

		downSelectedButton.setText("下载选中相片(" + s + ")");
		downSelectedButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				selectedDownloadAlbum(evt);
			}
		});
		showAlbumTopPanel.add(downSelectedButton);

		downAllButton.setText("一键下载(" + s + ")");
		downAllButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				oneKeyDownloadAlbum(evt);
			}
		});
		showAlbumTopPanel.add(downAllButton);
		showAlbumPanel.add(showAlbumTopPanel);
		int counts = 0;
		AlbumDetail f = null;
		String fileName = null;
		String type = ".jpg";
		searcher.showAlbumDetail(link.get(Integer.valueOf(s)).getB());
		HashSet<AlbumDetail> albumDetailList = searcher.getAlbumDetail();
		for (Iterator<AlbumDetail> iterator = albumDetailList.iterator(); iterator
				.hasNext(); ++counts) {
			f = iterator.next();
			fileName = f.getName();
			showAlbumGrid = new JPanel();
			showAlbumImage = new JLabel();
			showAlbumName = new JLabel();
			checkRadioButton = new JRadioButton();
			showAlbumGrid.setBorder(new javax.swing.border.LineBorder(
					new java.awt.Color(0, 0, 0), 1, true));
			showAlbumGrid.setPreferredSize(new java.awt.Dimension(200, 200));
			try {
				icon = new ImageIcon(ImageIO.read(new File(root + "\\~temp\\"
						+ fileName + f.getOrd() + type)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			showAlbumImage.setIcon(icon);
			// System.err.println(root + "\\~temp\\" +
			// fileName+f.getOrd()+type);
			showAlbumImage.setPreferredSize(new java.awt.Dimension(170, 140));
			showAlbumGrid.add(showAlbumImage);

			showAlbumName.setText(fileName);
			showAlbumName.setPreferredSize(new java.awt.Dimension(170, 20));
			showAlbumGrid.add(showAlbumName);

			checkRadioButton.setLabel("选择(" + counts + ")");
			checkRadioButton.setPreferredSize(new java.awt.Dimension(107, 20));
			showAlbumGrid.add(checkRadioButton);
			albumD.put(counts, new hashx(fileName, f.getLargeUrl()));
			radioButtonHash.add(checkRadioButton);
			showAlbumPanel.add(showAlbumGrid);
		}
		showAlbumPanel.setPreferredSize(new java.awt.Dimension(630,
				210 * (counts / 3 + 1) + 50));
		showAlbumPage
				.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		showAlbumPage.setViewportView(showAlbumPanel);
		card.show(jPanel5, "显示相册");
		message.setVisible(false);
	}
	private void downloadBlog(java.awt.event.MouseEvent evt) {
		Point mainLocal = jFrame.getLocation();
		message.setLocation((int) mainLocal.getX() + 420,
				(int) mainLocal.getY() + 280);
		message.setVisible(true);
		JButton id = (JButton) evt.getSource();
		String s = id.getText();
		Pattern p = Pattern.compile("\\((\\d*)\\)");
		Matcher m = p.matcher(s);
		if (m.find())
			s = m.group(1);
		JFileChooser jFC = new JFileChooser();
		jFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int option = jFC.showOpenDialog(null);
		if (option == JFileChooser.APPROVE_OPTION) {
			// card.show(jPanel5, "操作中");
			String savePath = jFC.getSelectedFile().getAbsolutePath();
			String fileName = link.get(Integer.valueOf(s)).getA();
			searcher.downloadBlog(link.get(Integer.valueOf(s)).getB(), root
					+ "\\~temp\\" + fileName);
			searcher.getHtmlToPdf().convert(root + "\\~temp\\" + fileName,
					savePath + "\\" + fileName + ".pdf");
			message.setVisible(false);
			JOptionPane.showMessageDialog(this, link.get(Integer.valueOf(s))
					.getA() + "下载完毕！", "提示", JOptionPane.INFORMATION_MESSAGE);
			// card.show(jPanel5, "相册");
		}
		else
			message.setVisible(false);
	}
	private void intoBlog(java.awt.event.MouseEvent evt) {
		Point mainLocal = jFrame.getLocation();
		message.setLocation((int) mainLocal.getX() + 420,
				(int) mainLocal.getY() + 280);
		message.setVisible(true);
		showBlogPanel.removeAll();
		backButton = new JButton();
		blogDownloadButton = new JButton();
		JScrollPane jScrollPane = new JScrollPane();
		JEditorPane jEditorPane = new JEditorPane();
		JButton id = (JButton) evt.getSource();
		String s = id.getText();
		Pattern p = Pattern.compile("\\((\\d*)\\)");
		Matcher m = p.matcher(s);
		if (m.find())
			s = m.group(1);
		showBlogPanel.setPreferredSize(new java.awt.Dimension(620, 35));
		showBlogPanel.setLayout(new java.awt.FlowLayout(
				java.awt.FlowLayout.LEFT));

		backButton.setText("返回日志");
		backButton.setPreferredSize(new java.awt.Dimension(310, 25));
		backButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				card.show(jPanel5, "日志");
			}
		});
		showBlogPanel.add(backButton);

		blogDownloadButton.setText("下载为PDF(" + s + ")");
		blogDownloadButton.setPreferredSize(new java.awt.Dimension(310, 25));
		blogDownloadButton.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				downloadBlog(evt);
			}
		});
		showBlogPanel.add(blogDownloadButton);

		jScrollPane.setPreferredSize(new java.awt.Dimension(620, 450));
		jEditorPane.setText(searcher.returnBlog(link.get(Integer.valueOf(s))
				.getB()));
		jEditorPane.setPreferredSize(new java.awt.Dimension(620, 450));
		jScrollPane.setViewportView(jEditorPane);

		showBlogPanel.add(jScrollPane);
		showBlogPanel.setPreferredSize(new java.awt.Dimension(630, 450));
		showBlogPage
				.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		showBlogPage.setViewportView(showBlogPanel);
		card.show(jPanel5, "显示日志");
		message.setVisible(false);
	}
	private void oneKeyDownloadAlbum(java.awt.event.MouseEvent evt) {
		Point mainLocal = jFrame.getLocation();
		message.setLocation((int) mainLocal.getX() + 420,
				(int) mainLocal.getY() + 280);
		message.setVisible(true);
		JButton id = (JButton) evt.getSource();
		String s = id.getText();
		Pattern p = Pattern.compile("\\((\\d*)\\)");
		Matcher m = p.matcher(s);
		if (m.find())
			s = m.group(1);
		JFileChooser jFC = new JFileChooser();
		jFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int option = jFC.showOpenDialog(null);
		if (option == JFileChooser.APPROVE_OPTION) {
			// card.show(jPanel5, "操作中");
			String savePath = jFC.getSelectedFile().getAbsolutePath();
			searcher.selectAlbum(savePath, link.get(Integer.valueOf(s)).getA(),
					link.get(Integer.valueOf(s)).getB());
			message.setVisible(false);
			JOptionPane.showMessageDialog(this, link.get(Integer.valueOf(s))
					.getA() + "下载完毕！", "提示", JOptionPane.INFORMATION_MESSAGE);
			// card.show(jPanel5, "相册");
		}
		else
			message.setVisible(false);
	}
	private void selectedDownloadAlbum(java.awt.event.MouseEvent evt) {
		Point mainLocal = jFrame.getLocation();
		message.setLocation((int) mainLocal.getX() + 420,
				(int) mainLocal.getY() + 280);
		message.setVisible(true);
		JButton id = (JButton) evt.getSource();
		String s = id.getText();
		Pattern p = Pattern.compile("\\((\\d*)\\)");
		Matcher m = p.matcher(s);
		String filePath = null;
		hashx temp = null;
		if (m.find())
			filePath = m.group(1);
		JFileChooser jFC = new JFileChooser();
		jFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int option = jFC.showOpenDialog(null);
		if (option == JFileChooser.APPROVE_OPTION) {
			// card.show(jPanel5, "操作中");
			String savePath = jFC.getSelectedFile().getAbsolutePath();
			JRadioButton f = null;
			filePath = link.get(Integer.valueOf(filePath)).getA();
			for (Iterator<JRadioButton> iterator = radioButtonHash.iterator(); iterator
					.hasNext();) {
				f = iterator.next();
				if (f.isSelected()) {
					s = f.getText();
					m = p.matcher(s);
					if (m.find())
						s = m.group(1);
					temp = albumD.get(Integer.valueOf(s));
					searcher.downSingleFile(savePath + "\\" + filePath + "\\",
							temp.getA(), temp.getB());
				}
			}
			message.setVisible(false);
			JOptionPane.showMessageDialog(jFrame, link.get(Integer.valueOf(s))
					.getA() + "下载完毕！", "提示", JOptionPane.INFORMATION_MESSAGE);
			// card.show(jPanel5, "相册");
		}
		else
			message.setVisible(false);
	}
	private void showFriend(java.awt.event.MouseEvent evt) {
		Point mainLocal = jFrame.getLocation();
		message.setLocation((int) mainLocal.getX() + 420,
				(int) mainLocal.getY() + 280);
		message.setVisible(true);
		jButton2.setEnabled(true);
		jButton3.setEnabled(true);
		jPanel9.removeAll();
		JLabel ico = (JLabel) evt.getSource();
		String s = ico.getText();
		Pattern p = Pattern.compile("\\((\\d*)\\)");
		Matcher m = p.matcher(s);
		String uid = null;
		if (m.find())
			uid = m.group(1);
		nowUid = uid;
		Friend f = null;
		HashSet<Friend> friendList = searcher.getFriendList();
		for (Iterator<Friend> iterator = friendList.iterator(); iterator
				.hasNext();) {
			f = (Friend) iterator.next();
			if (f.getId().equals(uid)) {
				searcher.downloadPicture(f.getImage(), f.getId() + ".jpg",
						"~friend");
				break;
			}
		}
		jLabel7.setIcon(null);
		try {
			jLabel7.setIcon(new ImageIcon(root + "\\~friend\\" + f.getId()
					+ ".jpg"));

		} catch (Exception e) {
		}
		jLabel11.setText(s);
		jLabel7.setPreferredSize(new java.awt.Dimension(50, 50));
		jPanel9.add(jLabel7);
		jLabel11.setPreferredSize(new java.awt.Dimension(200, 50));
		jPanel9.add(jLabel11);
		jScrollPane1.setViewportView(jPanel9);
		card.show(jPanel5, "主页");
		message.setVisible(false);
	}
	private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
		showFriendList(jTextField1.getText());
	}
	private void searchFriend(java.awt.event.MouseEvent evt) {
		jTextField1.setText("");
	}

	private void showAlbum(java.awt.event.ActionEvent evt) {
		Point mainLocal = jFrame.getLocation();
		message.setLocation((int) mainLocal.getX() + 420,
				(int) mainLocal.getY() + 280);
		message.setVisible(true);
		albumPanel.removeAll();
		link.clear();
		Icon icon = null;
		albumPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
		searcher.readAlbum(nowUid);
		Album f = null;
		String fileName = null;
		int counts = 0;
		HashSet<Album> albumList = searcher.getAlbumTitle();
		for (Iterator<Album> iterator = albumList.iterator(); iterator
				.hasNext(); ++counts) {
			f = iterator.next();
			albumGrid = new JPanel();
			albumShort = new javax.swing.JLabel();
			albumRight = new javax.swing.JPanel();
			albumName = new javax.swing.JLabel();
			albumNum = new javax.swing.JLabel();
			albumIntoButton = new javax.swing.JButton();
			albumDownloadButton = new javax.swing.JButton();
			albumGrid.setPreferredSize(new java.awt.Dimension(310, 100));
			albumGrid.setLayout(new java.awt.GridLayout(1, 2));
			fileName = f.getAlbumName();
			fileName = searcher.htmlToReal(fileName);
			fileName = fileName.replaceAll(
					"[.|\\\\|\\/|:|\\*|\\?|\"|<|>|\\|\\|]", " ").trim();// 去除不能当做文件名的编号和前后空格
			searcher.downloadPicture(f.getAlbumCover(), fileName + counts
					+ ".jpg", "~album");
			try {
				icon = new ImageIcon(ImageIO.read(new File(root + "\\~album\\"
						+ fileName + counts + ".jpg")));
			} catch (IOException e) {
				e.printStackTrace();
			}
			albumShort.setIcon(icon);
			// albumShort.setIcon(new ImageIcon(root + "\\~album\\" + fileName+
			// counts + ".jpg"));
			albumGrid.add(albumShort);
			albumRight.setLayout(new java.awt.GridLayout(4, 1));
			albumName.setText(f.getAlbumName());
			albumRight.add(albumName);
			albumNum.setText("照片数:" + f.getNum());
			albumRight.add(albumNum);
			albumDownloadButton.setText("一键下载(" + counts + ")");
			albumDownloadButton
					.addMouseListener(new java.awt.event.MouseAdapter() {
						public void mouseClicked(java.awt.event.MouseEvent evt) {
							oneKeyDownloadAlbum(evt);
						}
					});
			link.put(counts, new hashx(fileName, f.getUrl()));
			albumRight.add(albumDownloadButton);
			albumIntoButton.setText("进入相册(" + counts + ")");
			albumIntoButton.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent evt) {
					intoAlbum(evt);
				}
			});
			albumRight.add(albumIntoButton);
			albumGrid.add(albumRight);
			albumPanel.add(albumGrid);
			// System.err.println(f.getAlbumName() + ":" + f.getNum());
		}
		albumPanel.setPreferredSize(new java.awt.Dimension(630, counts * 65));
		albumPage
				.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		albumPage.setViewportView(albumPanel);
		card.show(jPanel5, "相册");
		message.setVisible(false);
	}
	private void showBlog(java.awt.event.ActionEvent evt) {
		Point mainLocal = jFrame.getLocation();
		message.setLocation((int) mainLocal.getX() + 420,
				(int) mainLocal.getY() + 280);
		message.setVisible(true);
		blogPanel.removeAll();
		link.clear();
		albumPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
		searcher.readNewBlog(nowUid);
		Blog f = null;
		String fileName = null;
		int counts = 0;
		HashSet<Blog> blogList = searcher.getBlogTitle();
		for (Iterator<Blog> iterator = blogList.iterator(); iterator.hasNext(); ++counts) {
			f = iterator.next();
			fileName = f.getBlogName();
			fileName = searcher.htmlToReal(fileName);
			fileName = fileName.replaceAll(
					"[.|\\\\|\\/|:|\\*|\\?|\"|<|>|\\|\\|]", " ").trim();// 去除不能当做文件名的编号和前后空格
			blogGrid = new JPanel();
			blogLeft = new JPanel();
			blogRight = new JPanel();
			blogTitleLabel = new JLabel();
			blogScrollPane = new JScrollPane();
			blogShort = new JEditorPane();
			blogScrollPane
					.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
			blogIntoButton = new JButton();
			blogDownloadButton = new JButton();
			selectRadioButton = new JRadioButton();
			blogGrid.setPreferredSize(new java.awt.Dimension(620, 110));
			blogLeft.setPreferredSize(new java.awt.Dimension(500, 110));
			blogTitleLabel.setFont(new java.awt.Font("宋体", 1, 16));
			blogTitleLabel.setText(fileName);
			blogTitleLabel.setPreferredSize(new java.awt.Dimension(500, 18));
			blogLeft.add(blogTitleLabel);
			blogScrollPane.setPreferredSize(new java.awt.Dimension(500, 85));
			blogShort.setFont(new java.awt.Font("宋体", 1, 16));
			blogShort.setText(f.getBody());
			blogShort.setEnabled(false);
			blogScrollPane.setViewportView(blogShort);
			blogLeft.add(blogScrollPane);
			blogGrid.add(blogLeft);
			blogRight.setLayout(new java.awt.GridLayout(2, 1));
			blogIntoButton.setText("查看全文" + "(" + counts + ")");
			blogIntoButton.setToolTipText("查看全文");
			blogIntoButton.setPreferredSize(new java.awt.Dimension(81, 30));
			blogIntoButton.addMouseListener(new java.awt.event.MouseAdapter() {
				public void mouseClicked(java.awt.event.MouseEvent evt) {
					intoBlog(evt);
				}
			});
			blogRight.add(blogIntoButton);
			blogDownloadButton.setText("下载为PDF" + "(" + counts + ")");
			blogDownloadButton.setToolTipText("下载为PDF");
			blogDownloadButton.setPreferredSize(new java.awt.Dimension(81, 30));
			blogDownloadButton
					.addMouseListener(new java.awt.event.MouseAdapter() {
						public void mouseClicked(java.awt.event.MouseEvent evt) {
							downloadBlog(evt);
						}
					});
			blogRight.add(blogDownloadButton);
			// selectRadioButton.setText("jRadioButton1");
			// blogRight.add(selectRadioButton);
			blogGrid.add(blogRight);
			blogPanel.add(blogGrid);
			link.put(counts, new hashx(fileName, f.getUrl()));
		}
		blogPanel.setPreferredSize(new java.awt.Dimension(630, counts * 120));
		blogPage.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		blogPage.setViewportView(blogPanel);
		card.show(jPanel5, "日志");
		message.setVisible(false);
	}

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
		System.exit(0);
	}

	/**
	 * @param args
	 *            the command line arguments
	 */

	// GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JButton jButton3;
	private javax.swing.JButton jButton4;
	private javax.swing.JButton albumDownloadButton;
	private javax.swing.JButton albumIntoButton;
	private javax.swing.JButton blogIntoButton;
	private javax.swing.JButton blogDownloadButton;
	private javax.swing.JButton shortButton;
	private javax.swing.JButton backButton;
	private javax.swing.JLabel albumShort;
	private javax.swing.JLabel albumName;
	private javax.swing.JLabel albumNum;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel10;
	private javax.swing.JLabel jLabel11;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JLabel blogTitleLabel;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JPanel jPanel6;
	private javax.swing.JPanel jPanel7;
	private javax.swing.JPanel jPanel8;
	private javax.swing.JPanel jPanel9;
	private javax.swing.JPanel albumPanel;
	private javax.swing.JPanel albumGrid;
	private javax.swing.JPanel albumRight;
	private javax.swing.JPanel blogPanel;
	private javax.swing.JPanel blogGrid;
	private javax.swing.JPanel blogLeft;
	private javax.swing.JPanel blogRight;
	private javax.swing.JPanel doingPage;
	private javax.swing.JPanel showBlogPanel;
	private javax.swing.JPanel showAlbumPanel;
	private javax.swing.JPanel showAlbumTopPanel;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JScrollPane blogPage;
	private javax.swing.JScrollPane albumPage;
	private javax.swing.JScrollPane showBlogPage;
	private javax.swing.JScrollPane showAlbumPage;
	private javax.swing.JScrollPane blogScrollPane;
	private javax.swing.JTextField jTextField1;
	private javax.swing.JEditorPane blogShort;
	private javax.swing.JRadioButton selectRadioButton;
	private CardLayout card;
	// End of variables declaration//GEN-END:variables

}
