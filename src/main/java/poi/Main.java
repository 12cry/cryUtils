
package poi;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.BreakClear;
import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.VerticalAlign;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVerticalJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

public class Main {
	public static final String TABLE_CODE_KEY = "表名";
	public static final String TABLE_NAME_KEY = "表说明";

	public static void main(String[] args) throws Exception {
		Main m = new Main();
		m.f4();
	}

	public void setCellBackgroup(XWPFTableCell cell) {
		CTTcPr tcpr = cell.getCTTc().addNewTcPr();
		CTVerticalJc va = tcpr.addNewVAlign();
		va.setVal(STVerticalJc.CENTER);
		CTShd ctshd = tcpr.addNewShd();
		ctshd.setColor("auto");
		ctshd.setVal(STShd.CLEAR);
		ctshd.setFill("EEEEEE");

	}

	public void f5(XWPFDocument doc, List<Map<String, Object>> list) throws Exception {
		String tableCode = (String) list.get(0).get(TABLE_CODE_KEY);
		String tableName = (String) list.get(0).get(TABLE_NAME_KEY);

		Set<String> keySet = list.get(0).keySet();
		keySet.remove(TABLE_CODE_KEY);
		keySet.remove(TABLE_NAME_KEY);
		Object[] cols = keySet.toArray();
		int nRows = list.size() + 3;
		int nCols = cols.length;

		XWPFTable table = doc.createTable(2, 2);
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

	public String getTableModelName(String code) {
		String name = "";
		if ("T_ALARM".equals(code)) {
			name = "告警管理";
		} else if ("T_B".equals(code)) {
			name = "业务拓扑";
		} else if ("T_CI".equals(code)) {
			name = "配置项管理";
		} else if ("T_P".equals(code)) {
			name = "性能管理";
		} else if ("T_TOPO".equals(code)) {
			name = "网络拓扑";
		}
		return name;
	}

	public void f4() throws Exception {
		XWPFDocument template = new XWPFDocument(new FileInputStream("E:/template.docx"));
		CTStyles wordStyles = template.getStyle();

		ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
		JdbcTemplate jdbcTemplate = (JdbcTemplate) context.getBean("jdbcTemplate");

		String sql = JeecgSqlUtil.getMethodSql("test1");
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);

		Map<String, List<Map<String, Object>>> tableMap = new HashMap<String, List<Map<String, Object>>>();
		for (Map<String, Object> m : list) {
			List<Map<String, Object>> l = tableMap.get((String) m.get(TABLE_CODE_KEY));
			if (l == null) {
				l = new ArrayList<Map<String, Object>>();
				tableMap.put((String) m.get(TABLE_CODE_KEY), l);
			}
			l.add(m);
		}

		Map<String, Map<String, List<Map<String, Object>>>> modelMap = new HashMap<String, Map<String, List<Map<String, Object>>>>();
		for (String tableCode : tableMap.keySet()) {
			String tableModel = tableCode.indexOf("_", 2) == -1 ? tableCode : tableCode.substring(0, tableCode.indexOf("_", 2));
			Map<String, List<Map<String, Object>>> m = modelMap.get(tableModel);
			if (m == null) {
				m = new HashMap<String, List<Map<String, Object>>>();
				modelMap.put(tableModel, m);
			}
			m.put(tableCode, tableMap.get(tableCode));
		}
		// System.out.println(modelMap);

		XWPFDocument doc = new XWPFDocument();
		XWPFStyles newStyles = doc.createStyles();
		newStyles.setStyles(wordStyles);
		int title1 = 0;
		for (String s1 : modelMap.keySet()) {
			doc.createParagraph();
			XWPFParagraph p = doc.createParagraph();
			p.setWordWrapped(true);
			p.setStyle("2");
			// p.setPageBreak(true);

			XWPFRun r = p.createRun();
			System.out.println(s1);
			r.setText(this.getTableModelName(s1));

			int title2 = 1;
			Map<String, List<Map<String, Object>>> map = modelMap.get(s1);
			for (String s2 : map.keySet()) {
				p = doc.createParagraph();
				p.setStyle("3");
				r = p.createRun();
//				r.setText("5." + title1 + "." + title2++ + ". " + s2);
				r.setText(s2);

				f5(doc, map.get(s2));
			}
		}

		OutputStream out = new FileOutputStream("E://t1.docx");
		try {
			doc.write(out);
		} finally {
			out.close();
		}

	}

