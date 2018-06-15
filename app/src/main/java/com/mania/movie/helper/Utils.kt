package com.mania.movie.helper

object Utils {

    const val LOGGED_IN = "logged_in"

    const val PHOTO_URL = "photo_url"

    const val TIMEOUT_IN_SEC: Long = 15

    const val ACCOUNT_ID = "account_id"

    private const val NOT_AVAILABLE = "n/a"

    @JvmStatic
    fun isLoggedIn(preferenceHelper: PreferenceHelper) = preferenceHelper.getBooleanPreference(LOGGED_IN, false)

    @JvmStatic
    fun getPhotoUrl(preferenceHelper: PreferenceHelper) = preferenceHelper.getStringPreference(PHOTO_URL, "")

    @JvmStatic
    fun getUserID(preferenceHelper: PreferenceHelper) = preferenceHelper.getStringPreference(ACCOUNT_ID, "")

    @JvmStatic
    fun isStringNullOrEmpty(string: String?) = string?.isEmpty() ?: true

    @JvmStatic
    fun isListNullOrEmpty(list: List<*>?) = list?.isEmpty() ?: true

    @JvmStatic
    fun isStringNotAvailable(string: String?) = string?.isEmpty()!! || (NOT_AVAILABLE == string.toLowerCase())

}