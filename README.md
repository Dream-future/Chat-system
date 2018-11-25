客户端


package chat;
import java.awt.BorderLayout;
//窗口初始化
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;








public class ChatRoomClient implements ActionListener{
		private JFrame jf;//窗体
		private JTextField jtf;//自己输入内容的文本框
		private JTextArea jta;//聊天内容显示文本域
		private JButton jb;//发送按钮
		private JLabel label;//用户标签
		private String name;//用户名
		private Socket s;//实现客户端与服务端的连接
		private PrintWriter pw;//向服务器写入的输出流
		private BufferedReader br;
	public ChatRoomClient() {
		jf=new JFrame("聊天室客户端v1.0");
		do {
		String ip=JOptionPane.showInputDialog(jf,"请输入服务器地址");
		int port=Integer.parseInt(JOptionPane.showInputDialog(jf,"请输入端口号"));
		
		try {
			s=new Socket(ip, port);//创建连接 建立连接
			pw=new PrintWriter(s.getOutputStream());//初始化
			//读取服务器端发来的消息
			br=new BufferedReader(new InputStreamReader(s.getInputStream()));
		}
		 catch (Exception e) {
		
			e.printStackTrace();
		}
		
		}while(s==null);
		//设置聊天内容显示框大小
		jta=new JTextArea(20,40);
		//设置聊天内容字体
		jta.setFont(new Font("宋体",Font.BOLD,24));
		//文本域不可编辑
		jta.setEditable(false);
		jtf=new JTextField(28);
		jb=new JButton("发送");
		name=JOptionPane.showInputDialog(jf,"请输入您的名称：");
		label=new JLabel(name+":");
		init();
		addEventHandler();	
		new ReadMessageThread().start();
		
	}//设置窗体布局 添加按钮
	private void init() {
		
		
		//可滚动面板
		JScrollPane jsp=new JScrollPane(jta);
		//布局方式
		jf.add(jsp,BorderLayout.CENTER);
		
		JPanel panel=new JPanel();
		panel.add(label);
		panel.add(jtf);
		panel.add(jb);
		
		jf.add(panel,BorderLayout.SOUTH);	
	}	//设置窗体大小
	public void showMe() {
		jf.pack();
		//可建
		jf.setVisible(true);
		jf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
	}	
	public static void main(String[] args) {
		ChatRoomClient c=new ChatRoomClient();
		c.showMe();
	
		
	}
	//注册监听器
	private void addEventHandler() {
		jb.addActionListener(this);
		jtf.addActionListener(this);
		//创建一个适配器
		jf.addWindowFocusListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//弹出窗体提示用户是否关闭
			int op=	JOptionPane.showConfirmDialog(jf, "要关闭聊天室？");
			if(op==JOptionPane.YES_OPTION) {
				pw.println("%EXIT%:"+name);
				pw.flush();
				try {
					s.close();
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
				System.exit(0);
			}
			}
			
		});
	}
	
	@Override//将用户发送的信息 发送给服务器端
	public void actionPerformed(ActionEvent e) {
		//判断用户发送的内容
		if(jtf.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(jf, "不能发送空消息");
			return;
		}
		pw.println(name+":"+jtf.getText());//
		pw.flush();
		jtf.setText("");//清空输入框
	}
	class ReadMessageThread extends Thread{
		@Override
		public void run() {
			while(true) {
				try {
					String str=br.readLine();
					//显示聊天内容文本域
					jta.append(str+"\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			
			
		}
	}		
}
