---
name: Tiffin Manager Mobile
colors:
  surface: '#f7f9fd'
  surface-dim: '#d8dade'
  surface-bright: '#f7f9fd'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f2f4f8'
  surface-container: '#eceef2'
  surface-container-high: '#e6e8ec'
  surface-container-highest: '#e0e3e6'
  on-surface: '#191c1f'
  on-surface-variant: '#5b4139'
  inverse-surface: '#2d3134'
  inverse-on-surface: '#eff1f5'
  outline: '#8f7067'
  outline-variant: '#e4beb4'
  surface-tint: '#af3100'
  primary: '#ab2f00'
  on-primary: '#ffffff'
  primary-container: '#d53e02'
  on-primary-container: '#fffbff'
  inverse-primary: '#ffb59f'
  secondary: '#545f73'
  on-secondary: '#ffffff'
  secondary-container: '#d5e0f8'
  on-secondary-container: '#586377'
  tertiary: '#006947'
  on-tertiary: '#ffffff'
  tertiary-container: '#00855b'
  on-tertiary-container: '#f5fff6'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#ffdbd1'
  primary-fixed-dim: '#ffb59f'
  on-primary-fixed: '#3a0a00'
  on-primary-fixed-variant: '#862300'
  secondary-fixed: '#d8e3fb'
  secondary-fixed-dim: '#bcc7de'
  on-secondary-fixed: '#111c2d'
  on-secondary-fixed-variant: '#3c475a'
  tertiary-fixed: '#6ffbbe'
  tertiary-fixed-dim: '#4edea3'
  on-tertiary-fixed: '#002113'
  on-tertiary-fixed-variant: '#005236'
  background: '#f7f9fd'
  on-background: '#191c1f'
  surface-variant: '#e0e3e6'
typography:
  headline-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 28px
    fontWeight: '700'
    lineHeight: 34px
  headline-lg-mobile:
    fontFamily: Plus Jakarta Sans
    fontSize: 24px
    fontWeight: '700'
    lineHeight: 30px
  headline-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 26px
  headline-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 24px
  body-lg:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  body-sm:
    fontFamily: Inter
    fontSize: 13px
    fontWeight: '400'
    lineHeight: 18px
  label-lg:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 18px
    letterSpacing: 0.1px
  label-md:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '600'
    lineHeight: 16px
    letterSpacing: 0.2px
  label-sm:
    fontFamily: Inter
    fontSize: 11px
    fontWeight: '500'
    lineHeight: 14px
    letterSpacing: 0.3px
  financial-counter:
    fontFamily: Plus Jakarta Sans
    fontSize: 22px
    fontWeight: '700'
    lineHeight: 26px
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  space-2xs: 0.25rem
  space-xs: 0.5rem
  space-sm: 0.75rem
  space-md: 1rem
  space-lg: 1.25rem
  space-xl: 1.5rem
  space-2xl: 2rem
  screen-margin-mobile: 1rem
  screen-margin-tablet: 1.5rem
  card-padding-mobile: 0.875rem
  touch-target-min: 2.75rem
---

## Brand & Style

The design system establishes an agile, high-utility operational mobile companion for cloud kitchens, meal subscription providers, and home chefs managing daily tiffin logistics. It strikes a balance between professional financial reliability and the appetizing warmth of culinary craft.

The brand personality is dependable, brisk, and approachable. Operators deal with split-second kitchen dispatching, morning delivery route packing, dietary compliance, and recurring billing. The visual language blends Modern Utility with Warm Culinary Minimalism:
- **Crisp Neutral Foundations:** Snow-white modular cards elevated gently over cool slate-gray canvas backgrounds to maximize scan speed in intense, fast-moving kitchen environments.
- **Vibrant Culinary Accents:** High-energy spice orange serves as the focal interaction driver, balanced by authoritative slate navy for financial telemetry and structural metadata.
- **Tactile Dietary & Status Anchoring:** Dedicated visual patterns for quick meal verification (classic emerald green dot veg indicators, deep crimson non-veg markers, and status badges).
- **Cross-Platform Parity:** Follows unified touch target ergonomics and visual clarity across iOS and Android without feeling strictly generic to either.

## Colors

The palette leverages contrast to partition culinary actions, financial values, and meal categorization.

### Core Swatches
- **Primary Spice Orange (`#f95721`):** Represents appetite, heat, and immediate action. Used for primary CTAs, active dispatch tabs, active navigation nodes, and high-priority action alerts.
- **Secondary Slate Navy (`#1e293b`):** Delivers grounded authority for primary typography, mobile top app bar headers, navigation icons, and structural divider surfaces.
- **Tertiary Kitchen Mint (`#10b981`):** Conveys healthy fulfillment, active customer accounts, positive wallet balances, and vegetarian dietary compliance.
- **Neutral Canvas Slate (`#f4f6fa`):** A soft, cool gray-slate foundation that reduces eye strain under harsh kitchen lighting while allowing white cards to float naturally.

### Functional & Semantic Accents
- **Non-Veg / Alert Crimson (`#ef4444`):** Dedicated to non-vegetarian dietary markers, low-wallet balance warnings, missed deliveries, and critical debt notices.
- **Prepping Amber (`#f59e0b`):** Indicates pending kitchen prep, route in-transit status, and mid-tier subscription skips.
- **Card Background Surface (`#ffffff`):** Pure white container surfaces for clean edge definition and crisp touch boundaries.
- **Border & Subtle Hairline (`#e2e8f0`):** Ultra-subtle borders defining tabular lists, customer rows, and form fields.

## Typography

