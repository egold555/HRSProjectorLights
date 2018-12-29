package org.golde.java.projectorlightshow.effects;

import org.golde.java.projectorlightshow.window.WindowProjection;

import controlP5.ControlP5;
import controlP5.Group;

public abstract class EffectBase {

	private WindowProjection window;
	private ControlP5 cp5;
	protected Group controlGroup;;
	

	public EffectBase(WindowProjection wp, int controlLocation) {
		this.window = wp;
		this.cp5 = window.getCp5();
		if(cp5 == null) {
			System.out.println("cp5 is null");
		}
		controlGroup = cp5.addGroup(getName()+"SettingsGroup").setPosition(10 + controlLocation * 420,195).setWidth(395).setHeight(30);
		controlGroup.disableCollapse();
		controlGroup.getCaptionLabel().set(getName()+" Settings").align(ControlP5.CENTER, ControlP5.CENTER);
		settings();
	}
	
	private float history_Rotate = 0; //Keeps track of how much we rotate, so we can reset it later
	private float[] history_translation = {0, 0}; //Keeps track of how much we translate, so we can reset it later

	private float frameRate;

	public abstract void draw();
	public abstract void settings();
	public abstract String getName();
	
	private boolean enabled;

	public final void rotate(float r) {
		window.rotate(r);
		history_Rotate = r;
	}

	public final void resetRotation() {
		window.rotate(-history_Rotate);
		history_Rotate = 0;
	}

	public final void translate(float x, float y) {
		window.translate(x, y);
		history_translation[0]+= x;
		history_translation[1]+= y;
	}

	public final void resetTranslation() {
		window.translate(-history_translation[0], -history_translation[1]);
		history_translation[0] = 0;
		history_translation[1] = 0;
	}

	public void resetWindow() {
		resetTranslation();
		resetRotation();
	}

	public final void refresh() { //This is called from the controller, it calls the correct functions in the right order
		frameRate = window.frameRate;
		draw();
		resetWindow();
	}

	public float getFrameRate() {
		return frameRate;
	}
	
	public WindowProjection getWindow() {
		return window;
	}
	
	public ControlP5 getCp5() {
		return cp5;
	}
	
	public boolean isHat()
	{
		return getLevel()>window.getWindowControls().minLevelSlider.getValue()?window.getWindowControls().bdFreq.isHat():false;
	}

	public boolean isSnare()
	{
		return getLevel()>window.getWindowControls().minLevelSlider.getValue()?window.getWindowControls().bdFreq.isSnare():false;
	}

	public boolean isKick()
	{
		return getLevel()>window.getWindowControls().minLevelSlider.getValue()?window.getWindowControls().bdFreq.isKick():false;
	}

	public boolean isOnset()
	{
		return getLevel()>window.getWindowControls().minLevelSlider.getValue()?window.getWindowControls().bdSound.isOnset():false;
	}
	
	//audio
	protected float getLevel() {
		return window.getWindowControls().in.mix.level();
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
