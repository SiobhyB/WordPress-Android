package org.wordpress.android.ui.bloggingreminders

import org.wordpress.android.R
import org.wordpress.android.ui.bloggingreminders.BloggingRemindersItem.DayButtons
import org.wordpress.android.ui.bloggingreminders.BloggingRemindersItem.DayButtons.DayItem
import org.wordpress.android.ui.bloggingreminders.BloggingRemindersItem.EmphasizedText
import org.wordpress.android.ui.bloggingreminders.BloggingRemindersItem.Illustration
import org.wordpress.android.ui.bloggingreminders.BloggingRemindersItem.MediumEmphasisText
import org.wordpress.android.ui.bloggingreminders.BloggingRemindersItem.Tip
import org.wordpress.android.ui.bloggingreminders.BloggingRemindersItem.Title
import org.wordpress.android.ui.bloggingreminders.BloggingRemindersViewModel.UiState.PrimaryButton
import org.wordpress.android.ui.utils.ListItemInteraction
import org.wordpress.android.ui.utils.UiString.UiStringRes
import org.wordpress.android.ui.utils.UiString.UiStringText
import org.wordpress.android.util.LocaleManagerWrapper
import java.time.DayOfWeek
import java.time.format.TextStyle.SHORT
import javax.inject.Inject

class DaySelectionBuilder
@Inject constructor(
    private val daysProvider: DaysProvider,
    private val dayLabelUtils: DayLabelUtils,
    private val localeManagerWrapper: LocaleManagerWrapper
) {
    fun buildSelection(
        bloggingRemindersModel: BloggingRemindersUiModel?,
        onSelectDay: (DayOfWeek) -> Unit
    ): List<BloggingRemindersItem> {
        val daysOfWeek = daysProvider.getDaysOfWeekByLocale()
        val text = dayLabelUtils.buildNTimesLabel(bloggingRemindersModel)
        val nTimesLabel = MediumEmphasisText(
                EmphasizedText(text),
                bloggingRemindersModel?.enabledDays?.isEmpty() == true
        )
        return listOf(
                Illustration(R.drawable.img_illustration_calendar),
                Title(UiStringRes(R.string.blogging_reminders_select_days)),
                MediumEmphasisText(UiStringRes(R.string.blogging_reminders_select_days_message)),
                DayButtons(daysOfWeek.map {
                    DayItem(
                            UiStringText(it.getDisplayName(SHORT, localeManagerWrapper.getLocale())),
                            bloggingRemindersModel?.enabledDays?.contains(it) == true,
                            ListItemInteraction.create(it, onSelectDay)
                    )
                }),
                nTimesLabel,
                Tip(UiStringRes(R.string.blogging_reminders_tip), UiStringRes(R.string.blogging_reminders_tip_message))
        )
    }

    fun buildPrimaryButton(
        bloggingRemindersModel: BloggingRemindersUiModel?,
        isFirstTimeFlow: Boolean,
        onConfirm: (BloggingRemindersUiModel?) -> Unit
    ): PrimaryButton {
        val buttonEnabled = if (isFirstTimeFlow) {
            bloggingRemindersModel?.enabledDays?.isNotEmpty() == true
        } else {
            true
        }
        val buttonText = if (isFirstTimeFlow) {
            R.string.blogging_reminders_notify_me
        } else {
            R.string.blogging_reminders_update
        }
        return PrimaryButton(
                UiStringRes(buttonText),
                enabled = buttonEnabled,
                ListItemInteraction.create(bloggingRemindersModel, onConfirm)
        )
    }
}
