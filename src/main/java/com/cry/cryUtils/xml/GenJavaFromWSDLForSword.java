package com.cry.cryUtils.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.cry.cryUtils.common.CommonUtils;
import com.cry.cryUtils.freeMarker.ClassVO;
import com.cry.cryUtils.freeMarker.Gen;
import com.cry.cryUtils.freeMarker.PropertyVO;

public class GenJavaFromWSDLForSword {

	public static final String SRC_ROOT = "src/main/java/";
	private String prefixName;
	private String packagePath;
	private String packageName;
	private List<ClassVO> classVOList = new ArrayList<ClassVO>();
	private ClassVO currClassVO;
	
	private String wsdlPath;//"src/main/resources/c1Request.xml"
	private String rootPath = "";
	
	public void parse() throws Exception {
		File file = new File(wsdlPath);
		prefixName = CommonUtils.upperFirst(file.getName().substring(0, file.getName().indexOf(".")));
		packagePath = SRC_ROOT + this.getRootPath() + prefixName;
		packageName = (this.getRootPath() + prefixName).replace("/", ".");

		DocumentBuilder documentBuilder = XMLUtils.getDocumentBuilder();
		Document document = documentBuilder.parse(file);
		Node root = document.getElementsByTagName("service").item(0);
		this.addClassVO(root);
		this.addClassVOs(root);

		if (!new File(packagePath).isDirectory()) {
			new File(packagePath).mkdirs();
		}
		for (ClassVO classVO : classVOList) {
			new Gen("src/main/resources/freeMarker", "genVOTemplate.ftl", packagePath + "/" + classVO.getName() + ".java").genVO(classVO);
		}

	}

	public void addClassVOs(Node root) {

		NodeList childNodes = root.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node node = childNodes.item(i);
			short nodeType = node.getNodeType();
			if (nodeType != Node.ELEMENT_NODE || !node.getNodeName().startsWith("gzc:")) {
				continue;
			}
			System.out.println(i + "  =  " + node.getNodeName());

			this.addClassVO(node);

			if (node.getNodeName().endsWith("VO")) {
				this.addPropertyVO(node);
			} else {
				this.addClassVOs(node);
			}
		}
	}

	private void addPropertyVO(Node root) {
		NodeList childNodes = root.getChildNodes();
		List<PropertyVO> propertyVOList = new ArrayList<PropertyVO>();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node node = childNodes.item(i);
			short nodeType = node.getNodeType();
			if (nodeType != Node.ELEMENT_NODE || !node.getNodeName().startsWith("gzc:")) {
				continue;
			}
			System.out.println(i + "  =  " + node.getNodeName()+" : "+node.getNodeValue()+"  --  "+node.getTextContent());
			propertyVOList.add(new PropertyVO("String", node.getNodeName().replace("gzc:", ""),node.getTextContent()));
		}
		currClassVO.setPropertyVOList(propertyVOList);
	}

	private void addClassVO(Node node) {
		ClassVO classVO = new ClassVO(packageName, CommonUtils.upperFirst(prefixName + CommonUtils.upperFirst(node.getNodeName().replace("gzc:", ""))));
		classVOList.add(classVO);

		if (currClassVO != null) {
			currClassVO.setPropertyVOList(Arrays.asList(new PropertyVO[] { new PropertyVO(classVO.getName(), CommonUtils.lowerFirst(classVO.getName())) }));
		}

		currClassVO = classVO;
	}

	public static void main(String[] args) throws Exception {
		GenJavaFromWSDLForSword xu = new GenJavaFromWSDLForSword();
		xu.setWsdlPath("src/main/resources/c1Request.xml");
		xu.setRootPath("com/cry/test2/");
		xu.parse();
	}

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public String getWsdlPath() {
		return wsdlPath;
	}

	public void setWsdlPath(String wsdlPath) {
		this.wsdlPath = wsdlPath;
	}

}
