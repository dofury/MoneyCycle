package com.dofury.moneycycle.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dofury.moneycycle.ListViewHolder
import com.dofury.moneycycle.R
import com.dofury.moneycycle.databinding.ListItemBinding
import com.dofury.moneycycle.dialog.LogPageDialog
import com.dofury.moneycycle.util.DataUtil
import com.dofury.moneycycle.viewmodel.SearchResultViewModel
import java.text.SimpleDateFormat

class SearchResultAdapter(private val context: Context, private val viewModel: SearchResultViewModel
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), LogPageDialog.LogPageDialogListener {
    private var moneyLogList = viewModel.moneyLogList.value
    override fun getItemCount(): Int {
        return moneyLogList!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = ListViewHolder(
        ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as ListViewHolder).biding//뷰에 데이터 출력
        if(moneyLogList?.get(position)!!.sign){//지출,수입 검사하여 색칠
            binding.itemMoney.setTextColor(ContextCompat.getColor(binding.root.context,
                R.color.blue
            ))
            binding.itemSign.setTextColor(ContextCompat.getColor(binding.root.context, R.color.blue))
        }else{
            binding.itemMoney.setTextColor(ContextCompat.getColor(binding.root.context, R.color.red))
            binding.itemSign.setTextColor(ContextCompat.getColor(binding.root.context, R.color.red))
        }
        binding.itemMoney.text = DataUtil.parseMoney(moneyLogList!![position].charge)
        binding.itemDate.text = parseDate(moneyLogList!![position].date)
        binding.itemType.text = moneyLogList!![position].category
        parseCategoryImage(binding,position)
        buttonEvent(holder,position)
    }
    private fun buttonEvent(holder: ListViewHolder, position: Int){
        holder.itemView.setOnClickListener{
            val dialog = LogPageDialog(context,viewModel.moneyLogList.value!!,this)
            dialog.show(position)
        }
    }

    private fun parseDate(date: String): String {
        val beforeDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date)
        val formatter = SimpleDateFormat("yyyy년 MM월 dd일 a hh:mm")
        return formatter.format(beforeDate).replace("PM", "오후").replace("AM", "오전")
    }

    private fun parseCategoryImage(binding: ListItemBinding,position: Int){
        val moneyLogList = viewModel.moneyLogList.value
        when(moneyLogList?.get(position)!!.category){

            binding.root.context.getString(R.string.house) ->
                binding.civCategory.setImageResource(R.drawable.house_icon)
            binding.root.context.getString(R.string.hobby) ->
                binding.civCategory.setImageResource(R.drawable.hobby_icon)
            binding.root.context.getString(R.string.change) ->
                binding.civCategory.setImageResource(R.drawable.change_icon)
            binding.root.context.getString(R.string.etc) ->
                binding.civCategory.setImageResource(R.drawable.etc_icon)
            binding.root.context.getString(R.string.event) ->
                binding.civCategory.setImageResource(R.drawable.event_icon)
            binding.root.context.getString(R.string.food) ->
                binding.civCategory.setImageResource(R.drawable.food_icon)
            binding.root.context.getString(R.string.pocket_money) ->
                binding.civCategory.setImageResource(R.drawable.pocket_money_icon)
            binding.root.context.getString(R.string.traffic) ->
                binding.civCategory.setImageResource(R.drawable.traffic_icon)
            binding.root.context.getString(R.string.culture) ->
                binding.civCategory.setImageResource(R.drawable.culture_icon)
            binding.root.context.getString(R.string.fashion) ->
                binding.civCategory.setImageResource(R.drawable.fashion_icon)
            binding.root.context.getString(R.string.income) ->
                binding.civCategory.setImageResource(R.drawable.income_icon)
            binding.root.context.getString(R.string.extra_income) ->
                binding.civCategory.setImageResource(R.drawable.extra_income_icon)
            binding.root.context.getString(R.string.finance) ->
                binding.civCategory.setImageResource(R.drawable.finance_icon)
            binding.root.context.getString(R.string.refund) ->
                binding.civCategory.setImageResource(R.drawable.refund_icon)
        }
    }

    fun updateLog(){
        moneyLogList = viewModel.moneyLogList.value
        notifyDataSetChanged()
    }

    override fun onLogUpdated(position: Int) {
        viewModel.moneyLogListUpdate(moneyLogList!![position])
        this.notifyItemChanged(position)
    }

    override fun onLogDeleted(position: Int) {
        viewModel.moneyLogListDelete(moneyLogList!![position])
        this.notifyItemRemoved(position)
    }
}