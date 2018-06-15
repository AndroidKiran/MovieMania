package com.mania.movie.main.authentication.presenter

import android.databinding.ObservableBoolean
import com.mania.movie.MovieManiaApplication
import com.mania.movie.helper.PreferenceHelper
import com.mania.movie.helper.Utils
import com.mania.movie.helper.Utils.LOGGED_IN
import com.mania.movie.helper.Utils.PHOTO_URL
import com.mania.movie.main.base.BaseViewModel
import javax.inject.Inject

class AuthenticationViewModel @Inject constructor (application: MovieManiaApplication, private val preferenceHelper: PreferenceHelper): BaseViewModel(application) {

    var isLoggedIn = ObservableBoolean(Utils.isLoggedIn(preferenceHelper))

    fun persistLoginState(boolean: Boolean) {
        preferenceHelper.set(LOGGED_IN, boolean)
    }

    fun persistUrlPhoto(url: String) {
        preferenceHelper.set(PHOTO_URL, url)
    }
}