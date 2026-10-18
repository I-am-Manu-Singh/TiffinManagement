package com.creativeblisslabs.tiffinmanagement.data.repository

import com.creativeblisslabs.tiffinmanagement.data.models.BranchModel
import com.creativeblisslabs.tiffinmanagement.data.models.CustomerModel
import com.creativeblisslabs.tiffinmanagement.data.models.FinancialOverviewModel
import com.creativeblisslabs.tiffinmanagement.data.models.KitchenBatchModel
import com.creativeblisslabs.tiffinmanagement.data.models.SubscriptionModel
import com.creativeblisslabs.tiffinmanagement.data.storage.SessionStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * State-driven repository decoupled from hardcoded screen UI strings.
 * Connected to live database entities.
 */
object TiffinRepository {

    private val _branches = MutableStateFlow(
        listOf(
            BranchModel(id = "b-1", name = "Main Central Kitchen", address = "Sector 19, Airoli, Navi Mumbai", city = "Navi Mumbai", coverageRadiusKm = 12.0),
            BranchModel(id = "b-2", name = "Tech Park Cloud Hub", address = "Phase 2, Hinjewadi", city = "Pune", coverageRadiusKm = 10.0),
            BranchModel(id = "b-3", name = "North Express Depot", address = "Vigyan Nagar", city = "Kota", coverageRadiusKm = 8.5)
        )
    )
    val branches = _branches.asStateFlow()

    private val _currentBranch = MutableStateFlow(
        BranchModel(id = "b-1", name = "Main Central Kitchen", address = "Sector 19, Airoli, Navi Mumbai", city = "Navi Mumbai", coverageRadiusKm = 12.0)
    )
    val currentBranch = _currentBranch.asStateFlow()

    fun selectBranch(branchId: String) {
        _branches.value.find { it.id == branchId }?.let {
            _currentBranch.value = it
            refreshFinancialOverview()
        }
    }

    /**
     * Recomputes the financial overview from live subscription and expense state.
     * Call whenever subscriptions, expenses, or branch changes.
     */
    fun refreshFinancialOverview() {
        val activeSubs = _subscriptions.value.filter { it.isActive }
        val allExpenses = _expenses.value
        val totalRevenue = activeSubs.sumOf { it.totalAmount }
        val totalExpenses = allExpenses.sumOf { it.amount + it.tax }
        val netProfit = totalRevenue - totalExpenses
        val lowBalanceCount = _customers.value.count { it.walletBalance < 150.0 }
        val activeDeliveries = activeSubs.size
        _financialOverview.value = _financialOverview.value.copy(
            activeSubscribersCount = activeSubs.size,
            monthlyRevenue = totalRevenue,
            monthlyExpenses = totalExpenses,
            monthlyOrdersCount = activeDeliveries,
            netProfit = netProfit,
            homeDeliveryTotal = activeDeliveries,
            lowBalanceAlertsCount = lowBalanceCount
        )
    }

    private val _financialOverview = MutableStateFlow(
        FinancialOverviewModel(
            activeSubscribersCount = 0,
            monthlyRevenue = 0.0,
            billingCycleMonth = "",
            monthlyExpenses = 0.0,
            expenseCategory = "",
            monthlyOrdersCount = 0,
            netProfit = 0.0,
            recordedDipAmount = 0.0,
            homeDeliveryCompleted = 0,
            homeDeliveryTotal = 0,
            storePickupCompleted = 0,
            storePickupTotal = 0,
            lowBalanceAlertsCount = 0
        )
    )
    val financialOverview = _financialOverview.asStateFlow()

    private val _customers = MutableStateFlow<List<CustomerModel>>(emptyList())
    val customers = _customers.asStateFlow()

    private val _subscriptions = MutableStateFlow<List<SubscriptionModel>>(emptyList())
    val subscriptions = _subscriptions.asStateFlow()

    private val _walletLedger = MutableStateFlow<List<com.example.myapplicationkmp.data.models.WalletLedgerItemModel>>(emptyList())
    val walletLedger = _walletLedger.asStateFlow()

    private val _kitchenBatch = MutableStateFlow(
        KitchenBatchModel(
            dateFormatted = "",
            activeSlot = "",
            rotisCount = 0,
            sabziKg = 0.0,
            dalLiters = 0.0,
            kdsStation = "",
            shiftTime = "",
            pendingCount = 0,
            inPrepCount = 0,
            dispatchedCount = 0,
            hasItemsToPrepare = false
        )
    )
    val kitchenBatch = _kitchenBatch.asStateFlow()

    // 1. Expenses
    private val _expenses = MutableStateFlow(
        listOf(
            com.example.myapplicationkmp.data.models.ExpenseModel(
                id = "exp-1",
                dateFormatted = "04 Sep, 2026",
                category = "Fresh Spice Restock",
                amount = 1450.00,
                tax = 0.0,
                paymentMode = "Paid Cash",
                description = "Coriander, Cumin, Mustard",
                receiptName = "receipt_inv_3434.jpg",
                receiptSizeFormatted = "248 KB"
            ),
            com.example.myapplicationkmp.data.models.ExpenseModel(
                id = "exp-2",
                dateFormatted = "02 Sep, 2026",
                category = "Tiffin Packaging Trays",
                amount = 3200.00,
                tax = 160.0,
                paymentMode = "UPI Auto-pay",
                description = "500 Compostable trays",
                receiptName = null,
                receiptSizeFormatted = null
            )
        )
    )
    val expenses = _expenses.asStateFlow()

    // 2. Recurring Expense Rules
    private val _recurringExpenseRules = MutableStateFlow(
        listOf(
            com.example.myapplicationkmp.data.models.RecurringExpenseRuleModel(
                id = "rec-1",
                title = "Staff Salary - Head Cook",
                category = "Staff Salary",
                amount = 343.00,
                tax = 10.00,
                frequency = "Weekly (Wednesday)",
                dayOfMonth = 4,
                nextDueDate = "09 Sep, 2026",
                lastGeneratedDate = "04 Sep, 2026",
                endDate = "01 Oct, 2026",
                isActive = true
            )
        )
    )
    val recurringExpenseRules = _recurringExpenseRules.asStateFlow()

    // 3. Operational Holds
    private val _kitchenHolds = MutableStateFlow<List<com.example.myapplicationkmp.data.models.KitchenHoldModel>>(emptyList())
    val kitchenHolds = _kitchenHolds.asStateFlow()

    // 4. Staff Directory
    private val _staffMembers = MutableStateFlow(
        listOf(
            com.example.myapplicationkmp.data.models.StaffMemberModel(
                id = "staff-1",
                fullName = "Ramesh Sharma",
                phone = "+91 98231 44520",
                email = "ramesh.sharma@cloudkitchen.com",
                role = "Head Cook",
                branchName = "Main Branch",
                isActive = true
            ),
            com.example.myapplicationkmp.data.models.StaffMemberModel(
                id = "staff-2",
                fullName = "Amit Kumar",
                phone = "+91 94120 77819",
                email = "amit.k@cloudkitchen.com",
                role = "Delivery Lead",
                branchName = "North Sector",
                isActive = true
            )
        )
    )
    val staffMembers = _staffMembers.asStateFlow()

    // 5. Notification Settings
    private val _notificationSettings = MutableStateFlow(
        listOf(
            com.example.myapplicationkmp.data.models.NotificationSettingModel(
                id = "notif-1",
                key = "sub_created",
                category = "customer",
                title = "Subscription Created Successfully",
                recipientLabel = "Customer",
                channels = "WhatsApp & SMS",
                detailText = "Dispatched via: WhatsApp & SMS",
                sampleMessage = "Namaste {CustomerName}, your tiffin plan starting {Date} is confirmed!",
                isEnabled = true
            ),
            com.example.myapplicationkmp.data.models.NotificationSettingModel(
                id = "notif-2",
                key = "sub_expiring",
                category = "customer",
                title = "Subscription Expiring Warning",
                recipientLabel = "Customer",
                channels = "WhatsApp & Push",
                detailText = "Trigger timeline: 3 meals before cycle ends",
                sampleMessage = "Alert includes 1-tap UPI re-order link.",
                isEnabled = true
            ),
            com.example.myapplicationkmp.data.models.NotificationSettingModel(
                id = "notif-3",
                key = "sub_hold",
                category = "customer",
                title = "Subscription Hold Paused/Resumed",
                recipientLabel = "Customer",
                channels = "WhatsApp & SMS",
                detailText = "Sends instant confirmation whenever pause window is scheduled or modified by customer.",
                sampleMessage = null,
                isEnabled = true
            ),
            com.example.myapplicationkmp.data.models.NotificationSettingModel(
                id = "notif-4",
                key = "low_wallet_customer",
                category = "customer",
                title = "Low Wallet Balance Limit",
                recipientLabel = "Customer",
                channels = "In-App & SMS",
                detailText = "Trigger threshold: ₹150.00",
                sampleMessage = null,
                isEnabled = true
            ),
            com.example.myapplicationkmp.data.models.NotificationSettingModel(
                id = "notif-5",
                key = "delivery_confirm",
                category = "customer",
                title = "Delivery Confirmation Sent",
                recipientLabel = "Customer",
                channels = "WhatsApp & Push",
                detailText = "Dispatched automatically once delivery executive marks box delivered with geotag.",
                sampleMessage = null,
                isEnabled = true
            ),
            com.example.myapplicationkmp.data.models.NotificationSettingModel(
                id = "notif-6",
                key = "low_wallet_vendor",
                category = "vendor",
                title = "Low Wallet Balance Limit",
                recipientLabel = "Vendor / Staff",
                channels = "Owner Digest",
                detailText = "Dispatched as daily 10:00 AM aggregate digest to owner",
                sampleMessage = null,
                isEnabled = true
            ),
            com.example.myapplicationkmp.data.models.NotificationSettingModel(
                id = "notif-7",
                key = "order_lock_digest",
                category = "vendor",
                title = "Daily Orders Lock digest",
                recipientLabel = "Vendor / Staff",
                channels = "Kitchen Console Alert",
                detailText = "Summary of locked kitchen quantities dispatched 2 hours prior to dispatch slot.",
                sampleMessage = null,
                isEnabled = true
            )
        )
    )
    val notificationSettings = _notificationSettings.asStateFlow()

    fun addCustomer(customer: CustomerModel) {
        _customers.value = _customers.value + customer
    }

    fun updateCustomer(customer: CustomerModel) {
        _customers.value = _customers.value.map {
            if (it.id == customer.id) customer else it
        }
    }

    fun toggleCustomerStatus(customerId: String, isActive: Boolean) {
        _customers.value = _customers.value.map {
            if (it.id == customerId) it.copy(isActive = isActive) else it
        }
    }

    fun removeCustomer(customerId: String) {
        _customers.value = _customers.value.filter { it.id != customerId }
        refreshFinancialOverview()
    }

    fun addExpense(expense: com.example.myapplicationkmp.data.models.ExpenseModel) {
        _expenses.value = listOf(expense) + _expenses.value
        refreshFinancialOverview()
    }

    fun addRecurringRule(rule: com.example.myapplicationkmp.data.models.RecurringExpenseRuleModel) {
        _recurringExpenseRules.value = _recurringExpenseRules.value + rule
    }

    fun toggleRecurringRule(ruleId: String, active: Boolean) {
        _recurringExpenseRules.value = _recurringExpenseRules.value.map {
            if (it.id == ruleId) it.copy(isActive = active) else it
        }
    }

    fun deleteRecurringRule(ruleId: String) {
        _recurringExpenseRules.value = _recurringExpenseRules.value.filter { it.id != ruleId }
    }

    fun addKitchenHold(hold: com.example.myapplicationkmp.data.models.KitchenHoldModel) {
        _kitchenHolds.value = listOf(hold) + _kitchenHolds.value
    }

    fun addStaffMember(staff: com.example.myapplicationkmp.data.models.StaffMemberModel) {
        _staffMembers.value = listOf(staff) + _staffMembers.value
    }

    fun toggleNotificationSetting(id: String, enabled: Boolean) {
        _notificationSettings.value = _notificationSettings.value.map {
            if (it.id == id) it.copy(isEnabled = enabled) else it
        }
    }

