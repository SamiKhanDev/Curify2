package com.saim.adapters

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.saim.MedicalStoreModule.MyCartViewModel
import com.saim.domain.entities.Drugs
import com.saim.curify.MyCart
import com.saim.domain.entities.MyCartData
import com.saim.curify.MyCartViewHolder
import com.saim.curify.R
import com.saim.curify.databinding.ItemAddedToCartBinding
import com.bumptech.glide.Glide

private var count = 1
lateinit var viewModel: MyCartViewModel
private var price = 0
private var addprice =0

class CartAdapter(val items: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       // if (viewType == 0) {
            val binding =
                ItemAddedToCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return MyCartViewHolder(binding)
        }

        //else if(viewType==1){
        //  val binding =
        //    ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        //return OrderViewHolder(binding)
        // }else{
        //   val binding =
        //     ItemBidBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        // return BidViewHolder(binding)
        //}

    companion object {
        var total=""

    }
  //  }


    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        if (items.get(position) is Drugs) return 0
     //   if (items.get(position) is Order) return 1
      //  if (items.get(position) is biditem) return 2
        return 3
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is MyCartViewHolder) {
            val mycart = items.get(position) as MyCartData
            holder.binding.title.text = mycart.title
           holder.binding.weight.text=mycart.weight
price =mycart.price.toIntOrNull()!!
            addprice =mycart.price.toIntOrNull()!!
            holder.binding.plus.setOnClickListener {
                if(count < mycart.quantity.toIntOrNull()!!) {
                    count++
                    price = price + addprice
                   // total= price.toString()
                    holder.binding.inputquantity.text = count.toString()
                    holder.binding.price.setText(price.toString())
                }
            }

            holder.binding.minus.setOnClickListener {
                if (count > 1) {
                    count--
                    price = price - addprice
                  //  total= price.toString()
                    holder.binding.inputquantity.text = count.toString()
                    holder.binding.price.setText(price.toString())
                }

            }
//            if (medicine.status == "Out of stock")
//                holder.binding.status.setTextColor(Color.RED)
//            else
          //      holder.binding.status.setTextColor(Color.GREEN)
            holder.binding.price.text = mycart.price.toString()

            Glide.with(holder.itemView.context)
                .load(mycart.image)
                .error(R.drawable.logo_curify)
                .placeholder(R.drawable.logo_curify)
                .into(holder.binding.productImage)
            holder.binding.productImage2.setOnClickListener {
                val context = holder.itemView.context
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Select Action")
                builder.setMessage("Do you want to delete this item?")




                builder.setNegativeButton("Delete") { dialog, _ ->
                    // Assuming you have a ViewModel in your Activity/Fragment
                    if (context is MyCart) {
                        val uid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""
                        context.viewModel.deletemycart(uid, mycart.id)
                    }
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
//
//        else if (holder is OrderViewHolder) {
//            val order = items.get(position) as Order
//            holder.binding.productTitle.text = order.item?.title
//
//            Glide.with(holder.itemView.context)
//                .load(order.image)
//                .error(R.drawable.paws)
//                .placeholder(R.drawable.paws)
//                .into(holder.binding.productImage)
//            holder.binding.productPrice.text=order.price
//            holder.binding.status.text =order.status
//
//            holder.itemView.setOnClickListener {
//                holder.itemView.context.startActivity(
//                    Intent(
//                        holder.itemView.context,
//                        OrderDetailsActivity::class.java
//                    ).putExtra("data", Gson().toJson(order))
//                )
//            }
//
//
//        }
//        else if (holder is BidViewHolder) {
//            val bid = items.get(position) as biditem
//
//            holder.binding.biddername.text=bid.username
//            holder.binding.bidderprice.text=bid.bidamount
//        }
//
//
//    }



