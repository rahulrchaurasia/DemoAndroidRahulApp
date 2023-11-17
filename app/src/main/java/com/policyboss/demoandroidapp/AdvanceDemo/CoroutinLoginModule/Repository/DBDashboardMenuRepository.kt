package com.policyboss.demoandroidapp.LoginModule.Repository

import android.view.Menu
import androidx.lifecycle.LiveData
import com.policyboss.demoandroidapp.APIState
import com.policyboss.demoandroidapp.LoginModule.DataModel.ResponseEntity.LoginResponse
import com.policyboss.demoandroidapp.LoginModule.DataModel.model.DashboardMenu
import com.policyboss.demoandroidapp.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class DBDashboardMenuRepository() {



    companion object{

        fun getDashBoardMenu() : List<DashboardMenu>{

            /*********************************************************************************
            //val menuList1: MutableList<DashboardMenu> = ArrayList()   // both are same
            //val mutableList = mutableListOf<DashboardMenu>()
            ***********************************************************************************/
            val menuList: MutableList<DashboardMenu> = mutableListOf()

            menuList.add(DashboardMenu(id = "1","Home Page", description = "List of multiple example", image = R.drawable.document_menu , isCheck = false))

             menuList.add(DashboardMenu(id = "2","WebView Demo",description ="Android To web Communication And Web To Android Both",image = R.drawable.ic_call_24 ,false))
            menuList.add(DashboardMenu(id = "3","WebView Using Kotlin",description ="Open Camera and send output as bitmap to web",image = R.drawable.ic_email_24, isCheck = false))
            menuList.add(DashboardMenu(id = "4","Activity Launcher",description ="Activity Launcher replace OnActivityResultset() method", image = R.drawable.ic_email_24, false))
            menuList.add(DashboardMenu(id = "5","Multiple Permission",description ="Camera and Storage Permission", image = R.drawable.ic_email_24, false))

            menuList.add(DashboardMenu(id = "6","Open Another App",description ="Open  Another App using existing app.",image = R.drawable.disclosure1 , isCheck = false))


            menuList.add(DashboardMenu(id = "6","Title",description ="Description",image = R.drawable.ic_email_24, isCheck = false))
            menuList.add(DashboardMenu(id = "7","Title",description ="Description",image = R.drawable.ic_email_24, isCheck = false))

            menuList.add(DashboardMenu(id = "8","Title",description ="Description",image = R.drawable.ic_email_24, isCheck = false))
            menuList.add(DashboardMenu(id = "9","demo",description ="Description", image =R.drawable.ic_email_24, isCheck = false))


            return menuList.toList()
        }
    }
}