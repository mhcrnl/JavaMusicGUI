package MusicProjectXie;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import javax.media.ControllerClosedEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.PrefetchCompleteEvent;
import javax.media.RealizeCompleteEvent;
import javax.media.Time;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;


import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.JScrollBar;
import javax.swing.BoxLayout;
import java.awt.CardLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;
import java.awt.Font;
import javax.swing.JTextArea;

public class MainFrameMp3 extends JFrame implements ActionListener,
ControllerListener {

	private JPanel contentPane;
	// JMF的播放器
	Player player;
	// 播放器的视频组件和控制组件
	Component vedioComponent; 
	Component controlComponent;
	// 标示是否是第一次打开播放器
	boolean first = true;
	// 标示是否需要循环
	boolean loop = true;
	boolean random=false;
	boolean isEdit=false;
	// 文件当前目录
	String currentDirectory;
	
	
	private JButton addMusic;
	private JLabel musicNameLabel;
	private MusicList musicList;
	private JButton deleteMusic;
	private JScrollPane scrollPane;
	private JLabel timeLabel;
	private Timer timer;
	protected JTextArea textArea;
	private JMenuItem playMp3;
	private JCheckBoxMenuItem edit;
	private URL urlMusic;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrameMp3 frame = new MainFrameMp3();
					frame.setVisible(true);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	

	/**
	 * Create the frame.
	 */
	public MainFrameMp3() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 500);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu menu = new JMenu("文件");
		menuBar.add(menu);
		
		JMenuItem menuItem = new JMenuItem("打开音乐");
		menu.add(menuItem);
		menuItem.addActionListener(this);
		menu.addSeparator();// 分割条
		JMenuItem donwloadMusic = new JMenuItem("下载音乐");
		donwloadMusic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//https://github.com/sherwinxie/Java/blob/master/MyMusicList.txt
				String inputUrl = JOptionPane.showInputDialog("请粘贴要下载的音乐的Url地址："); 
				if(inputUrl!=null) {
				InputStream in; 	//选用字节流
				FileOutputStream f;
				try {
				urlMusic=new URL(inputUrl);
				//URLConnection urlConn = urlMusic.openConnection();
				HttpURLConnection urlConn =(HttpURLConnection)urlMusic.openConnection();
				in=urlConn.getInputStream();//创建连接、输入流
//				in=new URL(inputUrl).openConnection().getInputStream();//创建连接、输入流
				f = new FileOutputStream("/Users/java/Desktop/MyDownloaded.txt");//创建文件输出流
				byte [] bb=new byte[1024];  //接收缓存
				int len;
				while( (len=in.read(bb))>0){ //接收
				  f.write(bb, 0, len);  //写入文件
				  
				}
				if(f!=null) {
					
					JOptionPane.showMessageDialog(null,"下载成功！路径为桌面 文件名为：MyDownloaded.");
				}
				f.close();
				in.close();
				}catch(IOException op) {
					op.printStackTrace();
				}
				
				
				}else {
					
					JOptionPane.showMessageDialog(null, "请输入Url地址");
					
					
				}
				
				
				
			}
		});
		menu.add(donwloadMusic);
		
		menu.addSeparator();// 分割条
		JMenuItem openMusicTxt = new JMenuItem("导入歌词");
		openMusicTxt.addActionListener(this);
