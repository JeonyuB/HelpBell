import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.help.bell.databinding.ItemHashtagBinding
import com.help.bell.model.HashtagData
import com.help.bell.ui.activity.HashtagDetailActivity

class HashtagAdapter(private val hashtagList: List<HashtagData>) :
    RecyclerView.Adapter<HashtagAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemHashtagBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HashtagData) {
            binding.textViewName.text = item.name
            binding.textViewEtc.text = item.hashtags
            binding.textViewLoc.text = item.location
            binding.textViewTime.text = item.time

            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, HashtagDetailActivity::class.java)
                intent.putExtra("data", item)
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHashtagBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hashtag = hashtagList[position]
        holder.bind(hashtag)
    }

    override fun getItemCount(): Int {
        return hashtagList.size
    }
}
