package com.example.du.deepPart;

/**
 * 
 * NK2023 Embedded System Design Body IV Design Deep Part
 * 
 * ListView items: choice for different clocks
 * 
 * 
 * @author achdu0000<achdu0000@163.com>
 * 
 */
public class ListViewItem {
	private int imageId;
	private String name;
	public ListViewItem(String name, int imageId) {
		this.name = name;
		this.imageId = imageId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getImageId() {
		return imageId;
	}
	public void setImageId(int imageId) {
		this.imageId = imageId;
	}
}
