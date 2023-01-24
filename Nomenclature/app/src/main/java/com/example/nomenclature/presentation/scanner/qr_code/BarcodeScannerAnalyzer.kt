package com.example.nomenclature.presentation.scanner.qr_code

import android.graphics.Rect
import android.graphics.RectF
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class BarcodeScannerAnalyzer(
    val isSearchMode: Boolean,
    private val onBarcodeScanned: (String, RectF) -> Unit,
) : ImageAnalysis.Analyzer {
    private var scaleX = 1f
    private var scaleY = 2f

    private fun translateX(x: Float) = x * scaleX
    private fun translateY(y: Float) = y * scaleY

    private fun adjustBoundingRect(rect: Rect) = RectF(
        translateX(rect.left.toFloat()),
        translateY(rect.top.toFloat()),
        translateX(rect.right.toFloat()),
        translateY(rect.bottom.toFloat())
    )

    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        val image = imageProxy.image
        if (image != null) {
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                    if(isSearchMode)
                        Barcode.FORMAT_QR_CODE
                    else
                        Barcode.FORMAT_ALL_FORMATS
                )
                .build()
            val barcodeScanner = BarcodeScanning.getClient(options)
            val imageToProcess =
                InputImage.fromMediaImage(image, imageProxy.imageInfo.rotationDegrees)

            barcodeScanner.process(imageToProcess)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {
                        onBarcodesDetected(barcodes)
                    } else {
                        onBarcodeScanned("", RectF())
                    }
                }
                .addOnFailureListener {
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
        else {
            imageProxy.close()
        }
    }

    private fun onBarcodesDetected(barcodes: List<Barcode>) {
        for(barcode in barcodes) {
            val text = barcode.displayValue
            barcode.boundingBox?.let { rect ->
                if(text != null) {
                    onBarcodeScanned(text, adjustBoundingRect(rect))
                }
            }
        }
    }
}