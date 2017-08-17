package com.cry.cryUtils.gen;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles;

public class GenGYDesignWord {
	static CTStyles wordStyles;
	static {
		XWPFDocument template = null;
		try {
			template = new XWPFDocument(new FileInputStream("C:/files/template/wordTemplate.docx"));
			wordStyles = template.getStyle();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws Exception, IOException {
		GenGYDesignWord main = new GenGYDesignWord();
		main.run();
	}

	public void run() throws Exception, IOException {
		String filePath = "c://recycle/1.docx";
		XWPFDocument doc = new XWPFDocument(OPCPackage.open(filePath));
		this.gen(doc);
		// doc.close();
	}

	public void gen(XWPFDocument doc) throws IOException {
		XWPFDocument newDoc = new XWPFDocument();
		XWPFStyles newStyles = newDoc.createStyles();
		newStyles.setStyles(wordStyles);

		for (IBodyElement e : doc.getBodyElements()) {
			BodyElementType elementType = e.getElementType();
			if (BodyElementType.PARAGRAPH.equals(elementType)) {
				XWPFParagraph p = (XWPFParagraph) e;
				String text = p.getRuns().isEmpty()?"":p.getRuns().get(0) + (p.getRuns().size() > 1 ? p.getRuns().get(1).toString() : "");

				XWPFParagraph newP = newDoc.createParagraph();
				newP.setWordWrapped(true);
				newP.setStyle(p.getStyle());
				newP.createRun().setText(text);

				if("3".equals(p.getStyle())||"4".equals(p.getStyle())){
					XWPFParagraph newP2 = newDoc.createParagraph();
					newP2.setWordWrapped(true);
					newP2.setStyle(String.valueOf(Integer.parseInt(p.getStyle())+1));
					newP2.createRun().setText("π¶ƒ‹√Ë ˆ");
				}
				
			} else if (BodyElementType.TABLE.equals(elementType)) {
				XWPFTable t = (XWPFTable)e;
				XWPFTableCell cell = t.getRow(2).getCell(1);
				List<XWPFParagraph> paragraphs = cell.getParagraphs();
				for(XWPFParagraph p : paragraphs){
					XWPFParagraph newP = newDoc.createParagraph();
					newP.setStyle(p.getStyle());
					String text = p.getText();
					if("%1.".equals(p.getNumLevelText())){
						newP.setNumID(p.getNumID());
					}else{
						newP.setIndentFromLeft(700);
					}
					newP.createRun().setText(text);
				}
			}
		}
		
		CommonUtils.genFile(newDoc);
		
	}

	public void printTitle(XWPFDocument doc) {
		int i = 0;
		int j = 1;
		int n = 1;
		List<XWPFParagraph> paragraphs = doc.getParagraphs();
		for (XWPFParagraph p : paragraphs) {
			String style = p.getStyle();
			if ("2".equals(style)) {
				System.out.println();
				System.out.print(n++ + "	");
				i++;
				j = 1;
				System.out.print(
						"GN0" + i + p.getRuns().get(0) + (p.getRuns().size() > 1 ? p.getRuns().get(1).toString() : ""));
			} else if ("3".equals(style)) {
				if (j != 1) {
					System.out.println();
					System.out.print(n++ + "	");
				}
				System.out.print("	GN0" + i + "0" + j++ + "	");
				System.out.print(p.getRuns().get(0) + (p.getRuns().size() > 1 ? p.getRuns().get(1).toString() : ""));
			}
		}

	}

}
