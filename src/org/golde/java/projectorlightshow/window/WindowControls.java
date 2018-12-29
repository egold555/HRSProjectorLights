package org.golde.java.projectorlightshow.window;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

import org.golde.java.projectorlightshow.Beat;
import org.golde.java.projectorlightshow.effects.Effect8Dots;
import org.golde.java.projectorlightshow.effects.EffectSnow;

import controlP5.ControlP5;
import controlP5.Slider;
import ddf.minim.AudioSource;
import ddf.minim.Minim;
import ddf.minim.analysis.BeatDetect;
import processing.core.PApplet;

public class WindowControls extends PApplet {

	public static final boolean DEBUG = false;
	
	private WindowProjection windowProjectionFloor = new WindowProjection(this, 2);
	private WindowProjection windowProjectionDJ = new WindowProjection(this, 3);
	
	public AudioSource in;
	public BeatDetect bdFreq;
	public BeatDetect bdSound;
	Minim minim;
	
	public Slider minLevelSlider, beatDelaySlider;
	
	public ControlP5 cp5;
	
	float maxLevel = 0;
	float goalMaxLevel=0;
	
	LinkedList<Beat> beatHistory = new LinkedList<Beat>();
	
	@Override
	public void settings() {
		initAudioInput();
		pixelDensity(displayDensity());
		size(850, 570);
	}
	
	@Override
	public void setup() {
		colorMode(HSB, 360, 100, 100);
		initControls();
		
		WindowProjection.runInstance(windowProjectionFloor);
		WindowProjection.runInstance(windowProjectionDJ);
		
		Timer timer= new Timer();
		
		timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				windowProjectionFloor.setActiveEffect(new Effect8Dots(windowProjectionFloor, 0));
				
				timer.schedule(new TimerTask() {
					
					@Override
					public void run() {
						windowProjectionDJ.setActiveEffect(new EffectSnow(windowProjectionDJ, 1));
						
					}
				}, 2000);
				
			}
		}, 2000);
		
		
		
		
		
	}
	
	@Override
	public void draw() {
		beatDetect();
		background(25);
		
		noStroke();

		drawBeatBoard();
	}
	
	public void beatDetect()
	{
		bdSound.setSensitivity(PApplet.parseInt(beatDelaySlider.getValue()));
		bdFreq.setSensitivity(PApplet.parseInt(beatDelaySlider.getValue()));
		bdSound.detect(in.mix);
		bdFreq.detect(in.mix);
	}
	
	private void initAudioInput()
	{
		String msg = "No audio input found!\n\n"
				+ "Please check the audio settings on your current operating system.\n"
				+ "There must be at least one audio input activated.";

		minim = new Minim(this);
		in = minim.getLineIn(Minim.STEREO, 512);

		if(in == null)
		{
			JOptionPane.showMessageDialog(null, msg, "Device detection", JOptionPane.ERROR_MESSAGE);
			System.exit(1);
		}

		bdFreq = new BeatDetect(in.bufferSize(), in.sampleRate());
		bdSound = new BeatDetect();
	}
	
	public void initControls()
	{
		cp5 = new ControlP5(this);
		cp5.setFont(createFont("Monospace", 12 / displayDensity()));
		beatDelaySlider = cp5.addSlider("beatDelay").setSize(395, 20).setPosition(10, 134).setRange(10, 1000);
		beatDelaySlider.getCaptionLabel().set("Beat Delay (ms)").align(ControlP5.CENTER, ControlP5.CENTER);
		beatDelaySlider.setValue(200);

		minLevelSlider = cp5.addSlider("minLevel").setSize(10, 122).setPosition(354, 10).setRange(0, 1);
		minLevelSlider.setLabelVisible(false);
		minLevelSlider.setValue(0.1f);
	}
	
	public void drawBeatBoard()
	{
		fill(200, 100, 20);
		rect(10, 10, 354, 122);
		fill(120, 100, getLevel()>minLevelSlider.getValue()&&bdFreq.isHat() ? 100 : 20);
		rect(365, 10, 40, 40);
		fill(220, 100, getLevel()>minLevelSlider.getValue()&&bdFreq.isSnare() ? 100 : 20);
		rect(365, 51, 40, 40);
		fill(320, 100, getLevel()>minLevelSlider.getValue()&&bdFreq.isKick() ? 100 : 20);
		rect(365, 92, 40, 40);
		fill(0);

		textSize(32);
		textAlign(CENTER, CENTER);
		text("H", 385, 26);
		text("S", 385, 67);
		text("K", 385, 108);
		textAlign(LEFT, BOTTOM);
		drawBeatHistory(beatHistory, 10, 110);
	}

	/** Draws a beat Visualisation.
	 * @param LinkedList<Bea>, Integer, Integer
	 *
	 *
	 */
	public void drawBeatHistory(LinkedList<Beat> history, int x, int y)
	{
		history.add(new Beat(isHat(), isSnare(), isKick(), isOnset(), getLevel()));
		goalMaxLevel=0;
		for (int i=0; i < history.size(); i++) {
			Beat b = history.get(i);
			if (b.level>goalMaxLevel)
				goalMaxLevel=b.level;
		}
		if (maxLevel<goalMaxLevel)maxLevel+=(goalMaxLevel-maxLevel)/2;
		for (int i=0; i < history.size(); i++) {
			Beat b = history.get(i);

			if (b.hat)
				stroke(120, 100, 100);
			else if (b.snare)
				stroke(220, 100, 100);
			else if (b.kick)
				stroke(320, 100, 100);
			else if (b.onset)
				stroke(0, 0, 100);
			else
				stroke(200, 100, 50);


			float d = 95*b.level/maxLevel;
			line(x+i, y-d, x+i, y);

			if (b.hat)
				stroke(120, 100, 30);
			else if (b.snare)
				stroke(220, 100, 30);
			else if (b.kick)
				stroke(320, 100, 30);
			else if (b.onset)
				stroke(0, 0, 30);
			else
				stroke(200, 100, 30);

			float e = 20*b.level/maxLevel;
			line(x+i, y+e, x+i, y);
		}
		if (history.size()>= 343)
			history.removeFirst();
		stroke(50, 20, 60);
		float n = (minLevelSlider.getValue()/maxLevel*95);
		float level = minLevelSlider.getValue();
		if (level*5 > maxLevel*4) {
			minLevelSlider.setValue(level*0.99f);
		} else if (level*5 < maxLevel) {
			minLevelSlider.setValue(level*1.01f);
		}
		if (n>98)
		{
			stroke(50, 20, 30);
			n=98;
		}
		line(x, y-n, x+343, y-n);
		stroke(0);
		maxLevel*=0.99f;
	}
	
	public boolean isHat()
	{
		return getLevel()>minLevelSlider.getValue()?bdFreq.isHat():false;
	}

	public boolean isSnare()
	{
		return getLevel()>minLevelSlider.getValue()?bdFreq.isSnare():false;
	}

	public boolean isKick()
	{
		return getLevel()>minLevelSlider.getValue()?bdFreq.isKick():false;
	}

	public boolean isOnset()
	{
		return getLevel()>minLevelSlider.getValue()?bdSound.isOnset():false;
	}

	public float getLevel()
	{
		if (in.mix.level()<0.0001f)return 0;
		return in.mix.level();
	}
	
}
