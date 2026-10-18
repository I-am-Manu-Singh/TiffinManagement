package com.creativeblisslabs.tiffinmanagement.data.models

data class BranchModel(
    val id: String,
    val name: String,
    val address: String? = null,
    val city: String? = null,
    val phone: String = "",
    val fssaiLicenseNumber: String = "",
    val latitude: String? = null,
    val longitude: String? = null,
    val isActive: Boolean = true,
    val coverageRadiusKm: Double = 10.0
)

data class MealCategoryModel(
    val id: String,
    val name: String, // Breakfast, Lunch, Dinner
    val startTime: String, // e.g. "07:00 AM"
    val endTime: String, // e.g. "09:30 AM"
    val description: String? = null,
    val isActive: Boolean,
    val displayOrder: Int
)

data class DayWiseMenuItemModel(
    val id: String,
    val dayOfWeek: String, // Sunday, Monday, etc.
    val itemName: String,
    val portionCount: String, // "4 units", "1 bowl", etc.
    val isVeg: Boolean = true
)

data class MealPlanModel(
    val id: String,
    val name: String, // e.g. "Standard Thali Plan" / "packages sql file"
    val categoryName: String, // Lunch, Dinner, etc.
    val isVeg: Boolean,
    val isJain: Boolean,
    val dailyRate: Double,
    val weeklyRate: Double,
    val monthlyRate: Double,
    val maxOrderIncrement: Int,
    val description: String ,
    val isFeatured: Boolean,
    val isActive: Boolean,
    val deliveryDays: List<String> = listOf("S", "M", "T", "W", "T", "F", "S"),
    val compositionItemsCount: Int,
    val dayWiseItems: List<DayWiseMenuItemModel> = listOf(
        DayWiseMenuItemModel("item-1", "Monday", "Chapati / Roti", "4 units", true),
        DayWiseMenuItemModel("item-2", "Monday", "Paneer Butter Masala", "1 bowl", true),
        DayWiseMenuItemModel("item-3", "Monday", "Dal Tadka & Steamed Rice", "1 pack", true)
    )
)

data class TiffinMapCoordinate(
    val latitude: Double,
    val longitude: Double,
    val label: String = "",
    val sequence: Int = 0,
    val isOrigin: Boolean = false,
    val isCompleted: Boolean = false
)

data class DeliveryRouteModel(
    val id: String,
    val name: String, // e.g. "Main Route"
    val originBranchId: String,
    val originBranchName: String,
    val assignedDriverId: String? = null,
    val assignedDriverName: String,
    val isActive: Boolean,
    val stopsCount: Int,
    val originLatitude: Double = 19.1663,
    val originLongitude: Double = 72.9986,
    val totalDistanceKm: Double = 0.0,
    val totalDurationMinutes: Int = 0,
    val waypoints: List<TiffinMapCoordinate> = emptyList()
)

data class CustomerModel(
    val id: String,
    val name: String,
    val phone: String,
    val email: String,
    val address: String,
    val branchName: String,
    val deliveryRoute: String,
    val latitude: String,
    val longitude: String,
    val isActive: Boolean,
    val isVeg: Boolean,
    val walletBalance: Double,
    val dueLimit: Double
)

data class VacationHoldModel(
    val id: String,
    val startDate: String,
    val endDate: String,
    val mealPlanName: String? = null,
    val reason: String? = null
)

data class WalletLedgerItemModel(
    val id: String,
    val dateFormatted: String,
    val description: String,
    val amount: Double,
    val isCredit: Boolean,
    val balanceAfter: Double
)

data class SubscriptionModel(
    val id: String,
    val subscriptionCode: String,
    val customerId: String = "",           // FK to CustomerModel.id
    val customerName: String,
    val packageName: String,
    val rate: Double,
    val ratePeriod: String,
    val isVeg: Boolean,
    val startDate: String,
    val endDate: String,
    val walletBalance: Double,
    val allowedSkips: Int,
    val skipsUsed: Int,
    val paymentStatus: String,
    val isActive: Boolean,
    val activeDays: List<String>,
    val mealComponents: List<String>,
    val dailyRate: Double,
    val weeklyRate: Double,
    val monthlyRate: Double,
    val shippingType: String,
    val instructions: String,
    val note: String,
    val vacationHolds: List<VacationHoldModel>
) {
    /** Total amount for this subscription based on ratePeriod. */
    val totalAmount: Double get() = when (ratePeriod.lowercase()) {
        "daily"   -> dailyRate
        "weekly"  -> weeklyRate
        "monthly", "month" -> monthlyRate
        else      -> rate
    }
}

data class KitchenBatchModel(
    val dateFormatted: String,
    val activeSlot: String,
    val rotisCount: Int,
    val sabziKg: Double,
    val dalLiters: Double,
    val kdsStation: String,
    val shiftTime: String,
    val pendingCount: Int,
    val inPrepCount: Int,
    val dispatchedCount: Int,
    val hasItemsToPrepare: Boolean
)

