package com.github.assisstion.MSToolkit;

public interface MSSelectable{
	boolean select();
	boolean deselect();
	boolean isSelected();
	MSSelectableGroup getSelectableGroup();
	String getName();
}
