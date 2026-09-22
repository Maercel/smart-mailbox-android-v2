package com.example.smartmailbox.home.ui

import android.R.attr.padding
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartmailbox.home.ui.components.AddNewMailboxButton
import com.example.smartmailbox.ui.theme.Black
import com.example.smartmailbox.ui.theme.LightGray
import com.example.smartmailbox.ui.theme.White


@Composable
fun HomeView(homeViewModel: HomeViewModel, paddingValues: PaddingValues) {
    Box(
        modifier = Modifier.fillMaxSize()
        .padding(paddingValues)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {

            }
        }

        AddNewMailboxButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(16.dp,8.dp)
            ,
            onClick = { /*TODO: ADD*/ }
        )
    }
}