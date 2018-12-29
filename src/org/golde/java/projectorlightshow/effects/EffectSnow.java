package org.golde.java.projectorlightshow.effects;

import java.util.LinkedList;

import org.golde.java.projectorlightshow.window.WindowProjection;

import controlP5.ControlP5;
import controlP5.Slider;
import controlP5.Toggle;
import processing.core.PApplet;

public class EffectSnow extends EffectHueAutoChanger {

	private Slider radiusSlider, speedSlider;
	private LinkedList<Dot> dots;
	private int px, py, pts;
	private int lauf = 0;
	
	public EffectSnow(WindowProjection wp, int controlLocation) {
		super(wp, controlLocation);
	}
	
	@Override
	public String getName() {
		return "Snow";
	}
	
	@Override
	public void settings() {
		radiusSlider = getCp5().addSlider("radius"+getName()).setGroup(controlGroup).setPosition(0, 5).setSize(395, 45);
		radiusSlider.setRange(50, 200).setValue(80);
		radiusSlider.getCaptionLabel().set("Size").align(ControlP5.RIGHT, ControlP5.CENTER);

		speedSlider = getCp5().addSlider("speed"+getName()).setRange(0.01f, 1).setValue(0.3f).setPosition(0, 55).setSize(395, 45).setGroup(controlGroup);
		speedSlider.getCaptionLabel().set("Speed").align(ControlP5.RIGHT, ControlP5.CENTER);

		calcPoints();
	}
	
	private void calcPoints() {
		px = PApplet.parseInt(getWindow().width/(2*radiusSlider.getValue()))+2;
		py = PApplet.parseInt(getWindow().height/(2*radiusSlider.getValue()))+1;
		pts = px*py;
		dots = new LinkedList<Dot>();
		for (int i=0;i<pts;i++) {
			incrementHue(360f / (pts - 1));
			float r = getWindow().random(radiusSlider.getValue()/10, radiusSlider.getValue());
			float x = getWindow().random(0, radiusSlider.getValue());
			float y = getWindow().random(0, radiusSlider.getValue());
			dots.add(new Dot(x, y, r, getHue()));
		}
	}

	@Override
	public void draw() {
		
		incrementHue(0.1f);
		
		if (radiusSlider.isMousePressed()) {
			calcPoints();
		}
		else {
			translate(-getWindow().width/2-lauf*speedSlider.getValue()*10, -getWindow().height/2);
			for (int i=0;i<pts;i++) {
				float posx, posy;
				posx = (2*radiusSlider.getValue()*(i%px))+dots.get(i).x;
				posy = (2*radiusSlider.getValue()*(i/px))+dots.get(i).y;
				getWindow().fill(getHue() + dots.get(i).hue%360, 100, 100);
				getWindow().ellipse(posx, posy, dots.get(i).r, dots.get(i).r);
			}

			if (lauf>=(2*radiusSlider.getValue()/(speedSlider.getValue()*10))-1) {  
				dots.add(dots.remove());
				lauf=0;
			} 
			else
				lauf++;
		}
	}
	
	private class Dot {
		public final float x, y, r, hue;
		
		public Dot(float x, float y, float r, float hue) {
			this.x = x;
			this.y = y;
			this.r = r;
			this.hue = hue;
		}
	}

}
