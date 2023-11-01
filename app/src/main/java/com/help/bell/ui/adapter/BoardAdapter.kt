import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.help.bell.databinding.ItemBoardBinding
import com.help.bell.databinding.ItemHashtagBinding
import com.help.bell.model.BoardData
import com.help.bell.model.HashtagData

class BoardAdapter(private val boardList: List<BoardData>) :
    RecyclerView.Adapter<BoardAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemBoardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BoardData) {
            binding.textViewName.text = item.name
            binding.textViewEtc.text = item.board
            binding.textViewPerson.text = item.username
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBoardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val board = boardList[position]
        holder.bind(board)
    }

    override fun getItemCount(): Int {
        return boardList.size
    }
}
