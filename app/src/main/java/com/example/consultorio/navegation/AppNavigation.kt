package com.example.consultorio.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.consultorio.screens.AppointmentDateTimeScreen
import com.example.consultorio.screens.AppointmentSummaryScreen
import com.example.consultorio.screens.PersonalDataScreen

sealed class Screen(val route: String) {
    object PersonalData : Screen("personal_data")

    object AppointmentDateTime : Screen("appointment_date_time/{name}/{phone}") {
        fun createRoute(name: String, phone: String): String {
            return "appointment_date_time/$name/$phone"
        }
    }

    object AppointmentSummary : Screen("appointment_summary/{name}/{phone}/{date}/{time}") {
        fun createRoute(name: String, phone: String, date: String, time: String): String {
            return "appointment_summary/$name/$phone/$date/$time"
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.PersonalData.route
    ) {
        composable(Screen.PersonalData.route) {
            PersonalDataScreen(
                onContinue = { name, phone ->
                    navController.navigate(
                        Screen.AppointmentDateTime.createRoute(name, phone)
                    )
                }
            )
        }

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
                    navController.navigate(
                        Screen.AppointmentSummary.createRoute(name, phone, date, time)
                    )
                }
            )
        }

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
                time = time
            )
        }
    }
}