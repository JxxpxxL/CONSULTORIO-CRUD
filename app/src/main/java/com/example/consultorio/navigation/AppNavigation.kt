package com.example.consultorio.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.consultorio.screens.*

sealed class Screen(val route: String) {
    object PersonalData : Screen("personal_data")
    
    object AppointmentDateTime : Screen("appointment_date_time/{name}/{phone}") {
        fun createRoute(name: String, phone: String) = "appointment_date_time/$name/$phone"
    }

    object AppointmentSummary : Screen("appointment_summary/{name}/{phone}/{date}/{time}") {
        fun createRoute(name: String, phone: String, date: String, time: String) = 
            "appointment_summary/$name/$phone/$date/$time"
    }

    object AppointmentList : Screen("appointment_list")

    object EditAppointment : Screen("edit_appointment/{citaId}") {
        fun createRoute(citaId: Int) = "edit_appointment/$citaId"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.PersonalData.route
    ) {
        // Pantalla 1: Ingreso de Datos Personales
        composable(Screen.PersonalData.route) {
            PersonalDataScreen(
                onContinue = { name, phone ->
                    navController.navigate(Screen.AppointmentDateTime.createRoute(name, phone))
                },
                onViewList = {
                    navController.navigate(Screen.AppointmentList.route)
                }
            )
        }

        // Pantalla 2: Selección de Fecha y Hora (Create)
        composable(
            route = Screen.AppointmentDateTime.route,
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("phone") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val phone = backStackEntry.arguments?.getString("phone") ?: ""

            AppointmentDateTimeScreen(
                name = name,
                phone = phone,
                onConfirm = { date, time ->
                    navController.navigate(Screen.AppointmentSummary.createRoute(name, phone, date, time))
                }
            )
        }

        // Pantalla de Resumen de Cita
        composable(
            route = Screen.AppointmentSummary.route,
            arguments = listOf(
                navArgument("name") { type = NavType.StringType },
                navArgument("phone") { type = NavType.StringType },
                navArgument("date") { type = NavType.StringType },
                navArgument("time") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            val date = backStackEntry.arguments?.getString("date") ?: ""
            val time = backStackEntry.arguments?.getString("time") ?: ""

            AppointmentSummaryScreen(
                name = name,
                phone = phone,
                date = date,
                time = time,
                onGoToList = {
                    navController.navigate(Screen.AppointmentList.route) {
                        popUpTo(Screen.PersonalData.route)
                    }
                }
            )
        }

        // Pantalla 3: Lista de Citas (Read & Delete)
        composable(Screen.AppointmentList.route) {
            AppointmentListScreen(
                onEditCita = { cita ->
                    navController.navigate(Screen.EditAppointment.createRoute(cita.id))
                },
                onBack = { navController.popBackStack() }
            )
        }

        // Pantalla de Edición (Update)
        composable(
            route = Screen.EditAppointment.route,
            arguments = listOf(navArgument("citaId") { type = NavType.IntType })
        ) { backStackEntry ->
            val citaId = backStackEntry.arguments?.getInt("citaId") ?: 0
            EditAppointmentScreen(
                citaId = citaId,
                onConfirm = {
                    navController.popBackStack()
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}
