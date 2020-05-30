package com.motifin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.motifin.core.PreviewActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
    }

    fun navToDragActivity(view: View) {
        val intent = Intent(this@HomeActivity, DragActivity::class.java)
        startActivity(intent)
    }

    fun navToResizeActivity(view: View) {
        val intent = Intent(this@HomeActivity, ResizeActivity::class.java)
        startActivity(intent)
    }

    fun navToPreviewActivity(view: View) {
        startActivity(Intent(this@HomeActivity, PreviewActivity::class.java))
    }
}
