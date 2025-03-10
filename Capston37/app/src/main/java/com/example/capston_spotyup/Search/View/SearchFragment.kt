package com.example.capston_spotyup.Search.View

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.capston_spotyup.R
import com.example.capston_spotyup.databinding.FragmentSubBinding


class SearchFragment : Fragment(R.layout.fragment_sub) {

    // ViewBinding 객체 선언
    private var _binding: FragmentSubBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // ViewBinding 초기화
        _binding = FragmentSubBinding.inflate(inflater, container, false)

        // ViewBinding을 통해 레이아웃을 반환
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // ViewBinding 객체를 해제하여 메모리 누수를 방지
        _binding = null
    }
}
