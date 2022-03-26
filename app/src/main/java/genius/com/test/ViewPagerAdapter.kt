package genius.com.test

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


import genius.com.test.fragments.MyFragment


class ViewPagerAdapter(activity: MainActivity) : FragmentStateAdapter(activity) {

    private val  fragments = mutableListOf<MyFragment>()

    fun addFragment(fragment: MyFragment) {
        fragments.add(fragment)
        notifyItemInserted(fragments.lastIndex)
    }

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    fun removeLastFragment() {
        val lastIndex = fragments.lastIndex
        fragments.removeAt(lastIndex)
        notifyItemRemoved(lastIndex)
    }

}
