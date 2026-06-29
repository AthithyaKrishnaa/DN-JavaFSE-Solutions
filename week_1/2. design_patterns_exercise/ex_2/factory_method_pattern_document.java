// Exercise 2: Implementing the Factory Method Pattern
// Scenario: A document management system that needs to create different
// types of documents (Word, PDF, Excel) using the Factory Method Pattern.

abstract class Document {
    public abstract void open();
}

class WordDocument extends Document {
    public void open() {
        System.out.println("Opening Word document...");
    }
}

class PdfDocument extends Document {
    public void open() {
        System.out.println("Opening PDF document...");
    }
}

class ExcelDocument extends Document {
    public void open() {
        System.out.println("Opening Excel document...");
    }
}

abstract class DocumentFactory {
    public abstract Document createDocument();
}

class WordDocumentFactory extends DocumentFactory {
    public Document createDocument() {
        return new WordDocument();
    }
}

class PdfDocumentFactory extends DocumentFactory {
    public Document createDocument() {
        return new PdfDocument();
    }
}

class ExcelDocumentFactory extends DocumentFactory {
    public Document createDocument() {
        return new ExcelDocument();
    }
}

public class Main {
    public static void main(String[] args) {

        System.out.println("=== FACTORY METHOD PATTERN: DOCUMENT MANAGEMENT ===");

        DocumentFactory wordFactory = new WordDocumentFactory();
        Document wordDoc = wordFactory.createDocument();
        System.out.println("\nCreated document type: Word");
        wordDoc.open();

        DocumentFactory pdfFactory = new PdfDocumentFactory();
        Document pdfDoc = pdfFactory.createDocument();
        System.out.println("\nCreated document type: PDF");
        pdfDoc.open();

        DocumentFactory excelFactory = new ExcelDocumentFactory();
        Document excelDoc = excelFactory.createDocument();
        System.out.println("\nCreated document type: Excel");
        excelDoc.open();

        System.out.println("\n=== CONCLUSION ===");
        System.out.println("The client code only depends on the abstract DocumentFactory");
        System.out.println("and Document types, not the concrete classes.");
    }
}
