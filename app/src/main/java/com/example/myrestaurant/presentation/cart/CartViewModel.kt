package com.example.myrestaurant.presentation.cart

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myrestaurant.domain.model.CartItem
import com.example.myrestaurant.domain.model.Dish
import com.example.myrestaurant.domain.usecase.MakePaymentUseCase
import kotlinx.coroutines.launch

class CartViewModel(
    private val makePaymentUseCase: MakePaymentUseCase
) : ViewModel() {

    var cartItems = mutableStateListOf<CartItem>()
        private set

    var txnRef by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun addToCart(dish: Dish) {
        val index = cartItems.indexOfFirst { it.dish.id == dish.id }
        if (index >= 0) {
            val current = cartItems[index]
            cartItems[index] = current.copy(quantity = current.quantity + 1)
        } else {
            cartItems.add(CartItem(dish, 1))
        }
    }

    fun removeFromCart(dish: Dish) {
        val index = cartItems.indexOfFirst { it.dish.id == dish.id }
        if (index >= 0) {
            val current = cartItems[index]
            if (current.quantity > 1) {
                cartItems[index] = current.copy(quantity = current.quantity - 1)
            } else {
                cartItems.removeAt(index)
            }
        }
    }

    fun getQuantity(dishId: String): Int {
        return cartItems.find { it.dish.id == dishId }?.quantity ?: 0
    }

    fun getSummary(): Triple<Int, Double, Double> {
        val subtotal = cartItems.sumOf { it.dish.price * it.quantity }
        val cgst = subtotal * 0.025
        val sgst = subtotal * 0.025
        return Triple(subtotal, cgst + sgst, subtotal + cgst + sgst)
    }

    fun placeOrder() {
        viewModelScope.launch {
            isLoading = true
            try {
                val totalAmount = cartItems.sumOf { it.dish.price * it.quantity }.toString()
                val totalItems = cartItems.sumOf { it.quantity }
                val txn = makePaymentUseCase(totalAmount, totalItems, cartItems)
                txnRef = txn
                cartItems.clear()
            } catch (_: Exception) {
                txnRef = null
            } finally {
                isLoading = false
            }
        }
    }
}
