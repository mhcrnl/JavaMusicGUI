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
	// JMF�Ĳ�����
	Player player;
	// ����������Ƶ����Ϳ������
	Component vedioComponent; 
	Component controlComponent;
	// ��ʾ�Ƿ��ǵ�һ�δ򿪲�����
	boolean first = true;
	// ��ʾ�Ƿ���Ҫѭ��
	boolean loop = true;
	boolean random=false;
	boolean isEdit=false;
	// �ļ���ǰĿ¼
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
		
		JMenu menu = new JMenu("�ļ�");
		menuBar.add(menu);
		
		JMenuItem menuItem = new JMenuItem("������");
		menu.add(menuItem);
		menuItem.addActionListener(this);
		menu.addSeparator();// �ָ���
		JMenuItem donwloadMusic = new JMenuItem("��������");
		donwloadMusic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//https://github.com/sherwinxie/Java/blob/master/MyMusicList.txt
				String inputUrl = JOptionPane.showInputDialog("��ճ��Ҫ���ص����ֵ�Url��ַ��"); 
				if(inputUrl!=null) {
				InputStream in; 	//ѡ���ֽ���
				FileOutputStream f;
				try {
				urlMusic=new URL(inputUrl);
				//URLConnection urlConn = urlMusic.openConnection();
				HttpURLConnection urlConn =(HttpURLConnection)urlMusic.openConnection();
				in=urlConn.getInputStream();//�������ӡ�������
//				in=new URL(inputUrl).openConnection().getInputStream();//�������ӡ�������
				f = new FileOutputStream("/Users/java/Desktop/MyDownloaded.txt");//�����ļ������
				byte [] bb=new byte[1024];  //���ջ���
				int len;
				while( (len=in.read(bb))>0){ //����
				  f.write(bb, 0, len);  //д���ļ�
				  
				}
				if(f!=null) {
					
					JOptionPane.showMessageDialog(null,"���سɹ���·��Ϊ���� �ļ���Ϊ��MyDownloaded.");
				}
				f.close();
				in.close();
				}catch(IOException op) {
					op.printStackTrace();
				}
				
				
				}else {
					
					JOptionPane.showMessageDialog(null, "������Url��ַ");
					
					
				}
				
				
				
			}
		});
		menu.add(donwloadMusic);
		
		menu.addSeparator();// �ָ���
		JMenuItem openMusicTxt = new JMenuItem("������");
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
		
		edit = new JCheckBoxMenuItem("�༭");
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
		
		menu.addSeparator();// �ָ���
		JMenuItem savePlayerList = new JMenuItem("���������б�");
		savePlayerList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
				musicList.savePlayList();
				JOptionPane.showMessageDialog(null, "���浱ǰ�����б�ɹ�!");
				}
				catch(IOException o) {
					o.printStackTrace();
				}
			
			}
		});
		
		menu.add(savePlayerList);
		menu.addSeparator();// �ָ���
		
		JMenuItem exitMemuItem = new JMenuItem("�˳�");
		exitMemuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		menu.add(exitMemuItem);
		
		
		JMenu menu_1 = new JMenu("����");
		menuBar.add(menu_1);
		
		JCheckBoxMenuItem loopMenuItem = new JCheckBoxMenuItem("����ѭ��",true);
		loopMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loop=!loop;
				
			}
		});
		
		playMp3 = new JMenuItem("��������");
		playMp3.addActionListener(this);
		
		menu_1.add(playMp3);
		
		JMenuItem closeMusic = new JMenuItem("�ر�����");
		closeMusic.addActionListener(this);
		
		JMenuItem randomMenuItem = new JMenuItem("�������");
		randomMenuItem.addActionListener(this);
		
		JMenuItem playNext = new JMenuItem("������һ��");
		playNext.addActionListener(this);
		menu_1.add(playNext);
		menu_1.add(randomMenuItem);
		menu_1.add(closeMusic);
		menu_1.addSeparator();// �ָ���
		
		menu_1.add(loopMenuItem);
		
		JMenu mnNewMenu = new JMenu("��Ա");
		menuBar.add(mnNewMenu);
		
		JCheckBoxMenuItem name1 = new JCheckBoxMenuItem("л��");
		mnNewMenu.add(name1);
		JCheckBoxMenuItem name2 = new JCheckBoxMenuItem("����");
		mnNewMenu.add(name2);
		JCheckBoxMenuItem name3 = new JCheckBoxMenuItem("��ʤ��");
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
		musicNameLabel.setText("���ڲ��ţ�");
		timeLabel.setText("���Ž��ȣ�");
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
		textArea.setLineWrap(true);//�Զ�����
		textArea.setWrapStyleWord(true);//��������
		textArea.setEnabled(false);
		
		panel.setLayout(gl_panel);

		
		try{
			
			addMusic.setText("��������");
			addMusic.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					JFileChooser chooser = new JFileChooser();
				    FileNameExtensionFilter filter = new FileNameExtensionFilter(
				        "��Ƶ�ļ�wav����mp3", "wav", "mp3","WAV","mp3");
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
			
			deleteMusic.setText("ɾ������");
			
			deleteMusic.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent evt) {
					// TODO Auto-generated method stub

						int index=musicList.getSelectedIndex();
						if(index!=-1)
						{
							musicList.deleteMusic(index);
//							musicNameLabel.setText("���ڲ��ţ������ѱ�ɾ��");
//							timeLabel.setText("���Ž��ȣ���");
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
						
						 timeLabel.setText("���Ž��ȣ�"+musicList.getMediaTime()+"/"+musicList.getDuration());
						
				}

			});
			
			
			
			}catch(Exception e){
				e.printStackTrace();
			}
	
		 
	}
	
	
	/**
	* ʵ����ActionListener�ӿڴ�������Ļ�¼�
	*/
	public void actionPerformed(ActionEvent e) {

	   if(e.getActionCommand().equals("��������")) {
		   
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
					musicNameLabel.setText("���ڲ��ţ�"+file.getName());
					player.addControllerListener(this);
					   // Ԥ���ļ�����
					player.prefetch();
					
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "��������ʧ��");
					ex.printStackTrace();
				}
				
			}
	   }
	   
	   if(e.getActionCommand().equals("������һ��")) {
		   
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
					musicNameLabel.setText("���ڲ��ţ�"+file.getName());
					musicList.setSelectedIndex(nextIndex);
					player.addControllerListener(this);
					   // Ԥ���ļ�����
					player.prefetch();
					
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "��������ʧ��");
					ex.printStackTrace();
				}
				
			}
	   }
	   
	   if (e.getActionCommand().equals("�������")){
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
			musicNameLabel.setText("���ڲ��ţ�"+file.getName());
			
			player.addControllerListener(this);
			   // Ԥ���ļ�����
			player.prefetch();
			
			
	    	}catch(Exception ew) {
	    		ew.printStackTrace();
	    	}
			
		}
	   
	   
		if (e.getActionCommand().equals("�ر�����")){
			if (player != null)
			player.close();
		}
		
	   if (e.getActionCommand().equals("������")) {
		   
	   FileDialog fileDialog = new FileDialog(this, "��Mp3�ļ�", FileDialog.LOAD);
	   fileDialog.setDirectory(currentDirectory);
	   fileDialog.setVisible(true);
	   fileDialog.setMultipleMode(true);
	   currentDirectory = fileDialog.getDirectory();
	   File files[]=fileDialog.getFiles();
	   musicList.addMusic(files);
	  
	    	
	   // ����û�����ѡ���ļ��򷵻�
	   if (fileDialog.getFile() == null){
	    return;
	   }
	   
	   if (player != null){
	    // �ر��Ѿ�����JMF����������
	    player.close();
	   }
	   try {
	    // ����һ����ѡ���ļ��Ĳ�����
	    player = Manager.createPlayer(new MediaLocator("file:"
	      + fileDialog.getDirectory() + fileDialog.getFile()));
	    
		
		   
	   } catch (java.io.IOException e2) {
	    System.out.println(e2);
	    return;
	   } catch (NoPlayerException e2) {
	    System.out.println("�����ҵ�������.");
	    return;
	   }
	   if (player == null) {
	    System.out.println("�޷�����������.");
	    return;
	   }
	   first = false;
	   this.setTitle(fileDialog.getFile());
	   musicNameLabel.setText("���ڲ��ţ�"+fileDialog.getFile());
	   // �������Ŀ����¼�����
	   player.addControllerListener(this);
	   // Ԥ���ļ�����
	   player.prefetch();
	   
	  
	   musicList.setSelectedIndex(nextIndex++);
	   }
	   
	   if(e.getActionCommand().equals("������")) {
		   try {
			   FileDialog fileDialoglrc = new FileDialog(this, "�򿪸���ļ�.txt", FileDialog.LOAD);
			   fileDialoglrc.setDirectory(currentDirectory);
			   fileDialoglrc.setVisible(true);
			   currentDirectory = fileDialoglrc.getDirectory();
			   File musicText[]=fileDialoglrc.getFiles();
			   // ����û�����ѡ���ļ��򷵻�
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
	* ʵ��ControllerListener�ӿڵķ��������������Ŀ����¼�
	*/
	public void controllerUpdate(ControllerEvent e) {
	   // ����player.close()ʱControllerClosedEvent�¼����֡�
	   // ��������Ӿ���������ò���Ӧ�ò����Ϊһ�����,�Կ�����岿��Ҳִ��ͬ���Ĳ�����
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
	   // �����ý���ļ�����β���¼�
	   if (e instanceof EndOfMediaEvent) {
	    if (loop) {
	     // ����ѭ�������¿�ʼ����
	     player.setMediaTime(new Time(0));
	     player.start();
	    }
	    return;
	   }
	   
	   // ����ǲ�����Ԥ���¼�
	   if (e instanceof PrefetchCompleteEvent) {
	    player.start();
//	    System.out.println("1111111111");
	    return;
	   }
	   
	   // ������ļ�����ȫ�¼�������ʾ��Ƶ����Ϳ��������
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
