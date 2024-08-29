package com.example.leaderboard

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class MainActivity : AppCompatActivity(), OnItemClickListener {
    private lateinit var addName: EditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlayerAdapter
    private val players = Player.getPlayers()
    private val fileName = "playersList.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addName = findViewById(R.id.addName)
        readPlayersFile()
        recyclerView = findViewById(R.id.recyclerView)
        adapter = PlayerAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onItemClick(position: Int) {
        Log.d("Position", position.toString())
        try {
            if (position >= 0 && position < players.size) {
                val intent = Intent(this, PointsActivity::class.java).apply {
                    putExtra("playerPosition", position)
                }
                activityResultLauncher.launch(intent)
            } else {
                Toast.makeText(this, "Invalid player position", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Exception occurred: ${e.message}", e)
            Toast.makeText(this, "An error occurred while starting PointsActivity", Toast.LENGTH_SHORT).show()
        }
    }

    private fun writePlayersFile() {
        try {
            val jsonString = Json.encodeToString(players)
            val file = File(this.filesDir, fileName)
            file.writeText(jsonString)
        } catch (e: Exception) {
            Log.d("Error", e.message.toString())
        }
    }

    private fun readPlayersFile() {
        try {
            val file = File(this.filesDir, fileName)
            if (file.exists()) {
                val jsonString = file.readText()
                val playersFromFile: List<Player> = Json.decodeFromString(jsonString)
                players.clear()
                players.addAll(playersFromFile)
            } else {
                println("File does not exist.")
            }
        } catch (e: Exception) {
            Log.i("Error", e.message.toString())
        }
    }

    fun deleteFile(view: View){
        try {
            val file = File(filesDir, fileName)
            if (file.exists()) {
                file.delete()
                players.clear()
                readPlayersFile()
                adapter.notifyDataSetChanged()
            } else {
                Log.i("FileDeletion", "File does not exist.")
                false
            }
        } catch (e: Exception) {
            Log.e("FileDeletionError", e.message.toString())
        }
    }


    private val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            sortPlayersByScore()
            writePlayersFile()
        }
    }

    private fun sortPlayersByScore() {
        players.sortByDescending { it.score }
        adapter.notifyDataSetChanged()
    }

    fun addPlayers(view: View) {
        val name: String = addName.text.toString()
        if (name.isNotBlank() && !players.any { it.name == name }) {
            players.add(Player(name, 0))
            adapter.notifyItemInserted(players.size - 1)
            addName.text.clear()
            writePlayersFile()
        } else {
            Toast.makeText(this, "Player name cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }
}

