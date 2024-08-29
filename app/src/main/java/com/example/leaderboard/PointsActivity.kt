package com.example.leaderboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PointsActivity : AppCompatActivity() {
    private lateinit var playerName: TextView
    private lateinit var playerScore: TextView
    private lateinit var rank: ImageView
    private var position = -1
    private lateinit var player: Player

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_points)

        initializeUIComponents()

        position = intent.getIntExtra("playerPosition", -1)
        if (position == -1) {
            showErrorAndFinish("Invalid player position.")
            return
        }

        player = Player.getPlayer(position)
        updateUI()
    }

    private fun initializeUIComponents() {
        playerName = findViewById(R.id.playerName)
        playerScore = findViewById(R.id.playerScore)
        rank = findViewById(R.id.rank)
    }

    private fun updateUI() {
        playerName.text = player.name
        playerScore.text = player.score.toString()
        rank.setImageResource(getRankImageResource(player.score))
    }

    private fun getRankImageResource(score: Int): Int {
        return when (score) {
            in 0..49 -> R.drawable.rank1
            in 50..99 -> R.drawable.rank2
            else -> R.drawable.rank3
        }
    }

    private fun showErrorAndFinish(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun updatePlayerScore(scoreIncrement: Int) {
        player.add(scoreIncrement)
        updateUI()
    }

    fun onePoint(view: View) {
        updatePlayerScore(1)
    }

    fun tenPoints(view: View) {
        updatePlayerScore(10)
    }

    fun backToMain(view: View) {
        setResult(Activity.RESULT_OK)
        finish()
    }
}
