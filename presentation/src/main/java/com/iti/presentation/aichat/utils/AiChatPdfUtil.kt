package com.iti.presentation.aichat.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import com.iti.presentation.R
import com.iti.presentation.aichat.model.AiChatMessageUiModel
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

object AiChatPdfUtil {

    fun generateAiChatPdf(context: Context, message: AiChatMessageUiModel): File {
        val pdfDocument = PdfDocument()
        val page = PdfPage(pdfDocument)

        page.drawHeader(context)
        page.drawMessageSection(message)
        page.finish()

        val file = File(context.cacheDir, "AI_Response_${System.currentTimeMillis()}.pdf")
        FileOutputStream(file).use { pdfDocument.writeTo(it) }
        pdfDocument.close()

        return file
    }

    private class PdfPage(private val document: PdfDocument) {

        private val pageWidth = 595
        private val pageHeight = 842
        private val marginX = 40f
        private val marginTop = 40f
        private val marginBottom = 50f

        private var pageNumber = 1
        private var page: PdfDocument.Page = startNewPage()
        private var canvas: Canvas = page.canvas
        private var y = marginTop

        private val colorPrimary = android.graphics.Color.parseColor("#4F8CFF")
        private val colorTextPrimary = android.graphics.Color.parseColor("#0B0B0F")
        private val colorTextSecondary = android.graphics.Color.parseColor("#64748B")

        private fun startNewPage(): PdfDocument.Page {
            val info = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
            return document.startPage(info)
        }

        private fun ensureSpace(requiredHeight: Float) {
            if (y + requiredHeight > pageHeight - marginBottom) {
                drawFooter()
                document.finishPage(page)
                pageNumber += 1
                page = startNewPage()
                canvas = page.canvas
                y = marginTop
            }
        }

        private fun drawFooter() {
            val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = 9f
                color = colorTextSecondary
                typeface = Typeface.DEFAULT
            }
            val year = Calendar.getInstance().get(Calendar.YEAR)
            canvas.drawText("© $year PC Building. All rights reserved.", marginX, pageHeight - 25f, paint)

            val pageText = "Page $pageNumber"
            val pageTextWidth = paint.measureText(pageText)
            canvas.drawText(pageText, pageWidth - marginX - pageTextWidth, pageHeight - 25f, paint)
        }

        fun finish() {
            drawFooter()
            document.finishPage(page)
        }

        fun drawHeader(context: Context) {
            val logo = runCatching {
                BitmapFactory.decodeResource(context.resources, R.drawable.pc_building_no_bg)
            }.getOrNull()

            val logoWidth = 72f
            var logoHeight = logoWidth
            if (logo != null) {
                logoHeight = logoWidth * (logo.height.toFloat() / logo.width.toFloat())
                val destRect = RectF(marginX, y, marginX + logoWidth, y + logoHeight)
                val bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { isFilterBitmap = true }
                canvas.drawBitmap(logo, null, destRect, bitmapPaint)
            }

            val brandPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = 13f
                color = colorTextSecondary
                typeface = Typeface.DEFAULT_BOLD
            }
            canvas.drawText("AI RESPONSE", marginX + logoWidth + 14f, y + logoHeight / 2f + 5f, brandPaint)

            y += logoHeight + 24f
            
            val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = colorPrimary
                strokeWidth = 2f
            }
            canvas.drawLine(marginX, y, pageWidth - marginX, y, linePaint)
            y += 20f
        }

        fun drawMessageSection(message: AiChatMessageUiModel) {
            val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = 12f
                color = colorTextPrimary
                typeface = Typeface.DEFAULT
            }
            
            // Clean up Markdown logic if present, simplistic wrapping
            val lines = message.content.split("\n").flatMap { wrapText(it, textPaint, pageWidth - marginX * 2) }
            
            for (line in lines) {
                ensureSpace(16f)
                canvas.drawText(line, marginX, y, textPaint)
                y += 16f
            }
            
            y += 20f

            if (message.mentionedProducts.isNotEmpty()) {
                ensureSpace(40f)
                val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    textSize = 14f
                    color = colorPrimary
                    typeface = Typeface.DEFAULT_BOLD
                }
                canvas.drawText("Mentioned Products", marginX, y, titlePaint)
                y += 24f

                for (product in message.mentionedProducts) {
                    val bulletPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        textSize = 12f
                        color = colorTextPrimary
                        typeface = Typeface.DEFAULT_BOLD
                    }
                    val pricePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        textSize = 11f
                        color = colorTextSecondary
                        typeface = Typeface.DEFAULT
                    }

                    val nameLines = wrapText("• ${product.productName}", bulletPaint, pageWidth - marginX * 2)
                    val blockHeight = nameLines.size * 16f + 20f
                    ensureSpace(blockHeight)

                    nameLines.forEach { line ->
                        canvas.drawText(line, marginX, y, bulletPaint)
                        y += 16f
                    }
                    
                    canvas.drawText("${product.category.name} | ${product.formattedPrice}", marginX + 15f, y, pricePaint)
                    y += 20f
                }
            }
        }

        private fun wrapText(text: String, paint: Paint, maxWidth: Float): List<String> {
            if (text.isEmpty()) return listOf("")
            val lines = mutableListOf<String>()
            val words = text.split(" ")
            var currentLine = ""

            for (word in words) {
                val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
                if (paint.measureText(testLine) <= maxWidth) {
                    currentLine = testLine
                } else {
                    if (currentLine.isNotEmpty()) {
                        lines.add(currentLine)
                        currentLine = word
                    } else {
                        lines.add(word)
                        currentLine = ""
                    }
                }
            }
            if (currentLine.isNotEmpty()) {
                lines.add(currentLine)
            }
            return lines
        }
    }
}


