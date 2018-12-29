package org.golde.java.projectorlightshow.window;

import java.util.ArrayList;
import java.util.List;

import org.golde.java.projectorlightshow.effects.Effect8Dots;
import org.golde.java.projectorlightshow.effects.EffectBase;
import org.golde.java.projectorlightshow.effects.EffectSnow;

import controlP5.ControlP5;
import processing.core.PApplet;

public class WindowProjection extends PApplet {

	private final WindowControls windowControls;

	private final int DISPLAY;
	
	private EffectBase activeEffect;
	
	public WindowProjection(WindowControls windowControls, int display) {
		this.windowControls = windowControls;
		this.DISPLAY = display;
		
	}
	
	@Override
	public void settings() {
		pixelDensity(displayDensity());
		fullScreen(P2D, DISPLAY); //Makes this window fullscreen
	}

	@Override
	public void setup() {
		colorMode(HSB, 360, 100, 100); //Set the colors we draw in to be HSV
		try {
			blendMode(ADD);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void draw() {
		background(0); //Set the background to draw black
		noStroke();

		//Draw debug framerate text
		if(WindowControls.DEBUG) {
			fill(0, 100, 100); //HSV
			textSize(30);
			text(frameRate, 100, 100);
		}
		
		translate(width/2, height/2); //Move center point of the drawing to the center point of the window
		
		if(activeEffect != null) {
			activeEffect.refresh();
		}

	}
	
	public final int getMaxRadius(){
		return PApplet.parseInt(sqrt(sq(width)+sq(height)));
	}

	public final int getMinRadius(){
		return height < width ? height : width;
	}
	
	public static final void runInstance(WindowProjection instance) {
		String[] args = {"org.golde.java.projectorlightshow.window.WindowProjection"};
		PApplet.runSketch(args, instance);
	}
	
	public WindowControls getWindowControls() {
		return windowControls;
	}

	public ControlP5 getCp5() {
		return windowControls.cp5;
	}

	public void setActiveEffect(EffectBase activeEffect) {
		this.activeEffect = activeEffect;
	}
	
}