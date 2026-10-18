package com.creativeblisslabs.tiffinmanagement

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.runtime.collectAsState
import com.creativeblisslabs.tiffinmanagement.data.repository.TiffinRepository
import com.creativeblisslabs.tiffinmanagement.theme.ColorTokens
import com.creativeblisslabs.tiffinmanagement.theme.TiffinTheme
import com.creativeblisslabs.tiffinmanagement.ui.components.CommonBackHandler
import com.creativeblisslabs.tiffinmanagement.ui.components.NavItem
import com.creativeblisslabs.tiffinmanagement.ui.components.TiffinBottomNavigation
import com.creativeblisslabs.tiffinmanagement.ui.components.TiffinTopHeader
import com.creativeblisslabs.tiffinmanagement.ui.screens.*
import com.creativeblisslabs.tiffinmanagement.ui.screens.admin.*
import com.creativeblisslabs.tiffinmanagement.ui.screens.customer.*
import com.creativeblisslabs.tiffinmanagement.ui.screens.rider.*
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    TiffinTheme {
        val authState by TiffinRepository.authState.collectAsState()
        val initialNav = remember {
            val user = TiffinRepository.authState.value.currentUser
            when {
                user == null -> NavItem.Login
                user.role.equals("customer", ignoreCase = true) || user.role.equals("subscriber", ignoreCase = true) -> NavItem.CustomerDashboard
                user.role.equals("rider", ignoreCase = true) || user.role.equals("driver", ignoreCase = true) -> NavItem.RiderDashboard
                else -> NavItem.Dashboard
            }
        }
        val navBackStack = remember { mutableStateListOf(initialNav) }
        var currentNav by remember { mutableStateOf(initialNav) }
        var activeCustomerId by remember { mutableStateOf<String?>(null) }
        var activeSubscriptionId by remember { mutableStateOf<String?>(null) }
        var activeMealPlanId by remember { mutableStateOf<String?>(null) }
        val branch by TiffinRepository.currentBranch.collectAsState()

        fun navigateTo(nav: NavItem) {
            if (navBackStack.lastOrNull() != nav) {
                navBackStack.add(nav)
            }
            currentNav = nav
        }

        fun navigateBack(): Boolean {
            if (navBackStack.size > 1) {
                navBackStack.removeAt(navBackStack.lastIndex)
                currentNav = navBackStack.last()
                return true
            }
            return false
        }

        fun resetTo(nav: NavItem) {
            navBackStack.clear()
            navBackStack.add(nav)
            currentNav = nav
        }

        // Platform back button handler across Android and desktop/web/iOS
        CommonBackHandler(enabled = navBackStack.size > 1) {
            navigateBack()
        }

        androidx.compose.runtime.LaunchedEffect(authState.currentUser) {
            val user = authState.currentUser
            if (user != null && currentNav == NavItem.Login) {
                val targetNav = when {
                    user.role.equals("customer", ignoreCase = true) || user.role.equals("subscriber", ignoreCase = true) -> NavItem.CustomerDashboard
                    user.role.equals("rider", ignoreCase = true) || user.role.equals("driver", ignoreCase = true) -> NavItem.RiderDashboard
                    else -> NavItem.Dashboard
                }
                resetTo(targetNav)
            } else if (user == null && currentNav != NavItem.CreateAccount && currentNav != NavItem.ForgotPassword && currentNav != NavItem.EmailOtpVerification && currentNav != NavItem.PrivacyPolicy && currentNav != NavItem.TermsOfService) {
                resetTo(NavItem.Login)
            }
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = ColorTokens.Surface
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                val isCustomerPortalScreen = currentNav == NavItem.CustomerDashboard ||
                        currentNav == NavItem.CustomerOrders ||
                        currentNav == NavItem.CustomerDeliveryProfile ||
                        currentNav == NavItem.CustomerMealCalendar ||
                        currentNav == NavItem.CustomerWalletInvoices ||
                        currentNav == NavItem.CustomerOrderGuestMeal ||
                        currentNav == NavItem.CustomerProfileEditor ||
                        currentNav == NavItem.CustomerLiveDeliveryTracker ||
                        currentNav == NavItem.CustomerChangeUpgradePlan ||
                        currentNav == NavItem.CustomerMealRatingFeedback ||
                        currentNav == NavItem.CustomerMonthlyMenuSchedule ||
                        currentNav == NavItem.CustomerReportMealIssue ||
                        currentNav == NavItem.CustomerSubscriberNotifications

                val isRiderPortalScreen = currentNav == NavItem.RiderDashboard ||
                        currentNav == NavItem.RiderStops ||
                        currentNav == NavItem.RiderBoxes ||
                        currentNav == NavItem.RiderEarnings ||
                        currentNav == NavItem.RiderDropVerification ||
                        currentNav == NavItem.RiderReportIncident ||
                        currentNav == NavItem.RiderProfileView ||
                        currentNav == NavItem.RiderNotifications

                // Persistent Top Bar matching all screen designs
                // Excludes auth, onboarding, customer portal, rider portal, and ALL legal/help screens
                val showStandardTopHeader = !isCustomerPortalScreen &&
                        !isRiderPortalScreen &&
                        currentNav != NavItem.Splash &&
                        currentNav != NavItem.Login &&
                        currentNav != NavItem.CreateAccount &&
                        currentNav != NavItem.ForgotPassword &&
                        currentNav != NavItem.EmailOtpVerification &&
                        currentNav != NavItem.OnboardingKitchenPlans &&
                        currentNav != NavItem.OnboardingDeliveryLedger &&
                        currentNav != NavItem.OnboardingKitchenSetup &&
                        currentNav != NavItem.PrivacyPolicy &&
                        currentNav != NavItem.TermsOfService &&
                        currentNav != NavItem.HelpCenter &&
                        currentNav != NavItem.FaqsArticles &&
                        currentNav != NavItem.ContactSupport &&
                        currentNav != NavItem.AppInfo &&
                        currentNav != NavItem.LegalCenterHub &&
                        currentNav != NavItem.CancellationPolicy &&
                        currentNav != NavItem.RefundPolicy &&
                        currentNav != NavItem.SecurityPolicy &&
                        currentNav != NavItem.DataProcessingAddendum

                if (showStandardTopHeader) {
                    val authState by TiffinRepository.authState.collectAsState()
                    val activeUser = authState.currentUser
                    val allBranches by TiffinRepository.branches.collectAsState()
                    TiffinTopHeader(
                        branchName = branch.name,
                        screenTitle = when (currentNav) {
                            NavItem.Dashboard -> "Dashboard"
                            NavItem.Kitchen -> "Kitchen Console"
                            NavItem.Plans -> "Subscriptions"
                            NavItem.Customers -> "Customers"
                            NavItem.More -> "Operations & Settings"
                            NavItem.Expenses -> "Expenses"
                            NavItem.RecurringExpenses -> "Recurring Expenses"
                            NavItem.CategoryBranchHolds -> "Create Hold"
                            NavItem.StaffDirectory -> "Staff Directory"
                            NavItem.NotificationSettings -> "Notification Settings"
                            NavItem.AddCustomer -> "Add Customer"
                            NavItem.CreateSubscription -> if (activeSubscriptionId != null) "Edit Subscription" else "New Subscription"
                            NavItem.CustomerDetailsLedger -> "Customer Ledger"
                            NavItem.SubscriptionDetails -> "Subscription Details"
                            NavItem.MealCategories -> "Meal Categories"
                            NavItem.MealPlans -> "Meal Plans"
                            NavItem.UpdateMealPlan -> "Update Meal Plan"
                            NavItem.UpdateBranch -> "Update Branch"
                            NavItem.UpdateDeliveryRoute -> "Update Route"
                            NavItem.Profile -> "Profile & Account"
                            NavItem.EditProfile -> "Edit Profile"
                            NavItem.Settings -> "Settings"
                            NavItem.Notifications -> "Activity & Notifications"
                            NavItem.Splash -> "Splash"
                            NavItem.OnboardingKitchenPlans -> "Onboarding Plans"
                            NavItem.OnboardingDeliveryLedger -> "Onboarding Ledger"
                            NavItem.OnboardingKitchenSetup -> "Kitchen Setup"
                            NavItem.Login -> "Sign In"
                            NavItem.CreateAccount -> "Create Account"
                            NavItem.ForgotPassword -> "Forgot Password"
                            NavItem.EmailOtpVerification -> "Verify OTP"
                            NavItem.PrivacyPolicy -> "Privacy Policy"
                            NavItem.TermsOfService -> "Terms of Service"
                            NavItem.HelpCenter -> "Help Center"
                            NavItem.FaqsArticles -> "FAQs & Guides"
                            NavItem.ContactSupport -> "Contact Support"
                            NavItem.AppInfo -> "App & Kitchen Info"
                            NavItem.LegalCenterHub -> "Legal Center"
                            NavItem.CancellationPolicy -> "Cancellation Policy"
                            NavItem.RefundPolicy -> "Refund Policy"
                            NavItem.SecurityPolicy -> "Security Policy"
                            NavItem.DataProcessingAddendum -> "Data Processing Addendum"
                            NavItem.CustomerDashboard -> "Today's Mess"
                            NavItem.CustomerOrders -> "My Orders"
                            NavItem.CustomerMealCalendar -> "Meal Schedule"
                            NavItem.CustomerWalletInvoices -> "Wallet & Ledger"
                            NavItem.CustomerDeliveryProfile -> "Delivery Profile"
                            NavItem.CustomerOrderGuestMeal -> "Order Guest Meal"
                            NavItem.CustomerProfileEditor -> "Edit Delivery Drop"
                            NavItem.CustomerLiveDeliveryTracker -> "Live Delivery"
                            NavItem.CustomerChangeUpgradePlan -> "Subscription Plan"
                            NavItem.CustomerMealRatingFeedback -> "Meal Rating"
                            NavItem.CustomerMonthlyMenuSchedule -> "Monthly Menu"
                            NavItem.CustomerReportMealIssue -> "Report Issue"
                            NavItem.CustomerSubscriberNotifications -> "Notifications"
                            NavItem.RiderDashboard -> "Delivery Run"
                            NavItem.RiderStops -> "Route Stops"
                            NavItem.RiderBoxes -> "Box Custody"
                            NavItem.RiderEarnings -> "Earnings Ledger"
                            NavItem.RiderDropVerification -> "Drop Proof"
                            NavItem.RiderReportIncident -> "Report Incident"
                            NavItem.RiderProfileView -> "Rider Profile"
                            NavItem.RiderNotifications -> "Rider Notifications"
                        },
                        userName = activeUser?.fullName ?: "Admin",
                        userRole = activeUser?.role?.replaceFirstChar { it.uppercase() } ?: "Owner",
                        branches = allBranches,
                        onSelectBranch = { selected ->
                            TiffinRepository.selectBranch(selected.id)
                        },
                        onEditBranchClick = { navigateTo(NavItem.UpdateBranch) },
                        onAddBranchClick = { navigateTo(NavItem.OnboardingKitchenPlans) },
                        onHoldsSettingsClick = { navigateTo(NavItem.CategoryBranchHolds) },
                        onNotificationClick = { navigateTo(NavItem.Notifications) },
                        onProfileClick = { navigateTo(NavItem.Profile) },
                        onSettingClick = { navigateTo(NavItem.Settings) }
                    )
                }

                // Main Screen Area (100% pixel-accurate screens from stitch designs)
                Box(modifier = Modifier.weight(1f)) {
                    when (currentNav) {
                        NavItem.Login -> LoginScreen(
                            onLoginSuccess = { email ->
                                activeSubscriptionId = null
                                navigateTo(NavItem.Dashboard)
                            },
                            onGoogleLoginSuccess = { email ->
                                activeSubscriptionId = null
                                navigateTo(NavItem.Dashboard)
                            },
                            onNavigateToRiderDashboard = { navigateTo(NavItem.RiderDashboard) },
                            onNavigateToDashboard = { navigateTo(NavItem.Dashboard) }
                        )
                        NavItem.Dashboard -> DashboardScreen(onNavigateTo = { navigateTo(it) })
                        NavItem.Kitchen -> KitchenConsoleScreen()
                        NavItem.Plans -> SubscriptionsScreen(
                            onCreateClick = {
                                activeSubscriptionId = null
                                navigateTo(NavItem.CreateSubscription)
                            },
                            onSubscriptionClick = { subId ->
                                activeSubscriptionId = subId
                                navigateTo(NavItem.SubscriptionDetails)
                            },
                            onEditSubscription = { subId ->
                                activeSubscriptionId = subId
                                navigateTo(NavItem.CreateSubscription)
                            }
                        )
                        NavItem.Customers -> CustomersScreen(
                            onAddCustomerClick = { navigateTo(NavItem.AddCustomer) },
                            onCustomerClick = { custId ->
                                activeCustomerId = custId
                                navigateTo(NavItem.CustomerDetailsLedger)
                            },
                            onEditCustomer = { custId ->
                                activeCustomerId = custId
                                navigateTo(NavItem.AddCustomer)
                            }
                        )
                        NavItem.More -> MoreHubScreen(onNavigateTo = { navigateTo(it) })
                        NavItem.Expenses -> ExpensesScreen(
                            onNavigateBack = { navigateBack() },
                            onNavigateToRecurring = { navigateTo(NavItem.RecurringExpenses) }
                        )
                        NavItem.RecurringExpenses -> RecurringExpensesScreen(
                            onNavigateBack = { navigateBack() },
                            onNavigateToAllExpenses = { navigateTo(NavItem.Expenses) }
                        )
                        NavItem.CategoryBranchHolds -> CategoryBranchHoldsScreen(
                            onNavigateBack = { navigateBack() }
                        )
                        NavItem.StaffDirectory -> StaffDirectoryScreen(
                            onNavigateBack = { navigateBack() }
                        )
                        NavItem.NotificationSettings -> NotificationSettingsScreen(
                            onNavigateBack = { navigateBack() }
                        )
                        NavItem.AddCustomer -> AddCustomerScreen(
                            onNavigateBack = { navigateBack() }
                        )
                        NavItem.CreateSubscription -> CreateSubscriptionScreen(
                            subscriptionId = activeSubscriptionId,
                            onNavigateBack = {
                                activeSubscriptionId = null
                                navigateBack()
                            }
                        )
                        NavItem.CustomerDetailsLedger -> CustomerDetailsLedgerScreen(
                            customerId = activeCustomerId ?: "",
                            onBack = { navigateBack() },
                            onEditCustomer = { custId ->
                                activeCustomerId = custId
                                navigateTo(NavItem.AddCustomer)
                            },
                            onViewSubscription = { subId ->
                                activeSubscriptionId = subId
                                navigateTo(NavItem.SubscriptionDetails)
                            }
                        )
                        NavItem.SubscriptionDetails -> SubscriptionDetailsScreen(
                            subscriptionId = activeSubscriptionId ?: "",
                            onBack = { navigateBack() },
                            onViewCustomerProfile = { custId ->
                                activeCustomerId = custId
                                navigateTo(NavItem.CustomerDetailsLedger)
                            },
                            onEditSubscription = { subId ->
                                activeSubscriptionId = subId
                                navigateTo(NavItem.CreateSubscription)
                            }
                        )
                        NavItem.MealCategories -> MealCategoriesScreen(
                            onBack = { navigateBack() },
                            onAddCategory = { /* in-place dialog handled in screen */ },
                            onEditCategory = { /* in-place dialog handled in screen */ }
                        )
                        NavItem.MealPlans -> MealPlansScreen(
                            onBack = { navigateBack() },
                            onCreatePlan = {
                                activeMealPlanId = "new"
                                navigateTo(NavItem.UpdateMealPlan)
                            },
                            onEditPlan = { plan ->
                                activeMealPlanId = plan.id
                                navigateTo(NavItem.UpdateMealPlan)
                            }
                        )
                        NavItem.UpdateMealPlan -> UpdateMealPlanScreen(
                            planId = activeMealPlanId ?: "new",
                            onNavigateBack = { navigateBack() }
                        )
                        NavItem.UpdateBranch -> UpdateBranchScreen(
                            onNavigateBack = { navigateBack() }
                        )
                        NavItem.UpdateDeliveryRoute -> UpdateDeliveryRouteScreen(
                            onNavigateBack = { navigateBack() }
                        )
                        NavItem.Profile -> ProfileScreen(
                            onNavigateBack = { navigateBack() },
                            onEditProfile = { navigateTo(NavItem.EditProfile) },
                            onBranchOperations = { navigateTo(NavItem.UpdateBranch) },
                            onNotificationSettings = { navigateTo(NavItem.NotificationSettings) },
                            onNavigateToCustomer = { navigateTo(NavItem.CustomerDashboard) },
                            onNavigateToRider = { navigateTo(NavItem.RiderDashboard) },
                            onNavigateToAppInfo = { navigateTo(NavItem.AppInfo) },
                            onNavigateToSupport = { navigateTo(NavItem.ContactSupport) },
                            onNavigateToLegal = { navigateTo(NavItem.LegalCenterHub) },
                            onSignOut = {
                                com.example.myapplicationkmp.data.repository.TiffinRepository.signOut()
                                resetTo(NavItem.Login)
                            }
                        )
                        NavItem.EditProfile -> EditProfileScreen(
                            onNavigateBack = { navigateBack() }
                        )
                        NavItem.Settings -> SettingsScreen(
                            onNavigateBack = { navigateBack() },
                            onNavigateToTerms = { navigateTo(NavItem.TermsOfService) },
                            onNavigateToPrivacy = { navigateTo(NavItem.PrivacyPolicy) }
                        )
                        NavItem.Notifications -> NotificationsScreen(
                            onNavigateBack = { navigateBack() },
                            onManageSettings = { navigateTo(NavItem.NotificationSettings) },
                            onViewRoute = { navigateTo(NavItem.UpdateDeliveryRoute) }
                        )
                        NavItem.Splash -> SplashScreen(
                            onFinishLoading = { resetTo(NavItem.Dashboard) }
                        )
                        NavItem.OnboardingKitchenPlans -> OnboardingKitchenPlansScreen(
                            onNavigateBack = { navigateBack() },
                            onContinue = { navigateTo(NavItem.OnboardingDeliveryLedger) },
                            onSkip = { resetTo(NavItem.Dashboard) }
                        )
                        NavItem.OnboardingDeliveryLedger -> OnboardingDeliveryLedgerScreen(
                            onNavigateBack = { navigateBack() },
                            onContinue = { navigateTo(NavItem.OnboardingKitchenSetup) },
                            onSkip = { resetTo(NavItem.Dashboard) }
                        )
                        NavItem.OnboardingKitchenSetup -> OnboardingKitchenSetupScreen(
                            onNavigateBack = { navigateBack() },
                            onLaunchDashboard = { resetTo(NavItem.Dashboard) },
                            onSkip = { resetTo(NavItem.Dashboard) }
                        )
                        NavItem.Login -> LoginScreen(
                            onNavigateToRegister = { navigateTo(NavItem.CreateAccount) },
                            onNavigateToForgotPassword = { navigateTo(NavItem.ForgotPassword) },
                            onLoginSuccess = { email ->
                                val user = TiffinRepository.authState.value.currentUser
                                val target = when {
                                    user?.role.equals("customer", ignoreCase = true) || user?.role.equals("subscriber", ignoreCase = true) -> NavItem.CustomerDashboard
                                    user?.role.equals("rider", ignoreCase = true) || user?.role.equals("driver", ignoreCase = true) -> NavItem.RiderDashboard
                                    else -> NavItem.Dashboard
                                }
                                resetTo(target)
                            },
                            onGoogleLoginSuccess = { email ->
                                val user = TiffinRepository.authState.value.currentUser
                                val target = when {
                                    user?.role.equals("customer", ignoreCase = true) || user?.role.equals("subscriber", ignoreCase = true) -> NavItem.CustomerDashboard
                                    user?.role.equals("rider", ignoreCase = true) || user?.role.equals("driver", ignoreCase = true) -> NavItem.RiderDashboard
                                    else -> NavItem.Dashboard
                                }
                                resetTo(target)
                            }
                        )
                        NavItem.CreateAccount -> CreateAccountScreen(
                            onNavigateToLogin = { navigateTo(NavItem.Login) },
                            onNavigateToTerms = { navigateTo(NavItem.TermsOfService) },
                            onNavigateToPrivacy = { navigateTo(NavItem.PrivacyPolicy) },
                            onRegisterSuccess = { email ->
                                navigateTo(NavItem.EmailOtpVerification)
                            },
                            onGoogleRegisterSuccess = { email ->
                                resetTo(NavItem.Dashboard)
                            }
                        )
                        NavItem.ForgotPassword -> ForgotPasswordScreen(
                            onNavigateBack = { navigateBack() },
                            onSendOtpSuccess = { identity ->
                                navigateTo(NavItem.EmailOtpVerification)
                            }
                        )
                        NavItem.EmailOtpVerification -> {
                            val authState by com.example.myapplicationkmp.data.repository.TiffinRepository.authState.collectAsState()
                            EmailOtpVerificationScreen(
                                email = authState.pendingOtpEmail ?: "",
                                flowType = authState.otpFlowType,
                                onNavigateBack = { navigateBack() },
                                onResetSuccess = { resetTo(NavItem.Login) },
                                onVerifySuccess = {
                                    if (authState.otpFlowType == "registration") {
                                        val user = authState.currentUser
                                        val target = when {
                                            user?.role.equals("customer", ignoreCase = true) || user?.role.equals("subscriber", ignoreCase = true) -> NavItem.CustomerDashboard
                                            user?.role.equals("rider", ignoreCase = true) || user?.role.equals("driver", ignoreCase = true) -> NavItem.RiderDashboard
                                            else -> NavItem.Dashboard
                                        }
                                        resetTo(target)
                                    } else {
                                        resetTo(NavItem.Login)
                                    }
                                }
                            )
                        }
                        NavItem.PrivacyPolicy -> PrivacyPolicyScreen(
                            onNavigateBack = { navigateBack() }
                        )
                        NavItem.TermsOfService -> TermsOfServiceScreen(
                            onNavigateBack = { navigateBack() }
                        )
                        NavItem.HelpCenter -> HelpCenterScreen(
                            onNavigateBack = { navigateBack() },
                            onNavigateToFaqs = { navigateTo(NavItem.FaqsArticles) },
                            onNavigateToContact = { navigateTo(NavItem.ContactSupport) },
                            onNavigateToAppInfo = { navigateTo(NavItem.AppInfo) },
                            onTopicClick = { categorySlug ->
                                navigateTo(NavItem.FaqsArticles)
                            }
                        )
                        NavItem.FaqsArticles -> FaqsArticlesScreen(
                            onNavigateBack = { navigateBack() },
                            onNavigateToContact = { navigateTo(NavItem.ContactSupport) }
                        )
                        NavItem.ContactSupport -> ContactSupportScreen(
                            onNavigateBack = { navigateBack() },
                            onNavigateToFaqs = { navigateTo(NavItem.FaqsArticles) },
                            onNavigateToPrivacy = { navigateTo(NavItem.PrivacyPolicy) },
                            onNavigateToTerms = { navigateTo(NavItem.TermsOfService) }
                        )
                        NavItem.AppInfo -> AppInfoScreen(
                            onNavigateBack = { navigateBack() },
                            onNavigateToPrivacy = { navigateTo(NavItem.PrivacyPolicy) },
                            onNavigateToTerms = { navigateTo(NavItem.TermsOfService) },
                            onNavigateToContact = { navigateTo(NavItem.ContactSupport) }
                        )
                        NavItem.LegalCenterHub -> LegalCenterHubScreen(
                            onNavigateBack = { navigateBack() },
                            onDocumentClick = { slug ->
                                val target = when (slug) {
                                    "terms-of-service" -> NavItem.TermsOfService
                                    "privacy-policy" -> NavItem.PrivacyPolicy
                                    "cancellation-policy" -> NavItem.CancellationPolicy
                                    "refund-policy" -> NavItem.RefundPolicy
                                    "security-policy" -> NavItem.SecurityPolicy
                                    "data-processing-addendum" -> NavItem.DataProcessingAddendum
                                    else -> NavItem.LegalCenterHub
                                }
                                navigateTo(target)
                            }
                        )
                        NavItem.CancellationPolicy -> CancellationPolicyScreen(
                            onNavigateBack = { navigateBack() },
                            onContactSupport = { navigateTo(NavItem.ContactSupport) },
                            onViewRefundPolicy = { navigateTo(NavItem.RefundPolicy) }
                        )
                        NavItem.RefundPolicy -> RefundPolicyScreen(
                            onNavigateBack = { navigateBack() },
                            onContactBilling = { navigateTo(NavItem.ContactSupport) },
                            onViewCancellation = { navigateTo(NavItem.CancellationPolicy) }
                        )
                        NavItem.SecurityPolicy -> SecurityPolicyScreen(
                            onNavigateBack = { navigateBack() },
                            onNavigatePolicy = { slug ->
                                val target = when (slug) {
                                    "privacy-policy" -> NavItem.PrivacyPolicy
                                    "terms-of-service" -> NavItem.TermsOfService
                                    else -> NavItem.SecurityPolicy
                                }
                                navigateTo(target)
                            },
                            onContactSecurity = { navigateTo(NavItem.ContactSupport) }
                        )
                 
                        NavItem.DataProcessingAddendum -> DataProcessingAddendumScreen(
                            onNavigateBack = { navigateBack() },
                            onAcknowledgeDpa = { },
                            onDownloadPdf = { }
                        )

                        // Folder 11 & 12: Customer / Subscriber Portal Screens
                        NavItem.CustomerDashboard -> CustomerDashboardScreen(
                            onNavigateToGuestMeal = { navigateTo(NavItem.CustomerOrderGuestMeal) },
                            onNavigateToSchedule = { navigateTo(NavItem.CustomerMonthlyMenuSchedule) },
                            onNavigateToWallet = { navigateTo(NavItem.CustomerWalletInvoices) },
                            onNavigateToProfile = { navigateTo(NavItem.Profile) },
                            onEditDropNote = { navigateTo(NavItem.CustomerProfileEditor) },
                            onNavigateToLiveTracker = { navigateTo(NavItem.CustomerLiveDeliveryTracker) },
                            onNavigateToNotifications = { navigateTo(NavItem.Notifications) },
                            onNavigateToMealRating = { navigateTo(NavItem.CustomerMealRatingFeedback) },
                            onNavigateToReportIssue = { navigateTo(NavItem.CustomerReportMealIssue) }
                        )
                        NavItem.CustomerOrders -> com.example.myapplicationkmp.ui.screens.customer.CustomerOrdersScreen(
                            onNavigateToLiveTracker = { navigateTo(NavItem.CustomerLiveDeliveryTracker) },
                            onNavigateToGuestMeal = { navigateTo(NavItem.CustomerOrderGuestMeal) },
                            onNavigateToProfile = { navigateTo(NavItem.Profile) },
                            onNavigateToNotifications = { navigateTo(NavItem.Notifications) }
                        )
                        NavItem.CustomerDeliveryProfile -> CustomerDeliveryProfileScreen(
                            onEditProfile = { navigateTo(NavItem.CustomerProfileEditor) },
                            onNavigateToRules = { navigateTo(NavItem.LegalCenterHub) },
                            onNavigateToRefund = { navigateTo(NavItem.RefundPolicy) },
                            onNavigateToPrivacy = { navigateTo(NavItem.PrivacyPolicy) },
                            onNavigateToAdmin = { resetTo(NavItem.Dashboard) },
                            onNavigateToRider = { resetTo(NavItem.RiderDashboard) },
                            onSignOut = {
                                com.example.myapplicationkmp.data.repository.TiffinRepository.signOut()
                                resetTo(NavItem.Login)
                            },
                            onNavigateToProfile = { navigateTo(NavItem.Profile) },
                            onNavigateToSettings = { navigateTo(NavItem.Settings) },
                        )
                        NavItem.CustomerMealCalendar -> CustomerMealCalendarHoldsScreen(
                            onNavigateToWallet = { navigateTo(NavItem.CustomerWalletInvoices) },
                            onNavigateToProfile = { navigateTo(NavItem.Profile) }
                        )
                        NavItem.CustomerWalletInvoices -> CustomerWalletInvoicesScreen(
                            onNavigateToProfile = { navigateTo(NavItem.Profile) }
                        )
                        NavItem.CustomerOrderGuestMeal -> CustomerOrderGuestMealScreen(
                            onNavigateBack = { navigateBack() },
                            onOrderSuccess = { navigateTo(NavItem.CustomerDashboard) }
                        )
                        NavItem.CustomerProfileEditor -> EditProfileScreen(
                            onNavigateBack = { navigateBack() }
                        )

                        // Folder 12: Extended Customer Screens
                        NavItem.CustomerLiveDeliveryTracker -> CustomerLiveDeliveryTrackerScreen(
                            repository = com.example.myapplicationkmp.data.repository.TiffinRepository,
                            onNavigateBack = { navigateBack() },
                            onNavigateToProfile = { navigateTo(NavItem.Profile) },
                            onNavigateToFeedback = { navigateTo(NavItem.CustomerMealRatingFeedback) },
                            onNavigateToReportIssue = { navigateTo(NavItem.CustomerReportMealIssue) }
                        )
                        NavItem.CustomerChangeUpgradePlan -> CustomerChangeUpgradePlanScreen(
                            repository = com.example.myapplicationkmp.data.repository.TiffinRepository,
                            onNavigateBack = { navigateBack() },
                            onNavigateToProfile = { navigateTo(NavItem.Profile) },
                            onNavigateToWallet = { navigateTo(NavItem.CustomerWalletInvoices) }
                        )
                        NavItem.CustomerMealRatingFeedback -> CustomerMealRatingFeedbackScreen(
                            repository = com.example.myapplicationkmp.data.repository.TiffinRepository,
                            onNavigateBack = { navigateBack() },
                            onNavigateToProfile = { navigateTo(NavItem.Profile) },
                            onNavigateToReportIssue = { navigateTo(NavItem.CustomerReportMealIssue) }
                        )
                        NavItem.CustomerMonthlyMenuSchedule -> CustomerMonthlyMenuScheduleScreen(
                            repository = com.example.myapplicationkmp.data.repository.TiffinRepository,
                            onNavigateBack = { navigateBack() },
                            onNavigateToProfile = { navigateTo(NavItem.Profile) },
                            onNavigateToChangePlan = { navigateTo(NavItem.CustomerChangeUpgradePlan) },
                            onNavigateToGuestMeal = { navigateTo(NavItem.CustomerOrderGuestMeal) }
                        )
                        NavItem.CustomerReportMealIssue -> CustomerReportMealIssueScreen(
                            repository = com.example.myapplicationkmp.data.repository.TiffinRepository,
                            onNavigateBack = { navigateBack() },
                            onNavigateToProfile = { navigateTo(NavItem.Profile) }
                        )
                        NavItem.CustomerSubscriberNotifications -> CustomerSubscriberNotificationsScreen(
                            onNavigateBack = { navigateBack() },
                            onNavigateToProfile = { navigateTo(NavItem.Profile) },
                            onNavigateToLiveTracker = { navigateTo(NavItem.CustomerLiveDeliveryTracker) },
                            onNavigateToFeedback = { navigateTo(NavItem.CustomerMealRatingFeedback) },
                            onNavigateToWallet = { navigateTo(NavItem.CustomerWalletInvoices) },
                            onNavigateToSchedule = { navigateTo(NavItem.CustomerMonthlyMenuSchedule) },
                            onNavigateToGuestMeal = { navigateTo(NavItem.CustomerOrderGuestMeal) }
                        )

                        // Folder 13: Rider / Driver User Screens
                        NavItem.RiderDashboard -> RiderRunShiftDashboardScreen(
                            repository = com.example.myapplicationkmp.data.repository.TiffinRepository,
                            onNavigateToStops = { navigateTo(NavItem.RiderStops) },
                            onNavigateToDropPhoto = { stopId -> navigateTo(NavItem.RiderDropVerification) },
                            onNavigateToIncident = { stopId -> navigateTo(NavItem.RiderReportIncident) },
                            onNavigateToNotifications = { navigateTo(NavItem.Notifications) },
                            onNavigateToProfile = { navigateTo(NavItem.Profile) }
                        )
                        NavItem.RiderStops -> RiderRouteStopListScreen(
                            repository = com.example.myapplicationkmp.data.repository.TiffinRepository,
                            onNavigateToDropPhoto = { stopId -> navigateTo(NavItem.RiderDropVerification) },
                            onNavigateToIncident = { stopId -> navigateTo(NavItem.RiderReportIncident) },
                            onNavigateToNotifications = { navigateTo(NavItem.Notifications) },
                            onNavigateToProfile = { navigateTo(NavItem.Profile) }
                        )
                        NavItem.RiderBoxes -> RiderBoxCustodyScreen(
                            repository = com.example.myapplicationkmp.data.repository.TiffinRepository,
                            onNavigateToNotifications = { navigateTo(NavItem.Notifications) },
                            onNavigateToProfile = { navigateTo(NavItem.Profile) },
                            onNavigateToDropPhoto = { stopId -> navigateTo(NavItem.RiderDropVerification) }
                        )
                        NavItem.RiderEarnings -> RiderEarningsBoxLedgerScreen(
                            repository = com.example.myapplicationkmp.data.repository.TiffinRepository,
                            onNavigateToNotifications = { navigateTo(NavItem.Notifications) },
                            onNavigateToProfile = { navigateTo(NavItem.Profile) }
                        )
                        NavItem.RiderDropVerification -> RiderDoorstepDropProofScreen(
                            stopId = "stop-19",
                            repository = com.example.myapplicationkmp.data.repository.TiffinRepository,
                            onBack = { navigateBack() },
                            onConfirmed = { resetTo(NavItem.RiderDashboard) }
                        )
                        NavItem.RiderReportIncident -> RiderReportIncidentScreen(
                            stopId = "stop-19",
                            repository = com.example.myapplicationkmp.data.repository.TiffinRepository,
                            onBack = { navigateBack() },
                            onSubmitSuccess = { navigateBack() }
                        )
                        NavItem.RiderProfileView -> RiderProfileVehicleInfoScreen(
                            repository = com.example.myapplicationkmp.data.repository.TiffinRepository,
                            onBack = { navigateBack() },
                            onNavigateToAdmin = { resetTo(NavItem.Dashboard) },
                            onNavigateToCustomer = { resetTo(NavItem.CustomerDashboard) },
                            onSignOut = {
                                com.example.myapplicationkmp.data.repository.TiffinRepository.signOut()
                                resetTo(NavItem.Login)
                            }
                        )
                        NavItem.RiderNotifications -> RiderNotificationsAlertsScreen(
                            repository = com.example.myapplicationkmp.data.repository.TiffinRepository,
                            onBack = { navigateBack() },
                            onNavigateToStops = { navigateTo(NavItem.RiderStops) },
                            onNavigateToPayouts = { navigateTo(NavItem.RiderEarnings) }
                        )
                    }
                }

                // Customer Portal Navigation Mode Check
                val isCustomerPortalFlow = currentNav == NavItem.CustomerDashboard ||
                                           currentNav == NavItem.CustomerOrders ||
                                           currentNav == NavItem.CustomerDeliveryProfile ||
                                           currentNav == NavItem.CustomerMealCalendar ||
                                           currentNav == NavItem.CustomerWalletInvoices ||
                                           currentNav == NavItem.CustomerOrderGuestMeal ||
                                           currentNav == NavItem.CustomerProfileEditor ||
                                           currentNav == NavItem.CustomerLiveDeliveryTracker ||
                                           currentNav == NavItem.CustomerChangeUpgradePlan ||
                                           currentNav == NavItem.CustomerMealRatingFeedback ||
                                           currentNav == NavItem.CustomerMonthlyMenuSchedule ||
                                           currentNav == NavItem.CustomerReportMealIssue ||
                                           currentNav == NavItem.CustomerSubscriberNotifications

                // Rider Portal Navigation Mode Check
                val isRiderPortalFlow = currentNav == NavItem.RiderDashboard ||
                                        currentNav == NavItem.RiderStops ||
                                        currentNav == NavItem.RiderBoxes ||
                                        currentNav == NavItem.RiderEarnings ||
                                        currentNav == NavItem.RiderDropVerification ||
                                        currentNav == NavItem.RiderReportIncident ||
                                        currentNav == NavItem.RiderProfileView ||
                                        currentNav == NavItem.RiderNotifications

                // Persistent Bottom Navigation Dock (Hidden during Fullscreen Splash, Setup, Auth & Legal)
                val isFullscreenFlow = currentNav == NavItem.Splash || 
                                       currentNav == NavItem.OnboardingKitchenPlans || 
                                       currentNav == NavItem.OnboardingDeliveryLedger || 
                                       currentNav == NavItem.OnboardingKitchenSetup ||
                                       currentNav == NavItem.Login ||
                                       currentNav == NavItem.CreateAccount ||
                                       currentNav == NavItem.ForgotPassword ||
                                       currentNav == NavItem.EmailOtpVerification ||
                                       currentNav == NavItem.PrivacyPolicy ||
                                       currentNav == NavItem.TermsOfService ||
                                       currentNav == NavItem.HelpCenter ||
                                       currentNav == NavItem.FaqsArticles ||
                                       currentNav == NavItem.ContactSupport ||
                                       currentNav == NavItem.AppInfo ||
                                       currentNav == NavItem.LegalCenterHub ||
                                       currentNav == NavItem.CancellationPolicy ||
                                       currentNav == NavItem.RefundPolicy ||
                                       currentNav == NavItem.SecurityPolicy ||
                                       currentNav == NavItem.DataProcessingAddendum

                if (!isFullscreenFlow) {
                    val authState by TiffinRepository.authState.collectAsState()
                    val userRole = (authState.currentUser?.role ?: "admin").lowercase()
                    val isCustomerRole = userRole == "customer" || userRole == "subscriber"
                    val isRiderRole = userRole == "rider" || userRole == "driver"

                    if (isRiderPortalFlow || (isRiderRole && (currentNav == NavItem.Profile || currentNav == NavItem.Settings || currentNav == NavItem.Notifications))) {
                        com.example.myapplicationkmp.ui.components.TiffinRiderBottomNavigation(
                            selectedItem = currentNav,
                            onItemSelected = { navigateTo(it) }
                        )
                    } else if (isCustomerPortalFlow || (isCustomerRole && (currentNav == NavItem.Profile || currentNav == NavItem.Settings || currentNav == NavItem.Notifications))) {
                        com.example.myapplicationkmp.ui.components.TiffinCustomerBottomNavigation(
                            selectedItem = currentNav,
                            onItemSelected = { navigateTo(it) }
                        )
                    } else {
                        val bottomNavSelection = when (currentNav) {
                            NavItem.AddCustomer, NavItem.CustomerDetailsLedger -> NavItem.Customers
                            NavItem.CreateSubscription, NavItem.SubscriptionDetails -> NavItem.Plans
                            NavItem.MealCategories, NavItem.MealPlans, NavItem.UpdateMealPlan -> NavItem.MealCategories
                            NavItem.UpdateBranch, NavItem.UpdateDeliveryRoute,
                            NavItem.Profile, NavItem.EditProfile, NavItem.Settings, NavItem.Notifications -> NavItem.More
                            else -> currentNav
                        }
                        TiffinBottomNavigation(
                            selectedItem = bottomNavSelection,
                            onItemSelected = { navigateTo(it) }
                        )
                    }
                }
            }
        }
    }
}


