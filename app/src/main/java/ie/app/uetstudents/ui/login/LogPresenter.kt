package ie.app.uetstudents.ui.login

import ie.app.uetstudents.Repository.Repository
import ie.app.uetstudents.ui.Entity.Account.account
class LogPresenter (
    private val View : LogContract.View,
    private val repository: Repository
    ) : LogContract.Presenter{
    override fun getAccount() {
        repository.CallAccount(this)
    }

    override fun UpdateUI(account: account) {
        View.UpdateViewData(account)
    }
}