data class FinancialOverviewModel(
    val activeSubscribersCount: Int,
    val monthlyRevenue: Double,
    val billingCycleMonth: String,
    val monthlyExpenses: Double,
    val expenseCategory: String,
    val monthlyOrdersCount: Int,
    val netProfit: Double,
    val recordedDipAmount: Double,
    val homeDeliveryCompleted: Int,
    val homeDeliveryTotal: Int,
    val storePickupCompleted: Int,
    val storePickupTotal: Int,
    val lowBalanceAlertsCount: Int
)

data class ExpenseModel(
    val id: String,
    val dateFormatted: String,
    val category: String,
    val amount: Double,
    val tax: Double,
    val paymentMode: String,
    val description: String,
    val receiptUrl: String? = null,
    val receiptName: String? = null,
    val receiptSizeFormatted: String? = null
)

data class RecurringExpenseRuleModel(
    val id: String,
    val title: String,
    val category: String,
    val amount: Double,
    val tax: Double = 0.0,
    val frequency: String, // Daily, Weekly, Monthly
    val dayOfMonth: Int? = null,
    val dayOfWeek: String? = null,
    val nextDueDate: String,
    val lastGeneratedDate: String? = null,
    val endDate: String? = null,
    val isActive: Boolean = true
)

data class KitchenHoldModel(
    val id: String,
    val branchId: String? = null,
    val branchName: String,
    val category: String? = null, // null = all categories; "breakfast", "lunch", "dinner", etc.
    val startDate: String,
    val endDate: String,
    val message: String,
    val isActive: Boolean
)

data class StaffMemberModel(
    val id: String,
    val fullName: String,
    val phone: String,
    val email: String? = null,
    val role: String, // Cook, Packing, Driver, Manager
    val branchName: String,
    val isActive: Boolean = true,
    val initials: String = fullName.split(" ").mapNotNull { it.firstOrNull()?.toString() }.take(2).joinToString("")
)

data class NotificationSettingModel(
    val id: String,
    val key: String,
    val category: String, // customer, vendor
    val title: String,
    val recipientLabel: String,
    val channels: String,
    val detailText: String,
    val sampleMessage: String? = null,
    val isEnabled: Boolean = true
)

data class UserProfileModel(
    val id: String,
    val fullName: String ,
    val email: String ,
    val phone: String ,
    val secondaryPhone: String? = null,
    val roleTitle: String,
    val displayJobTitle: String,
    val branchName: String ,
    val businessGstin: String ,
    val avatarUrl: String? = null,
    val operatingSince: String ,
    val activeSubscribersCount: Int = 0,
    val totalBranchesCount: Int = 1,
    val isKycVerified: Boolean = false
)

data class ActivityNotificationItemModel(
    val id: String,
    val type: String, // 'dispatch', 'hold', 'wallet', 'payment', 'cooking'
    val title: String,
    val description: String,
    val timeAgoFormatted: String,
    val isToday: Boolean = true,
    val isUnread: Boolean = false,
    val routeCode: String? = null,
    val driverName: String? = null,
    val amountFormatted: String? = null,
    val txnCode: String? = null,
    val chefName: String? = null,
    val customerName: String? = null,
    val holdDates: String? = null,
    val actionLabel: String? = null
)

data class AppSystemSettingsModel(
    val id: String,
    val branchId: String,
    val autoLockDailyOrders: Boolean,
    val dailyCutoffTime: String,
    val defaultDietaryMode: String,
    val kdsAutoRefresh: Boolean,
    val kdsRefreshSeconds: Int,
    val printStickerLabels: Boolean,
    val thermalPrinterModel: String,
    val isPrinterConnected: Boolean = true,
    val isPortionScaleConfigured: Boolean = false,
    val currencySymbol: String = "₹",
    val currencyName: String,
    val gstRatePercent: Double,
    val customerTrustLimit: Double,
    val thermalReceiptFooterText: String,
    val soundChimesAndAlerts: Boolean,
    val defaultBranchName: String,
    val operationalTimezone: String,
    val appLanguage: String
)

data class KitchenOnboardingStateModel(
    val brandName: String = "",
    val breakfastSlotEnabled: Boolean = true,
    val lunchSlotEnabled: Boolean = true,
    val dinnerSlotEnabled: Boolean = true,
    val dietarySpecialization: String = "",
    val operationalCurrency: String = "INR (₹)",
    val debitTrustLimit: Double = 0.0,
    val isOnboardingComplete: Boolean = true
)

data class UserAuthModel(
    val id: String,
    val email: String,
    val fullName: String,
    val businessName: String? = null,
    val phone: String? = null,
    val role: String,
    val provider: String, // "email", "google"
    val avatarUrl: String? = null,
    val isAuthenticated: Boolean = true
)

