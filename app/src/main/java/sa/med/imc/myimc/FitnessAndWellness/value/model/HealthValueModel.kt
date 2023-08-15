package sa.med.imc.myimc.FitnessAndWellness.value.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.Parceler

@Parcelize
data class HealthValueModel(val title: String,val field:Field,val dataType: DataType ,val postfix:String,var value : String = "" ):
    Parcelable{
    var length : Int = 10
    var hint:String = ""
        constructor( title: String, field:Field, dataType: DataType , postfix:String, value : String = "", length : Int = 10)
    :this(title, field, dataType, postfix, value){
        this.length = length
    }
    constructor( title: String, field:Field, dataType: DataType , postfix:String, value : String = "", length : Int = 10,hint:String)
            :this(title, field, dataType, postfix, value){
        this.length = length
        this.hint = hint
    }
    private companion object : Parceler<HealthValueModel> {
        override fun HealthValueModel.write(parcel: Parcel, flags: Int) {
            parcel.writeString(title)
            parcel.writeString(field.toString())
            parcel.writeString(dataType.toString())
            parcel.writeString(postfix)
            parcel.writeString(value)
            parcel.writeInt(length)
            parcel.writeString(hint)
        }

        override fun create(parcel: Parcel): HealthValueModel =
            HealthValueModel(
                parcel.readString() ?: "",
                Field.CREATOR.createFromParcel(parcel),
                DataType.CREATOR.createFromParcel(parcel),
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readInt(),
                parcel.readString() ?: ""
            )
    }
}