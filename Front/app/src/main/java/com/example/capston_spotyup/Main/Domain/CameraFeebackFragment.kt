package com.example.capston_spotyup.Main.Domain

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.fragment.app.Fragment
import com.example.capston_spotyup.R

class CameraFeedbackFragment : Fragment() {

    private lateinit var videoView: VideoView
    private lateinit var textPoseScore: TextView
    private lateinit var textRecommendPose: TextView
    private lateinit var cancelBtn: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_camera_feedback, container, false)

        videoView = view.findViewById(R.id.videoview)
        textPoseScore = view.findViewById(R.id.text_pose_score)
        textRecommendPose = view.findViewById(R.id.text_recommend_pose)
        cancelBtn = view.findViewById(R.id.cancel_feed)

        val videoUrl = arguments?.getString("videoUrl")
        val poseScore = arguments?.getString("poseScore")
        val recommendPose = arguments?.getString("recommendPose")

        if (!videoUrl.isNullOrBlank()) {
            videoView.setVideoURI(Uri.parse(videoUrl))
            videoView.start()
        }

        textPoseScore.text = poseScore ?: ""
        textRecommendPose.text = recommendPose ?: ""

        cancelBtn.setOnClickListener {
            parentFragmentManager.beginTransaction().remove(this@CameraFeedbackFragment).commit()
        }

        return view
    }

    companion object {
        fun newInstance(videoUrl: String, poseScore: String, recommendPose: String): CameraFeedbackFragment {
            val fragment = CameraFeedbackFragment()
            val bundle = Bundle().apply {
                putString("videoUrl", videoUrl)
                putString("poseScore", poseScore)
                putString("recommendPose", recommendPose)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
}
