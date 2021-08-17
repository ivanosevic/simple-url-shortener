package edu.pucmm.eict.urls;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class PngQrGenerator implements QrGenerator {
    private final int QR_SIZE = 400;
    private final String PNG_HEADER = "data:image/png;base64, ";

    @Override
    public String base64Qr(String text) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, QR_SIZE, QR_SIZE);
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        } catch (IOException | WriterException e) {
            e.printStackTrace();
        }
        byte[] bytes = outputStream.toByteArray();
        return PNG_HEADER + Base64.getEncoder().encodeToString(bytes);
    }
}
