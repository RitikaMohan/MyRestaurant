package com.example.myrestaurant.presentation.cart

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import com.example.myrestaurant.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: CartViewModel,
    onBackClick: () -> Unit
) {
    val cartItems = viewModel.cartItems
    val (subtotal, tax, total) = viewModel.getSummary()
    val isLoading = viewModel.isLoading
    var showSuccessDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                TextButton(onClick = {
                    viewModel.placeOrder()
                    showSuccessDialog = true
                }) {
                    Text(text = stringResource(R.string.order), color = Color.White)
                }
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.cart)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6200EE), // Purple background
                    titleContentColor = Color.White     // Optional: white title text
                ),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                if (cartItems.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = stringResource(R.string.cartEmpty), style = MaterialTheme.typography.titleMedium)
                    }
                } else {
                    cartItems.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // ✅ Dish Image
                            AsyncImage(
                                model = item.dish.imageUrl,
                                contentDescription = item.dish.name,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.dish.name,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "₹${item.dish.price} x ${item.quantity}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }

                            // ✅ Quantity Controls
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = { viewModel.removeFromCart(item.dish) }) {
                                    Icon(Icons.Default.Remove, contentDescription = "Remove")
                                }
                                Text(text = item.quantity.toString(), modifier = Modifier.width(24.dp), textAlign = TextAlign.Center)
                                IconButton(onClick = { viewModel.addToCart(item.dish) }) {
                                    Icon(Icons.Default.Add, contentDescription = "Add")
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    Divider()

                    CartTotalSection(
                        subtotal = subtotal,
                        tax = tax,
                        total = total
                    )
                }
            }
        }
    }
    if (showSuccessDialog) {
        OrderSuccessDialog {
            showSuccessDialog = false
        }
    }
}

@Composable
fun CartTotalSection(
    subtotal: Int,
    tax: Double,
    total: Double
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {

        // ✅ Show subtotal & tax above total when expanded
        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(bottom = 8.dp)) {
                Text(
                    "${stringResource(R.string.subTotal)}: ₹$subtotal",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "${stringResource(R.string.tax)} (CGST + SGST): ₹${"%.2f".format(tax)}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Divider(modifier = Modifier.padding(top = 8.dp))
            }
        }

        // ✅ Total remains at the bottom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.total),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "₹${"%.2f".format(total)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Collapse" else "Expand"
                )
            }
        }
    }
}

@Composable
fun OrderSuccessDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.ThumbUp,
                    contentDescription = "Success",
                    tint = Color(0xFFFF9800), // orange
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.orderSuccess),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
            }

            // Bottom black dismiss button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .clickable { onDismiss() }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(R.string.dismiss), color = Color.White, fontWeight = FontWeight.Medium)
            }
        }
    }
}
