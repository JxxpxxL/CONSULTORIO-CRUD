package com.example.consultorio.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.consultorio.data.Cita
import com.example.consultorio.data.DatabaseHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentDateTimeScreen(
    name: String,
    phone: String,
    onConfirm: (String, String) -> Unit
) {
    val context = LocalContext.current
    val dbHelper = remember { DatabaseHelper(context) }
    
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState(initialHour = 8, initialMinute = 0, is24Hour = false)

    var selectedDate by remember { mutableStateOf("") }
    var selectedTime by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.colorScheme.background)))
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(text = "📅 Selección de cita", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(text = "Paciente: $name", style = MaterialTheme.typography.titleMedium)
                Text(text = "Teléfono: $phone", style = MaterialTheme.typography.bodyLarge)
            }
        }

        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(text = "Selecciona la fecha", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                DatePicker(state = datePickerState, modifier = Modifier.fillMaxWidth())
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                        selectedDate = formatter.format(Date(it))
                    }
                }, modifier = Modifier.fillMaxWidth()) { Text("Guardar fecha") }
                if (selectedDate.isNotEmpty()) Text("✅ Fecha: $selectedDate")
            }
        }

        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(text = "Selecciona la hora", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                TimePicker(state = timePickerState)
                Button(onClick = {
                    selectedTime = String.format(Locale.getDefault(), "%02d:%02d", timePickerState.hour, timePickerState.minute)
                }, modifier = Modifier.fillMaxWidth()) { Text("Guardar hora") }
                if (selectedTime.isNotEmpty()) Text("✅ Hora: $selectedTime")
            }
        }

        if (selectedDate.isNotEmpty() && selectedTime.isNotEmpty()) {
            Button(
                onClick = {
                    // Guardar en la base de datos
                    val nuevaCita = Cita(nombre = name, telefono = phone, fecha = selectedDate, hora = selectedTime)
                    dbHelper.insertarCita(nuevaCita)
                    onConfirm(selectedDate, selectedTime)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(text = "Confirmar Cita", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}
