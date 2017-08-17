package com.cry.cryUtils.freeMarker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;

public class Gen {

	private Configuration cfg;
	private String templateDir = "FmTemplates/";// "src/main/resources/template"
	private String templateFileName = "template.ftl";// "fmRequest.ftl"
	private String targetFilePath = "targetFile.txt";//"F:/fm.txt"

	public Gen(String templateDir, String templateFileName, String targetFilePath) throws IOException {
		this.templateDir = templateDir;
		this.templateFileName = templateFileName;
		this.targetFilePath = targetFilePath;

		init();
	}
	
	public Gen() {
		init();
	}

	public void init() {
		cfg = new Configuration(Configuration.VERSION_2_3_23);
		cfg.setClassicCompatible(true);
		cfg.setDefaultEncoding("UTF-8");
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
	}

	public void genVO(ClassVO classVO) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, TemplateException {
		cfg.setDirectoryForTemplateLoading(new File(templateDir));
		Template temp = cfg.getTemplate(templateFileName);
		Writer out = new OutputStreamWriter(new FileOutputStream(new File(targetFilePath)));
//		Writer out = new OutputStreamWriter(System.out);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("classVO", classVO);
		temp.process(map, out);
	}

	private void genFile() throws IOException {
		File file = new File(templateDir);
		if(!file.isDirectory()) {
			file.mkdir();
		}
		file = new File(templateDir+"/"+templateFileName);
		if(!file.exists()) {
			file.createNewFile();
		}
	}
	public static void main(String[] args) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		ClassVO classVO = new ClassVO();
		classVO.setPackageName("com.com.test2");
		classVO.setName("cc");

		List<PropertyVO> propertyVOList = new ArrayList<PropertyVO>();
		PropertyVO propertyVO = new PropertyVO();
		propertyVO.setType("String");
		propertyVO.setName("jgDm");
		propertyVOList.add(propertyVO);
		classVO.setPropertyVOList(propertyVOList);
		map.put("classVO", classVO);

		 Gen main = new Gen("src/main/resources/freeMarker","genVOTemplate.ftl","F:/fm2.txt");
		 main.genVO(classVO);
	}

	public String getTemplateFileName() {
		return templateFileName;
	}

	public void setTemplateFileName(String templateFileName) {
		this.templateFileName = templateFileName;
	}

	public String getTemplateDir() {
		return templateDir;
	}

	public void setTemplateDir(String templateDir) {
		this.templateDir = templateDir;
	}

	public String getTargetFilePath() {
		return targetFilePath;
	}

	public void setTargetFilePath(String targetFilePath) {
		this.targetFilePath = targetFilePath;
	}

}
