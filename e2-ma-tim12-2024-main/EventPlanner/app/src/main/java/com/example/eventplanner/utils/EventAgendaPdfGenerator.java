package com.example.eventplanner.utils;

import com.example.eventplanner.model.EventActivity;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class EventAgendaPdfGenerator {

    public void createEventAgendaPdf(ArrayList<EventActivity> eventActivities, String filePath) {
        PdfWriter writer = null;
        PdfDocument pdfDoc = null;
        Document doc = null;
        try {
            writer = new PdfWriter(filePath);
            pdfDoc = new PdfDocument(writer);
            doc = new Document(pdfDoc);

            String fileName = new File(filePath).getName();
            String title = fileName.substring(0, fileName.lastIndexOf('.')).replace('_', ' ');

            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            Paragraph titleParagraph = new Paragraph(title).setFont(font).setFontSize(18);
            doc.add(titleParagraph);

            Paragraph spacer = new Paragraph(" ");
            doc.add(spacer);

            Table table = new Table(new float[]{4, 3, 2, 2, 2});
            table.setWidth(100);

            addTableHeader(table, font);
            addRowsToTable(table, eventActivities, font);

            doc.add(table);
        } catch (FileNotFoundException e) {
            System.err.println("Output file path is invalid: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IO exception occurred: " + e.getMessage());
        } finally {
            if (doc != null) {
                doc.close();
            }
            if (pdfDoc != null && !pdfDoc.isClosed()) {
                pdfDoc.close();
            }
        }

        File outputFile = new File(filePath);
        if (outputFile.exists() && outputFile.length() > 0) {
            System.out.println("PDF successfully created at: " + filePath);
        } else {
            System.err.println("Failed to create PDF or the file is empty.");
        }
    }

    private void addTableHeader(Table table, PdfFont font) {
        table.addHeaderCell(new Cell().add(new Paragraph("Name").setFont(font)));
        table.addHeaderCell(new Cell().add(new Paragraph("Description").setFont(font)));
        table.addHeaderCell(new Cell().add(new Paragraph("Start time").setFont(font)));
        table.addHeaderCell(new Cell().add(new Paragraph("End time").setFont(font)));
        table.addHeaderCell(new Cell().add(new Paragraph("Location").setFont(font)));
    }

    private void addRowsToTable(Table table, ArrayList<EventActivity> eventActivities, PdfFont font) {
        for (EventActivity activity : eventActivities) {
            table.addCell(new Cell().add(new Paragraph(activity.getName()).setFont(font)));
            table.addCell(new Cell().add(new Paragraph(activity.getDescription())).setFont(font));
            table.addCell(new Cell().add(new Paragraph(activity.getStartTime())).setFont(font));
            table.addCell(new Cell().add(new Paragraph(activity.getEndTime()).setFont(font)));
            table.addCell(new Cell().add(new Paragraph(activity.getLocation()).setFont(font)));
        }
    }
}
