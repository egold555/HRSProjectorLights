package org.golde.java.projectorlightshow;

public class Beat {
	public Boolean hat;

	public Boolean snare;

	public Boolean kick;

	public Boolean onset;

	public float level;

	public Beat(Boolean h, Boolean s, Boolean k, Boolean o, float l)
	{
		hat = h;
		snare = s;
		kick = k;
		onset = o;
		level = l;
	}
}
