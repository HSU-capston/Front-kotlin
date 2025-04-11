import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.kakao.vectormap.MapView
import com.kakao.vectormap.CoordConverter
import com.kakao.vectormap.LatLng




import com.example.capston_spotyup.databinding.FragmentMapBinding

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapView: MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        // MapView 참조 (XML에 정의된 MapView 사용)
        mapView = binding.mapView


        // 서울 시청 좌표 (WGS84 좌표계)
        val latLng = LatLng.from(37.566535, 126.977969)

        // 지도 초기화 및 중심 설정
        mapView.setMapCenter(latLng)

        val convertedLatLng = CoordConverter.toLatLngFromWCONG(524612.7, 1082046.6)

        // 좌표 변환 후 사용
        // 예를 들어, 변환된 좌표로 지도 중심을 설정할 수 있음
        mapView.setMapCenter(convertedLatLng)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}