The typographic hierarchy solves for readability in movement. **Plus Jakarta Sans** provides open, friendly, geometric clarity for high-level summaries, wallet counts, customer names, and section titles. **Inter** handles dense alphanumeric data—phone numbers, item counts, calendar dates, meal plan tags, and operational logs.

### Numeric & Currency Legibility
Currency figures (such as `₹3,000.00`) and countdown tallies are rendered using tabular numbers (`tnum`) in Plus Jakarta Sans to prevent jitter during state updates and maintain vertical alignment across ledger lists.

### Scaling & Line Truncation
- `headline-lg-mobile` anchors standard mobile headers, collapsing to `headline-md` when sticky scroll behaviors trigger top app bar compression.
- Body and label styles mandate a strict 1-line truncation with ellipsis for customer addresses and dietary allergy specifications within summary rows.

## Elevation & Depth

Visual hierarchy uses clean tonal layering paired with soft ambient shadows to keep the interface crisp, lightweight, and modern without heavy visual noise.

### Layer Hierarchy
- **Canvas Base (Level 0):** Hex `#f4f6fa`. Completely flat background holding the entire viewport canvas.
- **Card Surfaces (Level 1):** Hex `#ffffff`. White surface cards, customer rows, kitchen aggregate boards. Layered with `0 1px 3px rgba(30, 41, 59, 0.05), 0 1px 2px rgba(30, 41, 59, 0.03)` and bounded by a razor-thin border of `#e2e8f0` (0.75px).
- **Floating Controls & Modals (Level 2):** Elevated floating action buttons (FABs), bottom action sheets, date pickers, and sticky order dispatch bars. Uses ambient warm-tinted shadow `0 10px 25px -5px rgba(30, 41, 59, 0.12), 0 8px 10px -6px rgba(249, 87, 33, 0.06)`.
- **Top App Bar & Bottom Navigation:** Level 1 elevation with subtle top or bottom edge separator (`#e2e8f0`). When scrolling, the top bar gains a 4px frosted ambient glow (`backdrop-filter: blur(12px)` over 92% opaque white).

## Shapes

The design uses balanced, modern rounded geometries (Level 2). Containers and interactive components utilize soft radii that feel friendly yet structurally clean.

### Shape Scale Rules
- **Micro UI & Badges:** Full pill geometry (`rounded-full` / 9999px) for dietary tags (Veg, Non-Veg), subscription status badges (Active, Paused, Expired), and day-of-the-week bubbles (`S M T W T F S`).
- **Cards & Data Panels:** Moderate smooth corners (`rounded-lg` / 16px) to maintain structured perimeter boundaries while keeping card content feeling modern and approachable.
- **Buttons & Inputs:** Standardized at 10px radius (`rounded-md` to 12px) for consistent form ergonomics.
- **Bottom Navigation Dock:** 0px bottom flush to safe area with 16px soft inward bevels on top corners or a complete floating pill container depending on device constraints.

## Components

### Buttons
- **Primary CTA:** Background `#f95721`, text `#ffffff`, semi-bold Plus Jakarta Sans, 44px min height. Subtle active press scale (0.98x).
- **Secondary / Outline:** 1.5px solid border in `#e2e8f0` or `#1e293b`, transparent background, text `#1e293b`.
- **Tonal Icon Actions:** Circular 36px buttons (Call, WhatsApp, Edit, Print) with `#f8fafc` background and `#64748b` icon color, transitioning to primary tint on tap.

### Badges & Dietary Chips
- **Vegetarian Indicator:** White mini-square (14x14px) with crisp 1px green border (`#10b981`), housing an centered 6px green filled circle.
- **Non-Vegetarian Indicator:** Matching white square with crimson border (`#ef4444`) and crimson circle.
- **Status Pills:** 
  - *Active:* Light mint background (`#d1fae5`), deep green text (`#065f46`).
  - *Pending / Paused:* Amber tint (`#fef3c7`), dark amber text (`#92400e`).
  - *Due / Negative Wallet:* Soft coral tint (`#fee2e2`), bold red text (`#b91c1c`).

### Mobile Top App Bar
- Fixed 56px height.
- Left: Kitchen/Brand switcher dropdown or back caret.
- Center/Left Title: Screen identity in `headline-sm` Slate Navy (`#1e293b`).
- Right: Quick calendar shift trigger, notification bell with unread orange indicator badge, and staff profile avatar.

### Bottom Navigation Bar
- 5-destination architecture: **Home/Dashboard**, **Subscriptions**, **Kitchen Console**, **Deliveries**, and **More/Settings**.
- Selected Item: Active spice orange icon and text label (`#f95721`) with a subtle 4px orange indicator pill bar above or glowing background node.
- Unselected Item: Muted slate gray (`#94a3b8`) icon with label.

### Customer & Subscription List Cards
- Card container: Pure white background, 16px padding, rounded-lg.
- Header row: Customer name, active/paused pill, quick action kebab menu.
- Mid-body: Meal plan breakdown chip (`Standard Veg Lunch x 1`), weekly active day pills (`S M T W T F S` with active days filled in slate navy or spice orange).
- Footer summary: Wallet balance on left (green for positive `₹3,000.00`, red for debit), action buttons on right (Quick Edit, Print Label).

### Kitchen Console Aggregator
- Top Segmented Switcher: Meal slots (`Breakfast (12)`, `Lunch (84)`, `Dinner (45)`) as prominent rounded sliding pills.
- Summary counter blocks: High-contrast metric cards highlighting aggregate rotis, sabzi portions, dal liters, and special dietary requests.