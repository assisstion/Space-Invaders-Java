package com.github.assisstion.spaceInvaders.menu;

//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.image.BufferedImage;
//import java.io.IOException;

//import javax.swing.ImageIcon;

import com.github.assisstion.spaceInvaders.AchievementMethods;
import com.github.assisstion.spaceInvaders.gameObject.Achievement;
import com.github.assisstion.spaceInvaders.menu.canvas.UpgradesCanvas;

import static com.github.assisstion.spaceInvaders.MainCanvas.*;
//import com.github.assisstion.spaceInvaders.ResourceManager;

@ReturnableMenu
public class UpgradesMenuBuilder implements MenuBuilder {
	private Menu parent;
	private LevelMenuBuilder levelScreen;
	private UpgradesCanvas canvas;
	private UpgradesMenuBuilder instance;
	
	public UpgradesMenuBuilder(LevelMenuBuilder leScreen){
		instance = this;
		levelScreen = leScreen;
	}
	
	@Override
	public void build(Menu menu) {
		AchievementMethods.redeemAchievement(new Achievement("Vanity"));
		parent = menu;
		parent.enableMenuKeyListener();
		parent.requestFocus();
		parent.revalidate();
		
		canvas = new UpgradesCanvas(this);
		canvas.setBounds(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		/*
		returnButton = new JButton(new ImageIcon(getImage("resources/returnButton.png")));
		returnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Button pressed");
				parent.closeMenu(instance);
				parent.addMenuBuilder(levelScreen);
			}
		});
		
		returnButton.setBounds(0,0,162,94);
		*/
		parent.add(canvas);
		//parent.add(returnButton);
		
	}

	@Override
	public void unBuild(Menu menu) {
		parent = menu;
		parent.disableMenuKeyListener();
		//parent.remove(returnButton);
		parent.remove(canvas);

	}

	@Override
	public void exitMenu() {
		parent.closeMenu(instance);
		parent.addMenuBuilder(levelScreen);	
	}
	
	public Menu getParent(){
		return parent;
	}

	/*
	private BufferedImage getImage(String filepath){
		BufferedImage leIcon = null;
		try {
			leIcon = ResourceManager.getImageResource(filepath);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error loading image!");
		}
		return leIcon;
	}
	*/
}
