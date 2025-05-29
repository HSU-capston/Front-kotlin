package com.example.capston_spotyup.Map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.capston_spotyup.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.CameraUpdateFactory


class GoogleMapFragment : Fragment(R.layout.fragment_map_google), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // ⛰️ 서울 성북구 낙산관 좌표
        val sangsangLocation = LatLng(37.582187, 127.011414)

        // ✅ 마커와 카메라 이동 추가
        map.addMarker(
            MarkerOptions()
                .position(sangsangLocation)
                .title("상상관")
        )
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sangsangLocation, 17f))
    }
}

/*
class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var mapView: MapView
    private var kakaoMap: KakaoMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)


        // MapView 참조 (XML에 정의된 MapView 사용)
        mapView = binding.mapView

        // 지도 초기화
        initializeMap()

        return binding.root
    }

    private fun initializeMap() {
        mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {
                // 지도 API가 정상적으로 종료될 때 호출
                Log.d("KakaoMap", "onMapDestroy")
            }

            override fun onMapError(error: Exception) {
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출
                Log.e("KakaoMap", "onMapError", error)
            }
        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(map: KakaoMap) {
                // 정상적으로 인증이 완료되었을 때 호출
                // KakaoMap 객체를 얻어 옵니다.
                kakaoMap = map

                // 서울 시청 좌표(WGS84)
                val seoulCityHall = LatLng.from(37.566535, 126.977969)

                // CameraUpdate 객체를 사용하여 카메라 이동
                val cameraUpdate = CameraUpdateFactory.newCenterPosition(seoulCityHall)
                kakaoMap?.moveCamera(cameraUpdate)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}*/
