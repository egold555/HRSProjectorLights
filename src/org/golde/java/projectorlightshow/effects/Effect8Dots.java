package org.golde.java.projectorlightshow.effects;

import org.golde.java.projectorlightshow.window.WindowProjection;

import controlP5.ControlP5;
import controlP5.Slider;
import processing.core.PApplet;

public class Effect8Dots extends EffectHueAutoChanger {

	public Effect8Dots(WindowProjection wp, int controlLocation) {
		super(wp, controlLocation);
	}

	private Slider weightSlider, speedSlider, pointSlider;
	
	private final boolean mirror = true;
	
	@Override
	public void settings() {
		weightSlider = getCp5().addSlider("weight"+getName()).setPosition(0, 5).setSize(395, 45).setRange(0, 200).setGroup(controlGroup);
		weightSlider.getCaptionLabel().set("Weight").align(ControlP5.RIGHT, ControlP5.CENTER);
		weightSlider.setValue(80);

		speedSlider = getCp5().addSlider("speed"+getName()).setPosition(0, 55).setSize(395, 45).setRange(0, 1).setGroup(controlGroup);
		speedSlider.getCaptionLabel().set("speed").align(ControlP5.RIGHT, ControlP5.CENTER);
		speedSlider.setValue(0.5f);

		pointSlider = getCp5().addSlider("point"+getName()).setPosition(0, 105).setSize(395, 45).setRange(1, 10).setGroup(controlGroup);
		pointSlider.getCaptionLabel().set("points").align(ControlP5.RIGHT, ControlP5.CENTER);
		pointSlider.setValue(4);
	}
	
	int weight, points;
	float speed;
	float rotation = 0;
	boolean moving = false;
	
	@Override
	public void draw() {
		super.draw();
		weight = PApplet.parseInt(weightSlider.getValue());
		points = PApplet.parseInt(pointSlider.getValue());

		speed = speedSlider.getValue();

		float width = getWindow().width-weight;
		float height = getWindow().height-weight;
		
		if(isKick()) {
			incrementHue(10);
		}

		translate(-getWindow().width/2, -getWindow().height/2);

		for (int i=1;i<=points;i++)
		{
			float posx = weight/2+i * width/(points+1);
			float rotx = width/(points+1) * PApplet.cos(rotation);
			float posy = weight/2+height/3;
			float roty = -height/3 * PApplet.sin(rotation);

			getWindow().fill(getHue()%360, 100, 100);

			getWindow().ellipse(posx+rotx, posy+roty, weight*0.9f, weight*0.9f);

			if (mirror)
				getWindow().ellipse(posx-rotx, posy+roty, weight*0.9f, weight*0.9f);

			getWindow().fill((getHue()+120)%360, 100, 100);

			getWindow().ellipse(posx+rotx, height/3 + posy-roty, weight*0.9f, weight*0.9f);

			if (mirror)
				getWindow().ellipse(posx-rotx, height/3 + posy-roty, weight*0.9f, weight*0.9f);

		}

		if (rotation%(PApplet.PI/2)>0.1f) {
			moving = false;
			rotation = rotation + speed/10%(2*PApplet.PI);
		}
		else if (rotation%(PApplet.PI/2)<=0.1f && (isKick() || isSnare() || isOnset() || moving)) {
			moving = true;
			rotation = rotation + speed/10%(2*PApplet.PI);
		} 
		else
		{
			rotation = rotation + getLevel()/100%(2*PApplet.PI);
			moving = false;
		}
	}

	@Override
	public String getName() {
		return "8Dots";
	}

	@Override
	public float getHueIncrementAmount() {
		return 0.1f;
	}

}
