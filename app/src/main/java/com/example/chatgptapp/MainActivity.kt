package com.example.chatgptapp

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatgptapp.adapter.ChatIndexAdapter
import com.example.chatgptapp.ui.dialog.SendDialog
import com.example.chatgptapp.utils.DensityUtil
import com.example.chatgptapp.utils.SqlOprate
import com.lwh.comm.utils.OkhttpUtil
import kotlinx.android.synthetic.main.activity_introduction.*
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class MainActivity : AppCompatActivity() {
    var sqlOprate: SqlOprate? = null
    var list: MutableList<Msg> = mutableListOf()

    var handler: Handler? = null
    var chatIndexAdapter: ChatIndexAdapter? = null

    var con_content: ConstraintLayout? = null

    private var bottomDialog: Dialog? = null
    private var ed_reply: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        init()
        ll_input.setOnClickListener { show(this) }
        bt_send.setOnClickListener { show(this) }
        iv_photo.setOnClickListener { startActivity(Intent(this,IntroductionActivity::class.java)) }
        iv_set.setOnClickListener { startActivity(Intent(this,SettingActivity::class.java)) }
    }

    private fun init() {
        con_content = findViewById(R.id.con_content)
        sqlOprate = SqlOprate(this)
        dataQuery()

        val sp = getSharedPreferences("openAiKey", Context.MODE_PRIVATE)
        // 从 SharedPreferences 文件中读取数据
        val key = sp.getString("key", "")
        OkhttpUtil.apiKey = key.toString()

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        chatIndexAdapter = ChatIndexAdapter(list)
        recyclerView.adapter = chatIndexAdapter
        if (chatIndexAdapter!!.itemCount > 0)
            recyclerView.smoothScrollToPosition(chatIndexAdapter!!.itemCount - 1)

        this.handler = Handler(Handler.Callback {
            chatIndexAdapter!!.notifyDataSetChanged()
            recyclerView.smoothScrollToPosition(chatIndexAdapter!!.itemCount - 1)
            false
        })

    }


    fun show(context: Context) {
//        bottomDialog = new Dialog(context, R.style.BottomDialog);
        val contentView = LayoutInflater.from(context).inflate(R.layout.dialog_item_reply, null)
        ed_reply = contentView.findViewById(R.id.ed_reply)
        val bt_reply = contentView!!.findViewById<Button>(R.id.bt_reply)
        bottomDialog = SendDialog(context, R.style.BottomDialog, ed_reply)
        bottomDialog!!.setContentView(contentView!!)
        val params = contentView!!.layoutParams as ViewGroup.MarginLayoutParams
        params.width = context.resources.displayMetrics.widthPixels - DensityUtil.dp2px(
            context,
            16f
        )
        params.bottomMargin = DensityUtil.dp2px(context, 8f)
        contentView!!.layoutParams = params

        bt_reply.setOnClickListener {

            if (ed_reply!!.text.isNotEmpty()) {
                val content = ed_reply!!.text.toString()
                val okhttpUtil = OkhttpUtil()
                dataInsert(Msg(content, Msg.TYPE_SENT))

                var reply: String? = ""
                for (s in list.reversed()) {
                    if (s.type == Msg.TYPE_RECEIVED) {
                        reply = s.content
                        break
                    }
                }

                if (list.size >= 3) {
                    okhttpUtil.firstContent = list.get(list.size - 3).content.toString()
                    okhttpUtil.newContent = content
                } else {
                    okhttpUtil.firstContent = content
                    okhttpUtil.newContent = ""
                }
                okhttpUtil.received = reply.toString()

                list.add(Msg("正在加载，请等待......", Msg.TYPE_RECEIVED))
                chatIndexAdapter!!.notifyDataSetChanged()
                recyclerView.smoothScrollToPosition(chatIndexAdapter!!.itemCount - 1)

                okhttpUtil.doPost(object : Callback {

                    override fun onFailure(call: Call, e: IOException) {
                        list.removeAt(list.size - 1)
                        dataInsert(Msg("AI回复失败，请重试......", Msg.TYPE_RECEIVED))

                        handler!!.sendEmptyMessage(0)
                    }

                    override fun onResponse(call: Call, response: Response) {
                        var str = response.body?.string()

                        str = str?.replace(Regex("^(\n)+"), "")
                            ?.replace("```", "")
                            ?.replace(Regex("^(\\{AI\\}\n)+"), "")
                            ?.replace("{AI}", "")
                            ?.replace("{/AI}", "")
                        list.removeAt(list.size - 1)
                        dataInsert(Msg(str, Msg.TYPE_RECEIVED))
                        Log.i("ResponeContent", str.toString())

                        handler!!.sendEmptyMessage(0)
                    }
                })
            }

            Log.i("dialog", "关闭输入法")
            val inputMgr = context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMgr.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0)
            bottomDialog!!.dismiss()
        }


        bottomDialog!!.setCanceledOnTouchOutside(true)
        bottomDialog!!.getWindow()!!.setGravity(Gravity.BOTTOM)
        bottomDialog!!.getWindow()!!.setWindowAnimations(R.style.BottomDialog_Animation)
        bottomDialog!!.show()
    }


//    fun chatSend() {
//        if (con_content!!.text.isNotEmpty()) {
//            val content = con_content!!.text.toString()
//            val okhttpUtil = OkhttpUtil()
//            dataInsert(Msg(content, Msg.TYPE_SENT))
//
//            var reply: String? = ""
//            for (s in list.reversed()) {
//                if (s.type == Msg.TYPE_RECEIVED) {
//                    reply = s.content
//                    break
//                }
//            }
//
//            if (list.size >= 3) {
//                okhttpUtil.firstContent = list.get(list.size - 3).content.toString()
//                okhttpUtil.newContent = content
//            } else {
//                okhttpUtil.firstContent = content
//                okhttpUtil.newContent = ""
//            }
//            okhttpUtil.received = reply.toString()
//
//            list.add(Msg("正在加载，请等待......", Msg.TYPE_RECEIVED))
//            chatIndexAdapter!!.notifyDataSetChanged()
//            recyclerView.smoothScrollToPosition(chatIndexAdapter!!.itemCount - 1)
//
//            okhttpUtil.doPost(object : Callback {
//
//                override fun onFailure(call: Call, e: IOException) {
//                    list.removeAt(list.size - 1)
//                    dataInsert(Msg("AI回复失败，请重试......", Msg.TYPE_RECEIVED))
//
//                    handler!!.sendEmptyMessage(0)
//                }
//
//                override fun onResponse(call: Call, response: Response) {
//                    var str = response.body?.string()
//
//                    str = str?.replace(Regex("^(\n)+"), "")
//                        ?.replace("```", "")
//                        ?.replace(Regex("^(\\{AI\\}\n)+"), "")
//                        ?.replace("{AI}", "")
//                        ?.replace("{/AI}", "")
//                    list.removeAt(list.size - 1)
//                    dataInsert(Msg(str, Msg.TYPE_RECEIVED))
//                    Log.i("ResponeContent", str.toString())
//
//                    handler!!.sendEmptyMessage(0)
//                }
//            })
//        }
//    }

    public fun dataInsert(msg: Msg) {
        list.add(msg)
        sqlOprate!!.insert(msg.type, msg.content.toString())
    }

    public fun dataQuery() {
        var cursor = sqlOprate!!.query()
        if (cursor!!.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val type = cursor.getInt(cursor.getColumnIndex("type"))
                val content = cursor.getString(cursor.getColumnIndex("content"))
                list.add(Msg(content, type))
            } while (cursor.moveToNext())
        }
        cursor.close()
    }
}