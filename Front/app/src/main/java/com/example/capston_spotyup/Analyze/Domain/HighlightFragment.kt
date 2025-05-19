package com.example.capston_spotyup.Analyze.Domain

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.fragment.app.Fragment
import com.example.capston_spotyup.R

class HighlightFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_highlight, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val videoView = view.findViewById<VideoView>(R.id.videoview)
        val highlightUrl = arguments?.getString("highlightUrl")

        if (!highlightUrl.isNullOrEmpty()) {
            val uri = Uri.parse(highlightUrl)

            val mediaController = MediaController(requireContext())
            mediaController.setAnchorView(videoView)

            videoView.setMediaController(mediaController)
            videoView.setVideoURI(uri)
            videoView.setOnPreparedListener { mediaPlayer ->
                mediaPlayer.start()
            }
        } else {
            Toast.makeText(requireContext(), "영상 URL이 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<View>(R.id.cancel_feed)?.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}
