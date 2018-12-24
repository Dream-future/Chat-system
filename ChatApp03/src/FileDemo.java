import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;





public class FileDemo {
	
		public FileDemo() {
			//Ĭ��·��
			String pathname="C:java";
			//���öԻ����ʱ�ĸ�Ŀ¼
			JFileChooser fileChooser=new JFileChooser(pathname);
			//��ʾ��ѡ���ļ��ĶԻ���
			fileChooser.showOpenDialog(null);
			//����û�ѡ����ļ�
			File file=fileChooser.getSelectedFile();
			try {
				FileInputStream in=new FileInputStream(file);
				FileOutputStream out=new FileOutputStream("new.txt");
				byte[] buf=new byte[1024];
				int len=0;
				try {
					while((len=in.read(buf))!=-1) {
						out.write(buf,0,len);
					}
					in.close();
					out.close();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			}
			//�ж��Ƿ�ѡ���ļ�
			if(file==null) {
				System.out.println("δѡ���ļ�");
				System.exit(0);
			}
			
		}
	
}	
		
