package com.example.playlistmaker.mediateka.ui.updatePlaylist

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.mediateka.domain.Playlist
import com.example.playlistmaker.mediateka.ui.newPlaylist.NewPlaylistFragment
import com.example.playlistmaker.mediateka.ui.newPlaylist.NewPlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class UpdatePlaylistFragment : NewPlaylistFragment() {

    override val viewModel by viewModel<UpdatePlaylistViewModel>()

    var updatePlaylist : Playlist = Playlist(0, "", "", "", arrayListOf(),0,0)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Третий вариант передачи данных плэйлиста из DisplayPlaylistFragment
        val idPl = arguments?.getInt("idPl")
        var imagePl = arguments?.getString("imagePl")
        var namePl = arguments?.getString("namePl")
        var descriptPl = arguments?.getString("descriptPl")

        Log.d("PAN_UpdatePlaylistF", "Получено из bundle: idPl = $idPl \n namePl = ${namePl.toString()}  \n " +
                "imagePl = $imagePl \n descriptPl = ${descriptPl} selectedUri = ${selectedUri.toString()} ")

        //viewModel.initialization() // Второй вариант передачи данных плэйлиста из DisplayPlaylistFragment


        viewModel.update.observe(viewLifecycleOwner) { isUpdate ->
            if (isUpdate) findNavController().navigateUp()
        }

        viewModel.updateLiveData.observe(viewLifecycleOwner) { playlist ->
            if (playlist.name.isEmpty()) findNavController().navigateUp()
            updatePlaylist = playlist
            fillFields (playlist)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigateUp()
                }
            })

        binding.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }


        binding.tvButtonNew.isEnabled = false

        binding.allViewsLayout.setOnClickListener {
            binding.tvButtonNew.isEnabled = checkChanges()

        }

        binding.tvButtonNew.setOnClickListener {
            // запомнить старое название
            val oldNamePl = namePl

            //получить новое имя файла если есть изменения
            if (binding.etNamePl.editText!!.text.toString().isNotEmpty()) namePl = binding.etNamePl.editText!!.text.toString()
            if (selectedUri != null) {
                //удалить старый файл deletePicture(oldNamePl)
                if (oldNamePl != null) {
                    viewModel.deletePicture(oldNamePl)
                }
                // записать новый файл
                viewModel.savePicture(selectedUri, namePl !!)
                // новый путь к файлу
                imagePl = viewModel.imagePath() + "/" + namePl + ".jpg"
            } else {
                Log.d("PAN_Update", "СелектедЮрай равен нулл = $selectedUri")
                if (oldNamePl != namePl) {
                    if (oldNamePl != null) {
                        namePl?.let { it1 -> viewModel.renameCover(oldName = oldNamePl, newName = it1) }
                    }
                }
            }


            // если не выбрана новая обложка, то остается старый файл со старым названием

            Log.d("PAN_UpdatePlaylistF", "СОХРАНИТЬ: idPl = $idPl \n namePl = $namePl  \n " +
                    "imagePl = $imagePl \n descriptPl = ${descriptPl} \n selectedUri = ${selectedUri.toString()}")
            viewModel.updatePl(
                idPl,
                namePl = namePl ,
                descriptPl = binding.etDescriptPl.editText!!.text.toString())
            findNavController().navigateUp()
        }

        binding.etNamePl.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) binding.etNamePl.hint = "Название*"
            else  binding.etNamePl.hint = namePl
        }

    }
    private fun fillFields (playlist : Playlist) {
        binding.tvButtonNew.text = getString(R.string.save)
        binding.tvNewPlaylist.text = getString(R.string.edit)
        binding.ietNamePl.setText(playlist.name)
        binding.etNamePl.hint = getString(R.string.name_playlist)
        binding.etDescriptPl.hint = getString(R.string.descript_playlist)
        binding.ietDescriptPl.setText(playlist.descript)
        binding.ivPicturePlus.isVisible = false
        val radius = resources.getDimensionPixelSize(R.dimen.corner_radius)
        val coverPl = viewModel.imagePath() + "/" + playlist.name + ".jpg"
        Glide.with(binding.ivCoverPlImage)
            .load(coverPl)
            .transform(RoundedCorners(radius))
            .placeholder(R.drawable.media_placeholder)
            .into(binding.ivCoverPlImage)
    }

    private fun checkChanges() : Boolean {
        if  (binding.ietNamePl.text!!.isEmpty())   { return false }
        if  (!binding.ietNamePl.text !!.equals(updatePlaylist.name))   { return true }
        if  (!binding.ietDescriptPl.text !!.equals(updatePlaylist.descript))   { return true }
        if  (selectedUri != null) { return true }
        return false
    }

    companion object {
        // Первый вариант передачи данных плэйлиста из DisplayPlaylistFragment
        fun passArgs(idPl: Int, imagePl: String, namePl: String, descriptPl: String): Bundle =
            bundleOf("idPl" to idPl, "imagePl" to imagePl, "namePl" to namePl, "descriptPl" to descriptPl)
    }

}