package com.example.capston_spotyup.Analyze.Domain

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.app.AnalyzeFragmentSub
import com.example.capston_spotyup.Analyze.DTO.DatesResponse
import com.example.capston_spotyup.Main.Api.DatesApi
import com.example.capston_spotyup.Network.RetrofitClient.datesApi
import com.example.capston_spotyup.R
import com.example.capston_spotyup.databinding.FragmentAnalyzeCalenderBinding
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth


class AnalyzeCalendarFragment : Fragment() {

    private var _binding: FragmentAnalyzeCalenderBinding? = null
    private val binding get() = _binding!!
    private val availableDates = mutableSetOf<LocalDate>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAnalyzeCalenderBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 현재 날짜 정보
        val currentYear = YearMonth.now().year
        val currentMonth = YearMonth.now().monthValue

//        val currentMonth = YearMonth.now()
        // 서버에서 날짜 데이터 가져오기
        fetchAvailableDates(userId = 1L, year = currentYear, month = currentMonth)

        // 캘린더 설정
        val startMonth = YearMonth.now().minusMonths(12)
        val endMonth = YearMonth.now().plusMonths(12)
        val firstDayOfWeek = DayOfWeek.SUNDAY

        binding.calendarView.setup(startMonth, endMonth, firstDayOfWeek)
        binding.calendarView.scrollToMonth(YearMonth.now())

    //  기존 더미테이터로 인한 calendar 로직 -> 이거 더미데이터 설정이라 나중에 삭제함

//        binding.calendarView.dayBinder = object : DayBinder<DayViewContainer> {
//            override fun create(view: View) = DayViewContainer(view)
//            override fun bind(container: DayViewContainer, day: CalendarDay) {
//                container.textView.text =
//                    if (day.owner == DayOwner.THIS_MONTH) day.date.dayOfMonth.toString()
//                    else ""  // 현재 월이 아닌 날짜는 비워서 안 보이게
//
//                // 날짜를 선택했을 때 이벤트 처리
//                container.textView.setOnClickListener {
//                    if (day.owner == DayOwner.THIS_MONTH) {
//                        val selectedDate = day.date
//                        // 날짜를 Bundle로 전달하여 다음 프래그먼트로 이동
//                        val bundle = Bundle()
//                        bundle.putString("selectedDate", selectedDate.toString())
//
//                        // 직접 FragmentTransaction을 사용하여 이동
//                        val analyzeFragmentSub = AnalyzeFragmentSub()
//                        analyzeFragmentSub.arguments = bundle
//
//                        parentFragmentManager.beginTransaction()
//                            .replace(R.id.nav_host_fragment, analyzeFragmentSub) // Fragment container ID
//                            .addToBackStack(null) // Back stack에 추가 (뒤로 가기 가능)
//                            .commit()
//                    }
//                }
//            }
//        }

        // Calendar 날짜 load하는 APi
        binding.calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.textView.text =
                    if (day.owner == DayOwner.THIS_MONTH) day.date.dayOfMonth.toString()
                    else ""

                if (day.owner == DayOwner.THIS_MONTH) {
                    if (availableDates.contains(day.date)) {
                        container.textView.setTextColor(Color.BLUE) // 강조 색상
                        container.textView.setOnClickListener {
                            val selectedDate = day.date
                            val bundle = Bundle().apply {
                                putString("selectedDate", selectedDate.toString())
                            }
                            val analyzeFragmentSub = AnalyzeFragmentSub().apply {
                                arguments = bundle
                            }
                            parentFragmentManager.beginTransaction()
                                // 활성화 된 날짜만 화면 전환이 가능하게
                                .replace(R.id.nav_host_fragment, analyzeFragmentSub)
                                .addToBackStack(null)
                                .commit()
                        }
                    } else {
                        container.textView.setTextColor(Color.GRAY) // 비활성화 색상 -> 이것도 고칠게
                        container.textView.setOnClickListener(null)
                    }
                } else {
                    container.textView.setOnClickListener(null)
                }
            }
        }
        binding.calendarView.monthScrollListener = { month ->
            val monthText = "${month.yearMonth.monthValue}월"
            binding.textMonthTitle.text = monthText
        }



        binding.btnPreviousMonth.setOnClickListener {
            val prevMonth = binding.calendarView.findFirstVisibleMonth()?.yearMonth?.minusMonths(1)
            prevMonth?.let { binding.calendarView.scrollToMonth(it) }
        }

        binding.btnNextMonth.setOnClickListener {
            val nextMonth = binding.calendarView.findFirstVisibleMonth()?.yearMonth?.plusMonths(1)
            nextMonth?.let { binding.calendarView.scrollToMonth(it) }
        }

    }
    private fun fetchAvailableDates(userId: Long, year: Int, month: Int) {
        Log.d("CalendarAPI", "API 요청 시작 - userId: $userId, year: $year, month: $month")
        datesApi.getGameDates(sportsId = 1L, userId = userId, year = year, month = month)
            .enqueue(object : Callback<DatesResponse> {
                override fun onResponse(call: Call<DatesResponse>, response: Response<DatesResponse>) {
                    if (response.isSuccessful) {
                        Log.d("CalendarAPI", "응답 성공: ${response.body()}")
                        response.body()?.result?.gameDateList?.let { dates ->
                            Log.d("CalendarAPI", "받은 날짜 목록: $dates")
                            availableDates.clear()
                            dates.mapTo(availableDates) { LocalDate.parse(it) }
                            binding.calendarView.notifyCalendarChanged()
                        }
                    } else {
                        Log.e("CalendarAPI", "응답 실패 - code: ${response.code()}, message: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<DatesResponse>, t: Throwable) {
                    Log.e("CalendarAPI", "API 호출 실패", t)
                    //TODO: 오류 처리 로직
                }
            })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    inner class DayViewContainer(view: View) : ViewContainer(view) {
        val textView: TextView = view.findViewById(R.id.calendarDayText)
    }

}
