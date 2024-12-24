package com.example.cafeapp.ui.menu

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.cafeapp.data.Menu
import com.example.cafeapp.data.UserSession
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class OrderManager(private val context: Context){
    private val db = FirebaseFirestore.getInstance()

    fun showOrderPopup(menu: Menu) {
        val dialog = AlertDialog.Builder(context)
            .setTitle("주문 확인")
            .setMessage("${menu.name}을 주문하시겠습니까?")
            .setPositiveButton("주문하기") { _, _ ->
                ordering(menu)
            }
            .setNegativeButton("취소") {dialog, _ ->
                dialog.dismiss()
            }
            .create()

        dialog.show()
    }
    fun ordering(menu: Menu) {
        val currentUserId = UserSession.username
        val order = hashMapOf(
            "id" to currentUserId,
            "name" to menu.name,
            "price" to menu.price
        )

        db.collection("Order")
            .add(order)
            .addOnSuccessListener {
                Toast.makeText(context, "주문이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                if (currentUserId != null) {
                    updateUserPoints(currentUserId, menu.price)
                    updateMenuSold(menu)
                }
            }
            .addOnFailureListener {e ->
                Toast.makeText(context, "주문에 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
    }
    private fun updateUserPoints(userId: String, price: Int) {
        val pointsToAdd = (price * 0.01).toInt()

        db.collection("User")
            .document(userId)
            .update("point", FieldValue.increment(pointsToAdd.toLong()))
            .addOnSuccessListener {
                Log.d("PointUpdate", "포인트가 ${pointsToAdd}점 추가되었습니다.")
            }
            .addOnFailureListener { e ->
                Log.e("PointUpdate", "포인트 업데이트 실패: ${e.message}")
            }
    }
    private fun updateMenuSold(menu: Menu) {
        // "name" 필드를 기준으로 메뉴 찾기
        db.collection("Menu")
            .whereEqualTo("name", menu.name)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    // documentId를 사용하여 sold 업데이트
                    db.collection("Menu")
                        .document(document.id)
                        .update("sold", FieldValue.increment(1))
                        .addOnSuccessListener {
                            Log.d("OrderHelper", "${menu.name}의 판매 수가 증가했습니다.")
                        }
                        .addOnFailureListener { e ->
                            Log.e("OrderHelper", "판매 수 업데이트 실패: ${e.message}")
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("OrderHelper", "메뉴 찾기 실패: ${e.message}")
            }
    }


}