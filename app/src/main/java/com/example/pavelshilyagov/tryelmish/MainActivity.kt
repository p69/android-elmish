package com.example.pavelshilyagov.tryelmish

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.pavelshilyagov.tryelmish.Elmish.mkProgram
import com.example.pavelshilyagov.tryelmish.Elmish.withAnvil
import com.example.pavelshilyagov.tryelmish.pure.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        run(this)
    }

    fun run(activity: Activity) {
        mkProgram<Unit, Model, Msg, Unit>(
                init = { init() },
                update = { msg, model -> Pair(update(msg, model), emptyList()) },
                view = ::view)
        .let { withAnvil(it, findViewById(R.id.content), activity) }
        .let { com.example.pavelshilyagov.tryelmish.Elmish.run(it) }
    }
}

