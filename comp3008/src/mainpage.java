import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
public class mainpage extends JFrame {
	
	String[] img=new String[16];
	String[] type= {"Email","Banking","Shopping"};
	
	JPanel main=new JPanel();

	Random r=new Random();

	
    JPanel pPanel = new JPanel(new GridLayout(4,4));//image part
    JPanel rPanel = new JPanel(new BorderLayout());//radio button part
    JPanel fPanel = new JPanel(new GridLayout(2,3));//functional part

//image part	
  //  JPanel[][] panelHolder = new JPanel[4][4]; 
    File[] source=new File("img").listFiles();
    int[] sour= {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
	JLabel[] il=new JLabel[16];
	ImageIcon[] ii=new ImageIcon[16];
	
//radiobutton part
    ButtonGroup bg=new ButtonGroup();
    JRadioButton fb=new JRadioButton();
    JRadioButton sb=new JRadioButton();

	
	//functional part
	JLabel current=new JLabel();
	JLabel message=new JLabel();
	JButton confirm=new JButton("confirm");
	JButton retry=new JButton("retry");//retry current type password
	JButton restart=new JButton("restart");
	JButton again=new JButton("again");//show real password again
	
	//password
	int[][] result=new int[3][6];
	int[] users=new int[5];//pictures result
	int rr=0;//radiobutton reault
	
	//for testing
	ArrayList<ArrayList<String>> user=new ArrayList<>();
	ArrayList<String> fir=new ArrayList<>();
	ArrayList<String> sec=new ArrayList<>();
	ArrayList<String> thi=new ArrayList<>();
	private static int tc=0;//0-5
	private static int curt=0;//current tutorial 0-2
	Color[] cc= {Color.BLACK,Color.BLUE,Color.CYAN,Color.GREEN,Color.ORANGE};
	
	//flags
	boolean ir=false;//image ready, indicate if the images need to be shuffled
	boolean ip=false;//in progress, indicate if the user should input a password
	boolean pf=false;//picture part finished, indicate if the user has selected enough pictures
	boolean rf=false;//radiobutton part finished, indicate if the user has selected radio part
	boolean ng=true;//need generate, indicate if a new password result need to be generated
	int attempt=0;//times of attempt of a password
	int ct=0;//current type, 0-2 one from type
	int[] sh= {0,1,2};
	int ni=0;//number of input, 0-5 how many pictures the user has picked

	
    public mainpage() {
    	if(ng) {
    		generate();
    		ng=false;
    	}
    	

      //  image
        pPanel.setPreferredSize(new Dimension(600,480));
        refreshImage();
               
        //radiobutton part
        rPanel.setPreferredSize(new Dimension(600,50));
        
        refreshRadio();
        
        //functional part
        fPanel.setPreferredSize(new Dimension(600,70));
        refreshFunctional();
        
    	if(!ip) {
			restart.setEnabled(false);
			again.setEnabled(false);
			confirm.setEnabled(false);
    		tutorial();
    	}
        main.add(pPanel,BorderLayout.NORTH);
        main.add(rPanel,BorderLayout.CENTER);
        main.add(fPanel,BorderLayout.SOUTH);
        add(main);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setVisible(true);
    }

    public void check() {//check if the user finishing a single password
    	if(pf&&rf) {
    		if(ct==0) {
    			fir.add(users[0]+","+users[1]+","+users[2]+","+users[3]+","+users[4]+","+rr);
    		}
    		else if(ct==1) {
    			sec.add(users[0]+","+users[1]+","+users[2]+","+users[3]+","+users[4]+","+rr);
    		}
    		else {
    			thi.add(users[0]+","+users[1]+","+users[2]+","+users[3]+","+users[4]+","+rr);
    		}
    		//message.setText(+result[sh[ct]][0]+","+result[sh[ct]][1]+","+result[sh[ct]][2]+","+result[sh[ct]][3]+","+result[sh[ct]][4]+","+result[sh[ct]][5]);
    		bg.clearSelection();
    		pf=false;
    		rf=false;
    		ni=0;
    		if(compareP()) {
    			message.setText("Correct!!!");
    			attempt=0;
    			ct++;//result correct
    		}
    		else if(attempt==3) {
    			message.setText("Incorrect!");
    			attempt=0;
    			ct++;//the user used up all 3 chances for this password
    		}
    		else {
    			message.setText("Incorrect! "+(3-attempt)+" times left!");
    		}
    		if(ct==3) {
    			endGame();
    		}
    		else {
    			current.setText(type[sh[ct]]);
        		main.repaint();

    		}
    	}
    }
    public boolean compareP() {
    	for(int x=0;x<5;x++) {
    		if(users[x]!=result[sh[ct]][x]) {
    			attempt++;
    			return false;
    		}
    	}
    	if(rr!=result[sh[ct]][5]){
			attempt++;
			return false;
		}
		return true;
    }
    public void retryFunc() {
    	pf=false;
    	rf=false;
    	bg.clearSelection();
    	ni=0;
    }
    public void restartGame() {
    	user.add(fir);
    	user.add(sec);
    	user.add(thi);
    	JOptionPane.showMessageDialog(null, toString());//only for testing
    	
    	
    	ni=0;//number of input, 0-5 how many pictures the user has picked
    	//flags
    	ip=false;//in progress, indicate if the user should input a password
    	pf=false;//picture part finished, indicate if the user has selected enough pictures
    	rf=false;//radiobutton part finished, indicate if the user has selected radio part
    	attempt=0;
    	ct=0;    	
    	main.repaint();
    	message.setText("Start");
    	current.setText(type[ct]);
    	generate();
    	newImg();
    	main.repaint();
    	//main.repaint();
    	user=new ArrayList<>();
    	fir=new ArrayList<>();
    	sec=new ArrayList<>();
    	thi=new ArrayList<>();
		restart.setEnabled(false);
		again.setEnabled(false);
		confirm.setEnabled(false);
		curt=0;
    	tutorial();
    }
    public void newImg() {
    	shuffleArray(sour);
    	for(int x=0;x<16;x++) {
    		ii[x].setImage(new ImageIcon(source[sour[x]].listFiles()[r.nextInt(2)].getPath()).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
    		ii[x].getImage().flush();
    	}
    }
    public void endGame() {
    	user.add(fir);
    	user.add(sec);
    	user.add(thi);
    	JOptionPane.showMessageDialog(null, toString());
    	
    	ni=0;//number of input, 0-5 how many pictures the user has picked
    	//flags
    	ip=false;//in progress, indicate if the user should input a password
    	pf=false;//picture part finished, indicate if the user has selected enough pictures
    	rf=false;//radiobutton part finished, indicate if the user has selected radio part
    	attempt=0;
    	ct=0;    	
    	main.repaint();
    	message.setText("Start");
    	current.setText(type[ct]);
    	generate();
    	newImg();
    	user=new ArrayList<>();
    	fir=new ArrayList<>();
    	sec=new ArrayList<>();
    	thi=new ArrayList<>();
		restart.setEnabled(false);
		again.setEnabled(false);
		confirm.setEnabled(false);
		curt=0;
    	tutorial();
    }
   	public void refreshFunctional() {
   		confirm.addActionListener(new ActionListener() {
   			public void actionPerformed(ActionEvent e) {
   				if(!ip) {
   					curt++;
   					if(curt==3) {
   				    	ip=true;
   				    	bg.clearSelection();
   				    	shuffleArray(sh);
   				    	current.setText(type[sh[ct]]);
   				    	main.repaint();
   					}
   					else {
   						restart.setEnabled(false);
   						again.setEnabled(false);
   						confirm.setEnabled(false);
   						tutorial();
   					}
   				}
   				else check();
   			}
   		});
        fPanel.add(confirm);
        fPanel.add(current);
        restart.addActionListener(new ActionListener() {
   			public void actionPerformed(ActionEvent e) {
   				restartGame();
   			}
   		});
        fPanel.add(restart);
        retry.addActionListener(new ActionListener() {
   			public void actionPerformed(ActionEvent e) {
   				retryFunc();
   			}
   		});
        fPanel.add(retry);
        fPanel.add(message);
        again.addActionListener(new ActionListener() {
   			public void actionPerformed(ActionEvent e) {
   				if(!ip) {
   					restart.setEnabled(false);
   					again.setEnabled(false);
   					confirm.setEnabled(false);
   					tutorial();
   				}
   			}
   		});
        fPanel.add(again);
      //  current.setText(type[ct]);
   	}
    public void refreshImage() {//load images and listeners
    	if(!ir) {
    		ir=true;
    		shuffleArray(sour);
    	}
        for(int x=0;x<16;x++) {
    		ii[x]=new ImageIcon(new ImageIcon(source[sour[x]].listFiles()[r.nextInt(2)].getPath()).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
    		il[x]=new JLabel(ii[x]);
    		il[x].setOpaque(true);
    		il[x].setBorder(BorderFactory.createLineBorder(Color.GRAY));
    		il[x].setName(x+"");
    		il[x].addMouseListener(new MouseAdapter() {
    			public void mousePressed(MouseEvent e) {
    				if(ip&&!pf) {
    					((JLabel)e.getSource()).setOpaque(true);
    					((JLabel)e.getSource()).setBackground(Color.BLACK);
    					((JLabel)e.getSource()).repaint();
    				}
    			}
    			public void mouseReleased(MouseEvent e) {
    				if(ip&&!pf) {
    					users[ni]=Integer.parseInt(((JLabel)e.getSource()).getName());
    					ni++;
    					((JLabel)e.getSource()).setOpaque(false);
    					((JLabel)e.getSource()).repaint();
    					if(ni==5) {
    						pf=true;
    					}
    				}
    				//message.setText(((JLabel) e.getSource()).getName());
    				//if(((JLabel) e.getSource()).getName().equals(0+""))current.setText("bingo");
    			}
    		});
   // 		panelHolder[x/4][x%4]=new JPanel();
    //		panelHolder[x/4][x%4].add(il[x]);
    //		pPanel.add(panelHolder[x/4][x%4]);
    		pPanel.add(il[x]);
        }
    }
    public void tutorial() {
    	tc=0;
    	Timer t=new Timer();
    	t.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				restart.setEnabled(false);
				again.setEnabled(false);
				confirm.setEnabled(false);
				if(tc>0) {
					if((tc-1)%6==5) {
						if(result[curt][tc-1]==0) {
							fb.setOpaque(false);
						}
						else {
							sb.setOpaque(false);
						}
						main.repaint();
					}
					else {
						il[result[curt][tc-1]].setOpaque(false);
						main.repaint();

					}
				}
				if(tc==6) {
					t.cancel();
					pPanel.setFocusable(true);
					rPanel.setFocusable(true);
					restart.setEnabled(true);
					again.setEnabled(true);
					confirm.setEnabled(true);
					return;
				}
				current.setText(type[curt]);
				if(tc==5) {
					if(result[curt][tc]==0) {
						fb.setOpaque(true);
						fb.setBackground(Color.BLACK);
					}
					else {
						sb.setOpaque(true);
						sb.setBackground(Color.BLACK);
					}
				}
				else {
					il[result[curt][tc]].setOpaque(true);
					il[result[curt][tc]].setBackground(cc[tc]);
				}
				main.repaint();

				tc++;

			}
    		
    	}, 1500, 1500);

    	main.repaint();
    }
    public void refreshRadio() {

    	fb.setText("0");
    	fb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(ip) {
						rr=Integer.parseInt((((JRadioButton) e.getSource()).getText()));
						rf=true;
					}
			}
   	});
        fb.setPreferredSize(new Dimension(300,50));
        
        sb.setText("1");
    	sb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if(ip) {
						rr=Integer.parseInt((((JRadioButton) e.getSource()).getText()));
						rf=true;
					}
			}
   	});
        sb.setPreferredSize(new Dimension(300,50));
        bg.add(fb);
        bg.add(sb);
        rPanel.add(fb,BorderLayout.WEST);
        rPanel.add(sb,BorderLayout.EAST);
    }
    public void generate() {//generate the result
    	for(int x=0;x<3;x++) {
    		for(int y=0;y<5;y++) {
    			result[x][y]=r.nextInt(16);
    		}
    		result[x][5]=r.nextInt(2);
    	}
    }
    public String toString() {
    	String res="";
    	for(int x=0;x<3;x++) {
    		res=res+type[x]+": from system->\n";
    		for(int y=0;y<5;y++) {
    			res=res+result[x][y]+",";
    		}
    		res=res+result[x][5]+"\n from user->\n";
    		for(int y=0;y<user.get(x).size();y++) {
    			res=res+user.get(x).get(y)+"\n";
    		}
    	}
    	System.out.println(res);
    	return res;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable(){
            public void run() {
                new mainpage();
            }
        });
    }
    
    
    //from internet
    static void shuffleArray(int[] ar)
    {
      // If running on Java 6 or older, use `new Random()` on RHS here
      Random rnd = ThreadLocalRandom.current();
      for (int i = ar.length - 1; i > 0; i--)
      {
        int index = rnd.nextInt(i + 1);
        // Simple swap
        int a = ar[index];
        ar[index] = ar[i];
        ar[i] = a;
      }
    }
}