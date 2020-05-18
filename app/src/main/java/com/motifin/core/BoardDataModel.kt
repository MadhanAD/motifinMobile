package com.motifin.core

data class BoardComponentData(
    var data: Any? = null,
    var type: BoardComponentType = BoardComponentType.NONE
)

enum class BoardComponentType {
    IMAGE, TEXT, NONE
}

data class BoardImageComponent(
    var x: Int = 0,
    var y: Int = 0,
    var width: Int = 0,
    var height: Int = 0,
    var imageUrl: String = ""
)

data class BoardTextComponent(
    var x: Int = 0,
    var y: Int = 0,
    var width: Int = 0,
    var height: Int = 0,
    var text: String = "",
    var fontSize: Int = 16,
    var fontColor: String = "#000"
)