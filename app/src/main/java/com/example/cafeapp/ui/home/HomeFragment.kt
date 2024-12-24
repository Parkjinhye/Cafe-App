package com.example.cafeapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.cafeapp.R
import com.example.cafeapp.data.Menu
import com.example.cafeapp.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HomeFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var indicatorLayout: LinearLayout
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()

    private val topMenuList = mutableListOf<Menu>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = binding.viewPager
        indicatorLayout = binding.indicatorLayout

        val events = listOf(
            EventItem(R.drawable.event1, "🎉 특별 기념 케이크 예약!", "우리의 기념 케이크로 소중한 날을 더 달콤하게 만들어보세요!"),
            EventItem(R.drawable.event2, "🫐 신메뉴 출시, 블루베리 팬케이크", "촉촉하고 풍부한 블루베리 팬케이크로 아침을 시작하세요! 한정 판매 중입니다."),
            EventItem(R.drawable.event3, "❄️ 겨울 한정 따뜻한 윈터 메뉴", "올 겨울을 더 특별하게! 겨울에만 맛볼 수 있는 특별 메뉴를 만나보세요.")

        )

        viewPager.adapter = EventAdapter(events)
        setupIndicator(events.size)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                highlightIndicator(position)
            }
        })
        fetchTopRankedMenus()
    }

    private fun setupIndicator(count: Int) {
        indicatorLayout.removeAllViews()
        repeat(count) {
            val indicator = ImageView(requireContext()).apply {
                setImageResource(R.drawable.indicator_inactive)
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { marginStart = 12 }
                layoutParams = params
            }
            indicatorLayout.addView(indicator)
        }
        highlightIndicator(0) // 첫 번째 인디케이터 활성화
    }

    private fun highlightIndicator(position: Int) {
        for (i in 0 until indicatorLayout.childCount) {
            val indicator = indicatorLayout.getChildAt(i) as ImageView
            if (i == position) {
                indicator.setImageResource(R.drawable.indicator_active)
            } else {
                indicator.setImageResource(R.drawable.indicator_inactive)
            }
        }
    }

    private fun fetchTopRankedMenus() {
        db.collection("Menu")  // "Menu" 컬렉션을 기준으로
            .orderBy("sold", Query.Direction.DESCENDING) // 판매량 기준 내림차순
            .limit(3)  // 상위 3개 항목만 가져옴
            .get()
            .addOnSuccessListener { results ->
                topMenuList.clear()
                for (document in results) {
                    val menu = Menu(
                        name = document.getString("name") ?: "",
                        price = document.getLong("price")?.toInt() ?: 0,
                        sold = document.getLong("sold")?.toInt() ?: 0
                    )
                    topMenuList.add(menu)
                }
                updateRankViews(topMenuList)
            }
            .addOnFailureListener { exception ->
                Log.e("HomeFragment", "Error getting documents: ", exception)
            }
    }

    private fun updateRankViews(topMenus: List<Menu>) {
        if (topMenus.size >= 3) {
            binding.ranking1.text = "${topMenus[0].name}\n${topMenus[0].sold}잔"
            binding.ranking2.text = "${topMenus[1].name}\n${topMenus[1].sold}잔"
            binding.ranking3.text = "${topMenus[2].name}\n${topMenus[2].sold}잔"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}