	public void f3() throws Exception {
		XWPFDocument doc = new XWPFDocument();

		try {
			int nRows = 6;
			int nCols = 3;
			XWPFTable table = doc.createTable(nRows, nCols);
			List<XWPFTableRow> rows = table.getRows();
			int rowCt = 0;
			int colCt = 0;
			for (XWPFTableRow row : rows) {
				List<XWPFTableCell> cells = row.getTableCells();
				for (XWPFTableCell cell : cells) {

					CTTcPr tcpr = cell.getCTTc().addNewTcPr();
					CTVerticalJc va = tcpr.addNewVAlign();
					va.setVal(STVerticalJc.CENTER);
					CTShd ctshd = tcpr.addNewShd();
					ctshd.setColor("auto");
					ctshd.setVal(STShd.CLEAR);
					if (rowCt == 0) {
						ctshd.setFill("A7BFDE");
						// } else if (rowCt % 2 == 0) {
						// ctshd.setFill("D3DFEE");
						// } else {
						// ctshd.setFill("EDF2F8");
					}

					XWPFParagraph para = cell.getParagraphs().get(0);
					XWPFRun rh = para.createRun();
					if (rowCt == 0) {
						rh.setText("header row, col " + colCt);
						rh.setBold(true);
						para.setAlignment(ParagraphAlignment.CENTER);
					}

					rh.setText("KKK");
					colCt++;
				}
				rowCt++;
			}
			OutputStream out = new FileOutputStream("E://t1.docx");
			try {
				doc.write(out);
			} finally {
				out.close();
			}
		} finally {
			doc.close();
		}
	}

	public static void createStyledTable() throws Exception {
		// Create a new document from scratch
		XWPFDocument doc = new XWPFDocument();

		try {
			// -- OR --
			// open an existing empty document with styles already defined
			// XWPFDocument doc = new XWPFDocument(new
			// FileInputStream("base_document.docx"));

			// Create a new table with 6 rows and 3 columns
			int nRows = 6;
			int nCols = 3;
			XWPFTable table = doc.createTable(nRows, nCols);

			// Set the table style. If the style is not defined, the table style
			// will become "Normal".
			CTTblPr tblPr = table.getCTTbl().getTblPr();
			CTString styleStr = tblPr.addNewTblStyle();
			styleStr.setVal("StyledTable");

			// Get a list of the rows in the table
			List<XWPFTableRow> rows = table.getRows();
			int rowCt = 0;
			int colCt = 0;
			for (XWPFTableRow row : rows) {
				// get table row properties (trPr)
				CTTrPr trPr = row.getCtRow().addNewTrPr();
				// set row height; units = twentieth of a point, 360 = 0.25"
				CTHeight ht = trPr.addNewTrHeight();
				ht.setVal(BigInteger.valueOf(360));

				// get the cells in this row
				List<XWPFTableCell> cells = row.getTableCells();
				// add content to each cell
				for (XWPFTableCell cell : cells) {
					// get a table cell properties element (tcPr)
					CTTcPr tcpr = cell.getCTTc().addNewTcPr();
					// set vertical alignment to "center"
					CTVerticalJc va = tcpr.addNewVAlign();
					va.setVal(STVerticalJc.CENTER);

					// create cell color element
					CTShd ctshd = tcpr.addNewShd();
					ctshd.setColor("auto");
					ctshd.setVal(STShd.CLEAR);
					if (rowCt == 0) {
						// header row
						ctshd.setFill("A7BFDE");
					} else if (rowCt % 2 == 0) {
						// even row
						ctshd.setFill("D3DFEE");
					} else {
						// odd row
						ctshd.setFill("EDF2F8");
					}

					// get 1st paragraph in cell's paragraph list
					XWPFParagraph para = cell.getParagraphs().get(0);
					// create a run to contain the content
					XWPFRun rh = para.createRun();
					// style cell as desired
					if (colCt == nCols - 1) {
						// last column is 10pt Courier
						rh.setFontSize(10);
						rh.setFontFamily("Courier");
					}
					if (rowCt == 0) {
						// header row
						rh.setText("header row, col " + colCt);
						rh.setBold(true);
						para.setAlignment(ParagraphAlignment.CENTER);
					} else {
						// other rows
						rh.setText("row " + rowCt + ", col " + colCt);
						para.setAlignment(ParagraphAlignment.LEFT);
					}
					colCt++;
				} // for cell
				colCt = 0;
				rowCt++;
			} // for row

			// write the file
			OutputStream out = new FileOutputStream("E://styledTable.docx");
			try {
				doc.write(out);
			} finally {
				out.close();
			}
		} finally {
			doc.close();
		}
	}

	public void f2() throws Exception {
		try {
			createSimpleTable();
		} catch (Exception e) {
			throw (e);
		}
		try {
			createStyledTable();
		} catch (Exception e) {
			throw (e);
		}
	}

