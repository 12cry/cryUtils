
package com.cry.cryUtils.gen;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class GenDBDesignWord {
	public static final String TABLE_CODE_KEY = "表名";
	public static final String TABLE_NAME_KEY = "表说明";
	public static final String VIEW_CODE_KEY = "VIEW_NAME";
	public static final String INDEX_CODE_KEY = "INDEX_NAME";
	public static final String[] MODULE_CODE_ARRAY = new String[] { "ALARM", "B", "CI", "P", "TOPO" };
	static CTStyles wordStyles;
	static ApplicationContext context;
	static {
		XWPFDocument template = null;

		try {
			template = new XWPFDocument(new FileInputStream("C:/files/template/wordTemplate.docx"));
			wordStyles = template.getStyle();
			context = new ClassPathXmlApplicationContext("spring.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		GenDBDesignWord designWord = new GenDBDesignWord();
		designWord.genDoc();
	}

	public void genDoc() throws Exception {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) context.getBean("jdbcTemplate");

		String sql = JeecgSqlUtil.getMethodSql("table");
		sql+=" where t.TABLE_NAME like 'T\\_ALARM\\_%' escape '\\' or t.TABLE_NAME like 'T\\_CI%' escape '\\'";
//		sql+=" where t.TABLE_NAME like 'BOC%'";
		List<Map<String, Object>> tableList = jdbcTemplate.queryForList(sql);
		sql = JeecgSqlUtil.getMethodSql("view");
		List<Map<String, Object>> viewList = jdbcTemplate.queryForList(sql);
		sql = JeecgSqlUtil.getMethodSql("index");
		List<Map<String, Object>> indexList = jdbcTemplate.queryForList(sql);

		XWPFDocument doc = new XWPFDocument();
		XWPFStyles newStyles = doc.createStyles();
		newStyles.setStyles(wordStyles);

		this.getDocAction(doc, tableList, viewList, indexList);

		CommonUtils.genFile(doc);
	}

	public Map<String, Map<String, List<Map<String, Object>>>> toModuleMap(List<Map<String, Object>> list, String key) {
		Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String, Object>>>();
		for (Map<String, Object> m : list) {
			List<Map<String, Object>> l = map.get((String) m.get(key));
			if (l == null) {
				l = new ArrayList<Map<String, Object>>();
				map.put((String) m.get(key), l);
			}
			l.add(m);
		}
		System.out.println(map);

		Map<String, Map<String, List<Map<String, Object>>>> moduleMap = new HashMap<String, Map<String, List<Map<String, Object>>>>();
		for (String code : map.keySet()) {
			String module;
			if (code.startsWith("T_")) {
				module = code.indexOf("_", code.indexOf("_") + 1) == -1 ? code.substring(code.indexOf("_") + 1)
						: code.substring(code.indexOf("_") + 1, code.indexOf("_", code.indexOf("_") + 1));
			} else {
				module = code.indexOf("_") == -1 ? code : code.substring(0, code.indexOf("_") + 1);
			}

			Map<String, List<Map<String, Object>>> m = moduleMap.get(module);
			if (m == null) {
				m = new HashMap<String, List<Map<String, Object>>>();
				moduleMap.put(module, m);
			}
			m.put(code, map.get(code));
		}
		// System.out.println(moduleMap);
		return moduleMap;
	}

	public void getDocAction(XWPFDocument doc, List<Map<String, Object>> tableList, List<Map<String, Object>> viewList, List<Map<String, Object>> indexList) throws Exception {

		Map<String, Map<String, List<Map<String, Object>>>> tableModuleMap = this.toModuleMap(tableList, TABLE_CODE_KEY);
		// Map<String, Map<String, List<Map<String, Object>>>> indexModuleMap =
		// this.toModuleMap(indexList, INDEX_CODE_KEY);
		Map<String, List<Map<String, Object>>> map;
		for (String module : tableModuleMap.keySet()) {
			System.out.println(module);

			doc.createParagraph();
			XWPFParagraph p = doc.createParagraph();
			p.setWordWrapped(true);
			p.setStyle("2");

			XWPFRun r = p.createRun();
			r.setText(this.getTableModelName(module));

			map = tableModuleMap.get(module);
			if (map != null) {
				p = doc.createParagraph();
				p.setStyle("3");
				p.createRun().setText("表设计");
				for (String s : map.keySet()) {
					p = doc.createParagraph();
					p.setStyle("4");
					p.createRun().setText(s);

					writeTable(doc, map.get(s));
				}
			}

			// map = indexModuleMap.get(module);
			// if (map != null) {
			// p = doc.createParagraph();
			// p.setStyle("3");
			// p.createRun().setText("索引设计");
			//
			// }

			// Map<String, Map<String, List<Map<String, Object>>>> viewModuleMap
			// = this.toModuleMap(viewList, VIEW_CODE_KEY);
			// map = viewModuleMap.get(module);
			// if (map != null) {
			// p = doc.createParagraph();
			// p.setStyle("3");
			// p.createRun().setText("视图设计");
			// for (String s : map.keySet()) {
			// p = doc.createParagraph();
			// p.setStyle("4");
			// p.createRun().setText(s);
			//
			// XWPFParagraph p2 = doc.createParagraph();
			// List<Map<String, Object>> list = map.get(s);
			// p2.createRun().setText((String) list.get(0).get("TEXT"));
			// }
			// }
		}

	}

	public void writeIndex(XWPFDocument doc, Map<String, List<Map<String, Object>>> map) throws Exception {

		int nRows = map.size() + 1;

		String[] cols = new String[] { "ww", "dfd" };

		XWPFTable table = doc.createTable(nRows, 2);
		List<XWPFTableRow> rows = table.getRows();
		List<XWPFTableCell> headerList = rows.get(0).getTableCells();
		for (int i = 0; i < headerList.size(); i++) {
			this.setCellBackgroup(headerList.get(i));
			XWPFParagraph hp = headerList.get(i).getParagraphs().get(0);
			hp.setAlignment(ParagraphAlignment.CENTER);
			XWPFRun hr = hp.createRun();
			hr.setBold(true);
			hr.setText(cols[i]);
		}

		int i = 0;
		for (String key : map.keySet()) {
			// Map<String, Object> m = map.get(key).get(0);

			List<XWPFTableCell> cells = rows.get(i++ + 1).getTableCells();
			for (int j = 0; j < cols.length; j++) {
				XWPFParagraph parag = cells.get(j).getParagraphs().get(0);
				parag.setAlignment(ParagraphAlignment.CENTER);
				XWPFRun cr = parag.createRun();
				cr.setText("ss");
			}

		}

	}

	public void writeTable(XWPFDocument doc, List<Map<String, Object>> list) throws Exception {
		String tableCode = (String) list.get(0).get(TABLE_CODE_KEY);
		String tableName = (String) list.get(0).get(TABLE_NAME_KEY);

		Set<String> keySet = list.get(0).keySet();
		keySet.remove(TABLE_CODE_KEY);
		keySet.remove(TABLE_NAME_KEY);
		Object[] cols = keySet.toArray();
		int nRows = list.size() + 3;
		int nCols = cols.length;

		XWPFTable table = doc.createTable(2, nCols);
		table.setWidth(1000);
		table.getCTTbl().addNewTblGrid();
		List<XWPFTableRow> rows = table.getRows();

		List<XWPFTableCell> tableCells1 = rows.get(0).getTableCells();
		this.setCellBackgroup(tableCells1.get(0));
		XWPFParagraph p11 = tableCells1.get(0).getParagraphs().get(0);
		p11.setAlignment(ParagraphAlignment.CENTER);
		XWPFRun r11 = p11.createRun();
		r11.setBold(true);
		r11.setText(TABLE_CODE_KEY);
		XWPFParagraph p12 = tableCells1.get(1).getParagraphs().get(0);
		XWPFRun r12 = p12.createRun();
		r12.setText(tableCode);

		this.mergeCellsHorizontal(table, 0, 1, nCols-1);
		this.mergeCellsHorizontal(table, 1, 1, nCols-1);

		
		List<XWPFTableCell> tableCells2 = rows.get(1).getTableCells();
		this.setCellBackgroup(tableCells2.get(0));
		XWPFParagraph p21 = tableCells2.get(0).getParagraphs().get(0);
		p21.setAlignment(ParagraphAlignment.CENTER);
		XWPFRun r21 = p21.createRun();
		r21.setBold(true);
		r21.setText(TABLE_NAME_KEY);
		XWPFParagraph p22 = tableCells2.get(1).getParagraphs().get(0);
		XWPFRun r22 = p22.createRun();
		r22.setText(tableName);

		table = doc.createTable(nRows - 2, nCols);
		rows = table.getRows();
		List<XWPFTableCell> headerList = rows.get(0).getTableCells();
		for (int i = 0; i < headerList.size(); i++) {
			this.setCellBackgroup(headerList.get(i));
			XWPFParagraph hp = headerList.get(i).getParagraphs().get(0);
			hp.setAlignment(ParagraphAlignment.CENTER);
			XWPFRun hr = hp.createRun();
			hr.setBold(true);
			hr.setText(cols[i].toString());
		}

		for (int i = 0; i < list.size(); i++) {
			List<XWPFTableCell> cells = rows.get(i + 1).getTableCells();
			for (int j = 0; j < nCols; j++) {
				XWPFParagraph parag = cells.get(j).getParagraphs().get(0);
				parag.setAlignment(ParagraphAlignment.CENTER);
				XWPFRun cr = parag.createRun();
				cr.setText(list.get(i).get(cols[j]) == null ? "" : list.get(i).get(cols[j]).toString());
			}
		}
	}

	private String getTableModelName(String code) {
		String name = "";
		if ("ALARM".equals(code)) {
			name = "告警管理";
		} else if ("B".equals(code)) {
			name = "业务拓扑";
		} else if ("CI".equals(code)) {
			name = "配置项管理";
		} else if ("P".equals(code)) {
			name = "性能管理";
		} else if ("TOPO".equals(code)) {
			name = "网络拓扑";
		} else {
			name = code;
		}
		return name;
	}

	private void setCellBackgroup(XWPFTableCell cell) {
		CTTcPr tcpr = cell.getCTTc().addNewTcPr();
		CTVerticalJc va = tcpr.addNewVAlign();
		va.setVal(STVerticalJc.CENTER);
		CTShd ctshd = tcpr.addNewShd();
		ctshd.setColor("auto");
		ctshd.setVal(STShd.CLEAR);
		ctshd.setFill("EEEEEE");
	}
	
	private  void mergeCellsHorizontal(XWPFTable table, int row, int fromCell, int toCell) {  
        for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {  
            XWPFTableCell cell = table.getRow(row).getCell(cellIndex);  
            if ( cellIndex == fromCell ) {  
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);  
            } else {  
                cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);  
            }  
        }  
    }  
}
