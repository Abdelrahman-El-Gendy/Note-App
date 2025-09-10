package com.example.noteapp.feature_note.presentation

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.noteapp.di.AppModule
import com.example.noteapp.feature_note.presentstion.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@UninstallModules(AppModule::class)
@RunWith(AndroidJUnit4::class)
class NotesScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    private fun waitForIdle() = runBlocking {
        composeRule.waitForIdle()
        delay(300) // Add a small delay to ensure animations complete
    }

    @Test
    fun addNewNote_appearsInNotesList() {
        // Click FAB and wait for navigation
        composeRule.onNodeWithContentDescription("Add Note")
            .assertExists()
            .assertIsDisplayed()
            .performClick()
        waitForIdle()

        // Enter note title
        composeRule.onNodeWithTag("title_text_field")
            .assertExists()
            .assertIsDisplayed()
            .performTextReplacement("Test Note")
        waitForIdle()

        // Enter note content
        composeRule.onNodeWithTag("content_text_field")
            .assertExists()
            .assertIsDisplayed()
            .performTextReplacement("Test Content")
        waitForIdle()

        // Save the note and wait for navigation back
        composeRule.onNodeWithContentDescription("Save")  // Updated to match actual content description
            .assertExists()
            .assertIsDisplayed()
            .performClick()
        waitForIdle()

        // Verify note appears in list
        composeRule.onNodeWithText("Test Note")
            .assertExists()
            .assertIsDisplayed()
    }

    @Test
    fun deleteNote_disappearsFromList() {
        // Add a note first
        addNoteHelper("Note to Delete", "Content to Delete")
        waitForIdle()

        // Find the specific note item
        composeRule
            .onNode(
                hasTestTag("note_item")
                    .and(hasText("Note to Delete"))
            )
            .assertExists()
            .assertIsDisplayed()
        waitForIdle()

        // Find and click the delete button using its test tag
        composeRule
            .onNodeWithTag("delete_button")
            .assertExists()
            .assertIsDisplayed()
            .performClick()
        waitForIdle()

        // Verify note is gone
        composeRule
            .onNode(hasText("Note to Delete"))
            .assertDoesNotExist()
    }

    @Test
    fun editNote_updatesNoteInList() {
        // Add initial note
        addNoteHelper("Original Title", "Original Content")

        // Find and click the note to edit
        composeRule.onNodeWithText("Original Title")
            .assertExists()
            .assertIsDisplayed()
            .performClick()
        waitForIdle()

        // Update title
        composeRule.onNodeWithTag("title_text_field")
            .assertExists()
            .assertIsDisplayed()
            .performTextReplacement("Updated Title")
        waitForIdle()

        // Update content
        composeRule.onNodeWithTag("content_text_field")
            .assertExists()
            .assertIsDisplayed()
            .performTextReplacement("Updated Content")
        waitForIdle()

        // Save changes - using correct content description
        composeRule.onNodeWithContentDescription("Save")
            .assertExists()
            .assertIsDisplayed()
            .performClick()
        waitForIdle()

        // Verify updates
        composeRule.onNodeWithText("Updated Title")
            .assertExists()
            .assertIsDisplayed()
        composeRule.onNodeWithText("Original Title")
            .assertDoesNotExist()
    }

    private fun addNoteHelper(title: String, content: String) {
        // Click FAB and wait for navigation
        composeRule.onNodeWithContentDescription("Add Note")
            .assertExists()
            .assertIsDisplayed()
            .performClick()
        waitForIdle()

        // Enter title
        composeRule.onNodeWithTag("title_text_field")
            .assertExists()
            .assertIsDisplayed()
            .performTextReplacement(title)
        waitForIdle()

        // Enter content
        composeRule.onNodeWithTag("content_text_field")
            .assertExists()
            .assertIsDisplayed()
            .performTextReplacement(content)
        waitForIdle()

        // Save note and wait for navigation back
        composeRule.onNodeWithContentDescription("Save")  // Updated to match actual content description
            .assertExists()
            .assertIsDisplayed()
            .performClick()
        waitForIdle()
    }
}
