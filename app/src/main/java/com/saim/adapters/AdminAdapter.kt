import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.saim.AdminModule.AdminMedicineViewHolder
import com.saim.curify.databinding.ItemMedicineAdminBinding
import com.saim.domain.entities.Drugs
import com.saim.curify.R

class AdminAdapter(
    val items: List<Any>,
    private val onDeleteClick: (Drugs) -> Unit,
    private val onUpdateClick: (Drugs) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemMedicineAdminBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AdminMedicineViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return if (items[position] is Drugs) 0 else 3
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is AdminMedicineViewHolder) {
            val medicine = items[position] as Drugs

            holder.binding.title.text = medicine.title
            holder.binding.weight.text = medicine.weight
            holder.binding.status.text = medicine.status
            holder.binding.price.text = medicine.price.toString()

            Glide.with(holder.itemView.context)
                .load(medicine.image)
                .error(R.drawable.logo_curify)
                .placeholder(R.drawable.logo_curify)
                .into(holder.binding.productImage)

            holder.itemView.setOnLongClickListener {
                val context = holder.itemView.context

                val builder = AlertDialog.Builder(context)
                builder.setTitle("Select Action")
                builder.setMessage("Do you want to update or delete this medicine?")

                builder.setPositiveButton("Update") { dialog, _ ->
                    onUpdateClick(medicine)
                    dialog.dismiss()
                }

                builder.setNegativeButton("Delete") { dialog, _ ->
                    onDeleteClick(medicine)
                    dialog.dismiss()
                }

                builder.setNeutralButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }

                builder.show()
                true
            }
        }
    }
}
