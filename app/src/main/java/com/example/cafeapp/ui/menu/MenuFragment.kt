package com.example.cafeapp.ui.menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cafeapp.data.Menu
import com.example.cafeapp.data.UserSession
import com.example.cafeapp.databinding.FragmentMenuBinding
import com.google.firebase.firestore.FirebaseFirestore

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!
    private lateinit var menuAdapter: MenuAdapter
    private val menuList = mutableListOf<Menu>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var orderManager = OrderManager(requireContext())

        //firebase 데이터 가져오기
        fetchMenuData()

        //recyclerView 설정
        menuAdapter = MenuAdapter(menuList)

        binding.recyclerViewMenu.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = menuAdapter
        }

        menuAdapter.setOnItemClickListener { menu ->
            if (UserSession.username != null) {
                // 로그인 상태일 때만 팝업 띄우기
                orderManager.showOrderPopup(menu)
            } else {
                // 로그인되지 않은 경우 메시지 표시
                Toast.makeText(requireContext(), "로그인 후 주문 가능합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchMenuData() {
        val db = FirebaseFirestore.getInstance()
        db.collection("Menu")
            .get()
            .addOnSuccessListener { result ->
                menuList.clear() //기존 초기화
                for (document in result) {
                    val menu = Menu(
                        name = document.getString("name") ?: "",
                        price = document.getLong("price")?.toInt() ?: 0,
                        sold = document.getLong("sold")?.toInt() ?: 0
                    )
                    menuList.add(menu)
                }
                menuAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("DashboardFragment", "Error getting documents: ", exception)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}