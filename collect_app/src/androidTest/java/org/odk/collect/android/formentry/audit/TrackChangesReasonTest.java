package org.odk.collect.android.formentry.audit;

import android.Manifest;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.runner.RunWith;
import org.odk.collect.android.activities.MainMenuActivity;
import org.odk.collect.android.espressoutils.pages.MainMenuPage;
import org.odk.collect.android.support.CopyFormRule;
import org.odk.collect.android.support.ResetStateRule;

@RunWith(AndroidJUnit4.class)
public class TrackChangesReasonTest {

    private static final String TRACK_CHANGES_REASON_ON_EDIT_FORM = "track-changes-reason-on-edit.xml";
    private static final String NO_TRACK_CHANGES_REASON_FORM = "no-track-changes-reason.xml";

    @Rule
    public ActivityTestRule<MainMenuActivity> rule = new ActivityTestRule<>(MainMenuActivity.class);

    @Rule
    public RuleChain copyFormChain = RuleChain
            .outerRule(GrantPermissionRule.grant(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            ))
            .around(new ResetStateRule())
            .around(new CopyFormRule(TRACK_CHANGES_REASON_ON_EDIT_FORM))
            .around(new CopyFormRule(NO_TRACK_CHANGES_REASON_FORM));

    @Test
    public void openingAFormToEdit_andChangingAValue_andClickingSaveAndExit_promptsForReason() {
        new MainMenuPage(rule)
                .startBlankForm("Track Changes Reason")
                .inputText("Nothing much...")
                .swipeToNextQuestion()
                .clickSaveAndExit()
                .clickEditSavedForm()
                .clickOnForm("Track Changes Reason")
                .clickGoToStart()
                .inputText("Nothing much!")
                .swipeToNextQuestion()
                .clickSaveAndExitWithChangesReasonPrompt();
    }

    @Test
    public void whenFormDoesNotHaveTrackChangesReason_openingToEdit_andChangingAValue_andClickingSaveAndExit_returnsToMainMenu() {
        new MainMenuPage(rule)
                .startBlankForm("Normal Form")
                .inputText("Nothing much...")
                .swipeToNextQuestion()
                .clickSaveAndExit()
                .clickEditSavedForm()
                .clickOnForm("Normal Form")
                .clickGoToStart()
                .inputText("Nothing much!")
                .swipeToNextQuestion()
                .clickSaveAndExit();
    }
}
