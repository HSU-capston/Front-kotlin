package com.example.capston_spotyup.Profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.capston_spotyup.R

class StorageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_mypage_storage, container, false)

        // RecyclerView 설정
        val recyclerView = view.findViewById<RecyclerView>(R.id.storageRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 예제 데이터 추가
        val dataList = listOf("파일 1", "파일 2", "파일 3")
        val adapter = StorageAdapter(dataList)
        recyclerView.adapter = adapter

        return view
    }
}
