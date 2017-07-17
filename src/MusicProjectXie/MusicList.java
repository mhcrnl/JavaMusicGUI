package MusicProjectXie;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.Time;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import com.sun.media.MediaPlayer;

public class MusicList extends JList {
	protected ArrayList<File> musicFiles = new ArrayList<File>();
	protected DefaultListModel listModel = new DefaultListModel();
	
	protected ArrayList<String> musicFilesString = new ArrayList<String>();
	protected ArrayList<File> musicText = new ArrayList<File>();
	
	
	Player player;
	private boolean pause = false;
//	MainFrameMp3 mainFrameMp3;
	
	public MusicList() {
		this.setModel(listModel);
	}

	public void addMusic(File[] files) {
		for (File file : files) {
			musicFiles.add(file);
			listModel.addElement(file.getName());
			musicFilesString.add(file.getName());
			
		}
	}
	
	
	public void savePlayList() throws IOException{
		// 缓冲套接字符流
        BufferedWriter bw = new BufferedWriter(new FileWriter("/Users/java/Desktop/MyMusicList.txt"));  
        
        bw.write("我的播放列表：");
        bw.newLine();
        // 遍历  
        for (String f : musicFilesString) {  
        		
            bw.write(f);  
            bw.newLine();  
            bw.flush();  
        }  
        // 释放资源  
        bw.close();  
	}
	
	int musicTxtNumber=0;
	public String openMusicText(File[] files) throws IOException{
		
		for (File file : files) {
			musicText.add(file);
			
		}
		String result="";  
		  FileReader fileReader=null;  
		  BufferedReader bufferedReader=null;  
		  try{  
		   fileReader=new FileReader(musicText.get(musicTxtNumber++));  
		   bufferedReader=new BufferedReader(fileReader);  
		   try{  
			String read=null;  
		    while((read=bufferedReader.readLine())!=null){  
		     result=result+read+"\r\n";  
		    }  
		   }catch(Exception e){  
		    e.printStackTrace();  
		   }  
		  }catch(Exception e){  
		   e.printStackTrace();  
		  }finally{  
		   if(bufferedReader!=null){  
		    bufferedReader.close();  
		   }  
		   if(fileReader!=null){  
		    fileReader.close();  
		   }  
		  }  
//		  System.out.println("读取测试"+"\r\n"+result);  
		  return result;  
	}
	
	

	public void deleteMusic(int index) {
		musicFiles.remove(index);
		listModel.remove(index);
		if (player != null)
			player.close();
	}

	public String playMusic(int index) throws Exception {
		if (player != null)
			player.close();
		
		File file = musicFiles.get(index);
		player = Manager.createPlayer(new MediaLocator("file:///"
				+ file.getAbsolutePath()));
		player.start();
		
		return file.getName();
	}

	public void pauseMusic() {
		player.stop();
		pause = true;
	}
	
	

	public boolean isPause() {
		return pause;
	}

	public void resumeMusic() {

		player.start();
		pause = false;
	}
	
	
	

	public String getMediaTime() {
		int time = (int) player.getMediaTime().getSeconds();

		return secondsToString(time);
	}

	public String getDuration() {
		int time = (int) player.getDuration().getSeconds();
		return secondsToString(time);
	}

	private String secondsToString(int time) {
		StringBuffer buffer = new StringBuffer();
		int hours = time / 3600;
		int minutes = time / 60;
		int seconds = time % 60;
		if (hours < 10) {
			buffer.append("0" + hours);
		} else {
			buffer.append(hours);
		}
		buffer.append(":");
		if (minutes < 10) {
			buffer.append("0" + minutes);
		} else {
			buffer.append(minutes);
		}
		buffer.append(":");
		if (seconds < 10) {
			buffer.append("0" + seconds);
		} else {
			buffer.append(seconds);
		}
		return buffer.toString();
	}
	
	
}
