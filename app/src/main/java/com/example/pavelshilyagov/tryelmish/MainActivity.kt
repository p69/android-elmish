package com.example.pavelshilyagov.tryelmish

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.pavelshilyagov.tryelmish.elmish.mkProgram
import com.example.pavelshilyagov.tryelmish.elmish.withAnvil
import com.example.pavelshilyagov.tryelmish.main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        run(this)
    }

    private fun run(activity: Activity) {
        mkProgram<Unit, MainModel, Msg, Unit>(
                init = { Pair(init(), emptyList()) },
                update = ::update,
                view = ::view)
        .let { withAnvil(it, findViewById(R.id.content), activity) }
        .let { com.example.pavelshilyagov.tryelmish.elmish.run(it) }
    }
}

