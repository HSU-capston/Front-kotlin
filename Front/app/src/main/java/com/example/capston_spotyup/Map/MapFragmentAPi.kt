package com.example.capston_spotyup.Map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.capston_spotyup.databinding.FragmentMapApiBinding
import com.example.capston_spotyup.databinding.FragmentMapBinding
import com.example.capston_spotyup.databinding.FragmentSubBinding

//package com.example.capston_spotyup.Map
//
//import com.naver.maps.map.MapFragment
//import com.naver.maps.map.NaverMap
//import com.naver.maps.map.OnMapReadyCallback
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.fragment.app.Fragment
//import com.example.capston_spotyup.databinding.FragmentMapApiBinding
//
//
//class MyFragment : Fragment(), OnMapReadyCallback {
//
//    private var _binding: FragmentMapApiBinding? = null
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentMapApiBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        var mapFragment = childFragmentManager.findFragmentById(binding.mapFragmentContainer.id) as? MapFragment
//        if (mapFragment == null) {
//            mapFragment = MapFragment.newInstance()
//            childFragmentManager.beginTransaction()
//                .replace(binding.mapFragmentContainer.id, mapFragment)
//                .commit()
//        }
//
//        mapFragment.getMapAsync(this)
//    }
//
//
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}

class MyFragmentAPi : Fragment(){
    private var _binding: FragmentMapApiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // ViewBinding을 통해 레이아웃을 반환
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // ViewBinding 객체를 해제하여 메모리 누수를 방지
        _binding = null
    }

}