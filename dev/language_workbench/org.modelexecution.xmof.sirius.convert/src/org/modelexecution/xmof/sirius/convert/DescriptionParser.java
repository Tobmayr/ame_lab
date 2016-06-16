package org.modelexecution.xmof.sirius.convert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
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
import org.xml.sax.SAXException;

public class DescriptionParser {
	private Document description;
	private File outputFile;
	private Element root;
	private List<Element> delList = new ArrayList<>();

	private String[] xmofMmodel = { "platform:/resource/org.modelexecution.xmof/model/xmof.ecore#/",
			"platform:/resource/org.modelexecution.xmof/model/xmof.ecore#//Syntax",
			"platform:/resource/org.modelexecution.xmof/model/xmof.ecore#//Semantics",
			"platform:/resource/org.modelexecution.xmof/model/xmof.ecore#//Syntax/CommonBehaviors",
			"platform:/resource/org.modelexecution.xmof/model/xmof.ecore#//Syntax/Classes",
			"platform:/resource/org.modelexecution.xmof/model/xmof.ecore#//Syntax/Activities",
			"platform:/resource/org.modelexecution.xmof/model/xmof.ecore#//Syntax/Actions",
			"platform:/resource/org.modelexecution.xmof/model/xmof.ecore#//Semantics/Classes",
			"platform:/resource/org.modelexecution.xmof/model/xmof.ecore#//Semantics/Loci",
			"platform:/resource/org.modelexecution.xmof/model/xmof.ecore#//Semantics/CommonBehaviors" };

	public DescriptionParser(String inputDescFile, String outputDescFile) {
		File inputFile = new File(inputDescFile);
		outputFile = new File(outputDescFile);
		if (!outputFile.exists()) {
			outputFile.mkdirs();
		}
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			description = dBuilder.parse(inputFile);
			description.getDocumentElement().normalize();

		} catch (ParserConfigurationException | SAXException | IOException e) {
			System.err.println("Initialization not successful! DescriptionParser doesn't work porperly");
			e.printStackTrace();
		}

	}

	private void saveDocumentInFile() {
		Transformer transformer;
		try {
			transformer = TransformerFactory.newInstance().newTransformer();
			Result output = new StreamResult(outputFile);
			Source input = new DOMSource(description);
			transformer.transform(input, output);
		} catch (TransformerFactoryConfigurationError | TransformerException e) {
			System.err.println("Adapted description have not been saved succefully!");
			e.printStackTrace();
		}

	}

	public void convertToXMOFDescription() {
		root = description.getDocumentElement();
		adaptDescription();
		root.setAttribute("name", "xMOF modelers");
		root.setAttribute("documentation", "");
		saveDocumentInFile();
	}

	private void adaptDescription() {
		NodeList list = root.getElementsByTagName("ownedViewpoints");
		for (int i = 0; i < list.getLength(); i++) {
			validateViewpoint((Element) list.item(i));
		}
		removeUnusedNodes();

	}

	private void validateViewpoint(Element node) {
		if (node.getAttribute("name").equals("Design")) {
			adaptViewpoint(node);
		} else if(!node.getAttribute("name").equals("Reused")) {
			delList.add(node);
		}

	}

	private void adaptViewpoint(Element designPoint) {
		designPoint.setAttribute("modelFileExtension", "xmof");
		NodeList list = designPoint.getElementsByTagName("ownedRepresentations");
		for (int i = 0; i < list.getLength(); i++) {
			validateRepresentation((Element) list.item(i));

		}

	}

	private void validateRepresentation(Element node) {
		if (node.getAttribute("name").equals("Activity Diagram")) {
			adaptRepresentation(node);
		} else {
			delList.add(node);
		}

	}

	private void adaptRepresentation(Element node) {
		node.setAttribute("domainClass", node.getAttribute("domainClass").replace("uml", "IntermediateActivities"));
		changeMetamodel(node);
		NodeList list = node.getChildNodes();
		
	}

	private void changeMetamodel(Element node) {
		NodeList list = node.getElementsByTagName("metamodel");
		for (int i = 0; i < list.getLength(); i++) {
			delList.add((Element) list.item(i));
		}
		for (String s:xmofMmodel){
			Element metamodel = description.createElement("metamodel");
			metamodel.setAttribute("href", s);
			node.appendChild(metamodel);
		}
	

	}

	private void removeUnusedNodes() {
		for (Element e : delList) {
			e.getParentNode().removeChild(e);
		}

	}
}
