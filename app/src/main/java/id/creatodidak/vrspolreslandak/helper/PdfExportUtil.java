package id.creatodidak.vrspolreslandak.helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import id.creatodidak.vrspolreslandak.R;

public class PdfExportUtil {

    public static void exportViewToPdf(Context context, View viewToExport, String pdfFileName) {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(viewToExport.getWidth(), viewToExport.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        viewToExport.draw(canvas);

        document.finishPage(page);

        String directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/VRS/KMS";
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            directory.mkdirs(); // Create the directories if they don't exist
        }

        String filePath = directoryPath + "/" + pdfFileName;

        try {
            File file = new File(filePath);
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            fos.close();
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void exportScrollViewToPdf(Context context, ScrollView scrollView, String pdfFileName, LinearLayout grafikimtu, LinearLayout grafiktbu, LinearLayout grafikbbu, LinearLayout grafikbbtb) {
//        PdfDocument document = new PdfDocument();
//
//        int pageWidth = 842; // A4 width in points (landscape orientation)
//        int pageHeight = 595; // A4 height in points
//
//        String directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/VRS/KMS";
//        File directory = new File(directoryPath);
//
//        if (!directory.exists()) {
//            directory.mkdirs(); // Create the directories if they don't exist
//        }
//
//        // Create a list of views containing individual graphs
//        List<LinearLayout> graphViews = new ArrayList<>();
//        graphViews.add(grafikimtu);
//        graphViews.add(grafiktbu);
//        graphViews.add(grafikbbu);
//        graphViews.add(grafikbbtb);
//
//        int pageNum = 1;
//        PdfDocument.Page page = null;
//        Canvas canvas = null;
//
//        for (LinearLayout graphView : graphViews) {
//            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNum).create();
//
//            if (canvas == null) {
//                page = document.startPage(pageInfo);
//                canvas = page.getCanvas();
//            } else {
//                document.finishPage(page);
//                page = document.startPage(pageInfo);
//                canvas = page.getCanvas();
//            }
//
//            // Measure the graph view to determine its dimensions
//            int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(pageWidth, View.MeasureSpec.EXACTLY);
//            int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(pageHeight, View.MeasureSpec.EXACTLY);
//            graphView.measure(widthMeasureSpec, heightMeasureSpec);
//
//            // Layout the graph view within the available page dimensions
//            graphView.layout(0, 0, pageWidth, pageHeight);
//
//            // Draw the graph view on the canvas
//            graphView.draw(canvas);
//        }
//
//        // Finish the last page
//        if (page != null) {
//            document.finishPage(page);
//        }
//
//        String filePath = directoryPath + "/" + pdfFileName;
//
//        try {
//            File file = new File(filePath);
//            FileOutputStream fos = new FileOutputStream(file);
//            document.writeTo(fos);
//            fos.close();
//            document.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    public static void exportScrollViewToPdf(Context context, ScrollView scrollView, String pdfFileName, LinearLayout grafikimtu, LinearLayout grafiktbu, LinearLayout grafikbbu, LinearLayout grafikbbtb) {
//        PdfDocument document = new PdfDocument();
//
//        int pageWidth = 842; // A4 width in points (landscape orientation)
//        int pageHeight = 595; // A4 height in points
//
//        String directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/VRS/KMS";
//        File directory = new File(directoryPath);
//
//        if (!directory.exists()) {
//            directory.mkdirs(); // Create the directories if they don't exist
//        }
//
//        // Create a list of views containing individual graphs
//        List<LinearLayout> graphViews = new ArrayList<>();
//        graphViews.add(grafikimtu);
//        graphViews.add(grafiktbu);
//        graphViews.add(grafikbbu);
//        graphViews.add(grafikbbtb);
//
//        int pageNum = 1;
//        PdfDocument.Page page = null;
//        Canvas canvas = null;
//
//        for (LinearLayout graphView : graphViews) {
//            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNum).create();
//
//            if (canvas == null) {
//                page = document.startPage(pageInfo);
//                canvas = page.getCanvas();
//            } else {
//                document.finishPage(page);
//                page = document.startPage(pageInfo);
//                canvas = page.getCanvas();
//            }
//
//            // Measure the graph view to determine its dimensions
//            int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(pageWidth, View.MeasureSpec.EXACTLY);
//            int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(pageHeight, View.MeasureSpec.EXACTLY);
//            graphView.measure(widthMeasureSpec, heightMeasureSpec);
//
//            // Layout the graph view within the available page dimensions
//            graphView.layout(0, 0, pageWidth, pageHeight);
//
//            // Draw the graph view on the canvas
//            graphView.draw(canvas);
//        }
//
//        // Finish the last page
//        if (page != null) {
//            document.finishPage(page);
//        }
//
//        String filePath = directoryPath + "/" + pdfFileName;
//
//        try {
//            File file = new File(filePath);
//            FileOutputStream fos = new FileOutputStream(file);
//            document.writeTo(fos);
//            fos.close();
//            document.close();
//
//            // Show share dialog
//            Uri pdfUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
//            Intent shareIntent = new Intent(Intent.ACTION_SEND);
//            shareIntent.setType("application/pdf");
//            shareIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
//            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            context.startActivity(Intent.createChooser(shareIntent, "Share PDF"));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void exportScrollViewToPdf(Context context, ScrollView scrollView, String pdfFileName, LinearLayout grafikimtu, LinearLayout grafiktbu, LinearLayout grafikbbu, LinearLayout grafikbbtb) {
        PdfDocument document = new PdfDocument();

        String directoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/VRS/KMS";
        File directory = new File(directoryPath);

        if (!directory.exists()) {
            directory.mkdirs(); // Create the directories if they don't exist
        }

        // Create a list of views containing individual graphs
        List<LinearLayout> graphViews = new ArrayList<>();
        graphViews.add(grafikimtu);
        graphViews.add(grafiktbu);
        graphViews.add(grafikbbu);
        graphViews.add(grafikbbtb);

        int pageNum = 1;
        PdfDocument.Page page = null;
        Canvas canvas = null;

        for (LinearLayout graphView : graphViews) {
            // Measure the graph view to determine its dimensions
            int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            graphView.measure(widthMeasureSpec, heightMeasureSpec);

            int measuredWidth = graphView.getMeasuredWidth();
            int measuredHeight = graphView.getMeasuredHeight();

            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(measuredWidth, measuredHeight, pageNum).create();

            if (canvas == null) {
                page = document.startPage(pageInfo);
                canvas = page.getCanvas();
            } else {
                document.finishPage(page);
                page = document.startPage(pageInfo);
                canvas = page.getCanvas();
            }

            // Layout the graph view within the available page dimensions
            graphView.layout(0, 0, measuredWidth, measuredHeight);

            // Draw the graph view on the canvas
            graphView.draw(canvas);
        }

        // Finish the last page
        if (page != null) {
            document.finishPage(page);
        }

        String filePath = directoryPath + "/" + pdfFileName;

        try {
            File file = new File(filePath);
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            fos.close();
            document.close();

            // Show share dialog
            Uri pdfUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("application/pdf");
            shareIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(shareIntent, "Share PDF"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