data class AuthSessionState(
    val currentUser: UserAuthModel? = null,
    val pendingOtpEmail: String? = null,
    val isOtpVerified: Boolean = false,
    val otpFlowType: String = "forgot_password", // "forgot_password", "registration"
    val errorMessage: String? = null,
    val successMessage: String? = null
)
data class SupportInquiry(
    val id: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val businessName: String,
    val topic: String,
    val message: String,
    val status: String = "", // 'open', 'resolved'
    val createdAt: String = ""
)

data class KnowledgeTopic(
    val id: String,
    val title: String,
    val subtitle: String,
    val articleCount: Int,
    val iconName: String,
    val categorySlug: String
)

data class FaqArticle(
    val id: String,
    val title: String,
    val category: String,
    val previewText: String,
    val detailedContent: String,
    val steps: List<String> = emptyList(),
    val updatedAgo: String,
    val linkUrl: String? = null
)

data class LegalDocument(
    val id: String,
    val slug: String,
    val title: String,
    val version: String,
    val effectiveDate: String,
    val summary: String,
    val readTimeMinutes: Int,
    val badgeLabel: String,
    val category: String, // 'merchant', 'security', 'billing', 'all'
    val sectionsCount: Int
)

// ==========================================
// Folder 11: Customer / Subscriber Portal Models
// ==========================================
data class SubscriberProfile(
    val id: String = "",
    val subscriberNumber: String = "",
    val fullName: String = "",
    val phone: String = "",
    val email: String = "",
    val emergencyContact: String = "",
    val planTitle: String = "",
    val memberSince: String = "",
    val scheduleSummary: String = "",
    val locationType: String = "", // 'apartment', 'hostel', 'office', 'other'
    val flatDetails: String = "",
    val landmark: String = "",
    val cityPincode: String = "",
    val gpsCoordinates: String = "",
    val gpsAccuracy: String = "",
    val driverDropNote: String = "",
    val hasSundayAddress: Boolean = false,
    val sundayAddressTitle: String = "",
    val sundayAddressDetails: String = "",
    val sundayRoute: String = "",
    val lunchSlotWindow: String = "",
    val dinnerSlotWindow: String = "",
    val walletBalance: Double = 0.0,
    val overdraftBuffer: Double = 0.0,
    val notifyMorningMenu: Boolean = true,
    val notifyDriverDispatch: Boolean = true,
    val notifyDropPhoto: Boolean = true,
    val notifyLowBalance: Boolean = true,
    val deliveryOtp: String = ""
)

data class CustomerGuestOrder(
    val id: String,
    val sessionTitle: String,
    val sessionDate: String,
    val mealTitle: String,
    val quantity: Int,
    val unitPrice: Double,
    val totalAmount: Double,
    val packagingType: String, // 'tiffin', 'disposable'
    val packagingSurcharge: Double,
    val customNotes: String,
    val status: String
)

data class CustomerVacationHold(
    val id: String,
    val startDateFormatted: String,
    val endDateFormatted: String,
    val pauseType: String, // 'both', 'lunch_only', 'dinner_only'
    val daysCount: Int,
    val refundCreditAmount: Double,
    val status: String
)

data class SubscriberWalletTransaction(
    val id: String,
    val title: String,
    val timestamp: String,
    val subtext: String,
    val amount: Double,
    val isCredit: Boolean,
    val tag: String
)

// ==========================================
// Folder 12: Customer Portal Extended Models
// ==========================================
data class MealRatingFeedback(
    val id: String = "",
    val mealTitle: String = "",
    val mealDate: String = "",
    val overallRating: Int = 0,
    val sentimentLabel: String = "",
    val dishRatings: Map<String, String> = emptyMap(),
    val portionCheck: String = "",
    val foodTemperature: String = "",
    val packagingHygieneScore: Int = 0,
    val chefNote: String = ""
)

data class MealIssueTicket(
    val id: String,
    val targetDeliveryTitle: String,
    val deliveryTime: String,
    val category: String,
    val issueTags: List<String>,
    val description: String,
    val status: String,
    val refundAmount: Double,
    val timestamp: String
)

data class PlanUpgradeOption(
    val id: String,
    val title: String,
    val subtitle: String,
    val priceMonthly: Double,
    val priceDifference: Double, // positive for upgrade, negative for savings
    val differenceLabel: String,
    val features: List<String>,
    val containerNote: String,
    val isPopular: Boolean,
    val isVeg: Boolean = true
)

data class SubscriberNotification(
    val id: String,
    val title: String,
    val timestamp: String,
    val body: String,
    val category: String, // 'delivery', 'wallet', 'announcement'
    val isUrgent: Boolean,
    val isUnread: Boolean,
    val actionPrimaryTitle: String?,
    val actionSecondaryTitle: String?
)

