package com.mweis.game.box2d;

import com.mweis.game.util.Constants;

public final class Box2dConversionUtils {
	
	public static float pixelsToMeters (int pixels) {
		return (float)pixels * Constants.MPP;
	}

	public static int metersToPixels (float meters) {
		return (int)(meters * Constants.PPM);
	}
	
	private Box2dConversionUtils() { };
}
