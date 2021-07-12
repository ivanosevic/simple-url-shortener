package edu.pucmm.eict.common;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class QrCodeGenerator {

    private static QrCodeGenerator instance;

    private QrCodeGenerator() {

    }

    public static QrCodeGenerator getInstance() {
        if (instance == null) {
            instance = new QrCodeGenerator();
        }
        return instance;
    }

    public String getQRCodeImage(String text, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        } catch (IOException | WriterException e) {
            e.printStackTrace();
        }
        byte[] bytes = outputStream.toByteArray();
        return "data:image/png;base64, " + Base64.getEncoder().encodeToString(bytes);
    }
}