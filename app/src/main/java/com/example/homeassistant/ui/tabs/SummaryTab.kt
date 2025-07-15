package com.example.homeassistant.ui.tabs

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.homeassistant.data.Currency
import com.example.homeassistant.data.ElectricityBill
import com.example.homeassistant.data.Subscription
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

data class ChartDataPoint(
    val x: Float,
    val y: Float,
    val date: LocalDate,
    val amount: Double
)

@Composable
fun SummaryTab(
    subscriptions: List<Subscription>,
    filterFromDate: LocalDate? = null,
    filterToDate: LocalDate? = null,
    filterCurrency: Currency? = null
) {
    // Filter subscriptions based on current filters, default to current year if no date filters specified
    val filteredSubscriptions by remember(subscriptions, filterFromDate, filterToDate, filterCurrency) {
        derivedStateOf {
            val currentYear = LocalDate.now().year
            val defaultFromDate = if (filterFromDate == null && filterToDate == null) {
                LocalDate.of(currentYear, 1, 1)
            } else {
                filterFromDate
            }
            val defaultToDate = if (filterFromDate == null && filterToDate == null) {
                LocalDate.of(currentYear, 12, 31)
            } else {
                filterToDate
            }

            subscriptions.map { subscription ->
                val filteredBills = subscription.electricityBills.filter { bill ->
                    val dateMatch = (defaultFromDate == null || !bill.paymentDate.isBefore(defaultFromDate)) &&
                            (defaultToDate == null || !bill.paymentDate.isAfter(defaultToDate))
                    val currencyMatch = filterCurrency == null || bill.currency == filterCurrency
                    dateMatch && currencyMatch
                }
                subscription.copy(electricityBills = filteredBills)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Bills Summary",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        if (subscriptions.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No subscriptions available",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            filteredSubscriptions.forEach { subscription ->
                SubscriptionSummaryCard(
                    subscription = subscription,
                    originalSubscription = subscriptions.find { it.name == subscription.name }
                )
            }
        }

        // Add some bottom spacing for the FAB
        Spacer(modifier = Modifier.height(80.dp))
    }
}

@Composable
private fun BillsLineChart(
    subscription: Subscription,
    modifier: Modifier = Modifier
) {
    if (subscription.electricityBills.isEmpty()) {
        return
    }

    val billsByCurrency = subscription.electricityBills.groupBy { it.currency }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Bills Trend Over Time",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )

        billsByCurrency.forEach { (currency, bills) ->
            if (bills.isNotEmpty()) {
                CurrencyLineChart(
                    currency = currency,
                    bills = bills,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun CurrencyLineChart(
    currency: Currency,
    bills: List<ElectricityBill>,
    modifier: Modifier = Modifier
) {
    val sortedBills = remember(bills) {
        bills.sortedBy { it.paymentDate }
    }

    if (sortedBills.isEmpty()) return

    val chartData = remember(sortedBills) {
        val earliestDate = sortedBills.first().paymentDate
        sortedBills.map { bill ->
            ChartDataPoint(
                x = ChronoUnit.DAYS.between(earliestDate, bill.paymentDate).toFloat(),
                y = bill.amount.toFloat(),
                date = bill.paymentDate,
                amount = bill.amount
            )
        }
    }

    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "${currency.name} (${currency.symbol}) Bills",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (chartData.size >= 2) {
                SimpleLineChart(
                    data = chartData,
                    currency = currency,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    lineColor = MaterialTheme.colorScheme.primary
                )
            } else if (chartData.size == 1) {
                Text(
                    text = "Single data point: ${String.format("%.2f", chartData.first().amount)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun SimpleLineChart(
    data: List<ChartDataPoint>,
    currency: Currency,
    modifier: Modifier = Modifier,
    lineColor: Color = Color.Blue
) {
    val density = LocalDensity.current
    val textColor = MaterialTheme.colorScheme.onSurface

    Canvas(modifier = modifier) {
        if (data.size < 2) return@Canvas

        val leftPadding = 60.dp.toPx()
        val rightPadding = 20.dp.toPx()
        val topPadding = 20.dp.toPx()
        val bottomPadding = 50.dp.toPx()

        val chartWidth = size.width - leftPadding - rightPadding
        val chartHeight = size.height - topPadding - bottomPadding

        // Find min and max values
        val minX = data.minOf { it.x }
        val maxX = data.maxOf { it.x }
        val minY = data.minOf { it.y }
        val maxY = data.maxOf { it.y }

        val xRange = maxX - minX
        val yRange = maxY - minY

        // Chart bounds
        val chartLeft = leftPadding
        val chartTop = topPadding
        val chartRight = leftPadding + chartWidth
        val chartBottom = topPadding + chartHeight

        // Draw year range indicator above the chart
        val earliestDate = data.minByOrNull { it.date }?.date
        val latestDate = data.maxByOrNull { it.date }?.date
        if (earliestDate != null && latestDate != null) {
            val yearRange = if (earliestDate.year == latestDate.year) {
                "${earliestDate.year}"
            } else {
                "${earliestDate.year} - ${latestDate.year}"
            }

            drawContext.canvas.nativeCanvas.drawText(
                yearRange,
                (chartLeft + chartRight) / 2f,
                chartTop - 15.dp.toPx(),
                android.graphics.Paint().apply {
                    color = textColor.copy(alpha = 0.7f).toArgb()
                    textSize = with(density) { 11.sp.toPx() }
                    textAlign = android.graphics.Paint.Align.CENTER
                    isAntiAlias = true
                    isFakeBoldText = true
                }
            )
        }

        // Draw axis lines
        drawLine(
            color = textColor,
            start = Offset(chartLeft, chartBottom),
            end = Offset(chartRight, chartBottom),
            strokeWidth = 2.dp.toPx()
        )

        drawLine(
            color = textColor,
            start = Offset(chartLeft, chartTop),
            end = Offset(chartLeft, chartBottom),
            strokeWidth = 2.dp.toPx()
        )

        // Calculate grid lines and labels
        val yLabelCount = 5
        val xLabelCount = min(data.size, 5)

        // Draw Y-axis grid lines and labels
        for (i in 0..yLabelCount) {
            val y = chartBottom - (i.toFloat() / yLabelCount) * chartHeight
            val value = minY + (i.toFloat() / yLabelCount) * yRange

            // Grid line
            drawLine(
                color = textColor.copy(alpha = 0.2f),
                start = Offset(chartLeft, y),
                end = Offset(chartRight, y),
                strokeWidth = 1.dp.toPx()
            )

            // Y-axis label
            drawContext.canvas.nativeCanvas.drawText(
                String.format("%.0f", value),
                chartLeft - 10.dp.toPx(),
                y + 5.dp.toPx(),
                android.graphics.Paint().apply {
                    color = textColor.toArgb()
                    textSize = with(density) { 10.sp.toPx() }
                    textAlign = android.graphics.Paint.Align.RIGHT
                    isAntiAlias = true
                }
            )
        }

        // Draw X-axis grid lines and labels
        for (i in 0 until xLabelCount) {
            val dataIndex = (i.toFloat() / (xLabelCount - 1) * (data.size - 1)).roundToInt()
            val dataPoint = data[dataIndex]
            val x = chartLeft + ((dataPoint.x - minX) / max(xRange, 1f)) * chartWidth

            // Grid line
            drawLine(
                color = textColor.copy(alpha = 0.2f),
                start = Offset(x, chartTop),
                end = Offset(x, chartBottom),
                strokeWidth = 1.dp.toPx()
            )

            // X-axis label (date)
            val dateFormatter = DateTimeFormatter.ofPattern("MMM dd")
            val dateText = dataPoint.date.format(dateFormatter)

            drawContext.canvas.nativeCanvas.drawText(
                dateText,
                x,
                chartBottom + 25.dp.toPx(),
                android.graphics.Paint().apply {
                    color = textColor.toArgb()
                    textSize = with(density) { 10.sp.toPx() }
                    textAlign = android.graphics.Paint.Align.CENTER
                    isAntiAlias = true
                }
            )
        }

        // Convert data points to screen coordinates
        val points = data.map { dataPoint ->
            val screenX = chartLeft + ((dataPoint.x - minX) / max(xRange, 1f)) * chartWidth
            val screenY = chartBottom - ((dataPoint.y - minY) / max(yRange, 1f)) * chartHeight
            Offset(screenX, screenY)
        }

        // Draw the line
        val path = Path()
        path.moveTo(points.first().x, points.first().y)
        for (i in 1 until points.size) {
            path.lineTo(points[i].x, points[i].y)
        }

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 3.dp.toPx())
        )

        // Draw points
        points.forEach { point ->
            drawCircle(
                color = lineColor,
                radius = 4.dp.toPx(),
                center = point
            )
        }
    }
}

@Composable
private fun SubscriptionSummaryCard(
    subscription: Subscription,
    originalSubscription: Subscription?
) {
    // Calculate totals by currency for filtered bills
    val totalsByCurrency = subscription.electricityBills
        .groupBy { it.currency }
        .mapValues { (_, bills) -> bills.sumOf { it.amount } }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = subscription.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (subscription.electricityBills.isEmpty()) {
                val hasOriginalBills = originalSubscription?.electricityBills?.isNotEmpty() == true
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (hasOriginalBills) "No bills match the current filter" else "No bills recorded",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                Text(
                    text = "Total Bills: ${subscription.electricityBills.size}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "Total Amounts by Currency:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )

                totalsByCurrency.forEach { (currency, total) ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${currency.name} (${currency.symbol})",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = String.format("%.2f", total),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                // Add line chart for subscription
                BillsLineChart(
                    subscription = subscription,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
