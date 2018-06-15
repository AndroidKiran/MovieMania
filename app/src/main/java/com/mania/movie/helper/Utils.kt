package com.mania.movie.helper

object Utils {

    const val LOGGED_IN = "logged_in"

    const val PHOTO_URL = "photo_url"

    const val TIMEOUT_IN_SEC: Long = 15

    const val NOT_AVAILBLE = "n/a"

    @JvmStatic
    fun isLoggedIn(preferenceHelper: PreferenceHelper) = preferenceHelper.getBooleanPreference(LOGGED_IN, false)

    @JvmStatic
    fun getPhotoUrl(preferenceHelper: PreferenceHelper) = preferenceHelper.getStringPreference(PHOTO_URL, "")

    @JvmStatic
    fun isStringNullOrEmpty(string: String?) = string?.isEmpty() ?: true

    @JvmStatic
    fun isListNullOrEmpty(list: List<*>?) = list?.isEmpty() ?: true

    @JvmStatic
    fun isStringNotAvailable(string: String?) = string?.isEmpty()!! || (NOT_AVAILBLE == string.toLowerCase())

}