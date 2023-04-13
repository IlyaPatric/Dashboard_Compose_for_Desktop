package test

import data.remote.services.sign.SignService
import org.junit.Assert
import org.junit.Test

import screens.logIn.SignInViewModel

class SignInViewModelTest {

    private var viewModel = SignInViewModel(SignService.create())

    @Test
    fun `checkForRightValidTest1`() {
        val res = viewModel.checkFieldsForValid("login", "password")
        Assert.assertTrue(res)
    }

    @Test
    fun `checkForRightValidTest2`() {
        val res = viewModel.checkFieldsForValid("lgn", "pwd")
        Assert.assertFalse(res)
    }

    @Test
    fun `checkForRightValidTest3`() {
        val res = viewModel.checkFieldsForValid("myLogin", "qwerty")
        Assert.assertTrue(res)
    }



}


