plugins {
   id("com.android.application")
   id("org.jetbrains.kotlin.android")
   // id("com.google.devtools.ksp")
}

android {
   namespace = "com.adiluhung.mobilejournaling"
   compileSdk = 34

   defaultConfig {
      applicationId = "com.adiluhung.mobilejournaling"
      minSdk = 29
      targetSdk = 34
      versionCode = 1
      versionName = "1.0"

      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
      vectorDrawables {
         useSupportLibrary = true
      }

      buildConfigField("String", "BASE_URL", "\"https://meditate-new.apps.webku.xyz/api/\"")
   }

   buildTypes {
      release {
         isMinifyEnabled = false
         proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
         )
      }
   }
   compileOptions {
      sourceCompatibility = JavaVersion.VERSION_1_8
      targetCompatibility = JavaVersion.VERSION_1_8
   }
   kotlinOptions {
      jvmTarget = "1.8"
   }
   buildFeatures {
      compose = true
      buildConfig = true
   }
   composeOptions {
      kotlinCompilerExtensionVersion = "1.5.1"
   }
   packaging {
      resources {
         excludes += "/META-INF/{AL2.0,LGPL2.1}"
      }
   }
}

dependencies {

   implementation("androidx.core:core-ktx:1.10.1")
   implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
   implementation("androidx.activity:activity-compose:1.7.0")
   implementation(platform("androidx.compose:compose-bom:2023.08.00"))
   implementation("androidx.compose.ui:ui")
   implementation("androidx.compose.ui:ui-graphics")
   implementation("androidx.compose.ui:ui-tooling-preview")
   implementation("androidx.compose.material3:material3")
   testImplementation("junit:junit:4.13.2")
   androidTestImplementation("androidx.test.ext:junit:1.1.5")
   androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
   androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
   androidTestImplementation("androidx.compose.ui:ui-test-junit4")
   debugImplementation("androidx.compose.ui:ui-tooling")
   debugImplementation("androidx.compose.ui:ui-test-manifest")

   // Data Store
   implementation("androidx.datastore:datastore-preferences:1.0.0")

   // Livedata
   implementation("androidx.compose.runtime:runtime-livedata:1.6.0")
   implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

   // Compose Navigation
   implementation("androidx.navigation:navigation-compose:2.7.6")
   implementation("androidx.navigation:navigation-runtime-ktx:2.7.6")

   // Retrofit
   implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")
   implementation("com.squareup.retrofit2:retrofit:2.9.0")
   implementation("com.squareup.retrofit2:converter-gson:2.9.0")

   // Image
   implementation("io.coil-kt:coil-compose:2.5.0")

   // Constraint Layout
   implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

   // ExoPlayer
   implementation("com.google.android.exoplayer:exoplayer:2.19.1")

   // permission lib. must match with compose version
   implementation("com.google.accompanist:accompanist-permissions:0.25.1")

   // Room
   // implementation ("androidx.room:room-runtime:$2.5.1")
   // implementation( "androidx.room:room-ktx:$2.5.1")

   // Permission
   implementation("com.google.accompanist:accompanist-permissions:0.25.1")

   // System UI Controller
   implementation("androidx.media3:media3-exoplayer:1.2.1")
}