package com.example.chatgptapp

/**
 * @author : czl, ljx
 * @date : 2023/6/9
 */
class Msg {
    companion object {
        const val TYPE_RECEIVED = 0
        const val TYPE_SENT = 1
    }

    constructor(content: String?, type: Int) {
        this.content = content
        this.type = type
    }

    var content: String? = null
    var type:Int = TYPE_SENT;
}