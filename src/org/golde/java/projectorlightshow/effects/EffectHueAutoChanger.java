package org.golde.java.projectorlightshow.effects;

import org.golde.java.projectorlightshow.window.WindowProjection;

public abstract class EffectHueAutoChanger extends EffectBase {

	public EffectHueAutoChanger(WindowProjection wp, int controlLocation) {
		super(wp, controlLocation);
	}
	
	private float hue = 0;
	
	public abstract float getHueIncrementAmount();
	
	@Override
	public void draw() {
		
		incrementHue(getHueIncrementAmount());
		if(hue > 360) {
			hue = 0;
		}
		
	}
	
	public final float getHue() {
		return hue;
	}
	
	public final void incrementHue(float amount) {
		hue += amount;
	}

}
