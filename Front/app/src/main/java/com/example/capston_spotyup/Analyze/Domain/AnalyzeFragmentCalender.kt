package com.example.capston_spotyup.Analyze.Domain

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.app.AnalyzeFragmentSub
import com.example.capston_spotyup.Analyze.DTO.Response.DatesResponse
import com.example.capston_spotyup.Network.RetrofitClient.datesApi
import com.example.capston_spotyup.R
import com.example.capston_spotyup.Util.TokenManager
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
        fetchAvailableDates(year = currentYear, month = currentMonth)


        // 캘린더 설정
        val startMonth = YearMonth.now().minusMonths(12)
        val endMonth = YearMonth.now().plusMonths(12)
        val firstDayOfWeek = DayOfWeek.SUNDAY

        binding.calendarView.setup(startMonth, endMonth, firstDayOfWeek)
        binding.calendarView.scrollToMonth(YearMonth.now())


        // Calendar 날짜 load하는 APi
        binding.calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.textView.text =
                    if (day.owner == DayOwner.THIS_MONTH) day.date.dayOfMonth.toString()
                    else ""

                if (day.owner == DayOwner.THIS_MONTH) {
                    if (availableDates.contains(day.date)) {
                        container.textView.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.main) // 메인 색상
                        )
                        container.textView.setTypeface(null, android.graphics.Typeface.BOLD) // 강조
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

        setupCalendarIconSelectors()

    }
    private fun fetchAvailableDates(year: Int, month: Int) {
        val token = TokenManager.getAccessToken()
        if (token == null) {
            Log.e("CalendarAPI", "토큰 없음 - 로그인 필요")
            return
        }
        // 현재 선택된 날짜를 "YYYY-MM-DD" 형태로 포맷, 선언부 문제인가
//        val firstDayOfMonth = "${year}-${String.format("%02d", month)}-01" // 월의 첫 날

        Log.d("CalendarAPI", "API 요청 시작 - year: $year, month: $month")
        datesApi.getGameDates("Bearer $token", year, month)
            .enqueue(object : Callback<DatesResponse> {
                override fun onResponse(call: Call<DatesResponse>, response: Response<DatesResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.result?.gameDateList?.let { dates ->
                            availableDates.clear()
                            dates.mapTo(availableDates) { LocalDate.parse(it) }
                            binding.calendarView.notifyCalendarChanged()
                        }
                    } else {
                        Log.e("CalendarAPI", "응답 실패: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<DatesResponse>, t: Throwable) {
                    Log.e("CalendarAPI", "API 호출 실패", t)
                }
            })
    }


    private fun setupCalendarIconSelectors() {
        val icons = listOf(
            binding.undertab1.getChildAt(0) as ImageView,
            binding.undertab1.getChildAt(1) as ImageView,
            binding.undertab1.getChildAt(2) as ImageView
        )

        val selectedIcons = listOf(
            R.drawable.ic_anal_bill,
            R.drawable.ic_anal_golf,
            R.drawable.ic_anal_bowl
        )

        val defaultIcons = listOf(
            R.drawable.ic_chart_1,
            R.drawable.ic_chart_2,
            R.drawable.ic_chart_3
        )

        icons.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                icons.forEachIndexed { i, icon ->
                    icon.setImageResource(defaultIcons[i])
                }
                imageView.setImageResource(selectedIcons[index])

                // TODO: 여기에 종목 선택에 따른 데이터 변경 (필요 시)
                // 예: 현재 sportsId 저장 → 캘린더에 다른 데이터 연동 등
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    inner class DayViewContainer(view: View) : ViewContainer(view) {
        val textView: TextView = view.findViewById(R.id.calendarDayText)
    }

}
