package com.example.pavelshilyagov.tryelmish

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.pavelshilyagov.tryelmish.elmish.CmdF
import com.example.pavelshilyagov.tryelmish.elmish.mkProgram
import com.example.pavelshilyagov.tryelmish.elmish.run
import com.example.pavelshilyagov.tryelmish.elmish.withAnvil
import com.example.pavelshilyagov.tryelmish.main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // probably need some SystemMsg mechanism for updating actionbar
        supportActionBar?.title = "Hello"

        start(this)
    }

    private fun start(activity: Activity) {
        mkProgram<Unit, MainModel, Msg, Unit>(
                init = { Pair(init(), CmdF.none()) },
                update = ::update,
                view = ::view)
        .withAnvil(findViewById(R.id.content), activity)
        .run()
    }
}

