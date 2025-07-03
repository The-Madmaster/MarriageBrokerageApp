package com.marriagebureau.usermanagement.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.marriagebureau.usermanagement.entity.Profile;
import org.springframework.stereotype.Service;
import java.awt.Color; 

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.Period;
import java.time.LocalDate;

/**
 * Service class for generating PDF biodata from a user profile.
 * This service uses OpenPDF library to create PDF documents.
 */
@Service
public class BiodataPdfService {

    /**
     * Generates a PDF biodata for a given profile, with contact details hidden.
     *
     * @param profile The Profile object for which to generate the biodata.
     * @return A ByteArrayOutputStream containing the generated PDF.
     * @throws DocumentException If there's an error creating the PDF document.
     * @throws IOException If there's an I/O error.
     */
    public ByteArrayOutputStream generateBiodataPdf(Profile profile) throws DocumentException, IOException {
        // Create a new Document object with A4 size and margins
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Create a PdfWriter instance to write the document to the ByteArrayOutputStream
        PdfWriter.getInstance(document, baos);

        // Open the document for writing
        document.open();

        // Define fonts
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, new Color(0, 51, 102)); // Dark blue
        Font sectionTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, new Color(0, 102, 153)); // Medium blue
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        Font detailFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD);

        // Add Title
        Paragraph title = new Paragraph("Matrimonial Biodata", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Add Profile Photo (Placeholder if photoUrl is not available or not handled)
        // In a real application, you would fetch the image from photoUrl and add it.
        // For now, we'll just add a placeholder text or skip if no actual image handling is desired.
        if (profile.getPhotoUrl() != null && !profile.getPhotoUrl().isEmpty()) {
            // Placeholder: In a real app, load image from URL
            Paragraph photoPlaceholder = new Paragraph("Photo Placeholder: " + profile.getPhotoUrl(), normalFont);
            photoPlaceholder.setAlignment(Element.ALIGN_CENTER);
            photoPlaceholder.setSpacingAfter(10);
            document.add(photoPlaceholder);
        } else {
            Paragraph noPhoto = new Paragraph("No photo available", normalFont);
            noPhoto.setAlignment(Element.ALIGN_CENTER);
            noPhoto.setSpacingAfter(10);
            document.add(noPhoto);
        }


        // --- Personal Details Section ---
        document.add(new Paragraph("Personal Details", sectionTitleFont));
        document.add(Chunk.NEWLINE); // Add a line break for spacing

        addDetailRow(document, "Full Name:", profile.getFullName(), headerFont, normalFont);
        addDetailRow(document, "Date of Birth:", profile.getDateOfBirth() != null ? profile.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) : "N/A", headerFont, normalFont);
        addDetailRow(document, "Age:", profile.getDateOfBirth() != null ? String.valueOf(Period.between(profile.getDateOfBirth(), LocalDate.now()).getYears()) : "N/A", headerFont, normalFont);
        addDetailRow(document, "Gender:", profile.getGender(), headerFont, normalFont);
        addDetailRow(document, "Marital Status:", profile.getMaritalStatus(), headerFont, normalFont);
        addDetailRow(document, "Religion:", profile.getReligion(), headerFont, normalFont);
        addDetailRow(document, "Caste:", profile.getCaste(), headerFont, normalFont);
        addDetailRow(document, "Sub-Caste:", profile.getSubCaste(), headerFont, normalFont);
        document.add(Chunk.NEWLINE);

        // --- Physical Attributes Section ---
        document.add(new Paragraph("Physical Attributes", sectionTitleFont));
        document.add(Chunk.NEWLINE);

        addDetailRow(document, "Height:", profile.getHeightCm() != null ? profile.getHeightCm() + " cm" : "N/A", headerFont, normalFont);
        addDetailRow(document, "Complexion:", profile.getComplexion(), headerFont, normalFont);
        addDetailRow(document, "Body Type:", profile.getBodyType(), headerFont, normalFont);
        document.add(Chunk.NEWLINE);

        // --- Professional & Educational Details Section ---
        document.add(new Paragraph("Professional & Educational Details", sectionTitleFont));
        document.add(Chunk.NEWLINE);

        addDetailRow(document, "Education:", profile.getEducation(), headerFont, normalFont);
        addDetailRow(document, "Occupation:", profile.getOccupation(), headerFont, normalFont);
        addDetailRow(document, "Annual Income:", profile.getAnnualIncome() != null ? "INR " + String.format("%,.2f", profile.getAnnualIncome()) : "N/A", headerFont, normalFont);
        document.add(Chunk.NEWLINE);

        // --- Location Details Section ---
        document.add(new Paragraph("Location Details", sectionTitleFont));
        document.add(Chunk.NEWLINE);

        addDetailRow(document, "City:", profile.getCity(), headerFont, normalFont);
        addDetailRow(document, "State:", profile.getState(), headerFont, normalFont);
        addDetailRow(document, "Country:", profile.getCountry(), headerFont, normalFont);
        document.add(Chunk.NEWLINE);

        // --- About Me Section ---
        if (profile.getAboutMe() != null && !profile.getAboutMe().isEmpty()) {
            document.add(new Paragraph("About Me", sectionTitleFont));
            document.add(Chunk.NEWLINE);
            Paragraph aboutMe = new Paragraph(profile.getAboutMe(), normalFont);
            document.add(aboutMe);
            document.add(Chunk.NEWLINE);
        }

        // --- Preferred Partner Details Section (Optional) ---
        if (profile.getPreferredPartnerMinAge() != null || profile.getPreferredPartnerReligion() != null) {
            document.add(new Paragraph("Preferred Partner Details", sectionTitleFont));
            document.add(Chunk.NEWLINE);

            String ageRange = "N/A";
            if (profile.getPreferredPartnerMinAge() != null && profile.getPreferredPartnerMaxAge() != null) {
                ageRange = profile.getPreferredPartnerMinAge() + " - " + profile.getPreferredPartnerMaxAge() + " years";
            } else if (profile.getPreferredPartnerMinAge() != null) {
                ageRange = profile.getPreferredPartnerMinAge() + " years and above";
            } else if (profile.getPreferredPartnerMaxAge() != null) {
                ageRange = "Up to " + profile.getPreferredPartnerMaxAge() + " years";
            }
            addDetailRow(document, "Age Range:", ageRange, headerFont, normalFont);
            addDetailRow(document, "Religion:", profile.getPreferredPartnerReligion(), headerFont, normalFont);
            addDetailRow(document, "Caste:", profile.getPreferredPartnerCaste(), headerFont, normalFont);

            String heightRange = "N/A";
            if (profile.getPreferredPartnerMinHeightCm() != null && profile.getPreferredPartnerMaxHeightCm() != null) {
                heightRange = profile.getPreferredPartnerMinHeightCm() + " - " + profile.getPreferredPartnerMaxHeightCm() + " cm";
            } else if (profile.getPreferredPartnerMinHeightCm() != null) {
                heightRange = profile.getPreferredPartnerMinHeightCm() + " cm and above";
            } else if (profile.getPreferredPartnerMaxHeightCm() != null) {
                heightRange = "Up to " + profile.getPreferredPartnerMaxHeightCm() + " cm";
            }
            addDetailRow(document, "Height Range:", heightRange, headerFont, normalFont);
            document.add(Chunk.NEWLINE);
        }

        // Important: Contact details are intentionally NOT added here as per requirement.

        // Close the document
        document.close();

        return baos;
    }

    /**
     * Helper method to add a detail row (Label: Value) to the document.
     *
     * @param document The PDF document.
     * @param label The label for the detail (e.g., "Full Name:").
     * @param value The actual value.
     * @param labelFont Font for the label.
     * @param valueFont Font for the value.
     * @throws DocumentException
     */
    private void addDetailRow(Document document, String label, String value, Font labelFont, Font valueFont) throws DocumentException {
        Paragraph p = new Paragraph();
        p.add(new Chunk(label, labelFont));
        p.add(new Chunk(" " + (value != null && !value.isEmpty() ? value : "N/A"), valueFont));
        p.setSpacingAfter(2); // Small spacing after each detail line
        document.add(p);
    }
}
