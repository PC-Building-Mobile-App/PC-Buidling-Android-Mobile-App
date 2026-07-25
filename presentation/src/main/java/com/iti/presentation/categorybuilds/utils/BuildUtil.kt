package com.iti.presentation.categorybuilds.utils

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import androidx.compose.ui.graphics.toArgb
import com.iti.presentation.R
import com.iti.presentation.categorybuilds.model.BuildUiModel
import com.iti.presentation.ui.theme.DeepBlack
import com.iti.presentation.ui.theme.ElectricBlue
import com.iti.presentation.ui.theme.ErrorRed
import com.iti.presentation.ui.theme.RoyalPurple
import com.iti.presentation.ui.theme.SuccessGreen
import com.iti.presentation.ui.theme.TextMuted
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar

object BuildUtil {

    fun formatBuildShareText(build: BuildUiModel): String = buildString {
        appendLine(build.name.ifBlank { "Untitled Build" })
        appendLine("Total Price: ${build.priceFormatted.ifBlank { "N/A" }}")
        appendLine("Status: ${if (build.compatible) "Compatible" else "Incompatible"}")
        appendLine()

        val validSpecs = build.specs.filter { it.productName.isNotBlank() }
        if (validSpecs.isEmpty()) {
            appendLine("No components added yet.")
        } else {
            appendLine("Components:")
            validSpecs.forEach { spec ->
                val price = spec.formattedPrice.ifBlank { "Price N/A" }
                appendLine("- ${spec.productName}: $price")
            }
        }

        if (!build.compatible && build.issues.isNotEmpty()) {
            appendLine()
            appendLine("Issues:")
            build.issues.forEach { issue ->
                val category = issue.category.ifBlank { "General" }
                val reason = issue.reason.ifBlank { "Unspecified compatibility issue" }
                appendLine("- $category: $reason")
            }
        }
    }

