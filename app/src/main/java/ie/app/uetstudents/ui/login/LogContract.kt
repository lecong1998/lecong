package ie.app.uetstudents.ui.login

import ie.app.uetstudents.ui.Entity.Account.account

interface LogContract {
    interface View{
        fun UpdateViewData(account: account)
    }
    interface Presenter{
        fun getAccount()
        fun UpdateUI(account: account)
    }
}