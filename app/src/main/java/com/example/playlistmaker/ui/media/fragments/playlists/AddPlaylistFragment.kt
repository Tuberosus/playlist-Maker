package com.example.playlistmaker.ui.media.fragments.playlists

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAddPlaylistBinding
import com.example.playlistmaker.ui.media.view_model.AddPlaylistViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

open class AddPlaylistFragment : Fragment() {

    open val viewModel by viewModel<AddPlaylistViewModel>()

    private var _binding: FragmentAddPlaylistBinding? = null
    private val binding get() = _binding!!

    private var isAbleToClose = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // обработка нажатия системной кнопки назад
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    closeCurrentFragment()
                }
            })

        // обрботка кнопки назад в header panel
        binding.backArrow.setOnClickListener {
            closeCurrentFragment()
        }

        binding.nameInputEditText.doOnTextChanged { text, start, before, count ->
            binding.buttonSave.isEnabled = !text.isNullOrEmpty()
            isAbleToClose = text.isNullOrEmpty()
            viewModel.playlistName = text.toString()
        }

        binding.descriptionInputEditText.doOnTextChanged { text, start, before, count ->
            isAbleToClose = text.isNullOrEmpty()
            viewModel.playlistDescription = text.toString()
        }

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    viewModel.imageUri = uri
                    isAbleToClose = false
                    binding.addPhoto.setImageURI(uri)
                }
            }

        binding.addPhoto.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.buttonSave.setOnClickListener {
            lifecycleScope.launch {
                launch(Dispatchers.IO) {
                    viewModel.savePlaylist()
                    Log.d("MyTag", "save scope")
                }.join()
                showSaveToast(viewModel.playlistName!!)
                Log.d("MyTag", "save done")
                findNavController().popBackStack()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun closeCurrentFragment() {
        val confirmDialog = MaterialAlertDialogBuilder(requireContext(), R.style.PopUpDialog)
            .setTitle(R.string.confirm_dialog_title)
            .setMessage(R.string.confirm_dialog_message)
            .setNeutralButton(R.string.confirm_dialog_cancel) { dialog, which ->
                // ничего не делаем
            }.setPositiveButton(R.string.confirm_dialog_done) { dialog, which ->
                // выходим
                findNavController().popBackStack()
            }
        if (isAbleToClose) {
            findNavController().popBackStack()
        } else {
            confirmDialog.show()
        }
    }

    private fun showSaveToast(name: String) {
        val toastText = getString(R.string.toast_text, name)
        Toast.makeText(requireContext(), toastText, Toast.LENGTH_SHORT).show()
    }

}