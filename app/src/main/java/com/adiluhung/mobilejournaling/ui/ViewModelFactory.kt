package com.adiluhung.mobilejournaling.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.adiluhung.mobilejournaling.di.Injection
import com.adiluhung.mobilejournaling.ui.screen.authed.accountInfo.AccountInfoViewModel
import com.adiluhung.mobilejournaling.ui.screen.authed.advancedListMood.AdvancedListMoodViewModel
import com.adiluhung.mobilejournaling.ui.screen.authed.detailProgram.DetailProgramViewModel
import com.adiluhung.mobilejournaling.ui.screen.authed.detailSession.DetailSessionViewModel
import com.adiluhung.mobilejournaling.ui.screen.authed.home.HomeViewModel
import com.adiluhung.mobilejournaling.ui.screen.authed.listFavoriteProgram.ListFavoriteViewModel
import com.adiluhung.mobilejournaling.ui.screen.authed.listMood.ListMoodViewModel
import com.adiluhung.mobilejournaling.ui.screen.authed.listProgram.ListProgramViewModel
import com.adiluhung.mobilejournaling.ui.screen.authed.moodAddNote.MoodAddNoteViewModel
import com.adiluhung.mobilejournaling.ui.screen.authed.moodCheckIn.MoodCheckInViewModel
import com.adiluhung.mobilejournaling.ui.screen.authed.personalInfo.PersonalInfoViewModel
import com.adiluhung.mobilejournaling.ui.screen.authed.profile.ProfileViewModel
import com.adiluhung.mobilejournaling.ui.screen.authed.reminder.ReminderViewModel
import com.adiluhung.mobilejournaling.ui.screen.authed.completeProfile.CompleteProfileViewModel
import com.adiluhung.mobilejournaling.ui.screen.guest.AuthViewModel

class ViewModelFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {

   @Suppress("UNCHECKED_CAST")
   override fun <T : ViewModel> create(modelClass: Class<T>): T {
      if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
         return MainViewModel(Injection.provideUserPreference(context)) as T
      }
      if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
         return AuthViewModel(Injection.provideUserPreference(context)) as T
      }
      if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
         return ProfileViewModel(Injection.provideUserPreference(context)) as T
      }
      if (modelClass.isAssignableFrom(CompleteProfileViewModel::class.java)) {
         return CompleteProfileViewModel(Injection.provideUserPreference(context)) as T
      }
      if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
         return HomeViewModel(Injection.provideUserPreference(context)) as T
      }
      if (modelClass.isAssignableFrom(DetailProgramViewModel::class.java)) {
         return DetailProgramViewModel(Injection.provideUserPreference(context)) as T
      }
      if (modelClass.isAssignableFrom(DetailSessionViewModel::class.java)) {
         return DetailSessionViewModel(Injection.provideUserPreference(context)) as T
      }
      if (modelClass.isAssignableFrom(ListProgramViewModel::class.java)) {
         return ListProgramViewModel(Injection.provideUserPreference(context)) as T
      }
      if (modelClass.isAssignableFrom(MoodCheckInViewModel::class.java)) {
         return MoodCheckInViewModel(Injection.provideUserPreference(context)) as T
      }
      if (modelClass.isAssignableFrom(MoodAddNoteViewModel::class.java)) {
         return MoodAddNoteViewModel(Injection.provideUserPreference(context)) as T
      }
      if (modelClass.isAssignableFrom(ListMoodViewModel::class.java)) {
         return ListMoodViewModel(Injection.provideUserPreference(context)) as T
      }
      if (modelClass.isAssignableFrom(AdvancedListMoodViewModel::class.java)) {
         return AdvancedListMoodViewModel(Injection.provideUserPreference(context)) as T
      }
      if (modelClass.isAssignableFrom(ListFavoriteViewModel::class.java)) {
         return ListFavoriteViewModel(Injection.provideUserPreference(context)) as T
      }
      if (modelClass.isAssignableFrom(PersonalInfoViewModel::class.java)) {
         return PersonalInfoViewModel(Injection.provideUserPreference(context)) as T
      }
      if (modelClass.isAssignableFrom(AccountInfoViewModel::class.java)) {
         return AccountInfoViewModel(Injection.provideUserPreference(context)) as T
      }
      if (modelClass.isAssignableFrom(ReminderViewModel::class.java)) {
         return ReminderViewModel(Injection.provideUserPreference(context)) as T
      }
      throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
   }
}