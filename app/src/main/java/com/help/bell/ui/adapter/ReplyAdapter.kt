import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.help.bell.databinding.ItemHashtagReplyBinding
import com.help.bell.model.ReplyData
import com.help.bell.ui.activity.ProfileDetailActivity

class ReplyAdapter(private val replyList: List<ReplyData>) :
    RecyclerView.Adapter<ReplyAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemHashtagReplyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ReplyData) {
            binding.textViewName.text = "${item.name} : "
            binding.textViewReply.text = item.reply
            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, ProfileDetailActivity::class.java)
                intent.putExtra("uid", item.uid)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHashtagReplyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val reply = replyList[position]
        holder.bind(reply)
    }

    override fun getItemCount(): Int {
        return replyList.size
    }
}
