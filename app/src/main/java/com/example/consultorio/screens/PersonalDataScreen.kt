package com.example.consultorio.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun PersonalDataScreen(
    onContinue: (String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }

    fun validateName(value: String): Boolean {
        return value.trim().isNotEmpty()
    }

    fun validatePhone(value: String): Boolean {
        return value.matches(Regex("^\\d{10}$"))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.background
                    )
                )
            )
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "🏥 Consultorio Médico",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Agenda tu cita de forma rápida y sencilla.",
            style = MaterialTheme.typography.bodyLarge
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Datos personales",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = !validateName(it)
                    },
                    label = { Text("Nombre completo") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = nameError,
                    shape = RoundedCornerShape(14.dp)
                )

                if (nameError) {
                    Text(
                        text = "El nombre no puede estar vacío.",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                OutlinedTextField(
                    value = phone,
                    onValueChange = {
                        if (it.length <= 10 && it.all { char -> char.isDigit() }) {
                            phone = it
                            phoneError = !validatePhone(it)
                        } else if (it.isEmpty()) {
                            phone = it
                            phoneError = true
                        }
                    },
                    label = { Text("Número de teléfono") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = phoneError,
                    shape = RoundedCornerShape(14.dp)
                )

                if (phoneError) {
                    Text(
                        text = "El teléfono debe tener exactamente 10 dígitos.",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Button(
                    onClick = {
                        nameError = !validateName(name)
                        phoneError = !validatePhone(phone)

                        if (!nameError && !phoneError) {
                            onContinue(name, phone)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(14.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Continuar",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}