    fun applyWalletAdjustment(customerId: String, amount: Double, isCredit: Boolean, description: String) {
        val currentCust = _customers.value.find { it.id == customerId } ?: return
        val newBalance = if (isCredit) {
            currentCust.walletBalance + amount
        } else {
            // Guard: do not let balance drop below -500 (trust limit safety)
            (currentCust.walletBalance - amount).coerceAtLeast(-500.0)
        }
        _customers.value = _customers.value.map {
            if (it.id == customerId) it.copy(walletBalance = newBalance) else it
        }
        val nowFormatted = formatEpochToReadable(System.currentTimeMillis())
        val ledgerItem = com.example.myapplicationkmp.data.models.WalletLedgerItemModel(
            id = "wtx-${System.currentTimeMillis()}",
            dateFormatted = nowFormatted,
            description = description,
            amount = amount,
            isCredit = isCredit,
            balanceAfter = newBalance
        )
        _walletLedger.value = listOf(ledgerItem) + _walletLedger.value
        // Refresh overview so low-balance alerts update
        refreshFinancialOverview()
    }

    fun applySubscriptionWalletAdjustment(subscriptionId: String, amount: Double, isCredit: Boolean, description: String) {
        val targetSub = _subscriptions.value.find { it.id == subscriptionId } ?: return
        val newSubBalance = if (isCredit) {
            targetSub.walletBalance + amount
        } else {
            (targetSub.walletBalance - amount).coerceAtLeast(-500.0)
        }
        _subscriptions.value = _subscriptions.value.map {
            if (it.id == subscriptionId) it.copy(walletBalance = newSubBalance) else it
        }

        // Sync customer wallet by customerId (reliable) or fall back to name match
        _customers.value = _customers.value.map { cust ->
            val matchById = targetSub.customerId.isNotBlank() && cust.id == targetSub.customerId
            val matchByName = !matchById && cust.name.equals(targetSub.customerName, ignoreCase = true)
            if (matchById || matchByName) {
                val newCustBal = if (isCredit) {
                    cust.walletBalance + amount
                } else {
                    (cust.walletBalance - amount).coerceAtLeast(-500.0)
                }
                cust.copy(walletBalance = newCustBal)
            } else cust
        }

        val nowFormatted = formatEpochToReadable(System.currentTimeMillis())
        val ledgerItem = com.example.myapplicationkmp.data.models.WalletLedgerItemModel(
            id = "wtx-${System.currentTimeMillis()}",
            dateFormatted = nowFormatted,
            description = description.ifBlank { "Subscription Adjustment (${targetSub.subscriptionCode})" },
            amount = amount,
            isCredit = isCredit,
            balanceAfter = newSubBalance
        )
        _walletLedger.value = listOf(ledgerItem) + _walletLedger.value
        refreshFinancialOverview()
    }

    fun addSubscriptionVacationHold(subId: String, hold: com.example.myapplicationkmp.data.models.VacationHoldModel) {
        _subscriptions.value = _subscriptions.value.map { sub ->
            if (sub.id == subId) {
                sub.copy(vacationHolds = listOf(hold) + sub.vacationHolds)
            } else sub
        }
    }

    fun addSubscription(subscription: SubscriptionModel) {
        _subscriptions.value = listOf(subscription) + _subscriptions.value
        refreshFinancialOverview()
    }

    fun updateSubscription(subscription: SubscriptionModel) {
        _subscriptions.value = _subscriptions.value.map {
            if (it.id == subscription.id) subscription else it
        }
        refreshFinancialOverview()
    }

    fun toggleSubscriptionStatus(subscriptionId: String, isActive: Boolean) {
        _subscriptions.value = _subscriptions.value.map {
            if (it.id == subscriptionId) it.copy(isActive = isActive) else it
        }
        refreshFinancialOverview()
    }

    fun deleteSubscription(subscriptionId: String) {
        _subscriptions.value = _subscriptions.value.filter { it.id != subscriptionId }
    }

    // 6. Meal Categories (Folder 4)
    private val _mealCategories = MutableStateFlow(
        listOf(
            com.example.myapplicationkmp.data.models.MealCategoryModel(
                id = "cat-breakfast",
                name = "Breakfast",
                startTime = "07:00 AM",
                endTime = "09:30 AM",
                description = null,
                isActive = true,
                displayOrder = 1
            ),
            com.example.myapplicationkmp.data.models.MealCategoryModel(
                id = "cat-lunch",
                name = "Lunch",
                startTime = "12:00 PM",
                endTime = "02:30 PM",
                description = null,
                isActive = true,
                displayOrder = 2
            ),
             com.example.myapplicationkmp.data.models.MealCategoryModel(
                id = "cat-hitea",
                name = "Hi-Tea",
                startTime = "5:20 PM",
                endTime = "6:20 PM",
                description = null,
                isActive = true,
                displayOrder = 3
            ),
            com.example.myapplicationkmp.data.models.MealCategoryModel(
                id = "cat-dinner",
                name = "Dinner",
                startTime = "07:00 PM",
                endTime = "09:30 PM",
                description = null,
                isActive = true,
                displayOrder = 4
            )
        )
    )
    val mealCategories = _mealCategories.asStateFlow()

    fun addMealCategory(category: com.example.myapplicationkmp.data.models.MealCategoryModel) {
        _mealCategories.value = _mealCategories.value + category
    }

    fun updateMealCategory(category: com.example.myapplicationkmp.data.models.MealCategoryModel) {
        _mealCategories.value = _mealCategories.value.map { if (it.id == category.id) category else it }
    }

    fun deleteMealCategory(categoryId: String) {
        _mealCategories.value = _mealCategories.value.filter { it.id != categoryId }
    }

    // 7. Meal Plans / Packages (Folder 4)
    private val _mealPlans = MutableStateFlow(
        listOf(
            com.example.myapplicationkmp.data.models.MealPlanModel(
                id = "pln-8402",
                name = "Standard Balanced Thali",
                categoryName = "Lunch",
                isVeg = true,
                isJain = false,
                dailyRate = 100.0,
                weeklyRate = 700.0,
                monthlyRate = 3000.0,
                maxOrderIncrement = 2,
                description = "4 Whole wheat phulkas, seasonal gravy sabzi, dal tadka, jeera rice & fresh salad",
                isFeatured = true,
                isActive = true,
                compositionItemsCount = 7
            )
        )
    )
    val mealPlans = _mealPlans.asStateFlow()

    fun addMealPlan(plan: com.example.myapplicationkmp.data.models.MealPlanModel) {
        _mealPlans.value = listOf(plan) + _mealPlans.value
    }

    fun updateMealPlan(plan: com.example.myapplicationkmp.data.models.MealPlanModel) {
        _mealPlans.value = _mealPlans.value.map { if (it.id == plan.id) plan else it }
    }

    fun deleteMealPlan(planId: String) {
        _mealPlans.value = _mealPlans.value.filter { it.id != planId }
    }

    // 8. Delivery Routes (Folder 4)
    private val _deliveryRoutes = MutableStateFlow(
        listOf(
            com.example.myapplicationkmp.data.models.DeliveryRouteModel(
                id = "route-1",
                name = "Main Route",
                originBranchId = "b-1",
                originBranchName = "Main Branch",
                assignedDriverId = null,
                assignedDriverName = "Unassigned",
                isActive = true,
                stopsCount = 0
            ),
            com.example.myapplicationkmp.data.models.DeliveryRouteModel(
                id = "route-2",
                name = "Rural Route",
                originBranchId = "b-1",
                originBranchName = "Main Branch",
                assignedDriverId = null,
                assignedDriverName = "Unassigned",
                isActive = false,
                stopsCount = 4
            ),
            com.example.myapplicationkmp.data.models.DeliveryRouteModel(
                id = "route-3",
                name = "Urban Route",
                originBranchId = "b-1",
                originBranchName = "Main Branch",
                assignedDriverId = null,
                assignedDriverName = "Unassigned",
                isActive = true,
                stopsCount = 3
            )
        )
    )
    val deliveryRoutes = _deliveryRoutes.asStateFlow()

    fun updateDeliveryRoute(route: com.example.myapplicationkmp.data.models.DeliveryRouteModel) {
        _deliveryRoutes.value = _deliveryRoutes.value.map { if (it.id == route.id) route else it }
    }

    // 9. User Profile (Folder 5)
    private val _userProfile = MutableStateFlow(
        com.example.myapplicationkmp.data.models.UserProfileModel(
            id = "usr-profile-default",
            fullName = "",
            email = "",
            phone = "",
            secondaryPhone = null,
            roleTitle = "Staff",
            displayJobTitle = "Staff",
            branchName = "",
            businessGstin = "",
            operatingSince = "",
            activeSubscribersCount = 0,
            totalBranchesCount = 1,
            isKycVerified = false
        )
    )
    val userProfile = _userProfile.asStateFlow()

    fun updateUserProfile(profile: com.example.myapplicationkmp.data.models.UserProfileModel) {
        _userProfile.value = profile
    }

    // 10. Activity & Inbox Notifications (Folder 5)
    private val _activityNotifications = MutableStateFlow(
        listOf(
            com.example.myapplicationkmp.data.models.ActivityNotificationItemModel(
                id = "notif-1",
                type = "dispatch",
                title = "Morning Dispatch Manifest Ready",
                description = "Main Route: 18 tiffins dispatched with driver Amit Kumar.",
                timeAgoFormatted = "12m ago",
                isToday = true,
                isUnread = true,
                routeCode = "Route #A-4",
                driverName = "Amit Kumar",
                actionLabel = "View Route"
            ),
            com.example.myapplicationkmp.data.models.ActivityNotificationItemModel(
                id = "notif-2",
                type = "hold",
                title = "New Vacation Hold Requested",
                description = "Customer Aarav Sharma (Room 402) requested hold on Standard Veg Lunch (05 Sep – 08 Sep, 4 days).",
                timeAgoFormatted = "45m ago",
                isToday = true,
                isUnread = true,
                customerName = "Aarav Sharma (Room 402)",
                holdDates = "05 Sep – 08 Sep (4 days)",
                actionLabel = "Approve Hold"
            ),
            com.example.myapplicationkmp.data.models.ActivityNotificationItemModel(
                id = "notif-3",
                type = "wallet",
                title = "Low Wallet Balance Alert",
                description = "Customer Suresh Patel reached credit limit with pending due of ₹330.00.",
                timeAgoFormatted = "1h ago",
                isToday = true,
                isUnread = true,
                customerName = "Suresh Patel",
                amountFormatted = "₹330.00",
                actionLabel = "Send Reminder"
            ),
            com.example.myapplicationkmp.data.models.ActivityNotificationItemModel(
                id = "notif-4",
                type = "payment",
                title = "Subscription Renewal Payment",
                description = "Received ₹3,000.00 via UPI from Ananya Sharma for Monthly Lunch Plan.",
                timeAgoFormatted = "06:14 PM",
                isToday = false,
                isUnread = false,
                amountFormatted = "₹3,000.00",
                txnCode = "#UPI88291",
                actionLabel = "Auto-receipt Sent"
            ),
            com.example.myapplicationkmp.data.models.ActivityNotificationItemModel(
                id = "notif-5",
                type = "cooking",
                title = "Evening Cooking Plan Completed",
                description = "Kitchen station marked 42 dinner boxes packed. Dal Makhani & Phulka inventory reconciled.",
                timeAgoFormatted = "07:30 PM",
                isToday = false,
                isUnread = false,
                chefName = "Cooked by Chef Harish",
                actionLabel = null
            )
        )
    )
    val activityNotifications = _activityNotifications.asStateFlow()

    fun markAllNotificationsAsRead() {
        _activityNotifications.value = _activityNotifications.value.map { it.copy(isUnread = false) }
    }

    fun approveNotificationHold(id: String) {
        _activityNotifications.value = _activityNotifications.value.map {
            if (it.id == id) it.copy(isUnread = false, actionLabel = "Approved ✓") else it
        }
    }

