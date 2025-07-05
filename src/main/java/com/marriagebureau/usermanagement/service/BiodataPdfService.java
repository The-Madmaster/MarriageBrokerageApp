package com.marriagebureau.usermanagement.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color; // <--- CHANGE THIS IMPORT
import com.marriagebureau.usermanagement.entity.Profile;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

@Service
public class BiodataPdfService {

    public byte[] generateBiodataPdf(Profile profile) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);

        document.open();

        // Use Color from java.awt.Color
        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD, Color.DARK_GRAY);
        Font sectionFont = new Font(Font.HELVETICA, 14, Font.BOLD, Color.BLACK);
        Font contentFont = new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK);
        Font labelFont = new Font(Font.HELVETICA, 12, Font.BOLD, Color.BLACK);

        // Title
        Paragraph title = new Paragraph("Biodata for " + profile.getFullName(), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Personal Details Section
        document.add(new Paragraph("Personal Details", sectionFont));
        document.add(Chunk.NEWLINE);

        addKeyValue(document, "Full Name:", profile.getFullName(), labelFont, contentFont);
        addKeyValue(document, "Date of Birth:", profile.getDateOfBirth() != null ? profile.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) : "N/A", labelFont, contentFont);
        if (profile.getDateOfBirth() != null) {
            int age = Period.between(profile.getDateOfBirth(), LocalDate.now()).getYears();
            addKeyValue(document, "Age:", String.valueOf(age) + " years", labelFont, contentFont);
        }
        addKeyValue(document, "Gender:", profile.getGender(), labelFont, contentFont);
        addKeyValue(document, "Marital Status:", profile.getMaritalStatus(), labelFont, contentFont);
        addKeyValue(document, "Height:", profile.getHeightCm() != null ? profile.getHeightCm() + " cm" : "N/A", labelFont, contentFont);
        addKeyValue(document, "Complexion:", profile.getComplexion(), labelFont, contentFont);
        addKeyValue(document, "Body Type:", profile.getBodyType(), labelFont, contentFont);

        document.add(Chunk.NEWLINE);

        // Religious & Background Details
        document.add(new Paragraph("Religious & Background", sectionFont));
        document.add(Chunk.NEWLINE);

        addKeyValue(document, "Religion:", profile.getReligion(), labelFont, contentFont);
        addKeyValue(document, "Caste:", profile.getCaste(), labelFont, contentFont);
        addKeyValue(document, "Sub-Caste:", profile.getSubCaste(), labelFont, contentFont);
        addKeyValue(document, "Mother Tongue:", "N/A", labelFont, contentFont);

        document.add(Chunk.NEWLINE);

        // Professional Details
        document.add(new Paragraph("Professional Details", sectionFont));
        document.add(Chunk.NEWLINE);

        addKeyValue(document, "Education:", profile.getEducation(), labelFont, contentFont);
        addKeyValue(document, "Occupation:", profile.getOccupation(), labelFont, contentFont);
        addKeyValue(document, "Annual Income:", profile.getAnnualIncome() != null ? String.format("₹%.2f", profile.getAnnualIncome()) : "N/A", labelFont, contentFont);

        document.add(Chunk.NEWLINE);

        // Location Details
        document.add(new Paragraph("Location Details", sectionFont));
        document.add(Chunk.NEWLINE);

        addKeyValue(document, "City:", profile.getCity(), labelFont, contentFont);
        addKeyValue(document, "State:", profile.getState(), labelFont, contentFont);
        addKeyValue(document, "Country:", profile.getCountry(), labelFont, contentFont);

        document.add(Chunk.NEWLINE);

        // About Me
        if (profile.getAboutMe() != null && !profile.getAboutMe().isEmpty()) {
            document.add(new Paragraph("About Me", sectionFont));
            document.add(Chunk.NEWLINE);
            Paragraph aboutMeContent = new Paragraph(profile.getAboutMe(), contentFont);
            aboutMeContent.setAlignment(Element.ALIGN_JUSTIFIED);
            document.add(aboutMeContent);
            document.add(Chunk.NEWLINE);
        }

        document.close();
        return baos.toByteArray();
    }

    private void addKeyValue(Document document, String label, String value, Font labelFont, Font contentFont) throws DocumentException {
        Paragraph p = new Paragraph();
        p.add(new Chunk(label, labelFont));
        p.add(new Chunk(" ", labelFont)); // Small space
        p.add(new Chunk(value != null ? value : "N/A", contentFont));
        document.add(p);
    }
}