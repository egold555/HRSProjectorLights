package org.golde.java.projectorlightshow.effects;

import java.util.LinkedList;

import org.golde.java.projectorlightshow.window.WindowProjection;

import controlP5.ControlP5;
import controlP5.Slider;
import controlP5.Toggle;
import processing.core.PApplet;

public class EffectSnow extends EffectHueAutoChanger {

	private Slider radiusSlider, speedSlider;
	private LinkedList<Float> r, x, y;
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
		x = new LinkedList<Float>();
		y = new LinkedList<Float>();
		r = new LinkedList<Float>();
		for (int i=0;i<pts;i++) {
			r.add(i, getWindow().random(radiusSlider.getValue()/10, radiusSlider.getValue()));
			x.add(i, getWindow().random(0, radiusSlider.getValue()));
			y.add(i, getWindow().random(0, radiusSlider.getValue()));
		}
	}

	@Override
	public void draw() {
		super.draw();
		
		if (radiusSlider.isMousePressed()) {
			calcPoints();
		}
		else {
			translate(-getWindow().width/2-lauf*speedSlider.getValue()*10, -getWindow().height/2);
			for (int i=0;i<pts;i++) {
				float posx, posy;
				posx = (2*radiusSlider.getValue()*(i%px))+x.get(i);
				posy = (2*radiusSlider.getValue()*(i/px))+y.get(i);
				getWindow().fill(getHue()%360, 100, 100);
				getWindow().ellipse(posx, posy, r.get(i), r.get(i));
			}

			if (lauf>=(2*radiusSlider.getValue()/(speedSlider.getValue()*10))-1) {  
				x.add(x.remove());
				y.add(y.remove());
				r.add(r.remove());
				lauf=0;
			} 
			else
				lauf++;
		}
	}

	@Override
	public float getHueIncrementAmount() {
		return 0.1f;
	}

	

}
