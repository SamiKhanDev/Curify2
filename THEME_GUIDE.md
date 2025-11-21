# Curify Theme System Guide

## Overview

The Curify app now has a complete Material 3 theme system that supports both **Light Mode** and **Dark Mode** with proper contrast ratios and automatic color adaptation.

## Color System

### Light Mode Colors

**Primary Colors:**
- Primary: `#0B84FF` (Blue)
- Primary Variant: `#0066CC` (Darker Blue)
- Primary Container: `#D6E7FF` (Light Blue Background)
- On Primary: `#FFFFFF` (White text on primary)

**Background & Surface:**
- Background: `#F7F9FB` (Light Gray)
- Surface: `#FFFFFF` (White)
- Surface Variant: `#F1F3F5` (Slightly darker gray)

**Text Colors:**
- On Background: `#1A1A1A` (Dark text)
- On Surface: `#1A1A1A` (Dark text)
- On Surface Variant: `#6B7280` (Medium gray for secondary text)

**Semantic Colors:**
- Success: `#00A676` (Green)
- Warning: `#FFB020` (Orange)
- Error: `#E53935` (Red)

### Dark Mode Colors

**Primary Colors:**
- Primary: `#4DA3FF` (Lighter Blue for better visibility)
- Primary Variant: `#0066CC` (Same as light)
- Primary Container: `#003258` (Dark Blue Background)
- On Primary: `#001D35` (Dark text on primary)

**Background & Surface:**
- Background: `#0D0D0D` (Near Black)
- Surface: `#1A1A1A` (Dark Gray)
- Surface Variant: `#2F3133` (Lighter Dark Gray)

**Text Colors (High Contrast):**
- On Background: `#EDEDED` (Light text)
- On Surface: `#EDEDED` (Light text)
- On Surface Variant: `#B0B0B0` (Medium gray for secondary text)

**Semantic Colors:**
- Success: `#4DD4A6` (Lighter Green)
- Warning: `#FFC947` (Lighter Orange)
- Error: `#FF6B6B` (Lighter Red)

## Theme Attributes

Always use theme attributes instead of hardcoded colors. Here's the mapping:

### Text Colors
```xml
<!-- Primary text (headings, important content) -->
android:textColor="?attr/colorOnSurface"

<!-- Secondary text (subtitles, descriptions) -->
android:textColor="?attr/colorOnSurfaceVariant"

<!-- Text on primary-colored backgrounds -->
android:textColor="?attr/colorOnPrimary"

<!-- Text on background -->
android:textColor="?attr/colorOnBackground"
```

### Background Colors
```xml
<!-- Main background -->
android:background="?attr/colorBackground"

<!-- Card/surface background -->
android:background="?attr/colorSurface"

<!-- Variant surface (e.g., input fields) -->
android:background="?attr/colorSurfaceVariant"
```

### Component Colors
```xml
<!-- Primary button background -->
app:backgroundTint="?attr/colorPrimary"

<!-- Primary button text -->
android:textColor="?attr/colorOnPrimary"

<!-- Outlined button stroke -->
app:strokeColor="?attr/colorOutline"

<!-- Card background -->
app:cardBackgroundColor="?attr/colorSurface"

<!-- Divider -->
android:background="?attr/colorOutlineVariant"
```

## Usage Examples

### ✅ CORRECT: Using Theme Attributes

```xml
<TextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Title"
    android:textAppearance="?attr/textAppearanceHeadline2"
    android:textColor="?attr/colorOnSurface" />

<com.google.android.material.button.MaterialButton
    style="@style/Widget.Curify.Button"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="Click Me"
    android:textColor="?attr/colorOnPrimary"
    app:backgroundTint="?attr/colorPrimary" />

<com.google.android.material.card.MaterialCardView
    style="@style/Widget.Curify.Card"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:cardBackgroundColor="?attr/colorSurface">
    
    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Card Content"
        android:textColor="?attr/colorOnSurface" />
</com.google.android.material.card.MaterialCardView>
```

### ❌ WRONG: Using Hardcoded Colors