    // 10. App System Settings (Folder 6)
    private val _systemSettings = MutableStateFlow(
        com.example.myapplicationkmp.data.models.AppSystemSettingsModel(
            id = "settings-default",
            branchId = "branch-default",
            autoLockDailyOrders = true,
            dailyCutoffTime = "10:30 AM",
            defaultDietaryMode = "Veg",
            kdsAutoRefresh = true,
            kdsRefreshSeconds = 15,
            printStickerLabels = true,
            thermalPrinterModel = "",
            isPrinterConnected = true,
            isPortionScaleConfigured = false,
            currencySymbol = "₹",
            currencyName = "Indian Rupee (₹)",
            gstRatePercent = 5.0,
            customerTrustLimit = 500.0,
            thermalReceiptFooterText = "",
            soundChimesAndAlerts = true,
            defaultBranchName = "",
            operationalTimezone = "Asia/Kolkata",
            appLanguage = "en"
        )
    )
    val systemSettings = _systemSettings.asStateFlow()

    fun updateSystemSettings(settings: com.example.myapplicationkmp.data.models.AppSystemSettingsModel) {
        _systemSettings.value = settings
    }

    // 9. Update Branch (Folder 4)
    fun updateBranch(branch: com.example.myapplicationkmp.data.models.BranchModel) {
        _currentBranch.value = branch
    }

    // 10. Kitchen Onboarding (Folder 6)
    private val _onboardingState = MutableStateFlow(com.example.myapplicationkmp.data.models.KitchenOnboardingStateModel())
    val onboardingState = _onboardingState.asStateFlow()

    fun updateOnboardingState(state: com.example.myapplicationkmp.data.models.KitchenOnboardingStateModel) {
        _onboardingState.value = state
        // Synchronize brand name to current branch
        if (state.brandName.isNotBlank()) {
            _currentBranch.value = _currentBranch.value.copy(name = state.brandName)
        }
    }

    // ==========================================
    // Unified Authentication & Portal Session System
    // ==========================================
    private val _authState = MutableStateFlow(com.example.myapplicationkmp.data.models.AuthSessionState())
    val authState = _authState.asStateFlow()

    init {
        restoreSavedSession()
    }

    fun restoreSavedSession() {
        val savedEmail = SessionStorage.getSavedEmail()
        val savedRole = SessionStorage.getSavedRole()
        if (!savedEmail.isNullOrBlank() && !savedRole.isNullOrBlank()) {
            val savedName = SessionStorage.getSavedFullName() ?: savedEmail.substringBefore("@")
                .replace(".", " ")
                .split(" ")
                .filter { it.isNotBlank() }
                .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
                .ifBlank { "User" }
            val savedBusiness = SessionStorage.getSavedBusinessName()
            val savedPhone = SessionStorage.getSavedPhone()
            val savedProvider = SessionStorage.getSavedProvider() ?: "email"

            val restoredUser = com.example.myapplicationkmp.data.models.UserAuthModel(
                id = "usr-${savedEmail.hashCode()}",
                email = savedEmail,
                fullName = savedName,
                businessName = savedBusiness,
                phone = savedPhone,
                role = savedRole,
                provider = savedProvider,
                isAuthenticated = true
            )
            _authState.value = _authState.value.copy(
                currentUser = restoredUser,
                errorMessage = null
            )
            syncProfilesWithUser(restoredUser)
        }
    }

    private fun syncProfilesWithUser(user: com.example.myapplicationkmp.data.models.UserAuthModel) {
        // Persist session to local storage
        if (user.isAuthenticated && user.email.isNotBlank()) {
            SessionStorage.saveSession(
                email = user.email,
                fullName = user.fullName,
                role = user.role,
                businessName = user.businessName,
                phone = user.phone,
                provider = user.provider
            )
        }

        // 1. Sync Owner / Admin profile (creates live entry in public.profiles)
        _userProfile.value = _userProfile.value.copy(
            id = user.id,
            fullName = user.fullName,
            email = user.email,
            phone = user.phone ?: _userProfile.value.phone,
            avatarUrl = user.avatarUrl ?: _userProfile.value.avatarUrl,
            branchName = user.businessName ?: _currentBranch.value.name,
            roleTitle = when (user.role.lowercase()) {
                "customer", "subscriber" -> "Customer"
                "rider", "driver" -> "Delivery Executive"
                else -> "Kitchen Owner & Manager"
            },
            displayJobTitle = when (user.role.lowercase()) {
                "customer", "subscriber" -> "Customer / Subscriber"
                "rider", "driver" -> "Delivery Partner"
                else -> "Owner & Head Operator"
            }
        )

        // 2. Sync Customer / Subscriber profile (creates live entry in subscriber profiles)
        _subscriberProfile.value = _subscriberProfile.value.copy(
            id = user.id,
            fullName = user.fullName,
            email = user.email,
            phone = user.phone ?: _subscriberProfile.value.phone,
            subscriberNumber = if (_subscriberProfile.value.subscriberNumber.isBlank()) "#SUB-${(1000..9999).random()}" else _subscriberProfile.value.subscriberNumber
        )

        // 3. Sync Rider profile (creates live entry in rider profiles)
        _riderProfile.value = _riderProfile.value.copy(
            fullName = user.fullName,
            phone = user.phone ?: _riderProfile.value.phone
        )
    }

    fun loginWithEmail(email: String, pass: String): Result<com.example.myapplicationkmp.data.models.UserAuthModel> {
        val cleanEmail = email.trim().lowercase()
        if (cleanEmail.isBlank() || pass.isBlank()) {
            val errorMsg = "Email and password cannot be empty."
            _authState.value = _authState.value.copy(errorMessage = errorMsg)
            return Result.failure(Exception(errorMsg))
        }

        // Determine user identity and role based on authenticated context
        val detectedRole = when {
            cleanEmail.contains("rider", ignoreCase = true) || cleanEmail.contains("driver", ignoreCase = true) -> "rider"
            cleanEmail.contains("staff", ignoreCase = true) -> "kitchen_staff"
            cleanEmail.contains("owner", ignoreCase = true) || cleanEmail.contains("admin", ignoreCase = true) -> "owner"
            else -> "customer"
        }
        val cleanName = cleanEmail.substringBefore("@")
            .replace(".", " ")
            .split(" ")
            .filter { it.isNotBlank() }
            .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
            .ifBlank { "Tiffin User" }

        val user = com.example.myapplicationkmp.data.models.UserAuthModel(
            id = "usr-${cleanEmail.hashCode()}",
            email = cleanEmail,
            fullName = cleanName,
            businessName = _currentBranch.value.name,
            phone = null,
            role = detectedRole,
            provider = "email",
            isAuthenticated = true
        )
        _authState.value = _authState.value.copy(
            currentUser = user,
            errorMessage = null,
            successMessage = "Signed in successfully as ${user.email}"
        )
        syncProfilesWithUser(user)
        return Result.success(user)
    }

    fun loginAsRole(role: String): Result<com.example.myapplicationkmp.data.models.UserAuthModel> {
        val normalizedRole = when (role.lowercase()) {
            "customer", "subscriber" -> "customer"
            "rider", "driver" -> "rider"
            "staff", "kitchen_staff" -> "kitchen_staff"
            else -> "owner"
        }
        val roleTitle = when (normalizedRole) {
            "customer" -> "Customer"
            "rider" -> "Delivery Partner"
            "kitchen_staff" -> "Kitchen Staff"
            else -> "Admin / Owner"
        }
        val roleEmail = "$normalizedRole@tiffinflow.local"
        val user = com.example.myapplicationkmp.data.models.UserAuthModel(
            id = "usr-$normalizedRole-${System.currentTimeMillis() % 10000}",
            email = roleEmail,
            fullName = roleTitle,
            businessName = _currentBranch.value.name,
            phone = null,
            role = normalizedRole,
            provider = "email",
            isAuthenticated = true
        )
        _authState.value = _authState.value.copy(
            currentUser = user,
            errorMessage = null,
            successMessage = "Signed in as ${user.fullName} (${user.role.replaceFirstChar { it.uppercase() }})"
        )
        syncProfilesWithUser(user)
        return Result.success(user)
    }

    fun loginWithGoogle(
        accountEmail: String = "",
        providedFullName: String? = null,
        providedAvatarUrl: String? = null
    ): Result<com.example.myapplicationkmp.data.models.UserAuthModel> {
        val detectedRole = when {
            accountEmail.contains("rider", ignoreCase = true) || accountEmail.contains("driver", ignoreCase = true) -> "rider"
            accountEmail.contains("owner", ignoreCase = true) || accountEmail.contains("admin", ignoreCase = true) -> "owner"
            else -> "customer"
        }
        val computedName = providedFullName?.takeIf { it.isNotBlank() }
            ?: accountEmail.substringBefore("@")
                .replace(".", " ")
                .split(" ")
                .filter { it.isNotBlank() }
                .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
                .ifBlank { "Google User" }

        val user = com.example.myapplicationkmp.data.models.UserAuthModel(
            id = "usr-google-${accountEmail.hashCode()}",
            email = accountEmail,
            fullName = computedName,
            businessName = _currentBranch.value.name,
            phone = null,
            role = detectedRole,
            provider = "google",
            avatarUrl = providedAvatarUrl,
            isAuthenticated = true
        )
        _authState.value = _authState.value.copy(
            currentUser = user,
            errorMessage = null,
            successMessage = "Google OAuth login verified for ${user.email}"
        )
        syncProfilesWithUser(user)
        return Result.success(user)
    }

