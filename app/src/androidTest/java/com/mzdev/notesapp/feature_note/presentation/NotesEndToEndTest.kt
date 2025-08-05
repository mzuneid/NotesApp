package com.mzdev.notesapp.feature_note.presentation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import com.mzdev.notesapp.MainActivity
import com.mzdev.notesapp.di.AppModule
import com.mzdev.notesapp.utils.TestTags
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class NotesEndToEndTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @ExperimentalAnimationApi
    @Before fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun saveNewNote_editAfterwards() {
        // WAIT for FAB to appear
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithContentDescription("Add").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithContentDescription("Add").performClick()

        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD)
            .performTextInput("test‑title")
        composeRule.onNodeWithTag(TestTags.CONTENT_TEXT_FIELD)
            .performTextInput("test‑content")

        // WAIT for Save button
        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithContentDescription("Save").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithContentDescription("Save").performClick()

        // Debug tree export (for CI logs)
        composeRule.onRoot().printToLog("UI_TREE_AFTER_SAVE")

        composeRule.onNodeWithText("test‑title").assertIsDisplayed()
        composeRule.onNodeWithText("test‑content").assertIsDisplayed()
        composeRule.onNodeWithText("test‑title").performClick()

        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD)
            .assertTextEquals("test‑title")
        composeRule.onNodeWithTag(TestTags.CONTENT_TEXT_FIELD)
            .assertTextEquals("test‑content")

        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD)
            .performTextClearance()
        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD)
            .performTextInput("test‑title2")

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithContentDescription("Save").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onNodeWithContentDescription("Save").performClick()

        composeRule.waitUntil(timeoutMillis = 5000) {
            composeRule.onAllNodesWithText("test‑title2").fetchSemanticsNodes().isNotEmpty()
        }
        composeRule.onRoot().printToLog("UI_TREE_AFTER_EDIT")
        composeRule.onNodeWithText("test‑title2").assertIsDisplayed()
    }

    @Test
    fun saveNewNotes_orderByTitleDescending() {
        for (i in 1..3) {
            // Wait and Click on FAB
            composeRule.waitUntil(timeoutMillis = 5_000) {
                composeRule.onAllNodesWithContentDescription("Add", useUnmergedTree = true).fetchSemanticsNodes()
                    .isNotEmpty()
            }
            composeRule.onNodeWithContentDescription("Add", useUnmergedTree = true).performClick()

            // Wait until the Title TextField appears
            composeRule.waitUntil(timeoutMillis = 5_000) {
                composeRule.onAllNodesWithTag(TestTags.TITLE_TEXT_FIELD, useUnmergedTree = true).fetchSemanticsNodes()
                    .isNotEmpty()
            }

            // Input title and content
            composeRule
                .onNodeWithTag(TestTags.TITLE_TEXT_FIELD, useUnmergedTree = true)
                .performTextInput(i.toString())

            composeRule
                .onNodeWithTag(TestTags.CONTENT_TEXT_FIELD, useUnmergedTree = true)
                .performTextInput(i.toString())

            // Save the note
            composeRule.onNodeWithContentDescription("Save", useUnmergedTree = true).performClick()
        }

        composeRule.onNodeWithText("1").assertIsDisplayed()
        composeRule.onNodeWithText("2").assertIsDisplayed()
        composeRule.onNodeWithText("3").assertIsDisplayed()

        // Change sort order
        composeRule.onNodeWithContentDescription("Sort", useUnmergedTree = true).performClick()
        composeRule.onNodeWithContentDescription("Title", useUnmergedTree = true).performClick()
        composeRule.onNodeWithContentDescription("Descending", useUnmergedTree = true).performClick()

        composeRule.onAllNodesWithTag(TestTags.NOTE_ITEM)[0]
            .assertTextContains("3")
        composeRule.onAllNodesWithTag(TestTags.NOTE_ITEM)[1]
            .assertTextContains("2")
        composeRule.onAllNodesWithTag(TestTags.NOTE_ITEM)[2]
            .assertTextContains("1")
    }
}
