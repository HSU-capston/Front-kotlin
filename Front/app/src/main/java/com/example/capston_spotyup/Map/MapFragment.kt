//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.FrameLayout
//import androidx.fragment.app.Fragment
//import com.kakao.sdk.map.MapView
//import com.kakao.sdk.map.MapPoint
//
//import com.example.capston_spotyup.databinding.FragmentMapBinding
//
//class MapFragment : Fragment() {
//
//    private var _binding: FragmentMapBinding? = null
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        _binding = FragmentMapBinding.inflate(inflater, container, false)
//
//        // MapView 생성
//        val mapView = MapView(requireContext())
//
//        // 지도 초기 설정 (서울 시청 좌표)
//        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(37.566535, 126.977969), true)
//
//        // 지도 컨테이너에 MapView 추가
//        val container = binding.mapContainer
//        container.addView(mapView)
//
//        return binding.root
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}
