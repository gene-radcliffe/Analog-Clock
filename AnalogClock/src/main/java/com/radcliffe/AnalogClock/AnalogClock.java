package com.radcliffe.AnalogClock;


import java.awt.Graphics;
import javax.swing.*;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import com.radcliffe.forms.*;
import java.util.*;


public class AnalogClock extends JComponent implements Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double cx,cy;
	private int radius=100;
	private double lx,ly;
	private Font cfont;
	private static double[] mTable = new double[360];
	private WinForm mForm;
	private static int tick =0;
	private static int mtick=0;
	private static int htick =0;
	private Thread cthread;

	private boolean stop=true;
	static Calendar cTime;
	
	public AnalogClock(){
		
		mForm = new WinForm("My App", new Dimension(400,400));
		mForm.add(this);
		mForm.showWindow(true);
		cfont = new Font("Impact",Font.PLAIN, 25);
		cthread = new Thread(this);
		cthread.start();
	}
	public static void main(String args[]){
		cTime = Calendar.getInstance();
		ArrangeF();
		setToCurrentTime();
	new AnalogClock();
	
	}
	private  static void setToCurrentTime(){
		float prod=0.0f;
		
	
		htick = (30 * cTime.get(Calendar.HOUR));
		mtick =360;
		prod = cTime.get(Calendar.MINUTE) /60.0f;
		mtick = (int)(mtick* prod);
	
		if(mtick>=90 & mtick<180){
			htick +=10;
		}else if(mtick>=180 & mtick <270){
			htick +=20;
		}else if(mtick>=270 & mtick <360){
			//htick +=30;
		}
	
		tick = 360 *(cTime.get(Calendar.SECOND)/60);
		
	}
	private void drawSecondHand(Graphics2D g2){
		double x2,y2;
		/*
		 * if the ticker reaches 0 (9'o clock) then set to 359 to continue past
		 * 9
		 */
		
		//second hand
		x2 = Math.cos(mTable[tick]);
		y2 = Math.sin(mTable[tick]);
		x2 = 70 * x2;
		y2 = 70 * y2;
	
		x2 = 175+x2;
		y2 = 175 +y2;
		
		
		g2.setColor(Color.blue);
		g2.drawLine(175, 175,(int) y2, (int)x2);
	
	}// end drawSecondHand
	public void setClockFont(Font font){
		cfont=font;
	}
	private void drawHourHand(Graphics2D g2){
		double x2,y2;
		
		//hour hand
		x2 = Math.cos(mTable[htick]);
		y2 = Math.sin(mTable[htick]);
		x2 = 95 * x2;
		y2 = 95 * y2;
	
		x2 = 175+x2;
		y2 = 175 +y2;
		

		g2.setColor(Color.black);
	
		g2.drawLine(175, 175,(int) y2, (int)x2);

	}
	private void drawMinuteHand(Graphics2D g2){
		double x2,y2;
		
		//hour hand
		x2 = Math.cos(mTable[mtick]);
		y2 = Math.sin(mTable[mtick]);
		x2 = 85 * x2;
		y2 = 85 * y2;
	
		x2 = 175+x2;
		y2 = 175 +y2;
		

		g2.setColor(Color.red);
	
		g2.drawLine(175, 175,(int) y2, (int)x2);

	}
	private void drawFace(Graphics2D g2){
		char a=0;
		String str ="";
		g2.setFont(cfont);
		for(int x=0; x<360; x+=30){
			
			if(a==0){
				str =Integer.toString(12);
			}else{
			str = Integer.toString(a);
			}
			cx = Math.cos(mTable[x]);
			cy = Math.sin(mTable[x]);
			cx = radius * cx;
			cy = radius * cy;
			lx = 175 + cx;
			ly = 175 +cy;
			g2.setColor(Color.black);
			g2.drawString(str, (float)ly,(float) lx);
		
			a++;
		}
	}
/*
 * rearranges the Radian table to orient the clock face and make it
 * readable.
 */
	private static void ArrangeF(){
		int a=0;
		
		for(int x=180; x>=0; x--){
			
			mTable[a]=Math.toRadians(x);
			a++;
		}
		for(int x=359; x>=181; x--){
			
			mTable[a]=Math.toRadians(x);
			a++;
		}
		
	}
	@Override
	protected void paintComponent(Graphics arg0) {
		// TODO Auto-generated method stub
		super.paintComponent(arg0);
		Graphics2D g2 = (Graphics2D)arg0;
		drawFace(g2);
		drawSecondHand(g2);
		drawMinuteHand(g2);
		drawHourHand(g2);
	}
	private void clockLogic(){
	
		try{
			Thread.sleep(10);
			tick+=6;
			
			//resets the second hand to 0 degrees when it completes 60 seconds
			if(tick==360){
				tick=0;
				
			} 
				
			if(tick==0){
				mtick+=6;
				//adds 10 degrees every 15 minutes to simulate movement of an analog clock
				//if(((mtick==90) | (mtick==180) | (mtick==270))){
				if(((mtick==90) | (mtick==180))){
					htick+=10;
					
				}
				
			}
			//reset the minute hand to 0 degrees everytime it completes 1 hour
			if(mtick==360){
				mtick=0;
				htick+=10;//adds final 10 degrees to the hour hand when we arrive at the top of the hour
				
			}
			//resets the hour hand to 0 degrees when the clock has completed 12 hours
			if(htick==360){
				htick=0;
			}
			
			this.repaint();//repaint everything!!!
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	@Override
	public void run(){
		do{
			if(tick==360){
				stop =false;
			}	
	
			clockLogic();
			
		
		}while(stop);
	}
}
