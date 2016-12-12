package com.oliver.streaming.tools;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/*
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
*/
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.w3c.dom.CharacterData;


public class ProcessXMLTest {

	public static void main(String[] str) {
		String url = "https://www.trulia.com/rss2/for_sale/San_Francisco,CA/";
		String output = null;
		List<Property> properties = new ArrayList<Property>();

		HttpClient client = new HttpClient();
		//HttpGet request = new HttpGet(url);
		GetMethod method = new GetMethod(url);

		try {
		      // Execute the method.
		      int statusCode = client.executeMethod(method);

		      if (statusCode != HttpStatus.SC_OK) {
		        System.err.println("Method failed: " + method.getStatusLine());
		      }

		      // Read the response body.
		      byte[] responseBody = method.getResponseBody();

		      // Deal with the response.
		      // Use caution: ensure correct character encoding and is not binary data
		      output = new String(responseBody);
		      //System.out.println(new String(responseBody));


		    } catch (HttpException e) {
		      System.err.println("Fatal protocol violation: " + e.getMessage());
		      e.printStackTrace();
		    } catch (IOException e) {
		      System.err.println("Fatal transport error: " + e.getMessage());
		      e.printStackTrace();
		    } finally {
		      // Release the connection.
		      method.releaseConnection();
		    }  
	      if (output == null) {
	    	  System.out.println("Did not get output correctly");
	    	  System.exit(1);
	      }
	      try {
		      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		      DocumentBuilder builder = factory.newDocumentBuilder();
		      InputSource is = new InputSource();
		      is.setCharacterStream(new StringReader(output));
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
	      
	      for(Property prop : properties) {
	    	  System.out.println(prop.title + " " + prop.pubDate);
	      }
	      
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
