package com.github.assisstion.MSToolkit.impl;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import com.github.assisstion.MSToolkit.MSBasicFont;
import com.github.assisstion.MSToolkit.MSException;
import com.github.assisstion.MSToolkit.MSFont;
import com.github.assisstion.MSToolkit.MSGraphicalContext;
import com.github.assisstion.MSToolkit.style.MSStyleManager;
import com.github.assisstion.MSToolkit.style.MSStyleSystem;
import com.github.assisstion.MSToolkit.wrapper.MSFadingWrapper;
import com.github.assisstion.spaceInvaders.ResourceManager;

public final class MSHelper{
	
	private static boolean systemOn = true;
	
	private MSHelper(){
	
	}
	
	//Required Implementation
	public static int getTextWidth(MSFont f, String text, MSGraphicalContext graphicsContext){
		FontRenderContext frc = null;
		Graphics2D g2d = null;
		boolean b = false;
		if(graphicsContext == null || !(graphicsContext instanceof MSGraphicImpl)){
			b = true;
		}
		else{
			g2d = ((MSGraphicImpl)graphicsContext).getGraphics();
			if(g2d.getFontRenderContext() == null){
				b = true;
			}
		}
		if(b){
			frc = new FontRenderContext(new AffineTransform(), false, false);
		}
		else{
			frc = g2d.getFontRenderContext();
		}
		Font font = new Font(f.getName(), MSConverter.getFontModFromModifiers(f.getModifiers()), f.getSize());
		return (int) font.getStringBounds(text, frc).getWidth();
	}
	
	//Required Implementation
	public static int getTextHeight(MSFont f, String text, MSGraphicalContext graphicsContext){
		FontRenderContext frc = null;
		Graphics2D g2d = null;
		boolean b = false;
		if(graphicsContext == null || !(graphicsContext instanceof MSGraphicImpl)){
			b = true;
		}
		else{
			g2d = ((MSGraphicImpl)graphicsContext).getGraphics();
			if(g2d.getFontRenderContext() == null){
				b = true;
			}
		}
		if(b){
			frc = new FontRenderContext(new AffineTransform(), false, false);
		}
		else{
			frc = g2d.getFontRenderContext();
		}
		Font font = new Font(f.getName(), MSConverter.getFontModFromModifiers(f.getModifiers()), f.getSize());
		return (int) font.getStringBounds(text, frc).getHeight();
	}

	//Required Implementation
	public static BufferedImage getImage(String link){
		try{
			return ResourceManager.getImageResource(link);
		}
		catch(IOException e){
			throw new MSException("Unable to acquire image resource", e);
		}
	}
	
	//Required Implementation
	public static MSStyleSystem getDefaultStyleSystem(){
		return MSStyleManager.getStyleSystem("classic");
	}
	
	//Required Implementation
	public static boolean pointIn(int x1, int y1, int x2, int y2, int pointX, int pointY){
		if(pointX >= x1 && pointX <= x2 && pointY >= y1 && pointY <= y2){
			return true;
		}
		return false;
	}
	
	//Required Implementation
	public static boolean unmeaningfulActionEventsEnabled(){
		return true;
	}

	//Required Implementation
	public static MSFont getDefaultFont(){
		return new MSBasicFont("Calibri", 20);
	}
	
	//Required Implementation
	public static boolean isSystemOn(){
		return systemOn;
	}
	
	//Required Implementation
	public static void enableSystem(){
		systemOn = true;
	}
	
	//Required Implementation
	public static void disableSystem(){
		systemOn = false;
		MSFadingWrapper.stopAll();
	}
}
