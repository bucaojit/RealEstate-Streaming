package com.oliver.streaming.tools;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class TruliaParser {
	
	public static void main(String[] str) {
	}
	
	public List<Property> processRSS(String input) {
		List<Property> properties = new ArrayList<Property>();

		try {
	      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	      DocumentBuilder builder = factory.newDocumentBuilder();
	      InputSource is = new InputSource();
	      is.setCharacterStream(new StringReader(input));
	      Document document = builder.parse(is);
	      NodeList nodes = document.getElementsByTagName("item");

	      for( int i = 0; i < nodes.getLength(); i++) {
	    	  Property prop = new Property();
	    	  Element element = (Element)nodes.item(i);
	    	  NodeList description = element.getElementsByTagName("description");
	    	  Element descline =  (Element) description.item(0);
	    	  prop.description = getCharacterDataFromElement(descline);
	    	  
	    	  NodeList title = element.getElementsByTagName("title");
	    	  Element titleline = (Element) title.item(0);
	    	  prop.title = getCharacterDataFromElement(titleline);
	    	  
	    	  NodeList link = element.getElementsByTagName("link");
	    	  Element linkline =  (Element) link.item(0);
	    	  prop.link = getCharacterDataFromElement(linkline);
	    	  
	    	  NodeList pubDate = element.getElementsByTagName("pubDate");
	    	  Element pubDateline =  (Element) pubDate.item(0);
	    	  prop.pubDate = getCharacterDataFromElement(pubDateline);
	    	  
	    	  NodeList thumbnail = element.getElementsByTagName("media:thumbnail");
	    	  Element thumbnailline =  (Element) thumbnail.item(0);
	    	  prop.thumbnail = getCharacterDataFromElement(thumbnailline);
	    	  
	    	  properties.add(prop);
	      	}
		      
	      }
	      catch (Exception ex) {
	    	  ex.printStackTrace();
	      }
	      /*
	      for(Property prop : properties) {
	    	  System.out.println(prop.title + " " + prop.pubDate);
	      }
	      */
	      return properties;
	      
	  }
	  public static String getCharacterDataFromElement(Element e) {
		    Node child = e.getFirstChild();
		    if (child instanceof CharacterData) {
		      CharacterData cd = (CharacterData) child;
		      return cd.getData();
		    }
		    return "";
	  }
}
