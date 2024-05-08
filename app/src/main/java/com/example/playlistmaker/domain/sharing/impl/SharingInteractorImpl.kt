package com.example.playlistmaker.domain.sharing.impl

import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.SharingRepository
import com.example.playlistmaker.domain.sharing.models.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val sharingRepository: SharingRepository,
): SharingInteractor {

    override fun sharingApp() {
        return externalNavigator.shareLink(getShareAppLink())
    }

    override fun openTerms() {
        return externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        return externalNavigator.sendEmail(getSupportEmailData())
    }

    private fun getShareAppLink(): String {
        return sharingRepository.getSharingUrl()
    }
    private fun getTermsLink(): String {
        return sharingRepository.getTermsUrl()
    }

    private fun getSupportEmailData(): EmailData {
        return sharingRepository.getEmailData()
    }
}