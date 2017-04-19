package com.automic.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
import org.w3c.dom.NamedNodeMap;
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
		Node FirstChild = doc.getFirstChild();
		//Text a = newRoot.createTextNode(FolderPath); 
		
		//Element textNode = doc.createElement("textNode");
		Element newRoot = doc.createElement("Folder");
		//newRoot.setAttribute("Name", FolderPath.substring(4));
		
		newRoot.appendChild(doc.createTextNode(FolderPath.substring(4)));
		//newRoot.appendChild(doc.getFirstChild());
		FirstChild.appendChild(newRoot);
		

		// output to wrapped.xml
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.transform(new DOMSource(doc), new StreamResult(xmlfile));
		
	}
	
	public static String getFolderFromXMLFile(File xmlfile) throws ParserConfigurationException, SAXException, IOException, TransformerFactoryConfigurationError, TransformerException{
		//System.out.println("DEBUG:" + xmlfile.getAbsolutePath());
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance(); 
		domFactory.setIgnoringComments(true);
		DocumentBuilder builder = domFactory.newDocumentBuilder(); 
		Document doc = builder.parse(xmlfile); 
		NodeList nList = doc.getElementsByTagName("Folder");
		if(nList.getLength()!=0){
			Node nNode = nList.item(0);
			NamedNodeMap  nnm = nNode.getAttributes();
			return nNode.getTextContent();
		}else{
			return "";
		}

	}

	public static String getObjectTypeFromXmlFile(String FilePath) throws IOException{
		File inputFile = new File(FilePath);

		BufferedReader reader = new BufferedReader(new FileReader(inputFile));

		String currentLine;
		boolean NextLineContainsType = false;
		while((currentLine = reader.readLine()) != null) {
		    // trim newline when comparing with lineToRemove
		    String trimmedLine = currentLine.trim();
		    if(trimmedLine.startsWith("<uc-export")){
		    	NextLineContainsType = true;
		    	continue;
		    }
		    if(NextLineContainsType){
		    	String tLine = currentLine.trim();
		    	return tLine.split(" ")[0].replace("<","");
		    }
		    
		}
		return "";
	}
	
	public static String getObjectNameFromXmlFile(String FilePath) throws IOException{
		File inputFile = new File(FilePath);

		BufferedReader reader = new BufferedReader(new FileReader(inputFile));

		String currentLine;

		while((currentLine = reader.readLine()) != null) {
		    // trim newline when comparing with lineToRemove
		    String trimmedLine = currentLine.trim();
		    if(trimmedLine.contains(" name=")){
		    	return trimmedLine.split("name=")[1].replaceAll(">","").replaceAll("\"","");
		    }

		}
		return "";
	}
	
	public static File stripFolderFromXmlFile(String FilePath) throws IOException{
		File inputFile = new File(FilePath);
		File tempFile = new File(FilePath+"_TMP.xml");

		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

		String lineToRemove = "<Folder>";
		String currentLine;

		while((currentLine = reader.readLine()) != null) {
		    // trim newline when comparing with lineToRemove
		    String trimmedLine = currentLine.trim();
		    if(trimmedLine.startsWith(lineToRemove)) continue;
		    writer.write(currentLine + System.getProperty("line.separator"));
		}
		writer.close(); 
		reader.close(); 
		boolean successful = tempFile.renameTo(inputFile);
		return tempFile;
	}
}
