package com.adiluhung.mobilejournaling.ui.constants

import com.adiluhung.mobilejournaling.R

// List of Mood Enum
enum class ListMood(val id: Int, val mood: String, val icon: Int) {
   SENANG(1, "Senang", R.drawable.mood_senang),
   BERSEMANGAT(2, "Bersemangat", R.drawable.mood_bersemangat),
   BERSYUKUR(3, "Bersyukur", R.drawable.mood_bersyukur),
   TENANG(4, "Tenang", R.drawable.mood_tenang),
   LEGA(5, "Lega", R.drawable.mood_lega),
   LELAH(6, "Lelah", R.drawable.mood_lelah),
   RAGU(7, "Ragu", R.drawable.mood_ragu),
   BOSAN(8, "Bosan", R.drawable.mood_bosan),
   GELISAH(9, "Gelisah", R.drawable.mood_gelisah),
   MARAH(10, "Marah", R.drawable.mood_marah),
   STRESS(11, "Stress", R.drawable.mood_stress),
   SEDIH(12, "Sedih", R.drawable.mood_sedih),

}