    fun generateBuildPdf(context: Context, build: BuildUiModel): File {
        val pdfDocument = PdfDocument()
        val page = PdfPage(pdfDocument)

        page.drawHeader(context, build)
        page.drawComponentsSection(build)
        if (!build.compatible && build.issues.isNotEmpty()) {
            page.drawIssuesSection(build)
        }
        page.finish()

        val safeName = build.name.ifBlank { "build" }
            .replace(Regex("[^A-Za-z0-9_\\-]"), "_")
        val file = File(context.cacheDir, "$safeName.pdf")
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

        private val colorPrimary = ElectricBlue.toArgb()
        private val colorPrimaryDark = RoyalPurple.toArgb()
        private val colorSuccess = SuccessGreen.toArgb()
        private val colorSuccessBg = SuccessGreen.copy(alpha = 0.14f).toArgb()
        private val colorDanger = ErrorRed.toArgb()
        private val colorDangerBg = ErrorRed.copy(alpha = 0.12f).toArgb()
        private val colorTextPrimary = DeepBlack.toArgb()
        private val colorTextSecondary = TextMuted.toArgb()
        private val colorDivider = TextMuted.copy(alpha = 0.3f).toArgb()

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

        fun drawHeader(context: Context, build: BuildUiModel) {
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
            canvas.drawText("PC BUILDING", marginX + logoWidth + 14f, y + logoHeight / 2f + 5f, brandPaint)

            y += logoHeight + 24f

            val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = 24f
                color = colorTextPrimary
                typeface = Typeface.DEFAULT_BOLD
            }
            val titleLines = wrapText(
                build.name.ifBlank { "Untitled Build" },
                titlePaint,
                pageWidth - marginX * 2 - 130f,
            )
            titleLines.forEach { line ->
                canvas.drawText(line, marginX, y, titlePaint)
                y += 28f
            }

            val badgeText = if (build.compatible) "COMPATIBLE" else "INCOMPATIBLE"
            val badgePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = 11f
                typeface = Typeface.DEFAULT_BOLD
                color = if (build.compatible) colorSuccess else colorDanger
            }
            val badgeBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = if (build.compatible) colorSuccessBg else colorDangerBg
            }
            val badgeWidth = badgePaint.measureText(badgeText) + 24f
            val badgeRect = RectF(
                pageWidth - marginX - badgeWidth,
                marginTop,
                pageWidth - marginX,
                marginTop + 26f,
            )
            canvas.drawRoundRect(badgeRect, 13f, 13f, badgeBgPaint)
            canvas.drawText(
                badgeText,
                badgeRect.left + 12f,
                badgeRect.top + 17f,
                badgePaint,
            )

            y += 6f

            val pricePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = 18f
                color = colorPrimaryDark
                typeface = Typeface.DEFAULT_BOLD
            }
            canvas.drawText(
                "Total: ${build.priceFormatted.ifBlank { "N/A" }}",
                marginX,
                y,
                pricePaint,
            )

            y += 20f
            val dividerPaint = Paint().apply { color = colorDivider; strokeWidth = 1.5f }
            canvas.drawLine(marginX, y, pageWidth - marginX, y, dividerPaint)
            y += 28f
        }

        fun drawComponentsSection(build: BuildUiModel) {
            val validSpecs = build.specs.filter { it.productName.isNotBlank() }

            drawSectionTitle("Components")

            if (validSpecs.isEmpty()) {
                val emptyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    textSize = 13f
                    color = colorTextSecondary
                    typeface = Typeface.DEFAULT
                }
                ensureSpace(24f)
                canvas.drawText("No components added yet.", marginX, y, emptyPaint)
                y += 24f
                return
            }

            val namePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = 13f
                color = colorTextPrimary
                typeface = Typeface.DEFAULT_BOLD
            }
            val categoryPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = 10.5f
                color = colorPrimary
                typeface = Typeface.DEFAULT_BOLD
            }
            val pricePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = 13f
                color = colorTextPrimary
                typeface = Typeface.DEFAULT_BOLD
            }
            val rowPadding = 10f
            val contentWidth = pageWidth - marginX * 2 - 16f

            validSpecs.forEach { spec ->
                val category = spec.subtitle.ifBlank { spec.category.name }
                    .trim()
                    .trim('.', '·', '-', ' ')
                    .ifBlank { spec.category.name }
                val priceText = spec.formattedPrice.ifBlank { "Price N/A" }
                val nameLines = wrapText(spec.productName, namePaint, contentWidth - 90f)
                val rowHeight = rowPadding * 2 + 14f + (nameLines.size - 1) * 16f + 4f

                ensureSpace(rowHeight + 6f)

                canvas.drawText(category.uppercase(), marginX, y, categoryPaint)
                y += 14f

                nameLines.forEachIndexed { lineIndex, line ->
                    if (lineIndex == 0) {
                        canvas.drawText(line, marginX, y, namePaint)
                        val priceWidth = pricePaint.measureText(priceText)
                        canvas.drawText(priceText, pageWidth - marginX - priceWidth, y, pricePaint)
                    } else {
                        canvas.drawText(line, marginX, y, namePaint)
                    }
                    y += 16f
                }

                y += rowPadding
            }
        }

        fun drawIssuesSection(build: BuildUiModel) {
            drawSectionTitle("Compatibility Issues", accentColor = colorDanger)

            val reasonPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = 12.5f
                color = colorTextPrimary
                typeface = Typeface.DEFAULT
            }
            val categoryPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = 11f
                color = colorDanger
                typeface = Typeface.DEFAULT_BOLD
            }
            val contentWidth = pageWidth - marginX * 2

            build.issues.forEach { issue ->
                val category = issue.category.ifBlank { "General" }
                val reason = issue.reason.ifBlank { "Unspecified compatibility issue" }
                val reasonLines = wrapText(reason, reasonPaint, contentWidth)
                val blockHeight = 16f + reasonLines.size * 16f + 10f

                ensureSpace(blockHeight)

                canvas.drawText(category.uppercase(), marginX, y, categoryPaint)
                y += 16f
                reasonLines.forEach { line ->
                    canvas.drawText(line, marginX, y, reasonPaint)
                    y += 16f
                }
                y += 10f
            }
        }

        private fun drawSectionTitle(title: String, accentColor: Int = colorPrimary) {
            ensureSpace(40f)
            val barPaint = Paint().apply { color = accentColor }
            canvas.drawRect(marginX, y - 12f, marginX + 4f, y + 4f, barPaint)

            val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = 15f
                color = colorTextPrimary
                typeface = Typeface.DEFAULT_BOLD
            }
            canvas.drawText(title, marginX + 12f, y, titlePaint)
            y += 22f
        }

        private fun wrapText(text: String, paint: Paint, maxWidth: Float): List<String> {
            if (text.isBlank()) return listOf("")
            if (maxWidth <= 0f) return listOf(text)

            val words = text.split(" ")
            val lines = mutableListOf<String>()
            var current = StringBuilder()

            words.forEach { word ->
                val candidate = if (current.isEmpty()) word else "$current $word"
                if (paint.measureText(candidate) <= maxWidth) {
                    current = StringBuilder(candidate)
                } else {
                    if (current.isNotEmpty()) lines.add(current.toString())
                    current = StringBuilder(word)
                }
            }
            if (current.isNotEmpty()) lines.add(current.toString())
            return lines.ifEmpty { listOf(text) }
        }
    }
}