//		mntmNewMenuItem.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				try {
//					
//					musicList.openMusicText();
//					
//					}
//					catch(IOException openeTextError) {
//						openeTextError.printStackTrace();
//					}
//			}
//		});
		menu.add(openMusicTxt);
		
		edit = new JCheckBoxMenuItem("编辑");
		edit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!isEdit) {
					textArea.setEnabled(true);
					edit.setSelected(true);
				}else {
					textArea.setEnabled(false);
					edit.setSelected(false);
				}
				isEdit=!isEdit;
			}
		});
		menu.add(edit);
		
		menu.addSeparator();// 分割条
		JMenuItem savePlayerList = new JMenuItem("保存音乐列表");
		savePlayerList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				musicList.savePlayList();
				JOptionPane.showMessageDialog(null, "保存当前播放列表成功!");
				}
				catch(IOException o) {
					o.printStackTrace();
				}
			
			}
		});
		
		menu.add(savePlayerList);
		menu.addSeparator();// 分割条
		
		JMenuItem exitMemuItem = new JMenuItem("退出");
		exitMemuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		menu.add(exitMemuItem);
		
		
		JMenu menu_1 = new JMenu("操作");
		menuBar.add(menu_1);
		
		JCheckBoxMenuItem loopMenuItem = new JCheckBoxMenuItem("单曲循环",true);
		loopMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loop=!loop;
				
			}
		});
		
		playMp3 = new JMenuItem("播放音乐");
		playMp3.addActionListener(this);
		
		menu_1.add(playMp3);
		
		JMenuItem closeMusic = new JMenuItem("关闭音乐");
		closeMusic.addActionListener(this);
		
		JMenuItem randomMenuItem = new JMenuItem("随机播放");
		randomMenuItem.addActionListener(this);
		
		JMenuItem playNext = new JMenuItem("播放下一首");
		playNext.addActionListener(this);
		menu_1.add(playNext);
		menu_1.add(randomMenuItem);
		menu_1.add(closeMusic);
		menu_1.addSeparator();// 分割条
		
		menu_1.add(loopMenuItem);
		
		JMenu mnNewMenu = new JMenu("组员");
		menuBar.add(mnNewMenu);
		
		JCheckBoxMenuItem name1 = new JCheckBoxMenuItem("谢玮");
		mnNewMenu.add(name1);
		JCheckBoxMenuItem name2 = new JCheckBoxMenuItem("夏蕾");
		mnNewMenu.add(name2);
		JCheckBoxMenuItem name3 = new JCheckBoxMenuItem("董胜洋");
		mnNewMenu.add(name3);
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		
		addMusic = new JButton();
		deleteMusic = new JButton();
		
		musicNameLabel = new JLabel("musicNameLabel");
		
		
		timeLabel = new JLabel("timeLabel");
		musicNameLabel.setText("正在播放：");
		timeLabel.setText("播放进度：");
		timeLabel.setVisible(false);
		
		
		scrollPane = new JScrollPane();
		musicList = new MusicList();
		scrollPane.setViewportView(musicList);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		
		

		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(4)
							.addComponent(addMusic)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(deleteMusic, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(19)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE))
								.addComponent(musicNameLabel, GroupLayout.PREFERRED_SIZE, 238, GroupLayout.PREFERRED_SIZE)
								.addComponent(timeLabel, GroupLayout.PREFERRED_SIZE, 277, GroupLayout.PREFERRED_SIZE))))
					.addGap(15))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(addMusic)
						.addComponent(deleteMusic))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(musicNameLabel)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(timeLabel)
					.addGap(12)
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE))
					.addContainerGap())
		);
		
		
		textArea = new JTextArea();
		scrollPane_1.setViewportView(textArea);
		textArea.setLineWrap(true);//自动换行
		textArea.setWrapStyleWord(true);//断行连字
		textArea.setEnabled(false);
		
		panel.setLayout(gl_panel);

		
		try{
			
			addMusic.setText("导入音乐");
			addMusic.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JFileChooser chooser = new JFileChooser();
				    FileNameExtensionFilter filter = new FileNameExtensionFilter(
				        "音频文件wav或者mp3", "wav", "mp3","WAV","mp3");
				    chooser.setFileFilter(filter);
				    chooser.setMultiSelectionEnabled(true);
				    
				    currentDirectory = chooser.getCurrentDirectory().toString();
				    chooser.setCurrentDirectory(new File(currentDirectory));
				    int returnVal = chooser.showOpenDialog(null);
				    if(returnVal == JFileChooser.APPROVE_OPTION) {
				    	File files[]=chooser.getSelectedFiles();
				    	musicList.addMusic(files);
				    	musicList.setSelectedIndex(0);
				    }
					
					
				}
			});
			
			deleteMusic.setText("删除音乐");
			
			deleteMusic.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent evt) {
					// TODO Auto-generated method stub

						int index=musicList.getSelectedIndex();
						if(index!=-1)
						{
							musicList.deleteMusic(index);
//							musicNameLabel.setText("正在播放：歌曲已被删除");
//							timeLabel.setText("播放进度：无");
							index++;
							musicList.setSelectedIndex(index);
							
						}
					
				}
			});
			
			
			MouseListener mouseListener = new MouseAdapter() {
	             public void mouseClicked(MouseEvent mouseEvent) {
	            	 musicList = (MusicList) mouseEvent.getSource();
	            	 	
	                 if (mouseEvent.getClickCount() == 2) {
	                	 if(!isEdit) {
	     					textArea.setEnabled(true);
	     				}else {
	     					textArea.setEnabled(false);
	     				}
	     				isEdit=!isEdit;
	                	 	
	                 }
	                 
	                 
	             }
	         };
	         musicList.addMouseListener(mouseListener);
			
			
			timer=new Timer(1000,new ActionListener(){
				public void actionPerformed(ActionEvent e) {
						
						 timeLabel.setText("播放进度："+musicList.getMediaTime()+"/"+musicList.getDuration());
						
				}

			});
			
			
			
			}catch(Exception e){
				e.printStackTrace();
			}
	
		 
	}
	
	
	/**
	* 实现了ActionListener接口处理组件的活动事件
	*/
	public void actionPerformed(ActionEvent e) {

	   if(e.getActionCommand().equals("播放音乐")) {
		   
		   int index=musicList.getSelectedIndex();
			if(index!=-1)
			{
				try {
					
					if (player != null) {
						player.close();
					}
					
					File file = musicList.musicFiles.get(index);
					player = Manager.createPlayer(new MediaLocator("file:"
							+ file.getAbsolutePath()));
					player.start();
					this.setTitle(file.getName());
					musicNameLabel.setText("正在播放："+file.getName());
					player.addControllerListener(this);
					   // 预读文件内容
					player.prefetch();
					
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "播放音乐失败");
					ex.printStackTrace();
				}
				
			}
	   }
	   
	   if(e.getActionCommand().equals("播放下一首")) {
		   
		   int currentIndex=musicList.getSelectedIndex();
		   int maxMusic=musicList.musicFilesString.size();
		   int nextIndex=currentIndex+1;
			if(currentIndex!=-1)
			{
				try {
					
					if (player != null) {
						player.close();
					}
					if((currentIndex+1) >=maxMusic) {
						currentIndex=-1;
					}
					if((nextIndex+1)>maxMusic){
						nextIndex=0;
					}
					File file = musicList.musicFiles.get(++currentIndex);
					player = Manager.createPlayer(new MediaLocator("file:"
							+ file.getAbsolutePath()));
					player.start();
					this.setTitle(file.getName());
					musicNameLabel.setText("正在播放："+file.getName());
					musicList.setSelectedIndex(nextIndex);
					player.addControllerListener(this);
					   // 预读文件内容
					player.prefetch();
					
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "播放音乐失败");
					ex.printStackTrace();
				}
				
			}
	   }
	   
	   if (e.getActionCommand().equals("随机播放")){
			if (player != null)
			player.close();
			
		 	try {
	    		int min=0;
	    		int max=musicList.musicFilesString.size();
	    		Random random = new Random();
//	    		int shunxuNumber=++min % max;
//	        int randomNumber = random.nextInt(max)%(max-min+1) + min;
	    		
	    		int randomNumber = random.nextInt(max);// 0-max random
	        
	    		File file = musicList.musicFiles.get(randomNumber);
	    		
			player = Manager.createPlayer(new MediaLocator("file:"+ file.getAbsolutePath()));
			player.start();
			this.setTitle(file.getName());
			musicNameLabel.setText("正在播放："+file.getName());
			
			player.addControllerListener(this);
			   // 预读文件内容
			player.prefetch();
			
			
	    	}catch(Exception ew) {
	    		ew.printStackTrace();
	    	}
			
		}
	   
	   
		if (e.getActionCommand().equals("关闭音乐")){
			if (player != null)
			player.close();
		}
		
	   if (e.getActionCommand().equals("打开音乐")) {
		   
	   FileDialog fileDialog = new FileDialog(this, "打开Mp3文件", FileDialog.LOAD);
	   fileDialog.setDirectory(currentDirectory);
	   fileDialog.setVisible(true);
	   fileDialog.setMultipleMode(true);
	   currentDirectory = fileDialog.getDirectory();
	   File files[]=fileDialog.getFiles();
	   musicList.addMusic(files);
	  
	    	
	   // 如果用户放弃选择文件则返回
	   if (fileDialog.getFile() == null){
	    return;
	   }
	   
	   if (player != null){
	    // 关闭已经存在JMF播放器对象
	    player.close();
	   }
	   try {
	    // 创建一个打开选择文件的播放器
	    player = Manager.createPlayer(new MediaLocator("file:"
	      + fileDialog.getDirectory() + fileDialog.getFile()));
	    
		
		   
	   } catch (java.io.IOException e2) {
	    System.out.println(e2);
	    return;
	   } catch (NoPlayerException e2) {
	    System.out.println("不能找到播放器.");
	    return;
	   }
	   if (player == null) {
	    System.out.println("无法创建播放器.");
	    return;
	   }
	   first = false;
	   this.setTitle(fileDialog.getFile());
	   musicNameLabel.setText("正在播放："+fileDialog.getFile());
	   // 播放器的控制事件处理
	   player.addControllerListener(this);
	   // 预读文件内容
	   player.prefetch();
	   
	  
	   musicList.setSelectedIndex(nextIndex++);
	   }
	   
	   if(e.getActionCommand().equals("导入歌词")) {
		   try {
			   FileDialog fileDialoglrc = new FileDialog(this, "打开歌词文件.txt", FileDialog.LOAD);
			   fileDialoglrc.setDirectory(currentDirectory);
			   fileDialoglrc.setVisible(true);
			   currentDirectory = fileDialoglrc.getDirectory();
			   File musicText[]=fileDialoglrc.getFiles();
			   // 如果用户放弃选择文件则返回
			   if (fileDialoglrc.getFile() == null){
			    return;
			   }
			   
			   String txtContent=musicList.openMusicText(musicText);
			   textArea.setText(""+txtContent);
			   
				
				}
				catch(IOException openeTextError) {
					openeTextError.printStackTrace();
				}
	   }
	   
	}
	 int nextIndex=0;
	/**
	* 实现ControllerListener接口的方法，处理播放器的控制事件
	*/
	public void controllerUpdate(ControllerEvent e) {
	   // 调用player.close()时ControllerClosedEvent事件出现。
	   // 如果存在视觉部件，则该部件应该拆除（为一致起见,对控制面板部件也执行同样的操作）
	   if (e instanceof ControllerClosedEvent) {
	    if (vedioComponent != null) {
	     this.getContentPane().remove(vedioComponent);
	     this.vedioComponent = null;
	    }
	    if (controlComponent != null) {
	     this.getContentPane().remove(controlComponent);
	     this.controlComponent = null;
	    }
	    return;
	   }
	   // 如果是媒体文件到达尾部事件
	   if (e instanceof EndOfMediaEvent) {
	    if (loop) {
	     // 允许循环，重新开始播放
	     player.setMediaTime(new Time(0));
	     player.start();
	    }
	    return;
	   }
	   
	   // 如果是播放器预读事件
	   if (e instanceof PrefetchCompleteEvent) {
	    player.start();
//	    System.out.println("1111111111");
	    return;
	   }
	   
	   // 如果是文件打开完全事件，则显示视频组件和控制器组件
	   if (e instanceof RealizeCompleteEvent) {
	    vedioComponent = player.getVisualComponent();
	    if (vedioComponent != null){
	     this.getContentPane().add(vedioComponent);
	  
	    }
	    controlComponent = player.getControlPanelComponent();
	    if (controlComponent != null){
	     this.getContentPane().add(controlComponent, BorderLayout.SOUTH);
	    }
	    this.pack();
	   }
	}
}
