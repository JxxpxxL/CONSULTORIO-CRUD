package com.example.consultorio.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.consultorio.data.Cita
import com.example.consultorio.data.DatabaseHelper
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAppointmentScreen(
    citaId: Int,
    onConfirm: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val dbHelper = remember { DatabaseHelper(context) }
    val existingCita = remember { dbHelper.obtenerTodasLasCitas().find { it.id == citaId } }

    if (existingCita == null) {
        onBack()
        return
    }

    var name by remember { mutableStateOf(existingCita.nombre) }
    var phone by remember { mutableStateOf(existingCita.telefono) }
    var date by remember { mutableStateOf(existingCita.fecha) }
    var time by remember { mutableStateOf(existingCita.hora) }

    var nameError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState(
        initialHour = time.split(":")[0].toInt(),
        initialMinute = time.split(":")[1].toInt(),
        is24Hour = false
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.verticalGradient(listOf(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.background)))
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "✏️ Editar Cita", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

        OutlinedTextField(
            value = name,
            onValueChange = { name = it; nameError = it.isEmpty() },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth(),
            isError = nameError
        )

        OutlinedTextField(
            value = phone,
            onValueChange = { if (it.length <= 10) phone = it; phoneError = it.length != 10 },
            label = { Text("Número de teléfono") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = phoneError
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Fecha: $date", style = MaterialTheme.typography.bodyLarge)
                DatePicker(state = datePickerState, modifier = Modifier.fillMaxWidth())
                Button(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                        date = formatter.format(Date(it))
                    }
                }) { Text("Cambiar Fecha") }
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Hora: $time", style = MaterialTheme.typography.bodyLarge)
                TimePicker(state = timePickerState)
                Button(onClick = {
                    time = String.format(Locale.getDefault(), "%02d:%02d", timePickerState.hour, timePickerState.minute)
                }) { Text("Cambiar Hora") }
            }
        }

        Button(
            onClick = {
                if (name.isNotEmpty() && phone.length == 10) {
                    dbHelper.actualizarCita(Cita(citaId, name, phone, date, time))
                    onConfirm()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text("Guardar Cambios")
        }

        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Cancelar")
        }
    }
}
