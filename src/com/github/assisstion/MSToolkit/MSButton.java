package com.github.assisstion.MSToolkit;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import com.github.assisstion.MSToolkit.concurrent.CollectionSynchronizer;
import com.github.assisstion.MSToolkit.event.MSActionEvent;
import com.github.assisstion.MSToolkit.event.MSActionEventProcessor;
import com.github.assisstion.MSToolkit.event.MSActionHandler;
import com.github.assisstion.MSToolkit.event.MSActionListener;
import com.github.assisstion.MSToolkit.event.MSEvent;
import com.github.assisstion.MSToolkit.event.MSMouseEvent;
import com.github.assisstion.MSToolkit.event.MSMouseEventProcessor;
import com.github.assisstion.MSToolkit.event.MSMouseHandler;
import com.github.assisstion.MSToolkit.event.MSMouseListener;
import com.github.assisstion.MSToolkit.impl.MSHelper;
import com.github.assisstion.MSToolkit.style.MSStyleManager;

public class MSButton extends MSAbstractBoundedComponent implements MSMouseListener, MSMouseHandler, MSActionHandler, MSGraphicContextual{
	
	
	protected String text;
	private MSGraphicalContext graphicsContext;
	private Set<MSMouseListener> mouseListeners;
	protected CollectionSynchronizer<Set<MSMouseListener>, MSMouseListener> mouseListenerSync;
	private AtomicBoolean down = new AtomicBoolean(false);
	private Set<MSActionListener> actionListeners;
	protected CollectionSynchronizer<Set<MSActionListener>, MSActionListener> actionListenerSync;
	
	protected MSButton(){
		
	}
	
	public MSButton(int x, int y, String text){
		super(x, y);
		style = MSStyleManager.getDefaultStyleSystem().getButton();
		this.text = text;
		mouseListeners = new HashSet<MSMouseListener>();
		mouseListenerSync = new CollectionSynchronizer<Set<MSMouseListener>, 
				MSMouseListener>(mouseListeners);
		actionListeners = new HashSet<MSActionListener>();
		actionListenerSync = new CollectionSynchronizer<Set<MSActionListener>, 
				MSActionListener>(actionListeners);
	
	}
	
	@Override
	public boolean addTo(MSContainer c){
		c.addMSMouseListener(this);
		return true;
	}

	@Override
	public boolean removeFrom(MSContainer c){
		c.removeMSMouseListener(this);
		return true;
	}

	@Override
	public void render(MSGraphicalContext g, int x, int y){
		MSColor tempColor = g.getColor();
		MSFont tempFont = g.getFont();
		g.setFont(getStyle().getFont());
		g.setColor(down.get()?getStyle().getFrontBackground().darker():getStyle().getFrontBackground());
		g.fillRect(x, y, getWidth(), getHeight());
		g.setColor(getStyle().getForeground());
		g.drawRect(x, y, getWidth(), getHeight());
		g.setColor(getStyle().getForeground());
		g.drawString(text, x + getStyle().getPaddingLeft(), y + getStyle().getPaddingTop() + getStyle().getFont().getSize());
		g.setColor(tempColor);
		g.setFont(tempFont);
	}
	
	@Override
	public int getWidth(){
		return MSHelper.getTextWidth(getStyle().getFont(), text, graphicsContext) + getStyle().getPaddingLeft() + getStyle().getPaddingRight();
	}
	
	@Override
	public int getHeight(){
		return MSHelper.getTextHeight(getStyle().getFont(), text, graphicsContext) + getStyle().getPaddingTop() + getStyle().getPaddingBottom();
	}
	
	@Override
	public void setGraphicsContext(MSGraphicalContext graphicsContext){
		this.graphicsContext = graphicsContext;
	}
	
	@Override
	public MSGraphicalContext getGraphicsContext(){
		return graphicsContext;
	}

	@Override
	public void mousePressed(MSMouseEvent e){
		if(MSHelper.pointIn(getX(), getY(), getX()+getWidth(), getY()+getHeight(), e.getX(), e.getY())){
			setDown(true);
			processActionEvent(new MSActionEvent(this, e, false));
			processMouseEvent(e);
		}
	}

	@Override
	public void mouseReleased(MSMouseEvent e){
		setDown(false);
		if(MSHelper.pointIn(getX(), getY(), getX()+getWidth(), getY()+getHeight(), e.getX(), e.getY())){
			processActionEvent(new MSActionEvent(this, e, false));
			processMouseEvent(e);
		}
	}

	@Override
	public void mouseClicked(MSMouseEvent e){
		if(MSHelper.pointIn(getX(), getY(), getX()+getWidth(), getY()+getHeight(), e.getX(), e.getY())){
			processActionEvent(new MSActionEvent(this, e, true));
			processMouseEvent(e);
		}
	}

	@Override
	public boolean addMSMouseListener(MSMouseListener listener){
		if(!mouseListenerSync.contains(listener).get()){
			mouseListenerSync.add(listener);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean removeMSMouseListener(MSMouseListener listener){
		if(mouseListenerSync.contains(listener).get()){
			mouseListenerSync.remove(listener);
			return true;
		}
		return false;
	}
	
	protected void processMouseEvent(MSMouseEvent e){
		if(MSHelper.pointIn(getX(), getY(), getX()+getWidth(), getY()+getHeight(), e.getX(), e.getY())){
			String message = e.getMessage();
			new Thread(new MSMouseEventProcessor(new MSMouseEvent(this, message, e, e.getX()-x, e.getY()-y), mouseListenerSync)).start();
		}
	}
	
	protected void processActionEvent(MSActionEvent e){
		new Thread(new MSActionEventProcessor(e, actionListenerSync)).start();
	}
	
	public boolean isDown(){
		return down.get();
	}
	
	protected void setDown(boolean b){
		down.set(b);
		processActionEvent(new MSActionEvent(this, new MSEvent(this, "button state change; new state: " + b), false));
	}
	
	public void setText(String text){
		this.text = text;
		processActionEvent(new MSActionEvent(this, new MSEvent(this, "button text change; new text: " + text), false));
	}
	
	public String getText(){
		return text;
	}

	@Override
	public boolean addMSActionListener(MSActionListener listener){
		if(!actionListenerSync.contains(listener).get()){
			actionListenerSync.add(listener);
			return true;
		}
		return false;
	}

	@Override
	public boolean removeMSActionListener(MSActionListener listener){
		if(actionListenerSync.contains(listener).get()){
			actionListenerSync.remove(listener);
			return true;
		}
		return false;
	}
}
