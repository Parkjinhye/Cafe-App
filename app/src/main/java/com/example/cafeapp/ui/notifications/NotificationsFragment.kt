package com.example.cafeapp.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cafeapp.data.UserSession
import com.example.cafeapp.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val notificationsViewModel =
//            ViewModelProvider(this).get(NotificationsViewModel::class.java)
//
//        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        val textView: TextView = binding.textNotifications
//        notificationsViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
//        return root
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set user data from UserSession
        // username이 있을 때와 없을 때 조건에 따라 처리
        if (UserSession.username != null) { //로그인 하였을 때
            binding.tvUsername.text = "환영합니다\n${UserSession.username}님!"
            binding.tvPoints.text = "보유 포인트 : ${UserSession.point}" // username이 있을 때만 포인트 표시
            binding.btnOpenLogin.visibility = View.GONE
            binding.btnOpenSignup.visibility = View.GONE
            binding.btnLogout.visibility = View.VISIBLE
        } else {
            binding.tvUsername.text = "더 많은 기능을 위해 로그인을 해주세요"
            binding.tvPoints.text = "" // username이 없으면 포인트 텍스트는 빈 값
            binding.btnOpenLogin.visibility = View.VISIBLE
            binding.btnOpenSignup.visibility = View.VISIBLE
            binding.btnLogout.visibility = View.GONE
        }

        // 버튼 클릭 시 LoginActivity 열기
        binding.btnOpenLogin.setOnClickListener {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
        binding.btnOpenSignup.setOnClickListener {
            val intent = Intent(requireContext(), SignUpActivity::class.java)
            startActivity(intent)
        }
        binding.btnLogout.setOnClickListener {
            // 로그아웃 처리: UserSession 데이터 초기화
            UserSession.username = null
            UserSession.point = 0
//
            // 버튼 상태 갱신
            binding.btnOpenLogin.visibility = View.VISIBLE
            binding.btnOpenSignup.visibility = View.VISIBLE
            binding.btnLogout.visibility = View.GONE

            // 추가적으로 UI 초기화 (필요에 따라)
            binding.tvUsername.text = "더 많은 기능을 위해 로그인을 해주세요!"
            binding.tvPoints.visibility = View.GONE
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}