import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.io.*;

import javax.sound.sampled.*;


public class AudioCapture extends JPanel 
{
	int countt=0;
	int set=1;
	public boolean running;
	static JFrame f;
// static JScrollPane jScrollPane;
	class SRListner implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			if(countt==1)
			{
				running=true;
				countt=0;
			}
			startAudio();
			
		}
	}
	class StRListner implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			running=false;
			set=0;
		}
	}
	class pauListner implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			if(countt==0)
			{
				countt=1;
				running=false;
			}
			else 
			{
				countt=0;
			}
		}
	}
	
	public AudioCapture()
	{
		JButton SR = new JButton("Start Recording");
		JButton StR = new JButton("Stop recording");
		JButton pau = new JButton("Pause");
//    JScrollPane scroll=new JScrollPane();
		
		
		SRListner SRLis =new SRListner() ;
		SR.addActionListener(SRLis);
		add(SR);
		
		
		StRListner StRLis =new StRListner() ;
		StR.addActionListener(StRLis);
		add(StR);
		
		
		pauListner pauLis =new pauListner() ;
		pau.addActionListener(pauLis);
		add(pau);
	}	
	byte b[] = new byte[2]; 
	public void startAudio() 
	{
		try 
		{
			AudioFormat format = getFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
			TargetDataLine line = (TargetDataLine)AudioSystem.getLine(info);
			line.open(format);
			line.start();
			Runnable run = new Runnable() 
			{				
				public void run() 
				{
					if(set==1)
					{
						running = true;
						try
						{	
							while (running)
							{
								line.read(b, 0, b.length);
								k1=b[0]/128.0;
								k2=b[1]/128.0;
								j++;
								repaint();
							}
							line.stop();
							line.close();
						}
						catch (Exception e)
						{}
					}
				}
			};
			
			Thread SRThread = new Thread(run);
			SRThread.start();
		}
		catch (Exception e) 
		{}
	}
	static double k1,k2,prevk=0;
	static int j=0,o=0;
	public void paintComponent(Graphics g)
	{
		o++;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		if(o>=screenSize.width)
		{
			o=1;
			f.validate();
			f.repaint();
		}
		
		
		g.setFont(new Font("Times New Roman", Font.ITALIC,15));
		g.drawLine(50,50,40,50);
		g.drawString("1", 25,50+2);
		g.drawLine(50,650,40,650);
		g.drawString("-1", 20,650+2);//1
		
		
		g.drawLine(50,110,40,110);
		g.drawString("0.8", 18,110+5);
		g.drawLine(50,590,40,590);
		g.drawString("-0.8", 13,590+5);//.8
		
		
		g.drawLine(50,170,40,170);
		g.drawString("0.6", 18,170+5);
		g.drawLine(50,530,40,530);
		g.drawString("-0.6", 13,535);//.6
		
		
		g.drawLine(50,230,40,230);
		g.drawString("0.4", 18,230+5);
		g.drawLine(50,470,40,470);
		g.drawString("-0.4", 13,470+5);//.4
		
		
		g.drawLine(50,290,40,290);
//			g.drawString("0.2", 13,290+5);
		g.drawLine(50,410,40,410);
		g.drawString("-0.2", 13,410+5);//.2
		
		
		g.drawLine(46, 350, 50, 350);
		g.drawString("0", 35,350+5);//0
		
		
		g.setFont(new Font("Times New Roman", Font.ITALIC,30));
		g.setColor(Color.gray);
		g.drawString("Time ", 620, 550);
		g.drawLine( 750, 542,725,556 );
		g.drawLine(690, 542, 750, 542);
		g.drawLine(725,528 , 750, 542);
		
		
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform at = new AffineTransform();
		AffineTransform defaultAt = g2d.getTransform();
		at.rotate((-Math.PI) / 2);
		g2d.setTransform(at);
		g2d.drawString("Amplitude", -370, 30);
		g2d.setTransform(defaultAt);
		
		
		g.drawLine(23,240,23,180);
		g.drawLine(23,180,36,195);
		g.drawLine(10,195,23,180);
		
		
		g.drawLine(50, 350, 1450, 350);
		g.drawLine(50, 45, 50, 655);
		
		
		
		g.drawLine(50+o, 350-((int)(k1*300)),50+1+o, 350-((int)(k2*300)));
		g.drawLine(50+o, 350-((int)(prevk*300)),50+1+o, 350-((int)(k1*300)));
		prevk=k2;
	}
	private AudioFormat getFormat() 
	{
		float sampleRate = 8000;
		int sampleSizeInBits = 8;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = true;
		return new AudioFormat(sampleRate,sampleSizeInBits, channels, signed, bigEndian);
	}	
	public static void main(String args[]) 
	{
		AudioCapture c=new AudioCapture();
		f=new JFrame();
		f.setTitle("Audio File Graph");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		f.setSize(screenSize.width,screenSize.height-20);
		f.setVisible(true);
		f.setBackground(Color.cyan);
		f.setLocation(0,-26);
		f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
		f.setResizable(false);
		f.add(c);
	}
}