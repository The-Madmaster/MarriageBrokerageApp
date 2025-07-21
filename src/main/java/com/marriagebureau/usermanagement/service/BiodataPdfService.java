// src/main/java/com/marriagebureau/usermanagement/service/BiodataPdfService.java
package com.marriagebureau.usermanagement.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Color;
import com.marriagebureau.usermanagement.entity.Profile;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI; // ⭐ Changed: Import for URI
import java.net.URL;
import java.net.URISyntaxException; // ⭐ Changed: Import for URISyntaxException
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

        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD, Color.DARK_GRAY);
        Font sectionFont = new Font(Font.HELVETICA, 14, Font.BOLD, Color.BLACK);
        Font contentFont = new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLACK);
        Font labelFont = new Font(Font.HELVETICA, 12, Font.BOLD, Color.BLACK);

        try {
            // Title
            Paragraph title = new Paragraph("Biodata for " + profile.getFullName(), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // --- Profile Photo ---
            if (profile.getPhotoUrl() != null && !profile.getPhotoUrl().isEmpty()) {
                try {
                    // ⭐ UPDATED: Using URI to create URL to avoid deprecation warning
                    URL profileImageUrl = new URI(profile.getPhotoUrl()).toURL();
                    Image profileImage = Image.getInstance(profileImageUrl);
                    profileImage.scaleToFit(100, 100);
                    profileImage.setAlignment(Element.ALIGN_RIGHT);
                    profileImage.setSpacingAfter(10);
                    document.add(profileImage);
                } catch (URISyntaxException e) {
                    System.err.println("Invalid URI for profile image URL: " + profile.getPhotoUrl() + " Error: " + e.getMessage());
                    // Handle invalid URI (e.g., malformed URL string)
                } catch (Exception e) {
                    System.err.println("Could not load profile image from URL: " + profile.getPhotoUrl() + " Error: " + e.getMessage());
                }
            }

            // Personal Details Section
            document.add(createSectionTitle("Personal Details", sectionFont));
            document.add(createKeyValueTable(labelFont, contentFont,
                    new String[][]{
                            {"Full Name:", profile.getFullName()},
                            {"Date of Birth:", profile.getDateOfBirth() != null ? profile.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")) : "N/A"},
                            {"Age:", profile.getAge() != null ? String.valueOf(profile.getAge()) + " years" : "N/A"},
                            {"Gender:", profile.getGender() != null ? profile.getGender().name() : "N/A"},
                            {"Marital Status:", profile.getMaritalStatus() != null ? profile.getMaritalStatus().name().replace("_", " ") : "N/A"},
                            {"Height:", profile.getHeightCm() != null ? profile.getHeightCm() + " cm" : "N/A"},
                            {"Complexion:", profile.getComplexion() != null ? profile.getComplexion().name().replace("_", " ") : "N/A"},
                            {"Body Type:", profile.getBodyType() != null ? profile.getBodyType().name().replace("_", " ") : "N/A"},
                            {"Diet:", profile.getDiet() != null ? profile.getDiet().name().replace("_", " ") : "N/A"},
                            {"Smoking Habit:", profile.getSmokingHabit() != null ? profile.getSmokingHabit().name().replace("_", " ") : "N/A"},
                            {"Drinking Habit:", profile.getDrinkingHabit() != null ? profile.getDrinkingHabit().name().replace("_", " ") : "N/A"}
                    }));
            document.add(Chunk.NEWLINE);

            // Religious & Background Details
            document.add(createSectionTitle("Religious & Background", sectionFont));
            document.add(createKeyValueTable(labelFont, contentFont,
                    new String[][]{
                            {"Religion:", profile.getReligion()},
                            {"Caste:", profile.getCaste()},
                            {"Sub-Caste:", profile.getSubCaste()},
                            {"Mother Tongue:", profile.getMotherTongue() != null ? profile.getMotherTongue().name().replace("_", " ") : "N/A"}
                    }));
            document.add(Chunk.NEWLINE);

            // Professional Details
            document.add(createSectionTitle("Professional Details", sectionFont));
            document.add(createKeyValueTable(labelFont, contentFont,
                    new String[][]{
                            {"Education:", profile.getEducation()},
                            {"Occupation:", profile.getOccupation()},
                            {"Annual Income:", profile.getAnnualIncome() != null ? String.format("₹%.2f", profile.getAnnualIncome()) : "N/A"}
                    }));
            document.add(Chunk.NEWLINE);

            // Location Details
            document.add(createSectionTitle("Location Details", sectionFont));
            document.add(createKeyValueTable(labelFont, contentFont,
                    new String[][]{
                            {"City:", profile.getCity()},
                            {"State:", profile.getState()},
                            {"Country:", profile.getCountry()}
                    }));
            document.add(Chunk.NEWLINE);

            // About Me
            if (profile.getAboutMe() != null && !profile.getAboutMe().isEmpty()) {
                document.add(createSectionTitle("About Me", sectionFont));
                document.add(Chunk.NEWLINE);
                Paragraph aboutMeContent = new Paragraph(profile.getAboutMe(), contentFont);
                aboutMeContent.setAlignment(Element.ALIGN_JUSTIFIED);
                document.add(aboutMeContent);
                document.add(Chunk.NEWLINE);
            }

            // --- Preferred Partner Attributes (Optional: only if you want these in the biodata PDF) ---
            if (profile.getPreferredPartnerMinAge() != null || profile.getPreferredPartnerMaxAge() != null ||
                    (profile.getPreferredPartnerReligion() != null && !profile.getPreferredPartnerReligion().isEmpty()) ||
                    (profile.getPreferredPartnerCaste() != null && !profile.getPreferredPartnerCaste().isEmpty()) ||
                    profile.getPreferredPartnerMinHeightCm() != null || profile.getPreferredPartnerMaxHeightCm() != null) {

                document.add(createSectionTitle("Preferred Partner Attributes", sectionFont));
                document.add(Chunk.NEWLINE);

                String ageRange = (profile.getPreferredPartnerMinAge() != null ? profile.getPreferredPartnerMinAge() : "N/A") +
                        " - " +
                        (profile.getPreferredPartnerMaxAge() != null ? profile.getPreferredPartnerMaxAge() : "N/A") + " years";

                String heightRange = (profile.getPreferredPartnerMinHeightCm() != null ? profile.getPreferredPartnerMinHeightCm() : "N/A") +
                        " - " +
                        (profile.getPreferredPartnerMaxHeightCm() != null ? profile.getPreferredPartnerMaxHeightCm() : "N/A") + " cm";

                document.add(createKeyValueTable(labelFont, contentFont,
                        new String[][]{
                                {"Age Range:", ageRange},
                                {"Height Range:", heightRange},
                                {"Religion:", profile.getPreferredPartnerReligion()},
                                {"Caste:", profile.getPreferredPartnerCaste()}
                        }));
                document.add(Chunk.NEWLINE);
            }
        } finally {
            document.close();
        }
        return baos.toByteArray();
    }

    private Paragraph createSectionTitle(String titleText, Font font) {
        Paragraph title = new Paragraph(titleText, font);
        title.setSpacingAfter(5);
        title.setSpacingBefore(10);
        return title;
    }

    private PdfPTable createKeyValueTable(Font labelFont, Font contentFont, String[][] data) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(5f);
        table.setSpacingAfter(5f);

        float[] columnWidths = {0.3f, 0.7f};
        table.setWidths(columnWidths);

        for (String[] row : data) {
            String label = row[0];
            String value = row[1];

            PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
            labelCell.setBorder(Rectangle.NO_BORDER);
            labelCell.setPaddingBottom(5);
            labelCell.setPaddingLeft(0);
            table.addCell(labelCell);

            PdfPCell valueCell = new PdfPCell(new Phrase(value != null && !value.isEmpty() ? value : "N/A", contentFont));
            valueCell.setBorder(Rectangle.NO_BORDER);
            valueCell.setPaddingBottom(5);
            valueCell.setPaddingLeft(0);
            table.addCell(valueCell);
        }
        return table;
    }
}