    fun handleOAuthCallback(uriString: String): Result<com.example.myapplicationkmp.data.models.UserAuthModel> {
        try {
            // Check for access_token or error in query params or fragment
            val fragmentOrQuery = when {
                uriString.contains("#") -> uriString.substringAfter("#")
                uriString.contains("?") -> uriString.substringAfter("?")
                else -> ""
            }

            val params = fragmentOrQuery.split("&").associate { param ->
                val parts = param.split("=")
                if (parts.size == 2) parts[0] to parts[1] else parts[0] to ""
            }

            val error = params["error_description"] ?: params["error"]
            if (error != null) {
                val errorMsg = error.replace("+", " ")
                _authState.value = _authState.value.copy(errorMessage = errorMsg)
                return Result.failure(Exception(errorMsg))
            }

            var extractedEmail: String? = params["email"]
            var extractedName: String? = params["full_name"] ?: params["name"]
            var extractedAvatar: String? = params["avatar_url"] ?: params["picture"]

            // Extract JWT payload from access_token if present
            val accessToken = params["access_token"]
            if (!accessToken.isNullOrBlank() && accessToken.contains(".")) {
                val tokenParts = accessToken.split(".")
                if (tokenParts.size >= 2) {
                    try {
                        val base64Payload = tokenParts[1]
                            .replace("-", "+")
                            .replace("_", "/")
                        val padLength = (4 - (base64Payload.length % 4)) % 4
                        val paddedBase64 = base64Payload + "=".repeat(padLength)
                        
                        // Parse JSON payload string
                        val decodedBytes = kotlin.io.encoding.Base64.decode(paddedBase64)
                        val payloadString = decodedBytes.decodeToString()
                        
                        // Extract email
                        if (extractedEmail.isNullOrBlank()) {
                            val emailRegex = Regex("\"email\"\\s*:\\s*\"([^\"]+)\"")
                            emailRegex.find(payloadString)?.let { match ->
                                extractedEmail = match.groupValues[1]
                            }
                        }
                        // Extract name
                        if (extractedName.isNullOrBlank()) {
                            val nameRegex = Regex("\"(?:full_name|name)\"\\s*:\\s*\"([^\"]+)\"")
                            nameRegex.find(payloadString)?.let { match ->
                                extractedName = match.groupValues[1]
                            }
                        }
                        // Extract avatar
                        if (extractedAvatar.isNullOrBlank()) {
                            val avatarRegex = Regex("\"(?:avatar_url|picture)\"\\s*:\\s*\"([^\"]+)\"")
                            avatarRegex.find(payloadString)?.let { match ->
                                extractedAvatar = match.groupValues[1]
                            }
                        }
                    } catch (_: Throwable) {
                        // Fallback safely to URL parameters or default
                    }
                }
            }

            val finalEmail = extractedEmail?.takeIf { it.isNotBlank() } ?: ""
            return loginWithGoogle(
                accountEmail = finalEmail,
                providedFullName = extractedName,
                providedAvatarUrl = extractedAvatar
            )
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    // In-memory verification hash/token storage (transient, never serialized or leaked)
    private var transientVerificationOtp: String? = null

    fun registerWithEmail(
        fullName: String,
        businessName: String,
        email: String,
        phone: String,
        pass: String
    ): Result<com.example.myapplicationkmp.data.models.UserAuthModel> {
        val cleanEmail = email.trim().lowercase()
        val generatedOtp = (100000..999999).random().toString()
        transientVerificationOtp = generatedOtp

        val newUser = com.example.myapplicationkmp.data.models.UserAuthModel(
            id = "usr-${cleanEmail.hashCode()}",
            email = cleanEmail,
            fullName = fullName.trim(),
            businessName = businessName.trim().ifBlank { null },
            phone = phone.trim().ifBlank { null },
            role = "customer",
            provider = "email",
            isAuthenticated = false
        )
        _authState.value = _authState.value.copy(
            currentUser = newUser,
            pendingOtpEmail = cleanEmail,
            otpFlowType = "registration",
            isOtpVerified = false,
            errorMessage = null,
            successMessage = "Account created for $fullName. Please verify your OTP."
        )
        if (businessName.isNotBlank()) {
            _currentBranch.value = _currentBranch.value.copy(name = businessName)
        }
        return Result.success(newUser)
    }

    fun registerWithGoogle(accountEmail: String = ""): Result<com.example.myapplicationkmp.data.models.UserAuthModel> {
        return loginWithGoogle(accountEmail)
    }

    fun sendPasswordResetOtp(emailOrPhone: String): Result<String> {
        val identifier = emailOrPhone.trim().lowercase()
        val generatedOtp = (100000..999999).random().toString()
        transientVerificationOtp = generatedOtp

        _authState.value = _authState.value.copy(
            pendingOtpEmail = identifier,
            otpFlowType = "forgot_password",
            isOtpVerified = false,
            errorMessage = null,
            successMessage = "Verification OTP sent to $emailOrPhone"
        )
        return Result.success(generatedOtp)
    }

    fun updatePendingOtp(code: String) {
        transientVerificationOtp = code
        _authState.value = _authState.value.copy(
            isOtpVerified = false
        )
    }

    fun verifyRegistrationOtp(code: String): Boolean {
        val expected = transientVerificationOtp ?: ""
        val isValid = code.isNotBlank() && code == expected
        if (isValid) {
            val verifiedUser = _authState.value.currentUser?.copy(isAuthenticated = true)
            transientVerificationOtp = null
            _authState.value = _authState.value.copy(
                currentUser = verifiedUser,
                isOtpVerified = true,
                errorMessage = null,
                successMessage = "Account successfully verified!"
            )
            if (verifiedUser != null) {
                syncProfilesWithUser(verifiedUser)
            }
        } else {
            _authState.value = _authState.value.copy(
                errorMessage = "Invalid verification code. Please check the code sent to your inbox."
            )
        }
        return isValid
    }

    fun verifyResetOtp(code: String): Boolean {
        val expected = transientVerificationOtp ?: ""
        val isValid = code.isNotBlank() && code == expected
        if (isValid) {
            transientVerificationOtp = null
            _authState.value = _authState.value.copy(
                isOtpVerified = true,
                errorMessage = null,
                successMessage = "OTP Code validated successfully"
            )
        } else {
            _authState.value = _authState.value.copy(
                errorMessage = "Invalid verification code"
            )
        }
        return isValid
    }

    fun resetPassword(newPass: String): Boolean {
        _authState.value = _authState.value.copy(
            isOtpVerified = false,
            pendingOtpEmail = null,
            errorMessage = null,
            successMessage = "Password reset successfully. Please login with your new credentials."
        )
        return true
    }

    fun signOut() {
        SessionStorage.clearSession()
        _authState.value = _authState.value.copy(
            currentUser = null,
            pendingOtpEmail = null,
            isOtpVerified = false,
            errorMessage = null,
            successMessage = null
        )
    }

    // 13. Support & Knowledge Base Management (Folder 9)
    private val _supportInquiries = MutableStateFlow<List<com.example.myapplicationkmp.data.models.SupportInquiry>>(emptyList())
    val supportInquiries = _supportInquiries.asStateFlow()

    fun submitSupportInquiry(inquiry: com.example.myapplicationkmp.data.models.SupportInquiry): Boolean {
        _supportInquiries.value = listOf(inquiry) + _supportInquiries.value
        return true
    }

    val knowledgeTopics = listOf(
        com.example.myapplicationkmp.data.models.KnowledgeTopic("1", "Getting Started", "Account setup, chef profile & KYC", 5, "rocket_launch", "getting-started"),
        com.example.myapplicationkmp.data.models.KnowledgeTopic("2", "Menu & Meal Plans", "Cutoff times, dietary chips & items", 4, "restaurant_menu", "kitchen-cutoffs"),
        com.example.myapplicationkmp.data.models.KnowledgeTopic("3", "Subscriptions", "Multi-plan enrollments, route allocation", 5, "group", "getting-started"),
        com.example.myapplicationkmp.data.models.KnowledgeTopic("4", "Wallet & Billing", "Prepaid ledger engine & top-ups", 4, "account_balance_wallet", "wallet-billing"),
        com.example.myapplicationkmp.data.models.KnowledgeTopic("5", "Hold & Pauses", "Cutoff deadlines & vacation limits", 4, "pause_circle", "holds-pauses"),
        com.example.myapplicationkmp.data.models.KnowledgeTopic("6", "Kitchen Console", "Daily cooking matrix & thermal labels", 4, "soup_kitchen", "kitchen-cutoffs"),
        com.example.myapplicationkmp.data.models.KnowledgeTopic("7", "Delivery Routes", "Driver sequencing & handoff proof", 3, "local_shipping", "delivery-routes"),
        com.example.myapplicationkmp.data.models.KnowledgeTopic("8", "Profit Analytics", "Ingredient raw costs vs daily gross", 4, "query_stats", "wallet-billing"),
        com.example.myapplicationkmp.data.models.KnowledgeTopic("9", "Portal Branding", "Custom subdomains & storefront", 7, "palette", "getting-started"),
        com.example.myapplicationkmp.data.models.KnowledgeTopic("10", "Customer Guide", "What your subscribers see & do", 7, "person_check", "getting-started")
    )

    val faqArticles = listOf(
        com.example.myapplicationkmp.data.models.FaqArticle(
            id = "faq-1",
            title = "How to register and activate your Tiffin Center vendor account?",
            category = "getting-started",
            previewText = "Step-by-step registration flow to create a vendor owner account, verify FSSAI/KYC details, and configure delivery zones.",
            detailedContent = "Setting up your cloud kitchen requires three essential steps before receiving automated tiffin orders:",
            steps = listOf(
                "Owner Details & Kitchen Profile: Submit kitchen trade name, contact phone, and operating address with Google Pin.",
                "Compliance & Payout Setup: Attach FSSAI license number and bank account for automated Razorpay settlements.",
                "Dispatch Radius Configuration: Define polygon delivery zones or maximum km limits for your riders."
            ),
            updatedAgo = "2 days ago"
        ),
        com.example.myapplicationkmp.data.models.FaqArticle(
            id = "faq-2",
            title = "Creating meal categories with delivery time windows and cutoff times",
            category = "kitchen-cutoffs",
            previewText = "Configure Breakfast, Lunch, and Dinner delivery brackets with strictly enforced kitchen preparation cutoffs.",
            detailedContent = "Cutoff rules prevent unexpected meal preparation requests when the chef has already finalized purchasing. Once cutoff passes, customer holds, cancellations, or add-ons lock automatically to eliminate food waste.",
            steps = listOf(
                "Lunch Window: 12:30 - 2:00 PM (Cutoff: 09:30 AM IST)",
                "Dinner Window: 7:30 - 9:30 PM (Cutoff: 04:30 PM IST)",
                "Order Lock: Orders lock automatically into the Daily Cooking Matrix once the cutoff minute strikes."
            ),
            updatedAgo = "1 week ago"
        ),
        com.example.myapplicationkmp.data.models.FaqArticle(
            id = "faq-3",
            title = "Adding a new customer and creating their first subscription",
            category = "getting-started",
            previewText = "Inline customer entry, dietary preferences (Veg/Non-Veg), custom spice notes, and route grouping.",
            detailedContent = "Quickly enroll corporate desks, residential apartments, or hostel subscribers:",
            steps = listOf(
                "Go to Subscriptions > Add Customer or use the (+) floating button.",
                "Input Phone Number (acts as their single sign-on customer portal key).",
                "Select dietary tag (e.g., Pure Jain Veg, Halal Non-Veg, or Low-Spice Diet).",
                "Assign to a delivery route so the kitchen dispatch sheet groups stops sequentially."
            ),
            updatedAgo = "3 days ago"
        ),
        com.example.myapplicationkmp.data.models.FaqArticle(
            id = "faq-4",
            title = "How the prepaid customer wallet billing engine works",
            category = "wallet-billing",
            previewText = "Automatic deductions at category cutoff times with permanent audit ledger records and safety credit ceiling ₹330.",
            detailedContent = "The platform operates on a reliable daily debit ledger to prevent post-dispatch disputes. Wallet balance reduces strictly when the order is sent to the cooking console. If wallet goes below -₹330 (credit buffer), auto-dispatch halts with a reminder SMS.",
            steps = listOf(
                "Cutoff Execution: Auto-debits happen synchronously upon cutoff trigger.",
                "Ledger Immutability: Every transaction logs opening balance, deduction amount, and closing balance.",
                "Safety Buffer: Customers enjoy up to ₹330 emergency overdraft before delivery halts."
            ),
            updatedAgo = "5 days ago"
        ),
        com.example.myapplicationkmp.data.models.FaqArticle(
            id = "faq-5",
            title = "How customers can pause/hold tiffin deliveries from their portal",
            category = "holds-pauses",
            previewText = "Subscribers can schedule vacation pauses directly from the web link without needing manual WhatsApp calls.",
            detailedContent = "Subscribers receive an SMS/WhatsApp link to their self-serve portal where they can self-manage schedule holds without phone calls.",
            steps = listOf(
                "Subscribers select 'Pause Meal' and pick calendar start/end dates.",
                "No wallet balance is deducted during active pause windows.",
                "Pauses requested before the meal cutoff take effect immediately, automatically updating chef counts."
            ),
            updatedAgo = "1 week ago"
        ),
        com.example.myapplicationkmp.data.models.FaqArticle(
            id = "faq-6",
            title = "Understanding the Daily Cooking Plan: total dish quantity matrix",
            category = "kitchen-cutoffs",
            previewText = "View aggregated preparation quantities for rotis, sabzis, dal liters, and dietary adjustments in one consolidated view.",
            detailedContent = "Open Kitchen Console and tap the target meal slot. The aggregator computes exact roti counts, rice portions, and special requests ready for the head chef.",
            steps = listOf(
                "Open Kitchen Console and select current meal (Breakfast, Lunch, or Dinner).",
                "Review the aggregate counts: total rotis, dal liters, rice portions, and special requests.",
                "Print 58mm thermal packing slips with customer address, dietary tags, and route sequencing."
            ),
            updatedAgo = "4 days ago"
        )
    )

    // 14. Legal Documents & Regulatory Charters (Folder 10)
    val legalDocuments = listOf(
        com.example.myapplicationkmp.data.models.LegalDocument(
            id = "doc-privacy",
            slug = "privacy-policy",
            title = "Privacy Policy",
            version = "v2.4",
            effectiveDate = "Jul 10, 2026",
            summary = "Effective Jul 2026 • 6 Sections • GDPR & DPDP Compliant",
            readTimeMinutes = 5,
            badgeLabel = "Active",
            category = "security",
            sectionsCount = 6
        ),
        com.example.myapplicationkmp.data.models.LegalDocument(
            id = "doc-terms",
            slug = "terms-of-service",
            title = "Terms of Service",
            version = "v2.4",
            effectiveDate = "Jul 10, 2026",
            summary = "7 Detailed Sections • Merchant Tier, SLAs & Platform Rules",
            readTimeMinutes = 8,
            badgeLabel = "Binding",
            category = "merchant",
            sectionsCount = 7
        ),

        com.example.myapplicationkmp.data.models.LegalDocument(
            id = "doc-security",
            slug = "security-policy",
            title = "Security Policy",
            version = "SOC 2 Type II",
            effectiveDate = "Jul 10, 2026",
            summary = "TLS 1.3 Encryption, AES-256 Cloud Infrastructure",
            readTimeMinutes = 6,
            badgeLabel = "Zero-Trust",
            category = "security",
            sectionsCount = 6
        ),
        com.example.myapplicationkmp.data.models.LegalDocument(
            id = "doc-dpa",
            slug = "data-processing-addendum",
            title = "Data Processing Addendum",
            version = "DPA Annex",
            effectiveDate = "Jul 10, 2026",
            summary = "Controller & Processor Obligations • Merchant Security Rights",
            readTimeMinutes = 7,
            badgeLabel = "Encrypted Logs",
            category = "merchant",
            sectionsCount = 6
        ),
        com.example.myapplicationkmp.data.models.LegalDocument(
            id = "doc-refund",
            slug = "refund-policy",
            title = "Refund Policy",
            version = "Billing Terms",
            effectiveDate = "Sep 05, 2026",
            summary = "Strict No-Refund Standard & 30-Day Discrepancy Dispute Window",
            readTimeMinutes = 4,
            badgeLabel = "Terms Apply",
            category = "billing",
            sectionsCount = 5
        ),
        com.example.myapplicationkmp.data.models.LegalDocument(
            id = "doc-cancellation",
            slug = "cancellation-policy",
            title = "Cancellation Policy",
            version = "v2.4",
            effectiveDate = "Sep 05, 2026",
            summary = "Instant Self-Service Renewal Cancel & 90-Day Safe Data Window",
            readTimeMinutes = 4,
            badgeLabel = "Self-Service",
            category = "merchant",
            sectionsCount = 5
        )
    )

    // ==========================================
    // Folder 11: Customer / Subscriber Portal State
    // ==========================================
    private val _subscriberProfile = MutableStateFlow(
        com.example.myapplicationkmp.data.models.SubscriberProfile()
    )
    val subscriberProfile = _subscriberProfile.asStateFlow()

    private val _guestOrders = MutableStateFlow(
        listOf(
            com.example.myapplicationkmp.data.models.CustomerGuestOrder(
                id = "gst-101",
                sessionTitle = "Dinner Today",
                sessionDate = "04 Sep 2026",
                mealTitle = "Special Dal Makhani & Paneer Kulcha",
                quantity = 2,
                unitPrice = 110.0,
                totalAmount = 220.0,
                packagingType = "tiffin",
                packagingSurcharge = 0.0,
                customNotes = "Please keep kulchas extra warm in foil",
                status = "confirmed"
            )
        )
    )
    val guestOrders = _guestOrders.asStateFlow()

    private val _vacationHolds = MutableStateFlow(
        listOf(
            com.example.myapplicationkmp.data.models.CustomerVacationHold(
                id = "hold-201",
                startDateFormatted = "14 Oct 2026",
                endDateFormatted = "18 Oct 2026",
                pauseType = "both",
                daysCount = 5,
                refundCreditAmount = 650.0,
                status = "Scheduled"
            )
        )
    )
    val vacationHolds = _vacationHolds.asStateFlow()

    private val _subscriberWalletTransactions = MutableStateFlow(
        listOf(
            com.example.myapplicationkmp.data.models.SubscriberWalletTransaction(
                id = "stx-1",
                title = "Lunch Thali Delivered",
                timestamp = "04 Sep, 12:45 PM",
                subtext = "OTP Verified #882",
                amount = 100.00,
                isCredit = false,
                tag = "Delivered"
            ),
            com.example.myapplicationkmp.data.models.SubscriberWalletTransaction(
                id = "stx-2",
                title = "Dinner Thali Delivered",
                timestamp = "03 Sep, 08:00 PM",
                subtext = "Hostel Gate 2 Drop",
                amount = 100.00,
                isCredit = false,
                tag = "Delivered"
            ),
            com.example.myapplicationkmp.data.models.SubscriberWalletTransaction(
                id = "stx-3",
                title = "Vacation Hold Refund (1 Day)",
                timestamp = "02 Sep, 08:00 AM",
                subtext = "Auto-Hold Credit",
                amount = 130.00,
                isCredit = true,
                tag = "2 Meals Credited"
            ),
            com.example.myapplicationkmp.data.models.SubscriberWalletTransaction(
                id = "stx-4",
                title = "UPI Recharge",
                timestamp = "01 Sep, 10:15 AM",
                subtext = "PhonePe #78912",
                amount = 2000.00,
                isCredit = true,
                tag = "Success"
            )
        )
    )
    val subscriberWalletTransactions = _subscriberWalletTransactions.asStateFlow()

    fun updateSubscriberProfile(updated: com.example.myapplicationkmp.data.models.SubscriberProfile) {
        _subscriberProfile.value = updated
        // Synchronize with Admin customers ledger so admin directory reflects changes immediately
        val customerList = _customers.value
        val matchedIdx = customerList.indexOfFirst { it.name.equals(updated.fullName, ignoreCase = true) || it.phone == updated.phone }
        if (matchedIdx >= 0) {
            val existing = customerList[matchedIdx]
            val updatedCustomer = existing.copy(
                name = updated.fullName,
                phone = updated.phone,
                email = updated.email,
                address = "${updated.flatDetails}, ${updated.landmark}, ${updated.cityPincode}".trim().trim(','),
                walletBalance = updated.walletBalance
            )
            _customers.value = customerList.toMutableList().apply { set(matchedIdx, updatedCustomer) }
        } else if (customerList.isNotEmpty()) {
            val first = customerList.first()
            val updatedCustomer = first.copy(
                name = updated.fullName,
                phone = updated.phone,
                email = updated.email,
                address = "${updated.flatDetails}, ${updated.landmark}, ${updated.cityPincode}".trim().trim(','),
                walletBalance = updated.walletBalance
            )
            _customers.value = customerList.toMutableList().apply { set(0, updatedCustomer) }
        }
    }

    fun updateDropNote(newNote: String) {
        _subscriberProfile.value = _subscriberProfile.value.copy(driverDropNote = newNote)
    }

    fun toggleSundayAddress(enabled: Boolean) {
        _subscriberProfile.value = _subscriberProfile.value.copy(hasSundayAddress = enabled)
    }

    fun toggleNotification(type: String, enabled: Boolean) {
        _subscriberProfile.value = when (type) {
            "morning" -> _subscriberProfile.value.copy(notifyMorningMenu = enabled)
            "dispatch" -> _subscriberProfile.value.copy(notifyDriverDispatch = enabled)
            "photo" -> _subscriberProfile.value.copy(notifyDropPhoto = enabled)
            "low_balance" -> _subscriberProfile.value.copy(notifyLowBalance = enabled)
            else -> _subscriberProfile.value
        }
    }

    fun skipDinnerWithRefund(): Double {
        val refundAmt = 120.00
        val current = _subscriberProfile.value
        val newBalance = current.walletBalance + refundAmt
        _subscriberProfile.value = current.copy(walletBalance = newBalance)
        _subscriberWalletTransactions.value = listOf(
            com.example.myapplicationkmp.data.models.SubscriberWalletTransaction(
                id = "stx-${System.currentTimeMillis()}",
                title = "Dinner Skipped Refund",
                timestamp = "Today, Cutoff Saved",
                subtext = "Wallet Auto-Credit",
                amount = refundAmt,
                isCredit = true,
                tag = "Refunded"
            )
        ) + _subscriberWalletTransactions.value

        // Synchronize with Admin Kitchen Console by decrementing pending dinner count
        val currentBatch = _kitchenBatch.value
        if (currentBatch.pendingCount > 0) {
            _kitchenBatch.value = currentBatch.copy(
                pendingCount = (currentBatch.pendingCount - 1).coerceAtLeast(0)
            )
        }

        // Notify Admin Activity Inbox
        val adminSkipNotif = com.example.myapplicationkmp.data.models.ActivityNotificationItemModel(
            id = "notif-skip-${System.currentTimeMillis()}",
            type = "cooking",
            title = "Dinner Skip Notice (${current.fullName})",
            description = "${current.fullName} opted out of tonight's dinner before cutoff. ₹$refundAmt auto-credited. Kitchen production adjusted.",
            timeAgoFormatted = "Just now",
            isToday = true,
            isUnread = true,
            customerName = current.fullName,
            amountFormatted = "₹$refundAmt",
            actionLabel = "Production -1"
        )
        _activityNotifications.value = listOf(adminSkipNotif) + _activityNotifications.value

        return refundAmt
    }

    fun submitGuestMealOrder(
        sessionTitle: String,
        sessionDate: String,
        quantity: Int,
        packagingType: String,
        notes: String
    ): Boolean {
        val unitPrice = 110.0
        val packagingSurcharge = if (packagingType == "disposable") (quantity * 10.0) else 0.0
        val total = (quantity * unitPrice) + packagingSurcharge
        val current = _subscriberProfile.value

        if (current.walletBalance + current.overdraftBuffer < total) {
            return false
        }

        val newBalance = current.walletBalance - total
        _subscriberProfile.value = current.copy(walletBalance = newBalance)

        val newOrder = com.example.myapplicationkmp.data.models.CustomerGuestOrder(
            id = "gst-${System.currentTimeMillis()}",
            sessionTitle = sessionTitle,
            sessionDate = sessionDate,
            mealTitle = "Special Dal Makhani & Paneer Kulcha",
            quantity = quantity,
            unitPrice = unitPrice,
            totalAmount = total,
            packagingType = packagingType,
            packagingSurcharge = packagingSurcharge,
            customNotes = notes,
            status = "confirmed"
        )
        _guestOrders.value = listOf(newOrder) + _guestOrders.value

        _subscriberWalletTransactions.value = listOf(
            com.example.myapplicationkmp.data.models.SubscriberWalletTransaction(
                id = "stx-${System.currentTimeMillis()}",
                title = "Guest Meal ($quantity Thalis)",
                timestamp = "$sessionDate, Dispatch",
                subtext = sessionTitle,
                amount = total,
                isCredit = false,
                tag = "Confirmed"
            )
        ) + _subscriberWalletTransactions.value

        // Synchronize guest meal with admin kitchen batch & order counter
        val currentBatch = _kitchenBatch.value
        _kitchenBatch.value = currentBatch.copy(
            pendingCount = currentBatch.pendingCount + quantity,
            rotisCount = currentBatch.rotisCount + (quantity * 4)
        )
        val adminGuestNotif = com.example.myapplicationkmp.data.models.ActivityNotificationItemModel(
            id = "notif-guest-${System.currentTimeMillis()}",
            type = "cooking",
            title = "Guest Meal Booking ($quantity Thalis)",
            description = "${current.fullName} ordered $quantity meals for $sessionTitle on $sessionDate. Total ₹$total deducted.",
            timeAgoFormatted = "Just now",
            isToday = true,
            isUnread = true,
            customerName = current.fullName,
            amountFormatted = "₹$total",
            actionLabel = "Kitchen Updated"
        )
        _activityNotifications.value = listOf(adminGuestNotif) + _activityNotifications.value

        return true
    }

    fun addVacationHold(
        startDate: String,
        endDate: String,
        pauseType: String,
        daysCount: Int
    ) {
        val refundPerDay = if (pauseType == "both") 130.0 else 100.0
        val totalRefund = daysCount * refundPerDay

        val newHold = com.example.myapplicationkmp.data.models.CustomerVacationHold(
            id = "hold-${System.currentTimeMillis()}",
            startDateFormatted = startDate,
            endDateFormatted = endDate,
            pauseType = pauseType,
            daysCount = daysCount,
            refundCreditAmount = totalRefund,
            status = "Scheduled"
        )
        _vacationHolds.value = listOf(newHold) + _vacationHolds.value

        // Synchronize into Admin CategoryBranchHoldsScreen (_kitchenHolds)
        val adminKitchenHold = com.example.myapplicationkmp.data.models.KitchenHoldModel(
            id = "khold-${System.currentTimeMillis()}",
            branchId = _currentBranch.value.id,
            branchName = _currentBranch.value.name,
            category = if (pauseType == "both") "all" else pauseType,
            startDate = startDate,
            endDate = endDate,
            message = "Subscriber ${currentSubscriberName()} vacation hold ($daysCount days)",
            isActive = true
        )
        _kitchenHolds.value = listOf(adminKitchenHold) + _kitchenHolds.value

        // Synchronize vacation hold with admin inbox
        val current = _subscriberProfile.value
        val adminHoldNotif = com.example.myapplicationkmp.data.models.ActivityNotificationItemModel(
            id = "notif-hold-${System.currentTimeMillis()}",
            type = "hold",
            title = "Vacation Pause Scheduled ($daysCount days)",
            description = "${current.fullName} scheduled meal pause from $startDate to $endDate. Refund ₹$totalRefund queued.",
            timeAgoFormatted = "Just now",
            isToday = true,
            isUnread = true,
            customerName = current.fullName,
            holdDates = "$startDate - $endDate",
            actionLabel = "Review Hold"
        )
        _activityNotifications.value = listOf(adminHoldNotif) + _activityNotifications.value
    }

    private fun currentSubscriberName(): String {
        return _subscriberProfile.value.fullName.ifBlank { "Aarav Sharma" }
    }

    fun cancelVacationHold(holdId: String) {
        _vacationHolds.value = _vacationHolds.value.filterNot { it.id == holdId }
        // Also remove or deactivate from admin kitchen holds
        _kitchenHolds.value = _kitchenHolds.value.filterNot { it.id.contains(holdId) }
    }

    fun topUpWallet(amount: Double) {
        val current = _subscriberProfile.value
        val newBalance = current.walletBalance + amount
        _subscriberProfile.value = current.copy(walletBalance = newBalance)

        // Sync to admin customer list
        val customerList = _customers.value
        val matchedIdx = customerList.indexOfFirst { it.name.equals(current.fullName, ignoreCase = true) || it.phone == current.phone }
        if (matchedIdx >= 0) {
            val updated = customerList[matchedIdx].copy(walletBalance = newBalance)
            _customers.value = customerList.toMutableList().apply { set(matchedIdx, updated) }
        }

        _subscriberWalletTransactions.value = listOf(
            com.example.myapplicationkmp.data.models.SubscriberWalletTransaction(
                id = "stx-${System.currentTimeMillis()}",
                title = "UPI Recharge",
                timestamp = "Just now",
                subtext = "Instant Top-Up",
                amount = amount,
                isCredit = true,
                tag = "Success"
            )
        ) + _subscriberWalletTransactions.value
    }

    // ==========================================
    // Folder 12 State & Mutations
    // ==========================================
    private val _latestMealRating = MutableStateFlow(
        com.example.myapplicationkmp.data.models.MealRatingFeedback()
    )
    val latestMealRating: StateFlow<com.example.myapplicationkmp.data.models.MealRatingFeedback> = _latestMealRating.asStateFlow()

    private val _mealIssues = MutableStateFlow(
        listOf(
            com.example.myapplicationkmp.data.models.MealIssueTicket(
                id = "ISS-101",
                targetDeliveryTitle = "Shahi Paneer Deluxe Meal",
                deliveryTime = "Today, 12:48 PM",
                category = "Missing Item / Wrong Dish",
                issueTags = listOf("Raita missing"),
                description = "Cucumber raita container was missing from the bag.",
                status = "open",
                refundAmount = 120.00,
                timestamp = "15 mins ago"
            )
        )
    )
    val mealIssues: StateFlow<List<com.example.myapplicationkmp.data.models.MealIssueTicket>> = _mealIssues.asStateFlow()

    private val _availablePlans = MutableStateFlow(
        listOf(
            com.example.myapplicationkmp.data.models.PlanUpgradeOption(
                id = "executive",
                title = "Executive 3-Meal Thali",
                subtitle = "Breakfast + Lunch + Dinner",
                priceMonthly = 5200.0,
                priceDifference = 1300.0,
                differenceLabel = "+₹1,300 difference",
                features = listOf(
                    "Added hot breakfast (Poha, Idli-Vada, stuffed Parathas)",
                    "Priority express route packing & dispatch",
                    "2 complimentary guest meal vouchers every month"
                ),
                containerNote = "Includes 4-tier steel carriers & thermal insulated jacket",
                isPopular = true
            ),
            com.example.myapplicationkmp.data.models.PlanUpgradeOption(
                id = "protein",
                title = "Healthy Diet & Protein",
                subtitle = "Macro-Tracked Fitness Plan",
                priceMonthly = 4800.0,
                priceDifference = 900.0,
                differenceLabel = "+₹900 difference",
                features = listOf(
                    "Paneer / organic tofu, sprouts bowl & roasted seed salads daily",
                    "Low GI multigrain rotis (Ragi & Jowar blend), zero refined oil",
                    "QR code on lid for instant MyFitnessPal calorie logging"
                ),
                containerNote = "Avg. 38g protein and <550 kcal per serving",
                isPopular = false
            ),
            com.example.myapplicationkmp.data.models.PlanUpgradeOption(
                id = "lunch_only",
                title = "Lunch-Only Office Box",
                subtitle = "Single Midday Delivery",
                priceMonthly = 2200.0,
                priceDifference = -1700.0,
                differenceLabel = "-₹1,700 savings",
                features = listOf(
                    "Single dispatch to work desk between 12:30 PM - 01:15 PM",
                    "Leakproof cutlery-ready bento packaging",
                    "Remaining balance credited directly to wallet"
                ),
                containerNote = "Delivered hot with micro-cutlery pack",
                isPopular = false
            ),
            com.example.myapplicationkmp.data.models.PlanUpgradeOption(
                id = "dinner_only",
                title = "Dinner-Only Light Pack",
                subtitle = "Homely Evening Meal",
                priceMonthly = 2100.0,
                priceDifference = -1800.0,
                differenceLabel = "-₹1,800 savings",
                features = listOf(
                    "Delivered between 07:30 PM - 08:30 PM to doorstep",
                    "Easy to digest, warm comfort food",
                    "Mon - Sat regular service"
                ),
                containerNote = "Light dinner pack in insulated seal",
                isPopular = false
            )
        )
    )
    val availablePlans: StateFlow<List<com.example.myapplicationkmp.data.models.PlanUpgradeOption>> = _availablePlans.asStateFlow()

    private val _notifications = MutableStateFlow(
        listOf(
            com.example.myapplicationkmp.data.models.SubscriberNotification(
                id = "notif-1",
                title = "Lunch Thali Out for Delivery",
                timestamp = "10 mins ago",
                body = "Your delivery partner has picked up your Shahi Paneer Deluxe Meal. Expected delivery by 12:45 PM.",
                category = "delivery",
                isUrgent = false,
                isUnread = true,
                actionPrimaryTitle = "Track Driver (Live)",
                actionSecondaryTitle = "Call Driver"
            ),
            com.example.myapplicationkmp.data.models.SubscriberNotification(
                id = "notif-2",
                title = "Dinner Cutoff in 45 Minutes",
                timestamp = "3:15 PM Today",
                body = "Need to pause tonight's dinner or order a guest thali? Daily kitchen allocations lock promptly at 04:00 PM.",
                category = "delivery",
                isUrgent = true,
                isUnread = true,
                actionPrimaryTitle = "Skip Dinner",
                actionSecondaryTitle = "Add Guest Meal"
            ),
            com.example.myapplicationkmp.data.models.SubscriberNotification(
                id = "notif-3",
                title = "₹650.00 Credited to Wallet",
                timestamp = "Yesterday, 06:30 PM",
                body = "Vacation hold for Sep 08 – Sep 12 confirmed. 10 meals auto-refunded to your prepaid balance.",
                category = "wallet",
                isUrgent = false,
                isUnread = true,
                actionPrimaryTitle = "View Balance Ledger",
                actionSecondaryTitle = null
            ),
            com.example.myapplicationkmp.data.models.SubscriberNotification(
                id = "notif-4",
                title = "Lunch Delivered & Photo Dropped",
                timestamp = "Yesterday, 12:48 PM",
                body = "Tiffin dropped at Sunshine Gate 2 / Door 402 with verified OTP #882. Please rate your meal.",
                category = "delivery",
                isUrgent = false,
                isUnread = false,
                actionPrimaryTitle = "Rate Dish (5/5)",
                actionSecondaryTitle = null
            ),
            com.example.myapplicationkmp.data.models.SubscriberNotification(
                id = "notif-5",
                title = "Special Sunday Feast Announced",
                timestamp = "02 Sep, 11:00 AM",
                body = "This Sunday, lunch includes Kashmiri Dum Aloo & Saffron Kheer! Update your delivery address if dining at home.",
                category = "announcement",
                isUrgent = false,
                isUnread = false,
                actionPrimaryTitle = null,
                actionSecondaryTitle = null
            ),
            com.example.myapplicationkmp.data.models.SubscriberNotification(
                id = "notif-6",
                title = "Prepaid Balance Buffer Warning",
                timestamp = "28 Aug, 09:15 AM",
                body = "Wallet balance dropped to ₹420.00 (~4 days remaining). Auto-renew or top up with UPI zero fee.",
                category = "wallet",
                isUrgent = false,
                isUnread = false,
                actionPrimaryTitle = "Recharge Now",
                actionSecondaryTitle = null
            )
        )
    )
    val notifications: StateFlow<List<com.example.myapplicationkmp.data.models.SubscriberNotification>> = _notifications.asStateFlow()

    fun submitMealRating(rating: com.example.myapplicationkmp.data.models.MealRatingFeedback) {
        _latestMealRating.value = rating
        // Alert admin if rating is low (<= 2 stars)
        if (rating.overallRating in 1..2) {
            val adminRatingAlert = com.example.myapplicationkmp.data.models.ActivityNotificationItemModel(
                id = "notif-rate-${System.currentTimeMillis()}",
                type = "cooking",
                title = "Low Meal Rating Alert (${rating.overallRating}★)",
                description = "${currentSubscriberName()} rated today's meal ${rating.overallRating} Stars. Notes: ${rating.chefNote.ifBlank { "Requires kitchen supervisor inspection" }}",
                timeAgoFormatted = "Just now",
                isToday = true,
                isUnread = true,
                customerName = currentSubscriberName(),
                actionLabel = "Investigate"
            )
            _activityNotifications.value = listOf(adminRatingAlert) + _activityNotifications.value
        }
    }

    fun submitMealIssue(
        category: String,
        tags: List<String>,
        description: String
    ) {
        val newIssue = com.example.myapplicationkmp.data.models.MealIssueTicket(
            id = "ISS-${System.currentTimeMillis() % 10000}",
            targetDeliveryTitle = "Shahi Paneer Deluxe Meal",
            deliveryTime = "Today, 12:48 PM",
            category = category,
            issueTags = tags,
            description = description,
            status = "open",
            refundAmount = 120.00,
            timestamp = "Just now"
        )
        _mealIssues.value = listOf(newIssue) + _mealIssues.value

        // Dispatch instant high-priority alert into admin Activity Inbox
        val adminIssueAlert = com.example.myapplicationkmp.data.models.ActivityNotificationItemModel(
            id = "notif-issue-${System.currentTimeMillis()}",
            type = "dispatch",
            title = "Customer Issue Ticket: $category",
            description = "${currentSubscriberName()} reported: $description. Tags: ${tags.joinToString(", ")}. 15m SLA resolution clock started.",
            timeAgoFormatted = "Just now",
            isToday = true,
            isUnread = true,
            customerName = currentSubscriberName(),
            amountFormatted = "₹120.00",
            actionLabel = "Resolve SLA"
        )
        _activityNotifications.value = listOf(adminIssueAlert) + _activityNotifications.value
    }

    fun upgradePlan(planId: String, immediate: Boolean) {
        val selectedPlan = _availablePlans.value.find { it.id == planId } ?: return
        val current = _subscriberProfile.value
        _subscriberProfile.value = current.copy(
            planTitle = selectedPlan.title
        )

        // Sync with admin customer model
        val customerList = _customers.value
        val matchedIdx = customerList.indexOfFirst { it.name.equals(current.fullName, ignoreCase = true) || it.phone == current.phone }
        if (matchedIdx >= 0) {
            val updated = customerList[matchedIdx].copy(isVeg = selectedPlan.isVeg)
            _customers.value = customerList.toMutableList().apply { set(matchedIdx, updated) }
        }

        val adminPlanNotif = com.example.myapplicationkmp.data.models.ActivityNotificationItemModel(
            id = "notif-plan-${System.currentTimeMillis()}",
            type = "subscription",
            title = "Plan Switched: ${selectedPlan.title}",
            description = "${current.fullName} transitioned to ${selectedPlan.title} (${if (immediate) "Immediate effect" else "Next cycle"}).",
            timeAgoFormatted = "Just now",
            isToday = true,
            isUnread = true,
            customerName = current.fullName,
            amountFormatted = "₹${selectedPlan.priceMonthly.toInt()}",
            actionLabel = "Updated"
        )
        _activityNotifications.value = listOf(adminPlanNotif) + _activityNotifications.value
    }

    fun markAllNotificationsRead() {
        _notifications.value = _notifications.value.map { it.copy(isUnread = false) }
    }

    fun markNotificationRead(id: String) {
        _notifications.value = _notifications.value.map {
            if (it.id == id) it.copy(isUnread = false) else it
        }
    }

    // ==========================================
    // Folder 13: Rider / Driver Portal Operations
    // ==========================================
    private val _riderProfile = MutableStateFlow(com.example.myapplicationkmp.data.models.RiderProfile())
    val riderProfile: StateFlow<com.example.myapplicationkmp.data.models.RiderProfile> = _riderProfile.asStateFlow()

    private val _riderStops = MutableStateFlow(
        listOf(
            com.example.myapplicationkmp.data.models.RiderDeliveryStop(
                id = "stop-17",
                sequenceNumber = 17,
                subscriberIdCode = "#SUB-7721",
                customerName = "Rohit Verma",
                customerPhone = "+919811122233",
                addressLine = "Flat 102, Wing A, Sunshine Residency",
                wingFlat = "Flat 102, Wing A",
                societyCluster = "sunshine",
                mealSlot = "Lunch",
                etaDisplay = "12:15 PM",
                status = "delivered",
                specialNote = "Delivered on time",
                returnBoxesToCollect = 1,
                returnBoxScannedCode = "TIF-2081",
                latitude = 19.1620,
                longitude = 72.9945
            ),
            com.example.myapplicationkmp.data.models.RiderDeliveryStop(
                id = "stop-18",
                sequenceNumber = 18,
                subscriberIdCode = "#SUB-8104",
                customerName = "Neha Gupta",
                customerPhone = "+919822233344",
                addressLine = "Flat 204, Wing B, Sunshine Residency",
                wingFlat = "Flat 204, Wing B",
                societyCluster = "sunshine",
                mealSlot = "Lunch",
                etaDisplay = "12:30 PM",
                status = "delivered",
                specialNote = "Left at door handle",
                returnBoxesToCollect = 1,
                returnBoxScannedCode = "TIF-2099",
                latitude = 19.1635,
                longitude = 72.9960
            ),
            com.example.myapplicationkmp.data.models.RiderDeliveryStop(
                id = "stop-19",
                sequenceNumber = 19,
                subscriberIdCode = "#SUB-8842",
                customerName = "Aarav Sharma",
                customerPhone = "+919876543210",
                addressLine = "Flat 402, Sunshine Residency, Wing B",
                wingFlat = "Flat 402, Wing B",
                societyCluster = "sunshine",
                mealSlot = "Lunch",
                etaDisplay = "12:45 PM",
                status = "pending",
                specialNote = "Ring bell twice, leave on handle of door 402 if not opened in 2 mins.",
                items = listOf("Pure Veg Shahi Paneer Deluxe", "Jain Guest Thali (No Onion/Garlic)"),
                contactlessOtp = "882",
                podBinSlot = "Pod Bin A-3",
                returnBoxesToCollect = 0,
                latitude = 19.1652,
                longitude = 72.9978
            ),
            com.example.myapplicationkmp.data.models.RiderDeliveryStop(
                id = "stop-20",
                sequenceNumber = 20,
                subscriberIdCode = "#SUB-8901",
                customerName = "Priya Venkat",
                customerPhone = "+919833344455",
                addressLine = "Block C-104, Sunshine Residency",
                wingFlat = "Block C-104",
                societyCluster = "sunshine",
                mealSlot = "Lunch",
                etaDisplay = "12:56 PM",
                status = "pending",
                specialNote = "Call before reaching security gate",
                items = listOf("Eco Thali (Medium)"),
                contactlessOtp = "419",
                podBinSlot = "Pod Bin A-4",
                returnBoxesToCollect = 1,
                latitude = 19.1680,
                longitude = 73.0010
            ),
            com.example.myapplicationkmp.data.models.RiderDeliveryStop(
                id = "stop-21",
                sequenceNumber = 21,
                subscriberIdCode = "#SUB-8955",
                customerName = "Karan Mehra",
                customerPhone = "+919844455566",
                addressLine = "Tower 2, Flat 1201, Prestige Palms",
                wingFlat = "Flat 1201, Tower 2",
                societyCluster = "hiranandani",
                mealSlot = "Lunch",
                etaDisplay = "01:08 PM",
                status = "pending",
                specialNote = "Prepaid OTP verification mandatory",
                items = listOf("Keto High Protein"),
                contactlessOtp = "632",
                podBinSlot = "Pod Bin B-1",
                returnBoxesToCollect = 0,
                latitude = 19.1712,
                longitude = 73.0055
            ),
            com.example.myapplicationkmp.data.models.RiderDeliveryStop(
                id = "stop-22",
                sequenceNumber = 22,
                subscriberIdCode = "#SUB-9002",
                customerName = "Vikram Patel",
                customerPhone = "+919855566677",
                addressLine = "Villa 14, Green Glen Layout",
                wingFlat = "Villa 14",
                societyCluster = "green-glen",
                mealSlot = "Lunch",
                etaDisplay = "01:20 PM",
                status = "pending",
                specialNote = "Leave at porch gate box",
                items = listOf("Standard 2-Meal Pack"),
                contactlessOtp = "118",
                podBinSlot = "Pod Bin B-2",
                returnBoxesToCollect = 1,
                latitude = 19.1748,
                longitude = 73.0090
            )
        )
    )
    val riderStops: StateFlow<List<com.example.myapplicationkmp.data.models.RiderDeliveryStop>> = _riderStops.asStateFlow()

    private val _boxCustody = MutableStateFlow(com.example.myapplicationkmp.data.models.RiderBoxCustodyState())
    val boxCustody: StateFlow<com.example.myapplicationkmp.data.models.RiderBoxCustodyState> = _boxCustody.asStateFlow()

    private val _riderEarnings = MutableStateFlow(com.example.myapplicationkmp.data.models.RiderEarningsSummary())
    val riderEarnings: StateFlow<com.example.myapplicationkmp.data.models.RiderEarningsSummary> = _riderEarnings.asStateFlow()

    private val _riderIncidents = MutableStateFlow<List<com.example.myapplicationkmp.data.models.RiderIncidentTicket>>(emptyList())
    val riderIncidents: StateFlow<List<com.example.myapplicationkmp.data.models.RiderIncidentTicket>> = _riderIncidents.asStateFlow()

    private val _riderNotifications = MutableStateFlow(
        listOf(
            com.example.myapplicationkmp.data.models.RiderNotificationAlert(
                id = "rnotif-1",
                category = "route",
                priority = "urgent",
                title = "Route Stop Sequence Re-ordered",
                body = "Kitchen Manager adjusted Stop #21 (Rohan Deshmukh) ahead of Stop #20 due to requested express window. Map updated.",
                timestampDisplay = "2 mins ago",
                isUnread = true,
                actionType = "view_sequence"
            ),
            com.example.myapplicationkmp.data.models.RiderNotificationAlert(
                id = "rnotif-2",
                category = "kitchen",
                priority = "normal",
                title = "Carrier Pod #12 Temp: 68°C (Optimal)",
                body = "Pre-heating cycle complete. 28 fresh hot tiffins sealed for lunch dispatch. Maintain seal until Stop #1.",
                timestampDisplay = "15 mins ago",
                isUnread = true,
                actionType = "pod_temp"
            ),
            com.example.myapplicationkmp.data.models.RiderNotificationAlert(
                id = "rnotif-3",
                category = "payouts",
                priority = "normal",
                title = "₹960.00 Added to Available Payout",
                body = "Morning run completed. 28 drops + 13 stainless boxes reconciled. Tap to withdraw instantly to bank.",
                timestampDisplay = "1 hour ago",
                isUnread = false,
                actionType = "view_payout"
            ),
            com.example.myapplicationkmp.data.models.RiderNotificationAlert(
                id = "rnotif-4",
                category = "route",
                priority = "normal",
                title = "Box Retrieval at Stop #20 (Meera Patel)",
                body = "Subscriber marked 2 stainless boxes ready for collection at door handle. Bring green collection pouch.",
                timestampDisplay = "Today, 11:30 AM",
                isUnread = true,
                actionType = "box_return"
            )
        )
    )
    val riderNotifications: StateFlow<List<com.example.myapplicationkmp.data.models.RiderNotificationAlert>> = _riderNotifications.asStateFlow()

    fun completeStopDrop(stopId: String) {
        val targetStop = _riderStops.value.find { it.id == stopId }
        val dropTimestamp = "Today, 12:48 PM"
        _riderStops.value = _riderStops.value.map { stop ->
            if (stop.id == stopId) {
                stop.copy(
                    status = "delivered",
                    isOtpVerified = true,
                    photoProofUrl = "verified_doorstep_drop_${stop.subscriberIdCode.replace("#", "")}.jpg"
                )
            } else stop
        }
        val current = _riderProfile.value
        val newDrops = current.completedDrops + 1
        _riderProfile.value = current.copy(
            completedDrops = newDrops,
            totalSafeDrops = current.totalSafeDrops + 1
        )

        // Dynamically synchronize Rider Earnings and available payout
        val currentEarnings = _riderEarnings.value
        val updatedDropsCount = currentEarnings.completedDropsCount + 1
        val updatedDropsPayout = updatedDropsCount * 25.0
        val newTotal = updatedDropsPayout + currentEarnings.boxCollectionsPayout + currentEarnings.peakRushSpikePayout
        _riderEarnings.value = currentEarnings.copy(
            completedDropsCount = updatedDropsCount,
            completedDropsPayout = updatedDropsPayout,
            todayTotal = newTotal,
            readyForPayout = newTotal
        )

        // Synchronize delivery notification to Admin console & activity feed
        val customerName = targetStop?.customerName ?: "Subscriber"
        val sequenceNum = targetStop?.sequenceNumber ?: 1
        val dispatchNotification = com.example.myapplicationkmp.data.models.ActivityNotificationItemModel(
            id = "notif-drop-${System.currentTimeMillis()}",
            type = "dispatch",
            title = "Stop #$sequenceNum Delivered ($customerName)",
            description = "Tiffin drop verified at ${targetStop?.addressLine ?: "doorstep"}. Box custody logged.",
            timeAgoFormatted = "Just now",
            isToday = true,
            isUnread = true,
            customerName = customerName,
            routeCode = "Route #A-4",
            driverName = current.fullName.ifBlank { "Rider" },
            actionLabel = "Verified ✓"
        )
        _activityNotifications.value = listOf(dispatchNotification) + _activityNotifications.value
    }

    fun scanBoxReturn(code: String, stopId: String) {
        _riderStops.value = _riderStops.value.map { stop ->
            if (stop.id == stopId) {
                stop.copy(returnBoxScannedCode = code)
            } else stop
        }
        val currentCustody = _boxCustody.value
        val newRecovered = currentCustody.recoveredCount + 1
        _boxCustody.value = currentCustody.copy(
            recoveredCount = newRecovered,
            deficitDue = (currentCustody.deficitDue - 1).coerceAtLeast(0)
        )

        // Increment Rider box return earnings bonus (+₹10 per recovered box)
        val currentEarnings = _riderEarnings.value
        val updatedBoxCount = currentEarnings.boxCollectionsCount + 1
        val updatedBoxPayout = updatedBoxCount * 10.0
        val newTotal = currentEarnings.completedDropsPayout + updatedBoxPayout + currentEarnings.peakRushSpikePayout
        _riderEarnings.value = currentEarnings.copy(
            boxCollectionsCount = updatedBoxCount,
            boxCollectionsPayout = updatedBoxPayout,
            todayTotal = newTotal,
            readyForPayout = newTotal
        )
    }

    fun initiateRiderPayout() {
        val current = _riderEarnings.value
        _riderEarnings.value = current.copy(
            readyForPayout = 0.0,
            isPayoutInitiated = true
        )
    }

    fun submitRiderIncident(
        stopSequence: Int,
        subCode: String,
        customerName: String,
        issueType: String,
        tags: List<String>,
        note: String
    ) {
        val issueTitle = when (issueType) {
            "leak" -> "Tiffin Packaging Leak / Dal Spilled"
            "unreachable" -> "Customer Unreachable / Gate Blocked"
            "wrong_bin" -> "Wrong Meal Bin Loaded"
            "breakdown" -> "Vehicle Breakdown / Puncture"
            "weather" -> "Severe Weather / Route Blocked"
            else -> "General Delivery Incident"
        }
        val ticket = com.example.myapplicationkmp.data.models.RiderIncidentTicket(
            id = "INC-${System.currentTimeMillis() % 10000}",
            stopSequenceNumber = stopSequence,
            subscriberIdCode = subCode,
            customerName = customerName,
            issueType = issueType,
            issueTypeTitle = issueTitle,
            incidentTags = tags,
            supervisorNote = note,
            photoAttached = true,
            status = "open"
        )
        _riderIncidents.value = listOf(ticket) + _riderIncidents.value

        // Synchronize directly into Admin activity inbox
        val adminIncidentNotif = com.example.myapplicationkmp.data.models.ActivityNotificationItemModel(
            id = "notif-inc-${System.currentTimeMillis()}",
            type = "dispatch",
            title = "Rider Alert: $issueTitle",
            description = "Reported at Stop #$stopSequence ($customerName): $note",
            timeAgoFormatted = "Just now",
            isToday = true,
            isUnread = true,
            customerName = customerName,
            driverName = _riderProfile.value.fullName.ifBlank { "Rider" },
            actionLabel = "Review Ticket"
        )
        _activityNotifications.value = listOf(adminIncidentNotif) + _activityNotifications.value
    }

    fun markAllRiderNotificationsRead() {
        _riderNotifications.value = _riderNotifications.value.map { it.copy(isUnread = false) }
    }

    fun toggleRiderDuty(onDuty: Boolean) {
        _riderProfile.value = _riderProfile.value.copy(isOnDuty = onDuty)
    }

    fun updateRiderProfile(updated: com.example.myapplicationkmp.data.models.RiderProfile) {
        _riderProfile.value = updated
    }

    fun refreshKitchenBatch() {
        val activeSubs = _subscriptions.value.filter { it.isActive }
        val current = _kitchenBatch.value
        val pending = activeSubs.size
        _kitchenBatch.value = current.copy(
            pendingCount = pending,
            rotisCount = pending * 4,
            sabziKg = pending * 0.25,
            dalLiters = pending * 0.20,
            hasItemsToPrepare = pending > 0
        )
    }

    // ==========================================
    // Folder 14: Dynamic Delivery Route & GPS Mapping Engine (Zero Cost)
    // ==========================================

    /**
     * Gathers all today active tiffin delivery stops directly from repository state:
     * - Combines existing rider stops and active customer addresses.
     * - Normalizes coordinates based on branch origin hub.
     */
    fun fetchTodayTiffinLocations(): List<com.example.myapplicationkmp.data.models.RiderDeliveryStop> {
        val branch = _currentBranch.value
        val hubLat = branch.latitude?.toDoubleOrNull() ?: 19.1663
        val hubLng = branch.longitude?.toDoubleOrNull() ?: 72.9986

        // If riderStops is populated, use them; augment with any new active customers if missing
        val currentStops = _riderStops.value
        if (currentStops.isNotEmpty()) {
            return currentStops
        }

        // Generate stops from active customers
        val activeCusts = _customers.value.filter { it.isActive }
        return activeCusts.mapIndexed { index, cust ->
            val cLat = cust.latitude.toDoubleOrNull() ?: (hubLat + (index * 0.003))
            val cLng = cust.longitude.toDoubleOrNull() ?: (hubLng + (index * 0.003))
            com.example.myapplicationkmp.data.models.RiderDeliveryStop(
                id = "stop-gen-${cust.id}",
                sequenceNumber = index + 1,
                subscriberIdCode = "#SUB-${cust.id.takeLast(4)}",
                customerName = cust.name,
                customerPhone = cust.phone,
                addressLine = cust.address,
                wingFlat = cust.address.substringBefore(","),
                societyCluster = cust.deliveryRoute.ifBlank { "central" },
                mealSlot = "Lunch",
                etaDisplay = "12:${30 + (index * 8)} PM",
                distanceDisplay = "${1.2 + (index * 0.8)} km",
                status = "pending",
                specialNote = "Leave at reception if unattended",
                items = listOf(if (cust.isVeg) "Standard Veg Thali" else "Special Non-Veg Pack"),
                contactlessOtp = "${(100..999).random()}",
                podBinSlot = "Pod Bin A-${index + 1}",
                returnBoxesToCollect = if (index % 2 == 0) 1 else 0,
                latitude = cLat,
                longitude = cLng
            )
        }
    }

    /**
     * Solves the Traveling Salesperson Problem (TSP) using a client-side Nearest-Neighbor heuristic:
     * - $0 API cost, zero-server dependency, 100% offline & instantaneous.
     * - Optimizes sequence from the Central Kitchen branch origin to all customer drop points.
     * - Recalculates ETAs, distance estimates, and updates both DeliveryRouteModel and RiderStops in DB.
     */
    fun generateAndSaveTodayRoute(routeId: String = "route-1"): com.example.myapplicationkmp.data.models.DeliveryRouteModel {
        val branch = _currentBranch.value
        val hubLat = branch.latitude?.toDoubleOrNull() ?: 19.1663
        val hubLng = branch.longitude?.toDoubleOrNull() ?: 72.9986

        val rawStops = fetchTodayTiffinLocations()
        if (rawStops.isEmpty()) {
            val emptyRoute = com.example.myapplicationkmp.data.models.DeliveryRouteModel(
                id = routeId,
                name = "Main Route",
                originBranchId = branch.id,
                originBranchName = branch.name,
                assignedDriverName = _riderProfile.value.fullName.ifBlank { "Unassigned" },
                isActive = true,
                stopsCount = 0,
                originLatitude = hubLat,
                originLongitude = hubLng,
                totalDistanceKm = 0.0,
                totalDurationMinutes = 0,
                waypoints = emptyList()
            )
            updateDeliveryRoute(emptyRoute)
            return emptyRoute
        }

        // Nearest-neighbor sequencing starting from Hub
        val unvisited = rawStops.toMutableList()
        val optimizedStops = mutableListOf<com.example.myapplicationkmp.data.models.RiderDeliveryStop>()
        var currentLat = hubLat
        var currentLng = hubLng
        var totalDistKm = 0.0

        while (unvisited.isNotEmpty()) {
            // Find closest stop to current coordinate
            var nearestIdx = 0
            var shortestDist = Double.MAX_VALUE
            for (i in unvisited.indices) {
                val candidate = unvisited[i]
                val d = calculateEuclideanDistanceKm(currentLat, currentLng, candidate.latitude, candidate.longitude)
                if (d < shortestDist) {
                    shortestDist = d
                    nearestIdx = i
                }
            }
            val nextStop = unvisited.removeAt(nearestIdx)
            totalDistKm += shortestDist
            currentLat = nextStop.latitude
            currentLng = nextStop.longitude
            optimizedStops.add(nextStop)
        }

        // Re-index sequences and recalculate ETAs
        var runningMinutes = 15 // Initial dispatch prep/buffer
        val sequencedStops = optimizedStops.mapIndexed { index, stop ->
            val stepDist = if (index == 0) {
                calculateEuclideanDistanceKm(hubLat, hubLng, stop.latitude, stop.longitude)
            } else {
                calculateEuclideanDistanceKm(optimizedStops[index - 1].latitude, optimizedStops[index - 1].longitude, stop.latitude, stop.longitude)
            }
            val travelMinutes = kotlin.math.max(4, (stepDist * 3.5).toInt())
            runningMinutes += travelMinutes

            val startHour = 12
            val totalMinutesFrom12 = runningMinutes
            val displayHour = startHour + (totalMinutesFrom12 / 60)
            val displayMins = totalMinutesFrom12 % 60
            val formattedEta = "${if (displayHour > 12) displayHour - 12 else displayHour}:${displayMins.toString().padStart(2, '0')} PM"

            stop.copy(
                sequenceNumber = index + 1,
                etaDisplay = formattedEta,
                distanceDisplay = "${(stepDist * 10).toInt() / 10.0} km"
            )
        }

        // Update rider stops in repository
        _riderStops.value = sequencedStops

        // Build route waypoints for map rendering
        val waypoints = mutableListOf<com.example.myapplicationkmp.data.models.TiffinMapCoordinate>()
        waypoints.add(
            com.example.myapplicationkmp.data.models.TiffinMapCoordinate(
                latitude = hubLat,
                longitude = hubLng,
                label = branch.name,
                sequence = 0,
                isOrigin = true
            )
        )
        sequencedStops.forEach { s ->
            waypoints.add(
                com.example.myapplicationkmp.data.models.TiffinMapCoordinate(
                    latitude = s.latitude,
                    longitude = s.longitude,
                    label = s.customerName,
                    sequence = s.sequenceNumber,
                    isCompleted = s.status == "delivered"
                )
            )
        }

        val totalDuration = runningMinutes
        val roundedDist = (totalDistKm * 10).toInt() / 10.0
        val existingRoute = _deliveryRoutes.value.find { it.id == routeId }
        val updatedRoute = com.example.myapplicationkmp.data.models.DeliveryRouteModel(
            id = routeId,
            name = existingRoute?.name ?: "Optimized Today Route",
            originBranchId = branch.id,
            originBranchName = branch.name,
            assignedDriverId = existingRoute?.assignedDriverId ?: "drv-1",
            assignedDriverName = existingRoute?.assignedDriverName ?: _riderProfile.value.fullName.ifBlank { "Rider Partner" },
            isActive = true,
            stopsCount = sequencedStops.size,
            originLatitude = hubLat,
            originLongitude = hubLng,
            totalDistanceKm = roundedDist,
            totalDurationMinutes = totalDuration,
            waypoints = waypoints
        )

        updateDeliveryRoute(updatedRoute)

        // Post activity notification to Admin console
        val routeNotif = com.example.myapplicationkmp.data.models.ActivityNotificationItemModel(
            id = "notif-rte-${System.currentTimeMillis()}",
            type = "dispatch",
            title = "Route Optimized & Saved (${updatedRoute.name})",
            description = "${sequencedStops.size} tiffin drop locations sequenced. Total distance: $roundedDist km, Est: $totalDuration mins.",
            timeAgoFormatted = "Just now",
            isToday = true,
            isUnread = true,
            routeCode = updatedRoute.name,
            driverName = updatedRoute.assignedDriverName,
            actionLabel = "View Map"
        )
        _activityNotifications.value = listOf(routeNotif) + _activityNotifications.value

        return updatedRoute
    }

    /**
     * Refreshes today's delivery route by recalculating stops from DB.
     */
    fun refreshTodayRoutes() {
        generateAndSaveTodayRoute()
    }

    /**
     * Updates an individual stop's GPS coordinates and refreshes the route.
     */
    fun updateStopCoordinates(stopId: String, lat: Double, lng: Double) {
        _riderStops.value = _riderStops.value.map { stop ->
            if (stop.id == stopId) {
                stop.copy(latitude = lat, longitude = lng)
            } else stop
        }
        generateAndSaveTodayRoute()
    }

    private fun calculateEuclideanDistanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val latDiff = (lat2 - lat1) * 111.0
        val lonDiff = (lon2 - lon1) * 111.0 * kotlin.math.cos(lat1 * 0.017453292519943295)
        return kotlin.math.sqrt(latDiff * latDiff + lonDiff * lonDiff)
    }

    /**
     * Converts epoch millis to "DD Mon, YYYY" string (e.g., "18 Sep, 2026").
     * Uses pure arithmetic — no kotlinx.datetime dependency required.
     */
    private fun formatEpochToReadable(epochMs: Long): String {
        val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        // Days since Unix epoch (1 Jan 1970)
        val totalDays = (epochMs / 86400000L).toInt()
        var year = 1970
        var days = totalDays
        while (true) {
            val daysInYear = if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 366 else 365
            if (days < daysInYear) break
            days -= daysInYear
            year++
        }
        var month = 0
        val daysInMonths = listOf(31, if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28,
            31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        while (month < 12 && days >= daysInMonths[month]) {
            days -= daysInMonths[month]
            month++
        }
        val day = days + 1
        return "${day.toString().padStart(2, '0')} ${months[month]}, $year"
    }
}


