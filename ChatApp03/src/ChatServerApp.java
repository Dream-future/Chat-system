
//聊天室2.0 服务器

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ChatServerApp {

	private ServerSocket ss;
	private Set<Socket> allSockets;// 实现群发功能 创建set集合用于储存所有的socket实例化对象

	public ChatServerApp() {

		try {

			ss = new ServerSocket(7777);
			allSockets = new HashSet<Socket>();// 对集合进行初始化
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void Judgection() throws IOException {
		System.out.println("服务器启动");
		while (true) {
			try {
				Socket s = ss.accept();
				allSockets.add(s);// 获取每个客户端的连接

				System.out.println("服务器与一个客户端已连接");
				new ServerThread(s).start();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

	}

	// 负责某个客户端通信
	class ServerThread extends Thread {
		private Socket s;
		private BufferedReader br;
		// 构造成员方法 对Socket 进行初始化

		public ServerThread(Socket s) {
			this.s = s;
			try {//读取客户端向服务器发送的消息
				br = new BufferedReader(new InputStreamReader(s.getInputStream()));
			} catch (IOException e) {

				e.printStackTrace();
			}
		}

		public void run() {
			while (true) {

				try {
					String str = br.readLine();
					
					
					//
					if (str.indexOf("%EXIT%") == 0) {
						// 从集合中删除 scoket
						allSockets.remove(s);

						s.close();
						// split 将获取到的信息进行分割
						sendMessageToAllClient(str.split(":")[1] + "退出了");
						// 结束while 循环
						break;
					}

					sendMessageToAllClient(str);

				} catch (IOException e) {

					e.printStackTrace();
				}
			}

		}

	}

	// 实现群发功能
	private void sendMessageToAllClient(String mesg) {
		Date d = new Date();
		// 遍利所有的socket对象 
		for (Socket s : allSockets) {
			PrintWriter pw;
			try {
				pw = new PrintWriter(s.getOutputStream());
				pw.println(mesg + "\t[" + d + "]");
				pw.flush();
			} catch (IOException e) {

				e.printStackTrace();
			}

		}
	}

	public static void main(String[] args) throws IOException {
		ChatServerApp app = new ChatServerApp();
		
			app.Judgection();
	
		

	}

}
