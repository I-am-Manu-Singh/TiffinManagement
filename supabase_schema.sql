-- ==============================================================================
-- TIFFIN MANAGER SUPABASE SCHEMA & IDEMPOTENT POLICIES
-- Safe to run multiple times without 42710 (policy already exists) errors
-- ==============================================================================

-- Enable UUID and pgcrypto extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- 1. PROFILES (Users: Kitchen Owners, Staff, Delivery Partners, Customers)
CREATE TABLE IF NOT EXISTS public.profiles (
    id UUID PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
    email TEXT UNIQUE NOT NULL,
    full_name TEXT NOT NULL,
    phone TEXT,
    avatar_url TEXT,
    role TEXT NOT NULL DEFAULT 'owner' CHECK (role IN ('owner', 'kitchen_staff', 'driver', 'rider', 'customer')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

-- 2. KITCHEN BRANCHES
CREATE TABLE IF NOT EXISTS public.branches (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    owner_id UUID NOT NULL REFERENCES public.profiles(id) ON DELETE CASCADE,
    name TEXT NOT NULL DEFAULT '',
    code TEXT UNIQUE,
    address TEXT,
    city TEXT,
    state TEXT,
    pincode TEXT,
    phone TEXT,
    latitude NUMERIC(10, 7),
    longitude NUMERIC(10, 7),
    coverage_radius_km NUMERIC(5, 2) DEFAULT 0.0,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

-- 3. CUSTOMERS
CREATE TABLE IF NOT EXISTS public.customers (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    branch_id UUID NOT NULL REFERENCES public.branches(id) ON DELETE CASCADE,
    full_name TEXT NOT NULL,
    phone TEXT NOT NULL,
    email TEXT,
    delivery_address TEXT NOT NULL,
    delivery_route TEXT NOT NULL DEFAULT '',
    latitude NUMERIC(10, 7),
    longitude NUMERIC(10, 7),
    is_active BOOLEAN NOT NULL DEFAULT true,
    is_veg BOOLEAN NOT NULL DEFAULT true,
    wallet_balance NUMERIC(10, 2) NOT NULL DEFAULT 0.0 CHECK (wallet_balance >= -10000.0),
    max_due_limit NUMERIC(10, 2) NOT NULL DEFAULT 0.0 CHECK (max_due_limit >= 0.0),
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

-- 4. SUBSCRIPTION PLANS & PACKAGES
CREATE TABLE IF NOT EXISTS public.subscription_plans (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    branch_id UUID NOT NULL REFERENCES public.branches(id) ON DELETE CASCADE,
    name TEXT NOT NULL,
    plan_type TEXT NOT NULL DEFAULT '' CHECK (plan_type IN ('daily', 'weekly', 'bi-weekly', 'monthly', 'trial')),
    meal_type TEXT NOT NULL DEFAULT '' CHECK (meal_type IN ('breakfast', 'lunch', 'dinner', 'combo')),
    price NUMERIC(10, 2) NOT NULL,
    description TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

-- 5. CUSTOMER ACTIVE SUBSCRIPTIONS
CREATE TABLE IF NOT EXISTS public.customer_subscriptions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    subscription_code TEXT UNIQUE NOT NULL, -- e.g. SUB-2026-094
    customer_id UUID NOT NULL REFERENCES public.customers(id) ON DELETE CASCADE,
    plan_id UUID NOT NULL REFERENCES public.subscription_plans(id) ON DELETE RESTRICT,
    package_name TEXT NOT NULL,
    rate NUMERIC(10, 2) NOT NULL,
    daily_rate NUMERIC(10, 2) NOT NULL DEFAULT 0.0,
    weekly_rate NUMERIC(10, 2) NOT NULL DEFAULT 0.0,
    monthly_rate NUMERIC(10, 2) NOT NULL DEFAULT 0.0,
    rate_period TEXT NOT NULL DEFAULT '',
    dietary_type TEXT NOT NULL DEFAULT '',
    shipping_type TEXT NOT NULL DEFAULT '',
    instructions TEXT,
    note TEXT,
    start_date DATE NOT NULL,
    end_date DATE,
    allowed_skips INT NOT NULL DEFAULT 0,
    skips_used INT NOT NULL DEFAULT 0,
    active_days JSONB NOT NULL DEFAULT '["Su","M","T","W","T","F","S"]'::jsonb,
    meal_components JSONB NOT NULL DEFAULT '[]'::jsonb,
    payment_status TEXT NOT NULL DEFAULT '' CHECK (payment_status IN ('paid', 'pending', 'overdue', 'partially_paid')),
    status TEXT NOT NULL DEFAULT '' CHECK (status IN ('active', 'paused', 'expired', 'cancelled')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

-- 6. KITCHEN BATCHES & MEAL PREPARATION
CREATE TABLE IF NOT EXISTS public.kitchen_prep_batches (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    branch_id UUID NOT NULL REFERENCES public.branches(id) ON DELETE CASCADE,
    production_date DATE NOT NULL,
    meal_slot TEXT NOT NULL CHECK (meal_slot IN ('breakfast', 'lunch', 'dinner')),
    total_portions INT NOT NULL DEFAULT 0,
    rotis_breads_count INT NOT NULL DEFAULT 0,
    sabzi_mains_kg NUMERIC(6, 2) NOT NULL DEFAULT 0.00,
    dal_curries_liters NUMERIC(6, 2) NOT NULL DEFAULT 0.00,
    kds_station TEXT NOT NULL DEFAULT '',
    pending_count INT NOT NULL DEFAULT 0,
    in_prep_count INT NOT NULL DEFAULT 0,
    dispatched_count INT NOT NULL DEFAULT 0,
    status TEXT NOT NULL DEFAULT '' CHECK (status IN ('idle', 'in_progress', 'completed')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    UNIQUE(branch_id, production_date, meal_slot)
);

-- 7. DAILY DISPATCH & DELIVERIES
CREATE TABLE IF NOT EXISTS public.daily_deliveries (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    branch_id UUID NOT NULL REFERENCES public.branches(id) ON DELETE CASCADE,
    subscription_id UUID REFERENCES public.customer_subscriptions(id) ON DELETE SET NULL,
    customer_id UUID NOT NULL REFERENCES public.customers(id) ON DELETE CASCADE,
    delivery_date DATE NOT NULL,
    meal_slot TEXT NOT NULL CHECK (meal_slot IN ('breakfast', 'lunch', 'dinner')),
    delivery_type TEXT NOT NULL DEFAULT '' CHECK (delivery_type IN ('home_delivery', 'store_pickup')),
    status TEXT NOT NULL DEFAULT '' CHECK (status IN ('pending', 'in_transit', 'delivered', 'skipped', 'cancelled')),
    driver_id UUID REFERENCES public.profiles(id) ON DELETE SET NULL,
    delivery_sequence INT NOT NULL DEFAULT 0,
    delivery_address TEXT NOT NULL,
    delivered_at TIMESTAMPTZ,
    notes TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

-- 8. FINANCIAL WALLET LEDGER
CREATE TABLE IF NOT EXISTS public.wallet_ledger (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    customer_id UUID NOT NULL REFERENCES public.customers(id) ON DELETE CASCADE,
    branch_id UUID NOT NULL REFERENCES public.branches(id) ON DELETE CASCADE,
    transaction_type TEXT NOT NULL CHECK (transaction_type IN ('credit', 'debit')),
    amount NUMERIC(10, 2) NOT NULL,
    balance_after NUMERIC(10, 2) NOT NULL,
    reason TEXT NOT NULL,
    reference_order_id UUID,
    payment_mode TEXT CHECK (payment_mode IN ('upi', 'cash', 'card', 'net_banking', 'wallet')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

-- 9. MONTHLY FINANCIAL SNAPSHOTS
CREATE TABLE IF NOT EXISTS public.monthly_financial_records (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    branch_id UUID NOT NULL REFERENCES public.branches(id) ON DELETE CASCADE,
    month_year DATE NOT NULL,
    total_revenue NUMERIC(12, 2) NOT NULL DEFAULT 0.00,
    total_expenses NUMERIC(12, 2) NOT NULL DEFAULT 0.00,
    ingredients_fuel_expense NUMERIC(12, 2) NOT NULL DEFAULT 0.00,
    total_orders INT NOT NULL DEFAULT 0,
    net_profit NUMERIC(12, 2) GENERATED ALWAYS AS (total_revenue - total_expenses) STORED,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    UNIQUE(branch_id, month_year)
);

-- ==============================================================================
-- ROW LEVEL SECURITY (RLS) POLICIES (DROP IF EXISTS + CREATE)
-- ==============================================================================

ALTER TABLE public.profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.branches ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.customers ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.subscription_plans ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.customer_subscriptions ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.kitchen_prep_batches ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.daily_deliveries ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.wallet_ledger ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.monthly_financial_records ENABLE ROW LEVEL SECURITY;

-- Profiles
DROP POLICY IF EXISTS "Users can view and edit own profile" ON public.profiles;
CREATE POLICY "Users can view and edit own profile" ON public.profiles
    FOR ALL USING (auth.uid() = id);

DROP POLICY IF EXISTS "Public can create profiles" ON public.profiles;
CREATE POLICY "Public can create profiles" ON public.profiles
    FOR INSERT WITH CHECK (true);

-- Trigger: Automatically create or update public.profiles on new auth.users insertion (Google OAuth / Email Auth)
CREATE OR REPLACE FUNCTION public.handle_new_user()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO public.profiles (
        id,
        email,
        full_name,
        avatar_url,
        role,
        created_at,
        updated_at
    )
    VALUES (
        NEW.id,
        COALESCE(NEW.email, ''),
        COALESCE(NEW.raw_user_meta_data->>'full_name', NEW.raw_user_meta_data->>'name', split_part(COALESCE(NEW.email, 'User'), '@', 1)),
        NEW.raw_user_meta_data->>'avatar_url',
        COALESCE(NEW.raw_user_meta_data->>'role', 'customer'),
        timezone('utc'::text, now()),
        timezone('utc'::text, now())
    )
    ON CONFLICT (id) DO UPDATE SET
        email = EXCLUDED.email,
        full_name = CASE WHEN profiles.full_name IS NULL OR profiles.full_name = '' THEN EXCLUDED.full_name ELSE profiles.full_name END,
        avatar_url = COALESCE(EXCLUDED.avatar_url, profiles.avatar_url),
        updated_at = timezone('utc'::text, now());

    RETURN NEW;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

DROP TRIGGER IF EXISTS on_auth_user_created ON auth.users;
CREATE TRIGGER on_auth_user_created
    AFTER INSERT OR UPDATE ON auth.users
    FOR EACH ROW EXECUTE FUNCTION public.handle_new_user();

-- Branches
DROP POLICY IF EXISTS "Owners manage their branches" ON public.branches;
CREATE POLICY "Owners manage their branches" ON public.branches
    FOR ALL USING (auth.uid() = owner_id);

-- Customers
DROP POLICY IF EXISTS "Branch owners access branch customers" ON public.customers;
CREATE POLICY "Branch owners access branch customers" ON public.customers
    FOR ALL USING (
        EXISTS (SELECT 1 FROM public.branches WHERE branches.id = customers.branch_id AND branches.owner_id = auth.uid())
    );

-- Subscriptions
DROP POLICY IF EXISTS "Branch owners access subscriptions" ON public.customer_subscriptions;
CREATE POLICY "Branch owners access subscriptions" ON public.customer_subscriptions
    FOR ALL USING (
        EXISTS (
            SELECT 1 FROM public.customers
            JOIN public.branches ON branches.id = customers.branch_id
            WHERE customers.id = customer_subscriptions.customer_id AND branches.owner_id = auth.uid()
        )
    );

-- Kitchen Batches
DROP POLICY IF EXISTS "Branch owners access kitchen batches" ON public.kitchen_prep_batches;
CREATE POLICY "Branch owners access kitchen batches" ON public.kitchen_prep_batches
    FOR ALL USING (
        EXISTS (SELECT 1 FROM public.branches WHERE branches.id = kitchen_prep_batches.branch_id AND branches.owner_id = auth.uid())
    );

-- Deliveries
DROP POLICY IF EXISTS "Branch owners access deliveries" ON public.daily_deliveries;
CREATE POLICY "Branch owners access deliveries" ON public.daily_deliveries
    FOR ALL USING (
        EXISTS (SELECT 1 FROM public.branches WHERE branches.id = daily_deliveries.branch_id AND branches.owner_id = auth.uid())
    );

-- Wallet Ledger
DROP POLICY IF EXISTS "Branch owners access ledger" ON public.wallet_ledger;
CREATE POLICY "Branch owners access ledger" ON public.wallet_ledger
    FOR ALL USING (
        EXISTS (SELECT 1 FROM public.branches WHERE branches.id = wallet_ledger.branch_id AND branches.owner_id = auth.uid())
    );

-- Monthly Financial Records
DROP POLICY IF EXISTS "Branch owners access financial records" ON public.monthly_financial_records;
CREATE POLICY "Branch owners access financial records" ON public.monthly_financial_records
    FOR ALL USING (
        EXISTS (SELECT 1 FROM public.branches WHERE branches.id = monthly_financial_records.branch_id AND branches.owner_id = auth.uid())
    );

-- Subscription Specific Vacation Holds
CREATE TABLE IF NOT EXISTS public.subscription_vacation_holds (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    subscription_id UUID NOT NULL REFERENCES public.customer_subscriptions(id) ON DELETE CASCADE,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    meal_plan_name TEXT,
    reason TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

ALTER TABLE public.subscription_vacation_holds ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "Branch owners access subscription vacation holds" ON public.subscription_vacation_holds;
CREATE POLICY "Branch owners access subscription vacation holds" ON public.subscription_vacation_holds
    FOR ALL USING (
        EXISTS (
            SELECT 1 FROM public.customer_subscriptions
            JOIN public.customers ON customers.id = customer_subscriptions.customer_id
            JOIN public.branches ON branches.id = customers.branch_id
            WHERE customer_subscriptions.id = subscription_vacation_holds.subscription_id
            AND branches.owner_id = auth.uid()
        )
    );

-- ==============================================================================
-- 9. EXPENSES & RECEIPTS
-- ==============================================================================
CREATE TABLE IF NOT EXISTS public.expenses (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    branch_id UUID NOT NULL REFERENCES public.branches(id) ON DELETE CASCADE,
    expense_date DATE NOT NULL DEFAULT CURRENT_DATE,
    category TEXT NOT NULL CHECK (category IN ('groceries', 'spices', 'packaging', 'fuel', 'gas', 'rent', 'salary', 'other')),
    amount NUMERIC(10, 2) NOT NULL CHECK (amount >= 0),
    tax NUMERIC(10, 2) NOT NULL DEFAULT 0.0 CHECK (tax >= 0),
    payment_mode TEXT NOT NULL DEFAULT '' CHECK (payment_mode IN ('cash', 'upi', 'bank_transfer', 'card')),
    description TEXT,
    receipt_url TEXT,
    receipt_name TEXT,
    receipt_size_bytes BIGINT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

ALTER TABLE public.expenses ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "Branch owners access expenses" ON public.expenses;
CREATE POLICY "Branch owners access expenses" ON public.expenses
    FOR ALL USING (
        EXISTS (SELECT 1 FROM public.branches WHERE branches.id = expenses.branch_id AND branches.owner_id = auth.uid())
    );

-- ==============================================================================
-- 10. RECURRING EXPENSE RULES
-- ==============================================================================
CREATE TABLE IF NOT EXISTS public.recurring_expense_rules (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    branch_id UUID NOT NULL REFERENCES public.branches(id) ON DELETE CASCADE,
    title TEXT NOT NULL,
    category TEXT NOT NULL CHECK (category IN ('salary', 'rent', 'groceries', 'gas', 'packaging', 'other')),
    amount NUMERIC(10, 2) NOT NULL CHECK (amount >= 0),
    tax NUMERIC(10, 2) NOT NULL DEFAULT 0.0 CHECK (tax >= 0),
    frequency TEXT NOT NULL DEFAULT '' CHECK (frequency IN ('daily', 'weekly', 'monthly')),
    day_of_month INT CHECK (day_of_month BETWEEN 1 AND 31),
    start_date DATE NOT NULL DEFAULT CURRENT_DATE,
    end_date DATE,
    next_due_date DATE NOT NULL,
    last_generated_date DATE,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

ALTER TABLE public.recurring_expense_rules ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "Branch owners access recurring rules" ON public.recurring_expense_rules;
CREATE POLICY "Branch owners access recurring rules" ON public.recurring_expense_rules
    FOR ALL USING (
        EXISTS (SELECT 1 FROM public.branches WHERE branches.id = recurring_expense_rules.branch_id AND branches.owner_id = auth.uid())
    );

-- ==============================================================================
-- 11. CATEGORY & BRANCH OPERATIONAL HOLDS (PAUSES)
-- ==============================================================================
CREATE TABLE IF NOT EXISTS public.kitchen_holds (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    branch_id UUID REFERENCES public.branches(id) ON DELETE CASCADE,
    category TEXT NOT NULL DEFAULT '' CHECK (category IN ('breakfast', 'lunch', 'dinner', 'all')),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    hold_message TEXT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_by UUID REFERENCES public.profiles(id) ON DELETE SET NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

ALTER TABLE public.kitchen_holds ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "Branch owners access kitchen holds" ON public.kitchen_holds;
CREATE POLICY "Branch owners access kitchen holds" ON public.kitchen_holds
    FOR ALL USING (
        kitchen_holds.branch_id IS NULL OR
        EXISTS (SELECT 1 FROM public.branches WHERE branches.id = kitchen_holds.branch_id AND branches.owner_id = auth.uid())
    );

-- ==============================================================================
-- 12. STAFF DIRECTORY & OPERATIONAL ROLES
-- ==============================================================================
CREATE TABLE IF NOT EXISTS public.staff_members (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    branch_id UUID NOT NULL REFERENCES public.branches(id) ON DELETE CASCADE,
    full_name TEXT NOT NULL,
    phone TEXT NOT NULL,
    email TEXT,
    role TEXT NOT NULL DEFAULT '' CHECK (role IN ('Cook', 'Packing', 'Driver', 'Manager')),
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

ALTER TABLE public.staff_members ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "Branch owners access staff members" ON public.staff_members;
CREATE POLICY "Branch owners access staff members" ON public.staff_members
    FOR ALL USING (
        EXISTS (SELECT 1 FROM public.branches WHERE branches.id = staff_members.branch_id AND branches.owner_id = auth.uid())
    );

-- ==============================================================================
-- 13. NOTIFICATION PREFERENCES & SETTINGS
-- ==============================================================================
CREATE TABLE IF NOT EXISTS public.notification_settings (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    branch_id UUID NOT NULL REFERENCES public.branches(id) ON DELETE CASCADE,
    setting_key TEXT NOT NULL,
    category TEXT NOT NULL CHECK (category IN ('customer', 'vendor')),
    title TEXT NOT NULL,
    recipient_type TEXT NOT NULL,
    channels TEXT[] NOT NULL DEFAULT ARRAY['whatsapp', 'sms'],
    trigger_detail TEXT,
    sample_template TEXT,
    is_enabled BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    UNIQUE(branch_id, setting_key)
);

ALTER TABLE public.notification_settings ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "Branch owners access notification settings" ON public.notification_settings;
CREATE POLICY "Branch owners access notification settings" ON public.notification_settings
    FOR ALL USING (
        EXISTS (SELECT 1 FROM public.branches WHERE branches.id = notification_settings.branch_id AND branches.owner_id = auth.uid())
    );

-- ==============================================================================
-- 14. SUPABASE STORAGE BUCKET: RECEIPTS
-- ==============================================================================
INSERT INTO storage.buckets (id, name, public)
VALUES ('receipts', 'receipts', true)
ON CONFLICT (id) DO NOTHING;

DROP POLICY IF EXISTS "Authenticated users can upload receipts" ON storage.objects;
CREATE POLICY "Authenticated users can upload receipts"
ON storage.objects FOR INSERT TO authenticated
WITH CHECK (bucket_id = 'receipts');

DROP POLICY IF EXISTS "Public can view receipts" ON storage.objects;
CREATE POLICY "Public can view receipts"
ON storage.objects FOR SELECT TO authenticated, anon
USING (bucket_id = 'receipts');

DROP POLICY IF EXISTS "Branch owners delete receipts" ON storage.objects;
CREATE POLICY "Branch owners delete receipts"
ON storage.objects FOR DELETE TO authenticated
USING (bucket_id = 'receipts');

-- ==============================================================================
-- 15. MEAL CATEGORIES & SHIFTS
-- ==============================================================================
CREATE TABLE IF NOT EXISTS public.meal_categories (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    branch_id UUID NOT NULL REFERENCES public.branches(id) ON DELETE CASCADE,
    name TEXT NOT NULL, -- e.g. Breakfast, Lunch, Dinner
    start_time TEXT NOT NULL DEFAULT '',
    end_time TEXT NOT NULL DEFAULT '',
    description TEXT,
    display_order INT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

ALTER TABLE public.meal_categories ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "Branch owners access meal categories" ON public.meal_categories;
CREATE POLICY "Branch owners access meal categories" ON public.meal_categories
    FOR ALL USING (
        EXISTS (SELECT 1 FROM public.branches WHERE branches.id = meal_categories.branch_id AND branches.owner_id = auth.uid())
    );

-- ==============================================================================
-- 16. DELIVERY ROUTES & STOP SEQUENCES
-- ==============================================================================
CREATE TABLE IF NOT EXISTS public.delivery_routes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    branch_id UUID NOT NULL REFERENCES public.branches(id) ON DELETE CASCADE,
    route_name TEXT NOT NULL,
    assigned_driver_id UUID REFERENCES public.profiles(id) ON DELETE SET NULL,
    stops_count INT NOT NULL DEFAULT 0,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

ALTER TABLE public.delivery_routes ENABLE ROW LEVEL SECURITY;

-- ==============================================================================
-- 17. MEAL PLANS (CATALOG & PRICING PACKAGES)
-- ==============================================================================
CREATE TABLE IF NOT EXISTS public.meal_plans (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    branch_id UUID NOT NULL REFERENCES public.branches(id) ON DELETE CASCADE,
    category_id UUID REFERENCES public.meal_categories(id) ON DELETE SET NULL,
    category_name TEXT NOT NULL DEFAULT '',
    name TEXT NOT NULL, -- e.g. "Standard Thali Plan"
    description TEXT,
    is_veg BOOLEAN NOT NULL DEFAULT true,
    is_jain BOOLEAN NOT NULL DEFAULT false,
    daily_rate NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    weekly_rate NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    monthly_rate NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    max_order_increment INT NOT NULL DEFAULT 0,
    is_featured BOOLEAN NOT NULL DEFAULT true,
    is_active BOOLEAN NOT NULL DEFAULT true,
    composition_items_count INT NOT NULL DEFAULT 0,
    delivery_days TEXT[] NOT NULL DEFAULT ARRAY['Su', 'M', 'T', 'W', 'T', 'F', 'S'],
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

ALTER TABLE public.meal_plans ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "Branch owners access meal plans" ON public.meal_plans;
CREATE POLICY "Branch owners access meal plans" ON public.meal_plans
    FOR ALL USING (
        EXISTS (SELECT 1 FROM public.branches WHERE branches.id = meal_plans.branch_id AND branches.owner_id = auth.uid())
    );

-- 18. DAY-WISE MENU ITEMS (ROTATION SCHEDULE)
CREATE TABLE IF NOT EXISTS public.meal_plan_day_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    meal_plan_id UUID NOT NULL REFERENCES public.meal_plans(id) ON DELETE CASCADE,
    day_of_week TEXT NOT NULL CHECK (day_of_week IN ('Su', 'M', 'T', 'W', 'T', 'F', 'S')),
    item_name TEXT NOT NULL,
    portion_count TEXT NOT NULL DEFAULT '',
    is_veg BOOLEAN NOT NULL DEFAULT true,
    display_order INT NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

ALTER TABLE public.meal_plan_day_items ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "Branch owners access meal plan day items" ON public.meal_plan_day_items;
CREATE POLICY "Branch owners access meal plan day items" ON public.meal_plan_day_items
    FOR ALL USING (
        EXISTS (
            SELECT 1 FROM public.meal_plans 
            JOIN public.branches ON branches.id = meal_plans.branch_id 
            WHERE meal_plans.id = meal_plan_day_items.meal_plan_id 
            AND branches.owner_id = auth.uid()
        )
    );

-- ==============================================================================
-- 19. APP SYSTEM SETTINGS & HARDWARE CONFIG
-- ==============================================================================
CREATE TABLE IF NOT EXISTS public.app_system_settings (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    branch_id UUID NOT NULL REFERENCES public.branches(id) ON DELETE CASCADE,
    auto_lock_daily_orders BOOLEAN NOT NULL DEFAULT true,
    daily_cutoff_time TEXT NOT NULL DEFAULT '',
    default_dietary_mode TEXT NOT NULL DEFAULT '' CHECK (default_dietary_mode IN ('pure_veg', 'mix')),
    kds_auto_refresh BOOLEAN NOT NULL DEFAULT true,
    kds_refresh_seconds INT NOT NULL DEFAULT 0,
    print_sticker_labels BOOLEAN NOT NULL DEFAULT true,
    thermal_printer_model TEXT NOT NULL DEFAULT '',
    is_printer_connected BOOLEAN NOT NULL DEFAULT false,
    is_portion_scale_configured BOOLEAN NOT NULL DEFAULT false,
    currency_symbol TEXT NOT NULL DEFAULT '',
    currency_name TEXT NOT NULL DEFAULT 'Indian Rupee (₹)',
    gst_rate_percent NUMERIC(4, 2) NOT NULL DEFAULT 0.00,
    customer_trust_limit NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    thermal_receipt_footer_text TEXT NOT NULL DEFAULT '',
    sound_chimes_and_alerts BOOLEAN NOT NULL DEFAULT true,
    operational_timezone TEXT NOT NULL DEFAULT 'Asia/Kolkata (IST +5:30)',
    app_language TEXT NOT NULL DEFAULT 'English (UK / India)',
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    UNIQUE(branch_id)
);

ALTER TABLE public.app_system_settings ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "Branch owners access system settings" ON public.app_system_settings;
CREATE POLICY "Branch owners access system settings" ON public.app_system_settings
    FOR ALL USING (
        EXISTS (SELECT 1 FROM public.branches WHERE branches.id = app_system_settings.branch_id AND branches.owner_id = auth.uid())
    );

-- ==============================================================================
-- 20. ACTIVITY INBOX & DISPATCH NOTIFICATIONS
-- ==============================================================================
CREATE TABLE IF NOT EXISTS public.activity_notifications (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    branch_id UUID NOT NULL REFERENCES public.branches(id) ON DELETE CASCADE,
    type TEXT NOT NULL CHECK (type IN ('dispatch', 'hold', 'wallet', 'payment', 'cooking', 'system')),
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    is_unread BOOLEAN NOT NULL DEFAULT true,
    route_code TEXT,
    driver_name TEXT,
    amount NUMERIC(10, 2),
    txn_code TEXT,
    chef_name TEXT,
    customer_name TEXT,
    hold_dates TEXT,
    action_label TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

ALTER TABLE public.activity_notifications ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "Branch owners access activity notifications" ON public.activity_notifications;
CREATE POLICY "Branch owners access activity notifications" ON public.activity_notifications
    FOR ALL USING (
        EXISTS (SELECT 1 FROM public.branches WHERE branches.id = activity_notifications.branch_id AND branches.owner_id = auth.uid())
    );

-- Profile extensions for business credentials
ALTER TABLE public.profiles ADD COLUMN IF NOT EXISTS secondary_phone TEXT;
ALTER TABLE public.profiles ADD COLUMN IF NOT EXISTS display_job_title TEXT DEFAULT '';
ALTER TABLE public.profiles ADD COLUMN IF NOT EXISTS role_title TEXT DEFAULT '';
ALTER TABLE public.profiles ADD COLUMN IF NOT EXISTS business_gstin TEXT;
ALTER TABLE public.profiles ADD COLUMN IF NOT EXISTS is_kyc_verified BOOLEAN DEFAULT false;

-- ==============================================================================
-- 21. KITCHEN ONBOARDING & SETUP STATE (FOLDER 6)
-- ==============================================================================
CREATE TABLE IF NOT EXISTS public.kitchen_onboarding (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    branch_id UUID NOT NULL REFERENCES public.branches(id) ON DELETE CASCADE,
    brand_name TEXT NOT NULL DEFAULT '',
    breakfast_slot_enabled BOOLEAN NOT NULL DEFAULT true,
    lunch_slot_enabled BOOLEAN NOT NULL DEFAULT true,
    dinner_slot_enabled BOOLEAN NOT NULL DEFAULT true,
    dietary_specialization TEXT NOT NULL DEFAULT '' CHECK (dietary_specialization IN ('pure-veg', 'mix', 'jain')),
    operational_currency TEXT NOT NULL DEFAULT '₹ INR (Rupee)',
    debit_trust_limit NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    is_onboarding_complete BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    UNIQUE(branch_id)
);

ALTER TABLE public.kitchen_onboarding ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "Branch owners access kitchen onboarding" ON public.kitchen_onboarding;
CREATE POLICY "Branch owners access kitchen onboarding" ON public.kitchen_onboarding
    FOR ALL USING (
        EXISTS (SELECT 1 FROM public.branches WHERE branches.id = kitchen_onboarding.branch_id AND branches.owner_id = auth.uid())
    );

-- ==============================================================================
-- 22. SUPABASE STORAGE BUCKETS (BRAND LOGOS & CUSTOMER SLIPS)
-- ==============================================================================
INSERT INTO storage.buckets (id, name, public)
VALUES ('brand-assets', 'brand-assets', true)
ON CONFLICT (id) DO NOTHING;

INSERT INTO storage.buckets (id, name, public)
VALUES ('thermal-receipts', 'thermal-receipts', false)
ON CONFLICT (id) DO NOTHING;

DROP POLICY IF EXISTS "Public brand assets are viewable by everyone" ON storage.objects;
CREATE POLICY "Public brand assets are viewable by everyone" ON storage.objects
    FOR SELECT USING (bucket_id = 'brand-assets');

DROP POLICY IF EXISTS "Authenticated kitchen owners upload brand assets" ON storage.objects;
CREATE POLICY "Authenticated kitchen owners upload brand assets" ON storage.objects
    FOR ALL USING (bucket_id = 'brand-assets' AND auth.role() = 'authenticated');

-- ==============================================================================
-- 23. AUTH VERIFICATION SESSIONS & OTP CODES (FOLDER 8)
-- ==============================================================================
CREATE TABLE IF NOT EXISTS public.auth_verification_sessions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email TEXT,
    phone TEXT,
    otp_code_hash TEXT NOT NULL,
    verification_type TEXT NOT NULL DEFAULT '' CHECK (verification_type IN ('password_reset', 'registration', 'email_change')),
    is_verified BOOLEAN NOT NULL DEFAULT false,
    attempts_count INT NOT NULL DEFAULT 0,
    expires_at TIMESTAMPTZ NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

ALTER TABLE public.auth_verification_sessions ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "Public can create and verify own verification sessions" ON public.auth_verification_sessions;
CREATE POLICY "Public can create and verify own verification sessions" ON public.auth_verification_sessions
    FOR ALL USING (true);

-- ==============================================================================
-- 24. TERMS & PRIVACY CONSENT REGISTRY (FOLDER 8)
-- ==============================================================================
CREATE TABLE IF NOT EXISTS public.legal_consents (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES public.profiles(id) ON DELETE CASCADE,
    email TEXT NOT NULL,
    terms_version TEXT NOT NULL DEFAULT '',
    privacy_version TEXT NOT NULL DEFAULT '',
    accepted_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    ip_address TEXT,
    user_agent TEXT
);

ALTER TABLE public.legal_consents ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "Authenticated users record and view legal consent" ON public.legal_consents;
CREATE POLICY "Authenticated users record and view legal consent" ON public.legal_consents
    FOR ALL USING (auth.uid() = user_id OR auth.role() = 'anon');

-- ==============================================================================
-- 25. SUPPORT INQUIRIES & HELPDESK TICKETS (FOLDER 9)
-- ==============================================================================
CREATE TABLE IF NOT EXISTS public.support_inquiries (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    full_name TEXT NOT NULL,
    email TEXT NOT NULL,
    phone TEXT,
    business_name TEXT,
    topic TEXT NOT NULL DEFAULT '' CHECK (topic IN ('onboarding', 'billing', 'kitchen', 'routes', 'other')),
    message TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT '' CHECK (status IN ('open', 'in_progress', 'resolved', 'closed')),
    assigned_agent TEXT DEFAULT '',
    metadata JSONB DEFAULT '{}'::jsonb,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    resolved_at TIMESTAMPTZ
);

ALTER TABLE public.support_inquiries ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "Public can submit support inquiries" ON public.support_inquiries;
CREATE POLICY "Public can submit support inquiries" ON public.support_inquiries
    FOR INSERT WITH CHECK (true);

DROP POLICY IF EXISTS "Users can read own support inquiries" ON public.support_inquiries;
CREATE POLICY "Users can read own support inquiries" ON public.support_inquiries
    FOR SELECT USING (auth.role() = 'anon' OR email = auth.jwt() ->> 'email');

-- ==============================================================================
-- 26. KNOWLEDGE BASE ARTICLES & FAQ MODULES (FOLDER 9)
-- ==============================================================================
CREATE TABLE IF NOT EXISTS public.knowledge_base_articles (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title TEXT NOT NULL,
    category TEXT NOT NULL CHECK (category IN ('getting-started', 'kitchen-cutoffs', 'wallet-billing', 'holds-pauses', 'delivery-routes')),
    slug TEXT UNIQUE NOT NULL,
    preview_text TEXT NOT NULL,
    content TEXT NOT NULL,
    steps TEXT[] DEFAULT '{}',
    is_published BOOLEAN NOT NULL DEFAULT true,
    view_count INT NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

ALTER TABLE public.knowledge_base_articles ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "Public can view published knowledge articles" ON public.knowledge_base_articles;
CREATE POLICY "Public can view published knowledge articles" ON public.knowledge_base_articles
    FOR SELECT USING (is_published = true);

-- ==============================================================================
-- 27. PERSISTENT ACTIVE AUTH SESSIONS & DEVICE REGISTRY (FOLDER 10)
-- ==============================================================================
CREATE TABLE IF NOT EXISTS public.active_auth_sessions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES public.profiles(id) ON DELETE CASCADE,
    email TEXT NOT NULL,
    device_name TEXT NOT NULL DEFAULT '',
    device_id TEXT NOT NULL,
    ip_address TEXT,
    is_active BOOLEAN NOT NULL DEFAULT true,
    last_active_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

ALTER TABLE public.active_auth_sessions ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "Users can read and update own active sessions" ON public.active_auth_sessions;
CREATE POLICY "Users can read and update own active sessions" ON public.active_auth_sessions
    FOR ALL USING (auth.role() = 'anon' OR email = auth.jwt() ->> 'email');

-- ==============================================================================
-- 28. LEGAL POLICIES & CHARTER REGISTRY (FOLDER 10)
-- ==============================================================================
CREATE TABLE IF NOT EXISTS public.legal_charters (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    slug TEXT UNIQUE NOT NULL,
    title TEXT NOT NULL,
    version TEXT NOT NULL DEFAULT '',
    effective_date DATE NOT NULL DEFAULT CURRENT_DATE,
    category TEXT NOT NULL CHECK (category IN ('merchant', 'security', 'billing', 'privacy', 'dpa')),
    summary TEXT NOT NULL,
    full_text_markdown TEXT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

ALTER TABLE public.legal_charters ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "Public can read active legal charters" ON public.legal_charters;
CREATE POLICY "Public can read active legal charters" ON public.legal_charters
    FOR SELECT USING (is_active = true);

-- Seed Folder 10 Official Legal Charters & Policies
INSERT INTO public.legal_charters (slug, title, version, category, summary, full_text_markdown)
VALUES
    ('terms-of-service', 'Terms of Service', 'v2.4', 'merchant', 'Core merchant master agreement, subscription tiers, SLA, and acceptable use policy.', 'Full Terms of Service covering tenant separation, billing schedules, and liability.'),
    ('privacy-policy', 'Privacy Policy', 'v2.4', 'privacy', 'Collection of merchant and customer location data, route history, and consumer rights.', 'Full Privacy Policy covering DPDP Act, data retention, and phone contact privacy.'),
    ('cancellation-policy', 'Cancellation Policy', 'v2.4', 'merchant', 'Step-by-step 4-step self-service cancellation, 90-day safe export data guarantee.', 'Self service portal steps, data retention for 90 days, and zero termination fees.'),
    ('refund-policy', 'Refund Policy', 'v2.4', 'billing', 'Try before you pay, strict non-refundable subscription terms, and 30-day dispute resolution.', 'Trial period terms, no prorated refunds on active cycles, and billing discrepancy resolution.'),
    ('security-policy', 'Security Policy', 'v2.4', 'security', 'SOC 2 Type II controls, TLS 1.3 in-transit encryption, AES-256 at-rest, and strict ACL.', 'Comprehensive security policies including data encryption, hosting, vulnerability disclosure, and audits.'),
    ('data-processing-addendum', 'Data Processing Addendum (DPA)', 'v2.4', 'dpa', 'Controller-Processor clauses, 48-hour security incident notification SLA, and Bihar jurisdiction.', 'Formal data processing addendum compliant with enterprise vendor standards.')
ON CONFLICT (slug) DO UPDATE SET
    title = EXCLUDED.title,
    version = EXCLUDED.version,
    summary = EXCLUDED.summary,
    full_text_markdown = EXCLUDED.full_text_markdown,
    updated_at = timezone('utc'::text, now());

-- ==============================================================================
-- 29. CUSTOMER SUBSCRIBER PORTAL & GUEST ORDERS (FOLDER 11)
-- ==============================================================================
CREATE TABLE IF NOT EXISTS public.customer_subscriber_profiles (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    customer_id UUID REFERENCES public.customers(id) ON DELETE CASCADE,
    subscriber_number TEXT UNIQUE NOT NULL DEFAULT ('#SUB-' || lpad(floor(random()*10000)::text, 4, '0')),
    full_name TEXT NOT NULL,
    phone TEXT NOT NULL,
    email TEXT,
    emergency_contact TEXT,
    avatar_url TEXT,
    location_type TEXT NOT NULL DEFAULT '' CHECK (location_type IN ('apartment', 'hostel', 'office', 'other')),
    primary_flat_unit TEXT NOT NULL,
    primary_landmark TEXT,
    primary_city_pincode TEXT NOT NULL DEFAULT '',
    gps_latitude NUMERIC(10, 6),
    gps_longitude NUMERIC(10, 6),
    gps_accuracy_meters NUMERIC(5, 2),
    driver_drop_note TEXT,
    has_sunday_address BOOLEAN NOT NULL DEFAULT false,
    sunday_address_title TEXT,
    sunday_address_details TEXT,
    sunday_route_id TEXT,
    lunch_slot_window TEXT NOT NULL DEFAULT '',
    dinner_slot_window TEXT NOT NULL DEFAULT '',
    wallet_balance NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    wallet_overdraft_buffer NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    notify_morning_menu BOOLEAN NOT NULL DEFAULT true,
    notify_driver_dispatch BOOLEAN NOT NULL DEFAULT true,
    notify_drop_photo BOOLEAN NOT NULL DEFAULT true,
    notify_low_balance BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

ALTER TABLE public.customer_subscriber_profiles ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "Public can manage own subscriber profile" ON public.customer_subscriber_profiles;
CREATE POLICY "Public can manage own subscriber profile" ON public.customer_subscriber_profiles
    FOR ALL USING (true);

-- Guest Meals & Add-on Plates
CREATE TABLE IF NOT EXISTS public.customer_guest_orders (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    subscriber_id UUID REFERENCES public.customer_subscriber_profiles(id) ON DELETE CASCADE,
    session_title TEXT NOT NULL, -- e.g. 'Dinner Today' or 'Lunch Tomorrow'
    session_date DATE NOT NULL DEFAULT CURRENT_DATE,
    meal_title TEXT NOT NULL,
    quantity INT NOT NULL DEFAULT 0,
    unit_price NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    total_amount NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    packaging_type TEXT NOT NULL DEFAULT '' CHECK (packaging_type IN ('tiffin', 'disposable')),
    packaging_surcharge NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    dietary_types TEXT[] DEFAULT '{""}',
    spice_levels TEXT[] DEFAULT '{""}',
    include_cutlery BOOLEAN NOT NULL DEFAULT true,
    kitchen_note TEXT,
    payment_mode TEXT NOT NULL DEFAULT '',
    status TEXT NOT NULL DEFAULT '' CHECK (status IN ('confirmed', 'preparing', 'dispatched', 'delivered', 'cancelled')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

ALTER TABLE public.customer_guest_orders ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "Public can manage own guest orders" ON public.customer_guest_orders;
CREATE POLICY "Public can manage own guest orders" ON public.customer_guest_orders
    FOR ALL USING (true);

-- Vacation Holds & Pauses
CREATE TABLE IF NOT EXISTS public.customer_vacation_holds (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    subscriber_id UUID REFERENCES public.customer_subscriber_profiles(id) ON DELETE CASCADE,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    pause_type TEXT NOT NULL DEFAULT '' CHECK (pause_type IN ('both', 'lunch_only', 'dinner_only')),
    reason TEXT,
    days_count INT NOT NULL DEFAULT 0,
    total_refund_credit NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    status TEXT NOT NULL DEFAULT '' CHECK (status IN ('active', 'completed', 'cancelled')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

ALTER TABLE public.customer_vacation_holds ENABLE ROW LEVEL SECURITY;

DROP POLICY IF EXISTS "Public can manage own vacation holds" ON public.customer_vacation_holds;
CREATE POLICY "Public can manage own vacation holds" ON public.customer_vacation_holds
    FOR ALL USING (true);

-- ========================================================
-- 30. CUSTOMER PORTAL EXTENSIONS (FOLDER 12: RATINGS, ISSUES, PLAN UPGRADES, NOTIFICATIONS)
-- ========================================================

-- Meal Feedback & Dish-by-dish Ratings
CREATE TABLE IF NOT EXISTS public.customer_meal_ratings (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    subscriber_id UUID REFERENCES public.customer_subscriber_profiles(id) ON DELETE CASCADE,
    meal_title TEXT NOT NULL,
    meal_date DATE NOT NULL DEFAULT CURRENT_DATE,
    meal_slot TEXT NOT NULL DEFAULT '' CHECK (meal_slot IN ('breakfast', 'lunch', 'dinner')),
    overall_rating INT NOT NULL CHECK (overall_rating BETWEEN 1 AND 5),
    sentiment_label TEXT NOT NULL DEFAULT '',
    dish_ratings JSONB NOT NULL DEFAULT '[]'::jsonb,
    portion_check TEXT NOT NULL DEFAULT '' CHECK (portion_check IN ('Too Little', 'Just Right', 'Extra')),
    temperature_check TEXT NOT NULL DEFAULT '' CHECK (temperature_check IN ('Hot & Fresh', 'Lukewarm', 'Cold')),
    packaging_hygiene_score INT NOT NULL DEFAULT 0,
    photo_url TEXT,
    chef_note TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

ALTER TABLE public.customer_meal_ratings ENABLE ROW LEVEL SECURITY;
DROP POLICY IF EXISTS "Public can manage own meal ratings" ON public.customer_meal_ratings;
CREATE POLICY "Public can manage own meal ratings" ON public.customer_meal_ratings FOR ALL USING (true);

-- Customer Meal Issues & Helpdesk Tickets
CREATE TABLE IF NOT EXISTS public.customer_meal_issues (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    subscriber_id UUID REFERENCES public.customer_subscriber_profiles(id) ON DELETE CASCADE,
    target_delivery_title TEXT NOT NULL,
    delivery_time TEXT NOT NULL,
    category TEXT NOT NULL CHECK (category IN (
        'Food Quality / Taste Issue',
        'Missing Item / Wrong Dish',
        'Spill or Packaging Leak',
        'Tiffin Delayed / Late',
        'Driver / Drop Location',
        'Billing & Wallet Query'
    )),
    issue_tags TEXT[] DEFAULT '{}',
    photo_url TEXT,
    description TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT '' CHECK (status IN ('open', 'under_review', 'resolved', 'refunded', 'replacement_dispatched')),
    resolution_type TEXT CHECK (resolution_type IN ('instant_refund', 'replacement_meal', 'chef_call', 'dismissed')),
    refund_amount NUMERIC(10, 2) DEFAULT 0.00,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now()),
    resolved_at TIMESTAMPTZ
);

ALTER TABLE public.customer_meal_issues ENABLE ROW LEVEL SECURITY;
DROP POLICY IF EXISTS "Public can manage own meal issues" ON public.customer_meal_issues;
CREATE POLICY "Public can manage own meal issues" ON public.customer_meal_issues FOR ALL USING (true);

-- Plan Changes & Upgrades
CREATE TABLE IF NOT EXISTS public.customer_plan_changes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    subscriber_id UUID REFERENCES public.customer_subscriber_profiles(id) ON DELETE CASCADE,
    from_plan_title TEXT NOT NULL,
    to_plan_title TEXT NOT NULL,
    from_monthly_cost NUMERIC(10, 2) NOT NULL,
    to_monthly_cost NUMERIC(10, 2) NOT NULL,
    switch_timing TEXT NOT NULL CHECK (switch_timing IN ('immediate', 'next_cycle')),
    prorated_charge NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    effective_date DATE NOT NULL,
    delivery_location TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT '' CHECK (status IN ('active', 'pending', 'cancelled')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

ALTER TABLE public.customer_plan_changes ENABLE ROW LEVEL SECURITY;
DROP POLICY IF EXISTS "Public can manage own plan changes" ON public.customer_plan_changes;
CREATE POLICY "Public can manage own plan changes" ON public.customer_plan_changes FOR ALL USING (true);

-- Subscriber Portal Push & In-app Notifications
CREATE TABLE IF NOT EXISTS public.customer_notifications (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    subscriber_id UUID REFERENCES public.customer_subscriber_profiles(id) ON DELETE CASCADE,
    title TEXT NOT NULL,
    body TEXT NOT NULL,
    category TEXT NOT NULL CHECK (category IN ('delivery', 'wallet', 'announcement', 'general')),
    priority TEXT NOT NULL DEFAULT '' CHECK (priority IN ('normal', 'urgent', 'promotional')),
    is_read BOOLEAN NOT NULL DEFAULT false,
    timestamp_display TEXT NOT NULL,
    action_type TEXT,
    action_data JSONB,
    photo_url TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

ALTER TABLE public.customer_notifications ENABLE ROW LEVEL SECURITY;
DROP POLICY IF EXISTS "Public can manage own notifications" ON public.customer_notifications;
CREATE POLICY "Public can manage own notifications" ON public.customer_notifications FOR ALL USING (true);

-- ========================================================
-- 31. TIFFIN RIDER / DRIVER OPERATIONS (FOLDER 13)
-- ========================================================

-- Rider Shift Profiles & Telemetry
CREATE TABLE IF NOT EXISTS public.rider_profiles (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    driver_id_code TEXT NOT NULL UNIQUE,
    full_name TEXT NOT NULL,
    phone TEXT NOT NULL,
    vehicle_model TEXT,
    vehicle_plate TEXT,
    rating NUMERIC(3, 2) NOT NULL DEFAULT 0.00,
    total_safe_drops INT NOT NULL DEFAULT 0,
    tier TEXT NOT NULL DEFAULT '' CHECK (tier IN ('Silver', 'Gold', 'Platinum')),
    base_drop_rate NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    box_recovery_bonus NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    upi_id TEXT,
    bank_account_tail TEXT,
    is_on_duty BOOLEAN NOT NULL DEFAULT true,
    current_shift_name TEXT NOT NULL DEFAULT '',
    carrier_pod_id TEXT,
    carrier_pod_temp_c INT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

ALTER TABLE public.rider_profiles ENABLE ROW LEVEL SECURITY;
DROP POLICY IF EXISTS "Public can manage own rider profile" ON public.rider_profiles;
CREATE POLICY "Public can manage own rider profile" ON public.rider_profiles FOR ALL USING (true);

-- Rider Route Stops
CREATE TABLE IF NOT EXISTS public.rider_delivery_stops (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    rider_id UUID REFERENCES public.rider_profiles(id) ON DELETE SET NULL,
    sequence_number INT NOT NULL,
    subscriber_id_code TEXT,
    customer_name TEXT NOT NULL,
    customer_phone TEXT NOT NULL,
    address_line TEXT NOT NULL,
    wing_flat TEXT,
    society_cluster TEXT,
    meal_slot TEXT NOT NULL DEFAULT '',
    eta_display TEXT NOT NULL,
    status TEXT NOT NULL DEFAULT '' CHECK (status IN ('pending', 'en_route', 'delivered', 'skipped', 'hold')),
    special_note TEXT,
    items JSONB NOT NULL DEFAULT '[]'::jsonb,
    contactless_otp TEXT,
    is_otp_verified BOOLEAN NOT NULL DEFAULT false,
    pod_bin_slot TEXT NOT NULL DEFAULT '',
    photo_proof_url TEXT,
    delivered_at TIMESTAMPTZ,
    boxes_collected INT NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

ALTER TABLE public.rider_delivery_stops ENABLE ROW LEVEL SECURITY;
DROP POLICY IF EXISTS "Public can manage own delivery stops" ON public.rider_delivery_stops;
CREATE POLICY "Public can manage own delivery stops" ON public.rider_delivery_stops FOR ALL USING (true);

-- Steel Box Inventory & Custody Records
CREATE TABLE IF NOT EXISTS public.rider_box_custody_logs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    rider_id UUID REFERENCES public.rider_profiles(id) ON DELETE SET NULL,
    shift_code TEXT NOT NULL DEFAULT '',
    total_loaded INT NOT NULL DEFAULT 0,
    current_in_pod INT NOT NULL DEFAULT 0,
    deficit_due INT NOT NULL DEFAULT 0,
    recovered_count INT NOT NULL DEFAULT 0,
    undelivered_count INT NOT NULL DEFAULT 0,
    free_slots INT NOT NULL DEFAULT 0,
    scanned_lid_code TEXT,
    customer_reference TEXT,
    status TEXT NOT NULL DEFAULT '' CHECK (status IN ('in_pod', 'returned_to_hub', 'customer_deficit')),
    logged_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

ALTER TABLE public.rider_box_custody_logs ENABLE ROW LEVEL SECURITY;
DROP POLICY IF EXISTS "Public can manage box custody" ON public.rider_box_custody_logs;
CREATE POLICY "Public can manage box custody" ON public.rider_box_custody_logs FOR ALL USING (true);

-- Rider Earnings & Payout Ledger
CREATE TABLE IF NOT EXISTS public.rider_earnings_ledger (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    rider_id UUID REFERENCES public.rider_profiles(id) ON DELETE SET NULL,
    date DATE NOT NULL DEFAULT CURRENT_DATE,
    shift_name TEXT NOT NULL DEFAULT '',
    completed_drops INT NOT NULL DEFAULT 0,
    drops_payout NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    boxes_recovered INT NOT NULL DEFAULT 0,
    boxes_bonus NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    peak_incentive NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    total_today NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    available_for_payout NUMERIC(10, 2) NOT NULL DEFAULT 0.00,
    payout_status TEXT NOT NULL DEFAULT '' CHECK (payout_status IN ('ready', 'transferred', 'processing')),
    transferred_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

ALTER TABLE public.rider_earnings_ledger ENABLE ROW LEVEL SECURITY;
DROP POLICY IF EXISTS "Public can manage earnings ledger" ON public.rider_earnings_ledger;
CREATE POLICY "Public can manage earnings ledger" ON public.rider_earnings_ledger FOR ALL USING (true);

-- Rider Incident Reports
CREATE TABLE IF NOT EXISTS public.rider_incidents (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    rider_id UUID REFERENCES public.rider_profiles(id) ON DELETE SET NULL,
    stop_sequence_number INT NOT NULL DEFAULT 1,
    subscriber_id_code TEXT,
    customer_name TEXT NOT NULL,
    issue_type TEXT NOT NULL CHECK (issue_type IN ('leak', 'unreachable', 'wrong_bin', 'breakdown', 'weather')),
    incident_tags TEXT[] DEFAULT '{}',
    photo_url TEXT,
    supervisor_note TEXT,
    status TEXT NOT NULL DEFAULT '' CHECK (status IN ('open', 'dispatched_replacement', 'resolved')),
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

ALTER TABLE public.rider_incidents ENABLE ROW LEVEL SECURITY;
DROP POLICY IF EXISTS "Public can manage rider incidents" ON public.rider_incidents;
CREATE POLICY "Public can manage rider incidents" ON public.rider_incidents FOR ALL USING (true);

-- Rider Notifications & Route Dispatch Alerts
CREATE TABLE IF NOT EXISTS public.rider_notifications (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    rider_id UUID REFERENCES public.rider_profiles(id) ON DELETE SET NULL,
    category TEXT NOT NULL CHECK (category IN ('route', 'kitchen', 'payouts')),
    priority TEXT NOT NULL DEFAULT '' CHECK (priority IN ('normal', 'urgent')),
    title TEXT NOT NULL,
    body TEXT NOT NULL,
    timestamp_display TEXT NOT NULL,
    is_unread BOOLEAN NOT NULL DEFAULT true,
    action_type TEXT,
    created_at TIMESTAMPTZ NOT NULL DEFAULT timezone('utc'::text, now())
);

ALTER TABLE public.rider_notifications ENABLE ROW LEVEL SECURITY;
DROP POLICY IF EXISTS "Public can manage rider notifications" ON public.rider_notifications;
CREATE POLICY "Public can manage rider notifications" ON public.rider_notifications FOR ALL USING (true);

-- ==============================================================================
-- 32. MANUAL WALLET ADJUSTMENT & SUBSCRIPTION SYNCHRONIZATION EXTENSIONS
-- ==============================================================================
ALTER TABLE public.wallet_ledger ADD COLUMN IF NOT EXISTS subscription_id UUID REFERENCES public.customer_subscriptions(id) ON DELETE SET NULL;
ALTER TABLE public.wallet_ledger ADD COLUMN IF NOT EXISTS adjusted_by_user_id UUID REFERENCES public.profiles(id) ON DELETE SET NULL;
ALTER TABLE public.wallet_ledger ADD COLUMN IF NOT EXISTS remarks TEXT;

CREATE INDEX IF NOT EXISTS idx_wallet_ledger_customer_id ON public.wallet_ledger(customer_id);
CREATE INDEX IF NOT EXISTS idx_wallet_ledger_subscription_id ON public.wallet_ledger(subscription_id);
CREATE INDEX IF NOT EXISTS idx_customer_subscriptions_customer_id ON public.customer_subscriptions(customer_id);
CREATE INDEX IF NOT EXISTS idx_daily_deliveries_delivery_date ON public.daily_deliveries(delivery_date);

-- ==============================================================================
-- 33. MAP ROUTING, TSP STOPS & GEO-COORDINATE EXTENSIONS
-- ==============================================================================
ALTER TABLE public.branches ADD COLUMN IF NOT EXISTS address TEXT;
ALTER TABLE public.branches ADD COLUMN IF NOT EXISTS city TEXT;
ALTER TABLE public.branches ADD COLUMN IF NOT EXISTS state TEXT;
ALTER TABLE public.branches ADD COLUMN IF NOT EXISTS pincode TEXT;
ALTER TABLE public.branches ADD COLUMN IF NOT EXISTS phone TEXT;
ALTER TABLE public.branches ADD COLUMN IF NOT EXISTS latitude NUMERIC(10, 7) DEFAULT 19.1663000;
ALTER TABLE public.branches ADD COLUMN IF NOT EXISTS longitude NUMERIC(10, 7) DEFAULT 72.9986000;
ALTER TABLE public.branches ADD COLUMN IF NOT EXISTS coverage_radius_km NUMERIC(5, 2) DEFAULT 0.0;

ALTER TABLE public.customers ADD COLUMN IF NOT EXISTS latitude NUMERIC(10, 7);
ALTER TABLE public.customers ADD COLUMN IF NOT EXISTS longitude NUMERIC(10, 7);

ALTER TABLE public.delivery_routes ADD COLUMN IF NOT EXISTS origin_latitude NUMERIC(10, 7) DEFAULT 19.1663000;
ALTER TABLE public.delivery_routes ADD COLUMN IF NOT EXISTS origin_longitude NUMERIC(10, 7) DEFAULT 72.9986000;
ALTER TABLE public.delivery_routes ADD COLUMN IF NOT EXISTS total_distance_km NUMERIC(6, 2) DEFAULT 0.00;
ALTER TABLE public.delivery_routes ADD COLUMN IF NOT EXISTS total_duration_minutes INT DEFAULT 0;
ALTER TABLE public.delivery_routes ADD COLUMN IF NOT EXISTS waypoints JSONB DEFAULT '[]'::jsonb;

ALTER TABLE public.rider_delivery_stops ADD COLUMN IF NOT EXISTS latitude NUMERIC(10, 7) DEFAULT 19.1663000;
ALTER TABLE public.rider_delivery_stops ADD COLUMN IF NOT EXISTS longitude NUMERIC(10, 7) DEFAULT 72.9986000;

-- ==============================================================================
-- 34. SEED ACCOUNTS (ADMIN, DRIVER, STAFF, CUSTOMER)
-- Creates accounts directly in auth.users (with encrypted passwords) AND public.profiles
-- Default credentials:
-- Admin:    admin@gmail.com    / admin123
-- Driver:   driver@gmail.com   / driver123
-- Staff:    staff@gmail.com    / staff123
-- Customer: customer@gmail.com / customer123
-- ==============================================================================
DO $$
DECLARE
    v_admin_id UUID := 'a0000000-0000-0000-0000-000000000001'::UUID;
    v_driver_id UUID := 'a0000000-0000-0000-0000-000000000002'::UUID;
    v_staff_id UUID := 'a0000000-0000-0000-0000-000000000003'::UUID;
    v_customer_id UUID := 'a0000000-0000-0000-0000-000000000004'::UUID;
    v_branch_id UUID := 'b0000000-0000-0000-0000-000000000001'::UUID;
    v_existing_id UUID;
BEGIN
    -- 1. SEED ADMIN: admin@gmail.com / admin123
    SELECT id INTO v_existing_id FROM auth.users WHERE email = 'admin@gmail.com';
    IF v_existing_id IS NOT NULL THEN
        v_admin_id := v_existing_id;
        UPDATE auth.users
        SET encrypted_password = crypt('admin123', gen_salt('bf')),
            raw_user_meta_data = jsonb_build_object('full_name', 'Rajesh Sharma', 'role', 'owner'),
            email_confirmed_at = COALESCE(email_confirmed_at, timezone('utc'::text, now())),
            updated_at = timezone('utc'::text, now())
        WHERE id = v_admin_id;
    ELSE
        INSERT INTO auth.users (
            id, instance_id, aud, role, email, encrypted_password,
            email_confirmed_at, raw_app_meta_data, raw_user_meta_data,
            created_at, updated_at, confirmation_token, email_change,
            email_change_token_new, recovery_token
        ) VALUES (
            v_admin_id, '00000000-0000-0000-0000-000000000000'::UUID, 'authenticated', 'authenticated',
            'admin@gmail.com', crypt('admin123', gen_salt('bf')),
            timezone('utc'::text, now()),
            jsonb_build_object('provider', 'email', 'providers', array['email']),
            jsonb_build_object('full_name', 'Rajesh Sharma', 'role', 'owner'),
            timezone('utc'::text, now()), timezone('utc'::text, now()), '', '', '', ''
        ) ON CONFLICT (id) DO NOTHING;
    END IF;

    INSERT INTO public.profiles (id, email, full_name, phone, role, display_job_title, role_title)
    VALUES (v_admin_id, 'admin@gmail.com', 'Rajesh Sharma', '+91 98765 43210', 'owner', 'Head Chef & Kitchen Owner', 'Kitchen Owner')
    ON CONFLICT (id) DO UPDATE SET
        full_name = EXCLUDED.full_name,
        role = EXCLUDED.role,
        display_job_title = EXCLUDED.display_job_title,
        role_title = EXCLUDED.role_title;

    -- 2. SEED DRIVER / RIDER: driver@gmail.com / driver123
    SELECT id INTO v_existing_id FROM auth.users WHERE email = 'driver@gmail.com';
    IF v_existing_id IS NOT NULL THEN
        v_driver_id := v_existing_id;
        UPDATE auth.users
        SET encrypted_password = crypt('driver123', gen_salt('bf')),
            raw_user_meta_data = jsonb_build_object('full_name', 'Rohan Kadam', 'role', 'driver'),
            email_confirmed_at = COALESCE(email_confirmed_at, timezone('utc'::text, now())),
            updated_at = timezone('utc'::text, now())
        WHERE id = v_driver_id;
    ELSE
        INSERT INTO auth.users (
            id, instance_id, aud, role, email, encrypted_password,
            email_confirmed_at, raw_app_meta_data, raw_user_meta_data,
            created_at, updated_at, confirmation_token, email_change,
            email_change_token_new, recovery_token
        ) VALUES (
            v_driver_id, '00000000-0000-0000-0000-000000000000'::UUID, 'authenticated', 'authenticated',
            'driver@gmail.com', crypt('driver123', gen_salt('bf')),
            timezone('utc'::text, now()),
            jsonb_build_object('provider', 'email', 'providers', array['email']),
            jsonb_build_object('full_name', 'Rohan Kadam', 'role', 'driver'),
            timezone('utc'::text, now()), timezone('utc'::text, now()), '', '', '', ''
        ) ON CONFLICT (id) DO NOTHING;
    END IF;

    INSERT INTO public.profiles (id, email, full_name, phone, role, display_job_title, role_title)
    VALUES (v_driver_id, 'driver@gmail.com', 'Rohan Kadam', '+91 98111 22334', 'driver', 'Lead Route Courier', 'Delivery Partner')
    ON CONFLICT (id) DO UPDATE SET
        full_name = EXCLUDED.full_name,
        role = EXCLUDED.role,
        display_job_title = EXCLUDED.display_job_title,
        role_title = EXCLUDED.role_title;

    -- 3. SEED STAFF: staff@gmail.com / staff123
    SELECT id INTO v_existing_id FROM auth.users WHERE email = 'staff@gmail.com';
    IF v_existing_id IS NOT NULL THEN
        v_staff_id := v_existing_id;
        UPDATE auth.users
        SET encrypted_password = crypt('staff123', gen_salt('bf')),
            raw_user_meta_data = jsonb_build_object('full_name', 'Sunita Rao', 'role', 'kitchen_staff'),
            email_confirmed_at = COALESCE(email_confirmed_at, timezone('utc'::text, now())),
            updated_at = timezone('utc'::text, now())
        WHERE id = v_staff_id;
    ELSE
        INSERT INTO auth.users (
            id, instance_id, aud, role, email, encrypted_password,
            email_confirmed_at, raw_app_meta_data, raw_user_meta_data,
            created_at, updated_at, confirmation_token, email_change,
            email_change_token_new, recovery_token
        ) VALUES (
            v_staff_id, '00000000-0000-0000-0000-000000000000'::UUID, 'authenticated', 'authenticated',
            'staff@gmail.com', crypt('staff123', gen_salt('bf')),
            timezone('utc'::text, now()),
            jsonb_build_object('provider', 'email', 'providers', array['email']),
            jsonb_build_object('full_name', 'Sunita Rao', 'role', 'kitchen_staff'),
            timezone('utc'::text, now()), timezone('utc'::text, now()), '', '', '', ''
        ) ON CONFLICT (id) DO NOTHING;
    END IF;

    INSERT INTO public.profiles (id, email, full_name, phone, role, display_job_title, role_title)
    VALUES (v_staff_id, 'staff@gmail.com', 'Sunita Rao', '+91 98222 33445', 'kitchen_staff', 'Operations & Kitchen Supervisor', 'Kitchen Staff')
    ON CONFLICT (id) DO UPDATE SET
        full_name = EXCLUDED.full_name,
        role = EXCLUDED.role,
        display_job_title = EXCLUDED.display_job_title,
        role_title = EXCLUDED.role_title;

    -- 4. SEED CUSTOMER: customer@gmail.com / customer123
    SELECT id INTO v_existing_id FROM auth.users WHERE email = 'customer@gmail.com';
    IF v_existing_id IS NOT NULL THEN
        v_customer_id := v_existing_id;
        UPDATE auth.users
        SET encrypted_password = crypt('customer123', gen_salt('bf')),
            raw_user_meta_data = jsonb_build_object('full_name', 'Aarav Sharma', 'role', 'customer'),
            email_confirmed_at = COALESCE(email_confirmed_at, timezone('utc'::text, now())),
            updated_at = timezone('utc'::text, now())
        WHERE id = v_customer_id;
    ELSE
        INSERT INTO auth.users (
            id, instance_id, aud, role, email, encrypted_password,
            email_confirmed_at, raw_app_meta_data, raw_user_meta_data,
            created_at, updated_at, confirmation_token, email_change,
            email_change_token_new, recovery_token
        ) VALUES (
            v_customer_id, '00000000-0000-0000-0000-000000000000'::UUID, 'authenticated', 'authenticated',
            'customer@gmail.com', crypt('customer123', gen_salt('bf')),
            timezone('utc'::text, now()),
            jsonb_build_object('provider', 'email', 'providers', array['email']),
            jsonb_build_object('full_name', 'Aarav Sharma', 'role', 'customer'),
            timezone('utc'::text, now()), timezone('utc'::text, now()), '', '', '', ''
        ) ON CONFLICT (id) DO NOTHING;
    END IF;

    INSERT INTO public.profiles (id, email, full_name, phone, role, display_job_title, role_title)
    VALUES (v_customer_id, 'customer@gmail.com', 'Aarav Sharma', '+91 98765 43210', 'customer', 'Monthly Subscriber', 'Customer')
    ON CONFLICT (id) DO UPDATE SET
        full_name = EXCLUDED.full_name,
        role = EXCLUDED.role,
        display_job_title = EXCLUDED.display_job_title,
        role_title = EXCLUDED.role_title;

    -- 5. Default Branch seeded for Central Kitchen (linked to admin)
    INSERT INTO public.branches (id, owner_id, name, code, address, city, state, pincode, phone, latitude, longitude, coverage_radius_km)
    VALUES (v_branch_id, v_admin_id, 'Main Central Kitchen', 'MCK-01', 'Plot 12, Industrial Area, Powai', 'Mumbai', 'Maharashtra', '400076', '+91 98765 43210', 19.1663000, 72.9986000, 15.0)
    ON CONFLICT (code) DO UPDATE SET
        owner_id = EXCLUDED.owner_id,
        name = EXCLUDED.name;

END $$;

