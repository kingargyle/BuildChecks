package unit

import core.entity.BuildConfigDefault
import core.buildstate.HandleBuildStartedUseCase
import core.post.PostStatusUseCase
import data.GithubDatasource
import org.amshove.kluent.*
import org.junit.Test

class HandleBuildStartedUseCaseTests {

    @Test
    fun `when the plugin is disabled, nothing is posted`() {
        val postBuildStatus : PostStatusUseCase = mock()
        val config = BuildConfigDefault()
        config.isPluginActivated = false
        val usecase = HandleBuildStartedUseCase(postBuildStatus, config)

        usecase.invoke()

        VerifyNotCalled on postBuildStatus that postBuildStatus.pending(any(), any()) was called
    }

    @Test
    fun `when the build is started and the plugin is activated, post a pending build status and delete report files`() {
        val postBuildStatus : PostStatusUseCase = mock()
        val config = BuildConfigDefault()
        config.isPluginActivated = true
        config.isPostActivated = true
        val usecase = HandleBuildStartedUseCase(postBuildStatus, config)
        When calling postBuildStatus.datasources itReturns listOf(GithubDatasource(mock(), "", ""))

        usecase.invoke()

        Verify on postBuildStatus that postBuildStatus.pending(any(), any()) was called
    }
}

