package com.automic.utils;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class XMLUtils {

	public XMLUtils(){
		
	}
	
	public static void addFolderToXMLFile(File xmlfile, String FolderPath) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException{
		
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance(); 
		domFactory.setIgnoringComments(true);
		DocumentBuilder builder = domFactory.newDocumentBuilder(); 
		Document doc = builder.parse(xmlfile); 

		//Text a = newRoot.createTextNode(FolderPath); 
		Element newRoot = doc.createElement("Folder");
		newRoot.setAttribute("Name", FolderPath.substring(4));
		newRoot.appendChild(doc.getFirstChild());
		doc.appendChild(newRoot);

		// output to wrapped.xml
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.transform(new DOMSource(doc), new StreamResult(xmlfile));
		
	}
	
	public static String getFolderFromXMLFile(File xmlfile) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException{
		
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance(); 
		domFactory.setIgnoringComments(true);
		DocumentBuilder builder = domFactory.newDocumentBuilder(); 
		Document doc = builder.parse(xmlfile); 
		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

		NodeList nList = doc.getElementsByTagName("Folder");
		
		Node nNode = nList.item(0);

		Element eElement = (Element) nNode;

		System.out.println("Name : " + eElement.getAttribute("Name"));
		return eElement.getAttribute("Name");
	
	}

}
