# Curify - Healthcare Android App

A modern Android healthcare application built with Kotlin, Material Design 3, and ViewBinding.

## Features

- **Authentication**: Login, Signup, Forgot Password, OTP Verification
- **Home**: Categories, Popular Medicines, Recommended Medicines
- **Medicine**: Browse, Search, Product Details
- **Cart & Checkout**: Shopping cart with payment integration
- **Doctors**: Doctor listings and appointment booking
- **Chat**: In-app messaging
- **Prescriptions**: Upload and manage prescriptions with OCR support
- **Admin Panel**: Medicine and order management

## Tech Stack

- **Language**: Kotlin
- **UI**: Material Design 3 (MDC)
- **Architecture**: MVVM with ViewBinding
- **Min SDK**: 21 (Android 5.0)
- **Target SDK**: Latest

## Design System

### Colors
- Primary: `#0B84FF`
- Primary Variant: `#0066CC`
- Success: `#00A676`
- Background: `#F7F9FB`
- Surface: `#FFFFFF`

### Typography
- Headlines: Inter (fallback: Roboto)
- Body: Roboto

### Spacing
- Base grid: 8dp
- Border radius: 8dp, 12dp, 28dp
- Touch targets: 48dp minimum

## Project Structure

```
app/src/main/
├── java/com/saim/curify/
│   ├── ui/
│   │   ├── auth/          # Authentication screens
│   │   ├── home/          # Home fragment and ViewModel
│   │   ├── medicine/      # Medicine listing and details
│   │   ├── cart/          # Shopping cart
│   │   ├── checkout/      # Checkout flow
│   │   ├── payment/       # Payment methods
│   │   ├── doctors/       # Doctor listings
│   │   ├── chat/          # Chat functionality
│   │   ├── prescription/  # Prescription upload
│   │   └── admin/         # Admin panel
│   └── MainActivity.kt
└── res/
    ├── layout/            # XML layouts
    ├── values/            # Colors, themes, strings, dimens
    └── menu/              # Navigation menus
```

## Setup Instructions

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Curify-main
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the project directory

3. **Sync Gradle**
   - Android Studio will automatically sync Gradle
   - Wait for dependencies to download

4. **Run the app**
   - Connect an Android device or start an emulator
   - Click "Run" or press Shift+F10

## Integration Points

### Payment Integration

#### JazzCash SDK
Location: `ui/payment/PaymentDetailsActivity.kt`
```kotlin
// TODO: Integrate JazzCash SDK
// 1. Add JazzCash SDK dependency to build.gradle
// 2. Initialize SDK in Application class
// 3. Implement payment flow in PaymentDetailsActivity
```

#### EasyPaisa SDK
Location: `ui/payment/PaymentDetailsActivity.kt`
```kotlin
// TODO: Integrate EasyPaisa SDK
// 1. Add EasyPaisa SDK dependency
// 2. Initialize SDK
// 3. Implement payment flow
```

#### Card Payment Gateway
Location: `ui/payment/PaymentMethodsActivity.kt`
```kotlin
// TODO: Integrate Card payment gateway (Stripe/PayPal)
// 1. Add payment gateway SDK
// 2. Implement card payment flow
```

### OCR Integration (Google ML Kit)

Location: `ui/prescription/UploadPrescriptionActivity.kt`
```kotlin
// TODO: Integrate Google ML Kit for OCR
// 1. Add ML Kit Text Recognition dependency
// 2. Process prescription image
// 3. Extract text from prescription
// 4. Parse medicine names and dosages
```

### OTP Verification

Location: `ui/auth/OtpVerificationActivity.kt`
```kotlin
// TODO: Integrate OTP verification service
// 1. Add SMS Retriever API for auto-fill
// 2. Implement OTP verification API call
// 3. Add countdown timer for resend
```

### API Integration

All ViewModels contain TODO comments for API integration:
- `HomeViewModel.kt` - Home data API
- `MedicineListViewModel.kt` - Medicine listing API
- `CartViewModel.kt` - Cart management API
- And more...

## Development Notes

### ViewBinding
All activities and fragments use ViewBinding for type-safe view access:
```kotlin
private lateinit var binding: ActivityLoginBinding
binding = ActivityLoginBinding.inflate(layoutInflater)
```

### ViewModels
ViewModels follow MVVM pattern with LiveData:
```kotlin
private val _data = MutableLiveData<List<Item>>()
val data: LiveData<List<Item>> = _data
```

### Mock Data
Currently, ViewModels use mock data. Replace with actual API calls:
```kotlin
// TODO: Replace with API call
_data.value = getMockData()
```

## Accessibility

- All ImageViews have `contentDescription` attributes
- Touch targets meet minimum 48dp requirement
- Text sizes follow Material Design guidelines
- RTL support enabled in manifest

## RTL Support

The app supports RTL layouts:
- `android:supportsRtl="true"` in manifest
- Layouts use `start`/`end` instead of `left`/`right`
- Strings have Urdu placeholders in `values-ur/strings.xml`

## Future Enhancements

- [ ] Skeleton loaders for loading states
- [ ] Pull-to-refresh functionality
- [ ] Image caching with Glide/Coil
- [ ] Offline support with Room database
- [ ] Push notifications
- [ ] Dark theme support
- [ ] Unit tests
- [ ] UI tests

## Contributing

1. Create a feature branch
2. Make your changes
3. Ensure code follows Kotlin style guide
4. Test on multiple devices/emulators
5. Submit a pull request

## License

[Add your license here]

## Contact

[Add contact information]

