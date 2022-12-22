package customComponents

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.properties.Properties
import kotlinx.serialization.properties.decodeFromMap
import kotlinx.serialization.properties.encodeToMap

@ExperimentalSerializationApi
@Serializable
data class Item(
    val id: Int,
    val name: String,
    var isSelected: Boolean = true
) {
    val asMap: Map<String, Any> by lazy {
        Properties.encodeToMap(this)
    }

    companion object {
        fun from(map: Map<String, Any>): Item = Properties.decodeFromMap(map)
    }
}

object DataProvicder {
    val testItems = listOf(
        Item(1, "牛一"),
        Item(2, "牛二"),
        Item(3, "张三"),
        Item(4, "李四"),
        Item(5, "王五"),
        Item(6, "赵六"),
        Item(7, "鲁七"),
        Item(8, "诸葛baba"),
        Item(9, "欧阳lala"),
        Item(10, "易阳sasa"),
        Item(11, "完颜zaza"),
    )
}