package com.example.homeassistant.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.homeassistant.data.Currency
import com.example.homeassistant.data.ElectricityBill
import com.example.homeassistant.data.Property
import com.example.homeassistant.data.RentDuration
import com.example.homeassistant.data.ShareValue
import com.example.homeassistant.data.Shareholder
import com.example.homeassistant.data.Subscription
import com.example.homeassistant.ui.screens.PropertyEditScreen
import com.example.homeassistant.ui.screens.PropertyListScreen
import java.time.LocalDate

@Composable
fun PropertyManagerApp() {
    val navController = rememberNavController()

    // Manage property list state
    var properties by remember {
        mutableStateOf(
            listOf(
                Property(
                    id = "1",
                    name = "Sunny Garden Apartment",
                    address = "123 Maple Street, Downtown Springfield",
                    rentPrice = 1200.0,
                    rentDuration = RentDuration.MONTHLY,
                    renterName = "John Doe",
                    shareholders = listOf(
                        Shareholder("Alice Johnson", ShareValue.Percentage(60.0)),
                        Shareholder("Bob Smith", ShareValue.Percentage(40.0))
                    ),
                    subscriptions = listOf(
                        Subscription(
                            name = "main",
                            electricityBills = listOf(
                                ElectricityBill(85.50, Currency.USD, LocalDate.of(2023, 1, 15)),
                                ElectricityBill(92.30, Currency.USD, LocalDate.of(2023, 2, 15)),
                                ElectricityBill(88.75, Currency.USD, LocalDate.of(2023, 3, 15)),
                                ElectricityBill(95.20, Currency.USD, LocalDate.of(2023, 4, 15)),
                                ElectricityBill(102.40, Currency.USD, LocalDate.of(2023, 5, 15)),
                                ElectricityBill(118.60, Currency.USD, LocalDate.of(2023, 6, 15)),
                                ElectricityBill(134.80, Currency.USD, LocalDate.of(2023, 7, 15)),
                                ElectricityBill(128.90, Currency.USD, LocalDate.of(2023, 8, 15)),
                                ElectricityBill(115.30, Currency.USD, LocalDate.of(2023, 9, 15)),
                                ElectricityBill(98.70, Currency.USD, LocalDate.of(2023, 10, 15)),
                                ElectricityBill(87.40, Currency.USD, LocalDate.of(2023, 11, 15)),
                                ElectricityBill(93.80, Currency.USD, LocalDate.of(2023, 12, 15)),
                                ElectricityBill(89.60, Currency.USD, LocalDate.of(2024, 1, 15)),
                                ElectricityBill(96.50, Currency.USD, LocalDate.of(2024, 2, 15)),
                                ElectricityBill(91.20, Currency.USD, LocalDate.of(2024, 3, 15))
                            )
                        ),
                        Subscription(
                            name = "motor",
                            electricityBills = listOf(
                                ElectricityBill(45.20, Currency.USD, LocalDate.of(2023, 1, 15)),
                                ElectricityBill(38.90, Currency.USD, LocalDate.of(2023, 2, 15)),
                                ElectricityBill(42.30, Currency.USD, LocalDate.of(2023, 3, 15)),
                                ElectricityBill(48.70, Currency.USD, LocalDate.of(2023, 4, 15)),
                                ElectricityBill(51.20, Currency.USD, LocalDate.of(2023, 5, 15)),
                                ElectricityBill(58.40, Currency.USD, LocalDate.of(2023, 6, 15)),
                                ElectricityBill(62.80, Currency.USD, LocalDate.of(2023, 7, 15)),
                                ElectricityBill(59.90, Currency.USD, LocalDate.of(2023, 8, 15))
                            )
                        )
                    )
                ),
                Property(
                    id = "2",
                    name = "Cozy Studio Loft",
                    address = "456 Oak Avenue, Riverside District",
                    rentPrice = 850.0,
                    rentDuration = RentDuration.MONTHLY,
                    renterName = null,
                    shareholders = listOf(
                        Shareholder("Emma Davis", ShareValue.CurrencyValue(15000.0, Currency.USD)),
                        Shareholder("Michael Chen", ShareValue.CurrencyValue(10000.0, Currency.USD)),
                        Shareholder("Sarah Wilson", ShareValue.Percentage(30.0))
                    ),
                    subscriptions = listOf(
                        Subscription(
                            name = "main",
                            electricityBills = listOf(
                                ElectricityBill(78500.0, Currency.LBP, LocalDate.of(2022, 8, 20)),
                                ElectricityBill(82300.0, Currency.LBP, LocalDate.of(2022, 9, 20)),
                                ElectricityBill(85600.0, Currency.LBP, LocalDate.of(2022, 10, 20)),
                                ElectricityBill(88900.0, Currency.LBP, LocalDate.of(2022, 11, 20)),
                                ElectricityBill(91200.0, Currency.LBP, LocalDate.of(2022, 12, 20)),
                                ElectricityBill(94800.0, Currency.LBP, LocalDate.of(2023, 1, 20)),
                                ElectricityBill(97500.0, Currency.LBP, LocalDate.of(2023, 2, 20)),
                                ElectricityBill(99700.0, Currency.LBP, LocalDate.of(2023, 3, 20)),
                                ElectricityBill(102400.0, Currency.LBP, LocalDate.of(2023, 4, 20)),
                                ElectricityBill(105800.0, Currency.LBP, LocalDate.of(2023, 5, 20)),
                                ElectricityBill(112300.0, Currency.LBP, LocalDate.of(2023, 6, 20)),
                                ElectricityBill(118600.0, Currency.LBP, LocalDate.of(2023, 7, 20)),
                                ElectricityBill(115900.0, Currency.LBP, LocalDate.of(2023, 8, 20)),
                                ElectricityBill(109400.0, Currency.LBP, LocalDate.of(2023, 9, 20)),
                                ElectricityBill(103700.0, Currency.LBP, LocalDate.of(2023, 10, 20)),
                                ElectricityBill(98500.0, Currency.LBP, LocalDate.of(2023, 11, 20)),
                                ElectricityBill(101200.0, Currency.LBP, LocalDate.of(2023, 12, 20)),
                                ElectricityBill(96800.0, Currency.LBP, LocalDate.of(2024, 1, 20)),
                                ElectricityBill(99600.0, Currency.LBP, LocalDate.of(2024, 2, 20)),
                                ElectricityBill(108225.0, Currency.LBP, LocalDate.of(2024, 3, 20))
                            )
                        )
                    )
                ),
                Property(
                    id = "3",
                    name = "Modern Family House",
                    address = "789 Pine Boulevard, Hillside Heights",
                    rentPrice = 24000.0,
                    rentDuration = RentDuration.YEARLY,
                    renterName = "Jane Smith",
                    shareholders = listOf(
                        Shareholder("David Rodriguez", ShareValue.Percentage(50.0)),
                        Shareholder("Lisa Thompson", ShareValue.CurrencyValue(25000000.0, Currency.LBP))
                    ),
                    subscriptions = listOf(
                        Subscription(
                            name = "main",
                            electricityBills = listOf(
                                ElectricityBill(585000.0, Currency.LBP, LocalDate.of(2022, 10, 10)),
                                ElectricityBill(612000.0, Currency.LBP, LocalDate.of(2022, 11, 10)),
                                ElectricityBill(638500.0, Currency.LBP, LocalDate.of(2022, 12, 10)),
                                ElectricityBill(665200.0, Currency.LBP, LocalDate.of(2023, 1, 10)),
                                ElectricityBill(691800.0, Currency.LBP, LocalDate.of(2023, 2, 10)),
                                ElectricityBill(718400.0, Currency.LBP, LocalDate.of(2023, 3, 10)),
                                ElectricityBill(745600.0, Currency.LBP, LocalDate.of(2023, 4, 10)),
                                ElectricityBill(772300.0, Currency.LBP, LocalDate.of(2023, 5, 10)),
                                ElectricityBill(825400.0, Currency.LBP, LocalDate.of(2023, 6, 10)),
                                ElectricityBill(891200.0, Currency.LBP, LocalDate.of(2023, 7, 10)),
                                ElectricityBill(868900.0, Currency.LBP, LocalDate.of(2023, 8, 10)),
                                ElectricityBill(812600.0, Currency.LBP, LocalDate.of(2023, 9, 10)),
                                ElectricityBill(756300.0, Currency.LBP, LocalDate.of(2023, 10, 10)),
                                ElectricityBill(701800.0, Currency.LBP, LocalDate.of(2023, 11, 10)),
                                ElectricityBill(728400.0, Currency.LBP, LocalDate.of(2023, 12, 10)),
                                ElectricityBill(675000.0, Currency.LBP, LocalDate.of(2024, 1, 10)),
                                ElectricityBill(780375.0, Currency.LBP, LocalDate.of(2024, 2, 10)),
                                ElectricityBill(721125.0, Currency.LBP, LocalDate.of(2024, 3, 10))
                            )
                        ),
                        Subscription(
                            name = "motor",
                            electricityBills = listOf(
                                ElectricityBill(195000.0, Currency.LBP, LocalDate.of(2023, 6, 10)),
                                ElectricityBill(210500.0, Currency.LBP, LocalDate.of(2023, 7, 10)),
                                ElectricityBill(225800.0, Currency.LBP, LocalDate.of(2023, 8, 10)),
                                ElectricityBill(218600.0, Currency.LBP, LocalDate.of(2023, 9, 10)),
                                ElectricityBill(203400.0, Currency.LBP, LocalDate.of(2023, 10, 10)),
                                ElectricityBill(189700.0, Currency.LBP, LocalDate.of(2023, 11, 10)),
                                ElectricityBill(195300.0, Currency.LBP, LocalDate.of(2023, 12, 10)),
                                ElectricityBill(270750.0, Currency.LBP, LocalDate.of(2024, 1, 10)),
                                ElectricityBill(247950.0, Currency.LBP, LocalDate.of(2024, 2, 10))
                            )
                        )
                    )
                ),
                Property(
                    id = "4",
                    name = "Executive Penthouse",
                    address = "101 Skyline Drive, City Center",
                    rentPrice = 2800.0,
                    rentDuration = RentDuration.MONTHLY,
                    renterName = "Michael Johnson",
                    shareholders = listOf(
                        Shareholder("Jennifer Lee", ShareValue.Percentage(75.0)),
                        Shareholder("Robert Garcia", ShareValue.Percentage(25.0))
                    ),
                    subscriptions = listOf(
                        Subscription(
                            name = "main",
                            electricityBills = listOf(
                                ElectricityBill(125.60, Currency.USD, LocalDate.of(2022, 10, 5)),
                                ElectricityBill(138.90, Currency.USD, LocalDate.of(2022, 11, 5)),
                                ElectricityBill(145.30, Currency.USD, LocalDate.of(2022, 12, 5)),
                                ElectricityBill(152.80, Currency.USD, LocalDate.of(2023, 1, 5)),
                                ElectricityBill(168.40, Currency.USD, LocalDate.of(2023, 2, 5)),
                                ElectricityBill(174.20, Currency.USD, LocalDate.of(2023, 3, 5)),
                                ElectricityBill(189.60, Currency.USD, LocalDate.of(2023, 4, 5)),
                                ElectricityBill(201.30, Currency.USD, LocalDate.of(2023, 5, 5)),
                                ElectricityBill(225.80, Currency.USD, LocalDate.of(2023, 6, 5)),
                                ElectricityBill(248.90, Currency.USD, LocalDate.of(2023, 7, 5)),
                                ElectricityBill(235.40, Currency.USD, LocalDate.of(2023, 8, 5)),
                                ElectricityBill(198.70, Currency.USD, LocalDate.of(2023, 9, 5)),
                                ElectricityBill(172.50, Currency.USD, LocalDate.of(2023, 10, 5)),
                                ElectricityBill(156.80, Currency.USD, LocalDate.of(2023, 11, 5)),
                                ElectricityBill(163.20, Currency.USD, LocalDate.of(2023, 12, 5)),
                                ElectricityBill(150.75, Currency.USD, LocalDate.of(2024, 1, 5)),
                                ElectricityBill(165.20, Currency.USD, LocalDate.of(2024, 2, 5)),
                                ElectricityBill(125.60, Currency.USD, LocalDate.of(2022, 10, 5)),
                                ElectricityBill(138.90, Currency.USD, LocalDate.of(2022, 11, 5)),
                                ElectricityBill(145.30, Currency.USD, LocalDate.of(2022, 12, 5)),
                                ElectricityBill(152.80, Currency.USD, LocalDate.of(2023, 1, 5)),
                                ElectricityBill(168.40, Currency.USD, LocalDate.of(2023, 2, 5)),
                                ElectricityBill(174.20, Currency.USD, LocalDate.of(2023, 3, 5)),
                                ElectricityBill(189.60, Currency.USD, LocalDate.of(2023, 4, 5)),
                                ElectricityBill(201.30, Currency.USD, LocalDate.of(2023, 5, 5)),
                                ElectricityBill(225.80, Currency.USD, LocalDate.of(2023, 6, 5)),
                                ElectricityBill(248.90, Currency.USD, LocalDate.of(2023, 7, 5)),
                                ElectricityBill(235.40, Currency.USD, LocalDate.of(2023, 8, 5)),
                                ElectricityBill(198.70, Currency.USD, LocalDate.of(2023, 9, 5)),
                                ElectricityBill(172.50, Currency.USD, LocalDate.of(2023, 10, 5)),
                                ElectricityBill(156.80, Currency.USD, LocalDate.of(2023, 11, 5)),
                                ElectricityBill(163.20, Currency.USD, LocalDate.of(2023, 12, 5)),
                                ElectricityBill(150.75, Currency.USD, LocalDate.of(2024, 1, 5)),
                                ElectricityBill(165.20, Currency.USD, LocalDate.of(2024, 2, 5))
                            )
                        ),
                        Subscription(
                            name = "motor",
                            electricityBills = listOf(
                                ElectricityBill(72.40, Currency.USD, LocalDate.of(2023, 8, 5)),
                                ElectricityBill(68.90, Currency.USD, LocalDate.of(2023, 9, 5)),
                                ElectricityBill(75.30, Currency.USD, LocalDate.of(2023, 10, 5)),
                                ElectricityBill(81.60, Currency.USD, LocalDate.of(2023, 11, 5)),
                                ElectricityBill(78.20, Currency.USD, LocalDate.of(2023, 12, 5)),
                                ElectricityBill(95.40, Currency.USD, LocalDate.of(2024, 1, 5))
                            )
                        )
                    )
                ),
                Property(
                    id = "5",
                    name = "Charming Cottage",
                    address = "222 Willow Lane, Peaceful Valley",
                    rentPrice = 950.0,
                    rentDuration = RentDuration.MONTHLY,
                    renterName = null,
                    shareholders = listOf(
                        Shareholder("Amanda Clark", ShareValue.CurrencyValue(8000.0, Currency.USD)),
                        Shareholder("Kevin Martinez", ShareValue.CurrencyValue(7000.0, Currency.USD)),
                        Shareholder("Rachel Adams", ShareValue.CurrencyValue(5000.0, Currency.USD))
                    ),
                    subscriptions = listOf(
                        Subscription(
                            name = "main",
                            electricityBills = listOf(
                                ElectricityBill(65.20, Currency.USD, LocalDate.of(2022, 6, 25)),
                                ElectricityBill(71.80, Currency.USD, LocalDate.of(2022, 7, 25)),
                                ElectricityBill(78.90, Currency.USD, LocalDate.of(2022, 8, 25)),
                                ElectricityBill(84.30, Currency.USD, LocalDate.of(2022, 9, 25)),
                                ElectricityBill(76.50, Currency.USD, LocalDate.of(2022, 10, 25)),
                                ElectricityBill(69.40, Currency.USD, LocalDate.of(2022, 11, 25)),
                                ElectricityBill(72.80, Currency.USD, LocalDate.of(2022, 12, 25)),
                                ElectricityBill(75.60, Currency.USD, LocalDate.of(2023, 1, 25)),
                                ElectricityBill(81.20, Currency.USD, LocalDate.of(2023, 2, 25)),
                                ElectricityBill(86.90, Currency.USD, LocalDate.of(2023, 3, 25)),
                                ElectricityBill(93.40, Currency.USD, LocalDate.of(2023, 4, 25)),
                                ElectricityBill(98.70, Currency.USD, LocalDate.of(2023, 5, 25)),
                                ElectricityBill(105.80, Currency.USD, LocalDate.of(2023, 6, 25)),
                                ElectricityBill(112.30, Currency.USD, LocalDate.of(2024, 7, 25)),
                                ElectricityBill(108.60, Currency.USD, LocalDate.of(2024, 8, 25)),
                                ElectricityBill(95.40, Currency.USD, LocalDate.of(2024, 9, 25)),
                                ElectricityBill(82.70, Currency.USD, LocalDate.of(2024, 10, 25)),
                                ElectricityBill(77.20, Currency.USD, LocalDate.of(2024, 11, 25)),
                                ElectricityBill(79.90, Currency.USD, LocalDate.of(2024, 12, 25)),
                                ElectricityBill(83.50, Currency.USD, LocalDate.of(2024, 1, 25))
                            )
                        )
                    )
                )
            )
        )
    }

    NavHost(
        navController = navController,
        startDestination = "property_list"
    ) {
        composable("property_list") {
            PropertyListScreen(
                properties = properties,
                onPropertyClick = { property ->
                    navController.navigate("property_edit/${property.id}")
                },
                onAddProperty = {
                    navController.navigate("property_add")
                }
            )
        }

        composable("property_edit/{propertyId}") { backStackEntry ->
            val propertyId = backStackEntry.arguments?.getString("propertyId")
            val property = properties.find { it.id == propertyId }

            PropertyEditScreen(
                property = property,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSaveProperty = { updatedProperty ->
                    properties = properties.map {
                        if (it.id == updatedProperty.id) updatedProperty else it
                    }
                    navController.popBackStack()
                },
                onDeleteProperty = { propertyToDelete ->
                    properties = properties.filter { it.id != propertyToDelete.id }
                    navController.popBackStack()
                }
            )
        }

        composable("property_add") {
            PropertyEditScreen(
                property = null,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSaveProperty = { newProperty ->
                    properties = properties + newProperty
                    navController.popBackStack()
                }
            )
        }
    }
}