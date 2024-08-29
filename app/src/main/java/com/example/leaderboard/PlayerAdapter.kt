package com.example.leaderboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlayerAdapter(
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder>() {

    class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.playerName)
        val scoreTextView: TextView = itemView.findViewById(R.id.playerScore)
        val rank: ImageView = itemView.findViewById(R.id.rank)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return PlayerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val player = Player.getPlayer(position)

        holder.nameTextView.text = player.name
        holder.scoreTextView.text = player.score.toString()
        holder.rank.setImageResource(
            when (player.score) {
                in 0..49 -> R.drawable.rank1
                in 50..99 -> R.drawable.rank2
                else -> R.drawable.rank3
            }
        )

        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return Player.getPlayers().size
    }
}
