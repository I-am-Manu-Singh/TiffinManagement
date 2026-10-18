package com.creativeblisslabs.tiffinmanagement.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens

enum class NavItem(val label: String, val icon: ImageVector) {
    Dashboard("Dashboard", Icons.Default.Home),
    Kitchen("Kitchen", Icons.Default.SoupKitchen),
    MealCategories("Categories", Icons.Default.RestaurantMenu),
    Plans("Subscriptions", Icons.Default.DateRange),
    Customers("Customers", Icons.Default.People),
    More("More", Icons.Default.MoreHoriz),
    Expenses("Expenses", Icons.Default.MoreHoriz),
    RecurringExpenses("Recurring", Icons.Default.MoreHoriz),
    CategoryBranchHolds("Holds", Icons.Default.MoreHoriz),
    StaffDirectory("Staff", Icons.Default.MoreHoriz),
    NotificationSettings("Notifications", Icons.Default.MoreHoriz),
    AddCustomer("Add Customer", Icons.Default.People),
    CreateSubscription("New Subscription", Icons.Default.DateRange),
    CustomerDetailsLedger("Customer Ledger", Icons.Default.People),
    SubscriptionDetails("Subscription Details", Icons.Default.DateRange),
    MealPlans("Meal Plans", Icons.Default.DateRange),
    UpdateMealPlan("Update Meal Plan", Icons.Default.DateRange),
    UpdateBranch("Update Branch", Icons.Default.Home),
    UpdateDeliveryRoute("Update Route", Icons.Default.MoreHoriz),
    Profile("Profile", Icons.Default.People),
    EditProfile("Edit Profile", Icons.Default.People),
    Settings("Settings", Icons.Default.MoreHoriz),
    Notifications("Notifications Inbox", Icons.Default.MoreHoriz),
    Splash("Splash", Icons.Default.Home),
    OnboardingKitchenPlans("Onboarding Plans", Icons.Default.SoupKitchen),
    OnboardingDeliveryLedger("Onboarding Ledger", Icons.Default.MoreHoriz),
    OnboardingKitchenSetup("Onboarding Setup", Icons.Default.SoupKitchen),
    Login("Login", Icons.Default.People),
    CreateAccount("Create Account", Icons.Default.People),
    ForgotPassword("Forgot Password", Icons.Default.People),
    EmailOtpVerification("Verify OTP", Icons.Default.People),
    PrivacyPolicy("Privacy Policy", Icons.Default.MoreHoriz),
    TermsOfService("Terms of Service", Icons.Default.MoreHoriz),
    HelpCenter("Help Center", Icons.Default.MoreHoriz),
    FaqsArticles("FAQs & Guides", Icons.Default.MoreHoriz),
    ContactSupport("Contact Support", Icons.Default.MoreHoriz),
    AppInfo("App & Kitchen Info", Icons.Default.MoreHoriz),
    LegalCenterHub("Legal Center", Icons.Default.MoreHoriz),
    CancellationPolicy("Cancellation Policy", Icons.Default.MoreHoriz),
    RefundPolicy("Refund Policy", Icons.Default.MoreHoriz),
    SecurityPolicy("Security Policy", Icons.Default.MoreHoriz),
    DataProcessingAddendum("Data Processing Addendum", Icons.Default.MoreHoriz),

    // Customer / Subscriber Portal Tabs & Screens (Folder 11)
    CustomerDashboard("Today", Icons.Default.Home),
    CustomerOrders("Orders", Icons.Default.ReceiptLong),
    CustomerMealCalendar("Schedule", Icons.Default.DateRange),
    CustomerWalletInvoices("Wallet", Icons.Default.Home),
    CustomerDeliveryProfile("Profile", Icons.Default.People),
    CustomerOrderGuestMeal("Guest Meal", Icons.Default.People),
    CustomerProfileEditor("Edit Profile", Icons.Default.People),

    // Folder 12: Customer Extended Screens
    CustomerLiveDeliveryTracker("Live Tracker", Icons.Default.Home),
    CustomerChangeUpgradePlan("Change Plan", Icons.Default.DateRange),
    CustomerMealRatingFeedback("Rate Meal", Icons.Default.People),
    CustomerMonthlyMenuSchedule("Monthly Menu", Icons.Default.DateRange),
    CustomerReportMealIssue("Report Issue", Icons.Default.MoreHoriz),
    CustomerSubscriberNotifications("Notifications", Icons.Default.MoreHoriz),

    // Folder 13: Rider / Driver Screens
    RiderDashboard("Run", Icons.Default.Home),
    RiderStops("Stops", Icons.Default.MoreHoriz),
    RiderBoxes("Boxes", Icons.Default.SoupKitchen),
    RiderEarnings("Earnings", Icons.Default.DateRange),
    RiderDropVerification("Drop Verification", Icons.Default.People),
    RiderReportIncident("Report Incident", Icons.Default.MoreHoriz),
    RiderProfileView("Rider Profile", Icons.Default.People),
    RiderNotifications("Rider Notifications", Icons.Default.MoreHoriz)
}

data class RiderNavTab(val item: NavItem, val label: String, val icon: ImageVector)

