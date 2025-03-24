package com.example.capston_spotyup.Map

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.capston_spotyup.Network.RetrofitClient
import com.example.capston_spotyup.Map.DTO.Response.BowlingResponse
import com.example.capston_spotyup.databinding.FragmentMapBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 🔥 버튼 클릭 시 최신 비디오 파일을 찾아 서버에 업로드 후 재생
        binding.playbutton.setOnClickListener {
            uploadLatestVideoToServer()
        }
    }

    /**
     * ✅ 최신 저장된 비디오 파일을 찾아서 서버에 업로드하는 함수
     */
    private fun uploadLatestVideoToServer() {
        val latestVideoPath = getLatestVideoFileFromMediaStore()

        if (latestVideoPath.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "최신 비디오 파일을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val file = File(latestVideoPath)
        Log.d("MapFragment", "최신 비디오 파일 경로: $latestVideoPath")

        if (!file.exists() || !file.canRead()) {
            Toast.makeText(requireContext(), "비디오 파일을 찾을 수 없거나 접근할 수 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        // 🔥 MultipartBody.Part 변환
        val requestFile = file.asRequestBody("video/mp4".toMediaTypeOrNull())
        val videoPart = MultipartBody.Part.createFormData("file", file.name, requestFile)

        // 🔥 서버로 POST 요청 보내기
        RetrofitClient.instance.analyzeBowling(videoPart).enqueue(object : Callback<BowlingResponse> {
            override fun onResponse(call: Call<BowlingResponse>, response: Response<BowlingResponse>) {
                Log.d("MapFragment", "서버 응답 코드: ${response.code()}")

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("MapFragment", "서버 응답 데이터: $responseBody")

                    if (responseBody != null) {
                        val processedVideoUrl = responseBody.result?.videoUrl
                        Log.d("MapFragment", "비디오 URL: $processedVideoUrl")

                        if (processedVideoUrl.isNullOrEmpty()) {
                            Log.e("MapFragment", "서버에서 받은 videoUrl이 null 또는 빈 문자열입니다.")
                            Toast.makeText(requireContext(), "올바른 비디오 URL을 받지 못했습니다.", Toast.LENGTH_SHORT).show()
                            return
                        }
                        playVideo(processedVideoUrl)
                    } else {
                        Log.e("MapFragment", "서버 응답이 null 입니다.")
                        Toast.makeText(requireContext(), "서버 응답이 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("MapFragment", "비디오 처리 실패. 응답 코드: ${response.code()}, 에러 메시지: $errorBody")
                    Toast.makeText(requireContext(), "비디오 처리 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BowlingResponse>, t: Throwable) {
                Log.e("MapFragment", "네트워크 오류: ${t.message}")
                Toast.makeText(requireContext(), "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * ✅ 가장 최근에 저장된 비디오 파일 경로 가져오기 (MediaStore 사용)
     */
    private fun getLatestVideoFileFromMediaStore(): String? {
        val projection = arrayOf(MediaStore.Video.Media.DATA)
        val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC" // 최신 파일 순 정렬

        requireContext().contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection, null, null, sortOrder
        )?.use { cursor ->
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            if (cursor.moveToFirst()) {
                val filePath = cursor.getString(columnIndex)
                Log.d("MapFragment", "최신 비디오 파일 경로: $filePath")
                return filePath
            }
        }
        return null
    }

    /**
     * ✅ 서버에서 받은 비디오 URL을 VideoView에 로드하여 재생
     */
    private fun playVideo(videoUrl: String?) {
        if (videoUrl.isNullOrEmpty()) {
            Log.e("MapFragment", "playVideo()에 전달된 videoUrl이 null 또는 빈 문자열입니다.")
            Toast.makeText(requireContext(), "올바른 비디오 URL이 아닙니다.", Toast.LENGTH_SHORT).show()
            return
        }

        if (!videoUrl.startsWith("http")) {
            Log.e("MapFragment", "비디오 URL이 올바르지 않습니다: $videoUrl")
            Toast.makeText(requireContext(), "비디오 URL이 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val uri = Uri.parse(videoUrl)
        Log.d("MapFragment", "비디오 재생 URI: $uri")

        binding.videoview.setVideoURI(uri)
        binding.videoview.setOnPreparedListener { mediaPlayer: MediaPlayer ->
            mediaPlayer.start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
