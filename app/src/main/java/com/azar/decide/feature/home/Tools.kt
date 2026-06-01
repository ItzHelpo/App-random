package com.azar.decide.feature.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.filled.ThumbsUpDown
import androidx.compose.ui.graphics.vector.ImageVector
import com.azar.decide.R
import com.azar.decide.ui.navigation.Routes

/** A single tool shown on the home grid. */
data class Tool(
    val route: String,
    val icon: ImageVector,
    val titleRes: Int,
    val descRes: Int
)

val allTools: List<Tool> = listOf(
    Tool(Routes.COIN, Icons.Filled.Paid, R.string.tool_coin, R.string.tool_coin_desc),
    Tool(Routes.DICE, Icons.Filled.Casino, R.string.tool_dice, R.string.tool_dice_desc),
    Tool(Routes.NUMBER, Icons.Filled.Tag, R.string.tool_number, R.string.tool_number_desc),
    Tool(Routes.YESNO, Icons.Filled.ThumbsUpDown, R.string.tool_yesno, R.string.tool_yesno_desc),
    Tool(Routes.RAFFLE, Icons.Filled.EmojiEvents, R.string.tool_raffle, R.string.tool_raffle_desc),
    Tool(Routes.PASSWORD, Icons.Filled.Password, R.string.tool_password, R.string.tool_password_desc),
    Tool(Routes.LOTTERY, Icons.Filled.ConfirmationNumber, R.string.tool_lottery, R.string.tool_lottery_desc),
)