```xml
<!-- DON'T DO THIS -->
<TextView
    android:textColor="@color/text_primary" />

<MaterialButton
    app:backgroundTint="@color/primary" />

<MaterialCardView
    app:cardBackgroundColor="@color/surface" />
```

## Component Styles

### Buttons

**Primary Button:**
```xml
<com.google.android.material.button.MaterialButton
    style="@style/Widget.Curify.Button"
    android:text="Primary" />
```

**Outlined Button:**
```xml
<com.google.android.material.button.MaterialButton
    style="@style/Widget.Curify.Button.Outlined"
    android:text="Outlined" />
```

### Text Inputs

```xml
<com.google.android.material.textfield.TextInputLayout
    style="@style/Widget.Curify.TextInputLayout"
    android:hint="Enter text">
    
    <com.google.android.material.textfield.TextInputEditText
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textColor="?attr/colorOnSurface" />
</com.google.android.material.textfield.TextInputLayout>
```

### Cards

```xml
<com.google.android.material.card.MaterialCardView
    style="@style/Widget.Curify.Card"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:cardBackgroundColor="?attr/colorSurface">
    <!-- Card content -->
</com.google.android.material.card.MaterialCardView>
```

### Toolbars

```xml
<com.google.android.material.appbar.MaterialToolbar
    android:layout_width="match_parent"
    android:layout_height="?attr/actionBarSize"
    app:title="Title"
    app:titleTextColor="?attr/colorOnSurface" />
```

## Dark Mode Behavior

When the device is in dark mode:

1. **Backgrounds automatically switch:**
   - Light: `#F7F9FB` → Dark: `#0D0D0D`
   - Light: `#FFFFFF` → Dark: `#1A1A1A`

2. **Text colors automatically switch:**
   - Light: `#1A1A1A` (dark text) → Dark: `#EDEDED` (light text)
   - Light: `#6B7280` (gray) → Dark: `#B0B0B0` (lighter gray)

3. **Primary colors adjust:**
   - Light: `#0B84FF` → Dark: `#4DA3FF` (lighter for visibility)

4. **All Material Components adapt automatically** when using theme attributes

## Testing

### How to Test Dark Mode

1. **On Emulator/Device:**
   - Settings → Display → Dark theme (or similar)
   - Or use Quick Settings toggle

2. **In Android Studio:**
   - Run app → Change device theme
   - Or use "Force Dark" in Developer Options

### What to Check

- ✅ All text is readable (no invisible text)
- ✅ Buttons have proper contrast
- ✅ Cards are visible against background
- ✅ Input fields are clearly visible
- ✅ Icons are visible
- ✅ Status bar and navigation bar colors are correct

## Migration Checklist

When updating existing layouts:

- [ ] Replace `@color/text_primary` → `?attr/colorOnSurface`
- [ ] Replace `@color/text_secondary` → `?attr/colorOnSurfaceVariant`
- [ ] Replace `@color/primary` → `?attr/colorPrimary`
- [ ] Replace `@color/surface` → `?attr/colorSurface`
- [ ] Replace `@color/background` → `?attr/colorBackground`
- [ ] Replace `@color/divider` → `?attr/colorOutlineVariant`
- [ ] Use component styles (`@style/Widget.Curify.*`) instead of inline styles
- [ ] Test in both light and dark mode

## Files Reference

- **Light Mode Colors:** `app/src/main/res/values/colors.xml`
- **Dark Mode Colors:** `app/src/main/res/values-night/colors.xml`
- **Light Mode Theme:** `app/src/main/res/values/themes.xml`
- **Dark Mode Theme:** `app/src/main/res/values-night/themes.xml`
- **Example Layout:** `app/src/main/res/layout/example_theme_usage.xml`

## Support

If you encounter invisible text or poor contrast:

1. Check if you're using theme attributes (`?attr/...`)
2. Verify the component is using a Material 3 style
3. Test in both light and dark mode
4. Check the example layout file for reference

---

**Remember:** Always use theme attributes (`?attr/...`) instead of hardcoded colors (`@color/...`) for automatic dark mode support!

