package xyz.teamgravity.derivedstateofremember

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import xyz.teamgravity.derivedstateofremember.ui.theme.DerivedStateOfRememberTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DerivedStateOfRememberTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val lazyListState = rememberLazyListState()
                    var scrollTopButtonEnabled by remember { mutableStateOf(true) }

                    Scaffold(
                        floatingActionButton = {
                            ScrollTopButton(
                                state = lazyListState,
                                isEnabled = scrollTopButtonEnabled
                            )
                        }
                    ) { padding ->
                        LazyColumn(
                            state = lazyListState,
                            contentPadding = PaddingValues(16.dp),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(padding)
                        ) {
                            items(
                                count = 100,
                                key = { it }
                            ) { index ->
                                Text(
                                    text = stringResource(id = R.string.item, index),
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .clickable {
                                            scrollTopButtonEnabled = !scrollTopButtonEnabled
                                        }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ScrollTopButton(
        state: LazyListState,
        isEnabled: Boolean
    ) {
        val scope = rememberCoroutineScope()
        val visible by remember(isEnabled) {
            derivedStateOf {
                state.firstVisibleItemIndex >= 5 && isEnabled
            }
        }
//        val visible = remember(state.firstVisibleItemIndex) {
//            state.firstVisibleItemIndex >= 5
//        }

        if (visible) {
            FloatingActionButton(
                onClick = {
                    scope.launch {
                        state.animateScrollToItem(0)
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = null
                )
            }
        }
    }
}