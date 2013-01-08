import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * 
 * @author __USER__
 */
public class Login extends javax.swing.JFrame {
  private JFrame jFrame;
	private String root;
	/** Creates new form Login */
	public Login() {
		jFrame = this;
		this.setTitle("人人登陆");
		root = System.getProperty("user.dir");// 文件根目录 
		setDragable();
		initComponents();
		this.setLocation(500, 150);
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

	//GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {
		jFrame.setIconImage(new ImageIcon(root+ "\\~image\\main.jpg").getImage());
		jPanel4 = new javax.swing.JPanel();
		jPanel1 = new javax.swing.JPanel() {
			ImageIcon image = new ImageIcon(root + "\\~image\\night.gif");
			//ImageIcon image = new ImageIcon(this.getClass().getResource("/image/night.gif"));
			@Override
			public void paint(Graphics g) {
				g.drawImage(image.getImage(), 0, 0, this);
			}
		};
		jPanel2 = new javax.swing.JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				ImageIcon img = new javax.swing.ImageIcon(root+ "\\~image\\Login.jpg");
				//ImageIcon img = new javax.swing.ImageIcon(this.getClass().getResource("/image/Login.jpg"));
				g.drawImage(img.getImage(), 0, 0, null);
			}
		};
		jPanel3 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jTextField2 = new javax.swing.JPasswordField();
		jTextField1 = new javax.swing.JTextField();
		jLabel2 = new javax.swing.JLabel();
		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		getContentPane().setLayout(
				new org.netbeans.lib.awtextra.AbsoluteLayout());

		jPanel4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(
				0, 0, 0), 1, true));
		jPanel4.setLayout(new java.awt.BorderLayout());

		jPanel3.setOpaque(false);
		jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

		jLabel1.setFont(new java.awt.Font("微软雅黑", 1, 18));
		jLabel1.setForeground(new java.awt.Color(255, 255, 255));
		jLabel1.setText("\u7528\u6237\u540d");
		jLabel1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(
				0, 0, 0), 1, true));
		jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(
				0, 20, 60, 20));
		jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				if(evt.getKeyCode() == KeyEvent.VK_ENTER)
					login();
			}
		});
		jPanel3.add(jTextField2,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 60, 160,
						30));

		jPanel3.add(jTextField1,
				new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 20, 160,
						30));

		jLabel2.setFont(new java.awt.Font("微软雅黑", 1, 18));
		jLabel2.setForeground(new java.awt.Color(255, 255, 255));
		jLabel2.setText("\u5bc6\u7801");
		jLabel2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(
				0, 0, 0), 1, true));

		jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(
				0, 60, 50, 20));
		jTextField1.setFont(new java.awt.Font("微软雅黑", 1, 18));
		jTextField2.setFont(new java.awt.Font("微软雅黑", 1, 18));
		jButton1.setIcon(new javax.swing.ImageIcon(root+"\\~image\\renren.jpg")); 
		//jButton1.setIcon(new javax.swing.ImageIcon(this.getClass().getResource("/image/renren.jpg")));
		jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				jButton1MouseClicked(evt);
			}
		});

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(
				jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout
				.setHorizontalGroup(jPanel2Layout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								jPanel2Layout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												jPanel3,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												242, Short.MAX_VALUE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(
												jButton1,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												96,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(28, 28, 28)));
		jPanel2Layout.setVerticalGroup(jPanel2Layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						jPanel2Layout
								.createSequentialGroup()
								.addGap(38, 38, 38)
								.addComponent(jPanel3,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										92,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addContainerGap(35, Short.MAX_VALUE))
				.addGroup(
						javax.swing.GroupLayout.Alignment.TRAILING,
						jPanel2Layout
								.createSequentialGroup()
								.addContainerGap(62, Short.MAX_VALUE)
								.addComponent(jButton1,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										59,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(44, 44, 44)));

		jPanel4.add(jPanel2, java.awt.BorderLayout.PAGE_END);

		jButton2.setIcon(new javax.swing.ImageIcon(root+"\\~image\\exit.jpg")); 
		//jButton2.setIcon(new javax.swing.ImageIcon(this.getClass().getResource("/image/exit.jpg"))); 
		jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				jButton2MouseClicked(evt);
			}
		});

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(
				jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				jPanel1Layout
						.createSequentialGroup()
						.addContainerGap(339, Short.MAX_VALUE)
						.addComponent(jButton2,
								javax.swing.GroupLayout.PREFERRED_SIZE, 39,
								javax.swing.GroupLayout.PREFERRED_SIZE)));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				jPanel1Layout
						.createSequentialGroup()
						.addComponent(jButton2,
								javax.swing.GroupLayout.PREFERRED_SIZE, 32,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(251, Short.MAX_VALUE)));

		jPanel4.add(jPanel1, java.awt.BorderLayout.CENTER);

		getContentPane()
				.add(jPanel4,
						new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0,
								380, -1));
		this.setUndecorated(true);
		pack();
	}// </editor-fold>
	//GEN-END:initComponents
	private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {
		login();
	}

	private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {
		System.exit(0);
	}
	private void login(){
		String userId = jTextField1.getText();
		String pwd = jTextField2.getText();
		if (userId.equals("") || pwd.equals("")) {
			JOptionPane.showMessageDialog(this, "请输入账号和密码.", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		System.err.println(userId + "登入!");
		final Searcher sc = new Searcher(userId, pwd);
		if (!sc.login().equals("HTTP/1.1 200 OK")) {
			System.err.println("账号信息错误");
			JOptionPane.showMessageDialog(this, "登陆失败,请检查账号信息.", "提示",
					JOptionPane.INFORMATION_MESSAGE);
		} else {
			java.awt.EventQueue.invokeLater(new Runnable() {
				public void run() {
					new iFrame(sc).setVisible(true);
				}
			});
			// new Frame(sc);
			this.dispose();// 关闭登陆窗口
		}
	}
	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Login().setVisible(true);
			}
		});
	}

	//GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JTextField jTextField1;
	private javax.swing.JTextField jTextField2;
	// End of variables declaration//GEN-END:variables

}
