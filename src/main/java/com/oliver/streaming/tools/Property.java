package com.oliver.streaming.tools;

public class Property {
	String title;
	String link;
	String description;
	String pubDate;
	String thumbnail;
	@Override
	public String toString() {
		return title + " " + link + " " +pubDate;
	}
}