// ==========================================
// Folder 13: Tiffin Rider / Driver Models
// ==========================================

data class RiderProfile(
    val id: String = "",
    val driverIdCode: String = "",
    val fullName: String = "",
    val phone: String? = "",
    val vehicleModel: String = "",
    val vehiclePlate: String = "",
    val rating: Double = 0.0,
    val totalSafeDrops: Int = 0,
    val onTimeRatePercent: Double = 0.0,
    val boxReturnRatePercent: Double = 0.0,
    val tier: String = "",
    val baseDropRate: Double = 0.0,
    val boxRecoveryBonus: Double = 0.0,
    val upiId: String = "",
    val bankAccountTail: String = "",
    val hubName: String = "",
    val hubManagerName: String = "",
    val hubManagerPhone: String = "",
    val isOnDuty: Boolean = false,
    val currentShiftName: String = "",
    val carrierPodId: String = "",
    val carrierPodTempC: Int = 0,
    val shiftWindow: String = "",
    val completedDrops: Int = 0,
    val totalDrops: Int = 0,
    val enRouteDrops: Int = 0,
    val returnBoxesInPod: Int = 0,
    val returnBoxesDue: Int = 0,
    val isLunchShiftActive: Boolean = true,
    val isDinnerShiftActive: Boolean = false,
    val isAlertChimeEnabled: Boolean = true,
    val defaultNavEngine: String = ""
)

data class RiderDeliveryStop(
    val id: String = "",
    val sequenceNumber: Int = 1,
    val subscriberIdCode: String = "",
    val customerName: String = "",
    val customerPhone: String = "",
    val addressLine: String = "",
    val wingFlat: String = "",
    val societyCluster: String = "",
    val floorNote: String = "",
    val mealSlot: String = "",
    val etaDisplay: String = "",
    val distanceDisplay: String = "",
    val status: String = "", // 'pending', 'en_route', 'delivered', 'skipped', 'hold'
    val specialNote: String = "",
    val items: List<String> = emptyList(),
    val contactlessOtp: String = "",
    val isOtpVerified: Boolean = false,
    val podBinSlot: String = "",
    val photoProofUrl: String? = null,
    val returnBoxesToCollect: Int = 0,
    val returnBoxScannedCode: String? = null,
    val latitude: Double = 19.1663,
    val longitude: Double = 72.9986
)

data class RiderDeficitDue(
    val customerName: String = "",
    val flatAddress: String = "",
    val daysDue: Int = 0,
    val setCount: Int = 0,
    val depositHeld: Double = 0.0,
    val phone: String = ""
)

data class RiderBoxCustodyState(
    val shiftCode: String = "LUNCH-S1",
    val totalLoaded: Int = 0,
    val currentInPod: Int = 0,
    val deficitDue: Int = 0,
    val recoveredCount: Int = 0,
    val undeliveredCount: Int = 0,
    val freeSlots: Int = 0,
    val totalCapacity: Int = 0,
    val deficitsList: List<RiderDeficitDue> = listOf(
        RiderDeficitDue("Amitabh Khanna", "A-402, Sunshine Residency", 3, 2, 700.0, "+919876543211"),
        RiderDeficitDue("Pooja Verma", "Flat B-110, Hiranandani", 2, 1, 350.0, "+919876543212"),
        RiderDeficitDue("Rohit Sharma", "G-03, Green Glen", 1, 1, 350.0, "+919876543213")
    )
)

data class RiderEarningsSummary(
    val readyForPayout: Double = 0.0,
    val todayTotal: Double = 0.0,
    val completedDropsCount: Int = 0,
    val completedDropsPayout: Double = 0.0,
    val boxCollectionsCount: Int = 0,
    val boxCollectionsPayout: Double = 0.0,
    val peakRushSpikePayout: Double = 0.0,
    val onTimeScorePercent: Double = 0.0,
    val distanceCoveredKm: Double = 0.0,
    val upiId: String = "",
    val isPayoutInitiated: Boolean = false
)

data class RiderIncidentTicket(
    val id: String = "",
    val stopSequenceNumber: Int = 0,
    val subscriberIdCode: String = "",
    val customerName: String = "",
    val issueType: String = "", // 'leak', 'unreachable', 'wrong_bin', 'breakdown', 'weather'
    val issueTypeTitle: String = "",
    val incidentTags: List<String> = emptyList(),
    val supervisorNote: String = "",
    val photoAttached: Boolean = true,
    val timestamp: String = "",
    val status: String = ""
)

data class RiderNotificationAlert(
    val id: String,
    val category: String, // 'route', 'kitchen', 'payouts'
    val priority: String, // 'normal', 'urgent'
    val title: String,
    val body: String,
    val timestampDisplay: String,
    val isUnread: Boolean,
    val actionType: String? = null
)



