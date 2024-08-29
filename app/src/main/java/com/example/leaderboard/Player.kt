package com.example.leaderboard

import kotlinx.serialization.Serializable

@Serializable
data class Player(val name: String, var score: Int) {
    companion object {
        private val players: ArrayList<Player> = ArrayList()

        fun getPlayers(): ArrayList<Player> {
            return players
        }

        fun getPlayer(index: Int): Player {
            return players[index]
        }
    }

    fun add(increaseScore: Int) {
        score += increaseScore
    }
}