	public static void createSimpleTable() throws Exception {
		XWPFDocument doc = new XWPFDocument();

		try {
			XWPFTable table = doc.createTable(3, 3);

			table.getRow(1).getCell(1).setText("EXAMPLE OF TABLE");

			// table cells have a list of paragraphs; there is an initial
			// paragraph created when the cell is created. If you create a
			// paragraph in the document to put in the cell, it will also
			// appear in the document following the table, which is probably
			// not the desired result.
			XWPFParagraph p1 = table.getRow(0).getCell(0).getParagraphs().get(0);

			XWPFRun r1 = p1.createRun();
			r1.setBold(true);
			r1.setText("The quick brown fox");
			r1.setItalic(true);
			r1.setFontFamily("Courier");
			r1.setUnderline(UnderlinePatterns.DOT_DOT_DASH);
			r1.setTextPosition(100);

			table.getRow(2).getCell(2).setText("only text");

			OutputStream out = new FileOutputStream("E://simpleTable.docx");
			try {
				doc.write(out);
			} finally {
				out.close();
			}
		} finally {
			doc.close();
		}
	}

	public void f1() throws Exception {

		XWPFDocument doc = new XWPFDocument();

		XWPFParagraph p1 = doc.createParagraph();
		p1.setAlignment(ParagraphAlignment.CENTER);
		p1.setBorderBottom(Borders.DOUBLE);
		p1.setBorderTop(Borders.DOUBLE);

		p1.setBorderRight(Borders.DOUBLE);
		p1.setBorderLeft(Borders.DOUBLE);
		p1.setBorderBetween(Borders.SINGLE);

		p1.setVerticalAlignment(TextAlignment.TOP);

		XWPFRun r1 = p1.createRun();
		r1.setBold(true);
		r1.setText("The quick brown fox");
		r1.setBold(true);
		r1.setFontFamily("Courier");
		r1.setUnderline(UnderlinePatterns.DOT_DOT_DASH);
		r1.setTextPosition(100);

		XWPFParagraph p2 = doc.createParagraph();
		p2.setAlignment(ParagraphAlignment.RIGHT);

		// BORDERS
		p2.setBorderBottom(Borders.DOUBLE);
		p2.setBorderTop(Borders.DOUBLE);
		p2.setBorderRight(Borders.DOUBLE);
		p2.setBorderLeft(Borders.DOUBLE);
		p2.setBorderBetween(Borders.SINGLE);

		XWPFRun r2 = p2.createRun();
		r2.setText("jumped over the lazy dog");
		r2.setStrikeThrough(true);
		r2.setFontSize(20);

		XWPFRun r3 = p2.createRun();
		r3.setText("and went away");
		r3.setStrikeThrough(true);
		r3.setFontSize(20);
		r3.setSubscript(VerticalAlign.SUPERSCRIPT);

		XWPFParagraph p3 = doc.createParagraph();
		p3.setWordWrapped(true);
		p3.setPageBreak(true);

		// p3.setAlignment(ParagraphAlignment.DISTRIBUTE);
		p3.setAlignment(ParagraphAlignment.BOTH);
		// p3.setSpacingBetween(15, LineSpacingRule.EXACT);

		p3.setIndentationFirstLine(600);

		XWPFRun r4 = p3.createRun();
		r4.setTextPosition(20);
		r4.setText("To be, or not to be: that is the question: " + "Whether 'tis nobler in the mind to suffer "
				+ "The slings and arrows of outrageous fortune, " + "Or to take arms against a sea of troubles, "
				+ "And by opposing end them? To die: to sleep; ");
		r4.addBreak(BreakType.PAGE);
		r4.setText("No more; and by a sleep to say we end " + "The heart-ache and the thousand natural shocks "
				+ "That flesh is heir to, 'tis a consummation " + "Devoutly to be wish'd. To die, to sleep; "
				+ "To sleep: perchance to dream: ay, there's the rub; " + ".......");
		r4.setItalic(true);
		// This would imply that this break shall be treated as a simple line
		// break, and break the line after that word:

		XWPFRun r5 = p3.createRun();
		r5.setTextPosition(-10);
		r5.setText("For in that sleep of death what dreams may come");
		r5.addCarriageReturn();
		r5.setText(
				"When we have shuffled off this mortal coil," + "Must give us pause: there's the respect" + "That makes calamity of so long life;");
		r5.addBreak();
		r5.setText("For who would bear the whips and scorns of time," + "The oppressor's wrong, the proud man's contumely,");

		r5.addBreak(BreakClear.ALL);
		r5.setText("The pangs of despised love, the law's delay," + "The insolence of office and the spurns" + ".......");

		FileOutputStream out = new FileOutputStream("E://simple.docx");
		doc.write(out);
		out.close();
		doc.close();
	}

}
