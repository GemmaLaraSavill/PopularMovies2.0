package com.gemma.popularmovies.data

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.gemma.popularmovies.domain.repository.DataRefreshManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Checks if a data refresh is needed
 */
class DataRefreshManagerImpl @Inject constructor(context: Context): DataRefreshManager {
    
    private val daysToNextDataRefresh:Long = 7
    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val keyLastRefreshDateMs = "lastRefreshDateMs"

    override fun checkIfRefreshNeeded(): Boolean {

        val dataRefreshIntervalMs = TimeUnit.DAYS.toMillis(daysToNextDataRefresh)
        val refreshInterval = TimeUnit.MILLISECONDS.convert(dataRefreshIntervalMs, TimeUnit.DAYS)

        val lastRefreshDateInMs: Long = sharedPreferences.getLong(keyLastRefreshDateMs, 0)

        var differenceMs: Long = refreshInterval + 10
        if (lastRefreshDateInMs > 0) {
            differenceMs = System.currentTimeMillis() - lastRefreshDateInMs
        }

        return if (differenceMs > refreshInterval) {
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putLong(keyLastRefreshDateMs, System.currentTimeMillis())
            editor.apply()
            true
        } else {
            false
        }
    }

}
