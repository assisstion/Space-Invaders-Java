package com.github.assisstion.spaceInvaders.menu;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.assisstion.spaceInvaders.Engine;
import com.github.assisstion.spaceInvaders.Helper;
import com.github.assisstion.spaceInvaders.MainCanvas;
import com.github.assisstion.spaceInvaders.Scheduler;

public class Menu extends JPanel {

	public MenuBuilder currentMenu;
	private static final long serialVersionUID = 8162618142692095178L;
	private LinkedList<MenuBuilder> builders = new LinkedList<MenuBuilder>();
	private MenuKeyListener keyListener;
	public boolean started;

	public Menu() {
		setLayout(null);
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(960, 740));
		System.out.println("Menu built");
		MainCanvas.frame.pack();
		keyListener = new MenuKeyListener(this);
		addKeyListener(keyListener);
	}

	public synchronized void addMenuBuilder(MenuBuilder builder) {
		if (keyListener == null) {
			keyListener = new MenuKeyListener(this);
			addKeyListener(keyListener);
		}
		currentMenu = builder;
		builders.add(builder);
		builder.build(this);
		if(builder instanceof Scheduler){
			Scheduler s = (Scheduler) builder;
			s.setService(Helper.newService(3));
			s.startService();
		}
		requestFocus();
		revalidate();
		repaint();
	}

	public synchronized void closeMenu(MenuBuilder builder) {
		builders.remove(builder);
		builder.unBuild(this);
		if(builder instanceof Scheduler){
			Scheduler s = (Scheduler) builder;
			s.getService().shutdown();
		}
		requestFocus();
		revalidate();
		repaint();
	}

	public synchronized void closeAllMenus() {
		MenuBuilder builder = builders.pollLast();
		while (builder != null) {
			builders.remove(builder);
			builder.unBuild(this);
			if(builder instanceof Scheduler){
				Scheduler s = (Scheduler) builder;
				s.getService().shutdown();
			}
			builder = builders.pollLast();
		}
		revalidate();
		repaint();
	}

	public void enableMenuKeyListener() {
		addKeyListener(keyListener);
	}

	public void disableMenuKeyListener() {
		removeKeyListener(keyListener);
	}

	/*
	 * Starts the game engine
	 */

	public static void centerLabel(JLabel label, int height) {
		if (label.getFont().isItalic()) {
			label.setBounds((int) (MainCanvas.FRAME_WIDTH / 2 - label
					.getPreferredSize().getWidth() / 2), height, (int) label
					.getPreferredSize().getWidth() + 10, (int) label
					.getPreferredSize().getHeight());
		} else {
			label.setBounds((int) (MainCanvas.FRAME_WIDTH / 2 - label
					.getPreferredSize().getWidth() / 2), height, (int) label
					.getPreferredSize().getWidth(), (int) label
					.getPreferredSize().getHeight());
		}
	}

	public synchronized void startGame() {
		synchronized (this) {
			if (started) {
				System.out.println("Engine already created");
				return;
			}
			started = true;
		}
		removeKeyListener(keyListener);
		keyListener = null;
		MainCanvas.engine = new Engine();
		add(MainCanvas.engine);
		MainCanvas.frame.pack();
		MainCanvas.isOn = true;
		MainCanvas.rand = new Random();
		MainCanvas.engine.state = "ready";
		System.out.println("Engine starting");
	}

	public synchronized void done() {
		started = false;
	}

	public synchronized boolean started() {
		return started;
	}
	
	@Override
	public void remove(Component comp){
		super.remove(comp);
		if(comp instanceof Scheduler){
			Scheduler s = (Scheduler) comp;
			s.getService().shutdown();
		}
	}
	
	@Override
	public Component add(Component comp){
		Component c = super.add(comp);
		if(comp instanceof Scheduler){
			Scheduler s = (Scheduler) comp;
			s.setService(Helper.newService(3));
			s.startService();
		}
		return c;
	}
}
