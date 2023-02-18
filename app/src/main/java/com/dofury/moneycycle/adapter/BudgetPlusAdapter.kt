package com.dofury.moneycycle.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dofury.moneycycle.ListViewHolder
import com.dofury.moneycycle.R
import com.dofury.moneycycle.activity.BudgetPlusActivity
import com.dofury.moneycycle.databinding.ListItemBinding
import com.dofury.moneycycle.dialog.BudgetPlusDialog
import com.dofury.moneycycle.dto.MoneyLog
import com.dofury.moneycycle.util.DataUtil
import java.text.SimpleDateFormat

class BudgetPlusAdapter(val activity: BudgetPlusActivity,val list: MutableList<MoneyLog>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = ListViewHolder(
        ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as ListViewHolder).biding//뷰에 데이터 출력
        binding.itemMoney.setTextColor(ContextCompat.getColor(binding.root.context, R.color.blue))

        binding.itemMoney.text = DataUtil().parseMoney(list[position].charge)
        binding.itemDate.text = parseDate(list[position].date)
        binding.itemType.text = list[position].category
        parseCategoryImage(binding,position)
        buttonEvent(holder,position)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun buttonEvent(holder: ListViewHolder, position: Int){
        holder.itemView.setOnClickListener(View.OnClickListener {
            val dialog = BudgetPlusDialog(activity)
            dialog.show(list,this,position)
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun parseDate(date: String): String {
        val beforeDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date)
        val formatter = SimpleDateFormat("yyyy년 MM월 dd일 a hh:mm")
        return formatter.format(beforeDate).replace("PM", "오후").replace("AM", "오전")
    }

    private fun parseCategoryImage(binding: ListItemBinding,position: Int){
        when(list[position].category){

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
}