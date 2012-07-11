package ca.mcgill.mcb.pcingola.snpSift.nextProt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

	public static boolean verbose = true;

	public static final String NODE_NAME_PROTEIN = "protein";
	public static final String NODE_NAME_GENE = "gene";
	public static final String NODE_NAME_TRANSCRIPT = "transcript";
	public static final String ATTR_NAME_UNIQUE_NAME = "uniqueName";
	public static final String ATTR_NAME_DATABASE = "database";
	public static final String ATTR_VALUE_ENSEMBL = "Ensembl";
	public static final String ATTR_NAME_ACCESSION = "accession";
	public static final String ATTR_NAME_ANNOTATION_LIST = "annotationList";

	boolean write = true;
	BufferedWriter outFile;

	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		//String xmlFileName = Gpr.HOME + "/y.xml";
		String xmlFileName = Gpr.HOME + "/snpEff/nextProt/xml/nextprot_chromosome_22.xml";
		String outputFileName = Gpr.HOME + "/snpEff/nextProt/zzz.txt";

		NextProt nextProt = new NextProt();
		nextProt.parse(xmlFileName, outputFileName);
	}

	/**
	 * Parse a node
	 * @param tabs
	 * @param node
	 */
	ArrayList<Node> findNodes(String tabs, Node node, String nodeName, String nodeValue, String attrName, String attrValue) {
		ArrayList<Node> resulstsList = new ArrayList<Node>();

		while (node != null) {
			boolean found = false;
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
			if ((attrName != null) || (attrValue != null)) { // Are attributes required? (don't parse if they are not needed
				NamedNodeMap map = node.getAttributes();
				if (map != null) {
					for (int i = 0; i < map.getLength(); i++) {
						Node attr = map.item(i);
						if (attrSb.length() > 0) attrSb.append(", ");

						String aname = attr.getNodeName();
						String aval = attr.getNodeValue();
						attrSb.append(aname + "=" + aval);

						if (((nodeName == null) || ((name != null) && name.equals(nodeName))) //
								&& ((nodeValue == null) || ((value != null) && value.equals(nodeValue))) //
								&& ((attrName == null) || ((aname != null) && attrName.equals(aname))) //
								&& ((attrValue == null) || ((aval != null) && attrValue.equals(aval))) //
						) found = true;
					}
				}
			} else {
				//				Gpr.debug("name: '" + name + "'\tvalue: '" + value + "'");
				if (((nodeName == null) || ((name != null) && name.equals(nodeName))) //
						&& ((nodeValue == null) || ((value != null) && value.equals(nodeValue))) //
				) {
					found = true;
				}
			}

			if (found) {
				resulstsList.add(node);
				if (verbose) Timer.showStdErr("Found: " + toString(node, ""));
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
				resulstsList.addAll(findNodes(tabs, nodeList, nodeName, nodeValue, attrName, attrValue));
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

		return resulstsList;
	}

	/**
	 * Parse a node list
	 * @param tabs
	 * @param nodeList
	 */
	List<Node> findNodes(String tabs, NodeList nodeList, String nodeName, String nodeValue, String attrName, String attrValue) {
		ArrayList<Node> resulstsList = new ArrayList<Node>();

		for (int temp = 0; temp < nodeList.getLength(); temp++) {
			Node node = nodeList.item(temp);
			resulstsList.addAll(findNodes(tabs, node, nodeName, nodeValue, attrName, attrValue));
		}

		return resulstsList;
	}

	/**
	 * Get an attribute from a node
	 * @param node
	 * @param attrName
	 * @return
	 */
	String getAttribute(Node node, String attrName) {
		NamedNodeMap map = node.getAttributes();
		if (map == null) return null;

		Node attrNode = map.getNamedItem(attrName);
		if (attrNode == null) return null;

		return attrNode.getNodeValue();
	}

	String getGeneId(Node node, String uniqueName) {
		// Find Ensembl gene ID
		String geneId = null;
		List<Node> ensemblGeneIds = findNodes("", node, NODE_NAME_GENE, null, ATTR_NAME_DATABASE, ATTR_VALUE_ENSEMBL);
		if (ensemblGeneIds.size() > 1) throw new RuntimeException("Multiple Ensembl Gene IDs found for protein '" + uniqueName + "'!");
		for (Node geneNode : ensemblGeneIds) {
			geneId = getAttribute(geneNode, ATTR_NAME_ACCESSION);
			Gpr.debug("\tGene:\t" + geneId);
			return geneId;
		}
		return null;
	}

	/**
	 * Get uniqueId -> EnsemblId mapping for transcripts
	 * @param node
	 * @return
	 */
	HashMap<String, String> getTrIds(Node node) {
		HashMap<String, String> trIdMap = new HashMap<String, String>();

		// Find Ensembl transcript ID
		List<Node> ensemblTrIds = findNodes("", node, NODE_NAME_TRANSCRIPT, null, ATTR_NAME_DATABASE, ATTR_VALUE_ENSEMBL);
		for (Node trNode : ensemblTrIds) {
			// Get Ensembl ID
			String trId = getAttribute(trNode, ATTR_NAME_ACCESSION);

			// Get Unique ID
			Node isoMap = trNode.getParentNode();
			String trUniqName = getAttribute(isoMap, ATTR_NAME_UNIQUE_NAME);

			// Add to map
			trIdMap.put(trUniqName, trId);
			Gpr.debug("\tTr:\t" + trId + "\t <-> " + trUniqName);
		}

		return trIdMap;
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
	void parse(String xmlFileName, String outFileName) {
		try {
			//---
			// Load document
			//---
			Timer.showStdErr("Reading file:" + xmlFileName);
			File fXmlFile = new File(xmlFileName);
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fXmlFile);
			doc.getDocumentElement().normalize();

			//---
			// Parse nodes
			//---
			Timer.showStdErr("Parsing XML data.");
			outFile = new BufferedWriter(new FileWriter(outFileName));

			List<Node> nodeList = findNodes("", doc.getChildNodes(), NODE_NAME_PROTEIN, null, null, null);
			Timer.showStdErr("Found " + nodeList.size() + " protein nodes");
			verbose = false;

			// Parse each node
			for (Node node : nodeList) {
				String uniqueName = getAttribute(node, ATTR_NAME_UNIQUE_NAME);
				Gpr.debug("Protein: " + uniqueName);

				// Find Ensembl gene ID
				String geneId = getGeneId(node, uniqueName);
				if (geneId != null) {
					HashMap<String, String> trIdMap = getTrIds(node);

					// Get attributes
					if (!trIdMap.isEmpty()) {

					}
				}
			}

			outFile.close();
			Timer.showStdErr("Done. Data written to " + outFileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Show a node as a string
	 * @param node
	 * @param tabs
	 * @return
	 */
	String toString(Node node, String tabs) {
		StringBuilder sb = new StringBuilder();

		//---
		// Get name & value
		//---
		String name = node.getNodeName();
		String value = node.getNodeValue();
		if (value != null) value = value.replace('\n', ' ').trim();
		sb.append(tabs + name);

		//---
		// Get attributes
		//---
		NamedNodeMap map = node.getAttributes();
		if (map != null) {
			sb.append("( ");
			for (int i = 0; i < map.getLength(); i++) {
				Node attr = map.item(i);
				String aname = attr.getNodeName();
				String aval = attr.getNodeValue();

				if (i > 0) sb.append(", ");
				sb.append(aname + "='" + aval + "'");
			}
			sb.append(" )");
		}

		if (value != null) sb.append(" = '" + value + "'\n");

		return sb.toString();
	}
}
