package ca.mcgill.mcb.pcingola.snpSift.nextProt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ca.mcgill.mcb.pcingola.util.Gpr;
import ca.mcgill.mcb.pcingola.util.Timer;

/**
 * Parse NetxProt database
 * 
 * http://www.nextprot.org/
 * 
 * @author pablocingolani
 */
public class NextProt {

	boolean write = true;
	BufferedWriter outFile;

	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		//String xmlFileName = Gpr.HOME + "/y.xml";
		String xmlFileName = Gpr.HOME + "/nextProt/xml/nextprot_chromosome_6.xml";

		NextProt nextProt = new NextProt();
		nextProt.parse(xmlFileName);
	}

	/**
	 * Get node type as a string
	 * @param type
	 * @return
	 */
	String nodeType(short type) {
		switch (type) {
		case Node.ATTRIBUTE_NODE:
			return "ATTRIBUTE_NODE";
		case Node.CDATA_SECTION_NODE:
			return "CDATA_SECTION_NODE";
		case Node.COMMENT_NODE:
			return "COMMENT_NODE";
		case Node.DOCUMENT_FRAGMENT_NODE:
			return "DOCUMENT_FRAGMENT_NODE";
		case Node.DOCUMENT_NODE:
			return "DOCUMENT_NODE";
		case Node.DOCUMENT_POSITION_CONTAINED_BY:
			return "DOCUMENT_POSITION_CONTAINED_BY";
		case Node.DOCUMENT_TYPE_NODE:
			return "DOCUMENT_TYPE_NODE";
		case Node.ELEMENT_NODE:
			return "ELEMENT_NODE";
		case Node.ENTITY_NODE:
			return "ENTITY_NODE";
		case Node.ENTITY_REFERENCE_NODE:
			return "ENTITY_REFERENCE_NODE";
		case Node.NOTATION_NODE:
			return "NOTATION_NODE";
		case Node.PROCESSING_INSTRUCTION_NODE:
			return "PROCESSING_INSTRUCTION_NODE";
		case Node.TEXT_NODE:
			return "TEXT_NODE";
		default:
			throw new RuntimeException("Unknown");
		}
	}

	void out(String s) {
		if (!write) return;

		try {
			//System.out.println(s);
			if (outFile != null) outFile.write(s + "\n");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Parse an XML file
	 * @param xmlFileName
	 */
	void parse(String xmlFileName) {
		try {
			String outFileName = Gpr.HOME + "/zzz.txt";

			Timer.showStdErr("Reading file:" + xmlFileName);
			File fXmlFile = new File(xmlFileName);
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fXmlFile);

			Timer.showStdErr("Parsing XML data. Writing data to " + outFileName);
			doc.getDocumentElement().normalize();
			outFile = new BufferedWriter(new FileWriter(outFileName));
			parse("", doc.getChildNodes());
			outFile.close();
			Timer.showStdErr("Done!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Parse a node
	 * @param tabs
	 * @param node
	 */
	void parse(String tabs, Node node) {

		while (node != null) {
			short type = node.getNodeType();

			//---
			// Get name & value
			//---
			String name = node.getNodeName();
			String value = node.getNodeValue();
			if (value != null) value = value.replace('\n', ' ').trim();

			//---
			// Get attributes
			//---
			StringBuilder attrSb = new StringBuilder();
			NamedNodeMap map = node.getAttributes();
			if (map != null) {
				for (int i = 0; i < map.getLength(); i++) {
					Node attr = map.item(i);
					if (attrSb.length() > 0) attrSb.append(", ");

					String aname = attr.getNodeName();
					String aval = attr.getNodeValue();
					attrSb.append(aname + "=" + aval);

					if (name.equals("protein") && aname.equals("uniqueName")) {
						//						write = aval.equals("NX_Q5VV42");
						Timer.showStdErr("Protein: " + aval);
						if (aval.equals("NX_Q5VV42")) Timer.showStdErr("Found!");
					}
				}
			}

			//---
			// Show node
			//---
			switch (type) {
			case Node.ELEMENT_NODE:
				if (!tabs.isEmpty()) tabs += "->";
				tabs += name + (attrSb.length() > 0 ? "( " + attrSb + " )" : "");
				out(tabs);
				NodeList nodeList = node.getChildNodes();
				parse(tabs, nodeList);
				node = node.getNextSibling();
				break;
			case Node.TEXT_NODE:
				if (!value.isEmpty()) out(tabs + " = '" + value + "'");
				node = null;
				break;
			case Node.CDATA_SECTION_NODE:
				if (!value.isEmpty()) out(tabs + " = '" + value + "'");
				node = null;
				break;
			default:
				Gpr.debug(tabs + "Node type: " + nodeType(type) + ", Name: " + node.getNodeName() + ", Value: " + node.getNodeValue());
				node = null;
			}
		}
	}

	/**
	 * Parse a node list
	 * @param tabs
	 * @param nodeList
	 */
	void parse(String tabs, NodeList nodeList) {
		for (int temp = 0; temp < nodeList.getLength(); temp++) {
			Node node = nodeList.item(temp);
			parse(tabs, node);
		}
	}
}
