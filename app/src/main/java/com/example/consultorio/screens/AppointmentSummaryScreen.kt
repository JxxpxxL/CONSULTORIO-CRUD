package com.example.consultorio.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AppointmentSummaryScreen(
    name: String,
    phone: String,
    date: String,
    time: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.tertiaryContainer,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "✅ Cita confirmada",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Estos son los datos registrados de tu cita médica:",
            style = MaterialTheme.typography.bodyLarge
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "👤 Nombre: $name",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "📞 Teléfono: $phone",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "📅 Fecha: $date",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "⏰ Hora: $time",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Text(
            text = "Gracias por usar la aplicación del consultorio.",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}