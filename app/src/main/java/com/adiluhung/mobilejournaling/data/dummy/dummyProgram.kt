package com.adiluhung.mobilejournaling.data.dummy

import com.adiluhung.mobilejournaling.R

// create dummy program with id, name, image(drawable) and list of sessions with id, name, duration, category)
data class Program(
   val id: Int,
   val name: String,
   val description: String,
   val modules: List<Module>,
   val image: Int
)

data class Module(
   val id: Int,
   val name: String,
   val duration: Int,
   val sessions: List<Session>
)

data class Session(
   val id: Int,
   val name: String,
   val duration: Int,
   val category: String
)

val dummyProgram = listOf(
   Program(
      id = 1,
      name = "Weight Loss Challenge",
      description = "A 12-week weight loss challenge program",
      modules = listOf(
         Module(
            id = 1,
            name = "Introduction to Nutrition",
            duration = 30,
            sessions = listOf(
               Session(1, "Understanding Macros", 30, "Nutrition"),
               Session(2, "Meal Planning", 45, "Nutrition")
            )
         ),
         Module(
            id = 2,
            name = "Cardio Workouts",
            duration = 45,
            sessions = listOf(
               Session(3, "HIIT", 45, "Cardio"),
               Session(4, "Running", 30, "Cardio")
            )
         ),
         Module(
            id = 3,
            name = "Strength Training",
            duration = 60,
            sessions = listOf(
               Session(5, "Full Body Workout", 60, "Strength"),
               Session(6, "Upper Body Focus", 60, "Strength")
            )
         )
      ),
      image = R.drawable.program1
   ),
   Program(
      id = 2,
      name = "Mindfulness Meditation Program",
      description = "A 4-week program to cultivate mindfulness",
      modules = listOf(
         Module(
            id = 1,
            name = "Basics of Meditation",
            duration = 30,
            sessions = listOf(
               Session(1, "Breath Awareness", 30, "Meditation"),
               Session(2, "Body Scan", 45, "Meditation")
            )
         ),
         Module(
            id = 2,
            name = "Deepening Practice",
            duration = 45,
            sessions = listOf(
               Session(3, "Loving-Kindness Meditation", 45, "Meditation"),
               Session(4, "Walking Meditation", 30, "Meditation")
            )
         )
      ),
      image = R.drawable.program2
   ),
   Program(
      id = 3,
      name = "Language Learning Bootcamp",
      description = "An intensive language learning program",
      modules = listOf(
         Module(
            id = 1,
            name = "Grammar Basics",
            duration = 30,
            sessions = listOf(
               Session(1, "Verb Conjugation", 30, "Grammar"),
               Session(2, "Sentence Structure", 45, "Grammar")
            )
         ),
         Module(
            id = 2,
            name = "Vocabulary Building",
            duration = 45,
            sessions = listOf(
               Session(3, "Daily Phrases", 45, "Vocabulary"),
               Session(4, "Word Games", 30, "Vocabulary")
            )
         )
      ),
      image = R.drawable.program3
   ),
   Program(
      id = 4,
      name = "Financial Literacy Course",
      description = "A comprehensive course on personal finance",
      modules = listOf(
         Module(
            id = 1,
            name = "Budgeting Basics",
            duration = 30,
            sessions = listOf(
               Session(1, "Creating a Budget", 30, "Budgeting"),
               Session(2, "Tracking Expenses", 45, "Budgeting")
            )
         ),
         Module(
            id = 2,
            name = "Investment Strategies",
            duration = 45,
            sessions = listOf(
               Session(3, "Stock Market Basics", 45, "Investing"),
               Session(4, "Real Estate Investments", 30, "Investing")
            )
         )
      ),
      image = R.drawable.program1

   ),
   Program(
      id = 5,
      name = "Art Therapy Workshop",
      description = "Exploring creativity for self-expression",
      modules = listOf(
         Module(
            id = 1,
            name = "Introduction to Art Therapy",
            duration = 30,
            sessions = listOf(
               Session(1, "Art as Expression", 30, "Therapy"),
               Session(2, "Art Techniques", 45, "Therapy")
            )
         ),
         Module(
            id = 2,
            name = "Creative Exploration",
            duration = 45,
            sessions = listOf(
               Session(3, "Visual Journaling", 45, "Therapeutic Art"),
               Session(4, "Mixed Media Art", 30, "Therapeutic Art")
            )
         )
      ),
      image = R.drawable.program2
   )
)
