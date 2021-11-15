package com.gemma.popularmovies.data

import com.gemma.popularmovies.domain.repository.DataRefreshManager

class FakeDataRefreshManager: DataRefreshManager {

    override fun checkIfRefreshNeeded(): Boolean {
        return false
    }

}
