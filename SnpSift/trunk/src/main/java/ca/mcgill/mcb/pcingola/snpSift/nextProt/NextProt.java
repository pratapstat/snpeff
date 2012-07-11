package ca.mcgill.mcb.pcingola.snpSift.nextProt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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

	public static boolean verbose = false;

	// We don't care about these categories
	public static final String CATAGORY_BLACK_LIST_STR[] = { "sequence variant", "sequence conflict", "mature protein" };

	public static final String NODE_NAME_PROTEIN = "protein";
	public static final String NODE_NAME_GENE = "gene";
	public static final String NODE_NAME_TRANSCRIPT = "transcript";
	public static final String NODE_NAME_ANNOTATION = "annotation";
	public static final String NODE_NAME_ANNOTATION_LIST = "annotationList";
	public static final String NODE_NAME_POSITION = "position";
	public static final String NODE_NAME_DESCRIPTION = "description";
	public static final String NODE_NAME_CVNAME = "cvName";
	public static final String NODE_NAME_SEQUENCE = "sequence";

	public static final String ATTR_NAME_UNIQUE_NAME = "uniqueName";
	public static final String ATTR_NAME_DATABASE = "database";
	public static final String ATTR_NAME_ACCESSION = "accession";
	public static final String ATTR_NAME_ANNOTATION_LIST = "annotationList";
	public static final String ATTR_NAME_CATAGORY = "category";
	public static final String ATTR_NAME_FIRST = "first";
	public static final String ATTR_NAME_LAST = "last";
	public static final String ATTR_NAME_ISOFORM_REF = "isoformRef";

	public static final String ATTR_VALUE_ENSEMBL = "Ensembl";

	HashSet<String> categoryBlackList;
	HashMap<String, String> trIdMap;
	HashMap<String, String> seqById;
	boolean write = true;
	BufferedWriter outFile;

	/**
	 * Main
	 * @param args
	 */
	public static void main(String[] args) {
		Timer.showStdErr("Start");

		//String chrs[] = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "X", "Y", "MT" };
		String chrs[] = { "22", "Y", "MT" };

		String outputFileName = Gpr.HOME + "/snpEff/nextProt/zzz.txt";

		// Create nextprot
		NextProt nextProt = new NextProt();
		nextProt.openOut(outputFileName);
		nextProt.out("Gene Id\tID\tCategory\tDescription\tControlled Vocabulary\tFirst\tLast\tSequence"); // Title

		// Parse all files
		for (String chr : chrs) {
			String xmlFileName = Gpr.HOME + "/snpEff/nextProt/xml/nextprot_chromosome_" + chr + ".xml";
			nextProt.parse(xmlFileName);
		}

		nextProt.closeOut();
		Timer.showStdErr("Done!");
	}

	public NextProt() {
		trIdMap = new HashMap<String, String>();
		seqById = new HashMap<String, String>();

		// Create and populate black list
		categoryBlackList = new HashSet<String>();
		for (String cat : CATAGORY_BLACK_LIST_STR)
			categoryBlackList.add(cat);
	}

	void closeOut() {
		try {
			outFile.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Parse a node
	 * @param tabs
	 * @param node
	 */
	ArrayList<Node> findNodes(Node node, String nodeName, String nodeValue, String attrName, String attrValue) {
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
				if (((nodeName == null) || ((name != null) && name.equals(nodeName))) //
						&& ((nodeValue == null) || ((value != null) && value.equals(nodeValue))) //
				) {
					found = true;
				}
			}

			if (found) {
				resulstsList.add(node);
				if (verbose) Timer.showStdErr("Found: " + toString(node));
			}

			//---
			// Show node
			//---
			switch (type) {
			case Node.ELEMENT_NODE:
				NodeList nodeList = node.getChildNodes();
				resulstsList.addAll(findNodes(nodeList, nodeName, nodeValue, attrName, attrValue));
				node = node.getNextSibling();
				break;
			case Node.TEXT_NODE:
				node = null;
				break;
			case Node.CDATA_SECTION_NODE:
				node = null;
				break;
			default:
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
	List<Node> findNodes(NodeList nodeList, String nodeName, String nodeValue, String attrName, String attrValue) {
		ArrayList<Node> resulstsList = new ArrayList<Node>();

		for (int temp = 0; temp < nodeList.getLength(); temp++) {
			Node node = nodeList.item(temp);
			resulstsList.addAll(findNodes(node, nodeName, nodeValue, attrName, attrValue));
		}

		return resulstsList;
	}

	/**
	 * Find only one node
	 * @param node
	 * @param nodeName
	 * @param nodeValue
	 * @param attrName
	 * @param attrValue
	 * @return
	 */
	Node findOneNode(Node node, String nodeName, String nodeValue, String attrName, String attrValue) {
		ArrayList<Node> resulstsList = findNodes(node, nodeName, nodeValue, attrName, attrValue);
		if (resulstsList.isEmpty()) return null;
		return resulstsList.get(0);
	}

	/**
	 * Find sequences for a node
	 * @param node
	 */
	void findSequences(Node node) {
		// Get sequences
		List<Node> seqNodes = findNodes(node, NODE_NAME_SEQUENCE, null, null, null);
		for (Node seq : seqNodes) {
			String seqStr = getText(seq);
			Node iso = seq.getParentNode();
			String uniq = getAttribute(iso, ATTR_NAME_UNIQUE_NAME);
			seqById.put(uniq, seqStr);
		}
	}

	/**
	 * Get uniqueId -> EnsemblId mapping for transcripts
	 * @param node
	 * @return true if any was found
	 */
	boolean findTrIds(Node node) {
		boolean added = false;

		// Find Ensembl transcript ID
		List<Node> ensemblTrIds = findNodes(node, NODE_NAME_TRANSCRIPT, null, ATTR_NAME_DATABASE, ATTR_VALUE_ENSEMBL);
		for (Node trNode : ensemblTrIds) {
			// Get Ensembl ID
			String trId = getAttribute(trNode, ATTR_NAME_ACCESSION);

			// Get Unique ID
			Node isoMap = trNode.getParentNode();
			String trUniqName = getAttribute(isoMap, ATTR_NAME_UNIQUE_NAME);

			// Add to map
			trIdMap.put(trUniqName, trId);
			added = true;
		}

		return added;
	}

	/**
	 * Get an attribute from a node
	 * @param node
	 * @param attrName
	 * @return
	 */
	String getAttribute(Node node, String attrName) {
		if (node == null) return null;

		NamedNodeMap map = node.getAttributes();
		if (map == null) return null;

		Node attrNode = map.getNamedItem(attrName);
		if (attrNode == null) return null;

		return attrNode.getNodeValue();
	}

	/**
	 * Get Ensembl gene ID
	 * @param node
	 * @param uniqueName
	 * @return
	 */
	String getGeneId(Node node, String uniqueName) {
		Node geneNode = findOneNode(node, NODE_NAME_GENE, null, ATTR_NAME_DATABASE, ATTR_VALUE_ENSEMBL);
		return getAttribute(geneNode, ATTR_NAME_ACCESSION);
	}

	/**
	 * Get text form a node
	 * @param n
	 * @return
	 */
	String getText(Node n) {
		if (n == null) return null;
		return n.getTextContent().replace('\n', ' ').trim();
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

	void openOut(String outFileName) {
		try {
			Timer.showStdErr("Data written to " + outFileName);
			outFile = new BufferedWriter(new FileWriter(outFileName));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	void out(String s) {
		if (!write) return;

		try {
			// System.out.println(s);
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
			List<Node> nodeList = findNodes(doc.getChildNodes(), NODE_NAME_PROTEIN, null, null, null);
			Timer.showStdErr("Found " + nodeList.size() + " protein nodes");
			verbose = false;

			// Parse each node
			for (Node node : nodeList)
				parseProteinNode(node);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Parse a protein node
	 * @param node
	 */
	void parseAnnotation(Node ann, String geneId, String category) {
		Node descr = findOneNode(ann, NODE_NAME_DESCRIPTION, null, null, null);
		String description = getText(descr);

		// Controlled vocabulary
		Node cv = findOneNode(ann, NODE_NAME_DESCRIPTION, null, null, null);
		String contrVoc = getText(cv);

		List<Node> posNodes = findNodes(ann, NODE_NAME_POSITION, null, null, null);

		for (Node pos : posNodes) {
			// Get first & last position
			String first = getAttribute(pos, ATTR_NAME_FIRST);
			String last = getAttribute(pos, ATTR_NAME_LAST);
			int start = Gpr.parseIntSafe(first) - 1;
			int end = Gpr.parseIntSafe(last) - 1;

			Node isoAnn = pos.getParentNode().getParentNode();
			String isoformRef = getAttribute(isoAnn, ATTR_NAME_ISOFORM_REF);

			// Find sequence
			String sequence = seqById.get(isoformRef);
			if ((sequence != null) && (start >= 0) && (end >= start)) sequence = sequence.substring(start, end + 1);

			out(geneId //
					+ "\t" + isoformRef //
					+ "\t" + category //
					+ "\t" + (description != null ? description : "")//
					+ "\t" + (contrVoc != null ? contrVoc : "")//
					+ "\t" + first //
					+ "\t" + last //
					+ "\t" + sequence //
			);
		}
	}

	/**
	 * Parse annotations list
	 * @param node
	 * @param geneId
	 */
	void parseAnnotationList(Node node, String geneId) {
		// Get annotation lists
		List<Node> annListNodes = findNodes(node, NODE_NAME_ANNOTATION_LIST, null, null, null);
		for (Node annList : annListNodes) {
			// Parse each annotation in the list
			String category = getAttribute(annList, ATTR_NAME_CATAGORY);
			List<Node> annNodes = findNodes(annList, NODE_NAME_ANNOTATION, null, null, null);

			// Analyze the ones not in the blacklist
			for (Node ann : annNodes)
				if (!categoryBlackList.contains(category)) parseAnnotation(ann, geneId, category);
		}
	}

	/**
	 * Parse a protein node
	 * @param node
	 */
	void parseProteinNode(Node node) {
		String uniqueName = getAttribute(node, ATTR_NAME_UNIQUE_NAME);

		// Find Ensembl gene ID
		String geneId = getGeneId(node, uniqueName);
		if (geneId != null) {
			// Get transcript IDs
			if (findTrIds(node)) {
				findSequences(node); // Find sequences
				parseAnnotationList(node, geneId); // Parse annotation list
			}
		}
	}

	/**
	 * Show a node as a string
	 * @param node
	 * @param tabs
	 * @return
	 */
	String toString(Node node) {
		StringBuilder sb = new StringBuilder();

		//---
		// Get name & value
		//---
		String name = node.getNodeName();
		String value = node.getNodeValue();
		if (value != null) value = value.replace('\n', ' ').trim();
		sb.append(name);

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
