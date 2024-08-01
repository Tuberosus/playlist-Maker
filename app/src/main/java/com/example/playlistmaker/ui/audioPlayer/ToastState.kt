package com.example.playlistmaker.ui.audioPlayer

sealed interface ToastState {
    data object None: ToastState
    data class ShowSuccess(val text: String): ToastState
    data class ShowError(val text: String): ToastState
}