package com.repon.app.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.repon.app.R
import com.repon.app.data.ItemStatus
import com.repon.app.data.RestockStatus
import com.repon.app.ui.theme.statusOk
import com.repon.app.ui.theme.statusOut
import com.repon.app.ui.theme.statusSoon

fun colorFor(status: RestockStatus): Color = when (status) {
    RestockStatus.OK -> statusOk
    RestockStatus.SOON -> statusSoon
    RestockStatus.OUT -> statusOut
}

@Composable
fun daysLeftText(s: ItemStatus): String = when {
    s.daysLeft < 0 -> pluralStringResource(R.plurals.days_overdue, -s.daysLeft, -s.daysLeft)
    s.daysLeft == 0 -> stringResource(R.string.due_today)
    else -> pluralStringResource(R.plurals.days_left, s.daysLeft, s.daysLeft)
}
