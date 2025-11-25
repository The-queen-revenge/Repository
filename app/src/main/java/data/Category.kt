package com.example.quicknotes.data
import com.example.quicknotes.R
object Categories {
    const val NO_CATEGORY = "No Category"
    const val WORK = "Work"
    const val PERSONAL = "Personal"
    const val IDEAS = "Ideas"
    const val SHOPPING = "Shopping"
    const val OTHER = "Other"

    fun getAll(): List<String> {
        return listOf(NO_CATEGORY, WORK, PERSONAL, IDEAS, SHOPPING, OTHER)
    }

    fun getLocalized(context: android.content.Context): List<String> {
        return listOf(
            context.getString(R.string.no_category),
            context.getString(R.string.work),
            context.getString(R.string.personal),
            context.getString(R.string.ideas),
            context.getString(R.string.shopping),
            context.getString(R.string.other)
        )
    }
}