package com.example.capston_spotyup.Analyze.Domain

import android.os.Bundle
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.app.AnalyzeFragmentSub
import com.example.capston_spotyup.R
import com.example.capston_spotyup.databinding.FragmentAnalyzeCalenderBinding
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import java.time.DayOfWeek
import java.time.YearMonth


class AnalyzeCalendarFragment : Fragment() {

    private var _binding: FragmentAnalyzeCalenderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAnalyzeCalenderBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentMonth = YearMonth.now()
        val startMonth = currentMonth.minusMonths(12)
        val endMonth = currentMonth.plusMonths(12)
        val firstDayOfWeek = DayOfWeek.SUNDAY

        binding.calendarView.setup(startMonth, endMonth, firstDayOfWeek)
        binding.calendarView.scrollToMonth(currentMonth)

        binding.calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.textView.text =
                    if (day.owner == DayOwner.THIS_MONTH) day.date.dayOfMonth.toString()
                    else ""  // 현재 월이 아닌 날짜는 비워서 안 보이게

                // 날짜를 선택했을 때 이벤트 처리
                container.textView.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        val selectedDate = day.date
                        // 날짜를 Bundle로 전달하여 다음 프래그먼트로 이동
                        val bundle = Bundle()
                        bundle.putString("selectedDate", selectedDate.toString())

                        // 직접 FragmentTransaction을 사용하여 이동
                        val analyzeFragmentSub = AnalyzeFragmentSub()
                        analyzeFragmentSub.arguments = bundle

                        parentFragmentManager.beginTransaction()
                            .replace(R.id.nav_host_fragment, analyzeFragmentSub) // Fragment container ID
                            .addToBackStack(null) // Back stack에 추가 (뒤로 가기 가능)
                            .commit()
                    }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    inner class DayViewContainer(view: View) : ViewContainer(view) {
        val textView: TextView = view.findViewById(R.id.calendarDayText)
    }

}