@Composable
fun TiffinRiderBottomNavigation(
    selectedItem: NavItem,
    onItemSelected: (NavItem) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorTokens.SurfaceContainerLowest)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(ColorTokens.BorderSubtle)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                 .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val riderTabs = listOf(
                RiderNavTab(NavItem.RiderDashboard, "Run", Icons.Default.TwoWheeler),
                RiderNavTab(NavItem.RiderStops, "Stops", Icons.Default.FormatListNumbered),
                RiderNavTab(NavItem.RiderBoxes, "Boxes", Icons.Default.Inventory2),
                RiderNavTab(NavItem.RiderEarnings, "Earnings", Icons.Default.Payments)
            )

            riderTabs.forEach { tab ->
                val isSelected = selectedItem == tab.item ||
                    (tab.item == NavItem.RiderDashboard && selectedItem == NavItem.RiderDropVerification) ||
                    (tab.item == NavItem.RiderStops && (selectedItem == NavItem.RiderReportIncident || selectedItem == NavItem.RiderNotifications)) ||
                    (tab.item == NavItem.RiderEarnings && selectedItem == NavItem.RiderProfileView)

                val activeColor = ColorTokens.Primary
                val inactiveColor = ColorTokens.Secondary

                Column(
                    modifier = Modifier
                        .clickable { onItemSelected(tab.item) }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                  

                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.label,
                        tint = if (isSelected) activeColor else inactiveColor,
                        modifier = Modifier.size(24.dp)
                    )

                    Text(
                        text = tab.label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) activeColor else inactiveColor
                    )
                }
            }
        }
    }
}

@Composable
fun TiffinCustomerBottomNavigation(
    selectedItem: NavItem,
    onItemSelected: (NavItem) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorTokens.SurfaceContainerLowest)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(ColorTokens.BorderSubtle)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                 .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val customerTabs = listOf(
                NavItem.CustomerDashboard to "Today",
                NavItem.CustomerOrders to "Orders",
                NavItem.CustomerMealCalendar to "Schedule",
                NavItem.CustomerWalletInvoices to "Wallet",
                NavItem.Profile to "Profile"
            )

            customerTabs.forEach { (item, label) ->
                val isSelected = selectedItem == item ||
                    (item == NavItem.CustomerOrders && (selectedItem == NavItem.CustomerOrderGuestMeal || selectedItem == NavItem.CustomerLiveDeliveryTracker)) ||
                    (item == NavItem.Profile && (selectedItem == NavItem.CustomerDeliveryProfile || selectedItem == NavItem.CustomerProfileEditor || selectedItem == NavItem.CustomerMealRatingFeedback || selectedItem == NavItem.CustomerReportMealIssue || selectedItem == NavItem.CustomerSubscriberNotifications || selectedItem == NavItem.Settings || selectedItem == NavItem.Notifications)) ||
                    (item == NavItem.CustomerMealCalendar && (selectedItem == NavItem.CustomerMonthlyMenuSchedule || selectedItem == NavItem.CustomerChangeUpgradePlan))

                val activeColor = ColorTokens.Primary
                val inactiveColor = ColorTokens.Secondary

                val iconVector = when (item) {
                    NavItem.CustomerDashboard -> Icons.Default.Home
                    NavItem.CustomerOrders -> Icons.Default.ReceiptLong
                    NavItem.CustomerMealCalendar -> Icons.Default.DateRange
                    NavItem.CustomerWalletInvoices -> Icons.Default.AccountBalanceWallet
                    NavItem.Profile -> Icons.Default.AccountCircle
                    else -> Icons.Default.Home
                }

                Column(
                    modifier = Modifier
                        .clickable { onItemSelected(item) }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                   
                    Icon(
                        imageVector = iconVector,
                        contentDescription = label,
                        tint = if (isSelected) activeColor else inactiveColor,
                        modifier = Modifier.size(24.dp)
                    )

                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) activeColor else inactiveColor
                    )
                }
            }
        }
    }
}

@Composable
fun TiffinBottomNavigation(
    selectedItem: NavItem,
    onItemSelected: (NavItem) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorTokens.SurfaceContainerLowest)
    ) {
        // Divider hairline at top of navigation bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(ColorTokens.BorderSubtle)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                 .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val mainTabs = listOf(
                NavItem.Dashboard,
                NavItem.Kitchen,
                NavItem.MealCategories,
                NavItem.Plans,
                NavItem.Customers,
                NavItem.More
            )
            mainTabs.forEach { item ->
                val isSelected = selectedItem == item ||
                    (item == NavItem.MealCategories && (selectedItem == NavItem.MealPlans || selectedItem == NavItem.UpdateMealPlan)) ||
                    (item == NavItem.More && (
                        selectedItem == NavItem.Expenses ||
                        selectedItem == NavItem.RecurringExpenses ||
                        selectedItem == NavItem.CategoryBranchHolds ||
                        selectedItem == NavItem.StaffDirectory ||
                        selectedItem == NavItem.NotificationSettings
                    ))
                val activeColor = ColorTokens.Primary
                val inactiveColor = ColorTokens.Secondary

                Column(
                    modifier = Modifier
                        .clickable { onItemSelected(item) }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                  

                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected) activeColor else inactiveColor,
                        modifier = Modifier.size(24.dp)
                    )

                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) activeColor else inactiveColor
                    )
                }
            }
        }
    }
}

