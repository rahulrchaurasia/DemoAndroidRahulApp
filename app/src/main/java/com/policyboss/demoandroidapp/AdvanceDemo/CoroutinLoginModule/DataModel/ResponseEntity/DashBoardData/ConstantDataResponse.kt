import com.policyboss.demoandroidapp.MVVMDemo.Data.DashboardData.ConstantEntity

data class ConstantDataResponse(
    val MasterData: ConstantEntity,
    val Message: String,
    val Status: String,
    val StatusNo: Int
)